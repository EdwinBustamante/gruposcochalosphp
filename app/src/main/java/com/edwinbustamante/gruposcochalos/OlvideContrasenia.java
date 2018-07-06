package com.edwinbustamante.gruposcochalos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OlvideContrasenia extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    // UI references.
    LinearLayout linearLayoutanimacion;
    AnimationDrawable animacion;
    private AutoCompleteTextView usuarioRecordar;
    private EditText instrumetoRecordar;
    private ProgressDialog mProgress;
    RequestQueue rq;
    JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_contrasenia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Recupera tu contraseña");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        usuarioRecordar = findViewById(R.id.usuarioRecordar);
        instrumetoRecordar = findViewById(R.id.instrumentoRecordar);
        mProgress = new ProgressDialog(this);
        rq = Volley.newRequestQueue(getApplicationContext());
        //############################################################################
        //ANIMACION DEL FONDO
        linearLayoutanimacion = (LinearLayout) findViewById(R.id.colorAnimate);
        animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        animacion.setEnterFadeDuration(4500);
        animacion.setExitFadeDuration(4500);
        animacion.start();
        //############################################################################
    }

    public void RecuperarContrasenia(View view) {
        String usuarioR = usuarioRecordar.getText().toString().trim();
        String instrumentoR = instrumetoRecordar.getText().toString().trim();
        if (!TextUtils.isEmpty(usuarioR) && !TextUtils.isEmpty(instrumentoR)) {//compromamos que no este vacio
            mProgress.setTitle("Recuperación de Contraseña");
            mProgress.setMessage("Solicitando la contraseña de " + usuarioR + "\n" + "Espere un momento por favor...");
            mProgress.show();
            String url = Constantes.IP_SERVIDOR + "gruposcochalos/recordar.php?usuarior=" + usuarioRecordar.getText().toString() + "&instrumentor=" + instrumetoRecordar.getText().toString();
            //String url = "http://192.168.1.11/gruposcochalos/sesion.php?user=" + correo + "$pwd=" + contrasenia;
            jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            rq.add(jrq);


        } else {
            if (TextUtils.isEmpty(usuarioR)) {
                usuarioRecordar.setError("Campo vacio");
            }
            if (TextUtils.isEmpty(instrumentoR)) {
                instrumetoRecordar.setError("Campo vacio");
            }
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        mProgress.dismiss();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            Toast.makeText(this, "Error al recuperar la contraseña, fallo la conexion al servidor ", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "No tienes conexion a Internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResponse(JSONObject response) {
        mProgress.dismiss();

        JSONArray jsonArray = response.optJSONArray("usuariodatos");//datos esta en el php
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.optString("usuario").equals("no inicia")) {

                Toast.makeText(this, "No se puede recuperar la contraseña, Intente mas tarde", Toast.LENGTH_SHORT).show();
            } else {
                if (jsonObject.optString("usuario").equals("existe usuario")) {
                    instrumetoRecordar.setError("Instrumento incorrecto");
                    Toast.makeText(this, "Instrumento incorrecto", Toast.LENGTH_SHORT).show();
                } else {
                    if (jsonObject.optString("usuario").equals("datos incorrectos")) {
                        instrumetoRecordar.setError("Datos incorrectos");
                        usuarioRecordar.setError("Datos incorrectos");
                        Toast.makeText(this, "Los datos estan incorrectos", Toast.LENGTH_SHORT).show();
                    } else {

                        String usu = jsonObject.optString("usuario");
                        String contraseniaRecordada = jsonObject.optString("pwd");

                        AlertDialog.Builder contraseniaLlego = new AlertDialog.Builder(this);
                        contraseniaLlego.setIcon(R.drawable.ic_advertencia);
                        contraseniaLlego.setTitle("Grupos Cochalos");
                        contraseniaLlego.setMessage("Usuario:" + usu + "\n" + "Tu contraseña es: " + contraseniaRecordada);
                        contraseniaLlego.setPositiveButton("Gracias...", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        contraseniaLlego.create().show();


                    }
                }


            }


        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


}