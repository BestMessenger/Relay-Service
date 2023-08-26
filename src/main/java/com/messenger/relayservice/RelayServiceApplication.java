package com.messenger.relayservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@OpenAPIDefinition
@EnableDiscoveryClient
public class RelayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelayServiceApplication.class, args);
    }

}
