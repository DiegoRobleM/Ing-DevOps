package com.devops.springboot_app_devops.controller;

import com.devops.springboot_app_devops.exception.RecursoNoEncontradoException;
import com.devops.springboot_app_devops.model.Usuario;
import com.devops.springboot_app_devops.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba de la capa web (controller) de forma aislada: Spring solo levanta
 * el contexto MVC, sin base de datos, y el UsuarioService se simula.
 */
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_devuelve200ConListaDeUsuarios() throws Exception {
        Usuario usuario = new Usuario(1L, "Diego", "diego@duocuc.cl");
        when(usuarioService.listar()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Diego"));
    }

    @Test
    void obtenerUsuario_existente_devuelve200() throws Exception {
        Usuario usuario = new Usuario(1L, "Diego", "diego@duocuc.cl");
        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("diego@duocuc.cl"));
    }

    @Test
    void obtenerUsuario_inexistente_devuelve404() throws Exception {
        when(usuarioService.obtenerPorId(99L))
                .thenThrow(new RecursoNoEncontradoException("Usuario no encontrado con id 99"));

        mockMvc.perform(get("/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_devuelveUsuarioCreado() throws Exception {
        Usuario nuevo = new Usuario(null, "Diego", "diego@duocuc.cl");
        Usuario guardado = new Usuario(1L, "Diego", "diego@duocuc.cl");
        when(usuarioService.crear(any(Usuario.class))).thenReturn(guardado);

        mockMvc.perform(post("/usuarios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminar_devuelve200() throws Exception {
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isOk());
    }
}
