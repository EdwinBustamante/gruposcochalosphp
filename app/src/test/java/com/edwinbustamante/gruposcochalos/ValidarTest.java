package com.edwinbustamante.gruposcochalos;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by EDWIN on 16/8/2018.
 */
public class ValidarTest {

   private Validar validarObjeto;
   @Before
   public void setUp(){
       validarObjeto= new Validar();
    }
    @Test
    public void validarNotNull(){
       assertNotNull(validarObjeto);
    }

    @Test
    public void validarPassword() throws Exception {
    assertEquals(false,validarObjeto.validarPassword("migatitonegro*"));
    }
    @Test
    public void validarPassword2() throws Exception {
        assertEquals(true,validarObjeto.validarPassword("migationegro"));
    }

    @Test
    public void validarUsuario() throws Exception {
        assertEquals(true,validarObjeto.validarUsuario("75495889"));
    }
    @Test
    public void validarUsuario2() throws Exception {
        assertEquals(true,validarObjeto.validarUsuario("edwincito"));
    }
    @Test
    public void validarUsuario3() throws Exception {
        assertEquals(false,validarObjeto.validarUsuario("edwincito.#"));
    }
    @Test
    public void validarInstrumento() throws Exception {
        assertEquals(true,validarObjeto.validarUsuario("teclados"));
    }


}