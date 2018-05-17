package com.edwinbustamante.gruposcochalos.service;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.domain.ResultadoPublicacion;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class PublicacionAPI {
    private static final String url = Constantes.IP_SERVIDOR + "gruposcochalos/";

    public static PublicacionService publicacionService = null;

    public static PublicacionService getPublicacionService() {
        if (publicacionService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            publicacionService = retrofit.create(PublicacionService.class);

        }
        return publicacionService;
    }

    public interface PublicacionService {
        @GET("listarpublicacionesusuario.php")
        Call<ResultadoPublicacion> getCanciones();
    }

}