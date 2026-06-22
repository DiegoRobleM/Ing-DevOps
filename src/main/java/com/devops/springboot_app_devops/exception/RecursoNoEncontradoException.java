package com.devops.springboot_app_devops.exception;

/**
 * Se lanza cuando se busca un Usuario por id y no existe en la base de datos.
 * El GlobalExceptionHandler la traduce a una respuesta HTTP 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
