package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.domain.EventoEdwin;
import com.edwinbustamante.gruposcochalos.domain.ResultadoEvento;
import com.edwinbustamante.gruposcochalos.service.EventoAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AgendaGrupo extends AppCompatActivity implements WeekView.EventLongPressListener, WeekView.EventClickListener, WeekView.EmptyViewLongPressListener {
    WeekView mWeekView;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

    String rol;
    String idGrupoMusical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_grupo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAgenda);
        toolbar.setTitle("Agenda");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rol = getIntent().getExtras().getString("rol");
        idGrupoMusical = getIntent().getExtras().getString("idgrupomusical");

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
        // mWeekView.getLastVisibleDay();
        mWeekView.getFirstVisibleDay();


// The week view has infinite scrolling horizontally. We have to provide the events of a
        if (rol.equals("usuario")) {
            // Set long press listener for events.
            mWeekView.setEventLongPressListener(this);
            // Set an action when any event is clicked.
            mWeekView.setOnEventClickListener(this);
            // Set long press listener for empty view
            mWeekView.setEmptyViewLongPressListener(this);
        }


//configurando la formato de la fecha y anio
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" d/M/y", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (true)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        events.clear();
        mWeekView.notifyDatasetChanged();
        obtenerEventos();

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
        if (rol.equals("usuario")) {
            getMenuInflater().inflate(R.menu.guardar_agenda, menu);

        }
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
            case R.id.diaunover:
                mWeekView.setNumberOfVisibleDays(1);
                break;
            case R.id.diadosver:
                mWeekView.setNumberOfVisibleDays(2);
                break;
            case R.id.diatresver:
                mWeekView.setNumberOfVisibleDays(3);
                break;
            case R.id.diacuatrover:
                mWeekView.setNumberOfVisibleDays(4);
                break;
            case R.id.diacincover:
                mWeekView.setNumberOfVisibleDays(5);
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
                eliminarEvento(event.getId());
                dialog.dismiss();
            }
        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public void eliminarEvento(final long idevento) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Eliminando..");
        progressDialog.setMessage("Eliminando el evento espere un momento por favor...");
        progressDialog.show();
        String urlUpload = Constantes.IP_SERVIDOR + "/gruposcochalos/eliminarevento.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                int pos = 0;
                boolean encontrado = false;
                while (pos < events.size() && encontrado == false) {
                    if (idevento == events.get(pos).getId()) {
                        events.remove(pos);
                        mWeekView.notifyDatasetChanged();
                        encontrado = true;
                    }
                    pos++;
                }
                progressDialog.dismiss();
                Toast.makeText(AgendaGrupo.this, response, Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AgendaGrupo.this, "error" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idevento", "" + idevento);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AgendaGrupo.this);
        requestQueue.add(stringRequest);

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
        int am_pm = time.get(Calendar.AM_PM);
        addEvento.putExtra("datos", 1);
        addEvento.putExtra("hora", h);
        addEvento.putExtra("minuto", m);
        addEvento.putExtra("dia", d);
        addEvento.putExtra("mes", ms);
        addEvento.putExtra("anio", a);
        addEvento.putExtra("AM_PM", am_pm);
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

    private void obtenerEventos() {


        Call<ResultadoEvento> resultadoAgendaCall = EventoAPI.getEventoService().getAgenda(idGrupoMusical);//lacemos la llamada al archivo php
        resultadoAgendaCall.enqueue(new Callback<ResultadoEvento>() {
            @Override
            public void onResponse(Call<ResultadoEvento> call, retrofit2.Response<ResultadoEvento> response) {
                ResultadoEvento body = response.body();
                List<EventoEdwin> results = body.getListaEventos();
                for (EventoEdwin eventoe : results) {

                    Calendar startTime = Calendar.getInstance();
                    //OTRO EVENTO............................
                    startTime = Calendar.getInstance();
                    startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(eventoe.getFechai()));
                    startTime.set(Calendar.MONTH, Integer.parseInt(eventoe.getMesi()));
                    startTime.set(Calendar.YEAR, Integer.parseInt(eventoe.getAnioi()));
                    startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(eventoe.getHorai()));
                    startTime.set(Calendar.MINUTE, Integer.parseInt(eventoe.getMinutoi()));

                    Calendar endTime = (Calendar) startTime.clone();
                    endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(eventoe.getFechaf()));
                    endTime.set(Calendar.MONTH, Integer.parseInt(eventoe.getMesf()));
                    endTime.set(Calendar.YEAR, Integer.parseInt(eventoe.getAniof()));
                    endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(eventoe.getHoraf()));
                    endTime.set(Calendar.MINUTE, Integer.parseInt(eventoe.getMinutof()));
                    WeekViewEvent event = new WeekViewEvent(Integer.parseInt(eventoe.getIdevento()), eventoe.getTitulo(), startTime, endTime);
                    // event.setName("Estamos en el local villa del carmen los esperamos");
                    int selectedColor = Integer.parseInt(eventoe.getColor());
                    int col = Color.parseColor("#" + Integer.toHexString(selectedColor).toUpperCase());
                    event.setColor(col);
                    events.add(event);
                    mWeekView.notifyDatasetChanged();
                    // publicacionlista.clear();
                    // publicacionlista.addAll(results);
                    // mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResultadoEvento> call, Throwable t) {
                Toast.makeText(AgendaGrupo.this, "Ha ocurrido un error" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error", t.getMessage());
                t.printStackTrace();
            }
        });

    }
}
