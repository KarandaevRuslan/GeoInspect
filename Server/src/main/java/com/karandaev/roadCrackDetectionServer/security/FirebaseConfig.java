package com.karandaev.roadCrackDetectionServer.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Firebase Admin SDK configuration.
 *
 * <p>This configuration initializes the default {@link FirebaseApp} using a service account
 * resource provided through the {@code firebase.service-account} application property.
 */
@Configuration
public class FirebaseConfig {

  @Value("${firebase.service-account}")
  private Resource serviceAccount;

  /**
   * Initializes Firebase Admin SDK after Spring creates this configuration bean.
   *
   * <p>The method avoids creating a second Firebase application if one has already been initialized
   * in the current JVM.
   *
   * @throws IOException if the configured service account resource cannot be read
   */
  @PostConstruct
  public void init() throws IOException {
    // Avoid creating a duplicate FirebaseApp if it has already been initialized.
    if (!FirebaseApp.getApps().isEmpty()) return;

    try (InputStream in = serviceAccount.getInputStream()) {
      FirebaseOptions options =
          FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(in)).build();

      FirebaseApp.initializeApp(options);
    }
  }
}
