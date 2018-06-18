package com.edwinbustamante.gruposcochalos.VisitarMapa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.ads.AdRequest.LOGTAG;

public class MapsActivityVisitar extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private String latitud, longitud, tituloMarcador;
    private UiSettings mUiSettings;
    private static final int LOCATION_REQUEST = 500;
    LocationManager locationManager;
    double latitudOrigen, longitudOrigen;
    private boolean dibujado = false;
    private TextView nuevaRuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_visitar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setTitle("Informacion de Grupo Musical");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapVisit);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        nuevaRuta = findViewById(R.id.nuevaruta);
        nuevaRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dibujado = false;
            }
        });
        latitud = (String) getIntent().getExtras().get("latitud");
        longitud = (String) getIntent().getExtras().get("longitud");
        tituloMarcador = (String) getIntent().getExtras().get("titulomarcador");
        // Toast.makeText(this, latitud + longitud, Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        mapFragment.getMapAsync(this);

    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);

        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        //Toast.makeText(this, " Mensaje 1 localizacion Agregada", Toast.LENGTH_SHORT).show();
        //  mensaje1.setText("Localización agregada");
        //mensaje2.setText("");
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

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {

            if (dibujado == false) {
                latitudOrigen = loc.getLatitude();
                longitudOrigen = loc.getLongitude();
                LatLng origen = new LatLng(latitudOrigen, longitudOrigen);

                Address DirCalle;
                String direccion = "Direción Desconocida";
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = null;
                    list = geocoder.getFromLocation(
                            loc.getLatitude(), loc.getLongitude(), 1);
                    if (!list.isEmpty()) {
                        DirCalle = list.get(0);
                        DirCalle.getAddressLine(0);
                        if (DirCalle != null) {
                            direccion = DirCalle.getAddressLine(0);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Marker markerOrigen = mMap.addMarker(new MarkerOptions()
                        .position(origen)
                        .snippet(direccion)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.elegir))
                        .title("Tu ubicación actual"));


                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Create background thread to connect and get data
                    LatLng ubicacionVisitante = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                    String url = getRequestUrl(ubicacionVisitante, origen);
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);

                } else {
                    Toast.makeText(this, "No tienes conexion a Internet", Toast.LENGTH_SHORT).show();
                }

                dibujado = true;
            }

          /*  try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);

                    // Toast.makeText(this, "Mi direccion es: \n"+ DirCalle.getAddressLine(0), Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MapsActivityVisitar mainActivity;

        public MapsActivityVisitar getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MapsActivityVisitar mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();

            // Toast.makeText(mainActivity, "mensaje1" + Text, Toast.LENGTH_SHORT).show();
            this.mainActivity.setLocation(loc);

        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(mainActivity, "Debe activar el GPS para usar la aplicación", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(mainActivity, "GPS activado estas listo para usar la aplicación", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mUiSettings = googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);//Habilita icono de zoon
        mUiSettings.setMapToolbarEnabled(true);//barra de herramienta cuando se toca en el marker
        mUiSettings.setCompassEnabled(true);//brujula
        // Add a marker in Sydney and move the camera
        LatLng ubicacionVisitante = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        Marker markerDestino = mMap.addMarker(new MarkerOptions()
                .position(ubicacionVisitante)
                .snippet("clik para llegar a ella")
                .title(tituloMarcador));
        markerDestino.showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ubicacionVisitante)
                .zoom(15)
                .bearing(90)
                .tilt(90)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

/*
        LatLng origen = new LatLng(latitudOrigen, longitudOrigen);
        String url = getRequestUrl(ubicacionVisitante, origen);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);*/
        //mMap.addPolyline(new PolylineOptions().add(ubicacionVisitante,origen).width(5).color(Color.RED));

    }

    private String getRequestUrl(LatLng ubicacionVisitante, LatLng origen) {
        String str_origen = "origin=" + origen.latitude + "," + origen.longitude;
        String str_dest = "destination=" + ubicacionVisitante.latitude + "," + ubicacionVisitante.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String param = str_origen + "&" + str_dest + "&" + sensor + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }

        public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
                JSONObject jsonObject = null;
                List<List<HashMap<String, String>>> routes = null;
                try {
                    jsonObject = new JSONObject(strings[0]);
                    DirectionsParser directionsParser = new DirectionsParser();
                    routes = directionsParser.parse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
                ArrayList points = null;
                PolylineOptions polylineOptions = null;
                for (List<HashMap<String, String>> path : lists) {
                    points = new ArrayList();
                    polylineOptions = new PolylineOptions();
                    for (HashMap<String, String> point : path) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lon = Double.parseDouble(point.get("lon"));
                        points.add(new LatLng(lat, lon));
                    }
                    polylineOptions.addAll(points);
                    polylineOptions.width(5);
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.geodesic(true);
                }
                if (polylineOptions != null) {
                    mMap.addPolyline(polylineOptions);
                } else {
                    Toast.makeText(MapsActivityVisitar.this, "Trabajando en encontrar la mejor ruta, aguarde..!", Toast.LENGTH_SHORT).show();
                    //  Toast.makeText(MapsActivityVisitar.this, "Aguarde un momento, estamos trabajando para encontrar una ruta", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_add, menu);
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
            case R.id.maps_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.maps_hibrido:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.maps_satelital:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.maps_tierra:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                break;
        }

        return true;
    }


}
