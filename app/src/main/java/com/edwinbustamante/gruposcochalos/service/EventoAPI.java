package com.edwinbustamante.gruposcochalos.service;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.domain.ResultadoEvento;
import com.edwinbustamante.gruposcochalos.domain.ResultadoPublicacion;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class EventoAPI {
    private static final String url = Constantes.IP_SERVIDOR + "gruposcochalos/";

    public static EventoService eventoService = null;

    public static EventoService getEventoService() {
        if (eventoService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            eventoService = retrofit.create(EventoService.class);

        }
        return eventoService;
    }

    public interface EventoService {
        @GET("listaragenda.php")
        Call<ResultadoEvento> getAgenda(@Query("idgrupomusical") String idgrupomusical);
    }

}