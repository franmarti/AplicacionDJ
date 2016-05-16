package com.proyecto.fmarti.menulateral;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.fmarti.menulateral.Logica.Establecimiento;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by fmarti on 22/03/2016.
 */
public class ListViewAdapter extends BaseAdapter implements Filterable {
    //Variables
    Context context;
    ArrayList<String> nombre;
    ArrayList<String> id;
    ArrayList<String> descripcion;
    ArrayList<Bitmap> imagen;

    public ArrayList<Establecimiento> estArrayList;
    public ArrayList<Establecimiento> orig;


    //Constructor ArrayList Strings
    public ListViewAdapter(Context context, ArrayList<String> nombre, ArrayList<String> id, ArrayList<String> descripcion, ArrayList<Bitmap> imagen) {
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

    public class EstHolder {
        ImageView imgImg;
        TextView tvEstilo;
        TextView tvNombre;
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

        final EstHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_view_personalizado, parent, false);
            holder = new EstHolder();
            holder.imgImg = (ImageView) convertView.findViewById(R.id.imgTV);
            holder.tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);
            holder.tvEstilo = (TextView) convertView.findViewById(R.id.tvEstilo);

            convertView.setTag(holder);
        } else {
            holder = (EstHolder) convertView.getTag();
        }

        holder.imgImg.setImageBitmap(estArrayList.get(position).getImagen());
        holder.tvNombre.setText(estArrayList.get(position).getNombre());
        holder.tvEstilo.setText(estArrayList.get(position).getTipoMusica());

        holder.imgImg.setOnClickListener(new imageViewClickListener(position));

        return convertView;
    }

    class imageViewClickListener implements View.OnClickListener {
        int position;

        public imageViewClickListener(int pos) {
            this.position = pos;
        }

        public void onClick(View v) {
            {
                Dialog nagDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.imagen_full_screen);
                ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_preview_image);
                ivPreview.setImageBitmap(estArrayList.get(position).getImagen());
                nagDialog.show();
            }
        }
    }


}//fin ListViewAdapter
