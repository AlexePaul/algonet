package com.algonet.algonetapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AlgonetApiApplication {

    public static void main(String[] args) {
        log.info("Starting AlgoNet API Application...");
        SpringApplication.run(AlgonetApiApplication.class, args);
        log.info("AlgoNet API Application started successfully!");
    }

}
