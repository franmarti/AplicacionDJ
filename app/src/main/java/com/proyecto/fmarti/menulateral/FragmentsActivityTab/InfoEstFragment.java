package com.proyecto.fmarti.menulateral.FragmentsActivityTab;

/**
 * Created by fmarti on 07/04/2016.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;
import com.proyecto.fmarti.menulateral.R;

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
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_CIUDAD = "ciudad";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_IMAGEN = "rutaimagen";

    private View rootView;
    private Establecimiento establecimiento;

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
            String descripcion = getArguments().getString(TAG_DESCRIPCION);
            String ciudad = getArguments().getString(TAG_CIUDAD);
            String direccion = getArguments().getString(TAG_DIRECCION);
            byte[] byteArray = getActivity().getIntent().getByteArrayExtra(TAG_IMAGEN);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            establecimiento = new Establecimiento(Integer.parseInt(idEst), nombre, tpMusica, descripcion, ciudad, direccion, bitmap);


            //Carga de datos en el fragment
            ImageView imagen = (ImageView) rootView.findViewById(R.id.ivInfoEst);
            imagen.setImageBitmap(establecimiento.getImagen());

            TextView tvEstilo = (TextView) rootView.findViewById(R.id.tvInfoEstilo);
            TextView tvCiudad = (TextView) rootView.findViewById(R.id.tvInfoCiudad);
            TextView tvMasInfo = (TextView) rootView.findViewById(R.id.tvMasInfo);

            tvEstilo.setText(establecimiento.getTipoMusica());
            tvCiudad.setText(establecimiento.getCiudad());
            tvMasInfo.setText(establecimiento.getDescripcion());

            //Botón flotante de favorito
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "En construcción", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);



            //Mapa de google


            /*String mapPath = "https://goo.gl/maps/CBxC3QniAtQ2";

            WebView myWebView = (WebView) rootView.findViewById(R.id.webView);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new WebViewClient());

            myWebView.loadUrl(mapPath);*/
        }


        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
