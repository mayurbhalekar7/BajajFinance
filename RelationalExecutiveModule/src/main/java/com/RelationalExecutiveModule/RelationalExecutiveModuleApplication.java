package com.RelationalExecutiveModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient

@SpringBootApplication
public class RelationalExecutiveModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelationalExecutiveModuleApplication.class, args);
	}

}
