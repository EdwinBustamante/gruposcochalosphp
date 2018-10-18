package com.edwinbustamante.gruposcochalos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edwinbustamante.gruposcochalos.Objetos.Constantes;
import com.edwinbustamante.gruposcochalos.VisitanteArchivos.InformacionGrupoVisitante;
import com.edwinbustamante.gruposcochalos.domain.GrupoMusical;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22/2/2018.
 */

public class RecyclerViewAdaptadorPrincipal extends RecyclerView.Adapter<RecyclerViewAdaptadorPrincipal.ViewHolder> implements Filterable {


    public List<GrupoMusical> gruposMusicalesLista;//lista de todos los grupos
    private List<GrupoMusical> mFilteredList;
    private Context context;

    public RecyclerViewAdaptadorPrincipal(List<GrupoMusical> gruposMusicalLista, Context context) {
        this.gruposMusicalesLista = gruposMusicalLista;
        this.mFilteredList = gruposMusicalLista;
        this.context = context;
    }

    //encargado de inflar un nuevo item para la lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // aqui es donde se esta haciendo llamado a  la tarjeta que vamos a  cargar
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_grupo, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GrupoMusical grupoMusical = mFilteredList.get(position);
        String urlImagenPerfil = grupoMusical.getFotoperfil();

        Picasso.get().load(Constantes.IP_SERVIDOR + "gruposcochalos/" + urlImagenPerfil)
                .error(R.drawable.perfilmusic)
                .placeholder(R.drawable.progress_animation).into(holder.fotoPortada);

        holder.nombreGrupo.setText(grupoMusical.getNombre());
        holder.tipoMusica.setText(grupoMusical.getGenero());
        holder.cardgrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InformacionGrupoVisitante.class);
                intent.putExtra("grupomusical", grupoMusical);
                context.startActivity(intent);
            }
        });
       /* holder.fotoPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InformacionGrupoVisitante.class);
                intent.putExtra("grupomusical", grupoMusical);
                context.startActivity(intent);
            }
        });*/
        // /holder.nombreGrupo.setOnClickListener(this); el onclik

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

                    mFilteredList = gruposMusicalesLista;
                } else {

                    ArrayList<GrupoMusical> filteredList = new ArrayList<>();

                    for (GrupoMusical grupomusical : gruposMusicalesLista) {

                        if (grupomusical.getNombre().toLowerCase().contains(charString)) {

                            filteredList.add(grupomusical);
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
                mFilteredList = (ArrayList<GrupoMusical>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView fotoPortada;
        private TextView nombreGrupo, tipoMusica;
        private LinearLayout cardgrupo;

        public ViewHolder(View itemView) {
            super(itemView);
            fotoPortada = (ImageView) itemView.findViewById(R.id.fotoPortada);
            nombreGrupo = (TextView) itemView.findViewById(R.id.textNombreGrupo);
            tipoMusica = (TextView) itemView.findViewById(R.id.textTipoDeMusica);
            cardgrupo = (LinearLayout) itemView.findViewById(R.id.cardgrupo);
        }
    }
}
