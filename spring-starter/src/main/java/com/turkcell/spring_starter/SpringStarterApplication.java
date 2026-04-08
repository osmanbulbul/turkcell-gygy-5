package com.turkcell.spring_starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // anntation: bulundugu class fonk degiskene ozellik kazandiran yapidir. SpringBoot uygulamasinin baslatilmasi icin gerekli olan bir annotationdur.
public class SpringStarterApplication {

	// Entry point
	public static void main(String[] args) {
		SpringApplication.run(SpringStarterApplication.class, args);// springi baslatmak icin kullanlir.
	}

}
