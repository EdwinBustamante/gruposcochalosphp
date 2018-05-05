package com.edwinbustamante.gruposcochalos;

import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarUsuario extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    LinearLayout linearLayoutanimacion;
    AnimationDrawable animacion;
    private EditText nombreGrupoRegistro, nombreUsuario, pasRegistro1, pasRegistro2;
    private Button registarRegistro;
    private ProgressDialog mProgress;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectReques;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //############################################################################
        //ANIMACION DEL FONDO
        linearLayoutanimacion = (LinearLayout) findViewById(R.id.fondoregistroanimacion);
        animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        animacion.setEnterFadeDuration(4500);
        animacion.setExitFadeDuration(4500);
        animacion.start();
        //############################################################################
        nombreGrupoRegistro = (EditText) findViewById(R.id.nombreGrupoRegistro);
        nombreUsuario = (EditText) findViewById(R.id.nombreUsuario);
        pasRegistro1 = (EditText) findViewById(R.id.contraseniaRegistro);
        pasRegistro2 = (EditText) findViewById(R.id.contraseniaRegistro2);
        registarRegistro = (Button) findViewById(R.id.btnRegistrarCuenta);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //instanciamos el autentificador de fire base y tambien el progress Dialog
        mProgress = new ProgressDialog(this);
        pasRegistro1.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                String tiempoRealNombreGrupo = String.valueOf(s);
                if (!validarPassword(tiempoRealNombreGrupo)) {
                    pasRegistro1.setError("Espacios no permitido");
                }

            }
        });
        pasRegistro2.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                String tiempoRealNombreGrupo = String.valueOf(s);
                if (!validarPassword(tiempoRealNombreGrupo)) {
                    pasRegistro2.setError("Espacios no permitido");
                }

            }
        });
        nombreUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tiempoRealUsuario = String.valueOf(s);
                if (!validarUsuario(tiempoRealUsuario)) {
                    nombreUsuario.setError("Caracteres especiales y espacios no permitido");
                }

            }
        });

        registarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();
            }
        });
    }

    private void startRegister() {

        final String nombreGrupo = nombreGrupoRegistro.getText().toString().trim();// con e trim eliminamos los caracteres blancos al inicio y fin de la cadena
        final String usuario = nombreUsuario.getText().toString().trim();
        final String pas1 = pasRegistro1.getText().toString().trim();
        final String pas2 = pasRegistro2.getText().toString().trim();
        if (!TextUtils.isEmpty(nombreGrupo) && !TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(pas1) && !TextUtils.isEmpty(pas2)) {
            if (usuario.length() > 6) {
                if (pas1.length() > 6) {
                    if (validarUsuario(usuario) && validarPassword(pas1) && validarPassword(pas2)) {

                        if (pas1.equals(pas2)) {

                            mProgress.setMessage("Registrando, espere un momento por favor...");
                            mProgress.show();//lanzamos el progres Dialog

                            cargarWebService();

                        } else {
                            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "La contraseña deber ser mayor a 6 Caracteres", Toast.LENGTH_SHORT).show();
                }
            } else {
                nombreUsuario.setError("El nombre usuario debe ser mayor a 6 caracteres");
                Toast.makeText(this, "El nombre usuario debe ser mayor a 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (TextUtils.isEmpty(nombreGrupo)) {
                nombreGrupoRegistro.setError("El campo esta vacio");
            }
            if (TextUtils.isEmpty(usuario)) {
                nombreUsuario.setError("El campo esta vacio");
            }
            if (TextUtils.isEmpty(pas1)) {
                pasRegistro1.setError("El campo esta vacio");
            }
            if (TextUtils.isEmpty(pas2)) {
                pasRegistro2.setError("El campo esta vacio");
            }
            Toast.makeText(this, "Debe llenar todos los campos de manera obligatoria", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validarPassword(String cadena) {
        Pattern pat = Pattern.compile("[a-zA-Z0-9]+");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }

    public boolean validarUsuario(String cadena) {
        Pattern pat = Pattern.compile("[a-zA-Z0-9]+");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }

    private void cargarWebService() {
        // String url = "http://192.168.43.219/gruposcochalos/registro.php?nombre=" + nombreGrupoRegistro.getText().toString() + "&user=" + nombreUsuario.getText().toString() + "&pwd=" + pasRegistro1.getText().toString();
        String url = "http://192.168.1.11/gruposcochalos/registro.php?nombre=" + nombreGrupoRegistro.getText().toString() + "&usuario=" + nombreUsuario.getText().toString() + "&pwd=" + pasRegistro1.getText().toString();
        url = url.replace(" ", "%20");
        jsonObjectReques = new JsonObjectRequest(Request.Method.GET, url, null, this, this);//realiza el llamado ala url
        requestQueue.add(jsonObjectReques);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("usuariodatos");//datos esta en el php
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.optString("usuario").equals("ya existe usuario")) {
                nombreUsuario.setError("El nombre de usuario ya existe..");
                Toast.makeText(this, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Su cuenta se registro correctamente...", Toast.LENGTH_SHORT).show();

                finish();
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        mProgress.dismiss();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se pudo registrar, fallo la conexion al servidor ", Toast.LENGTH_SHORT).show();
        mProgress.dismiss();
    }
}
