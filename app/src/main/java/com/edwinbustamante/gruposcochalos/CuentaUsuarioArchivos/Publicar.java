package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.DialogAnadirUbicacion;
import com.edwinbustamante.gruposcochalos.DialogFragmentUbicacion;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static android.app.PendingIntent.getActivity;

public class Publicar extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, DialogFragmentUbicacion.OnCompleteListener {
    PhotoViewAttacher mAttacher;//Para hacer Zoom en el imagen
    private EditText editTextPublicacionPrevio;
    private ImageView fotoAdjuntarPublicacion, ubicacionAdjuntarPublicacion, imageViewPublicacionPrevio;
    private final int MY_PERMISSIONS = 100;
    private RelativeLayout mRlView;
    private static String APP_DIRECTORY = "GruposCochalos/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "GruposCochalosImages";
    private String namefoto = "";
    private Button mOptionButton;
    final int CODE_GALLERY_REQUEST = 999;
    final int CODE_CAMARA_REQUEST = 100;
    ProgressDialog cargarImagen;
    Bitmap bitmap;
    String FileNameGrupo = "IdGrupo";
    String urlUpload = Constantes.IP_SERVIDOR + "/gruposcochalos/publicarpublicacion.php?";
    private ScrollView scrollViewPublicar;
    private GoogleMap googleMap;
    private UiSettings mUiSettings;

    private int contadorDeTap = 0;
    boolean mIsLargeLayout;
    ImageView normal, satelital, tierra, hibrido;

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
        normal = findViewById(R.id.tiponormalp);
        normal.setOnClickListener(this);
        hibrido = findViewById(R.id.tipohibridop);
        hibrido.setOnClickListener(this);
        satelital = findViewById(R.id.tiposatelitalp);
        satelital.setOnClickListener(this);
        tierra = findViewById(R.id.tipotierrap);
        tierra.setOnClickListener(this);

        scrollViewPublicar = (ScrollView) findViewById(R.id.scrollViewPublicar);
        mAttacher = new PhotoViewAttacher(imageViewPublicacionPrevio);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        imageViewPublicacionPrevio.setMaxHeight(height);
        imageViewPublicacionPrevio.setMaxWidth(width);
        cargarImagen = new ProgressDialog(Publicar.this);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapSeccionada);
        mapFragment.getMapAsync(this);


        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
        // buttoncargar = (Button) findViewById(R.id.buttoncargar);

       /* if (validarPermisos()) {
            buttoncargar.setEnabled(true);
        } else {
            buttoncargar.setEnabled(false);
        }
        buttoncargar.setOnClickListener(this);
    */
    }

    public boolean validarPermisos() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {

            cargarDialogRecomendacion();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);

        }
        return false;
    }

    private void cargarDialogRecomendacion() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(Publicar.this);
        dialogo.setTitle("Permisos desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la Aplicacion Grupos Chochalos");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                //  buttoncargar.setEnabled(true);

            } else {
                solicitarPermisosManual();
            }
        }


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
                showOptions();
                break;


            case R.id.imageViewAdjuntarUbicacionPublicacion:
                //  Intent adjuntar = new Intent(Publicar.this, MapsActivityAdjuntarUbicacion.class);
                //startActivity(adjuntar);

            /*
                Snackbar snackbar = Snackbar.make(findViewById(R.id.mapSeccionada), "Presiona en el mapa para añadir ubicación...", Snackbar.LENGTH_INDEFINITE).setDuration(10000)
                        .setAction("Entendido", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                snackbar.show();

                FullScreenDialog dialog = new FullScreenDialog();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft, FullScreenDialog.TAG);

                */

                onCreateDialog();

                break;

            case R.id.tiponormalp:
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.tiposatelitalp:
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.tipotierrap:
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.tipohibridop:
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void onCreateDialog() {
        DialogFragmentUbicacion nd = new DialogFragmentUbicacion();
        nd.setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.MyStyle);
        nd.show(getSupportFragmentManager(), null);

        // new DialogFragmentUbicacion().setStyle(DialogFragment.STYLE_NORMAL.).show(getSupportFragmentManager(), null);


//       FragmentManager fragmentManager = getSupportFragmentManager();
//        DialogFragmentUbicacion dialogAnadirUbicacion = new DialogFragmentUbicacion();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.add(android.R.id.content, dialogAnadirUbicacion).addToBackStack(null).commit();


    }

    private void showOptions() {
        final CharSequence[] option = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Publicar.this);
        builder.setTitle("Elige una opción");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (option[which] == "Tomar foto") {

                    abrirCamara();

                } else if (option[which] == "Elegir de galeria") {

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), CODE_GALLERY_REQUEST);

                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }


    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder dialogOpciones = new AlertDialog.Builder(Publicar.this);
        dialogOpciones.setTitle("Desea configurar los permisos de forma manual?");
        dialogOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                }

            }
        });
        dialogOpciones.show();
    }

    private void abrirCamara() {
        // Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if (!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if (isDirectoryCreated) {
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            namefoto = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY + File.separator + imageName;

            Uri output = Uri.fromFile(new File(namefoto));
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
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
                imageViewPublicacionPrevio.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            if (requestCode == CODE_CAMARA_REQUEST && resultCode == RESULT_OK) {

                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{namefoto}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("nombrefoto", namefoto);
                    }
                });

                bitmap = BitmapFactory.decodeFile(namefoto);
                imageViewPublicacionPrevio.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString("file_path", mPath);
//    }


    private String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
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
                finish();

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
                String hora = obtenerFechaHora();
                params.put("hora", hora);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Publicar.this);
        requestQueue.add(stringRequest);
    }

    private String obtenerFechaHora() {
        String fechaHora = "";
        //  long codigoHora = System.currentTimeMillis()/1000;
        long codigoHora = new Date().getTime();
        Date d = new Date(codigoHora);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm:ss a");//se da el formato en este caso la hora los minutos y los segundos
        java.text.SimpleDateFormat mes = new java.text.SimpleDateFormat("MM");
        java.text.SimpleDateFormat fecha = new java.text.SimpleDateFormat("dd");
        java.text.SimpleDateFormat anio = new java.text.SimpleDateFormat("yyyy");
        String mesLiteral = "";
        switch (mes.format(d)) {
            case "01":
                mesLiteral = "ene.";
                break;
            case "02":
                mesLiteral = "feb.";
                break;
            case "03":
                mesLiteral = "mar.";
                break;
            case "04":
                mesLiteral = "abr.";
                break;
            case "05":
                mesLiteral = "may.";
                break;
            case "06":
                mesLiteral = "jun.";
                break;
            case "07":
                mesLiteral = "jul.";
                break;
            case "08":
                mesLiteral = "agos.";
                break;
            case "09":
                mesLiteral = "setp.";
                break;
            case "010":
                mesLiteral = "oct.";
                break;
            case "011":
                mesLiteral = "nov.";
                break;

            case "012":
                mesLiteral = "dic.";
                break;
        }

        fechaHora = "Agredado el " + fecha.format(d) + " de " + mesLiteral + " de " + anio.format(d) + " a las " + sdf.format(d);

        return fechaHora;
    }

    private String imageToString(Bitmap bitmap) {

        //metodo que se encarga de convertir el bitmap  a string
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            googleMap.setMyLocationEnabled(true);


        }
        mUiSettings = googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);//Habilita icono de zoon
        mUiSettings.setMapToolbarEnabled(true);//barra de herramienta cuando se toca en el marker
        mUiSettings.setCompassEnabled(true);//brujula

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onComplete(LatLng latLng) {
        Toast.makeText(this, "" + latLng.longitude, Toast.LENGTH_SHORT).show();

        if (latLng != null) {
            googleMap.clear();
            LatLng posicionDePublicacion = new LatLng(latLng.latitude, latLng.longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionDePublicacion, 15));

            googleMap.addMarker(new MarkerOptions()
                    .title("Ubicación añadida")
                    .snippet("para la publicación")
                    .position(posicionDePublicacion));

        }
    }
}
