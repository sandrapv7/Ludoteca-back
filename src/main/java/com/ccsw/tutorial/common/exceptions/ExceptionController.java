package com.ccsw.tutorial.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author sandra
 * Clase encargada de manejar las excepciones globales de la aplici0n.
 */
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    /**
     * Metodo que maneja todas las excepciones de tipo Exception.
     * Cuando se lanza una excepcion en la aplicacion, este metodo se encarga de crear una respuesta HTTP
     * con un codigo de estado 500 y el mensaje de la excepcion.
     * @param exception La excepcion que se ha lanzado.
     * @return Respuesta HTTP con el código de estado y el mensaje de la excepcion.
     */

    @ExceptionHandler(Exceptions.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
