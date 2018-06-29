package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;


/**
 * Created by EDWIN on 1/6/2018.
 */


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnadirUbicacionCasa extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>, OnMapReadyCallback, View.OnClickListener {


    String FileNameGrupo = "IdGrupo";
    RequestQueue rq;
    JsonRequest jrq;
    GoogleMap mMap;
    public LatLng latLngPasar = null;
    private UiSettings mUiSettings;
    private int contadorDeTap = 0;
    LinearLayout anadirubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_ubicacion_casa);
        rq = Volley.newRequestQueue(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCasa);
        toolbar.setTitle("Añadir ubicación");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        anadirubicacion = findViewById(R.id.anadirubicacionmapacasa);
        anadirubicacion.setOnClickListener(this);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapcasa);

        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

    }
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_add_casa, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.maps_normalcasa:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Toast.makeText(this, "normal", Toast.LENGTH_SHORT).show();
                break;
            case R.id.maps_satelitalcasa:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                Toast.makeText(this, "satelital", Toast.LENGTH_SHORT).show();
                break;
            case R.id.maps_tierracasa:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                Toast.makeText(this, "tiera", Toast.LENGTH_SHORT).show();
                break;
            case R.id.maps_hibridocasa:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                Toast.makeText(this, "híbrido", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Falló al añadir ubicación", Toast.LENGTH_SHORT).show();
            } else {
                if (jsonObject.getString("idgrupomusical").equals("error en la consulta")) {
                    Toast.makeText(this, "Fallo al  añadir ubicación ", Toast.LENGTH_SHORT).show();
                } else {
                    /// idGrupoMusical = jsonObject.getString("idgrupomusical");
                    String defaultValue = "DefaultName";
                    SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                    String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);

                    String idRespuesta= jsonObject.optString("idgrupomusical");//obteniendo resultado
                    if (idGrupoMusical.equals(idRespuesta)){
                        Toast.makeText(this, "Ubicación actualizada exitosamente...", Toast.LENGTH_SHORT).show();

                        finish();
                    }else {
                        Toast.makeText(this, "Se produjo un error al actualizar la ubicacción, intente nuevamente", Toast.LENGTH_SHORT).show();
                    }



                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            mMap.setMyLocationEnabled(true);
            LatLng cbba = new LatLng(-17.393576, -66.156955);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(cbba)
                    .zoom(7)
                    .bearing(90)
                    .tilt(90)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }
        mUiSettings = googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);//Habilita icono de zoon
        mUiSettings.setMapToolbarEnabled(false);//barra de herramienta cuando se toca en el marker
        mUiSettings.setCompassEnabled(true);//brujula
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {


                if (contadorDeTap == 0) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .snippet("para la publicación")
                            .title("Añadir esta ubicación"));
                    marker.showInfoWindow();


                    contadorDeTap++;
                } else {
                    mMap.clear();
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .snippet("para la publicación")
                            .title("Añadir esta ubicación"));
                    marker.showInfoWindow();


                }
                latLngPasar = latLng;

                //  DialogFragmentUbicacion.this.mListener.onComplete(latLng);


            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anadirubicacionmapacasa:
                if (latLngPasar != null) {
                    String defaultValue = "DefaultName";
                    SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                    String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);

                    String urlActualizacioninformacion = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarubicacion.php?idgrupomusical=" + idGrupoMusical + "&latitudg=" + latLngPasar.latitude + "&longitudg=" + latLngPasar.longitude;

                    actualizarDatosUsuario(urlActualizacioninformacion);

                    //Toast.makeText(this, "" + latLngPasar.latitude, Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(this, "Debe seleccionar una ubicación", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }



}

