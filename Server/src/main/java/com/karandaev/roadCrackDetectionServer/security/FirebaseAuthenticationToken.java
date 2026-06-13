package com.karandaev.roadCrackDetectionServer.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Spring Security authentication token used for Firebase authentication.
 *
 * <p>The token has two states:
 *
 * <ul>
 *   <li>Unauthenticated: contains a raw Firebase ID token as credentials.
 *   <li>Authenticated: contains a {@link FirebasePrincipal} and granted authorities.
 * </ul>
 */
public class FirebaseAuthenticationToken extends AbstractAuthenticationToken {

  private final Object principal;
  private final String credentials;

  /**
   * Creates an unauthenticated token before Firebase ID token verification.
   *
   * @param idToken raw Firebase ID token received from the client
   */
  public FirebaseAuthenticationToken(String idToken) {
    super(null);
    this.principal = null;
    this.credentials = idToken;
    setAuthenticated(false);
  }

  /**
   * Creates an authenticated token after successful Firebase ID token verification.
   *
   * @param principal authenticated Firebase principal
   * @param authorities granted Spring Security authorities
   */
  public FirebaseAuthenticationToken(
      FirebasePrincipal principal, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.credentials = null;
    setAuthenticated(true);
  }

  /**
   * Returns the raw Firebase ID token for unauthenticated requests.
   *
   * @return raw ID token before authentication, or {@code null} after authentication
   */
  @Override
  public Object getCredentials() {
    return credentials;
  }

  /**
   * Returns the authenticated Firebase principal.
   *
   * @return Firebase principal after authentication, or {@code null} before authentication
   */
  @Override
  public Object getPrincipal() {
    return principal;
  }
}
