package com.edwinbustamante.gruposcochalos.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 22/2/2018.
 */

public class GrupoMusical implements Parcelable {
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

    protected GrupoMusical(Parcel in) {
        nombre = in.readString();
        fotoperfil = in.readString();
        genero = in.readString();
        trackName = in.readString();
        collectionName = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeString(this.nombre);
      parcel.writeString(this.fotoperfil);
      parcel.writeString(this.genero);
    }
}
