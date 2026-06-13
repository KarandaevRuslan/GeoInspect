package com.karandaev.roadCrackDetectionServer.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Servlet filter that applies configured rate limiting rules to incoming HTTP requests.
 *
 * <p>The filter resolves a matching rule for the current request, determines the client key using
 * either the authenticated Firebase UID or the request IP address, and then consumes tokens from
 * the corresponding Bucket4j bucket.
 *
 * <p>If the request exceeds the configured limit, the filter returns HTTP 429 with a {@code
 * Retry-After} header.
 */
public class RateLimitingFilter extends OncePerRequestFilter {

  private final RateLimitProperties props;
  private final RateLimiterService service;
  private final RateLimitKeyResolver keyResolver = new RateLimitKeyResolver();
  private final RateLimitRuleResolver ruleResolver;

  /**
   * Creates a rate limiting filter.
   *
   * @param props rate limiting configuration properties
   * @param service service used to create and cache Bucket4j buckets
   */
  public RateLimitingFilter(RateLimitProperties props, RateLimiterService service) {
    this.props = props;
    this.service = service;
    this.ruleResolver = new RateLimitRuleResolver(props);
  }

  /**
   * Applies rate limiting to the current request if rate limiting is enabled and a matching rule
   * exists.
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

    if (!props.isEnabled()) {
      filterChain.doFilter(request, response);
      return;
    }

    var rule = ruleResolver.resolve(request);
    if (rule == null) {
      filterChain.doFilter(request, response);
      return;
    }

    var key = keyResolver.resolve(request);

    var limits = service.buildLimits(rule, key.authenticated());
    if (limits.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    String cacheKey = rule.getId() + "|" + key.value();
    Bucket bucket = service.bucketFor(cacheKey, limits);

    int cost = Math.max(1, rule.getCost());
    ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(cost);

    // These headers are useful for debugging and for clients that want to display quota state.
    response.setHeader("X-RateLimit-Rule", rule.getId());
    response.setHeader("X-RateLimit-Key", key.authenticated() ? "uid" : "ip");
    response.setHeader("X-RateLimit-Remaining", String.valueOf(probe.getRemainingTokens()));

    if (probe.isConsumed()) {
      filterChain.doFilter(request, response);
      return;
    }

    long waitNanos = probe.getNanosToWaitForRefill();
    long waitSeconds = Math.max(1, waitNanos / 1_000_000_000L);

    response.setStatus(429);
    response.setHeader("Retry-After", String.valueOf(waitSeconds));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response
        .getWriter()
        .write(
            """
                {"error":"rate_limited","message":"Too many requests. Try later."}
                """);
  }
}
