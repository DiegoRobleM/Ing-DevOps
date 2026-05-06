package com.devops.springboot_app_devops.controller;

import com.devops.springboot_app_devops.model.Usuario;
import com.devops.springboot_app_devops.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar(){
        return this.usuarioService.listar();
    }

    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable Long id){
        return this.usuarioService.obtenerPorId(id);
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario){
        return this.usuarioService.crear(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario){
        return this.usuarioService.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        this.usuarioService.eliminar(id);
    }


}
