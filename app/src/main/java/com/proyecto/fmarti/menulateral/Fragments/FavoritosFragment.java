package com.proyecto.fmarti.menulateral.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.proyecto.fmarti.menulateral.Adapters.ListViewAdapter;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;
import com.proyecto.fmarti.menulateral.MainActivity;
import com.proyecto.fmarti.menulateral.ParserJSON;
import com.proyecto.fmarti.menulateral.R;
import com.proyecto.fmarti.menulateral.TabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fmarti on 07/04/2016.
 */
public class FavoritosFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "2";
    private ArrayList<Establecimiento> favoritos = new ArrayList<Establecimiento>();

    // Creating JSON Parser object
    private ParserJSON jParser = new ParserJSON();

    // Progress Dialog
    private ProgressDialog pDialog;

    // url to get all products list
    private static String url_favoritos = "http://projectinf.esy.es/www/getFavoritos.php";
    private static String URL_IMAGENES = "http://projectinf.esy.es/imagenes/";

    // JSON Node names Canciones
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_FAVORITOS = "favoritos";
    private static final String TAG_ID = "id";
    private static final String TAG_AUTOR = "autor";
    private static final String TAG_TITULO = "titulo";
    private static final String TAG_ALBUM = "album";
    private static final String TAG_GENERO = "genero";
    private static final String TAG_VOTOS = "votos";

    //Establecimientos
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_TIPO_MUSICA = "tipoMusica";
    private static final String TAG_CIUDAD = "ciudad";
    private static final String TAG_LATITUD = "latitud";
    private static final String TAG_LONGITUD = "longitud";
    private static final String TAG_IMAGEN = "rutaimagen";

    //ListaCancion
    private static final String TAG_PEDIDA = "pedida";
    private static final String TAG_ID_LISTA_CANCION = "idListaCancion";


    private static final String ARG_ESTABLECIMIENTO = "establecimiento";

    private View rootView;
    private Establecimiento establecimiento;


    // products JSONArray
    private JSONArray products = null;

    private SwipeRefreshLayout swipeContainer;
    private SearchView searchView;
    private TextView tvVacio;
    private ListViewAdapter adapterList;
    private ListView lista;
    private View view;
    private String favString="";
    private String[] listaFavoritos;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.fragment_tab_peticiones, container, false);

        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_favoritos, container, false);
            tvVacio = (TextView) view.findViewById(R.id.tvOcultoFavoritos);

            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Favoritos");

            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainerFavoritos);

            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    fetchTimelineAsync();
                    swipeContainer.setRefreshing(false);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            cargarFavoritos();
            new CargaFavoritos().execute(favString);
        }

        //((TabActivity) getActivity()).getSupportActionBar().setTitle("Sonando");
        /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return view;
    }

    public void fetchTimelineAsync() {
        favoritos.clear();
        cargarFavoritos();
        new CargaFavoritos().execute(favString);
    }



    class CargaFavoritos extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cargando();
        }

        /**
         * obteniendo todos los productos
         */
        protected String doInBackground(String... args) {

            HashMap<String, String> params = new HashMap<>();
            params.put("favoritos", args[0]);
            // getting JSON string from URL
            // Check your log cat for JSON reponse

            System.out.println("Parámetros ----------> " + params.toString());

            JSONObject json = jParser.makeHttpRequest(url_favoritos, "POST", params);
            Bitmap bitmap;
            // Check your log cat for JSON reponse
            //Log.d("Favoritos: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                /*int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {*/
                    // products found
                    // Getting Array of Products

                    products = json.getJSONArray(TAG_FAVORITOS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String idEst = c.getString(TAG_ID);
                        String nombre = c.getString(TAG_NOMBRE);
                        String tpMusica = c.getString(TAG_TIPO_MUSICA);
                        String ciudad = c.getString(TAG_CIUDAD);
                        String latitud = c.getString(TAG_LATITUD);
                        String longitud = c.getString(TAG_LONGITUD);
                        String rutaimagen = c.getString(TAG_IMAGEN);

                        //Image to Bitmap
                        if(rutaimagen.equals("noimage")){
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                        }
                        else{
                            URL urlImagen = new URL(URL_IMAGENES + rutaimagen);
                            HttpURLConnection conimagen = (HttpURLConnection) urlImagen.openConnection();
                            conimagen.connect();
                            bitmap = BitmapFactory.decodeStream(conimagen.getInputStream());
                        }

                        favoritos.add(new Establecimiento(Integer.parseInt(idEst), nombre, tpMusica, ciudad, latitud, longitud, bitmap));

                    }
               // }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    lista = (ListView) view.findViewById(R.id.lvFavoritos);
                    adapterList = new ListViewAdapter(getActivity(), favoritos);
                    lista.setAdapter(adapterList);

                    if (adapterList.isEmpty()) {
                        tvVacio.setVisibility(rootView.VISIBLE);
                        tvVacio.setText("¡Todavía no has guardado ningún favorito!");
                    } else {
                        tvVacio.setVisibility(rootView.INVISIBLE);
                        tvVacio.setText("");
                    }

                    //Listeners

                    //Paso de Establecimiento a la clase TabActivity
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                            Establecimiento est;

                            est = (Establecimiento) adapterView.getAdapter().getItem(posicion);

                            Intent intent = new Intent(getActivity(), TabActivity.class);
                            intent.putExtra(TAG_ID, String.valueOf(est.getId()));
                            intent.putExtra(TAG_NOMBRE, est.getNombre());
                            intent.putExtra(TAG_TIPO_MUSICA, est.getTipoMusica());
                            intent.putExtra(TAG_CIUDAD, est.getCiudad());
                            intent.putExtra(TAG_LATITUD, est.getLatitud());
                            intent.putExtra(TAG_LONGITUD, est.getLongitud());
                            //Convert to byte array
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            est.getImagen().compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            intent.putExtra(TAG_IMAGEN, byteArray);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
    }



    public void cargando() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cargando canciones. Por favor espere...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void anyadirFavoritos() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("listaFavoritos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fav", favString);
        editor.commit();
    }

    public String[] cargarFavoritos() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("listaFavoritos", Context.MODE_PRIVATE);
        favString = sharedPreferences.getString("fav", null);
        if(favString == null){
            favString = "";
            return null;
        }else{
            String auxFav = favString;
            return auxFav.split("\\.");
        }

    }
}