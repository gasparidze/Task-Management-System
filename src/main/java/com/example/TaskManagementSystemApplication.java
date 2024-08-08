package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Класс, запускающий Spring Boot приложение
 *
 * @author Arthur Gasparyan
 */
@SpringBootApplication
public class TaskManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskManagementSystemApplication.class, args);
    }
}