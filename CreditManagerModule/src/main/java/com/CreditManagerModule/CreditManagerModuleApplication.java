package com.CreditManagerModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class CreditManagerModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditManagerModuleApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplateBean() {
        
		RestTemplate rs = new RestTemplate();
		return rs;
    }

}
