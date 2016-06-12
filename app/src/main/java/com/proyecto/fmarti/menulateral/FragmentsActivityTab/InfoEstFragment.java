package com.proyecto.fmarti.menulateral.FragmentsActivityTab;

/**
 * Created by fmarti on 07/04/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;
import com.proyecto.fmarti.menulateral.R;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class InfoEstFragment extends Fragment implements OnMapReadyCallback  {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "1";
    //Establecimientos
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_TIPO_MUSICA = "tipoMusica";
    private static final String TAG_CIUDAD = "ciudad";
    private static final String TAG_LATITUD = "latitud";
    private static final String TAG_LONGITUD = "longitud";
    private static final String TAG_IMAGEN = "rutaimagen";

    private View rootView;
    private Establecimiento establecimiento;
    private ArrayList<Integer> idFavoritos;

    private AsyncTask mTask;

    public InfoEstFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static InfoEstFragment newInstance(Bundle bundle) {
        InfoEstFragment fragment = new InfoEstFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.fragment_tab_info, container, false);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_info, container, false);

            //Recuperamos el establecimiento seleccionado
            String idEst = getArguments().getString(TAG_ID);
            String nombre = getArguments().getString(TAG_NOMBRE);
            String tpMusica = getArguments().getString(TAG_TIPO_MUSICA);
            String ciudad = getArguments().getString(TAG_CIUDAD);
            String latitud = getArguments().getString(TAG_LATITUD);
            String longitud = getArguments().getString(TAG_LONGITUD);
            byte[] byteArray = getActivity().getIntent().getByteArrayExtra(TAG_IMAGEN);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            establecimiento = new Establecimiento(Integer.parseInt(idEst), nombre, tpMusica, ciudad, latitud, longitud, bitmap);


            //Carga de datos en el fragment
            ImageView imagen = (ImageView) rootView.findViewById(R.id.ivInfoEst);
            imagen.setImageBitmap(establecimiento.getImagen());

            TextView tvEstilo = (TextView) rootView.findViewById(R.id.tvInfoEstilo);
            TextView tvCiudad = (TextView) rootView.findViewById(R.id.tvInfoCiudad);
            //TextView tvMasInfo = (TextView) rootView.findViewById(R.id.tvMasInfo);

            tvEstilo.setText(establecimiento.getTipoMusica());
            tvCiudad.setText(establecimiento.getCiudad());


            //cargarFavoritos();


            //Botón flotante de favorito
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "¡Añadido a favoritos!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });

            //Mapa de google
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


        return rootView;
    }

/*    @Override
    public void onStop() {
        super.onStop();
        //check the state of the task
        if(mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING)
            mTask.cancel(true);
    }*/

    @Override
    public void onMapReady(GoogleMap map) {
        MarkerOptions marker = new MarkerOptions();
        LatLng localizacion = new LatLng(Double.parseDouble(establecimiento.getLatitud()), Double.parseDouble(establecimiento.getLongitud()));
        map.addMarker(marker.position(localizacion).title(establecimiento.getNombre()));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(localizacion, 16));
        
    }

    /*public double[] getLatLng(){
        String loc = establecimiento.getDireccion();
        String[] latCad = loc.split("@");
        latCad = latCad[1].split(",");
        double lat = Double.parseDouble(latCad[0]);
        double lon = Double.parseDouble(latCad[1]);
        double coord[] = {lat, lon};
        System.out.println("Latitud: ----------> " + lat);
        System.out.println("Longitud: ----------> " + lon);
        return coord;
    }*/

  /*  public void cargarFavoritos(){
        SharedPreferences favoritos = getActivity().getSharedPreferences("listaFavoritos", Context.MODE_PRIVATE);
        Map<String, ?> todosFavoritos = favoritos.getAll();
        for (Map.Entry<String, ?> entry : todosFavoritos.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            idFavoritos.add(Integer.valueOf(entry.getValue().toString()));
        }
    }*/
}
