package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.R;

public class EditUsuario extends AppCompatActivity {

    private EditText editTextContraseniaEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_usuario);
        Bundle textoInformacion = this.getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Editar Datos Usuario");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        editTextContraseniaEdit = (EditText) findViewById(R.id.editTextContraseniaEdit);
        editTextContraseniaEdit.setText(textoInformacion.getString("usuario"));
        editTextContraseniaEdit.setSelection(textoInformacion.getString("usuario").toString().length());

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

                Toast.makeText(this, editTextContraseniaEdit.getText().toString(), Toast.LENGTH_SHORT).show();

                break;

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }
}
