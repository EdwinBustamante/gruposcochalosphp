package com.edwinbustamante.gruposcochalos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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

        String correo = in_usuario.getText().toString().trim();
        String contrasenia = in_contrasenia.getText().toString().trim();
        if (!TextUtils.isEmpty(correo) && !TextUtils.isEmpty(contrasenia)) {//compromamos que no este vacio
            mProgress.setMessage("Ingresando al sistema, espere un momento");
            mProgress.show();
            String url = "http://192.168.43.219/gruposcochalos/sesion.php?user=" + in_usuario.getText().toString()+ "&pwd=" + in_contrasenia.getText().toString();
            //String url = "http://192.168.1.11/gruposcochalos/sesion.php?user=" + correo + "$pwd=" + contrasenia;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);


        } else {
            Toast.makeText(this, "LOS CAMPOS DEBEN SER LLENADOS CORRECTAMENTE PARA INGRESAR AL SISTEMA", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarse(View view) {

        Intent i = new Intent(LoginActivity.this, RegistrarUsuario.class);
        startActivity(i);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgress.dismiss();
        Toast.makeText(this, "No se encontro usuario" + error.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        mProgress.dismiss();
        User usuario = new User();
        JSONArray jsonArray = response.optJSONArray("datos");//datos esta en el php
        JSONObject jsonObject = null;
        try {

            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.optString("user").equals("no inicia")) {

                Toast.makeText(this, "No se pudo iniciar la sesion Revise Conexion", Toast.LENGTH_SHORT).show();
            } else {
                if (jsonObject.optString("user").equals("fallo al iniciar")) {

                } else {
                    Toast.makeText(this, "Se inicio Correctamente", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, CuentaUsuario.class);
                    startActivity(i);


                    usuario.setCorreo(jsonObject.optString("user"));
                    usuario.setContrasenia(jsonObject.optString("pwd"));
                    usuario.setGenero(jsonObject.optString("nombre"));
                }

            }


        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
}