package com.proyecto.fmarti.menulateral.FragmentsActivityTab;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.proyecto.fmarti.menulateral.ListViewAdapterSimple;
import com.proyecto.fmarti.menulateral.Logica.Cancion;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;
import com.proyecto.fmarti.menulateral.ParserJSON;
import com.proyecto.fmarti.menulateral.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fmarti on 07/04/2016.
 */
public class ListaCancionesFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String ARG_SECTION_NUMBER = "2";
    ListViewAdapterSimple adapterList;
    ArrayList<Cancion> canciones = new ArrayList<Cancion>();

    // Creating JSON Parser object
    ParserJSON jParser = new ParserJSON();

    // Progress Dialog
    private ProgressDialog pDialog;

    // url to get all products list
    private static String url_canciones = "http://projectinf.esy.es/www/getAllCanciones.php";

    // JSON Node names Canciones
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CANCIONES = "cancionesArray";
    private static final String TAG_ID = "id";
    private static final String TAG_AUTOR = "autor";
    private static final String TAG_TITULO = "titulo";
    private static final String TAG_ALBUM = "album";
    private static final String TAG_GENERO = "genero";

    //Establecimientos
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_TIPO_MUSICA = "tipoMusica";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_CIUDAD = "ciudad";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_IMAGEN = "rutaimagen";


    private static final String ARG_ESTABLECIMIENTO = "establecimiento";

    private View rootView;
    private Establecimiento establecimiento;


    // products JSONArray
    private JSONArray products = null;
    private android.widget.Filter filter;
    private SwipeRefreshLayout swipeContainer;
    private SearchView searchView;
    private TextView tvVacio;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    /*public static PeticionesFragment newInstance(int sectionNumber, Bundle bundle) {
        PeticionesFragment fragment = new PeticionesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(bundle);
        return fragment;
    }*/

    public static ListaCancionesFragment newInstance(Bundle bundle) {
        ListaCancionesFragment fragment = new ListaCancionesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.fragment_tab_peticiones, container, false);

        if (rootView == null){
            rootView = inflater.inflate(R.layout.fragment_tab_canciones, container, false);
            tvVacio = (TextView) rootView.findViewById(R.id.tvOcultoCanciones);

            //Recuperamos el establecimiento seleccionado
            String idEst = getArguments().getString(TAG_ID);
            String nombre = getArguments().getString(TAG_NOMBRE);
            String tpMusica = getArguments().getString(TAG_TIPO_MUSICA);
            String descripcion = getArguments().getString(TAG_DESCRIPCION);
            String ciudad = getArguments().getString(TAG_CIUDAD);
            String direccion = getArguments().getString(TAG_DIRECCION);
            //Bitmap bitmap = (Bitmap) getArguments().getParcelable(TAG_IMAGEN);

            establecimiento = new Establecimiento(Integer.parseInt(idEst), nombre, tpMusica, descripcion, ciudad, direccion, null);

            searchView = (SearchView) rootView.findViewById(R.id.svBusquedaAutor);
            searchView.clearFocus();

            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainerCanciones);

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

            new CargaCanciones().execute(String.valueOf(establecimiento.getId()));
        }

        //((TabActivity) getActivity()).getSupportActionBar().setTitle("Sonando");
        /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }

    public void fetchTimelineAsync() {
        canciones.clear();
        new CargaCanciones().execute(String.valueOf(establecimiento.getId()));
        // ocultarTeclado();
    }


    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.clearFocus();
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        filter.filter(newText);
        return true;
    }



    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    class CargaCanciones extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cargando();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {

            HashMap<String, String> params = new HashMap<>();
            params.put("idEst", args[0]);
            // getting JSON string from URL
            // Check your log cat for JSON reponse

            JSONObject json = jParser.makeHttpRequest(url_canciones, "POST", params);
            try {

                // Checking for SUCCESS TAG
                // int success = json.getInt(TAG_SUCCESS);

                //if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray(TAG_CANCIONES);

                // looping through All Products
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    // Storing each json item in variable
                    String idCancion = c.getString(TAG_ID);
                    String autor = c.getString(TAG_AUTOR);
                    String titulo = c.getString(TAG_TITULO);
                    String album = c.getString(TAG_ALBUM);
                    String genero = c.getString(TAG_GENERO);

                    canciones.add(new Cancion(Integer.parseInt(idCancion), autor, titulo, album, genero));

                }
                //}
            } catch (JSONException e) {
                Log.e("Error canciones: ", json.toString());
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    ListView lista = (ListView) rootView.findViewById(R.id.lvCanciones);
                    adapterList = new ListViewAdapterSimple(getActivity(), canciones);
                    lista.setAdapter(adapterList);

                    filter = adapterList.getFilter();
                    setupSearchView();

                    if (adapterList.isEmpty()) {
                        tvVacio.setVisibility(rootView.VISIBLE);
                        tvVacio.setText("Este sitio todavía no ha publicado ninguna canción");
                    } else {
                        tvVacio.setVisibility(rootView.INVISIBLE);
                        tvVacio.setText("");
                    }
                }
            });
        }
    }

    public void cargando(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cargando canciones. Por favor espere...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}