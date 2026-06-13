package com.karandaev.roadCrackDetectionServer.security;

import com.karandaev.roadCrackDetectionServer.ratelimit.RateLimitProperties;
import com.karandaev.roadCrackDetectionServer.ratelimit.RateLimiterService;
import com.karandaev.roadCrackDetectionServer.ratelimit.RateLimitingFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main Spring Security configuration.
 *
 * <p>The application uses stateless Firebase authentication. Public endpoints are available without
 * authentication, while {@code /v1/**} endpoints require an authenticated Firebase principal with a
 * verified email address.
 *
 * <p>The rate limiting filter is placed after Firebase authentication so it can use the
 * authenticated user identity from the security context.
 */
@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class SecurityConfig {

  /**
   * Creates the authentication manager used by the Firebase authentication filter.
   *
   * @param provider Firebase authentication provider
   * @return authentication manager delegating authentication to the Firebase provider
   */
  @Bean
  public AuthenticationManager authenticationManager(FirebaseAuthenticationProvider provider) {
    // ProviderManager is the standard AuthenticationManager that delegates to providers.
    return new ProviderManager(provider);
  }

  /**
   * Creates the rate limiter service.
   *
   * @param props rate limiting configuration properties
   * @return rate limiter service
   */
  @Bean
  public RateLimiterService rateLimiterService(RateLimitProperties props) {
    return new RateLimiterService(props);
  }

  /**
   * Creates the rate limiting filter.
   *
   * @param props rate limiting configuration properties
   * @param service rate limiter service
   * @return rate limiting filter
   */
  @Bean
  public RateLimitingFilter rateLimitingFilter(
      RateLimitProperties props, RateLimiterService service) {
    return new RateLimitingFilter(props, service);
  }

  /**
   * Configures HTTP security, Firebase authentication, authorization rules, and rate limiting.
   *
   * @param http HTTP security builder
   * @param authenticationManager authentication manager used by Firebase authentication
   * @param rateLimitingFilter rate limiting filter applied after authentication
   * @return configured security filter chain
   * @throws Exception if Spring Security configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AuthenticationManager authenticationManager,
      RateLimitingFilter rateLimitingFilter)
      throws Exception {

    var firebaseFilter = new FirebaseAuthenticationFilter(authenticationManager);

    return http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/public/**")
                    .permitAll()
                    // /v1/** is available only to authenticated users with verified emails.
                    .requestMatchers("/v1/**")
                    .access(
                        (authentication, context) -> {
                          var a = authentication.get();
                          var principal = a.getPrincipal();

                          if (principal instanceof FirebasePrincipal fp) {
                            return new AuthorizationDecision(fp.emailVerified());
                          }
                          return new AuthorizationDecision(false);
                        })
                    .anyRequest()
                    .denyAll())
        // 1. Authenticate Firebase ID token first.
        .addFilterBefore(firebaseFilter, UsernamePasswordAuthenticationFilter.class)
        // 2. Apply rate limiting after authentication so the user UID is available.
        .addFilterAfter(rateLimitingFilter, FirebaseAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults())
        .build();
  }
}
