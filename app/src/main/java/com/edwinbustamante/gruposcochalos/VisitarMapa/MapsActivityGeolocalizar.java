package com.edwinbustamante.gruposcochalos.VisitarMapa;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.Main;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.VisitanteArchivos.InformacionGrupoVisitante;
import com.edwinbustamante.gruposcochalos.domain.GrupoMusical;
import com.edwinbustamante.gruposcochalos.domain.Resultado;
import com.edwinbustamante.gruposcochalos.service.ItunesAPI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivityGeolocalizar extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private String latitud, longitud, tituloMarcador;
    private UiSettings mUiSettings;
    private static final int LOCATION_REQUEST = 500;
    LocationManager locationManager;
    double latitudOrigen, longitudOrigen;
    private boolean dibujado = false;
    private static final String GOOGLE_API_KEY = "AIzaSyC4QP4S06vfGHpEAdSzJIwnnelz1AD1VvE";
    List<GrupoMusical> grupoMusicales = new ArrayList<>();


    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Create background thread to connect and get data
            grupoMusicales.clear();
            obtenerCanciones();
        } else {
            Toast.makeText(this, "No tienes conexion a Internet", Toast.LENGTH_SHORT).show();
        }

    }


    private void obtenerCanciones() {
        Call<Resultado> resultadoCall = ItunesAPI.getItunesService().getCanciones();

        resultadoCall.enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                Resultado body = response.body();
                List<GrupoMusical> results = body.getListagrupos();
                grupoMusicales.addAll(results);

                for (int i = 0; i < grupoMusicales.size(); i++) {
                    if (grupoMusicales.get(i).getLatitudg().equals("no")) {

                    } else {

                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                View infoMarker = null;
                                if (infoMarker == null&&!marker.getTitle().equals("Tu ubicación actual")) {
                                    infoMarker = getLayoutInflater().inflate(R.layout.info_marker, null);
                                }
                                if (!marker.getTitle().equals("Tu ubicación actual")) {
                                    TextView titulo = (TextView) infoMarker.findViewById(R.id.titulo_info);
                                    titulo.setText(marker.getTitle());
                                    TextView snippet = (TextView) infoMarker.findViewById(R.id.snippet_info);
                                    snippet.setText("Click aquí para mas información");
                                    ImageView markerPerfil = (ImageView) infoMarker.findViewById(R.id.marker_perfil);
                                    Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + marker.getSnippet())
                                            .error(R.drawable.perfilmusic)
                                            .resize(50, 50)
                                            .into(markerPerfil);
                                }
                                return infoMarker;
                            }
                        });
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                boolean encontrado = false;
                                int i = 0;
                                while (encontrado == false && i < grupoMusicales.size()) {
                                    String nombre = grupoMusicales.get(i).getNombre();
                                    if (nombre.equals(marker.getTitle())) {

                                        Intent intent = new Intent(getApplicationContext(), InformacionGrupoVisitante.class);
                                        intent.putExtra("grupomusical", grupoMusicales.get(i));
                                        startActivity(intent);
                                        encontrado = true;
                                    }
                                    i++;
                                }
                            }
                        });

                        LatLng ubicacionGrupo = new LatLng(Double.parseDouble(grupoMusicales.get(i).getLatitudg()), Double.parseDouble(grupoMusicales.get(i).getLongitudg()));
                        Marker markerGrupo = mMap.addMarker(new MarkerOptions()
                                .position(ubicacionGrupo)
                                .snippet(grupoMusicales.get(i).getFotoperfil())
                                .title(grupoMusicales.get(i).getNombre()));
                    }
                }

            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {
                Toast.makeText(MapsActivityGeolocalizar.this, "Ha ocurrido un error al obtener la ubicacion de los grupos musicales", Toast.LENGTH_LONG).show();
                Log.e("error", t.getMessage());
                noHayConexion();
                t.printStackTrace();
            }
        });

    }

    private void noHayConexion() {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.mapVisitgeolocalizacion), "Ups parece que no tienes conexion a internet o ocurrio algún problema en Grupos Cochalos?", Snackbar.LENGTH_INDEFINITE).setDuration(10000)
                .setAction("Reintentar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        grupoMusicales.clear();
                        obtenerCanciones();


                    }
                });
        snackbar.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_geolocalizar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbargeolocalizacion);
        toolbar.setTitle("Geolocalización de grupos musicales");
        // toolbar.setTitle("Informacion de Grupo Musical");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapVisitgeolocalizacion);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Todo Aqui estan los destinos implementar
        //latitud = (String) getIntent().getExtras().get("latitud");
        //longitud = (String) getIntent().getExtras().get("longitud");
        //tituloMarcador = (String) getIntent().getExtras().get("titulomarcador");
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
                markerOrigen.showInfoWindow();

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(origen)
                        .zoom(13)
                        .bearing(90)
                        .tilt(90)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    /**
                     // Create background thread to connect and get data
                     LatLng ubicacionVisitante = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                     String url = getRequestUrl(ubicacionVisitante, origen);
                     MapsActivityVisitar.TaskRequestDirections taskRequestDirections = new MapsActivityVisitar.TaskRequestDirections();
                     taskRequestDirections.execute(url);
                     */
                } else {
                    Toast.makeText(this, "No tienes conexion a Internet", Toast.LENGTH_SHORT).show();
                }

                dibujado = true;
            }

        }
    }

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
        mUiSettings.setMapToolbarEnabled(false);//barra de herramienta cuando se toca en el marker
        mUiSettings.setCompassEnabled(true);//brujula
        mMap.setOnMarkerClickListener(this);

        // Add a marker in Sydney and move the camera
/*
        LatLng origen = new LatLng(latitudOrigen, longitudOrigen);
        String url = getRequestUrl(ubicacionVisitante, origen);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);*/
        //mMap.addPolyline(new PolylineOptions().add(ubicacionVisitante,origen).width(5).color(Color.RED));

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.getTitle().equals("Tu ubicación actual")) {
            mUiSettings.setMapToolbarEnabled(false);
        }else{
            mUiSettings.setMapToolbarEnabled(true);
        }

        return false;
    }


    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MapsActivityGeolocalizar mainActivityGeolocalizar;

        public MapsActivityGeolocalizar getMainActivityGeolocalizar() {
            return mainActivityGeolocalizar;
        }

        public void setMainActivity(MapsActivityGeolocalizar mainActivity) {
            this.mainActivityGeolocalizar = mainActivity;
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
            this.mainActivityGeolocalizar.setLocation(loc);//enviando para que agregue la localizacion actual

        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(mainActivityGeolocalizar, "Debe activar el GPS para usar la aplicación", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(mainActivityGeolocalizar, "GPS activado estas listo para usar la aplicación", Toast.LENGTH_SHORT).show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_geolocalizacion, menu);
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
            case R.id.maps_normalg:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.maps_hibridog:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.maps_satelitalg:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.maps_tierrag:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                break;
        }

        return true;
    }



}
