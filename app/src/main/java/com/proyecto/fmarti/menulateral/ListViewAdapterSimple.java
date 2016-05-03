package com.proyecto.fmarti.menulateral;

/**
 * Created by fmarti on 20/04/2016.
 */


import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.proyecto.fmarti.menulateral.Logica.Cancion;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;

import java.util.ArrayList;

public class ListViewAdapterSimple extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] titulos, autores;

    public ArrayList<Cancion> cancionArrayList;

    public ListViewAdapterSimple(Context context, String[] autores, String[] titulos) {
        super();
        this.context = context;
        this.autores = autores;
        this.titulos = titulos;
    }

    public ListViewAdapterSimple(Context context,ArrayList<Cancion> canciones) {
        super();
        this.context = context;
        this.cancionArrayList = canciones;
    }

    public class EstHolder {
        TextView autor;
        TextView titulo;
    }

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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_canciones, parent, false);
            holder = new EstHolder();
            holder.autor = (TextView) convertView.findViewById(R.id.tvAutor);
            holder.titulo = (TextView) convertView.findViewById(R.id.tvTitulo);
            convertView.setTag(holder);
        } else {
            holder = (EstHolder) convertView.getTag();
        }

        holder.autor.setText(cancionArrayList.get(position).getAutor());
        holder.titulo.setText(cancionArrayList.get(position).getTitulo());

        return convertView;
    }
}
