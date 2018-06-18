package com.edwinbustamante.gruposcochalos.domain;

/**
 * Created by EDWIN on 8/6/2018.
 */

public class Agenda {
    private String idgrupomusical;
    private String anio;
    private String mes;
    private String dia;

    public Agenda(String idgrupomusical, String anio, String mes, String dia) {
        this.idgrupomusical = idgrupomusical;
        this.anio = anio;
        this.mes = mes;
        this.dia = dia;
    }

    public String getIdgrupomusical() {
        return idgrupomusical;
    }

    public void setIdgrupomusical(String idgrupomusical) {
        this.idgrupomusical = idgrupomusical;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
