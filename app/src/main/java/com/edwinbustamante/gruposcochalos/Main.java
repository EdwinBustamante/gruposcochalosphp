package com.edwinbustamante.gruposcochalos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.domain.GrupoMusical;
import com.edwinbustamante.gruposcochalos.domain.Resultado;
import com.edwinbustamante.gruposcochalos.service.ItunesAPI;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {

    List<GrupoMusical> grupoMusicales = new ArrayList<>();
    private RecyclerViewAdaptadorPrincipal recyclerViewAdaptadorPrincipal;
    private RecyclerView recyclerViewGrupos;
    private RecyclerView.LayoutManager layoutManager;
    private String TAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        recyclerViewGrupos = (RecyclerView) findViewById(R.id.recyclerGrupos);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewGrupos.setLayoutManager(layoutManager);

        recyclerViewAdaptadorPrincipal = new RecyclerViewAdaptadorPrincipal(grupoMusicales,this);
        recyclerViewGrupos.setAdapter(recyclerViewAdaptadorPrincipal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        grupoMusicales.clear();
        obtenerCanciones();
    }

    public void obtenerGruposMusicales() {
//        List<GrupoMusical> listagrupos = new ArrayList<>();
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "BONANZA", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "PANDORA DE COLOMI", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "BONANZA", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "PANDORA DE COLOMI", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "BONANZA", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "PANDORA DE COLOMI", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "BONANZA", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "PANDORA DE COLOMI", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        listagrupos.add(new GrupoMusical(R.drawable.portada, "MISION RESCATE", "Cumbia Tropical"));
//        return listagrupos;

    }

    private void obtenerCanciones() {
        Call<Resultado> resultadoCall = ItunesAPI.getItunesService().getCanciones();

        resultadoCall.enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                Resultado body = response.body();
                List<GrupoMusical> results = body.getListagrupos();

                grupoMusicales.addAll(results);
                recyclerViewAdaptadorPrincipal.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {
                Toast.makeText(Main.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                Log.e("error",t.getMessage());
                t.printStackTrace();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.ingresar_al_sistema:

                Intent ingresar = new Intent(Main.this, LoginActivity.class);
                startActivity(ingresar);

                break;


        }

        return true;
    }


}
