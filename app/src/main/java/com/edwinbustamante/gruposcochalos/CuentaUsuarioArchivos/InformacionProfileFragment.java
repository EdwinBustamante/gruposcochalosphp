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
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.edwinbustamante.gruposcochalos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InformacionProfileFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

    private String idGrupoMusical = "";
    // UI references. animacion
    RelativeLayout linearLayoutanimacion;
    AnimationDrawable animacion;
    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialogFotoSubir;
    private Toolbar toolbar;
    private ImageView cuenta_perfil;
    private TextView usuario, nombreGrupo, generoMusica, movil1, movil2, movilWhatsApp, linkFacebook;
    private TextView informacionEdit, contactosEdit, direccionEdit;
    String FileName = "myUserId";

    private LinearLayout editMainCuenta;
    private ImageView fotoPerfil;
    RequestQueue rq;
    JsonRequest jrq;
    String idUsuarioInput;

    public InformacionProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        String urlActualizacionTraerDatos = Constantes.IP_SERVIDOR + "gruposcochalos/traerdatosusuario.php?idusuario=" + idUsuarioInput;
        actualizarDatosUsuario(urlActualizacionTraerDatos);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View vista = inflater.inflate(R.layout.fragment_informacion_profile, container, false);
        rq = Volley.newRequestQueue(getActivity());
        //ANIMACION DEL FONDO
        linearLayoutanimacion = (RelativeLayout) vista.findViewById(R.id.profile_layout);
        animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        animacion.setEnterFadeDuration(4500);
        animacion.setExitFadeDuration(4500);
        animacion.start();

        fotoPerfil = (ImageView) vista.findViewById(R.id.foto_perfil);
        fotoPerfil.setOnClickListener(this);
        //progressDialogFotoSubir = new ProgressDialog(this);
        usuario = (TextView) vista.findViewById(R.id.usuario);
        usuario.setOnClickListener(this);
        nombreGrupo = (TextView) vista.findViewById(R.id.nombregrupo);
        String defaultValue = "DefaultName";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        idUsuarioInput = sharedPreferences.getString("idusuario", defaultValue);
        //Toast.makeText(getContext(), idUsuarioInput, Toast.LENGTH_SHORT).show();
        nombreGrupo.setOnClickListener(this);
        generoMusica = (TextView) vista.findViewById(R.id.texgeneroMusica);
        generoMusica.setOnClickListener(this);
        informacionEdit = vista.findViewById(R.id.texViewInformacion);
        informacionEdit.setOnClickListener(this);
        movil1 = (TextView) vista.findViewById(R.id.texViewMovil1);
        movil1.setOnClickListener(this);
        movil2 = (TextView) vista.findViewById(R.id.texViewMovil2);
        movil2.setOnClickListener(this);
        movilWhatsApp = (TextView) vista.findViewById(R.id.texViewMovilWhatsApp);
        movilWhatsApp.setOnClickListener(this);
        linkFacebook = (TextView) vista.findViewById(R.id.texViewLinkFacebook);
        // linkFacebook.setOnClickListener(this);

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

    private void actualizarDatosUsuario(String urls) {
        String url = urls;
        url = url.replace(" ", "%20");
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usuario:
                Intent usuarioIntent = new Intent(getActivity(), EditUsuario.class);
                usuarioIntent.putExtra("usuario", usuario.getText().toString());
                startActivity(usuarioIntent);
                break;
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
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});//maximo de caracterres
                dialogoEditNombre.setView(input);

                dialogoEditNombre.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionNombre = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarnombre.php?idgrupomusical=" + idGrupoMusical + "&nombre=" + input.getText().toString();
                        actualizarDatosUsuario(urlActualizacionNombre);
                        // Toast.makeText(getContext(), input.getText().toString(), Toast.LENGTH_SHORT).show();
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
                inputGenero.setFilters(new InputFilter[]{new InputFilter.LengthFilter(70)});//maximo de caracterres
                dialogoEditGenero.setView(inputGenero);

                dialogoEditGenero.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionNombre = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizargenero.php?idgrupomusical=" + idGrupoMusical + "&genero=" + inputGenero.getText().toString();
                        actualizarDatosUsuario(urlActualizacionNombre);
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
            case R.id.texViewMovil1:
                final EditText inputMovil1;
                AlertDialog.Builder dialogoEditMovil1 = new AlertDialog.Builder(getContext());
                dialogoEditMovil1.setTitle("Desea cambiar el número de movil 1..?");
                inputMovil1 = new EditText(getContext());
                inputMovil1.setText(movil1.getText().toString());
                inputMovil1.setSelection(movil1.getText().toString().length());
                inputMovil1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//maximo de caracterres
                inputMovil1.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                dialogoEditMovil1.setView(inputMovil1);

                dialogoEditMovil1.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionMovil2 = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarnumtelefono.php?idgrupomusical=" + idGrupoMusical + "&numtelefono=" + inputMovil1.getText().toString();
                        actualizarDatosUsuario(urlActualizacionMovil2);

                        //       Toast.makeText(getContext(), inputMovil1.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogoEditMovil1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarMovil1 = dialogoEditMovil1.create();
                editarMovil1.show();
                break;
            case R.id.texViewMovil2:
                final EditText inputMovil2;
                AlertDialog.Builder dialogoEditMovil2 = new AlertDialog.Builder(getContext());
                dialogoEditMovil2.setTitle("Desea cambiar el número de movil 2..?");
                inputMovil2 = new EditText(getContext());
                inputMovil2.setText(movil2.getText().toString());
                inputMovil2.setSelection(movil2.getText().toString().length());
                inputMovil2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//maximo de caracterres
                inputMovil2.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                dialogoEditMovil2.setView(inputMovil2);

                dialogoEditMovil2.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionMovil2 = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarnumtelefonodos.php?idgrupomusical=" + idGrupoMusical + "&numtelefonodos=" + inputMovil2.getText().toString();
                        actualizarDatosUsuario(urlActualizacionMovil2);

                        //Toast.makeText(getContext(), inputMovil2.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogoEditMovil2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarMovil2 = dialogoEditMovil2.create();
                editarMovil2.show();
                break;
            case R.id.texViewMovilWhatsApp:
                final EditText inputMovilWhatsApp;
                AlertDialog.Builder dialogoEditMovilWhatsApp = new AlertDialog.Builder(getContext());
                dialogoEditMovilWhatsApp.setTitle("Desea cambiar el número de movil para WhatsApp?");
                inputMovilWhatsApp = new EditText(getContext());
                inputMovilWhatsApp.setText(movilWhatsApp.getText().toString());
                inputMovilWhatsApp.setSelection(movilWhatsApp.getText().toString().length());
                inputMovilWhatsApp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//maximo de caracterres
                inputMovilWhatsApp.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                dialogoEditMovilWhatsApp.setView(inputMovilWhatsApp);

                dialogoEditMovilWhatsApp.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionWhatsApp = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarnumwhatsapp.php?idgrupomusical=" + idGrupoMusical + "&numwhatsapp=" + inputMovilWhatsApp.getText().toString();
                        actualizarDatosUsuario(urlActualizacionWhatsApp);
                        //Toast.makeText(getContext(), inputMovilWhatsApp.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogoEditMovilWhatsApp.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarMovilWhatsApp = dialogoEditMovilWhatsApp.create();
                editarMovilWhatsApp.show();
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
                    /// idGrupoMusical = jsonObject.getString("idgrupomusical");
                    guardarIdGrupoMusical(jsonObject.optString("idgrupomusical"));//guardando en SharePreference
                    nombreGrupo.setText(jsonObject.getString("nombre"));
                    generoMusica.setText(jsonObject.getString("genero"));
                    informacionEdit.setText(jsonObject.getString("informaciondescripcion"));
                    contactosEdit.setText(jsonObject.getString("descripcioncontactos"));
                    direccionEdit.setText(jsonObject.getString("direcciondescripcion"));
                    movil1.setText(jsonObject.getString("numtelefono"));
                    movil2.setText(jsonObject.getString("numtelefonodos"));
                    movilWhatsApp.setText(jsonObject.getString("numwhatsapp"));
                    linkFacebook.setText(jsonObject.getString("linkfacebook"));

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    *BASE DE DATOS SHAREPREFRENCE PARA GUARDAR EN USUARIO
     */
    String FileNameGrupo = "IdGrupo";

    private void guardarIdGrupoMusical(String idgrupomusical) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idgrupomusical", idgrupomusical);
        editor.commit();
    }
}