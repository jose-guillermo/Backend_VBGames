package com.vbgames.backend.friendshipservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FriendshipserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendshipserviceApplication.class, args);
	}

}
