package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edwinbustamante.gruposcochalos.R;
import com.getbase.floatingactionbutton.FloatingActionButton;


public class PublicacionesProfileFragment extends Fragment {


    public PublicacionesProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_publicaciones_profile, container, false);


        return vista;
    }
}
