package dev.daw.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class CodeSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeSampleApplication.class, args);
	}
}
