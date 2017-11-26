package com.intelect.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages={"com.intelect"})// same as @Configuration @EnableAutoConfiguration @ComponentScan combined
public class SpringBootDemoApp {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApp.class, args);
	}
}
