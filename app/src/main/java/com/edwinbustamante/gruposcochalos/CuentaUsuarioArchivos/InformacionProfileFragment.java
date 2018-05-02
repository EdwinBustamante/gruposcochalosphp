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
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.ImagenFull.FulImagen;
import com.edwinbustamante.gruposcochalos.R;


public class InformacionProfileFragment extends Fragment implements View.OnClickListener {

    // UI references. animacion
    RelativeLayout linearLayoutanimacion;
    AnimationDrawable animacion;
    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialogFotoSubir;
    private Toolbar toolbar;
    private ImageView cuenta_perfil;
    private TextView nombreGrupo, generoMusica;
    private TextView informacionEdit, contactosEdit, direccionEdit;
    String FileName = "myUser";

    private LinearLayout editMainCuenta;
    private ImageView fotoPerfil;

    public InformacionProfileFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_informacion_profile, container, false);
        //ANIMACION DEL FONDO
        //  linearLayoutanimacion = (RelativeLayout) findViewById(R.id.profile_layout);
        // animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        //animacion.setEnterFadeDuration(4500);
        //animacion.setExitFadeDuration(4500);
        //animacion.start();

        fotoPerfil = (ImageView) vista.findViewById(R.id.foto_perfil);
        fotoPerfil.setOnClickListener(this);
        //progressDialogFotoSubir = new ProgressDialog(this);

        nombreGrupo = (TextView) vista.findViewById(R.id.nombregrupo);
        String defaultValue = "DefaultName";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("username", defaultValue);
        nombreGrupo.setText(name);

        nombreGrupo.setOnClickListener(this);
        generoMusica = (TextView) vista.findViewById(R.id.texgeneroMusica);
        generoMusica.setOnClickListener(this);
        informacionEdit = vista.findViewById(R.id.texViewInformacion);
        informacionEdit.setOnClickListener(this);
        contactosEdit = (TextView) vista.findViewById(R.id.texViewContactos);
        contactosEdit.setOnClickListener(this);
        direccionEdit = (TextView) vista.findViewById(R.id.texViewDireccion);
        direccionEdit.setOnClickListener(this);

        //nombreGrupo.setText(USUARIO.getNombre());
        // generoMusica.setText(USUARIO.getGenero());
        //Toast.makeText(this, USUARIO.getUser(), Toast.LENGTH_SHORT).show();

        // nombreGrupo.setOnClickListener(this);
        //generoMusica.setOnClickListener(this);
        // cuenta_perfil.setOnClickListener(this);
        // Inflate the layout for this fragment

        return vista;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nombregrupo:
                /*
                ALERT DIALOG QUE SE LANZA PARA CAMBIAR EL NOMBRE DEL GRUPO MUSICAL
                **/
                final EditText input;
                AlertDialog.Builder dialogoEditNombre = new AlertDialog.Builder(getContext());
                dialogoEditNombre.setTitle("Desea cambiar el nombre del Grupo Musical..?");
                input = new EditText(getContext());
                input.setText(nombreGrupo.getText().toString());
                input.setSelection(nombreGrupo.getText().toString().length());
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});//maximo de caracterres
                dialogoEditNombre.setView(input);

                dialogoEditNombre.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Agarramos el id del usuario siempre verificando que este logueado y cambiamos su atributo de nombre agarrando de los cambios
                        Toast.makeText(getContext(), input.getText().toString(), Toast.LENGTH_SHORT).show();

                    }
                });
                dialogoEditNombre.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarNombre = dialogoEditNombre.create();
                editarNombre.show();
                break;
            case R.id.texgeneroMusica:
                final EditText inputGenero;
                AlertDialog.Builder dialogoEditGenero = new AlertDialog.Builder(getContext());
                dialogoEditGenero.setTitle("Desea cambiar el género música..?");
                inputGenero = new EditText(getContext());
                inputGenero.setText(generoMusica.getText().toString());
                inputGenero.setSelection(generoMusica.getText().toString().length());
                inputGenero.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//maximo de caracterres
                dialogoEditGenero.setView(inputGenero);

                dialogoEditGenero.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), inputGenero.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogoEditGenero.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarGenero = dialogoEditGenero.create();
                editarGenero.show();
                break;
            case R.id.texViewInformacion:
                Intent i = new Intent(getActivity(), EditInformacion.class);
                i.putExtra("informacion", informacionEdit.getText().toString());
                startActivity(i);
                break;
            case R.id.texViewContactos:
                Intent intt = new Intent(getActivity(), EditContactos.class);
                intt.putExtra("contactos", contactosEdit.getText().toString());
                startActivity(intt);
                break;
            case R.id.texViewDireccion:
                Intent direccionIntent = new Intent(getActivity(), EditDireccion.class);
                direccionIntent.putExtra("direccion", direccionEdit.getText().toString());
                startActivity(direccionIntent);
                break;
            case R.id.foto_perfil:

                Intent imagenFu = new Intent(getActivity(), FulImagen.class);
                startActivity(imagenFu);
            default:
                break;
        }
    }

}
