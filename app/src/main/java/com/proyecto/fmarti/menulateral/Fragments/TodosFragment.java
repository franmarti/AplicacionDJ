package com.proyecto.fmarti.menulateral.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.fmarti.menulateral.InfoEstablecimiento;
import com.proyecto.fmarti.menulateral.JSONParser;
import com.proyecto.fmarti.menulateral.ListViewAdapter;
import com.proyecto.fmarti.menulateral.MainActivity;
import com.proyecto.fmarti.menulateral.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by fmarti on 22/03/2016.
 */
public class TodosFragment extends Fragment {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> listaUsuarios;


    // url to get all products list
    private static String url_all_establecimientos = "http://projectinf.esy.es/www/getAllEstablecimientos.php";
    private static String URL_IMAGENES = "http://projectinf.esy.es/imagenes/";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "establecimientos";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_MUSICA = "musica";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_IMAGEN = "rutaimagen";

    // products JSONArray
    JSONArray products = null;

    //variables
    ListViewAdapter adapter;
    ArrayList<String> nombre = new ArrayList<String>();
    ArrayList<String> id = new ArrayList<String>();
    ArrayList<String> musica = new ArrayList<String>();
    ArrayList<String> descripcion = new ArrayList<String>();
    ArrayList<Bitmap> imagen = new ArrayList<Bitmap>();

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_carga_establecimientos, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Establecimientos");

        // Hashmap para el ListView
        listaUsuarios = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        if(id.isEmpty()){
            new LoadAllProducts().execute();
        }
        else{
            ListView lista = (ListView) view.findViewById(R.id.lvUsuarios);
            adapter = new ListViewAdapter(getActivity(), nombre, id, descripcion, imagen);
            lista.setAdapter(adapter);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                    switch (posicion) {
                        case 0:
                            Intent ii = new Intent(getActivity(), InfoEstablecimiento.class);
                            startActivity(ii);
                            Toast.makeText(getActivity(), "Entrando al sitio...", Toast.LENGTH_SHORT).show();
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


//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }



        return view;
    }

    class LoadAllProducts extends AsyncTask<String, String, String> {

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
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String description = c.getString(TAG_DESCRIPCION);
                        String name = c.getString(TAG_NOMBRE);
                        String idJson = c.getString(TAG_ID);
                        String rutaimagen = c.getString(TAG_IMAGEN);

                        descripcion.add(description);
                        nombre.add(name);
                        id.add(idJson);
                        if(rutaimagen.equals("noimage")){
                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                        }
                        else{
                            URL urlImagen = new URL(URL_IMAGENES + rutaimagen);
                            HttpURLConnection conimagen = (HttpURLConnection) urlImagen.openConnection();
                            conimagen.connect();
                            bitmap = BitmapFactory.decodeStream(conimagen.getInputStream());
                        }
                        imagen.add(bitmap);
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

                    ListView lista = (ListView) view.findViewById(R.id.lvUsuarios);
                    adapter = new ListViewAdapter(getActivity(), nombre, id, descripcion, imagen);
                    lista.setAdapter(adapter);

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                            switch (posicion) {
                                case 0:
                                    Intent ii = new Intent(getActivity(), InfoEstablecimiento.class);
                                    startActivity(ii);
                                    Toast.makeText(getActivity(), "Entrando al sitio...", Toast.LENGTH_SHORT).show();
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
}
