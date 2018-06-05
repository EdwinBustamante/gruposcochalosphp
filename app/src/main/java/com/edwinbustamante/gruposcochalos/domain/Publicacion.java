package com.edwinbustamante.gruposcochalos.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EDWIN on 8/5/2018.
 */

public class Publicacion {
    @SerializedName("idpublicacion")
    private String idpublicacion;
    private String descripcion;
    private String hora;
    private String foto;
    private String grupomusical_idgrupomusical;
    private String latitud;
    private String longitud;
    private String nombre;
    private String fotoperfil;

    public Publicacion() {

    }

    public Publicacion(String idpublicacion, String descripcion, String hora, String foto, String grupomusical_idgrupomusical, String latitud, String longitud, String nombre, String fotoperfil) {
        this.idpublicacion = idpublicacion;
        this.descripcion = descripcion;
        this.hora = hora;
        this.foto = foto;
        this.grupomusical_idgrupomusical = grupomusical_idgrupomusical;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.fotoperfil = fotoperfil;
    }

    public String getIdpublicacion() {
        return idpublicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getHora() {
        return hora;
    }

    public String getFoto() {
        return foto;
    }

    public String getGrupomusical_idgrupomusical() {
        return grupomusical_idgrupomusical;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFotoperfil() {
        return fotoperfil;
    }
}
