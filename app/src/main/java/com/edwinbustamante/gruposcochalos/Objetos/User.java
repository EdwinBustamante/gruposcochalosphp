package com.edwinbustamante.gruposcochalos.Objetos;

import java.io.Serializable;

/**
 * Created by EDWIN on 24/4/2018.
 */

public class User implements Serializable {
    private int idusuario;
    private String nombre;
    private String user;
    private String genero;
    private boolean estado;
    private int perfil;

    public User(int idusuario, String nombre, String user, String genero, boolean estado, int perfil) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.user = user;
        this.genero = genero;
        this.estado = estado;
        this.perfil = perfil;
    }

    public User() {

    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }
}
