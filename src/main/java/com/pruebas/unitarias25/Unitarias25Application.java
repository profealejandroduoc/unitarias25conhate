package com.pruebas.unitarias25;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// HATEOAS - Excluir auto-configuración de SpringDocHateoasConfiguration para evitar conflictos
@SpringBootApplication(exclude = org.springdoc.core.configuration.SpringDocHateoasConfiguration.class)
public class Unitarias25Application {

	public static void main(String[] args) {
		SpringApplication.run(Unitarias25Application.class, args);
	}

}
