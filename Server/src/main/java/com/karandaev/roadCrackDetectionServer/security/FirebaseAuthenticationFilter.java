package com.karandaev.roadCrackDetectionServer.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Servlet filter that authenticates requests using a Firebase ID token from the Authorization
 * header.
 *
 * <p>The filter expects the token in the following format:
 *
 * <pre>
 * Authorization: Bearer &lt;firebase-id-token&gt;
 * </pre>
 *
 * <p>If the header is missing, the request is passed further down the filter chain and access
 * control is delegated to the Spring Security configuration. If the token is present but invalid,
 * the filter clears the security context and returns HTTP 401.
 */
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  /**
   * Creates a Firebase authentication filter.
   *
   * @param authenticationManager authentication manager used to validate Firebase tokens
   */
  public FirebaseAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  /**
   * Extracts a Firebase Bearer token from the request, authenticates it, and stores the
   * authenticated principal in the Spring Security context.
   *
   * @param request current HTTP request
   * @param response current HTTP response
   * @param filterChain remaining servlet filter chain
   * @throws ServletException if the downstream filter chain fails
   * @throws IOException if request or response processing fails
   */
  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    // If there is no Bearer token, continue and let SecurityConfig decide access.
    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String idToken = header.substring("Bearer ".length()).trim();
    try {
      Authentication authRequest = new FirebaseAuthenticationToken(idToken);
      Authentication authResult = authenticationManager.authenticate(authRequest);
      SecurityContextHolder.getContext().setAuthentication(authResult);
    } catch (Exception ex) {
      // If the token is invalid, clear authentication state and return 401 immediately.
      SecurityContextHolder.clearContext();
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response
          .getWriter()
          .write(
              """
                  {"error":"unauthorized","message":"Invalid or expired token"}
                  """);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
