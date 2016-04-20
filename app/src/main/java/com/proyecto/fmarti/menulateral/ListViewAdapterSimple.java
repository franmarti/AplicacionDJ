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

public class ListViewAdapterSimple extends BaseAdapter {
    // Declare Variables
    Context context;
    String[] titulos;
    int[] imagenes;
    LayoutInflater inflater;

    public ListViewAdapterSimple(Context context, String[] titulos) {
        this.context = context;
        this.titulos = titulos;
    }

    @Override
    public int getCount() {
        return titulos.length;
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
        TextView txtTitle;
        /*ImageView imgImgLike;
        ImageView imgDislike;*/

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_view_canciones, parent, false);

        // Locate the TextViews in listview_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.tvTituloElemento);
        /*imgImgLike = (ImageView) itemView.findViewById(R.id.ivLike);
        imgDislike = (ImageView) itemView.findViewById(R.id.ivDislike);*/

        // Capture position and set to the TextViews
        txtTitle.setText(titulos[position]);

        return itemView;
    }
}
