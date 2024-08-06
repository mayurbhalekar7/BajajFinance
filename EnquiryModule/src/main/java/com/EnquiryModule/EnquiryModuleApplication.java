package com.EnquiryModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@EnableDiscoveryClient
@SpringBootApplication
public class EnquiryModuleApplication {

	public static void main(String[] args) {
		System.out.println("Enquiry Module Execution start");
		SpringApplication.run(EnquiryModuleApplication.class, args);
	}

}
