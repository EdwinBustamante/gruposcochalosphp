package com.edwinbustamante.gruposcochalos.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EDWIN on 9/7/2018.
 */

public class Usuario {
    @SerializedName("idusuario")
    private String id;
    @SerializedName("usuario")
    private String usuario;
    @SerializedName("nombre")
    private String nombreGrupoA;
    @SerializedName("pwd")
    private String pwd;
    @SerializedName("estado")
    private String estado;
    @SerializedName("instrumento")
    private String instrumento;
    @SerializedName("rol")
    private String rol;

    public String getNombreGrupoA() {
        return nombreGrupoA;
    }

    public void setNombreGrupoA(String nombreGrupoA) {
        this.nombreGrupoA = nombreGrupoA;
    }

    public Usuario(String id, String nombreGrupoA, String usuario, String pwd, String estado, String instrumento, String rol) {
        this.id = id;
        this.nombreGrupoA = nombreGrupoA;
        this.usuario = usuario;
        this.pwd = pwd;
        this.estado = estado;
        this.instrumento = instrumento;
        this.rol = rol;
    }

    public Usuario() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
