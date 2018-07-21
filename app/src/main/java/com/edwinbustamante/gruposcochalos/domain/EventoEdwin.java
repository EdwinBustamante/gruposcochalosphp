package com.edwinbustamante.gruposcochalos.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EDWIN on 8/5/2018.
 */

public class EventoEdwin {
    //@SerializedName("idpublicacion")
    private String idevento;
    private String titulo;
    private String color;
    private String fechai;
    private String mesi;
    private String anioi;
    private String horai;
    private String minutoi;
    private String fechaf;
    private String mesf;
    private String aniof;
    private String horaf;
    private String minutof;
    private String grupomusical_idgrupomusical;

    public EventoEdwin(String idevento, String titulo, String color, String fechai, String mesi, String anioi, String horai, String minutoi, String fechaf, String mesf, String aniof, String horaf, String minutof, String grupomusical_idgrupomusical) {
        this.idevento = idevento;
        this.titulo = titulo;
        this.color = color;
        this.fechai = fechai;
        this.mesi = mesi;
        this.anioi = anioi;
        this.horai = horai;
        this.minutoi = minutoi;
        this.fechaf = fechaf;
        this.mesf = mesf;
        this.aniof = aniof;
        this.horaf = horaf;
        this.minutof = minutof;
        this.grupomusical_idgrupomusical = grupomusical_idgrupomusical;
    }

    public EventoEdwin() {

    }

    public String getIdevento() {
        return idevento;
    }

    public void setIdevento(String idevento) {
        this.idevento = idevento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFechai() {
        return fechai;
    }

    public void setFechai(String fechai) {
        this.fechai = fechai;
    }

    public String getMesi() {
        return mesi;
    }

    public void setMesi(String mesi) {
        this.mesi = mesi;
    }

    public String getAnioi() {
        return anioi;
    }

    public void setAnioi(String anioi) {
        this.anioi = anioi;
    }

    public String getHorai() {
        return horai;
    }

    public void setHorai(String horai) {
        this.horai = horai;
    }

    public String getMinutoi() {
        return minutoi;
    }

    public void setMinutoi(String minutoi) {
        this.minutoi = minutoi;
    }

    public String getFechaf() {
        return fechaf;
    }

    public void setFechaf(String fechaf) {
        this.fechaf = fechaf;
    }

    public String getMesf() {
        return mesf;
    }

    public void setMesf(String mesf) {
        this.mesf = mesf;
    }

    public String getAniof() {
        return aniof;
    }

    public void setAniof(String aniof) {
        this.aniof = aniof;
    }

    public String getHoraf() {
        return horaf;
    }

    public void setHoraf(String horaf) {
        this.horaf = horaf;
    }

    public String getMinutof() {
        return minutof;
    }

    public void setMinutof(String minutof) {
        this.minutof = minutof;
    }

    public String getGrupomusical_idgrupomusical() {
        return grupomusical_idgrupomusical;
    }

    public void setGrupomusical_idgrupomusical(String grupomusical_idgrupomusical) {
        this.grupomusical_idgrupomusical = grupomusical_idgrupomusical;
    }
}
