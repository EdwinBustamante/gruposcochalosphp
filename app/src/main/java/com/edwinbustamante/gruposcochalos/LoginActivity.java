package com.edwinbustamante.gruposcochalos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos.CuentaUsuario;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.Objetos.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    /*animacion en el fondo del login
    * */
    // UI references.
    LinearLayout linearLayoutanimacion;
    AnimationDrawable animacion;

    private AutoCompleteTextView in_usuario;
    private EditText in_contrasenia;
    private ProgressDialog mProgress;
    RequestQueue rq;
    JsonRequest jrq;
    /*
    *BASE DE DATOS SHAREPREFRENCE PARA GUARDAR EN USUARIO
     */
    String FileName = "myUserId";

    public void guardarUsuario(String idusername) {
        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idusuario", idusername);
        editor.commit();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //############################################################################
        //ANIMACION DEL FONDO
        linearLayoutanimacion = (LinearLayout) findViewById(R.id.fondologinanimacion);
        animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        animacion.setEnterFadeDuration(4500);
        animacion.setExitFadeDuration(4500);
        animacion.start();
        //############################################################################
        in_usuario = (AutoCompleteTextView) findViewById(R.id.usuario);
        in_contrasenia = (EditText) findViewById(R.id.contrasenia);
        mProgress = new ProgressDialog(this);
        rq = Volley.newRequestQueue(getApplicationContext());


    }


    public void iniciarSesion(View view) {

        String usuario = in_usuario.getText().toString().trim();
        String contrasenia = in_contrasenia.getText().toString().trim();
        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(contrasenia)) {//compromamos que no este vacio
            mProgress.setMessage("Ingresando al sistema, espere un momento");
            mProgress.show();
            String url = Constantes.IP_SERVIDOR+"gruposcochalos/sesion.php?usuario=" + in_usuario.getText().toString() + "&pwd=" + in_contrasenia.getText().toString();
            //String url = "http://192.168.1.11/gruposcochalos/sesion.php?user=" + correo + "$pwd=" + contrasenia;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);


        } else {
            if (TextUtils.isEmpty(usuario)) {
                in_usuario.setError("Campo vacio");
            }
            if (TextUtils.isEmpty(contrasenia)) {
                in_contrasenia.setError("Campo vacio");
            }
        }
    }

    public void registrarse(View view) {

        Intent i = new Intent(LoginActivity.this, RegistrarUsuario.class);
        startActivity(i);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgress.dismiss();
        // Toast.makeText(this, "No se puede iniciar", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        mProgress.dismiss();
        User usuario = new User();
        JSONArray jsonArray = response.optJSONArray("usuariodatos");//datos esta en el php
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.optString("usuario").equals("no inicia")) {

                Toast.makeText(this, "No se pudo iniciar la sesion Revise Conexion", Toast.LENGTH_SHORT).show();
            } else {
                if (jsonObject.optString("usuario").equals("existe usuario")) {
                    in_contrasenia.setError("Contraseña incorrecta");
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else {
                    if (jsonObject.optString("usuario").equals("datos incorrectos")) {
                        in_contrasenia.setError("Contraseña incorrecta");
                        in_usuario.setError("Nombre de usuario incorrecto");
                        Toast.makeText(this, "Los datos estan incorrectos", Toast.LENGTH_SHORT).show();
                    } else {
                        guardarUsuario(jsonObject.optString("idusuario"));//guardando en SharePreference
                        Toast.makeText(this, "Se inicio correctamente", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, CuentaUsuario.class);
                        User usr = new User();
                        usr.setUser(jsonObject.optString("usuario"));
                        i.putExtra("objetoUsuario", usr);
                        startActivity(i);

                    }
                }


            }


        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
}