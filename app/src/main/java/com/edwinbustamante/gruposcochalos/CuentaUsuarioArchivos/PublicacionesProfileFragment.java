package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;

import com.edwinbustamante.gruposcochalos.domain.Publicacion;
import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.domain.ResultadoPublicacion;
import com.edwinbustamante.gruposcochalos.service.PublicacionAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;


public class PublicacionesProfileFragment extends Fragment implements View.OnClickListener {


    String FileName = "myUserId";
    String FileNameGrupo = "IdGrupo";
    private LinearLayout editMainCuenta;
    String idUsuarioInput;

    //A CONTINUACION SE DECLARA LAS VARIABLES DEL RECYCLERVIEW
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Publicacion> publicacionlista = new ArrayList<>();

    public PublicacionesProfileFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_publicaciones_profile, container, false);
        //ANIMACION DEL FONDO
        //  linearLayoutanimacion = (RelativeLayout) findViewById(R.id.profile_layout);
        // animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        //animacion.setEnterFadeDuration(4500);
        //animacion.setExitFadeDuration(4500);
        //animacion.start();

        //progressDialogFotoSubir = new ProgressDialog(this);

        //     actualizarDatosUsuario();
        /*
     LA CONFIGURACION DEL RECYCLER VIEW
      */
        mRecyclerView = (RecyclerView) vista.findViewById(R.id.my_recycler_view_publicar);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        publicacionlista.clear();

        mAdapter = new MyAdapterPublicar(publicacionlista, this.getContext(), "administrador");
        mRecyclerView.setAdapter(mAdapter);
        String defaultValue = "DefaultName";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        idUsuarioInput = sharedPreferences.getString("idusuario", defaultValue);
        return vista;
    }


    @Override
    public void onStart() {
        super.onStart();

        String defaultValue = "DefaultName";
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("estado", Context.MODE_PRIVATE);
        String bandera = sharedPreferences.getString("estado", defaultValue);
        if (bandera.equals("1")) {
            obtenerPublicaciones();
        }


    }


    private void obtenerPublicaciones() {
        String defaultValues = "DefaultName";
        SharedPreferences sharedPreferencess = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
        String idGrupoMusical = sharedPreferencess.getString("idgrupomusical", defaultValues);
        // Toast.makeText(getContext(), idGrupoMusical, Toast.LENGTH_SHORT).show();

        Call<ResultadoPublicacion> resultadoPublicacionCall = PublicacionAPI.getPublicacionService().getCanciones(idGrupoMusical);//lacemos la llamada al archivo php
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
                Toast.makeText(getContext(), "Ha ocurrido un error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error", t.getMessage());
                t.printStackTrace();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }


}