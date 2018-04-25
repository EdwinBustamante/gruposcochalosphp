package com.edwinbustamante.gruposcochalos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.Objetos.FirebaseReferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegistrarUsuario extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    LinearLayout linearLayoutanimacion;
    AnimationDrawable animacion;
    private EditText nombreGrupoRegistro, correoRegistro, pasRegistro1, pasRegistro2;
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
        correoRegistro = (EditText) findViewById(R.id.correoRegistro);
        pasRegistro1 = (EditText) findViewById(R.id.contraseniaRegistro);
        pasRegistro2 = (EditText) findViewById(R.id.contraseniaRegistro2);
        registarRegistro = (Button) findViewById(R.id.btnRegistrarCuenta);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //instanciamos el autentificador de fire base y tambien el progress Dialog
        mProgress = new ProgressDialog(this);
        registarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegister();
            }
        });

    }

    private void startRegister() {

        final String nombreGrupo = nombreGrupoRegistro.getText().toString().trim();// con e trim eliminamos los caracteres blancos al inicio y fin de la cadena
        final String correoRegis = correoRegistro.getText().toString().trim();
        final String pas1 = pasRegistro1.getText().toString().trim();
        final String pas2 = pasRegistro2.getText().toString().trim();
        if (!TextUtils.isEmpty(nombreGrupo) && !TextUtils.isEmpty(correoRegis) && !TextUtils.isEmpty(pas1) && !TextUtils.isEmpty(pas2)) {
            if (pas1.equals(pas2)) {
                mProgress.setMessage("Registrando, espere un momento por favor...");
                mProgress.show();//lanzamos el progres Dialog
                /*
                * AQUI ESTAMOS INGRESANDO EL CORREO Y LA CONTRASEÑA
                * */
                cargarWebService();


            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Debe llenar todos los campos de manera obligatoria", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarWebService() {
        // String url = "http://192.168.43.219/gruposcochalos/registro.php?nombre=" + nombreGrupoRegistro.getText().toString() + "&user=" + correoRegistro.getText().toString() + "&pwd=" + pasRegistro1.getText().toString();
        String url = "http://192.168.1.11/gruposcochalos/registro.php?nombre=" + nombreGrupoRegistro.getText().toString() + "&user=" + correoRegistro.getText().toString() + "&pwd=" + pasRegistro1.getText().toString();
        url = url.replace(" ", "%20");
        jsonObjectReques = new JsonObjectRequest(Request.Method.GET, url, null, this, this);//realiza el llamado ala url
        requestQueue.add(jsonObjectReques);
    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("usuario");//datos esta en el php
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.optString("user").equals("no registro")) {
                correoRegistro.setError("El nombre de usuario ya existe..!!!");
                Toast.makeText(this, "El Nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Su cuenta se registro correctamente", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegistrarUsuario.this, LoginActivity.class);
                startActivity(i);
                finish();
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        mProgress.dismiss();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se pudo registrar " + error.getMessage().toString(), Toast.LENGTH_SHORT).show();
        mProgress.dismiss();
    }
}
