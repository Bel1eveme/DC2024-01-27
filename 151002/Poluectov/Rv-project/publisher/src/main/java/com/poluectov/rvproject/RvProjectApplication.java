package com.poluectov.rvproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication

public class RvProjectApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        SpringApplication.run(RvProjectApplication.class, args);
    }



}
