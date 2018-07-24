package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;


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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ScrollView;
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
import com.edwinbustamante.gruposcochalos.domain.Publicacion;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
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
    private Button mOptionButton,rotatFoto;
    final int CODE_GALLERY_REQUEST = 999;
    final int CODE_CAMARA_REQUEST = 100;
    ProgressDialog cargarImagen;
    Bitmap bitmap;
    String FileNameGrupo = "IdGrupo";
    String urlUpload = Constantes.IP_SERVIDOR + "/gruposcochalos/publicarpublicacion.php?";
    private ScrollView scrollViewPublicar;
    private GoogleMap googleMap;
    private UiSettings mUiSettings;
    private MapView mapView;

    private int contadorDeTap = 0;
    boolean mIsLargeLayout;
    ImageView normal, satelital, tierra, hibrido, eliminarmapa;
    private LatLng latLngPubicar = null;
    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 80;

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
        eliminarmapa = findViewById(R.id.eliminarmapapublicar);
        eliminarmapa.setOnClickListener(this);

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

        //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapSeccionada);
        //  mapFragment.getMapAsync(this);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        quitarMapa();
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        magicalPermissions = new MagicalPermissions(this, permissions);
        magicalCamera = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, magicalPermissions);
        rotatFoto=findViewById(R.id.rotarFotop);
        rotatFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap rotado= rotateImage(bitmap,90);
                imageViewPublicacionPrevio.setImageBitmap(rotado);
                bitmap=rotado;
            }
        });
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

    private void quitarMapa() {
        mapView.setVisibility(View.INVISIBLE);
        normal.setVisibility(View.INVISIBLE);
        tierra.setVisibility(View.INVISIBLE);
        satelital.setVisibility(View.INVISIBLE);
        hibrido.setVisibility(View.INVISIBLE);
        eliminarmapa.setVisibility(View.INVISIBLE);
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
                    Toast.makeText(this, "Debes escribir una publicacion y añadir una foto para poder publicar", Toast.LENGTH_SHORT).show();
                } else {
                    publicarPublicacion();
                }

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
            case R.id.eliminarmapapublicar:
                quitarMapa();
                latLngPubicar = null;
                Toast.makeText(this, "Ubicación eliminada", Toast.LENGTH_SHORT).show();
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

                    magicalCamera.takePhoto();

                } else if (option[which] == "Elegir de galeria") {

                    magicalCamera.selectedPicture("Selecciona una aplicación");

                } else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        magicalCamera.resultPhoto(requestCode, resultCode, data);


        //this is for rotate picture in this method
        //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);
        //alistando para enviar al servidor
        bitmap = magicalCamera.getPhoto();
        //with this form you obtain the bitmap (in this example set this bitmap in image view)
        imageViewPublicacionPrevio.setImageBitmap(bitmap);
        rotatFoto.setVisibility(View.VISIBLE);
        //if you need save your bitmap in device use this method and return the path if you need this
        //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.
       /* String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(), "gc", "Grupos Cochalos", MagicalCamera.JPEG, true);

        if (path != null) {
            Toast.makeText(Publicar.this, "Foto guardado en el dispositivo " + path, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Publicar.this, "No se guardo la foto", Toast.LENGTH_SHORT).show();
        }*/
    }

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
                Toast.makeText(Publicar.this, "error al publicar intente nuevamente..." , Toast.LENGTH_SHORT).show();
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
                if (latLngPubicar != null) {
                    params.put("latitud", "" + latLngPubicar.latitude);
                    params.put("longitud", "" + latLngPubicar.longitude);
                } else {
                    params.put("latitud", "no");
                    params.put("longitud", "no");
                }
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
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");//se da el formato en este caso la hora los minutos y los segundos
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
        latLngPubicar = latLng;

        if (latLngPubicar != null) {
            googleMap.clear();

            mapView.setVisibility(View.VISIBLE);
            normal.setVisibility(View.VISIBLE);
            tierra.setVisibility(View.VISIBLE);
            satelital.setVisibility(View.VISIBLE);
            hibrido.setVisibility(View.VISIBLE);
            eliminarmapa.setVisibility(View.VISIBLE);
            LatLng posicionDePublicacion = new LatLng(latLngPubicar.latitude, latLngPubicar.longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicionDePublicacion, 15));

            googleMap.addMarker(new MarkerOptions()
                    .title("Ubicación añadida")
                    .snippet("para la publicación")
                    .position(posicionDePublicacion));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
