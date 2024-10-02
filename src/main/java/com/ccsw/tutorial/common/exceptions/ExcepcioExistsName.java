package com.ccsw.tutorial.common.exceptions;

public class ExcepcioExistsName extends Excepcio {

    private String mensaje;

    public ExcepcioExistsName() {
        super("Ya existe un cliente con ese nombre.\"");
        mensaje = "Ya existe un cliente con ese nombre.";
    }
}
