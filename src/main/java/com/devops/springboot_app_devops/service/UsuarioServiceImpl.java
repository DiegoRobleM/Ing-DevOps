package com.devops.springboot_app_devops.service;

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
        return repo.findById(id).orElse(null);
    }

    @Override
    public Usuario crear(Usuario usuario) {
        return repo.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario u = repo.findById(id).orElse(null);
        if (u != null) {
            u.setNombre(usuario.getNombre());
            u.setEmail(usuario.getEmail());
            return repo.save(u);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
