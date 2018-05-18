package com.edwinbustamante.gruposcochalos.CuentaUsuarioArchivos;

import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.domain.Publicacion;
import com.edwinbustamante.gruposcochalos.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by EDWIN on 8/5/2018.
 */

public class MyAdapterPublicar extends RecyclerView.Adapter<MyAdapterPublicar.ViewHolder> {


    public List<Publicacion> publicacionLista;//lista de todos los grupos

    Display display ;

    public MyAdapterPublicar(List<Publicacion> publicacionLista,Display display) {
        this.publicacionLista = publicacionLista;//Aqui paso la lista que stoy recibiendo
        this.display=display;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Publicacion publicacion=publicacionLista.get(position);
        //modificaciones del cntenido de cada item...despues de recibir del ViewHolder
       // holder.fotoPerfilPublicacion.setImageResource(publicacionLista.get(position).getFotoPerfil());
       // holder.nombreGrupoPublicacion.setText(publicacionLista.get(position).getNombreGrupo());
       // holder.fechaPublicacion.setText(publicacionLista.get(position).getFechaPublicacion());

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
       holder.fotoPublicacion.setMaxHeight(height);
       holder.fotoPublicacion.setMaxWidth(width);
        holder.descripcionPublicacion.setText(publicacion.getDescripcion());
        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + publicacion.getFoto()).into(holder.fotoPublicacion);
    }

    @Override
    public int getItemCount() {

        return publicacionLista.size();
    }

    /**
     * LA INER CLASE ESTA ABAJO QUE ENCARGA DE DIBUJAR
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView fotoPerfilPublicacion, fotoPublicacion;
        private TextView nombreGrupoPublicacion, fechaPublicacion, descripcionPublicacion;


        public ViewHolder(View itemView) {
            super(itemView);
            fotoPerfilPublicacion = (ImageView) itemView.findViewById(R.id.fotoPerfilPublicacion);
            nombreGrupoPublicacion = (TextView) itemView.findViewById(R.id.textNombreGrupoPublicacion);
            fechaPublicacion = (TextView) itemView.findViewById(R.id.fechaPublicacion);
            descripcionPublicacion = (TextView) itemView.findViewById(R.id.textViewDescripcionPublicacion);
            fotoPublicacion = (ImageView) itemView.findViewById(R.id.imageViewPublicacion);




           itemView.setOnClickListener(this);// esto es a todo

        }


        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "se presiono", Toast.LENGTH_SHORT).show();
        }
    }
}
