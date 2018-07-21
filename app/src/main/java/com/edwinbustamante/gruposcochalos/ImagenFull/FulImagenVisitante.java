package com.edwinbustamante.gruposcochalos.ImagenFull;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FulImagenVisitante extends AppCompatActivity {

    private ImageView perfil;
    PhotoViewAttacher mAttacher;//Para hacer Zoom en el imagen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ful_imagen_visitante);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Foto de " + getIntent().getExtras().getString("nombretoolbar"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        String fotoPerfil = getIntent().getExtras().getString("fotoperfil");
        //  Toast.makeText(this, fotoPerfil, Toast.LENGTH_SHORT).show();
        perfil = (ImageView) findViewById(R.id.imagenfullVisitante);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        perfil.setMaxHeight(height);
        perfil.setMaxWidth(width);
        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + fotoPerfil).error(R.drawable.perfilmusic)
                .placeholder(R.drawable.progress_animation).into(perfil);
        //hace que la imagen sea expansible
        mAttacher = new PhotoViewAttacher(perfil);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_full_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
