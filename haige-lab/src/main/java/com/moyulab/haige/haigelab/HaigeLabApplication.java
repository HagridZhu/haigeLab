package com.moyulab.haige.haigelab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.moyulab.haige.haigelab.plusmapper")
public class HaigeLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(HaigeLabApplication.class, args);
	}

}
