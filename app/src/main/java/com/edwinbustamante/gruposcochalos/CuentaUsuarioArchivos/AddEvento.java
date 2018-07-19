package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.Calendar;


public class AddEvento extends AppCompatActivity implements View.OnClickListener {
    private TextView horaInicio, horaFin, fechaInicio, fechaFin, colorC,mensajeAlerta;
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
        mensajeAlerta=findViewById(R.id.mensajealerta);

        colorC.setOnClickListener(this);
        if (getIntent().getExtras().getInt("datos") == 1) {
            h = getIntent().getExtras().getInt("hora");
            m = getIntent().getExtras().getInt("minuto");
            d = getIntent().getExtras().getInt("dia");
            ms = getIntent().getExtras().getInt("mes");
            a = getIntent().getExtras().getInt("anio");
            am_pm = getIntent().getExtras().getInt("AM_PM");
            configurarTexto(h, m, d, ms, a, am_pm);
            if (estaPasadoLaFecha(d,ms,a)){
                mensajeAlerta.setText("La fecha del comienzo del evento está en el pasado");
            }
        }


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
        if (fechaI != 0 && fechaF != 0) {
            Toast.makeText(this, tituloEv + "\n" + colorDelEvento + "\n" + fechaI + "" + mesI + "" + anioI + "\n" + fechaF + "" + mesF + "" + anioF, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Defina fecha de inicio y fin", Toast.LENGTH_SHORT).show();
        }


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

                        // definimos el color
                        colorEvento = Color.parseColor("#" + Integer.toHexString(selectedColor).toUpperCase());

                        // instanciamos el drawable
                        // icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_color_lens);
                        // definimos filtro
                        icon.mutate().setColorFilter(colorEvento, PorterDuff.Mode.SRC_ATOP);
                        iconColor.setImageDrawable(icon);
                        colorC.setTextColor(colorEvento);

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

            if (estaPasadoLaFecha(dayOfMonth,month,year)){
                mensajeAlerta.setText("La fecha del comienzo del evento está en el pasado");
            }
        }


    };

    private boolean estaPasadoLaFecha(int dayOfMonth, int month, int year) {
        boolean res=false;
        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        if (year<y|| month<m||dayOfMonth<d){
            res=true;
        }else{
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
}
