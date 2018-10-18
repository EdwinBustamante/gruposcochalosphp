package com.edwinbustamante.gruposcochalos;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.VisitarMapa.MapsActivityGeolocalizar;
import com.edwinbustamante.gruposcochalos.domain.GrupoMusical;
import com.edwinbustamante.gruposcochalos.domain.Resultado;
import com.edwinbustamante.gruposcochalos.service.ItunesAPI;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;


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

        recyclerViewAdaptadorPrincipal = new RecyclerViewAdaptadorPrincipal(grupoMusicales, this);
        recyclerViewGrupos.setAdapter(recyclerViewAdaptadorPrincipal);
        Toolbar toolbarMain = (Toolbar) findViewById(R.id.toolbarm);
        setSupportActionBar(toolbarMain);



        /*try {
            Badges.setBadge(this, 0);
        } catch (BadgesNotSupportedException badgesNotSupportedException) {
            Log.d(TAG, badgesNotSupportedException.getMessage());
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Create background thread to connect and get data
            grupoMusicales.clear();
            obtenerCanciones();
        } else {
            Toast.makeText(this, "No tienes conexion a Internet", Toast.LENGTH_SHORT).show();
        }

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
                Log.e("error", t.getMessage());
                noHayConexion();
                t.printStackTrace();
            }
        });

    }


    private void noHayConexion() {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.recyclerGrupos), "Ups parece que no tienes conexion a internet o ocurrio algún problema en Grupos Cochalos?", Snackbar.LENGTH_INDEFINITE).setDuration(10000)
                .setAction("Reintentar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        grupoMusicales.clear();
                        obtenerCanciones();


                    }
                });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.search_main);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
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
            case R.id.grupos_cerca_de_mi:
                Intent cerca_de_mi = new Intent(Main.this, MapsActivityGeolocalizar.class);
                startActivity(cerca_de_mi);
                break;


        }

        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                recyclerViewAdaptadorPrincipal.getFilter().filter(newText);
                return true;
            }
        });
    }

}
