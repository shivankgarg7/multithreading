package com.craft.onboarding.driveronboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.IOException;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DriverOnboardingApplication{


	public static void main(String[] args) throws IOException {


		SpringApplication.run(DriverOnboardingApplication.class, args);
	}

}