package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.ImagenFull.FulImagen;
import com.edwinbustamante.gruposcochalos.ImagenFull.FullFotoPortada;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.SelectecDateFragment;
import com.edwinbustamante.gruposcochalos.VisitarMapa.MapsActivityVisitar;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;


public class InformacionProfileFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject>, View.OnTouchListener, OnMapReadyCallback {

    private String idGrupoMusical = "";
    // UI references. animacion
    RelativeLayout linearLayoutanimacion;
    AnimationDrawable animacion;
    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialogFotoSubir;
    private Toolbar toolbar;
    private ImageView foto_perfil, foto_portada;
    private TextView usuario, nombreGrupo, generoMusica, movil1, movil2, movilWhatsApp, linkFacebook, linkYoutube;
    private TextView informacionEdit, contactosEdit, direccionEdit;
    private ImageView imageViewMovil1, imageViewMovil2, imageViewMovilWhasapp, anadirubicaciocasa, irfacebook, imageViewYoutube;
    String FileName = "myUserId";
    String urlImagen;
    String urlImagenPortada;
    private LinearLayout editMainCuenta;
    private ImageView agendaGrupo;

    RequestQueue rq;
    JsonRequest jrq;
    String idUsuarioInput;
    private String latitudg;
    private String longitudg;
    static final int DATE_DIALOG_ID = 999;
    List<Calendar> calendarioguardar;
    private WebView webadmi;
    private GoogleMap map;
    private UiSettings mUiSettings;
    MapView mapView;

    public InformacionProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        String urlActualizacionTraerDatos = Constantes.IP_SERVIDOR + "gruposcochalos/traerdatosusuario.php?idusuario=" + idUsuarioInput;
        actualizarDatosUsuario(urlActualizacionTraerDatos);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View vista = inflater.inflate(R.layout.fragment_informacion_profile, container, false);
        rq = Volley.newRequestQueue(getActivity());
        //ANIMACION DEL FONDO
        linearLayoutanimacion = (RelativeLayout) vista.findViewById(R.id.profile_layout);
        animacion = (AnimationDrawable) linearLayoutanimacion.getBackground();
        animacion.setEnterFadeDuration(4500);
        animacion.setExitFadeDuration(4500);
        animacion.start();
        foto_portada = (ImageView) vista.findViewById(R.id.header_cover_image);
        foto_portada.setOnClickListener(this);
        foto_perfil = (ImageView) vista.findViewById(R.id.foto_perfil);
        foto_perfil.setOnClickListener(this);
        //progressDialogFotoSubir = new ProgressDialog(this);
        usuario = (TextView) vista.findViewById(R.id.usuario);
        usuario.setOnClickListener(this);
        nombreGrupo = (TextView) vista.findViewById(R.id.nombregrupo);
        String defaultValue = "DefaultName";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileName, Context.MODE_PRIVATE);
        idUsuarioInput = sharedPreferences.getString("idusuario", defaultValue);
        //Toast.makeText(getContext(), idUsuarioInput, Toast.LENGTH_SHORT).show();
        nombreGrupo.setOnClickListener(this);
        generoMusica = (TextView) vista.findViewById(R.id.texgeneroMusica);
        generoMusica.setOnClickListener(this);
        informacionEdit = vista.findViewById(R.id.texViewInformacion);
        informacionEdit.setOnClickListener(this);
        movil1 = (TextView) vista.findViewById(R.id.texViewMovil1);
        movil1.setOnClickListener(this);
        movil2 = (TextView) vista.findViewById(R.id.texViewMovil2);
        movil2.setOnClickListener(this);
        movilWhatsApp = (TextView) vista.findViewById(R.id.texViewMovilWhatsApp);
        movilWhatsApp.setOnClickListener(this);
        linkFacebook = (TextView) vista.findViewById(R.id.texViewLinkFacebook);
        linkFacebook.setOnClickListener(this);
        irfacebook = (ImageView) vista.findViewById(R.id.irFacebookCuenta);
        irfacebook.setOnClickListener(this);

        contactosEdit = (TextView) vista.findViewById(R.id.texViewContactos);
        contactosEdit.setOnClickListener(this);
        direccionEdit = (TextView) vista.findViewById(R.id.texViewDireccion);
        direccionEdit.setOnClickListener(this);
        imageViewMovil1 = (ImageView) vista.findViewById(R.id.imageViewMovil1);
        imageViewMovil1.setOnClickListener(this);
        imageViewMovil2 = (ImageView) vista.findViewById(R.id.imageViewMovil2);
        imageViewMovil2.setOnClickListener(this);
        imageViewMovilWhasapp = (ImageView) vista.findViewById(R.id.imageViewMovilWhatsapp);
        imageViewMovilWhasapp.setOnClickListener(this);
        anadirubicaciocasa = (ImageView) vista.findViewById(R.id.anadirubicacioncasa);

        anadirubicaciocasa.setOnClickListener(this);
        agendaGrupo = (ImageView) vista.findViewById(R.id.agendaGrupo);
        agendaGrupo.setOnClickListener(this);
        linkYoutube = (TextView) vista.findViewById(R.id.linkYoutube);
        linkYoutube.setOnClickListener(this);
        imageViewYoutube = (ImageView) vista.findViewById(R.id.imageViewYoutube);
        imageViewYoutube.setOnClickListener(this);
        webadmi = (WebView) vista.findViewById(R.id.webviewyoutubeadmi);
        webadmi.getSettings().setJavaScriptEnabled(true);
        webadmi.setWebChromeClient(new WebChromeClient() {

        });
        webadmi.setOnTouchListener(this);
        mapView = (MapView) vista.findViewById(R.id.visitarubicacioncasa);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        MapsInitializer.initialize(this.getActivity());

        return vista;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void actualizarDatosUsuario(String urls) {
        String url = urls;
        url = url.replace(" ", "%20");
        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usuario:
                Intent usuarioIntent = new Intent(getContext(), EditUsuario.class);
                usuarioIntent.putExtra("usuario", usuario.getText().toString());
                startActivity(usuarioIntent);
                break;
            case R.id.nombregrupo:
                /*
                ALERT DIALOG QUE SE LANZA PARA CAMBIAR EL NOMBRE DEL GRUPO MUSICAL
                **/
                final EditText input;
                AlertDialog.Builder dialogoEditNombre = new AlertDialog.Builder(getContext());
                dialogoEditNombre.setTitle("Desea cambiar el nombre del Grupo Musical..?");
                input = new EditText(getContext());
                input.setText(nombreGrupo.getText().toString());
                input.setSelection(nombreGrupo.getText().toString().length());
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});//maximo de caracterres
                dialogoEditNombre.setView(input);

                dialogoEditNombre.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionNombre = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarnombre.php?idgrupomusical=" + idGrupoMusical + "&nombre=" + input.getText().toString();
                        actualizarDatosUsuario(urlActualizacionNombre);
                        // Toast.makeText(getContext(), input.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogoEditNombre.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarNombre = dialogoEditNombre.create();
                editarNombre.show();
                break;
            case R.id.texgeneroMusica:
                final EditText inputGenero;
                AlertDialog.Builder dialogoEditGenero = new AlertDialog.Builder(getContext());
                dialogoEditGenero.setTitle("Desea cambiar el género música..?");
                inputGenero = new EditText(getContext());
                inputGenero.setText(generoMusica.getText().toString());
                inputGenero.setSelection(generoMusica.getText().toString().length());
                inputGenero.setFilters(new InputFilter[]{new InputFilter.LengthFilter(70)});//maximo de caracterres
                dialogoEditGenero.setView(inputGenero);

                dialogoEditGenero.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionNombre = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizargenero.php?idgrupomusical=" + idGrupoMusical + "&genero=" + inputGenero.getText().toString();
                        actualizarDatosUsuario(urlActualizacionNombre);
                    }
                });
                dialogoEditGenero.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarGenero = dialogoEditGenero.create();
                editarGenero.show();
                break;
            case R.id.agendaGrupo:
                String defaultValue = "DefaultName";
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                String idGrupoMusica = sharedPreferences.getString("idgrupomusical", defaultValue);
                Intent agendaIntent = new Intent(getContext(), AgendaGrupo.class);
                agendaIntent.putExtra("rol", "usuario");
                agendaIntent.putExtra("idgrupomusical", idGrupoMusica);
                startActivity(agendaIntent);
                break;

            /** case R.id.imageViewYoutube:
             Uri uris = Uri.parse(linkYoutube.getText().toString());
             Intent it = new Intent(Intent.ACTION_VIEW, uris);
             startActivity(it);
             break;*/
            case R.id.imageViewYoutube:
                Intent youtubelink = new Intent(getContext(), EditLinkYoutube.class);
                youtubelink.putExtra("linkyoutube", linkYoutube.getText().toString());
                startActivity(youtubelink);
                break;
            case R.id.texViewMovil1:
                final EditText inputMovil1;
                AlertDialog.Builder dialogoEditMovil1 = new AlertDialog.Builder(getContext());
                dialogoEditMovil1.setTitle("Desea cambiar el número de movil 1..?");
                inputMovil1 = new EditText(getContext());
                inputMovil1.setText(movil1.getText().toString());
                inputMovil1.setSelection(movil1.getText().toString().length());
                inputMovil1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//maximo de caracterres
                inputMovil1.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                dialogoEditMovil1.setView(inputMovil1);

                dialogoEditMovil1.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionMovil2 = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarnumtelefono.php?idgrupomusical=" + idGrupoMusical + "&numtelefono=" + inputMovil1.getText().toString();
                        actualizarDatosUsuario(urlActualizacionMovil2);

                        //       Toast.makeText(getContext(), inputMovil1.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogoEditMovil1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarMovil1 = dialogoEditMovil1.create();
                editarMovil1.show();
                break;
            case R.id.texViewMovil2:
                final EditText inputMovil2;
                AlertDialog.Builder dialogoEditMovil2 = new AlertDialog.Builder(getContext());
                dialogoEditMovil2.setTitle("Desea cambiar el número de movil 2..?");
                inputMovil2 = new EditText(getContext());
                inputMovil2.setText(movil2.getText().toString());
                inputMovil2.setSelection(movil2.getText().toString().length());
                inputMovil2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//maximo de caracterres
                inputMovil2.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                dialogoEditMovil2.setView(inputMovil2);

                dialogoEditMovil2.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionMovil2 = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarnumtelefonodos.php?idgrupomusical=" + idGrupoMusical + "&numtelefonodos=" + inputMovil2.getText().toString();
                        actualizarDatosUsuario(urlActualizacionMovil2);

                        //Toast.makeText(getContext(), inputMovil2.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogoEditMovil2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarMovil2 = dialogoEditMovil2.create();
                editarMovil2.show();
                break;
            case R.id.texViewMovilWhatsApp:
                final EditText inputMovilWhatsApp;
                AlertDialog.Builder dialogoEditMovilWhatsApp = new AlertDialog.Builder(getContext());
                dialogoEditMovilWhatsApp.setTitle("Desea cambiar el número de movil para WhatsApp?");
                inputMovilWhatsApp = new EditText(getContext());
                inputMovilWhatsApp.setText(movilWhatsApp.getText().toString());
                inputMovilWhatsApp.setSelection(movilWhatsApp.getText().toString().length());
                inputMovilWhatsApp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});//maximo de caracterres
                inputMovilWhatsApp.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                dialogoEditMovilWhatsApp.setView(inputMovilWhatsApp);

                dialogoEditMovilWhatsApp.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);
                        String urlActualizacionWhatsApp = Constantes.IP_SERVIDOR + "gruposcochalos/actualizarinformacion/actualizarnumwhatsapp.php?idgrupomusical=" + idGrupoMusical + "&numwhatsapp=" + inputMovilWhatsApp.getText().toString();
                        actualizarDatosUsuario(urlActualizacionWhatsApp);
                        //Toast.makeText(getContext(), inputMovilWhatsApp.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogoEditMovilWhatsApp.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog editarMovilWhatsApp = dialogoEditMovilWhatsApp.create();
                editarMovilWhatsApp.show();
                break;
            case R.id.texViewInformacion:
                Intent i = new Intent(getContext(), EditInformacion.class);
                i.putExtra("informacion", informacionEdit.getText().toString());
                startActivity(i);
                break;
            case R.id.texViewContactos:
                Intent intt = new Intent(getContext(), EditContactos.class);
                intt.putExtra("contactos", contactosEdit.getText().toString());
                startActivity(intt);
                break;
            case R.id.texViewDireccion:
                Intent direccionIntent = new Intent(getContext(), EditDireccion.class);
                direccionIntent.putExtra("direccion", direccionEdit.getText().toString());
                startActivity(direccionIntent);
                break;
            case R.id.foto_perfil:

                Intent imagenFu = new Intent(getContext(), FulImagen.class);
                imagenFu.putExtra("foto", urlImagen);
                startActivity(imagenFu);
                break;
            case R.id.header_cover_image:

                Intent imagenPortada = new Intent(getContext(), FullFotoPortada.class);
                imagenPortada.putExtra("foto", urlImagenPortada);
                startActivity(imagenPortada);
                break;

            case R.id.imageViewMovil1:
                llamar(movil1.getText().toString());
                break;
            case R.id.imageViewMovil2:
                llamar(movil2.getText().toString());
                break;
            case R.id.imageViewMovilWhatsapp:
                PackageManager pm = getActivity().getPackageManager();
                try {
                    // Intent launchIntent =getContext().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                    //startActivity(launchIntent);

                    Uri uri = Uri.parse("smsto:" + movilWhatsApp.getText().toString());
                    Intent ir = new Intent(Intent.ACTION_SENDTO, uri);

                    if (estaInstaladaAplicacion("com.whatsapp", getContext())) {
                        ir.setPackage("com.whatsapp");
                        startActivity(ir);
                    } else {
                        ir.setPackage("com.gbwhatsapp");
                        startActivity(ir);
                    }


                } catch (Exception e) {
                    Toast.makeText(getContext(), "Por favor instale WhatsApp Oficial.." + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.texViewLinkFacebook:
                Intent faceboooklink = new Intent(getContext(), EditLinkFacebook.class);
                faceboooklink.putExtra("linkfacebook", linkFacebook.getText().toString());
                startActivity(faceboooklink);
                break;
            case R.id.irFacebookCuenta:
                String facebookId = "fb.//page/<Faceboook Page ID>";
                String urlPage = linkFacebook.getText().toString();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }

                break;
            case R.id.anadirubicacioncasa:
                Intent ubicacioncasa = new Intent(getContext(), AnadirUbicacionCasa.class);
                startActivity(ubicacioncasa);

                break;
           /* case R.id.visitarubicacioncasa:
                if (latitudg.equals("no")) {
                    Toast.makeText(getContext(), "Falta añadir ubicación", Toast.LENGTH_SHORT).show();
                } else {

                    Intent mapsvisitar = new Intent(getContext(), MapsActivityVisitar.class);
                    mapsvisitar.putExtra("latitud", latitudg);
                    mapsvisitar.putExtra("longitud", longitudg);
                    mapsvisitar.putExtra("titulomarcador", nombreGrupo.getText().toString());
                    startActivity(mapsvisitar);
                }

                break;*/
            default:
                break;
        }
    }

    protected void onCreateDialog() {
        DialogFragment newFragment = new SelectecDateFragment();
        newFragment.show(getFragmentManager(), "DatePicker");
    }


    private boolean estaInstaladaAplicacion(String nombrePaquete, Context contexto) {
        PackageManager pm = contexto.getPackageManager();
        try {
            pm.getPackageInfo(nombrePaquete, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void llamar(String numero) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        if (numero.trim().isEmpty()) {
            i.setData(Uri.parse("tel:000000000"));
        } else {
            i.setData(Uri.parse("tel:" + numero));
        }
        startActivity(i);
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
                Toast.makeText(getContext(), "Datos incorrectos de entrada", Toast.LENGTH_SHORT).show();
            } else {
                if (jsonObject.getString("idgrupomusical").equals("error en la consulta")) {
                    Toast.makeText(getContext(), "Fallo al actualizar los datos intenete nuevamente", Toast.LENGTH_SHORT).show();
                } else {
                    /// idGrupoMusical = jsonObject.getString("idgrupomusical");
                    guardarIdGrupoMusical(jsonObject.optString("idgrupomusical"));//guardando en SharePreference
                    nombreGrupo.setText(jsonObject.getString("nombre"));
                    generoMusica.setText(jsonObject.getString("genero"));
                    informacionEdit.setText(jsonObject.getString("informaciondescripcion"));
                    contactosEdit.setText(jsonObject.getString("descripcioncontactos"));
                    direccionEdit.setText(jsonObject.getString("direcciondescripcion"));
                    movil1.setText(jsonObject.getString("numtelefono"));
                    movil2.setText(jsonObject.getString("numtelefonodos"));
                    movilWhatsApp.setText(jsonObject.getString("numwhatsapp"));
                    linkFacebook.setText(jsonObject.getString("linkfacebook"));
                    try {
                        //  Toast.makeText(getContext(), jsonObject.getString("fotoperfil"), Toast.LENGTH_SHORT).show();
                        //if (!jsonObject.getString("fotoperfil").equals("null")) {
                        urlImagen = jsonObject.getString("fotoperfil").toString();
                        urlImagenPortada = jsonObject.getString("fotoportada").toString();

                        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + urlImagen).error(R.drawable.perfilmusic)
                                .placeholder(R.drawable.progress_animation).into(foto_perfil);

                        //   Glide.with(getContext()).load(Constantes.IP_SERVIDOR + "gruposcochalos/" + urlImagen).centerCrop().into(foto_perfil);
                        // Glide.with(getContext()).load(Constantes.IP_SERVIDOR + "gruposcochalos/" + urlImagen).placeholder(R.drawable.ic_camara_de_fotos).centerCrop().into(foto_portada);
                        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + urlImagenPortada).error(R.drawable.perfilmusic)
                                .placeholder(R.drawable.progress_animation).into(foto_portada);
                        //}


                    } catch (Exception e) {

                    }

                    latitudg = jsonObject.getString("latitudg");
                    longitudg = jsonObject.getString("longitudg");
                    ConfigurarMapa(latitudg,longitudg);
                    linkYoutube.setText(jsonObject.getString("linkyoutube"));
                    AnadirWebView(jsonObject.getString("linkyoutube"));//pasando link de youtuve a webview
                       /*
                    if (latitudg.equals("no")) {
                        visitarubicacioncasa.setVisibility(View.INVISIBLE);

                    } else {
                        visitarubicacioncasa.setVisibility(View.VISIBLE);

                    }**/
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ConfigurarMapa(String latitudg, String longitudg) {

        if (latitudg.equals("no")) {
            // Toast.makeText(InformacionGrupoVisitante.this, "El grupo " + nombreGrupo.getText().toString() + " no añadio ubicación", Toast.LENGTH_SHORT).show();
        } else {
            LatLng ubicacionL = new LatLng(Double.parseDouble(latitudg), Double.parseDouble(longitudg));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ubicacionL, 10);
            map.animateCamera(cameraUpdate);
        }
    }

    /*
    *BASE DE DATOS SHAREPREFRENCE PARA GUARDAR EN USUARIO
     */
    String FileNameGrupo = "IdGrupo";

    private void guardarIdGrupoMusical(String idgrupomusical) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("idgrupomusical", idgrupomusical);
        editor.commit();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.webviewyoutubeadmi && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (linkYoutube.getText().toString().equals("https://www.youtube.com/")) {
                Toast.makeText(getContext(), "Primero debes añadir la url de tu video promocional", Toast.LENGTH_SHORT).show();
            } else {
                Uri uris = Uri.parse(linkYoutube.getText().toString());
                Intent it = new Intent(Intent.ACTION_VIEW, uris);
                startActivity(it);
            }
        }
        return true;
    }

    private void AnadirWebView(String linkY) {
        String urlFinal = "";
        if (linkY.equals("https://www.youtube.com/")) {
            ///no hay video promocional
            // Toast.makeText(getContext(), "El grupo no tiene video promocional", Toast.LENGTH_SHORT).show();
        } else {
            String url = linkY.toString();
            int i = url.length() - 1;
            boolean encontrado = false;

            while (encontrado == false) {
                if (url.charAt(i) == '=' || url.charAt(i) == '/') {
                    encontrado = true;
                } else {
                    String aux = urlFinal;
                    urlFinal = url.charAt(i) + aux;
                }
                i--;
            }
        }
        webadmi.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + urlFinal + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

    }
/**
 @Override public void onMapReady(GoogleMap googleMap) {
 mMapPreviaCasa = googleMap;
 if (latitudg.equals("no")) {
 // Toast.makeText(InformacionGrupoVisitante.this, "El grupo " + nombreGrupo.getText().toString() + " no añadio ubicación", Toast.LENGTH_SHORT).show();
 } else {
 LatLng previa = new LatLng(Double.parseDouble(latitudg), Double.parseDouble(longitudg));
 Marker markerPrevia = mMapPreviaCasa.addMarker(new MarkerOptions().position(previa).title(nombreGrupo.getText().toString()));
 markerPrevia.showInfoWindow();
 mUiSettings = googleMap.getUiSettings();
 mUiSettings.setMapToolbarEnabled(false);
 mUiSettings.setZoomControlsEnabled(false);//Habilita icono de zoon
 mUiSettings.setCompassEnabled(false);//brujula
 CameraPosition cameraPosition = new CameraPosition.Builder()
 .target(previa)
 .zoom(15)
 .tilt(90)
 .build();
 mMapPreviaCasa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
 }
 mMapPreviaCasa.setOnMapClickListener(this);
 mMapPreviaCasa.setOnMarkerClickListener(this);
 }

 @Override public void onMapClick(LatLng latLng) {
 if (latitudg.equals("no")) {
 Toast.makeText(getContext(), "Falta añadir ubicación", Toast.LENGTH_SHORT).show();
 } else {

 Intent mapsvisitar = new Intent(getContext(), MapsActivityVisitar.class);
 mapsvisitar.putExtra("latitud", latitudg);
 mapsvisitar.putExtra("longitud", longitudg);
 mapsvisitar.putExtra("titulomarcador", nombreGrupo.getText().toString());
 startActivity(mapsvisitar);
 }
 }

 @Override public boolean onMarkerClick(Marker marker) {
 if (latitudg.equals("no")) {
 Toast.makeText(getContext(), "Falta añadir ubicación", Toast.LENGTH_SHORT).show();
 } else {

 Intent mapsvisitar = new Intent(getContext(), MapsActivityVisitar.class);
 mapsvisitar.putExtra("latitud", latitudg);
 mapsvisitar.putExtra("longitud", longitudg);
 mapsvisitar.putExtra("titulomarcador", nombreGrupo.getText().toString());
 startActivity(mapsvisitar);
 }
 return false;
 }*/


}