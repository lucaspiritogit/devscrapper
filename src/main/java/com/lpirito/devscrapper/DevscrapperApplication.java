package com.lpirito.devscrapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages="com.lpirito.devscrapper")
public class DevscrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevscrapperApplication.class, args);
	}

}
