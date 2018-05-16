package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.annotation.TargetApi;
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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.ImagenFull.FulImagen;
import com.edwinbustamante.gruposcochalos.MapsActivityAdjuntarUbicacion;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class Publicar extends AppCompatActivity implements View.OnClickListener {
    PhotoViewAttacher mAttacher;//Para hacer Zoom en el imagen
    private EditText editTextPublicacionPrevio;
    private ImageView fotoAdjuntarPublicacion, ubicacionAdjuntarPublicacion, imageViewPublicacionPrevio;
    private final int MY_PERMISSIONS = 100;
    private RelativeLayout mRlView;
    private static String APP_DIRECTORY = "GruposCochalos/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "GruposCochalosImages";
    private String mPath;
    private final int PHOTO_CODE = 100;
    private final int SELECT_PICTURE = 200;
    private Button mOptionButton;
    ProgressDialog cargarImagen;
    Bitmap bitmap;
    String FileNameGrupo = "IdGrupo";
    String urlUpload = Constantes.IP_SERVIDOR + "/gruposcochalos/publicarpublicacion.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);
        Bundle textoInformacion = this.getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Escribir actualización de estado");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        editTextPublicacionPrevio = (EditText) findViewById(R.id.editTextPublicacionPrevio);
        fotoAdjuntarPublicacion = (ImageView) findViewById(R.id.imageViewAdjuntarImagenPublicacion);
        fotoAdjuntarPublicacion.setOnClickListener(this);
        ubicacionAdjuntarPublicacion = (ImageView) findViewById(R.id.imageViewAdjuntarUbicacionPublicacion);
        ubicacionAdjuntarPublicacion.setOnClickListener(this);
        imageViewPublicacionPrevio = (ImageView) findViewById(R.id.imageViewPublicacionPrevio);
        imageViewPublicacionPrevio = (ImageView) findViewById(R.id.imageViewPublicacionPrevio);
        mAttacher = new PhotoViewAttacher(imageViewPublicacionPrevio);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        imageViewPublicacionPrevio.setMaxHeight(height);
        imageViewPublicacionPrevio.setMaxWidth(width);
        cargarImagen = new ProgressDialog(Publicar.this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.publicar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.publicar:
                if (editTextPublicacionPrevio.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Debes escribir una publicacion para poder publicar", Toast.LENGTH_SHORT).show();
                } else {
                    publicarPublicacion();
                }

                Toast.makeText(this, "lista para publicar", Toast.LENGTH_SHORT).show();
                break;

            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewAdjuntarImagenPublicacion:
                if (mayRequestStoragePermission()) {
                    showOptions();
                }

                break;
            case R.id.imageViewAdjuntarUbicacionPublicacion:
                Intent adjuntar = new Intent(Publicar.this, MapsActivityAdjuntarUbicacion.class);
                startActivity(adjuntar);
                break;
            default:
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean mayRequestStoragePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Publicar.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Tomar foto") {
                    openCamera();
                } else if (option[which] == "Elegir de galeria") {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), SELECT_PICTURE);
                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
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
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
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
                    imageViewPublicacionPrevio.setImageBitmap(rotateImage(bitmap, 90));
                    break;
                case SELECT_PICTURE:

                    if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
                        Uri filePath = data.getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(filePath);
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            imageViewPublicacionPrevio.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;

                    }
            }
        }
    }

    private String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Publicar.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                mOptionButton.setEnabled(true);
            }
        } else {
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Publicar.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void publicarPublicacion() {
        cargarImagen.setTitle("Grupos Cochalos");
        cargarImagen.setMessage("Publicando publicacion, espere un momento por favor...");
        cargarImagen.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cargarImagen.dismiss();
                String respuesta = response.toString();
                Toast.makeText(Publicar.this, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Publicar.this, "error" + error.toString(), Toast.LENGTH_SHORT).show();
                cargarImagen.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                /// idGrupoMusical = jsonObject.getString("idgrupomusical");
                String defaultValue = "DefaultName";
                SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                String imageData = imageToString(bitmap);//se encarga de convertir a cadena el metodo metodo to String
                params.put("idgrupomusical", idGrupoMusical);
                params.put("textopublicacion", editTextPublicacionPrevio.getText().toString());
                params.put("image", imageData);//el nombre image es la clave para recibir en el php
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Publicar.this);
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

}
