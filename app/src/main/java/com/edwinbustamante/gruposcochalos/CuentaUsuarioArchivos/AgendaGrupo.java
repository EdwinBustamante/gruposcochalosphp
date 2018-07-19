package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgendaGrupo extends AppCompatActivity implements WeekView.EventLongPressListener, WeekView.EventClickListener, WeekView.EmptyViewLongPressListener {
    WeekView mWeekView;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_grupo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAgenda);
        toolbar.setTitle("Agenda");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Return only the events that matches newYear and newMonth.
                List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();

               /*Calendar startTime = Calendar.getInstance();
                //OTRO EVENTO............................
                startTime = Calendar.getInstance();
                startTime.set(Calendar.DAY_OF_MONTH, 15);
                startTime.set(Calendar.HOUR_OF_DAY, 2);
                startTime.set(Calendar.MINUTE, 30);
                startTime.set(Calendar.MONTH, newMonth -1);
                startTime.set(Calendar.YEAR, newYear);
                Calendar endTime = (Calendar) startTime.clone();
                endTime.set(Calendar.HOUR_OF_DAY, 6);
                endTime.set(Calendar.MINUTE, 30);
                endTime.set(Calendar.MONTH, newMonth -1);
                WeekViewEvent event = new WeekViewEvent(2, "ocupado", startTime, endTime);
                event.setName("RESEVADO");
                event.setColor(getResources().getColor(R.color.colorPrimaryDark));
                matchedEvents.add(event);*/


                for (WeekViewEvent event : events) {
                    if (eventMatches(event, newYear, newMonth)) {

                        matchedEvents.add(event);
                    }
                }
                return matchedEvents;
            }
        };
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(mMonthChangeListener);


// The week view has infinite scrolling horizontally. We have to provide the events of a

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(this);
        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);
    }

    /**
     * Checks if an event falls into a specific year and month.
     *
     * @param event The event to check for.
     * @param year  The year.
     * @param month The month.
     * @return True if the event matches the year and month.
     */
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
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
            case R.id.addeventos:
                Intent addEvento = new Intent(AgendaGrupo.this, AddEvento.class);
                addEvento.putExtra("datos", 0);
                startActivity(addEvento);
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onEventLongPress(final WeekViewEvent event, RectF eventRect) {
        final AlertDialog.Builder alertaEliminar = new AlertDialog.Builder(AgendaGrupo.this);
        alertaEliminar.setTitle("Desea Eliminar el evento..?");
        alertaEliminar.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int pos = 0;
                boolean encontrado = false;
                while (pos < events.size() && encontrado == false) {
                    if (event.getId() == events.get(pos).getId()) {
                        events.remove(pos);
                        mWeekView.notifyDatasetChanged();
                        encontrado = true;
                    }
                    pos++;
                }
                dialog.dismiss();
            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

        Intent addEvento = new Intent(AgendaGrupo.this, AddEvento.class);
        int h = time.get(Calendar.HOUR);
        int m = time.get(Calendar.MINUTE);
        int d = time.get(Calendar.DAY_OF_MONTH);
        int ms = time.get(Calendar.MONTH);
        int a = time.get(Calendar.YEAR);
        int am_pm=time.get(Calendar.AM_PM);
        addEvento.putExtra("datos", 1);
        addEvento.putExtra("hora", h);
        addEvento.putExtra("minuto", m);
        addEvento.putExtra("dia", d);
        addEvento.putExtra("mes", ms);
        addEvento.putExtra("anio", a);
        addEvento.putExtra("AM_PM",am_pm);
        startActivity(addEvento);

       /* Calendar startTime = time;
        //OTRO EVENTO............................
        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 15);
        startTime.set(Calendar.HOUR_OF_DAY, 2);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, 8 - 1);
        startTime.set(Calendar.YEAR, 2018);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 6);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, 8 - 1);
        WeekViewEvent event = new WeekViewEvent(2, "ocupado", startTime, endTime);
        event.setName("Estamos en el local villa del carmen los esperamos");
        event.setColor(getResources().getColor(R.color.colorPrimaryDark));
        events.add(event);
        //////////////////////////////////////
        //OTRO EVENTO............................

        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 16);
        startTime.set(Calendar.HOUR_OF_DAY, 5);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, 8 - 1);
        startTime.set(Calendar.YEAR, 2018);

        endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR_OF_DAY, 7);
        endTime.set(Calendar.MONTH, 8 - 1);
        event = new WeekViewEvent(3, "estamos aquisitos", startTime, endTime);
        event.setColor(getResources().getColor(R.color.colorAccent));
        events.add(event);
        mWeekView.notifyDatasetChanged();*/
    }
}
