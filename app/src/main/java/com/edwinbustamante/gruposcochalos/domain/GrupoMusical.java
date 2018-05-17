package com.edwinbustamante.gruposcochalos.domain;

/**
 * Created by Admin on 22/2/2018.
 */

public class
GrupoMusical {
    private String nombre;
    private String fotoperil;
    private String genero;

    public GrupoMusical() {
    }

    public GrupoMusical(String urlFotoPortada, String nombreGrupoMusical, String tipoDeMusica) {
        this.nombre = nombreGrupoMusical;
        this.fotoperil = urlFotoPortada;
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

    public String getFotoperil() {
        return fotoperil;
    }

    public void setFotoperil(String fotoperil) {
        this.fotoperil = fotoperil;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
