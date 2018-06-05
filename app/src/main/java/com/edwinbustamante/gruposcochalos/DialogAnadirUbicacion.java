package com.edwinbustamante.gruposcochalos;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by EDWIN on 30/5/2018.
 */

public class DialogAnadirUbicacion extends DialogFragment {
   /* private static View view;
    private Button pasarUbicacion;
    private OnCompleteListener mListener;
    private GoogleMap googleMap;
    private UiSettings mUiSettings;
    private int contadorDeTap = 0;

    private LinearLayout anadirUbicacionMapa;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        //return inflater.inflate(R.layout.dialog_layout, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(85, 15, 15, 15)));


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {

            view = inflater.inflate(R.layout.dialog_layout, container, false);

            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarAn);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.satelitalU:
                            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                        case R.id.normalU:
                            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case R.id.hibridoU:
                            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case R.id.tierraU:
                            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                        case R.id.cancelarU:
                         getDialog().dismiss();
                            break;
                        default:
                            break;
                    }

                    return true;
                }
            });
            toolbar.inflateMenu(R.menu.menu_anadir_map_dialog);
            toolbar.setTitle("Elije tipo de mapa");

            anadirUbicacionMapa = view.findViewById(R.id.anadirubicacionmapa);
            SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.mapAnadirUbicacion);
            mapFragment.getMapAsync(this);

        } catch (InflateException e) {
        *//* map is already there, just return view as it is *//*
        }


        return view;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }


    public static final String TAG = "FullScreenDialog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener) activity;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            googleMap.setMyLocationEnabled(true);
            LatLng cbba = new LatLng(-17.393576, -66.156955);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(cbba)
                    .zoom(7)
                    .bearing(90)
                    .tilt(90)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        }
        mUiSettings = googleMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);//Habilita icono de zoon
        mUiSettings.setMapToolbarEnabled(false);//barra de herramienta cuando se toca en el marker
        mUiSettings.setCompassEnabled(true);//brujula
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                if (contadorDeTap == 0) {
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .snippet("Copmo Funciona esto")
                            .title("Añadir esta ubicación para la publicacion"));
                    marker.showInfoWindow();


                    contadorDeTap++;
                } else {
                    googleMap.clear();
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .snippet("Copmo Funciona esto")
                            .title("Añadir esta ubicación para la publicacion"));
                    marker.showInfoWindow();


                }

                anadirUbicacionMapa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (latLng != null) {
                            DialogAnadirUbicacion.this.mListener.onComplete(latLng);
                          getDialog().dismiss();

                        } else {
                            Toast.makeText(getContext(), "Debes tener una ubicación selecionada en el mapa", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    public static interface OnCompleteListener {

        public abstract void onComplete(LatLng latLng);
    }
*/

}