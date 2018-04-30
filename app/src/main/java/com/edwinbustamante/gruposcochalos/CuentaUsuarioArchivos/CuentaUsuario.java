package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edwinbustamante.gruposcochalos.HeaderDialogFragment;
import com.edwinbustamante.gruposcochalos.ImagenFull.FulImagen;
import com.edwinbustamante.gruposcochalos.InformacionProfileFragment;
import com.edwinbustamante.gruposcochalos.LoginActivity;
import com.edwinbustamante.gruposcochalos.Main;
import com.edwinbustamante.gruposcochalos.Objetos.User;
import com.edwinbustamante.gruposcochalos.PublicacionesProfileFragment;
import com.edwinbustamante.gruposcochalos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CuentaUsuario extends AppCompatActivity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        User USUARIO = (User) getIntent().getExtras().getSerializable("objetoUsuario");

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        /*Glide.with(CuentaUsuario.this)
                .load(imageUrl)
                .fitCenter()
                // .skipMemoryCache(true)//Almacenando en cache
                .centerCrop()
                .into(cuenta_perfil);

     */
    }

    /**
     * ################################################################################
     * TODOS DEL TAB Y FRAGMENT
     * ##############################################################################
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void cerrarSesion(View view) {


    }

    public void ShowDialog(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Grupos Cochalos");
        builder.setMessage("Desea Cerrar Sesion..?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }


}


/**
 * ################################################################################
 * Mi clase adaptador...
 * ##############################################################################
 */
class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        switch (i) {
            case 0:
                fragment = new InformacionProfileFragment();
                break;
            case 1:
                fragment = new PublicacionesProfileFragment();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "INFORMACIÓN";
            case 1:
                return "PUBLICACIONES";
        }
        return null;
    }

}