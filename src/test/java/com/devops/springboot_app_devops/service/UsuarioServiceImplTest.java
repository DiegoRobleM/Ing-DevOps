package com.devops.springboot_app_devops.service;

import com.devops.springboot_app_devops.exception.RecursoNoEncontradoException;
import com.devops.springboot_app_devops.model.Usuario;
import com.devops.springboot_app_devops.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de la capa de servicio, sin levantar el contexto de
 * Spring ni una base de datos real: el repositorio se simula con Mockito.
 * Estas pruebas son las que alimentan el reporte de cobertura de JaCoCo
 * que luego analiza SonarCloud en el pipeline (Pasos 4, 5 y 6 de la guia).
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository repo;

    @InjectMocks
    private UsuarioServiceImpl service;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "Diego", "diego@duocuc.cl");
    }

    @Test
    void listar_devuelveTodosLosUsuarios() {
        when(repo.findAll()).thenReturn(List.of(usuario));

        List<Usuario> resultado = service.listar();

        assertThat(resultado).hasSize(1).containsExactly(usuario);
        verify(repo, times(1)).findAll();
    }

    @Test
    void obtenerPorId_existente_devuelveUsuario() {
        when(repo.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = service.obtenerPorId(1L);

        assertThat(resultado.getNombre()).isEqualTo("Diego");
    }

    @Test
    void obtenerPorId_inexistente_lanzaExcepcion() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void crear_guardaYDevuelveUsuario() {
        when(repo.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = service.crear(new Usuario(null, "Diego", "diego@duocuc.cl"));

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repo).save(any(Usuario.class));
    }

    @Test
    void actualizar_existente_modificaYGuarda() {
        Usuario cambios = new Usuario(null, "Diego Roble", "nuevo@duocuc.cl");
        when(repo.findById(1L)).thenReturn(Optional.of(usuario));
        when(repo.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = service.actualizar(1L, cambios);

        assertThat(resultado.getNombre()).isEqualTo("Diego Roble");
        assertThat(resultado.getEmail()).isEqualTo("nuevo@duocuc.cl");
    }

    @Test
    void actualizar_inexistente_lanzaExcepcion() {
        when(repo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(5L, usuario))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void eliminar_existente_invocaDeleteById() {
        when(repo.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaExcepcionYNoBorra() {
        when(repo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);
        verify(repo, never()).deleteById(anyLong());
    }
}
