package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.ImagenFull.FulImagen;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.Objetos.Publicacion;
import com.edwinbustamante.gruposcochalos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PublicacionesProfileFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {


    String FileName = "myUserId";
    Button publicar;
    private LinearLayout editMainCuenta;
    RequestQueue rq;
    JsonRequest jrq;
    String idUsuarioInput;
    //A CONTINUACION SE DECLARA LAS VARIABLES DEL RECYCLERVIEW
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public PublicacionesProfileFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_publicaciones_profile, container, false);
        rq = Volley.newRequestQueue(getActivity());
        //ANIMACION DEL FONDO
        //  linearLayoutanimacion = (RelativeLayout) findViewById(R.id.profile_layout);
        // animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        //animacion.setEnterFadeDuration(4500);
        //animacion.setExitFadeDuration(4500);
        //animacion.start();

        //progressDialogFotoSubir = new ProgressDialog(this);

        String defaultValue = "DefaultName";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        idUsuarioInput = sharedPreferences.getString("idusuario", defaultValue);
      //  Toast.makeText(getContext(), idUsuarioInput, Toast.LENGTH_SHORT).show();
        //     actualizarDatosUsuario();
        publicar = (Button) vista.findViewById(R.id.buttonPublicar);
        publicar.setOnClickListener(this);
     /*
     LA CONFIGURACION DEL RECYCLER VIEW
      */
        mRecyclerView = (RecyclerView) vista.findViewById(R.id.my_recycler_view_publicar);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapterPublicar(obtenerListaPublicaciones());
        mRecyclerView.setAdapter(mAdapter);
        return vista;
    }

    public List<Publicacion> obtenerListaPublicaciones() {
        List<Publicacion> listapublicacion = new ArrayList<>();
        listapublicacion.add(new Publicacion(R.drawable.portada, R.drawable.portada, "Nombre Grupo Musical", "la fecha de publicacion es",
                "HOy estamos en tiraque en un evento musical no falten"));
        listapublicacion.add(new Publicacion(R.drawable.portada, R.drawable.portada, "Nombre Grupo Musical", "la fecha de publicacion es",
                "HOy estamos en tiraque en un evento musical no falten"));
        listapublicacion.add(new Publicacion(R.drawable.portada, R.drawable.portada, "Nombre Grupo Musical", "la fecha de publicacion es",
                "HOy estamos en tiraque en un evento musical no falten"));
        return listapublicacion;
    }

    private void actualizarDatosUsuario() {
        String url = Constantes.IP_SERVIDOR+"gruposcochalos/traerdatosusuario.php?idusuario=" + idUsuarioInput;
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPublicar:
                Intent intent = new Intent(getActivity(), Publicar.class);
                startActivity(intent);
            default:
                break;
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("usuariodatos");//datos esta en el php
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.getString("idgrupomusical").equals("datos incorrectos de entrada")) {
                Toast.makeText(getContext(), "Datos incorrectos de entrada", Toast.LENGTH_SHORT).show();
            } else {
                if (jsonObject.getString("idgrupomusical").equals("error en la consulta")) {
                    Toast.makeText(getContext(), "Fallo al actualizar los datos intenete nuevamente", Toast.LENGTH_SHORT).show();
                } else {

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}