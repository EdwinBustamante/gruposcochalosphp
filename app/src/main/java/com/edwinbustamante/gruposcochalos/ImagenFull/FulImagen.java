package com.edwinbustamante.gruposcochalos.ImagenFull;


import android.Manifest;
import android.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;

import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.SubirFoto;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;


public class FulImagen extends AppCompatActivity {


    PhotoViewAttacher mAttacher;//Para hacer Zoom en el imagen

    Button btnChoose, btnUpload;
    ImageView imageUpload;
    final int CODE_GALLERY_REQUEST = 999;
    final int CODE_CAMARA_REQUEST = 100;
    String urlUpload = Constantes.IP_SERVIDOR + "/gruposcochalos/actualizarfotoperfi.php?";
    Bitmap bitmap;
    String FileNameGrupo = "IdGrupo";
    ProgressDialog cargarImagen;
    private String mPath;
    private static String APP_DIRECTORY = "GruposCochalos/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "GruposCochalosImages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ful_imagen);

        Bundle textoDireccion = this.getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Foto de Perfil");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        Intent i = getIntent();
        String imgPerfil = i.getExtras().getString("foto");///recibiendo la imagen del fragmente anterior
        imageUpload = (ImageView) findViewById(R.id.imagenfull);
        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + imgPerfil).into(imageUpload);
        //   imageUpload.setImageResource(imgPortada);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        imageUpload.setMaxHeight(height);
        imageUpload.setMaxWidth(width);
        //hace que la imagen sea expansible
        mAttacher = new PhotoViewAttacher(imageUpload);
        cargarImagen = new ProgressDialog(FulImagen.this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_perfil:
                showOptions();

                break;
            case R.id.guardarperfil:
                subirFoto();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;

        }
        return true;
    }


    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(FulImagen.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Tomar foto") {
                    ActivityCompat.requestPermissions(
                            FulImagen.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_CAMARA_REQUEST
                    );

                } else if (option[which] == "Elegir de galeria") {
                    ActivityCompat.requestPermissions(
                            FulImagen.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST
                    );

                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(this, "No tienes permisos para aceder a Galeria", Toast.LENGTH_SHORT).show();
            }
            return;
        } else {
            if (requestCode == CODE_CAMARA_REQUEST) {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    abrirCamara();
                } else {
                    Toast.makeText(this, "No tienes permisos para aceder a Camara", Toast.LENGTH_SHORT).show();

                }

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void abrirCamara() {
        Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, CODE_CAMARA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageUpload.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            if (requestCode == CODE_CAMARA_REQUEST && resultCode == RESULT_OK) {
                MediaScannerConnection.scanFile(this,
                        new String[]{mPath}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> Uri = " + uri);
                            }
                        });


                bitmap = BitmapFactory.decodeFile(mPath);
                imageUpload.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    public void subirFoto() {
        cargarImagen.setTitle("Grupos Cochalos");
        cargarImagen.setMessage("Cambiando foto de perfil");
        cargarImagen.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cargarImagen.dismiss();
                Toast.makeText(FulImagen.this, response.toString(), Toast.LENGTH_SHORT).show();
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FulImagen.this, "error" + error.toString(), Toast.LENGTH_SHORT).show();
                cargarImagen.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String imageData = imageToString(bitmap);//se encarga de convertir a cadena el metodo metodo to String
                params.put("image", imageData);//el nombre image es la clave para recibir en el php

                /// idGrupoMusical = jsonObject.getString("idgrupomusical");
                String defaultValue = "DefaultName";
                SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);

                params.put("idgrupomusical", idGrupoMusical);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(FulImagen.this);
        requestQueue.add(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {

        //metodo que se encarga de convertir el bitmap  a string
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}



