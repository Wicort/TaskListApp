package ru.vovchinnikov.tasklistapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Ovchinnikov Viktor
 */

@SpringBootApplication
public class TaskListApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskListApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
