package com.karandaev.roadCrackDetectionServer.security;

import java.util.Set;

/**
 * Authenticated Firebase user principal stored in the Spring Security context.
 *
 * @param uid unique Firebase user identifier
 * @param name user display name, may be {@code null} if not provided by the identity provider
 * @param email user email address, may be {@code null} for anonymous or provider-specific accounts
 * @param emailVerified whether the user's email address is verified
 * @param roles application roles extracted from Firebase custom claims
 */
public record FirebasePrincipal(
    String uid, String name, String email, boolean emailVerified, Set<String> roles) {}
