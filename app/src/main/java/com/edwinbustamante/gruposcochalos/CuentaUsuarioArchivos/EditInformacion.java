package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditInformacion extends AppCompatActivity implements  Response.ErrorListener, Response.Listener<JSONObject>{

    private EditText editTextInformacionEdit;
    String FileNameGrupo = "IdGrupo";
    RequestQueue rq;
    JsonRequest jrq;
    private boolean exito=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_informacion);
        rq = Volley.newRequestQueue(this);
        Bundle textoInformacion = this.getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Información");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        editTextInformacionEdit = (EditText) findViewById(R.id.editTextInformacionEdit);
        editTextInformacionEdit.setText(textoInformacion.getString("informacion"));
        editTextInformacionEdit.setSelection(textoInformacion.getString("informacion").toString().length());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guardar_informacion, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.guardarInformacion:

                String defaultValue = "DefaultName";
                SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                String urlActualizacioninformacion = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarinformaciondescripcion.php?idgrupomusical=" + idGrupoMusical + "&informaciondescripcion=" + editTextInformacionEdit.getText().toString();
                actualizarDatosUsuario(urlActualizacioninformacion);


                break;

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }
    private void actualizarDatosUsuario(String urls) {
        String url = urls;
        url = url.replace(" ", "%20");
        url = url.replace("\n", "%20");
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);

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
                Toast.makeText(this, "Datos incorrectos de entrada", Toast.LENGTH_SHORT).show();
            } else {
                if (jsonObject.getString("idgrupomusical").equals("error en la consulta")) {
                    Toast.makeText(this, "Fallo al actualizar los datos intenete nuevamente", Toast.LENGTH_SHORT).show();
                } else {
                    /// idGrupoMusical = jsonObject.getString("idgrupomusical");
                    String defaultValue = "DefaultName";
                    SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                    String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);

                   String idRespuesta= jsonObject.optString("idgrupomusical");//obteniendo resultado
                    if (idGrupoMusical.equals(idRespuesta)){
                        Toast.makeText(this, "Información actualizada exitosamente...", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(this, "Se produjo un error al actualizar la información, intente nuevamente", Toast.LENGTH_SHORT).show();
                    }



                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
