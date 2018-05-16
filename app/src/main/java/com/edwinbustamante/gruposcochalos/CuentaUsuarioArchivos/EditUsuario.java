package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUsuario extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    private TextView texViewNombreUsuario;
    private EditText editTextContraseniaAntigua, editTextContraseniaNueva;
    private String idUsuario;
    private String Usuario;
    private String FileName = "myUserId";
    private String FileNameUsuario = "usuario";
    private ProgressDialog mProgress;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectReques;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_usuario);
        Bundle textoInformacion = this.getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cambiar contraseña");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        String defaultValue = "DefaultName";
        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getString("idusuario", defaultValue);
        SharedPreferences sharedPreference = getSharedPreferences(FileNameUsuario, Context.MODE_PRIVATE);
        Usuario = sharedPreference.getString("usuario", defaultValue);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        texViewNombreUsuario = (TextView) findViewById(R.id.texViewNombreUsuario);
        texViewNombreUsuario.setText(Usuario);
        mProgress = new ProgressDialog(this);

        editTextContraseniaAntigua = (EditText) findViewById(R.id.editTexContraseniaAntigua);
        editTextContraseniaNueva = (EditText) findViewById(R.id.editTexContraseniaNueva);
        editTextContraseniaAntigua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tiempoRealContraseniaAntigua = String.valueOf(s);
                if (!validarPassword(tiempoRealContraseniaAntigua)) {
                    editTextContraseniaAntigua.setError("Espacios no permitido");
                }
            }
        });
        editTextContraseniaNueva.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tiempoRealContraseniaNueva = String.valueOf(s);
                if (!validarPassword(tiempoRealContraseniaNueva)) {
                    editTextContraseniaNueva.setError("Espacios no permitido");
                }
            }
        });

    }

    public boolean validarPassword(String cadena) {
        Pattern pat = Pattern.compile("[a-zA-Z0-9]+");
        Matcher mat = pat.matcher(cadena);
        return mat.matches();
    }

    private void cargarWebService() {
        // String url = "http://192.168.43.219/gruposcochalos/registro.php?nombre=" + nombreGrupoRegistro.getText().toString() + "&user=" + nombreUsuario.getText().toString() + "&pwd=" + pasRegistro1.getText().toString();
        String url = Constantes.IP_SERVIDOR + "gruposcochalos/cambiarcontrasenia.php?idusuario=" + idUsuario.toString() + "&pwda=" + editTextContraseniaAntigua.getText().toString() + "&pwdn=" + editTextContraseniaNueva.getText().toString();
        url = url.replace(" ", "%20");
        jsonObjectReques = new JsonObjectRequest(Request.Method.GET, url, null, this, this);//realiza el llamado ala url
        requestQueue.add(jsonObjectReques);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guardar_contrasenia, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.guardarContrasenia:
                startCambiarContrasenias();

                break;

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    private void startCambiarContrasenias() {
        final String pwd1 = editTextContraseniaAntigua.getText().toString().trim();
        final String pwd2 = editTextContraseniaNueva.getText().toString().trim();
        if (!TextUtils.isEmpty(pwd1) && !TextUtils.isEmpty(pwd2)) {
            if (pwd1.length() > 6) {
                if (pwd2.length() > 6) {
                    if (validarPassword(pwd1) && validarPassword(pwd2)) {

                        if (pwd1.equals(pwd2)) {

                            editTextContraseniaNueva.setError("La contraseña deber ser distinta a la antigua");
                        } else {

                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                            alertDialog.setTitle("Es correcto la contraseña nueva..?");
                                                        alertDialog.setMessage(editTextContraseniaNueva.getText().toString());
                            alertDialog.setPositiveButton("Correcto", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mProgress.setMessage("Cambiando Contraseña, espere un momento por favor...");
                                    mProgress.show();//lanzamos el progres Dialog

                                   cargarWebService();
                                }
                            }).setNegativeButton("Incorrecto", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog cambiarContrasenia = alertDialog.create();
                            cambiarContrasenia.show();

                        }
                    }
                } else {
                    editTextContraseniaNueva.setError("La contraseña deber ser mayor a 6 Caracteres");
                }
            } else {
                editTextContraseniaAntigua.setError("La contraseña deber ser mayor a 6 Caracteres");
            }
        } else {
            if (TextUtils.isEmpty(pwd1)) {
                editTextContraseniaAntigua.setError("El campo esta vacio");
            }
            if (TextUtils.isEmpty(pwd2)) {
                editTextContraseniaNueva.setError("El campo esta vacio");
            }
            Toast.makeText(this, "Debe llenar todos los campos de manera obligatoria", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se pudo registrar, fallo la conexion al servidor ", Toast.LENGTH_SHORT).show();
        mProgress.dismiss();

    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("usuariodatos");//datos esta en el php
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.optString("respuesta").equals("contrasenia antigua incorrecta")) {
                mProgress.dismiss();
                editTextContraseniaAntigua.setError("Contraseña antigua incorrecta ");
            } else {
                if (jsonObject.optString("respuesta").equals("la contrasenia fue cambiada exitosamente")) {
                    mProgress.dismiss();
                    finish();
                    Toast.makeText(this, "La contraseña fue cambiada exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    mProgress.dismiss();
                    Toast.makeText(this, "Ocurrio un error inesperado al cambiar la contraseña intente nuevamente", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        mProgress.dismiss();
    }
}
