package com.pruebas.unitarias25.service;

import com.pruebas.unitarias25.model.Mascota;
import com.pruebas.unitarias25.repository.MascotaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    @Test
    void testGuardarMascota() {
        Mascota mascota = new Mascota(null, "Toby", "Perro", 3);
        Mascota guardada = new Mascota(1L, "Toby", "Perro", 3);

        when(mascotaRepository.save(mascota)).thenReturn(guardada);

        Mascota resultado = mascotaService.guardarMascota(mascota);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Toby", resultado.getNombre());
        assertEquals("Perro", resultado.getTipo());
        assertEquals(3, resultado.getEdad());

        verify(mascotaRepository, times(1)).save(mascota);
    }

    @Test
    void testListarMascotas() {
        Mascota m1 = new Mascota(1L, "Toby", "Perro", 3);
        Mascota m2 = new Mascota(2L, "Michi", "Gato", 1);

        when(mascotaRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Mascota> resultado = mascotaService.listarMascotas();

        assertEquals(2, resultado.size());
        assertEquals("Toby", resultado.get(0).getNombre());
        assertEquals("Michi", resultado.get(1).getNombre());

        verify(mascotaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerMascotaPorIdExistente() {
        Mascota mascota = new Mascota(1L, "Toby", "Perro", 3);

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        Optional<Mascota> resultado = mascotaService.obtenerMascotaPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Toby", resultado.get().getNombre());

        verify(mascotaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerMascotaPorIdNoExistente() {
        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Mascota> resultado = mascotaService.obtenerMascotaPorId(99L);

        assertFalse(resultado.isPresent());

        verify(mascotaRepository, times(1)).findById(99L);
    }

    @Test
    void testActualizarMascotaExistente() {
        Mascota existente = new Mascota(1L, "Toby", "Perro", 3);
        Mascota datosNuevos = new Mascota(null, "Rocky", "Perro", 5);
        Mascota actualizada = new Mascota(1L, "Rocky", "Perro", 5);

        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(mascotaRepository.save(existente)).thenReturn(actualizada);

        Mascota resultado = mascotaService.actualizarMascota(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Rocky", resultado.getNombre());
        assertEquals("Perro", resultado.getTipo());
        assertEquals(5, resultado.getEdad());

        verify(mascotaRepository, times(1)).findById(1L);
        verify(mascotaRepository, times(1)).save(existente);
    }

    @Test
    void testActualizarMascotaNoExistente() {
        Mascota datosNuevos = new Mascota(null, "Ghost", "Gato", 2);

        when(mascotaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> mascotaService.actualizarMascota(99L, datosNuevos));

        assertEquals("No existe la mascota", exception.getMessage());

        verify(mascotaRepository, times(1)).findById(99L);
        verify(mascotaRepository, never()).save(any(Mascota.class));
    }

    @Test
    void testEliminarMascota() {
        doNothing().when(mascotaRepository).deleteById(1L);

        mascotaService.eliminarMascota(1L);

        verify(mascotaRepository, times(1)).deleteById(1L);
    }
}