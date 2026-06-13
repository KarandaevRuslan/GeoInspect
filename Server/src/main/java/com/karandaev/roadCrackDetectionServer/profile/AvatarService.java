package com.karandaev.roadCrackDetectionServer.profile;

import com.karandaev.roadCrackDetectionServer.image.model.SafeImage;
import com.karandaev.roadCrackDetectionServer.profile.config.AvatarStorageProperties;
import com.karandaev.roadCrackDetectionServer.profile.dto.AvatarResponse;
import com.karandaev.roadCrackDetectionServer.security.FirebasePrincipal;
import com.karandaev.roadCrackDetectionServer.service.ImageValidationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/** Handles profile avatar uploads and resets. */
@Service
public class AvatarService {

  private final UserProfileRepository repository;
  private final ImageValidationService imageValidationService;
  private final AvatarStorageProperties properties;

  public AvatarService(
      UserProfileRepository repository,
      ImageValidationService imageValidationService,
      AvatarStorageProperties properties) {
    this.repository = repository;
    this.imageValidationService = imageValidationService;
    this.properties = properties;
  }

  @Transactional(readOnly = true)
  public AvatarResponse getAvatar(FirebasePrincipal principal) {
    return repository
        .findById(principal.uid())
        .map(profile -> new AvatarResponse(profile.getAvatarUrl()))
        .orElseGet(() -> new AvatarResponse(null));
  }

  @Transactional
  public AvatarResponse updateAvatar(
      FirebasePrincipal principal, MultipartFile file, HttpServletRequest request)
      throws IOException {
    String uid = principal.uid();

    byte[] bytes = file.getBytes();

    // Validate and normalize image using your existing image security pipeline.
    SafeImage safe = imageValidationService.validateAndNormalize(bytes);

    UserProfile profile = repository.findById(uid).orElseGet(() -> new UserProfile(uid));

    profile.setEmail(principal.email());

    profile.setDisplayName(principal.name());

    // Delete previous avatar file if it exists.
    deleteExistingAvatarFile(uid, profile.getAvatarFileName());

    String fileName = UUID.randomUUID() + ".png";

    Path userDir = avatarRootDirectory().resolve(uid).normalize();
    Files.createDirectories(userDir);

    Path target = userDir.resolve(fileName).normalize();

    if (!target.startsWith(userDir)) {
      throw new SecurityException("Invalid avatar path");
    }

    Files.write(target, safe.normalizedBytes());

    String avatarUrl = publicAvatarUrl(uid, fileName, request);

    profile.setAvatarFileName(fileName);
    profile.setAvatarUrl(avatarUrl);
    profile.touch();

    repository.save(profile);

    return new AvatarResponse(avatarUrl);
  }

  @Transactional
  public AvatarResponse resetAvatar(FirebasePrincipal principal) throws IOException {
    String uid = principal.uid();

    UserProfile profile = repository.findById(uid).orElseGet(() -> new UserProfile(uid));

    deleteExistingAvatarFile(uid, profile.getAvatarFileName());

    profile.setEmail(principal.email());
    profile.setAvatarFileName(null);
    profile.setAvatarUrl(null);
    profile.touch();

    repository.save(profile);

    return new AvatarResponse(null);
  }

  private void deleteExistingAvatarFile(String uid, String fileName) throws IOException {
    if (fileName == null || fileName.isBlank()) {
      return;
    }

    Path userDir = avatarRootDirectory().resolve(uid).normalize();
    Path file = userDir.resolve(fileName).normalize();

    if (file.startsWith(userDir)) {
      Files.deleteIfExists(file);
    }
  }

  private Path avatarRootDirectory() {
    return Path.of(properties.getDirectory()).toAbsolutePath().normalize();
  }

  private String publicAvatarUrl(String uid, String fileName, HttpServletRequest request) {
    String base = properties.getPublicBaseUrl();

    if (base == null || base.isBlank()) {
      base =
          ServletUriComponentsBuilder.fromRequestUri(request)
              .replacePath(null)
              .replaceQuery(null)
              .build()
              .toUriString();
    }

    if (base.endsWith("/")) {
      base = base.substring(0, base.length() - 1);
    }

    return base + "/public/avatars/" + uid + "/" + fileName;
  }
}
