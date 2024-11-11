package com.scare.api.core.config.firebase;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

	@Value("${fcm.key}")
	private String pathFcmKey;

	@PostConstruct
	public void initializeFirebase() {
		try (InputStream serviceAccount = getClass().getClassLoader()
			.getResourceAsStream(pathFcmKey)) {
			if (serviceAccount == null) {
				throw new RuntimeException("[FirebaseException] service-account-key.json 파일을 찾을 수 없습니다.");
			}

			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			throw new RuntimeException("[FirebaseException] firebase 초기화 실패: ", e);
		}
	}
}