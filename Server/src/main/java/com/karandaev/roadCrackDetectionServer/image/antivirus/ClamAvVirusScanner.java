package com.karandaev.roadCrackDetectionServer.image.antivirus;

import com.karandaev.roadCrackDetectionServer.image.exception.ImageValidationException;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Virus scanner implementation that sends image bytes to a ClamAV {@code clamd} daemon.
 *
 * <p>The scanner uses the ClamAV {@code INSTREAM} protocol to stream file bytes over TCP. If ClamAV
 * reports a match, an {@link ImageValidationException} is thrown.
 */
public class ClamAvVirusScanner implements VirusScanner {

  private final String host;
  private final int port;
  private final int timeoutMs;

  /**
   * Creates a ClamAV virus scanner client.
   *
   * @param host ClamAV daemon host
   * @param port ClamAV daemon TCP port
   * @param timeoutMs socket connect and read timeout in milliseconds
   */
  public ClamAvVirusScanner(String host, int port, int timeoutMs) {
    this.host = host;
    this.port = port;
    this.timeoutMs = timeoutMs;
  }

  /**
   * Scans the provided bytes with ClamAV and throws if the content is malicious or the scanner is
   * unavailable.
   *
   * @param bytes file bytes to scan
   * @throws ImageValidationException if malicious content is detected or ClamAV returns an error
   */
  @Override
  public void scanOrThrow(byte[] bytes) {
    // ClamAV INSTREAM protocol:
    // command "zINSTREAM\0", then repeated chunks as [4-byte length][data],
    // then a final zero-length chunk, followed by a single response line.
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress(host, port), timeoutMs);
      socket.setSoTimeout(timeoutMs);

      OutputStream out = socket.getOutputStream();
      InputStream in = socket.getInputStream();

      out.write("zINSTREAM\0".getBytes(StandardCharsets.US_ASCII));

      int offset = 0;
      int chunkSize = 8192;
      while (offset < bytes.length) {
        int len = Math.min(chunkSize, bytes.length - offset);
        out.write(ByteBuffer.allocate(4).putInt(len).array());
        out.write(bytes, offset, len);
        offset += len;
      }

      // A zero-length chunk marks the end of the streamed file.
      out.write(ByteBuffer.allocate(4).putInt(0).array());
      out.flush();

      String resp = readLine(in);

      // Example responses:
      // "stream: OK"
      // "stream: Eicar-Test-Signature FOUND"
      if (resp == null) {
        throw new ImageValidationException(HttpStatus.BAD_GATEWAY, "Antivirus no response");
      }
      if (resp.contains("FOUND")) {
        throw new ImageValidationException(
            HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Malicious content detected");
      }
      if (!resp.contains("OK")) {
        throw new ImageValidationException(HttpStatus.BAD_GATEWAY, "Antivirus error: " + resp);
      }
    } catch (IOException e) {
      throw new ImageValidationException(HttpStatus.BAD_GATEWAY, "Antivirus unavailable");
    }
  }

  /**
   * Reads a single line from the ClamAV response stream.
   *
   * @param in ClamAV response input stream
   * @return response line, or {@code null} if the stream ends before any data is received
   * @throws IOException if reading from the stream fails
   */
  private String readLine(InputStream in) throws IOException {
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    int b;
    while ((b = in.read()) != -1) {
      if (b == '\n') break;
      buf.write(b);
    }
    if (buf.size() == 0 && b == -1) return null;
    return buf.toString(StandardCharsets.UTF_8);
  }
}
