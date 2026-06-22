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
        u.setNo