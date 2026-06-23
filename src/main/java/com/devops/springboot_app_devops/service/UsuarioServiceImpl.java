package com.devops.springboot_app_devops.service;

import com.devops.springboot_app_devops.exception.RecursoNoEncontradoException;
import com.devops.springboot_app_devops.model.Usuario;
import com.devops.springboot_app_devops.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repo;

    public UsuarioServiceImpl(UsuarioRepository repo){
        this.repo = repo;
    }

    @Override
    public List<Usuario> listar() {
        return this.repo.findAll();
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        return repo.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id " + id));
        u.setNombre(usuario.getNombre());
        u.setEmail(usuario.getEmail());
        return repo.save(u);
    }

    @Override
    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con id " + id);
        }
        repo.deleteById(id);
    }

    // DEMO temporal para evidencia 6.2 (Quality Gate): código nuevo sin
    // ninguna prueba, para que la cobertura de "new code" caiga bajo el
    // 80% que exige el Quality Gate por defecto de SonarCloud.
    // Eliminar este método despues de capturar la evidencia.
    public String clasificarDominioEmail(String email) {
        if (email == null || email.isBlank()) {
            return "DESCONOCIDO";
        } else if (email.endsWith("@duocuc.cl")) {
            return "INSTITUCIONAL";
        } else if (email.endsWith("@gmail.com")) {
            return "PERSONAL";
        } else if (email.contains("@")) {
            return "EXTERNO";
        } else {
            return "INVALIDO";
        }
    }

    // DEMO temporal para evidencia 6.2 (Quality Gate): segundo método nuevo
    // sin pruebas. SonarCloud omite la condición de cobertura cuando el
    // código nuevo tiene menos de 20 líneas, así que este método extra
    // asegura superar ese umbral. Eliminar junto con el método anterior.
    public String validarFormatoUsuario(Usuario usuario) {
        if (usuario == null) {
            return "USUARIO_NULO";
        } else if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            return "NOMBRE_INVALIDO";
        } else if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
            return "EMAIL_INVALIDO";
        } else {
            return "VALIDO";
        }
    }
}
