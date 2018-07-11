package com.edwinbustamante.gruposcochalos.Administrador;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.R;
import com.edwinbustamante.gruposcochalos.domain.Usuario;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EDWIN on 9/7/2018.
 */

public class AdaptadorAdministrador extends RecyclerView.Adapter<AdaptadorAdministrador.ViewHolder> implements Filterable {

    public List<Usuario> usuariosLista;//lista de todos los grupos
    private List<Usuario> mFilteredList;
    private Context context;

    public AdaptadorAdministrador(List<Usuario> usuariosLista, Context context) {
        this.usuariosLista = usuariosLista;
        this.mFilteredList = usuariosLista;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // aqui es donde se esta haciendo llamado a  la tarjeta que vamos a  cargar
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_administrador, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Usuario usuario = mFilteredList.get(position);
        holder.nombreGrupoA.setText(usuario.getNombreGrupoA());
        if (usuario.getEstado().equals("1")) {
            holder.textEstado.setText("Habilitado");
        } else {
            holder.textEstado.setText("Deshabilitado");
        }
        holder.cambiarEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alerdialogCambiar = new AlertDialog.Builder(context);
                alerdialogCambiar.setTitle("Cambiar estado de: " + "\n" + usuario.getNombreGrupoA().toString());
                alerdialogCambiar.setPositiveButton("HABILITAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cambiarEstado(usuario.getId(), "1", position, usuario.getNombreGrupoA());
                    }
                }).setNegativeButton("DESHABILITAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cambiarEstado(usuario.getId(), "0", position, usuario.getNombreGrupoA());
                    }
                });
                alerdialogCambiar.create().show();
            }
        });

    }

    public void cambiarEstado(final String idusuario, final String estado, final int posicion, final String nombre) {

        String url = Constantes.IP_SERVIDOR + "/gruposcochalos/cambiarestadousuario.php?idusuario=" + idusuario + "&estado=" + estado;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("administracion");//datos esta en el php
                JSONObject jsonObject = null;
                try {
                    jsonObject = jsonArray.getJSONObject(0);
                    if (jsonObject.optString("mensaje").equals("Estado cambiado exitosamente")) {
                        Toast.makeText(context, jsonObject.optString("mensaje"), Toast.LENGTH_SHORT).show();
                        if (estado.equals("1")) {
                            //TODO: handle success
                            Usuario usuario = new Usuario();
                            usuario.setId(idusuario);
                            usuario.setEstado(estado);
                            usuario.setNombreGrupoA(nombre);
                            mFilteredList.remove(posicion);
                            notifyItemRemoved(posicion);
                            mFilteredList.add(posicion, usuario);
                            notifyItemInserted(posicion);
                        } else {
                            Usuario usuario = new Usuario();
                            usuario.setId(idusuario);
                            usuario.setEstado(estado);
                            usuario.setNombreGrupoA(nombre);
                            mFilteredList.remove(posicion);
                            notifyItemRemoved(posicion);
                            mFilteredList.add(posicion, usuario);
                            notifyItemInserted(posicion);
                        }

                    } else {
                        Toast.makeText(context, jsonObject.optString("mensaje"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //TODO: handle failure
                Toast.makeText(context, "error en el servidor intente mas tarde", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(context).add(jsonRequest);

    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = usuariosLista;
                } else {

                    ArrayList<Usuario> filteredList = new ArrayList<>();

                    for (Usuario usuario : usuariosLista) {

                        if (usuario.getNombreGrupoA().toLowerCase().contains(charString)) {

                            filteredList.add(usuario);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Usuario>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreGrupoA, textEstado;
        private Button cambiarEstado;

        public ViewHolder(View itemView) {
            super(itemView);
            nombreGrupoA = itemView.findViewById(R.id.textNombreGrupoA);
            textEstado = itemView.findViewById(R.id.textEstado);
            cambiarEstado = itemView.findViewById(R.id.cambiarEstado);

        }
    }

}
