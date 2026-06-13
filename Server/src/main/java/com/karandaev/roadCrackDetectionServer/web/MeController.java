package com.karandaev.roadCrackDetectionServer.web;

import com.karandaev.roadCrackDetectionServer.security.FirebasePrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** REST controller that exposes information about the currently authenticated user. */
@RestController
public class MeController {

  /**
   * Returns the current authenticated principal.
   *
   * <p>For Firebase-authenticated requests, the response contains Firebase user data stored in
   * {@link FirebasePrincipal}. For other principal types, the raw principal object is returned.
   *
   * @param authentication current Spring Security authentication object
   * @return map containing authenticated user information
   */
  @GetMapping("/v1/me")
  public Map<String, Object> me(Authentication authentication) {
    // Spring Security provides this object from the current SecurityContext.
    Object principal = authentication.getPrincipal();

    if (principal instanceof FirebasePrincipal fp) {
      return Map.of(
          "uid", fp.uid(),
          "name", fp.name(),
          "email", fp.email(),
          "emailVerified", fp.emailVerified(),
          "roles", fp.roles());
    }

    return Map.of("principal", principal);
  }
}
