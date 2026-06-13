package com.karandaev.roadCrackDetectionServer.ratelimit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for application rate limiting.
 *
 * <p>Properties are loaded from the {@code rate-limit} prefix in {@code application.yml} or {@code
 * application.properties}.
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

  private boolean enabled = true;
  private int cacheTtlMinutes = 60;

  private List<Rule> rules = new ArrayList<>();

  /**
   * Token bucket limit configuration.
   *
   * <p>The bucket can hold up to {@code capacity} tokens. Every {@code refillPeriodSeconds}, {@code
   * refillTokens} tokens are added back to the bucket.
   */
  @Setter
  @Getter
  public static class Limit {
    private int capacity;
    private int refillTokens;
    private int refillPeriodSeconds;
  }

  /**
   * Rate limiting rule matched against request method and path.
   *
   * <p>Each rule can define separate limits for authenticated users and IP-based clients. The first
   * matching rule is used by {@link RateLimitRuleResolver}.
   */
  @Setter
  @Getter
  public static class Rule {
    private String id;
    private String method = "ANY";
    private String path;
    private int cost = 1;

    private Limit user;
    private Limit ip;

    private Limit userPerSecond;
  }
}
