package com.karandaev.roadCrackDetectionServer.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** User profile stored by Firebase UID. */
@Getter
@Entity
@Table(name = "user_profiles")
public class UserProfile {

  @Id
  @Column(name = "firebase_uid", nullable = false, length = 128)
  private String firebaseUid;

  @Setter
  @Column(name = "email")
  private String email;

  @Setter
  @Column(name = "display_name")
  private String displayName;

  @Setter
  @Column(name = "avatar_url", length = 2048)
  private String avatarUrl;

  @Setter
  @Column(name = "avatar_file_name")
  private String avatarFileName;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected UserProfile() {}

  public UserProfile(String firebaseUid) {
    this.firebaseUid = firebaseUid;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public void touch() {
    this.updatedAt = Instant.now();
  }
}
