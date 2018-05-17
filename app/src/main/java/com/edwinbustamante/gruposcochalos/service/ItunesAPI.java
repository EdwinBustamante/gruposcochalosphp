package com.edwinbustamante.gruposcochalos.service;

import com.edwinbustamante.gruposcochalos.domain.Resultado;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class ItunesAPI {
    private static final String url = "http://192.168.1.9/gruposcochalos/";

    public static ItunesService itunesService = null;

    public static ItunesService getItunesService() {
        if (itunesService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            itunesService = retrofit.create(ItunesService.class);

        }
        return itunesService;
    }

    public interface ItunesService {
        @GET("listargrupos.php")
        Call<Resultado> getCanciones();
    }

}