package com.edwinbustamante.gruposcochalos.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Admin on 22/2/2018.
 */
@Entity(tableName = "favoritos")//nombre para la base de datos
public class GrupoMusical implements Parcelable {//pasar objeto por parcelable y Serializable se puede pasar
    //primary key para la bse de datos interna

    @PrimaryKey(autoGenerate = true)///que sea autoincrementable
    @NonNull
    private Long grupopk;

    private String idgrupomusical;
    private String nombre;
    private String genero;
    private String informaciondescripcion;
    private String descripcioncontactos;
    private String direcciondescripcion;
    private String numtelefono;
    private String numwhatsapp;
    private String linkfacebook;
    private String fotoperfil;
    private String usuario_idusuario;
    private String numtelefonodos;
    private String latitud;
    private String longitud;

    @NonNull
    public Long getGrupopk() {
        return grupopk;
    }

    public void setGrupopk(@NonNull Long grupopk) {
        this.grupopk = grupopk;
    }

    public GrupoMusical(String idgrupomusical, String nombre, String genero, String informaciondescripcion, String descripcioncontactos, String direcciondescripcion, String numtelefono, String numwhatsapp, String linkfacebook, String fotoperfil, String usuario_idusuario, String numtelefonodos, String latitud, String longitud) {
        this.idgrupomusical = idgrupomusical;
        this.nombre = nombre;
        this.genero = genero;
        this.informaciondescripcion = informaciondescripcion;
        this.descripcioncontactos = descripcioncontactos;
        this.direcciondescripcion = direcciondescripcion;
        this.numtelefono = numtelefono;
        this.numwhatsapp = numwhatsapp;
        this.linkfacebook = linkfacebook;
        this.fotoperfil = fotoperfil;
        this.usuario_idusuario = usuario_idusuario;
        this.numtelefonodos = numtelefonodos;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    protected GrupoMusical(Parcel in) {
        idgrupomusical = in.readString();
        nombre = in.readString();
        genero = in.readString();
        informaciondescripcion = in.readString();
        descripcioncontactos = in.readString();
        direcciondescripcion = in.readString();
        numtelefono = in.readString();
        numwhatsapp = in.readString();
        linkfacebook = in.readString();
        fotoperfil = in.readString();
        usuario_idusuario = in.readString();
        numtelefonodos = in.readString();
        latitud = in.readString();
        longitud = in.readString();
    }

    public static final Creator<GrupoMusical> CREATOR = new Creator<GrupoMusical>() {
        @Override
        public GrupoMusical createFromParcel(Parcel in) {
            return new GrupoMusical(in);
        }

        @Override
        public GrupoMusical[] newArray(int size) {
            return new GrupoMusical[size];
        }
    };

    public String getIdgrupomusical() {
        return idgrupomusical;
    }

    public void setIdgrupomusical(String idgrupomusical) {
        this.idgrupomusical = idgrupomusical;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getInformaciondescripcion() {
        return informaciondescripcion;
    }

    public void setInformaciondescripcion(String informaciondescripcion) {
        this.informaciondescripcion = informaciondescripcion;
    }

    public String getDescripcioncontactos() {
        return descripcioncontactos;
    }

    public void setDescripcioncontactos(String descripcioncontactos) {
        this.descripcioncontactos = descripcioncontactos;
    }

    public String getDirecciondescripcion() {
        return direcciondescripcion;
    }

    public void setDirecciondescripcion(String direcciondescripcion) {
        this.direcciondescripcion = direcciondescripcion;
    }

    public String getNumtelefono() {
        return numtelefono;
    }

    public void setNumtelefono(String numtelefono) {
        this.numtelefono = numtelefono;
    }

    public String getNumwhatsapp() {
        return numwhatsapp;
    }

    public void setNumwhatsapp(String numwhatsapp) {
        this.numwhatsapp = numwhatsapp;
    }

    public String getLinkfacebook() {
        return linkfacebook;
    }

    public void setLinkfacebook(String linkfacebook) {
        this.linkfacebook = linkfacebook;
    }

    public String getFotoperfil() {
        return fotoperfil;
    }

    public void setFotoperfil(String fotoperfil) {
        this.fotoperfil = fotoperfil;
    }

    public String getUsuario_idusuario() {
        return usuario_idusuario;
    }

    public void setUsuario_idusuario(String usuario_idusuario) {
        this.usuario_idusuario = usuario_idusuario;
    }

    public String getNumtelefonodos() {
        return numtelefonodos;
    }

    public void setNumtelefonodos(String numtelefonodos) {
        this.numtelefonodos = numtelefonodos;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.idgrupomusical);
        parcel.writeString(this.nombre);
        parcel.writeString(this.genero);
        parcel.writeString(this.informaciondescripcion);
        parcel.writeString(this.descripcioncontactos);
        parcel.writeString(this.direcciondescripcion);
        parcel.writeString(this.numtelefono);
        parcel.writeString(this.numwhatsapp);
        parcel.writeString(this.linkfacebook);
        parcel.writeString(this.fotoperfil);
        parcel.writeString(this.usuario_idusuario);
        parcel.writeString(this.numtelefonodos);
        parcel.writeString(this.latitud);
        parcel.writeString(this.longitud);

    }


}
