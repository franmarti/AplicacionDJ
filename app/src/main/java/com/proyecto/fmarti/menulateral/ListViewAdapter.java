package com.proyecto.fmarti.menulateral;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyecto.fmarti.menulateral.Logica.Establecimiento;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by fmarti on 22/03/2016.
 */
public class ListViewAdapter extends BaseAdapter implements Filterable {
    //Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<String> nombre;
    ArrayList<String> id;
    ArrayList<String> descripcion;
    ArrayList<Bitmap> imagen;

    public ArrayList<Establecimiento> estArrayList;
    public ArrayList<Establecimiento> orig;


    //Constructor ArrayList Strings
    public ListViewAdapter(Context context,  ArrayList<String> nombre, ArrayList<String> id, ArrayList<String> descripcion, ArrayList<Bitmap> imagen ) {
        this.context = context;
        this.nombre = nombre;
        this.id = id;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    //Constructor ArrayList Establecimiento
    public ListViewAdapter(Context context, ArrayList<Establecimiento> estArrayList) {
        super();
        this.context = context;
        this.estArrayList = estArrayList;
    }

    public class EstHolder
    {
        ImageView imgImg;
        TextView txtDescripcion;
        TextView txtNombre;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Establecimiento> results = new ArrayList<Establecimiento>();
                if (orig == null)
                    orig = estArrayList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Establecimiento est : orig) {
                            if (est.getNombre().toLowerCase().contains(constraint.toString()))
                                results.add(est);
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
                estArrayList = (ArrayList<Establecimiento>) results.values;
                notifyDataSetChanged();
            }


        };
    }


    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return estArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return estArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        EstHolder holder;

        if(convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.list_view_personalizado, parent, false);
            holder=new EstHolder();
            holder.imgImg = (ImageView) convertView.findViewById(R.id.imgTV);
            holder.txtNombre=(TextView) convertView.findViewById(R.id.tvNombre);
            holder.txtDescripcion=(TextView) convertView.findViewById(R.id.tvDescripcion);
            convertView.setTag(holder);
        }
        else
        {
            holder=(EstHolder) convertView.getTag();
        }

        holder.imgImg.setImageBitmap(estArrayList.get(position).getImagen());
        holder.txtNombre.setText(estArrayList.get(position).getNombre());
        holder.txtDescripcion.setText(String.valueOf(estArrayList.get(position).getDescripcion()));

        return convertView;
    }


}//fin ListViewAdapter
