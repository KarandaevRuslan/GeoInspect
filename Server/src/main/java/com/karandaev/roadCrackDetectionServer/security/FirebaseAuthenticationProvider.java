package com.karandaev.roadCrackDetectionServer.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security authentication provider that validates Firebase ID tokens.
 *
 * <p>This provider accepts {@link FirebaseAuthenticationToken} instances containing a raw Firebase
 * ID token. The token is verified through the Firebase Admin SDK. If verification succeeds, the
 * provider creates an authenticated {@link FirebaseAuthenticationToken} containing a {@link
 * FirebasePrincipal} and Spring Security authorities derived from Firebase custom claims.
 */
@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

  /**
   * Authenticates a raw Firebase ID token and converts it into an authenticated Spring Security
   * authentication object.
   *
   * @param authentication authentication request containing the raw Firebase ID token
   * @return authenticated Firebase authentication token, or {@code null} if this provider does not
   *     support the supplied authentication type
   * @throws BadCredentialsException if the token is missing, invalid, or expired
   */
  @Override
  public Authentication authenticate(Authentication authentication) {
    if (!(authentication instanceof FirebaseAuthenticationToken token)) {
      return null;
    }

    String idToken = (String) token.getCredentials();
    if (idToken == null || idToken.isBlank()) {
      throw new BadCredentialsException("Missing Firebase ID token");
    }

    try {
      // verifyIdToken validates signature, expiration time, issuer, and audience internally.
      FirebaseToken decoded = FirebaseAuth.getInstance().verifyIdToken(idToken);

      // Custom claims can be used to store application-specific roles.
      // Expected example: {"roles": ["USER", "ADMIN"]}
      Object rolesObj = decoded.getClaims().get("roles");
      Set<String> roles = ClaimUtils.extractRoles(rolesObj);

      var authorities =
          roles.stream()
              // Spring Security convention requires role authorities to start with "ROLE_".
              .map(r -> "ROLE_" + r)
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toSet());

      FirebasePrincipal principal =
          new FirebasePrincipal(
              decoded.getUid(),
              decoded.getName(),
              decoded.getEmail(),
              Boolean.TRUE.equals(decoded.isEmailVerified()),
              roles);

      return new FirebaseAuthenticationToken(principal, authorities);
    } catch (Exception e) {
      // BadCredentialsException is the standard Spring Security exception for invalid credentials.
      throw new BadCredentialsException("Invalid Firebase ID token", e);
    }
  }

  /**
   * Checks whether this provider supports the given authentication type.
   *
   * @param authentication authentication class to check
   * @return {@code true} if the authentication class is compatible with {@link
   *     FirebaseAuthenticationToken}
   */
  @Override
  public boolean supports(Class<?> authentication) {
    return FirebaseAuthenticationToken.class.isAssignableFrom(authentication);
  }

  /** Utility methods for extracting application roles from Firebase custom claims. */
  static class ClaimUtils {

    /**
     * Extracts role names from a Firebase custom claim value.
     *
     * <p>The claim may be absent, iterable, or a single scalar value. If no valid roles are found,
     * the default {@code USER} role is returned.
     *
     * @param rolesObj raw value of the Firebase {@code roles} custom claim
     * @return normalized set of role names
     */
    static Set<String> extractRoles(Object rolesObj) {
      if (rolesObj == null) return Set.of("USER");

      if (rolesObj instanceof Iterable<?> it) {
        Set<String> out = new java.util.HashSet<>();
        for (Object o : it) {
          if (o != null) out.add(o.toString());
        }
        return out.isEmpty() ? Set.of("USER") : out;
      }

      // Support a single string-like role value, for example: "ADMIN".
      return Set.of(rolesObj.toString());
    }
  }
}
