package com.karandaev.roadCrackDetectionServer.profile.web;

import com.karandaev.roadCrackDetectionServer.profile.AvatarService;
import com.karandaev.roadCrackDetectionServer.profile.dto.AvatarResponse;
import com.karandaev.roadCrackDetectionServer.security.FirebasePrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/** REST controller for profile avatar management. */
@RestController
@RequestMapping("/v1/profile/avatar")
public class AvatarController {

  private final AvatarService avatarService;

  public AvatarController(AvatarService avatarService) {
    this.avatarService = avatarService;
  }

  /**
   * Returns the authenticated user's current avatar URL.
   *
   * @param authentication current Spring Security authentication
   * @return response containing current avatar URL, or null if no custom avatar is set
   */
  @GetMapping
  public AvatarResponse getAvatar(Authentication authentication) {
    FirebasePrincipal principal = firebasePrincipal(authentication);
    return avatarService.getAvatar(principal);
  }

  /**
   * Uploads or replaces the authenticated user's avatar.
   *
   * @param authentication current Spring Security authentication
   * @param file multipart image file uploaded under the {@code file} part name
   * @param request current HTTP request used to build public avatar URL when no base URL is
   *     configured
   * @return public avatar URL
   * @throws IOException if the file cannot be read or saved
   */
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public AvatarResponse updateAvatar(
      Authentication authentication,
      @RequestPart("file") MultipartFile file,
      HttpServletRequest request)
      throws IOException {
    FirebasePrincipal principal = firebasePrincipal(authentication);
    return avatarService.updateAvatar(principal, file, request);
  }

  /**
   * Resets the authenticated user's avatar to default.
   *
   * @param authentication current Spring Security authentication
   * @return response with null avatarUrl
   * @throws IOException if the old avatar file cannot be deleted
   */
  @DeleteMapping
  public AvatarResponse resetAvatar(Authentication authentication) throws IOException {
    FirebasePrincipal principal = firebasePrincipal(authentication);
    return avatarService.resetAvatar(principal);
  }

  private FirebasePrincipal firebasePrincipal(Authentication authentication) {
    return (FirebasePrincipal) authentication.getPrincipal();
  }
}
