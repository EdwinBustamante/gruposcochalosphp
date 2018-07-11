package com.edwinbustamante.gruposcochalos.Administrador;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.domain.ResultadoAdministrador;
import com.edwinbustamante.gruposcochalos.domain.Usuario;
import com.edwinbustamante.gruposcochalos.service.AdministradorApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainAdministrador extends AppCompatActivity {

    List<Usuario> usuarioLista = new ArrayList<>();
    private AdaptadorAdministrador adaptadorAdministrador;
    private RecyclerView recyclerViewUsuarios;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrador);
        recyclerViewUsuarios = (RecyclerView) findViewById(R.id.recyclerAdministrar);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewUsuarios.setLayoutManager(layoutManager);

       // usuarioLista.add(new Usuario("1", "mision Rescate", "345667", "1234567", "0", "teclados", "a"));
       // usuarioLista.add(new Usuario("1", "Sinai", "345667", "1234567", "0", "teclados", "a"));

        adaptadorAdministrador = new AdaptadorAdministrador(usuarioLista, this);
        recyclerViewUsuarios.setAdapter(adaptadorAdministrador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbara);
        toolbar.setTitle("Administrar Grupos");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Create background thread to connect and get data
            usuarioLista.clear();
            obtenerUsuarios();
        } else {
            Toast.makeText(this, "No tienes conexion a Internet", Toast.LENGTH_SHORT).show();
        }

    }



    private void obtenerUsuarios() {
        Call<ResultadoAdministrador> resultadoCall = AdministradorApi.getUsuariosService().getUsuarios();

        resultadoCall.enqueue(new Callback<ResultadoAdministrador>() {
            @Override
            public void onResponse(Call<ResultadoAdministrador> call, Response<ResultadoAdministrador> response) {
                ResultadoAdministrador body = response.body();
                List<Usuario> results = body.getListausuarios();

                usuarioLista.addAll(results);
                adaptadorAdministrador.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResultadoAdministrador> call, Throwable t) {
                Toast.makeText(MainAdministrador.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
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
                        usuarioLista.clear();
                        obtenerUsuarios();


                    }
                });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_administrador, menu);
        MenuItem search = menu.findItem(R.id.search_administrador);
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

            case R.id.salir_del_administrador:
                finish();
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

                adaptadorAdministrador.getFilter().filter(newText);
                return true;
            }
        });
    }

}
