package com.edwinbustamante.gruposcochalos;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class InformacionProfileFragment extends Fragment {

    // UI references. animacion
    RelativeLayout linearLayoutanimacion;
    AnimationDrawable animacion;
    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialogFotoSubir;
    private Toolbar toolbar;
    private ImageView cuenta_perfil;
    private TextView nombreGrupo, generoMusica;

    private LinearLayout editMainCuenta;
    private boolean confotodeperfil = false;


    public InformacionProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //ANIMACION DEL FONDO
      //  linearLayoutanimacion = (RelativeLayout) findViewById(R.id.profile_layout);
       // animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        //animacion.setEnterFadeDuration(4500);
        //animacion.setExitFadeDuration(4500);
        //animacion.start();


        //cuenta_perfil = (ImageView) findViewById(R.id.cuenta_perfil);
        //progressDialogFotoSubir = new ProgressDialog(this);
        //nombreGrupo = (TextView) findViewById(R.id.nombregrupo);
        //generoMusica = (TextView) findViewById(R.id.texgeneroMusica);

        //nombreGrupo.setText(USUARIO.getNombre());
       // generoMusica.setText(USUARIO.getGenero());
        //Toast.makeText(this, USUARIO.getUser(), Toast.LENGTH_SHORT).show();

       // nombreGrupo.setOnClickListener(this);
        //generoMusica.setOnClickListener(this);
       // cuenta_perfil.setOnClickListener(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_informacion_profile, container, false);
    }}
