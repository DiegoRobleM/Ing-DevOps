package com.devops.springboot_app_devops.repository;

import com.devops.springboot_app_devops.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
