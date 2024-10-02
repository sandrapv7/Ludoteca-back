package com.ccsw.tutorial.common.exceptions;

public abstract class Excepcio extends Exception {
    private String mensaje;

    public Excepcio() {
        super();
        this.mensaje = "Error";
    }

    public Excepcio(String s) {
        super(s);
        this.mensaje = s;
    }

    public String getMessage() {
        return mensaje;
    }
}
