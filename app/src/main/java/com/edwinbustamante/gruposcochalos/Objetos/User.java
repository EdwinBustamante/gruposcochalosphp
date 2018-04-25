package com.edwinbustamante.gruposcochalos.Objetos;

/**
 * Created by EDWIN on 24/4/2018.
 */

public class User {
    private String correo, contrasenia, genero;

    public String getCorreo() {
        return correo;
    }

    public User() {

    }

    public User(String correo, String contrasenia, String genero) {
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.genero = genero;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
