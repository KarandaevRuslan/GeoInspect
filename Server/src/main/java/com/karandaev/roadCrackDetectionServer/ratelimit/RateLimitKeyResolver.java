package com.karandaev.roadCrackDetectionServer.ratelimit;

import com.karandaev.roadCrackDetectionServer.security.FirebasePrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Resolves the rate limiting identity key for the current request.
 *
 * <p>Authenticated users are identified by their Firebase UID. Anonymous or unauthenticated clients
 * are identified by IP address.
 */
public class RateLimitKeyResolver {

  /**
   * Resolved rate limiting key.
   *
   * @param authenticated whether the key belongs to an authenticated Firebase user
   * @param value normalized cache key value, prefixed with {@code uid:} or {@code ip:}
   */
  public record Key(boolean authenticated, String value) {}

  /**
   * Resolves the current request identity for rate limiting.
   *
   * @param request current HTTP request
   * @return rate limiting key based on Firebase UID or IP address
   */
  public Key resolve(HttpServletRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null
        && auth.isAuthenticated()
        && auth.getPrincipal() instanceof FirebasePrincipal fp) {
      return new Key(true, "uid:" + fp.uid());
    }

    // When the app is behind a reverse proxy, X-Forwarded-For usually contains
    // the original client IP as the first value in a comma-separated list.
    String xff = request.getHeader("X-Forwarded-For");
    String ip =
        (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : request.getRemoteAddr();

    return new Key(false, "ip:" + ip);
  }
}
