package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;


/**
 * Created by EDWIN on 1/6/2018.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import java.util.Objects;


public class DialogFragmentUbicacion extends DialogFragment
        implements OnMapReadyCallback, View.OnClickListener {

    private UiSettings mUiSettings;
    private int contadorDeTap = 0;
    GoogleMap mMap;
    ImageView cerrarDialog, normal, hibrido, satelital, tierra;
    LinearLayout anadirpublicacion;
    public LatLng latLngPasar = null;

    public DialogFragmentUbicacion() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_layout, container, false);
        cerrarDialog = rootView.findViewById(R.id.cerrarDialog);
        cerrarDialog.setOnClickListener(this);
        normal = rootView.findViewById(R.id.tiponormal);
        normal.setOnClickListener(this);
        hibrido = rootView.findViewById(R.id.tipohibrido);
        hibrido.setOnClickListener(this);
        satelital = rootView.findViewById(R.id.tiposatelital);
        satelital.setOnClickListener(this);
        tierra = rootView.findViewById(R.id.tipotierra);
        tierra.setOnClickListener(this);
        anadirpublicacion = rootView.findViewById(R.id.anadirubicacionmapa);
        anadirpublicacion.setOnClickListener(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assert getFragmentManager() != null;
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));
        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
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

    private OnCompleteListener mListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cerrarDialog:
                dismiss();
                break;
            case R.id.tiponormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Toast.makeText(getContext(), "normal", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tiposatelital:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                Toast.makeText(getContext(), "satelital", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tipohibrido:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                Toast.makeText(getContext(), "híbrido", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tipotierra:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                Toast.makeText(getContext(), "tiera", Toast.LENGTH_SHORT).show();
                break;
            case R.id.anadirubicacionmapa:
                if (latLngPasar != null) {
                    DialogFragmentUbicacion.this.mListener.onComplete(latLngPasar);
                    dismiss();
                } else {

                    Toast.makeText(getContext(), "Debe seleccionar una ubicación", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;

        }
    }

    public static interface OnCompleteListener {

        public abstract void onComplete(LatLng latLng);
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
}
