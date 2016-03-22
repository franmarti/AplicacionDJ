package com.proyecto.fmarti.menulateral;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fmarti on 22/03/2016.
 */
public class ListViewAdapter extends BaseAdapter {
    //Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<String> nombre;
    ArrayList<String> id;
    ArrayList<String> descripcion;
    ArrayList<Bitmap> imagen;


    //Constructor
    public ListViewAdapter(Context context,  ArrayList<String> nombre, ArrayList<String> id, ArrayList<String> descripcion, ArrayList<Bitmap> imagen ) {
        this.context = context;
        this.nombre = nombre;
        this.id = id;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        ImageView imgImg;
        TextView txtDescripcion;
        TextView txtNombre;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_view_personalizado, parent, false);

        // Locate the TextViews in listview_item.xml
        imgImg = (ImageView) itemView.findViewById(R.id.imgTV);
        txtDescripcion = (TextView) itemView.findViewById(R.id.tvDescripcion);
        txtNombre = (TextView) itemView.findViewById(R.id.tvNombre);

        // Capture position and set to the TextViews
        imgImg.setImageBitmap(imagen.get(position));
        txtDescripcion.setText(descripcion.get(position));
        txtNombre.setText(nombre.get(position));

        return itemView;
    }
}//fin ListViewAdapter
