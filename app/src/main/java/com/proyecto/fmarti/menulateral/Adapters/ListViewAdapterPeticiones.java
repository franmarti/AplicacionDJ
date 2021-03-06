package com.proyecto.fmarti.menulateral.Adapters;

/**
 * Created by fmarti on 20/04/2016.
 */


import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
        import android.widget.TextView;

import com.proyecto.fmarti.menulateral.Logica.Cancion;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;
import com.proyecto.fmarti.menulateral.R;

import java.util.ArrayList;

public class ListViewAdapterPeticiones extends BaseAdapter{
    // Declare Variables
    Context context;
    String[] titulos, autores;

    public ArrayList<Cancion> cancionArrayList;
    public ArrayList<Cancion> orig;

    public ListViewAdapterPeticiones(Context context, ArrayList<Cancion> canciones) {
        super();
        this.context = context;
        this.cancionArrayList = canciones;
    }

    public class EstHolder {
        TextView autor;
        TextView titulo;
        TextView votos;
    }

    /*public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Cancion> results = new ArrayList<Cancion>();
                if (orig == null)
                    orig = cancionArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Cancion cancion : orig) {
                            if (cancion.getAutor().toLowerCase().contains(constraint.toString()))
                                results.add(cancion);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                cancionArrayList = (ArrayList<Cancion>) results.values;
                notifyDataSetChanged();
            }


        };
    }*/

    @Override
    public int getCount() {
        return cancionArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return cancionArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        EstHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_peticiones, parent, false);
            holder = new EstHolder();
            holder.autor = (TextView) convertView.findViewById(R.id.tvAutor);
            holder.titulo = (TextView) convertView.findViewById(R.id.tvTitulo);
            holder.votos = (TextView) convertView.findViewById(R.id.tvVotos);
            convertView.setTag(holder);
        } else {
            holder = (EstHolder) convertView.getTag();
        }

        holder.autor.setText(cancionArrayList.get(position).getAutor());
        holder.titulo.setText(cancionArrayList.get(position).getTitulo());
        holder.votos.setText(String.valueOf(cancionArrayList.get(position).getVotos()));

        return convertView;
    }
}
