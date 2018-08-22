package com.edwinbustamante.gruposcochalos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by EDWIN on 16/8/2018.
 */

public class Validar {
    public Validar() {
    }
    public boolean validarPassword(String cadena) {
        Pattern pat = Pattern.compile("[a-zA-Z0-9]+");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }

    public boolean validarUsuario(String cadena) {
        Pattern pat = Pattern.compile("[a-zA-Z0-9]+");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }
    public boolean validarInstrumento(String cadena) {
        Pattern pat = Pattern.compile("[a-zA-Z0-9]+");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }
}
