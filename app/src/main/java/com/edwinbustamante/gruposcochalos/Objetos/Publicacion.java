package com.edwinbustamante.gruposcochalos.Objetos;

/**
 * Created by EDWIN on 8/5/2018.
 */

public class Publicacion {
    private int fotoPerfil;
    private int fotoPublicacion;
    private String nombreGrupo;
    private String fechaPublicacion;
    private String descripcionPublicacion;

    public Publicacion(int fotoPerfil, int fotoPublicacion, String nombreGrupo, String fechaPublicacion, String descripcionPublicacion) {
        this.fotoPerfil = fotoPerfil;
        this.fotoPublicacion = fotoPublicacion;
        this.nombreGrupo = nombreGrupo;
        this.fechaPublicacion = fechaPublicacion;
        this.descripcionPublicacion = descripcionPublicacion;
    }

    public int getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public int getFotoPublicacion() {
        return fotoPublicacion;
    }

    public void setFotoPublicacion(int fotoPublicacion) {
        this.fotoPublicacion = fotoPublicacion;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getDescripcionPublicacion() {
        return descripcionPublicacion;
    }

    public void setDescripcionPublicacion(String descripcionPublicacion) {
        this.descripcionPublicacion = descripcionPublicacion;
    }
}
