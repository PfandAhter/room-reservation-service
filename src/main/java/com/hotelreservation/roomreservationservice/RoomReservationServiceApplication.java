package com.hotelreservation.roomreservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class RoomReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomReservationServiceApplication.class, args);
	}

}
