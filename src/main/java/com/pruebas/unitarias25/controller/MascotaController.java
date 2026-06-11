package com.pruebas.unitarias25.controller;

import java.util.List;
// HATEOAS - Importar clases para implementar hypermedia
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// HATEOAS - Importar EntityModel y CollectionModel para empaquetar las respuestas
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import com.pruebas.unitarias25.model.Mascota;
import com.pruebas.unitarias25.service.MascotaService;

@RestController
@RequestMapping("/api/v1/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    @PostMapping
    // HATEOAS - Cambiar retorno a EntityModel para incluir links hipermedia
    public EntityModel<Mascota> crearMascota(@RequestBody Mascota mascota) {
        // HATEOAS - Guardar la mascota
        Mascota mascotaGuardada = mascotaService.guardarMascota(mascota);
        // HATEOAS - Retornar EntityModel con links de self, obtener por id, y listar
        // todas
        return EntityModel.of(mascotaGuardada,
                linkTo(methodOn(MascotaController.class).obtenerPorId(mascotaGuardada.getId())).withSelfRel(),
                linkTo(methodOn(MascotaController.class).obtenerTodas()).withRel("mascotas"));
    }

    @GetMapping
    // HATEOAS - Cambiar retorno a CollectionModel para incluir links hipermedia
    public CollectionModel<EntityModel<Mascota>> obtenerTodas() {
        // HATEOAS - Obtener lista de mascotas
        List<Mascota> mascotas = mascotaService.listarMascotas();
        // HATEOAS - Agregar links a cada mascota y al listado completo
        List<EntityModel<Mascota>> mascotasConLinks = mascotas.stream()
                .map(mascota -> EntityModel.of(mascota,
                        linkTo(methodOn(MascotaController.class).obtenerPorId(mascota.getId())).withSelfRel()))
                .toList();
        // HATEOAS - Retornar CollectionModel con el enlace self
        return CollectionModel.of(mascotasConLinks,
                linkTo(methodOn(MascotaController.class).obtenerTodas()).withSelfRel());
    }

    @GetMapping("/{id}")
    // HATEOAS - Cambiar retorno a ResponseEntity<EntityModel<Mascota>>
    public ResponseEntity<EntityModel<Mascota>> obtenerPorId(@PathVariable Long id) {
        // HATEOAS - Mapear la respuesta para agregar links hipermedia
        return mascotaService.obtenerMascotaPorId(id)
                .map(mascota -> ResponseEntity.ok(EntityModel.of(mascota,
                        linkTo(methodOn(MascotaController.class).obtenerPorId(id)).withSelfRel(),
                        linkTo(methodOn(MascotaController.class).obtenerTodas()).withRel("mascotas"))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    // HATEOAS - Cambiar retorno a ResponseEntity<EntityModel<Mascota>>
    public ResponseEntity<EntityModel<Mascota>> actualizar(@PathVariable Long id, @RequestBody Mascota mascota) {
        try {
            // HATEOAS - Actualizar y agregar links hipermedia
            Mascota actualizada = mascotaService.actualizarMascota(id, mascota);
            // HATEOAS - Retornar EntityModel con links de self y listado
            return ResponseEntity.ok(EntityModel.of(actualizada,
                    linkTo(methodOn(MascotaController.class).obtenerPorId(id)).withSelfRel(),
                    linkTo(methodOn(MascotaController.class).obtenerTodas()).withRel("mascotas")));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mascotaService.eliminarMascota(id);
        return ResponseEntity.noContent().build();
    }
}