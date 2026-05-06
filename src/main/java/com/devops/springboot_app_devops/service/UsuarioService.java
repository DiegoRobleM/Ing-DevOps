package com.devops.springboot_app_devops.service;

import com.devops.springboot_app_devops.model.Usuario;

import java.util.List;

public interface UsuarioService {

    List<Usuario> listar();

    Usuario obtenerPorId(Long id);

    Usuario crear(Usuario usuario);

    Usuario actualizar(Long id, Usuario usuario);

    void eliminar(Long id);
}
