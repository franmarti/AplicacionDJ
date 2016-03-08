package com.proyecto.fmarti.menulateral;

/**
 * Created by fmarti on 07/03/2016.
 */
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CargaDatosRemoto extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> listaUsuarios;


    // url to get all products list
    private static String url_all_usuarios = "http://projectinf.esy.es/www/getAllEstablecimientos.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "usuarios";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_MUSICA = "musica";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_IMAGEN = "imagen";

    // products JSONArray
    JSONArray products = null;

    //variables
    ListViewAdapter adapter;
    ListView lista;
    ArrayList<String> nombre = new ArrayList<String>();
    ArrayList<String> id = new ArrayList<String>();
    ArrayList<String> musica = new ArrayList<String>();
    ArrayList<String> descripcion = new ArrayList<String>();
    ArrayList<String> imagen = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_datos_remoto);

        // Hashmap para el ListView
        listaUsuarios = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();




        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }//fin onCreate

    public class ListViewAdapter extends BaseAdapter {
        // Declare Variables
        Context context;
        LayoutInflater inflater;
        ArrayList<String> nombre;
        ArrayList<String> id;



        public ListViewAdapter(Context context,  ArrayList<String> nombre, ArrayList<String> id ) {
            this.context = context;
            this.nombre = nombre;
            this.id = id;
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
            TextView txtId;
            TextView txtNombre;

            //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = inflater.inflate(R.layout.list_row, parent, false);

            // Locate the TextViews in listview_item.xml
            imgImg = (ImageView) itemView.findViewById(R.id.imgTV);
            txtId = (TextView) itemView.findViewById(R.id.tvId);
            txtNombre = (TextView) itemView.findViewById(R.id.tvNombre);

            // Capture position and set to the TextViews
            imgImg.setImageResource(R.drawable.header);
            txtId.setText(id.get(position));
            txtNombre.setText(nombre.get(position));

            return itemView;
        }
    }//fin ListViewAdapter


    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CargaDatosRemoto.this);
            pDialog.setMessage("Cargando usuarios. Por favor espere...");
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
            JSONObject json = jParser.makeHttpRequest(url_all_usuarios, "GET", params);

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
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String idJson = c.getString(TAG_ID);
                        String name = c.getString(TAG_NOMBRE);

                        id.add(idJson);
                        nombre.add(name);

                        // creating new HashMap
                        HashMap map = new HashMap();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, idJson);
                        map.put(TAG_NOMBRE, name);

                        listaUsuarios.add(map);
                    }
                }
            } catch (JSONException e) {
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
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    /*ListAdapter adapter = new SimpleAdapter(
                            CargaDatosRemoto.this,
                            listaUsuarios,
                            R.layout.single_post,
                            new String[] {
                                    TAG_ID,
                                    TAG_NOMBRE,
                            },
                            new int[] {
                                    R.id.single_post_tv_id,
                                    R.id.single_post_tv_nombre,
                            });

                    lista.setAdapter(adapter);
                    */
                    ListView lista = (ListView) findViewById(R.id.lvUsuarios);
                    adapter = new ListViewAdapter(getApplicationContext(), nombre, id);
                    lista.setAdapter(adapter);
                }
            });
        }
    }








}