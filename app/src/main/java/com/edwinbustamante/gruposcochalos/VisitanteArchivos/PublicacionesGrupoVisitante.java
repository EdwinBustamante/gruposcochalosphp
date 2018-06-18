package com.edwinbustamante.gruposcochalos.VisitanteArchivos;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos.MyAdapterPublicar;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.domain.Publicacion;
import com.edwinbustamante.gruposcochalos.domain.ResultadoPublicacion;
import com.edwinbustamante.gruposcochalos.service.PublicacionAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class PublicacionesGrupoVisitante extends AppCompatActivity {

    //A CONTINUACION SE DECLARA LAS VARIABLES DEL RECYCLERVIEW
    private RecyclerView mRecyclerViewVPublicaciones;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Publicacion> publicacionlista = new ArrayList<>();
    private String idgrupomusical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones_grupo_visitante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarvisitarp);
        toolbar.setTitle("Publicaciones");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        idgrupomusical = (String) getIntent().getExtras().get("idgrupomusical");
        mRecyclerViewVPublicaciones = (RecyclerView) findViewById(R.id.my_recycler_view_visitar_publicaciones);
        mLayoutManager = new LinearLayoutManager(PublicacionesGrupoVisitante.this);
        mRecyclerViewVPublicaciones.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapterPublicar(publicacionlista, PublicacionesGrupoVisitante.this,"visitante");
        mRecyclerViewVPublicaciones.setAdapter(mAdapter);

        obtenerPublicaciones();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.guardar_informacion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    private void obtenerPublicaciones() {

        Call<ResultadoPublicacion> resultadoPublicacionCall = PublicacionAPI.getPublicacionService().getCanciones(idgrupomusical);//hacemos la llamada al archivo php para traeer kas publicaciones
        resultadoPublicacionCall.enqueue(new Callback<ResultadoPublicacion>() {
            @Override
            public void onResponse(Call<ResultadoPublicacion> call, retrofit2.Response<ResultadoPublicacion> response) {
                ResultadoPublicacion body = response.body();
                List<Publicacion> results = body.getListapublicacion();
                publicacionlista.clear();
                publicacionlista.addAll(results);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResultadoPublicacion> call, Throwable t) {
                Toast.makeText(PublicacionesGrupoVisitante.this, "Ha ocurrido un error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error", t.getMessage());
                t.printStackTrace();
            }
        });

    }

}
