package com.edwinbustamante.gruposcochalos.VisitanteArchivos;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.domain.GrupoMusical;
import com.squareup.picasso.Picasso;

public class InformacionGrupoVisitante extends AppCompatActivity {
    private ImageView imageViewPortada, imageViewPerfil;
    private TextView nombreGrupo, generoGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_grupo_visitante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Informacion de Grupo Musical");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        GrupoMusical grupoMusical = getIntent().getExtras().getParcelable("grupomusical");

        imageViewPortada = (ImageView) findViewById(R.id.portada_visitante);
        imageViewPerfil = (ImageView) findViewById(R.id.perfil_visitante);
        nombreGrupo = (TextView) findViewById(R.id.nombregrupo_visitante);
        generoGrupo = (TextView) findViewById(R.id.texgeneroMusica_visitante);
        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + grupoMusical.getFotoperfil()).into(imageViewPortada);
        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + grupoMusical.getFotoperfil()).into(imageViewPerfil);
        nombreGrupo.setText(grupoMusical.getNombre());
        generoGrupo.setText(grupoMusical.getGenero());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.guardar_contrasenia, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }
}
