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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.proyecto.fmarti.menulateral.JSONParser;
import com.proyecto.fmarti.menulateral.ListViewAdapter;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;
import com.proyecto.fmarti.menulateral.MainActivity;
import com.proyecto.fmarti.menulateral.R;
import com.proyecto.fmarti.menulateral.TabActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class BuscarFragment extends Fragment implements SearchView.OnQueryTextListener{

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    // url to get all products list
    private static String url_all_establecimientos = "http://projectinf.esy.es/www/getAllEstablecimientos.php";
    private static String url_por_tipos = "http://projectinf.esy.es/www/getEstPorTipo.php$tipo=";
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
    JSONArray products = null;

    //variables
    ListViewAdapter adapterList;

    ArrayList<Establecimiento> establecimientos = new ArrayList<Establecimiento>();
    ArrayList<Establecimiento> estAuxTipo = new ArrayList<Establecimiento>();
    ArrayList<Establecimiento> estAuxCiudad = new ArrayList<Establecimiento>();

    View view;
    SearchView searchView;
    ListView lista;
    Spinner spMusica, spCiudades;
    android.widget.Filter filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buscar, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Buscar");


        ArrayAdapter<CharSequence> adapter;

        spCiudades = (Spinner) view.findViewById(R.id.spCiudades);
        spMusica = (Spinner) view.findViewById(R.id.spMusica);

        searchView = (SearchView) view.findViewById(R.id.svBusqueda);

        //Asignas el origen de datos desde los recursos
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Ciudades, R.layout.spinner_personalizado);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCiudades.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Musica,
                R.layout.spinner_personalizado);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMusica.setAdapter(adapter);



        // Cargar los productos en el Background Thread
        ocultarTeclado();

        new CargaTodosEstablecimientos().execute();
        return view;
    }

    private void setupSearchView()
    {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
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

/*    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/


    class CargaTodosEstablecimientos extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Cargando establecimientos. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
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

                    spMusica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            String tipo = spMusica.getSelectedItem().toString().toLowerCase().trim();
                            if (tipo.equals("Todos los tipos".toLowerCase())) {
                                if(estAuxCiudad.isEmpty())
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
                                if(estAuxCiudad.isEmpty())
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
                            ocultarTeclado();
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
                            ocultarTeclado();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });

                    ocultarTeclado();


                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                            switch (posicion) {
                                case 0:
                                    Intent ii = new Intent(getActivity(), TabActivity.class);
                                    startActivity(ii);
                                    break;
                                case 1:
                                    Toast.makeText(getActivity(), "Item 2", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "Item 3", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }

    public void ocultarTeclado(){
        //Ocultar teclado
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}