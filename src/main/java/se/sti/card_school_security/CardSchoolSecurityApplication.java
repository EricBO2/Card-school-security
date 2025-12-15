package se.sti.card_school_security;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class CardSchoolSecurityApplication {


	public static void main(String[] args) {
		SpringApplication.run(CardSchoolSecurityApplication.class, args);
	}

}
