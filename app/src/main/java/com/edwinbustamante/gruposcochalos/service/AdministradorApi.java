package com.edwinbustamante.gruposcochalos.service;

import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.domain.Resultado;
import com.edwinbustamante.gruposcochalos.domain.ResultadoAdministrador;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class AdministradorApi {
    private static final String url = Constantes.IP_SERVIDOR + "gruposcochalos/";

    public static UsuariosService usuariosService = null;

    public static UsuariosService getUsuariosService() {
        if (usuariosService== null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            usuariosService = retrofit.create(UsuariosService.class);

        }
        return usuariosService;
    }

    public interface UsuariosService {
        @GET("listargruposadministrar.php")
        Call<ResultadoAdministrador> getUsuarios();
    }

}