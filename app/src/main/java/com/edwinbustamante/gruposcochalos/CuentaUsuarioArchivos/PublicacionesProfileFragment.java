package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.R;
import com.getbase.floatingactionbutton.FloatingActionButton;


public class PublicacionesProfileFragment extends Fragment implements View.OnClickListener {


    public PublicacionesProfileFragment() {
        // Required empty public constructor
    }

    Button btnPublicar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_publicaciones_profile, container, false);

        btnPublicar = (Button) vista.findViewById(R.id.buttonPublicar);
        btnPublicar.setOnClickListener(this);

        return vista;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonPublicar:


                break;
        }
    }
}
