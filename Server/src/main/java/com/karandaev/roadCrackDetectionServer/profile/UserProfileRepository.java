package com.karandaev.roadCrackDetectionServer.profile;

import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for user profiles. */
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {}
