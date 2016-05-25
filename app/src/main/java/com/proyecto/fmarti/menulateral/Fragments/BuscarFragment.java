package com.proyecto.fmarti.menulateral.Fragments;

/**
 * Created by fmarti on 10/03/2016.
 */
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.proyecto.fmarti.menulateral.JSONParser;
import com.proyecto.fmarti.menulateral.Adapters.ListViewAdapter;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;
import com.proyecto.fmarti.menulateral.MainActivity;
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
import java.util.Iterator;
import java.util.List;


public class BuscarFragment extends Fragment implements SearchView.OnQueryTextListener {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    private JSONParser jParser = new JSONParser();

    // url to get all products list
    private static String url_all_establecimientos = "http://projectinf.esy.es/www/getAllEstablecimientos.php";
    private static String URL_IMAGENES = "http://projectinf.esy.es/imagenes/";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ESTABLECIMIENTOS = "establecimientos";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_TIPO_MUSICA = "tipoMusica";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_CIUDAD = "ciudad";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_IMAGEN = "rutaimagen";


    // products JSONArray
    private JSONArray products = null;

    //variables
    private ListViewAdapter adapterList;

    private ArrayList<Establecimiento> establecimientos = new ArrayList<Establecimiento>();
    private ArrayList<Establecimiento> estAuxTipo = new ArrayList<Establecimiento>();
    private ArrayList<Establecimiento> estAuxCiudad = new ArrayList<Establecimiento>();

    private View view;
    private SearchView searchView;
    private ListView lista;
    private Spinner spMusica, spCiudades;
    private android.widget.Filter filter;
    private SwipeRefreshLayout swipeContainer;
    private ImageView imagen;

    private AsyncTask mTask;
    public static float screen_width;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view==null){
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_buscar, container, false);

            ((MainActivity) getActivity()).getSupportActionBar().setTitle("Buscar");


            ArrayAdapter<CharSequence> adapter;

            spCiudades = (Spinner) view.findViewById(R.id.spCiudades);
            spMusica = (Spinner) view.findViewById(R.id.spMusica);

            searchView = (SearchView) view.findViewById(R.id.svBusqueda);
            searchView.clearFocus();

            //Asignas el origen de datos desde los recursos
            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Ciudades, R.layout.spinner_personalizado);

            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCiudades.setAdapter(adapter);

            adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Musica,
                    R.layout.spinner_personalizado);

            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMusica.setAdapter(adapter);

            imagen = (ImageView) view.findViewById(R.id.imgTV);

            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

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



            // Cargar los productos en el Background Thread
            mTask = new CargaTodosEstablecimientos().execute();
            // ocultarTeclado();
        }

        return view;

    }

    @Override
    public void onStop() {
        super.onStop();
        //check the state of the task
        if(mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING)
            mTask.cancel(true);
    }

    public void fetchTimelineAsync() {
        establecimientos.clear();
        estAuxCiudad.clear();
        estAuxTipo.clear();
        spMusica.setSelection(0);
        spCiudades.setSelection(0);
        if(mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING)
            mTask.cancel(true);
        mTask = new CargaTodosEstablecimientos().execute();
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



    class CargaTodosEstablecimientos extends AsyncTask<String, String, String> {

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
            cargaDatos();
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

                    lista = (ListView) view.findViewById(R.id.lvEstablecimientos);
                    adapterList = new ListViewAdapter(getActivity(), establecimientos);
                    lista.setAdapter(adapterList);


                    filter = adapterList.getFilter();
                    setupSearchView();

                    //Listeners


                    spMusica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            String tipo = spMusica.getSelectedItem().toString().toLowerCase().trim();
                            if (tipo.equals("Todos los estilos".toLowerCase())) {
                                if (estAuxCiudad.isEmpty())
                                    adapterList = new ListViewAdapter(getActivity(), establecimientos);
                                else
                                    adapterList = new ListViewAdapter(getActivity(), estAuxCiudad);
                                estAuxTipo.clear();
                                lista.setAdapter(adapterList);
                                filter = adapterList.getFilter();
                                setupSearchView();
                            } else {
                                estAuxTipo = new ArrayList<Establecimiento>();
                                Iterator<Establecimiento> iterator;
                                if (estAuxCiudad.isEmpty())
                                    iterator = establecimientos.iterator();
                                else
                                    iterator = estAuxCiudad.iterator();

                                while (iterator.hasNext()) {
                                    Establecimiento log = iterator.next();
                                    if (tipo.equals(log.getTipoMusica().toLowerCase().trim())) {
                                        estAuxTipo.add(log);
                                    }
                                }
                                adapterList = new ListViewAdapter(getActivity(), estAuxTipo);
                                lista.setAdapter(adapterList);

                                filter = adapterList.getFilter();
                                setupSearchView();
                            }
                           // ocultarTeclado();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

                    spCiudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            String tipo = spCiudades.getSelectedItem().toString().toLowerCase().trim();
                            spMusica.setSelection(0);
                            estAuxTipo.clear();
                            //spMusica.notify();
                            if (tipo.equals("Todas las ciudades".toLowerCase())) {
                                estAuxCiudad.clear();
                                adapterList = new ListViewAdapter(getActivity(), establecimientos);
                                lista.setAdapter(adapterList);
                                filter = adapterList.getFilter();
                                setupSearchView();
                            } else {

                                estAuxCiudad = new ArrayList<Establecimiento>();
                                Iterator<Establecimiento> iterator = establecimientos.iterator();
                                while (iterator.hasNext()) {
                                    Establecimiento log = iterator.next();
                                    if (tipo.equals(log.getCiudad().toLowerCase().trim())) {
                                        estAuxCiudad.add(log);
                                    }
                                }
                                adapterList = new ListViewAdapter(getActivity(), estAuxCiudad);
                                lista.setAdapter(adapterList);

                                filter = adapterList.getFilter();
                                setupSearchView();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

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
                            intent.putExtra(TAG_DESCRIPCION, est.getDescripcion());
                            intent.putExtra(TAG_CIUDAD, est.getCiudad());
                            intent.putExtra(TAG_DIRECCION, est.getDireccion());
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

    public void cargaDatos(){
        List params = new ArrayList();


        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_establecimientos, "GET", params);
        Bitmap bitmap;
        // Check your log cat for JSON reponse
        Log.d("All Products: ", json.toString());

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray(TAG_ESTABLECIMIENTOS);

                // looping through All Products
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    // Storing each json item in variable
                    String idEst = c.getString(TAG_ID);
                    String nombre = c.getString(TAG_NOMBRE);
                    String tpMusica = c.getString(TAG_TIPO_MUSICA);
                    String descripcion = c.getString(TAG_DESCRIPCION);
                    String ciudad = c.getString(TAG_CIUDAD);
                    String direccion = c.getString(TAG_DIRECCION);
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

                    establecimientos.add(new Establecimiento(Integer.parseInt(idEst), nombre, tpMusica, descripcion, ciudad, direccion, bitmap));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargando(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cargando establecimientos. Por favor espere...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

}