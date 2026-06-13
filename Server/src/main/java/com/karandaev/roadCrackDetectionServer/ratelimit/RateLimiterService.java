package com.karandaev.roadCrackDetectionServer.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for creating and caching Bucket4j rate limit buckets.
 *
 * <p>Each bucket is stored by a cache key that usually combines a rule identifier and a resolved
 * client identity, such as a Firebase user UID or an IP address. Buckets expire after a configured
 * period of inactivity to avoid keeping unused clients in memory forever.
 */
public class RateLimiterService {

  private final Cache<String, Bucket> buckets;

  /**
   * Creates a rate limiter service with a Caffeine cache for Bucket4j buckets.
   *
   * @param props rate limiting configuration properties
   */
  public RateLimiterService(RateLimitProperties props) {
    this.buckets =
        Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(props.getCacheTtlMinutes()))
            .maximumSize(200_000)
            .build();
  }

  /**
   * Returns a cached bucket for the given key or creates a new one with the supplied limits.
   *
   * @param cacheKey unique bucket key, usually based on rule id and client identity
   * @param limits Bucket4j bandwidth limits that should be attached to the bucket
   * @return existing or newly created bucket, or {@code null} if no limits are provided
   */
  public Bucket bucketFor(String cacheKey, List<Bandwidth> limits) {
    if (limits == null || limits.isEmpty()) {
      return null;
    }

    return buckets.get(
        cacheKey,
        k -> {
          var builder = Bucket.builder();

          // A single bucket can contain multiple bandwidth limits, for example
          // a per-minute limit and an additional per-second burst limit.
          for (Bandwidth bw : limits) {
            builder.addLimit(bw);
          }

          return builder.build();
        });
  }

  /**
   * Builds Bucket4j limits for the matched rule and current client identity type.
   *
   * <p>Authenticated users are limited by the {@code user} section of the rule. Anonymous clients
   * are limited by the {@code ip} section. An optional per-second user limit is added only for
   * authenticated users.
   *
   * @param rule matched rate limit rule
   * @param authenticated whether the current request belongs to an authenticated user
   * @return list of Bucket4j bandwidth limits, or an empty list when limiting is disabled for the
   *     current identity type
   */
  public List<Bandwidth> buildLimits(RateLimitProperties.Rule rule, boolean authenticated) {
    var base = authenticated ? rule.getUser() : rule.getIp();
    if (base == null) {
      return List.of();
    }

    List<Bandwidth> limits = new ArrayList<>();
    limits.add(toBandwidth(base));

    // The extra per-second limit is currently applied only to authenticated users.
    // It can be extended to IP-based clients later if needed.
    if (authenticated && rule.getUserPerSecond() != null) {
      limits.add(toBandwidth(rule.getUserPerSecond()));
    }

    return limits;
  }

  /**
   * Converts application-level limit configuration into a Bucket4j bandwidth limit.
   *
   * @param limit configured capacity and refill settings
   * @return Bucket4j bandwidth object
   */
  private Bandwidth toBandwidth(RateLimitProperties.Limit limit) {
    return Bandwidth.builder()
        .capacity(limit.getCapacity())
        .refillIntervally(
            limit.getRefillTokens(), Duration.ofSeconds(limit.getRefillPeriodSeconds()))
        .build();
  }
}
