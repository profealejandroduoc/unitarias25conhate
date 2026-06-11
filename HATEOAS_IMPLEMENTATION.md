# Implementación de HATEOAS en la Aplicación de Mascotas

## ¿Qué es HATEOAS?

HATEOAS (Hypermedia As The Engine Of Application State) es un principio REST que proporciona hipervínculos en las respuestas HTTP, permitiendo que los clientes naveguen el API dinámicamente sin necesidad de conocer las URLs hardcodeadas.

## Cambios Realizados

### 1. Dependencia Maven (pom.xml)
```xml
<!-- HATEOAS - Dependencia para implementar hypermedia en las respuestas REST -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

### 2. Controlador (MascotaController.java)

#### Cambios en Importes:
```java
// HATEOAS - Importar clases para implementar hypermedia
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
```

#### Métodos Modificados:

##### POST - Crear Mascota
- **Antes**: Retornaba `Mascota` simple
- **Ahora**: Retorna `EntityModel<Mascota>` con links:
  - `self`: link a la mascota creada
  - `mascotas`: link a la lista completa de mascotas

##### GET - Obtener Todas las Mascotas
- **Antes**: Retornaba `List<Mascota>`
- **Ahora**: Retorna `CollectionModel<EntityModel<Mascota>>` con:
  - Cada mascota envuelta en `EntityModel` con link `self`
  - Link `self` a la colección completa dentro de `_embedded`

##### GET - Obtener Mascota por ID
- **Antes**: Retornaba `ResponseEntity<Mascota>`
- **Ahora**: Retorna `ResponseEntity<EntityModel<Mascota>>` con links:
  - `self`: link a la mascota actual
  - `mascotas`: link a la lista completa

##### PUT - Actualizar Mascota
- **Antes**: Retornaba `ResponseEntity<Mascota>`
- **Ahora**: Retorna `ResponseEntity<EntityModel<Mascota>>` con links:
  - `self`: link a la mascota actualizada
  - `mascotas`: link a la lista completa

### 3. Configuración (application.properties)
```properties
# HATEOAS - Configuración para Spring HATEOAS
# HATEOAS - Habilitar la serialización de los links en las respuestas
spring.hateoas.use-hal-as-default-json-media-type=true
```

### 4. Tests Actualizados

#### MascotaControllerTest.java
- **testObtenerTodas()**: Ahora valida `$._embedded.mascotaList` y `$._links.self`
- **testGuardarMascota()**: Ahora valida links `$._links.self` y `$._links.mascotas`
- **testObtenerMascotaPorIdExistente()**: Valida los links en la respuesta
- **testActualizarMascota()**: Valida los links en la respuesta actualizada

#### MascotaControllerIT.java
- **testCrearYObtenerMascota()**: Valida estructura con `_embedded` y `_links`
- **testActualizarMascota()**: Valida los links en respuesta

## Estructura de Respuestas

### Ejemplo: GET /api/v1/mascotas (Obtener todas)
```json
{
  "_embedded": {
    "mascotaList": [
      {
        "id": 1,
        "nombre": "Max",
        "tipo": "Perro",
        "edad": 4,
        "_links": {
          "self": {
            "href": "http://localhost:8081/api/v1/mascotas/1"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8081/api/v1/mascotas"
    }
  }
}
```

### Ejemplo: GET /api/v1/mascotas/1 (Obtener una mascota)
```json
{
  "id": 1,
  "nombre": "Max",
  "tipo": "Perro",
  "edad": 4,
  "_links": {
    "self": {
      "href": "http://localhost:8081/api/v1/mascotas/1"
    },
    "mascotas": {
      "href": "http://localhost:8081/api/v1/mascotas"
    }
  }
}
```

### Ejemplo: POST /api/v1/mascotas (Crear mascota)
```json
{
  "id": 2,
  "nombre": "Firulais",
  "tipo": "Perro",
  "edad": 3,
  "_links": {
    "self": {
      "href": "http://localhost:8081/api/v1/mascotas/2"
    },
    "mascotas": {
      "href": "http://localhost:8081/api/v1/mascotas"
    }
  }
}
```

## Beneficios de HATEOAS

1. **Descubrimiento de API**: Los clientes pueden descubrir endpoints dinámicamente
2. **Desacoplamiento**: No es necesario hardcodear URLs en el cliente
3. **Documentación Automática**: Los links sirven como documentación
4. **Navegación**: Los clientes pueden navegar el API siguiendo los links
5. **Evolución del API**: Los cambios en URLs no rompen los clientes

## Cómo Usar el API

1. **Listar todas las mascotas**: `GET /api/v1/mascotas`
2. **Obtener una mascota**: Usar el link `self` de la respuesta
3. **Volver a listar**: Usar el link `mascotas` de cualquier respuesta
4. **Crear nueva mascota**: `POST /api/v1/mascotas` con body JSON
5. **Actualizar mascota**: `PUT /api/v1/mascotas/{id}` con body JSON

## Notas Importantes

- La configuración `spring.hateoas.use-hal-as-default-json-media-type=true` utiliza el formato HAL (Hypertext Application Language)
- HAL es un formato estándar para APIs REST con hypermedia
- Los tests han sido actualizados para validar la estructura HAL con los prefijos `_embedded` y `_links`

## Referencias

- [Spring HATEOAS Official Documentation](https://spring.io/projects/spring-hateoas)
- [HAL Specification](https://stateless.group/hal_specification.html)
- [REST Maturity Model - Level 3: HATEOAS](https://martinfowler.com/articles/richardsonMaturityModel.html)
