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
//	TODO: StackExchange error handing and testing
//	TODO:  testing (potentially integration)
//	TODO:  dockerize, deploy, push to git, document architecture/decisions

}
