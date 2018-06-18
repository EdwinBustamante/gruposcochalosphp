package com.edwinbustamante.gruposcochalos;


import android.annotation.SuppressLint;
import android.os.Build;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.edwinbustamante.gruposcochalos.domain.Agenda;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarioUsuario extends AppCompatActivity {

    CalendarView calendarView;
    List<Calendar> selectedDates;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarcalendario);
        toolbar.setTitle("Agenda");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        List<EventDay> events = new ArrayList<>();
        CalendarProperties calendarProperties = new CalendarProperties(this);
        calendarProperties.setCalendarType(2);


        Calendar calendar = Calendar.getInstance();
        calendarView = (CalendarView) findViewById(R.id.calendarViewAdd);
        calendar.set(2018, 6, 5);



        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
        //Deshabilita las fechas
        /*List<Calendar> calendars = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(2018, 5, 7);
        Calendar calendario = Calendar.getInstance();
        calendario.set(2018, 5, 5);
        calendars.add(calendario);
        calendars.add(c);
        calendarView.setDisabledDays(calendars);
        */
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guardar_agenda, menu);
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
            case R.id.guardarAgenda:
                selectedDates = calendarView.getSelectedDates();

                for (Calendar c : selectedDates) {
                    int anio = c.get(Calendar.YEAR);
                    int mes = c.get(Calendar.MONTH) + 1;
                    int fecha = c.get(Calendar.DAY_OF_MONTH);
                    Toast.makeText(this, "" + fecha + "" + mes + "" + anio, Toast.LENGTH_SHORT).show();
                }


                break;
            default:
                break;
        }

        return true;
    }

}
