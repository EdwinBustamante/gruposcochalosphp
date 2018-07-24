package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edwinbustamante.gruposcochalos.ImagenFull.FulImagenVisitante;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.VisitarMapa.MapsActivityVisitar;
import com.edwinbustamante.gruposcochalos.domain.Publicacion;
import com.edwinbustamante.gruposcochalos.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EDWIN on 8/5/2018.
 */

public class MyAdapterPublicar extends RecyclerView.Adapter<MyAdapterPublicar.ViewHolder> {


    public List<Publicacion> publicacionLista;//lista de todos los grupos

    private Context context;
    private String rol;

    public MyAdapterPublicar(List<Publicacion> publicacionLista, Context context, String rol) {
        this.publicacionLista = publicacionLista;//Aqui paso la lista que stoy recibiendo
        this.context = context;
        this.rol = rol;
    }

    //encargado de inflar un nuevo item para la lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // aqui es donde se esta haciendo llamado a  la tarjeta que vamos a  cargar
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_publicacion, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Publicacion publicacion = publicacionLista.get(position);
        //modificaciones del cntenido de cada item...despues de recibir del ViewHolder
        // holder.fotoPerfilPublicacion.setImageResource(publicacionLista.get(position).getFotoPerfil());
        // holder.nombreGrupoPublicacion.setText(publicacionLista.get(position).getNombreGrupo());
        // holder.fechaPublicacion.setText(publicacionLista.get(position).getFechaPublicacion());
        //condicional que detecta si es visitante o administrador
        if (rol.equals("visitante")) {

            holder.eliminarPublicacion.setVisibility(View.INVISIBLE);
        }
        holder.descripcionPublicacion.setText(publicacion.getDescripcion());
        holder.fechaPublicacion.setText(publicacion.getHora());
        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + publicacion.getFoto()).error(R.drawable.perfilmusic)
                .placeholder(R.drawable.progress_animation).into(holder.fotoPublicacion);
        holder.fotoPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verfoto = new Intent(context, FulImagenVisitante.class);
                verfoto.putExtra("fotoperfil", publicacion.getFoto());
                verfoto.putExtra("nombretoolbar", "Publicación");
                context.startActivity(verfoto);
            }
        });
        holder.nombreGrupoPublicacion.setText(publicacion.getNombre());
        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + publicacion.getFotoperfil()).error(R.drawable.perfilmusic)
                .placeholder(R.drawable.progress_animation).into(holder.fotoPerfilPublicacion);

        if (publicacion.getLatitud().equals("no")) {
            holder.mapsUbicacion.setVisibility(View.INVISIBLE);
        } else {
            holder.mapsUbicacion.setImageResource(R.drawable.ic_mapa_normal);
            holder.mapsUbicacion.setVisibility(View.VISIBLE);
            holder.mapsUbicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent visitarMapa = new Intent(context, MapsActivityVisitar.class);
                    visitarMapa.putExtra("latitud", publicacion.getLatitud());
                    visitarMapa.putExtra("longitud", publicacion.getLongitud());
                    visitarMapa.putExtra("titulomarcador", "Ubicación de la publicación");
                    context.startActivity(visitarMapa);
                }
            });
        }

        holder.eliminarPublicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alerdialogEliminar = new AlertDialog.Builder(context);
                alerdialogEliminar.setTitle("Desea eliminar publicación..?");
                alerdialogEliminar.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarPublicacion(publicacion.getIdpublicacion(), position);

                        // Toast.makeText(context, publicacion.getIdpublicacion() + " en la posicion" + position, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alerdialogEliminar.create().show();
            }
        });

    }

    public void eliminarPublicacion(final String idpublicacion, final int position) {

        final ProgressDialog progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Eliminando publicación");
        progressDialog.setMessage("Eliminando publicación espere un momento por favor...");
        progressDialog.show();
        String urlUpload = Constantes.IP_SERVIDOR + "/gruposcochalos/eliminarpublicacion.php?";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String respuesta = response.toString();
                publicacionLista.remove(position);
                notifyItemRemoved(position);
                progressDialog.dismiss();
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "error al publicar intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idpublicacion", idpublicacion);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    @Override
    public int getItemCount() {

        return publicacionLista.size();
    }

    /**
     * LA INER CLASE ESTA ABAJO QUE ENCARGA DE DIBUJAR
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView fotoPerfilPublicacion, fotoPublicacion, mapsUbicacion;
        private TextView nombreGrupoPublicacion, fechaPublicacion, descripcionPublicacion, eliminarPublicacion;


        public ViewHolder(View itemView) {
            super(itemView);
            fotoPerfilPublicacion = (ImageView) itemView.findViewById(R.id.fotoPerfilPublicacion);
            nombreGrupoPublicacion = (TextView) itemView.findViewById(R.id.textNombreGrupoPublicacion);
            fechaPublicacion = (TextView) itemView.findViewById(R.id.fechaPublicacion);
            descripcionPublicacion = (TextView) itemView.findViewById(R.id.textViewDescripcionPublicacion);
            fotoPublicacion = (ImageView) itemView.findViewById(R.id.imageViewPublicacion);
            eliminarPublicacion = (TextView) itemView.findViewById(R.id.eliminarpublicacion);
            mapsUbicacion = (ImageView) itemView.findViewById(R.id.imageViewmaps);


            //itemView.setOnClickListener(this);// esto es a todo

        }


        @Override
        public void onClick(View view) {

        }
    }
}
