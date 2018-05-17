package com.edwinbustamante.gruposcochalos.domain;

/**
 * Created by Admin on 22/2/2018.
 */

public class
GrupoMusical {
    private String nombre;
    private String fotoperfil;
    private String genero;

    public GrupoMusical() {
    }

    public GrupoMusical(String urlFotoPortada, String nombreGrupoMusical, String tipoDeMusica) {
        this.nombre = nombreGrupoMusical;
        this.fotoperfil = urlFotoPortada;
        genero = tipoDeMusica;
    }

    private String trackName;
    private String collectionName;

    public String getTrackName() {
        return trackName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoperfil() {
        return fotoperfil;
    }

    public void setFotoperfil(String fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
