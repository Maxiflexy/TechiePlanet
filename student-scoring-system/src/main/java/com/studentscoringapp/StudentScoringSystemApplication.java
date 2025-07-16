package com.studentscoringapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class StudentScoringSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentScoringSystemApplication.class, args);
	}

}