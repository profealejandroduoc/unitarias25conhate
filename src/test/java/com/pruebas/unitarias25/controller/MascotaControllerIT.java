package com.pruebas.unitarias25.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebas.unitarias25.model.Mascota;
import com.pruebas.unitarias25.repository.MascotaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MascotaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MascotaRepository mascotaRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void cleanDb() {
        mascotaRepository.deleteAll();
    }

    @Test
    void testCrearYObtenerMascota() throws Exception {
        Mascota mascota = new Mascota(null, "Max", "Perro", 4);

        mockMvc.perform(post("/api/v1/mascotas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mascota)))
                .andExpect(status().isOk())
                // HATEOAS - Verificar que el ID existe en EntityModel
                .andExpect(jsonPath("$.id").exists())
                // HATEOAS - Verificar datos en EntityModel
                .andExpect(jsonPath("$.nombre").value("Max"))
                // HATEOAS - Verificar que existen los links
                .andExpect(jsonPath("$._links").exists());

        mockMvc.perform(get("/api/v1/mascotas"))
                .andExpect(status().isOk())
                // HATEOAS - Los mascotas están dentro de _embedded.mascotaList
                .andExpect(jsonPath("$._embedded.mascotaList[0].nombre").value("Max"))
                .andExpect(jsonPath("$._embedded.mascotaList[0].tipo").value("Perro"))
                .andExpect(jsonPath("$._embedded.mascotaList[0].edad").value(4))
                // HATEOAS - Verificar que existen los links
                .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testEliminarMascota() throws Exception {
        Mascota mascota = new Mascota(null, "Firulais", "Perro", 3);
        Mascota guardada = mascotaRepository.save(mascota);

        mockMvc.perform(delete("/api/v1/mascotas/" + guardada.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/mascotas/" + guardada.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarMascota() throws Exception {
        Mascota mascota = new Mascota(null, "Rocky", "Perro", 2);
        Mascota guardada = mascotaRepository.save(mascota);

        Mascota actualizada = new Mascota(null, "Rocky", "Perro", 5);

        mockMvc.perform(put("/api/v1/mascotas/" + guardada.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
                .andExpect(status().isOk())
                // HATEOAS - Verificar que la edad se actualizó
                .andExpect(jsonPath("$.edad").value(5))
                // HATEOAS - Verificar que existen los links
                .andExpect(jsonPath("$._links").exists());
    }
}