package com.doomedcat17.nbpexchangeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NbpExchangeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbpExchangeApiApplication.class, args);
    }

}
