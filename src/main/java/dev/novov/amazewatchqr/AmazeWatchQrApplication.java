package dev.novov.amazewatchqr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AmazeWatchQrApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmazeWatchQrApplication.class, args);
	}

	@org.springframework.context.annotation.Bean
	org.springframework.boot.CommandLineRunner init(
			dev.novov.amazewatchqr.service.storage.StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
}
