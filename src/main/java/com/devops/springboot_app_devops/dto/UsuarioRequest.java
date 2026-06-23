package com.devops.springboot_app_devops.dto;

/**
 * DTO usado como cuerpo de las peticiones POST/PUT de /usuarios.
 * No expone el id ni nada propio de la entidad JPA: asi el cliente
 * nunca puede mandar un id arbitrario (evita el problema de seguridad
 * "mass assignment" que marco SonarCloud sobre Usuario como @RequestBody).
 */
public class UsuarioRequest {

    private String nombre;
    private String email;

    public UsuarioRequest() {}

    public UsuarioRequest(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
