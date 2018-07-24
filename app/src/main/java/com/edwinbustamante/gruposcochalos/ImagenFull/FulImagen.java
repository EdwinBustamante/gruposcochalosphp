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
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
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
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import uk.co.senab.photoview.PhotoViewAttacher;

import com.frosquivel.magicalcamera.MagicalCamera;


//and maybe you need in some ocations
import com.frosquivel.magicalcamera.Objects.MagicalCameraObject;

public class FulImagen extends AppCompatActivity {


    PhotoViewAttacher mAttacher;//Para hacer Zoom en el imagen

    Button btnChoose, btnUpload, rotatFoto;
    ImageView imageUpload;
    final int CODE_GALLERY_REQUEST = 999;
    final int CODE_CAMARA_REQUEST = 100;
    String urlUpload = Constantes.IP_SERVIDOR + "/gruposcochalos/actualizarfotoperfi.php?";
    Bitmap bitmap = null;
    String FileNameGrupo = "IdGrupo";
    ProgressDialog cargarImagen;
    private String mPath;
    private static String APP_DIRECTORY = "GruposCochalos/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "GruposCochalosImages";
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 100;//la resolucion en pixeles

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

        String imgPerfil = getIntent().getExtras().getString("foto");///recibiendo la imagen del fragmente anterior
        imageUpload = (ImageView) findViewById(R.id.imagenfull);
        rotatFoto = findViewById(R.id.rotarFoto);
        rotatFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap rotado = rotateImage(bitmap, 90);
                imageUpload.setImageBitmap(rotado);
                bitmap = rotado;
            }
        });


        //   imageUpload.setImageResource(imgPortada);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        imageUpload.setMaxHeight(height);
        imageUpload.setMaxWidth(width);


        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + imgPerfil).error(R.drawable.perfilmusic)
                .placeholder(R.drawable.progress_animation).into(imageUpload);
        //hace que la imagen sea expansible
        mAttacher = new PhotoViewAttacher(imageUpload);
        cargarImagen = new ProgressDialog(FulImagen.this);

        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));
        }
        //Following the example you could also
        //locationPermissions(requestCode, permissions, grantResults);
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
                    // ActivityCompat.requestPermissions(FulImagen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_CAMARA_REQUEST);

                    abrirCamara();

                } else if (option[which] == "Elegir de galeria") {
                    // ActivityCompat.requestPermissions(FulImagen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
                    // Intent intent = new Intent(Intent.ACTION_PICK);
                    // intent.setType("image/*");
                    // startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), CODE_GALLERY_REQUEST);

                    magicalCamera.selectedPicture("Selecciona una aplicación");
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void abrirCamara() {
        //take photo
        magicalCamera.takePhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        magicalCamera.resultPhoto(requestCode, resultCode, data);


        //this is for rotate picture in this method
        //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);
        //alistando para enviar al servidor

        bitmap = magicalCamera.getPhoto();        //with this form you obtain the bitmap (in this example set this bitmap in image view)

        imageUpload.setImageBitmap(bitmap);
        rotatFoto.setVisibility(View.VISIBLE);
        //if you need save your bitmap in device use this method and return the path if you need this
        //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.


      /*  String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(), "gc", "Grupos Cochalos", MagicalCamera.JPEG, true);
        if (path != null) {
            Toast.makeText(FulImagen.this, "Foto guardado en el dispositivo " + path, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(FulImagen.this, "No se guardo la foto", Toast.LENGTH_SHORT).show();
        }*/
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
                Toast.makeText(FulImagen.this, "error intente nuevamente...", Toast.LENGTH_SHORT).show();
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

}



