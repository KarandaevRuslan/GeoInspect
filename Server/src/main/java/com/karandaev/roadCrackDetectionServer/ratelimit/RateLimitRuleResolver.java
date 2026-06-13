package com.karandaev.roadCrackDetectionServer.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;

/**
 * Resolves the first matching rate limiting rule for an HTTP request.
 *
 * <p>Rules are evaluated in the same order as they are declared in configuration. This allows
 * specific rules, such as {@code /v1/detect}, to be placed before broader fallback rules, such as
 * {@code /v1/**}.
 */
public class RateLimitRuleResolver {

  private final RateLimitProperties props;
  private final AntPathMatcher matcher = new AntPathMatcher();

  /**
   * Creates a resolver using configured rate limit properties.
   *
   * @param props rate limiting configuration properties
   */
  public RateLimitRuleResolver(RateLimitProperties props) {
    this.props = props;
  }

  /**
   * Finds the first configured rule matching the current request URI and HTTP method.
   *
   * @param request current HTTP request
   * @return matched rule, or {@code null} if no rule matches
   */
  public RateLimitProperties.Rule resolve(HttpServletRequest request) {
    String uri = request.getRequestURI();
    String method = request.getMethod();

    // Rules are checked from top to bottom; the first matching rule wins.
    for (var rule : props.getRules()) {
      if (rule.getPath() == null) continue;

      boolean pathOk = matcher.match(rule.getPath(), uri);
      if (!pathOk) continue;

      String ruleMethod = rule.getMethod() == null ? "ANY" : rule.getMethod().toUpperCase();
      boolean methodOk = ruleMethod.equals("ANY") || ruleMethod.equalsIgnoreCase(method);

      if (methodOk) return rule;
    }

    // Returning null means that the request is not rate limited.
    // A default catch-all rule is still recommended in configuration.
    return null;
  }
}
