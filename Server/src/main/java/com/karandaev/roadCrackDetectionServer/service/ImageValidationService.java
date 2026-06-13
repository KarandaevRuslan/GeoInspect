package com.karandaev.roadCrackDetectionServer.service;

import com.karandaev.roadCrackDetectionServer.image.validation.ImageByteValidator;
import com.karandaev.roadCrackDetectionServer.image.decode.ImageDecoder;
import com.karandaev.roadCrackDetectionServer.image.decode.ImageDimensionProbe;
import com.karandaev.roadCrackDetectionServer.image.format.ImageFormatValidator;
import com.karandaev.roadCrackDetectionServer.image.normalize.ImageNormalizer;
import com.karandaev.roadCrackDetectionServer.image.config.ImageSecurityProperties;
import com.karandaev.roadCrackDetectionServer.image.format.MagicBytesSniffer;
import com.karandaev.roadCrackDetectionServer.image.model.SafeImage;
import com.karandaev.roadCrackDetectionServer.image.antivirus.VirusScanner;
import org.springframework.stereotype.Service;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 * Validates uploaded image bytes and normalizes accepted images into a safe output format.
 *
 * <p>This service coordinates the validation pipeline:
 *
 * <ol>
 *   <li>Validate raw byte size.
 *   <li>Run antivirus scanning.
 *   <li>Detect image format by magic bytes.
 *   <li>Validate that the detected format is allowed.
 *   <li>Read dimensions without full pixel decoding.
 *   <li>Validate image dimensions and megapixels.
 *   <li>Decode the full image.
 *   <li>Normalize the image into the configured output format.
 * </ol>
 *
 * <p>The service does not trust user-provided file names, extensions, or content types.
 */
@Service
public class ImageValidationService {

  private final ImageSecurityProperties props;
  private final VirusScanner virusScanner;
  private final MagicBytesSniffer sniffer;
  private final ImageByteValidator byteValidator;
  private final ImageFormatValidator formatValidator;
  private final ImageDimensionProbe dimensionProbe;
  private final ImageDecoder decoder;
  private final ImageNormalizer normalizer;

  /**
   * Creates an image validation service.
   *
   * @param props image security and validation properties
   * @param virusScanner scanner used to check uploaded image bytes
   */
  public ImageValidationService(ImageSecurityProperties props, VirusScanner virusScanner) {
    this.props = props;
    this.virusScanner = virusScanner;
    this.sniffer = new MagicBytesSniffer();
    this.byteValidator = new ImageByteValidator(props);
    this.formatValidator = new ImageFormatValidator(props);
    this.dimensionProbe = new ImageDimensionProbe(props);
    this.decoder = new ImageDecoder();
    this.normalizer = new ImageNormalizer();
  }

  /**
   * Validates raw image bytes and returns a normalized safe image.
   *
   * @param bytes raw uploaded image bytes
   * @return normalized image data with dimensions and output format
   */
  public SafeImage validateAndNormalize(byte[] bytes) {
    byteValidator.validateBytes(bytes);

    virusScanner.scanOrThrow(bytes);

    MagicBytesSniffer.Format format =
        sniffer.sniff(bytes).orElseThrow(formatValidator::unknownFormat);

    formatValidator.validateAllowedFormat(format);

    Dimension dimensions = dimensionProbe.probeDimensions(bytes);
    dimensionProbe.validateDimensions(dimensions);

    BufferedImage image = decoder.decode(bytes);

    String outputFormat = props.getNormalizeFormat().toUpperCase(Locale.ROOT);
    formatValidator.validateNormalizationFormat(outputFormat);

    byte[] normalizedBytes = normalizer.normalize(image, outputFormat);

    return new SafeImage(normalizedBytes, image.getWidth(), image.getHeight(), outputFormat);
  }
}
