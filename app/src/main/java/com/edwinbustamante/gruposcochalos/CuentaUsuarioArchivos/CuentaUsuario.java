package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;


import com.edwinbustamante.gruposcochalos.Objetos.User;
import com.edwinbustamante.gruposcochalos.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CuentaUsuario extends AppCompatActivity {

    ImageView imageViewCerrarSesion;
    FloatingActionButton fab;
    boolean click = false;
    String FileNameEstado = "estado";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_advertencia)
                    .setTitle("Grupos Cochalos")
                    .setMessage("Desea cerrar sesion?")
                    .setNegativeButton(android.R.string.cancel, null)// sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
// Salir
                            BorrarEstadoIdGrupoMusical();
                            BorrarIdGrupoMusical();
                            BorrarIdUsuario();
                            CuentaUsuario.this.finish();
                        }
                    })
                    .show();

// Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
// para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        User USUARIO = (User) getIntent().getExtras().getSerializable("objetoUsuario");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click = !click;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                            android.R.interpolator.fast_out_slow_in);

                    view.animate()
                            .rotation(click ? 360f : 0)
                            .setInterpolator(interpolador)
                            .start();
                }
                String defaultValue = "DefaultName";
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(FileNameEstado, Context.MODE_PRIVATE);
                String bandera = sharedPreferences.getString("estado", defaultValue);

                if (bandera.equals("1")) {
                    Intent intent = new Intent(view.getContext(), Publicar.class);
                    startActivity(intent);
                    Toast.makeText(CuentaUsuario.this, "Escribe una publicación", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(view.getContext())
                            .setIcon(R.drawable.ic_advertencia)
                            .setTitle("RESTRICCIÓN Y CONDICIONES DE GRUPOS COCHALOS")
                            .setMessage("Debes completar tu informacion, para poder ser habilitado por el administrador," +
                                    " una vez habilitado tu grupo musical sera reflejado en la interfaz principal de esta aplicación," +
                                    " quedando activa para la interacción de los usuarios visitantes...." + "\n" + "\n" +
                                    "USO DE LA APLICACION MOVIL Y SUS SERVICIOS" + "\n"
                                    + "En la aplicacion Grupos Cochalos no deben introducirse ni difundirse propagandas de caracter racista" +
                                    ", pornográfico, contenidos falsos, virus y software nocivo. " +
                                    "Si se detecta incumplimiento de las condiciones mencionadas se deshabilitará esta cuenta del sistema" + "\n" + "\n" +
                                    "Aguarde la habilitación del administrador gracias...")
                            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        final MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Toast.makeText(CuentaUsuario.this, "" + tab.getPosition(), Toast.LENGTH_SHORT).show();
                if (tab.getPosition() != 0) {
                    fab.show();//aparecer
                    viewPager.setCurrentItem(tab.getPosition());
                    animateFab(tab.getPosition());
                } else {

                    fab.hide();//desvanecer
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imageViewCerrarSesion = (ImageView) findViewById(R.id.imageViewCerrarSesion);
        imageViewCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(true);
                builder.setIcon(R.drawable.ic_advertencia);
                builder.setTitle("Grupos Cochalos");
                builder.setMessage("Desea cerrar sesion..?");
                builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BorrarEstadoIdGrupoMusical();
                        BorrarIdGrupoMusical();
                        BorrarIdUsuario();

                        finish();

                    }
                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });

    }

    String FileName = "myUserId";
    String FileNameGrupo = "IdGrupo";

    public void BorrarIdUsuario() {
        SharedPreferences sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idusuario", "");
        editor.commit();
    }

    private void BorrarIdGrupoMusical() {
        SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idgrupomusical", "");
        editor.commit();
    }

    private void BorrarEstadoIdGrupoMusical() {
        SharedPreferences sharedPreferences = getSharedPreferences("estado", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("estado", "");
        editor.commit();
    }

    /**
     * ################################################################################
     * TODOS DEL TAB Y FRAGMENT
     * ##############################################################################
     */
    int[] colorIntArray = {R.color.green, R.color.pink};
    int[] iconIntArray = {R.drawable.ic_camara_de_fotos, R.drawable.ic_publicar};


    protected void animateFab(final int position) {
        fab.clearAnimation();
        // Scale down animation
        ScaleAnimation shrink = new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change FAB color and icon
                fab.setBackgroundTintList(getResources().getColorStateList(colorIntArray[position]));

                fab.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));

                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

/*
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
            default:
        }
        return null;
    }

}