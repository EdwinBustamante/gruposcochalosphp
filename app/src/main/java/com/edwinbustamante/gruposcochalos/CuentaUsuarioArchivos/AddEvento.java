package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddEvento extends AppCompatActivity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
    private TextView horaInicio, horaFin, fechaInicio, fechaFin, colorC, mensajeAlerta;
    private EditText tituloEvento;
    static final int TIME_DIALOG_ID_INI = 999;
    static final int TIME_DIALOG_ID_FIN = 998;
    static final int DATE_DIALOG_ID_INI = 997;
    static final int DATE_DIALOG_ID_FIN = 996;
    private int horaI, minutoI, horaF, minutoF, fechaI, fechaF, mesF, mesI, anioI, anioF;
    private String titulo, color;
    int h;
    int m;
    int d;
    int ms;
    int a;
    int am_pm;
    ImageView iconColor;
    private int colorEvento;
    Drawable icon;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectReques;
    String FileNameGrupo = "IdGrupo";
    private Switch todoElDia;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_evento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEvento);
        toolbar.setTitle("Configurar Evento");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        horaI = -1;
        horaF = -1;
        horaInicio = findViewById(R.id.horaInicio);
        horaFin = findViewById(R.id.horaFin);
        horaInicio.setOnClickListener(this);
        horaFin.setOnClickListener(this);
        fechaInicio = findViewById(R.id.fechaInicio);
        fechaInicio.setOnClickListener(this);
        fechaFin = findViewById(R.id.fechaFin);
        fechaFin.setOnClickListener(this);
        colorC = findViewById(R.id.colorEvento);
        iconColor = findViewById(R.id.iconcolor);
        tituloEvento = findViewById(R.id.tituloevento);
        //#########dando color al icono######
        // definimos el color
        colorEvento = Color.parseColor("#007887");
        // instanciamos el drawable
        icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_color_lens);
        // definimos filtro
        icon.mutate().setColorFilter(colorEvento, PorterDuff.Mode.SRC_ATOP);
        iconColor.setImageDrawable(icon);
        colorC.setTextColor(colorEvento);
        mensajeAlerta = findViewById(R.id.mensajealerta);
        todoElDia = findViewById(R.id.todoeldia);
        colorC.setOnClickListener(this);
        if (getIntent().getExtras().getInt("datos") == 1) {
            h = getIntent().getExtras().getInt("hora");
            m = getIntent().getExtras().getInt("minuto");
            d = getIntent().getExtras().getInt("dia");
            ms = getIntent().getExtras().getInt("mes");
            a = getIntent().getExtras().getInt("anio");
            am_pm = getIntent().getExtras().getInt("AM_PM");
            configurarTexto(h, m, d, ms, a, am_pm);


            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            String fecha1 = d + "/" + m + "/" + y;
            String fecha2 = d + "/" + m + "/" + a;
            String s = compararFechasConDate(fecha1, fecha2);
            if (s.equals("incorrecto")) {
                mensajeAlerta.setText("La fecha del comienzo del evento está en el pasado");
            } else {
                mensajeAlerta.setText("");
            }

        }
        requestQueue = Volley.newRequestQueue(this);
        todoElDia.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Agregando evento");
        progressDialog.setMessage("Agregando evento a la agenda espere un momento por favor...");

    }

    private void configurarTexto(int h, int m, int d, int ms, int a, int am_pm) {

        String AM_PM = "";
        int hora = 00;
        if (am_pm == 0) {
            AM_PM = "AM";
            hora = h;
        } else {
            if (h == 0) {
                hora = 12;
                AM_PM = "PM";
            } else {
                AM_PM = "PM";
                hora = h;
            }
        }
        horaInicio.setText("" + hora + " : " + m + " " + AM_PM);
        horaFin.setText("" + hora + " : " + m + " " + AM_PM);

        String mes = reformatearMes(ms + 1);

        fechaFin.setText("" + d + " " + mes + " " + a);
        fechaInicio.setText("" + d + " " + mes + " " + a);
        fechaI = d;
        mesI = ms;
        anioI = a;
        fechaF = d;
        mesF = ms;
        anioF = a;
        horaI = h;
        minutoI = m;
        horaF = h;
        minutoF = m;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guardar_evento, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.guardarevento:
                alistarDatos();
                // Toast.makeText(this, "evento guardar", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    private void alistarDatos() {

        String tituloEv = tituloEvento.getText().toString();
        if (tituloEvento.getText().toString().isEmpty()) {
            tituloEv = "OCUPADO";
        }
        String colorDelEvento = "" + colorEvento;

        String fecha1 = fechaI + "/" + mesI + "/" + anioI;
        String fecha2 = fechaF + "/" + mesF + "/" + anioF;
        if (fechaI != 0 && fechaF != 0 && horaF != -1 && horaI != -1) {
            if (validoFechaHoy(fecha1)) {
                switch (compararFechasConDate(fecha1, fecha2)) {
                    case "igual":
                        if (horaValida(horaI, minutoI, horaF, minutoF)) {
                            String defaultValue = "DefaultName";
                            SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                            String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);

                            progressDialog.show();

                            String url = Constantes.IP_SERVIDOR + "gruposcochalos/addevento.php?idgrupomusical=" + idGrupoMusical + "&tituloevento=" + tituloEv + "&colorevento=" + colorDelEvento + "&fechai=" + fechaI + "&mesi=" + mesI + "&anioi=" + anioI + "&horai=" + horaI + "&minutoi=" + minutoI + "&fechaf=" + fechaF + "&mesf=" + mesF + "&aniof=" + anioF + "&horaf=" + horaF + "&minutof=" + minutoF;
                            url = url.replace(" ", "%20");
                            jsonObjectReques = new JsonObjectRequest(Request.Method.GET, url, null, this, this);//realiza el llamado ala url
                            requestQueue.add(jsonObjectReques);
                            ///RESPUESTA
                            // Toast.makeText(this, tituloEv + "\n" + colorDelEvento + "\n" + fechaI + "" + mesI + "" + anioI + "\n" + fechaF + "" + mesF + "" + anioF, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Rango de horas del evento incorrecta", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "correcto":
                        String defaultValue = "DefaultName";
                        SharedPreferences sharedPreferences = getSharedPreferences(FileNameGrupo, Context.MODE_PRIVATE);
                        String idGrupoMusical = sharedPreferences.getString("idgrupomusical", defaultValue);


                        progressDialog.show();
                        String url = Constantes.IP_SERVIDOR + "gruposcochalos/addevento.php?idgrupomusical=" + idGrupoMusical + "&tituloevento=" + tituloEv + "&colorevento=" + colorDelEvento + "&fechai=" + fechaI + "&mesi=" + mesI + "&anioi=" + anioI + "&horai=" + horaI + "&minutoi=" + minutoI + "&fechaf=" + fechaF + "&mesf=" + mesF + "&aniof=" + anioF + "&horaf=" + horaF + "&minutof=" + minutoF;
                        url = url.replace(" ", "%20");
                        jsonObjectReques = new JsonObjectRequest(Request.Method.GET, url, null, this, this);//realiza el llamado ala url
                        requestQueue.add(jsonObjectReques);
                        ///RESPUESTA
                        // Toast.makeText(this, tituloEv + "\n" + colorDelEvento + "\n" + fechaI + "" + mesI + "" + anioI + "\n" + fechaF + "" + mesF + "" + anioF, Toast.LENGTH_SHORT).show();

                        break;
                    case "incorrecto":
                        Toast.makeText(this, "La fecha fin del evento, está antes del inicio del evento", Toast.LENGTH_SHORT).show();

                        break;
                }
            } else {
                Toast.makeText(this, "No puede agregar un evento en el pasado", Toast.LENGTH_SHORT).show();
            }


        } else {
            Toast.makeText(this, "Defina fecha y hora de inicio y fin", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean validoFechaHoy(String fecha1) {
        boolean res = false;
        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(fecha1);

            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            String fechahoy = d + "/" + m + "/" + y;
            Date fechaDateHoy = formateador.parse(fechahoy);

            if (fechaDate1.before(fechaDateHoy)) {
                //  resultado = "La Fecha 1 es menor ";
                res = false;
            } else {
                if (fechaDateHoy.before(fechaDate1)) {
                    res = true;
                } else {
                    // resultado = "Las Fechas Son iguales ";
                    res = true;
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  " + e.getMessage());
        }

        return res;
    }


    private boolean horaValida(int hI, int mI, int hF, int mF) {
        boolean res = false;
        if (hI < hF) {
            res = true;
        } else {
            if (hI == hF && mI < mF) {
                res = true;
            }
        }
        return res;
    }


    /**
     * Comparamos las Fechas
     *
     * @param fecha1
     * @param fecha2
     * @return
     * @author CHENAO
     */
    private String compararFechasConDate(String fecha1, String fecha2) {

        String resultado = "";
        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fecha2);

            if (fechaDate1.before(fechaDate2)) {
                //  resultado = "La Fecha 1 es menor ";
                resultado = "correcto";
            } else {
                if (fechaDate2.before(fechaDate1)) {
                    //  resultado = "La Fecha 1 es Mayor ";
                    resultado = "incorrecto";
                } else {
                    // resultado = "Las Fechas Son iguales ";
                    resultado = "igual";
                }
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  " + e.getMessage());
        }

        return resultado;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.horaInicio:
                showDialog(TIME_DIALOG_ID_INI);
                break;
            case R.id.horaFin:
                showDialog(TIME_DIALOG_ID_FIN);
                break;
            case R.id.fechaInicio:
                showDialog(DATE_DIALOG_ID_INI);
                break;
            case R.id.fechaFin:
                showDialog(DATE_DIALOG_ID_FIN);
                break;
            case R.id.colorEvento:
                colorDialogo();
                break;
            case R.id.todoeldia:
                if (todoElDia.isChecked()) {
                    horaI = 0;
                    minutoI = 0;
                    horaInicio.setText(horaI + " : " + minutoI + " " + "AM");
                    horaF = 23;
                    minutoF = 59;
                    horaFin.setText(horaF + " : " + minutoF + " " + "PM");
                }
                break;
            default:
                break;
        }
    }

    private void colorDialogo() {
        int currentBackgroundColor = 0xffffffff;
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Elige un color")
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {


                    }
                })
                .setPositiveButton("aceptar", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                        colorEvento = selectedColor;

                        // definimos el color
                        int col = Color.parseColor("#" + Integer.toHexString(selectedColor).toUpperCase());

                        // instanciamos el drawable
                        // icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_color_lens);
                        // definimos filtro
                        icon.mutate().setColorFilter(col, PorterDuff.Mode.SRC_ATOP);
                        iconColor.setImageDrawable(icon);
                        colorC.setTextColor(col);

                        //Toast.makeText(AddEvento.this, "onColorSelected: " + Integer.toHexString(selectedColor).toUpperCase(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case TIME_DIALOG_ID_INI:
                if (getIntent().getExtras().getInt("datos") == 1) {
                    if (am_pm == 1) {
                        return new TimePickerDialog(this, timePickerListenerInicio, h + 12, m, false);
                    } else {
                        return new TimePickerDialog(this, timePickerListenerInicio, h, m, false);
                    }

                } else {
                    return new TimePickerDialog(this, timePickerListenerInicio, 0, 0, false);
                }

            case TIME_DIALOG_ID_FIN:
                if (getIntent().getExtras().getInt("datos") == 1) {
                    if (am_pm == 1) {
                        return new TimePickerDialog(this, timePickerListenerfin, h + 12, m, false);
                    } else {
                        return new TimePickerDialog(this, timePickerListenerfin, h, m, false);
                    }

                } else {
                    return new TimePickerDialog(this, timePickerListenerfin, 0, 0, false);
                }

            case DATE_DIALOG_ID_INI:
                if (getIntent().getExtras().getInt("datos") == 1) {
                    return new DatePickerDialog(this, datePickerListenerIni, a, ms, d);
                } else {
                    return new DatePickerDialog(this, datePickerListenerIni, year, month, day);
                }

            case DATE_DIALOG_ID_FIN:
                if (getIntent().getExtras().getInt("datos") == 1) {
                    return new DatePickerDialog(this, datePickerListenerFin, a, ms, d);
                } else {
                    return new DatePickerDialog(this, datePickerListenerFin, year, month, day);
                }
        }
        return null;
    }

    /*
    *
    *las variables de time pickcher
    */
    private TimePickerDialog.OnTimeSetListener timePickerListenerInicio = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            horaI = hourOfDay;
            minutoI = minute;
            String AM_PM;
            int hora = 00;
            if (hourOfDay < 12) {
                AM_PM = "AM";
                hora = hourOfDay;
            } else {
                AM_PM = "PM";
                hora = preguntarHora(hourOfDay);

            }
            horaInicio.setText(hora + " : " + minute + " " + AM_PM);

        }
    };


    private int preguntarHora(int hourOfDay) {
        int hora = 00;
        switch (hourOfDay) {
            case 12:
                hora = 12;
                break;
            case 13:
                hora = 1;
                break;
            case 14:
                hora = 2;
                break;
            case 15:
                hora = 3;
                break;
            case 16:
                hora = 4;
                break;
            case 17:
                hora = 5;
                break;
            case 18:
                hora = 6;
                break;
            case 19:
                hora = 7;
                break;
            case 20:
                hora = 8;
                break;
            case 21:
                hora = 9;
                break;
            case 22:
                hora = 10;
                break;
            case 23:
                hora = 11;
                break;

        }
        return hora;
    }


    private TimePickerDialog.OnTimeSetListener timePickerListenerfin = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            horaF = hourOfDay;
            minutoF = minute;
            String AM_PM;
            int hora = 00;
            if (hourOfDay < 12) {
                AM_PM = "AM";
                hora = hourOfDay;
            } else {
                AM_PM = "PM";
                hora = preguntarHora(hourOfDay);

            }
            horaFin.setText(hora + " : " + minute + " " + AM_PM);

        }
    };
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //############################################################################################
    DatePickerDialog.OnDateSetListener datePickerListenerIni = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            fechaI = dayOfMonth;
            mesI = month;
            anioI = year;
            String mes = reformatearMes(month + 1);
            fechaInicio.setText("" + dayOfMonth + " " + mes + " " + year);
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            String fecha1 = d + "/" + m + "/" + y;
            String fecha2 = dayOfMonth + "/" + month + "/" + year;
            String s = compararFechasConDate(fecha1, fecha2);
            if (s.equals("incorrecto")) {
                mensajeAlerta.setText("La fecha del comienzo del evento está en el pasado");
            } else {
                mensajeAlerta.setText("");
            }
        }


    };

    private boolean estaPasadoLaFecha(int dayOfMonth, int month, int year) {
        boolean res = false;
        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        if (year < y || month < m || dayOfMonth < d) {
            res = true;
        } else {
            mensajeAlerta.setText("");
        }
        return res;
    }


    DatePickerDialog.OnDateSetListener datePickerListenerFin = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            fechaF = dayOfMonth;
            mesF = month;
            anioF = year;

            String mes = reformatearMes(month + 1);
            fechaFin.setText("" + dayOfMonth + " " + mes + " " + year);
        }
    };

    private String reformatearMes(int month) {
        String res = "";
        switch (month) {
            case 1:
                return "ene.";
            case 2:
                return "feb.";
            case 3:
                return "mar.";
            case 4:
                return "abr.";
            case 5:
                return "may.";
            case 6:
                return "jun.";
            case 7:
                return "jul.";
            case 8:
                return "ago.";
            case 9:
                return "sep.";
            case 10:
                return "oct.";
            case 11:
                return "nov";
            case 12:
                return "dic.";
        }
        return res;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.dismiss();
        Toast.makeText(this, "Error en el servidor intente nuevamente o mas tarde", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("mensaje");
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);
            if (jsonObject.optString("agregado").equals("exito")) {
                Toast.makeText(this, "Se agrego correctamente el evento...", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Error al agregar el evento " + "\n" + "configure nuevamente el evento...", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
