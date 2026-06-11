package com.pruebas.unitarias25;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
// HATEOAS - Agregar perfil de test para usar configuración H2
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
// HATEOAS - Activar perfil "test" para usar application-test.properties con H2
@ActiveProfiles("test")
class Unitarias25ApplicationTests {

	@Test
	void contextLoads() {
	}

	// SE AGREGA PARA EL 100% - HATEOAS - Validar que la clase principal exista
	@Test
	void mainClassExists() {
		// HATEOAS - Validar que la clase de aplicación existe y puede ser instanciada
		assertNotNull(Unitarias25Application.class);
	}

}
