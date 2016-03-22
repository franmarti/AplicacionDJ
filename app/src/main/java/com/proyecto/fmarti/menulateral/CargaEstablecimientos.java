package com.proyecto.fmarti.menulateral;

/**
 * Created by fmarti on 07/03/2016.
 */
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;

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

import android.view.View;
import android.widget.Toast;


public class CargaEstablecimientos extends AppCompatActivity {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga_establecimientos);

        // Hashmap para el ListView
        listaUsuarios = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }//fin onCreate

    public void onClickEst(){

    }

   //ListViewAdapter


    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CargaEstablecimientos.this);
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
                    //Log.i("ramiro", "produtos.length" + products.length());
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

                        // creating new HashMap
                       // HashMap map = new HashMap();

                        // adding each child node to HashMap key => value
                       // map.put(TAG_DESCRIPCION, descripcion);
                        //map.put(TAG_NOMBRE, name);

                        //listaUsuarios.add(map);
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
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */

                    ListView lista = (ListView) findViewById(R.id.lvUsuarios);
                    adapter = new ListViewAdapter(getApplicationContext(), nombre, id, descripcion, imagen);
                    lista.setAdapter(adapter);

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                            switch (posicion) {
                                case 0:
                                    /*Intent ii = new Intent(getApplicationContext(), InfoEstablecimiento.class);
                                    startActivity(ii);*/
                                    Toast.makeText(getApplicationContext(), "Item 1", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(getApplicationContext(), "Item 2", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "Item 3", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }








}