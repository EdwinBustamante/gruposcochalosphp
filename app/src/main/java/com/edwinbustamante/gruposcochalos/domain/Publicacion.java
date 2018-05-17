package com.edwinbustamante.gruposcochalos.domain;

/**
 * Created by EDWIN on 8/5/2018.
 */

public class Publicacion {
    private String idpublicacion;
    private String descripcion;
    private String hora;
    private String foto;
    private String grupomusical_idgrupomusical;
    private String latitud;
    private String longitud;

    public Publicacion() {

    }

    public Publicacion(String idpublicacion, String descripcion, String hora, String foto, String grupomusical_idgrupomusical, String latitud, String longitud) {
        this.idpublicacion = idpublicacion;
        this.descripcion = descripcion;
        this.hora = hora;
        this.foto = foto;
        this.grupomusical_idgrupomusical = grupomusical_idgrupomusical;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getIdpublicacion() {
        return idpublicacion;
    }

    public void setIdpublicacion(String idpublicacion) {
        this.idpublicacion = idpublicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getGrupomusical_idgrupomusical() {
        return grupomusical_idgrupomusical;
    }

    public void setGrupomusical_idgrupomusical(String grupomusical_idgrupomusical) {
        this.grupomusical_idgrupomusical = grupomusical_idgrupomusical;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
