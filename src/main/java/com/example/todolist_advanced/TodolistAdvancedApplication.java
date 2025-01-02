package com.example.todolist_advanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TodolistAdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodolistAdvancedApplication.class, args);
    }

}
