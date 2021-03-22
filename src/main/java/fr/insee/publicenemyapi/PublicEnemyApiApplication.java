package fr.insee.publicenemyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "fr.insee.publicenemyapi")
public class PublicEnemyApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PublicEnemyApiApplication.class, args);
	}

}