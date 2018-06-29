package com.edwinbustamante.gruposcochalos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    String FileNameUsuario = "usuario";
    String FileNameGrupo = "IdGrupo";
    String FileNameContrasenia = "Rcontrasenia";

    public void recordarContrasenia(String contrasenia) {
        SharedPreferences sharedPreferences = getSharedPreferences(FileNameContrasenia, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("contrasenia", contrasenia);
        editor.commit();
    }

    public void guardarIdUsuario(String idusername) {
        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idusuario", idusername);
        editor.commit();
    }

    public void guardarUsuario(String usuario) {
        SharedPreferences sharedPreferences = getSharedPreferences(FileNameUsuario, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", usuario);
        editor.commit();
    }


    private void guardarIdGrupoMusical(String idgrupomusical) {
        SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idgrupomusical", idgrupomusical);
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
        String defaultValue = "";
        SharedPreferences sharedPreferences = this.getSharedPreferences(FileNameUsuario, Context.MODE_PRIVATE);
        String usuarioEntrada = sharedPreferences.getString("usuario", defaultValue);
        in_usuario.setText(usuarioEntrada.toString());
        in_usuario.setSelection(usuarioEntrada.toString().length());

        SharedPreferences sharedPreferencesContrasenia = this.getSharedPreferences(FileNameContrasenia, Context.MODE_PRIVATE);
        String RecordContraEntrada = sharedPreferencesContrasenia.getString("contrasenia", defaultValue);
        in_contrasenia = (EditText) findViewById(R.id.contrasenia);
        in_contrasenia.setText(RecordContraEntrada.toString());
        in_contrasenia.setSelection(RecordContraEntrada.toString().length());

        mProgress = new ProgressDialog(this);
        rq = Volley.newRequestQueue(getApplicationContext());


    }


    public void iniciarSesion(View view) {

        String usuario = in_usuario.getText().toString().trim();
        String contrasenia = in_contrasenia.getText().toString().trim();
        if (!TextUtils.isEmpty(usuario) && !TextUtils.isEmpty(contrasenia)) {//compromamos que no este vacio
            mProgress.setMessage("Ingresando al sistema, espere un momento");
            mProgress.show();
            String url = Constantes.IP_SERVIDOR + "gruposcochalos/sesion.php?usuario=" + in_usuario.getText().toString() + "&pwd=" + in_contrasenia.getText().toString();
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
                        guardarIdUsuario(jsonObject.optString("idusuario"));//guardando en SharePreference
                        guardarUsuario(jsonObject.optString("usuario"));//guardando en SharePreference
                        guardarIdGrupoMusical(jsonObject.optString("idusuario"));
                        Toast.makeText(this, "Se inicio correctamente", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder recordarContrasenia = new AlertDialog.Builder(this);
                        recordarContrasenia.setTitle("Grupos Cochalos");
                        recordarContrasenia.setMessage("Desea que Grupos Cochalos recuerde la contraseña..?");
                        JSONObject JsonObject = jsonObject;

                                recordarContrasenia(JsonObject.optString("pwd"));
                                Intent i = new Intent(LoginActivity.this, CuentaUsuario.class);
                                User usr = new User();
                                usr.setUser(JsonObject.optString("usuario"));
                                i.putExtra("objetoUsuario", usr);
                                startActivity(i);
                                finish();




                    }
                }


            }


        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public void olvideContrasenia(View view) {
        Intent olvide = new Intent(this, OlvideContrasenia.class);
        startActivity(olvide);
    }
}