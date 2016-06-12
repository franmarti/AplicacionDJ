package com.proyecto.fmarti.menulateral.FragmentsActivityTab;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.fmarti.menulateral.Adapters.ListViewAdapterPeticiones;
import com.proyecto.fmarti.menulateral.Logica.Cancion;
import com.proyecto.fmarti.menulateral.Logica.Establecimiento;
import com.proyecto.fmarti.menulateral.ParserJSON;
import com.proyecto.fmarti.menulateral.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by fmarti on 07/04/2016.
 */


public class PeticionesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "2";
    ListViewAdapterPeticiones adapterList;
    ArrayList<Cancion> canciones = new ArrayList<Cancion>();

    // Creating JSON Parser object
    ParserJSON jParser = new ParserJSON();

    // Progress Dialog
    private ProgressDialog pDialog;
    private TextView tvVacio;

    // url to get all products list
    private static String url_canciones = "http://projectinf.esy.es/www/getCancionesPedidas.php";
    private static String url_voto = "http://projectinf.esy.es/www/setVoto.php";
    // JSON Node names Canciones
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_CANCIONES = "cancionesArray";
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
    private SwipeRefreshLayout swipeContainer;
    private Establecimiento establecimiento;


    // products JSONArray
    private JSONArray products = null;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public static PeticionesFragment newInstance(Bundle bundle) {
        PeticionesFragment fragment = new PeticionesFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_tab_peticiones, container, false);
            tvVacio = (TextView) rootView.findViewById(R.id.tvOcultoPeticiones);

            //Recuperamos el establecimiento seleccionado
            String idEst = getArguments().getString(TAG_ID);
            String nombre = getArguments().getString(TAG_NOMBRE);
            String tpMusica = getArguments().getString(TAG_TIPO_MUSICA);
            String ciudad = getArguments().getString(TAG_CIUDAD);
            String latitud = getArguments().getString(TAG_LATITUD);
            String longitud = getArguments().getString(TAG_LONGITUD);
            //Bitmap bitmap = (Bitmap) getArguments().getParcelable(TAG_IMAGEN);

            establecimiento = new Establecimiento(Integer.parseInt(idEst), nombre, tpMusica, ciudad, latitud, longitud, null);

            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainerPeticiones);

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

            new CargaCancionesPedidas().execute(String.valueOf(establecimiento.getId()));
            // Initialise your layout here

       }


        return rootView;
    }

    public void fetchTimelineAsync() {
        canciones.clear();
        new CargaCancionesPedidas().execute(String.valueOf(establecimiento.getId()));
    }

    class CargaCancionesPedidas extends AsyncTask<String, String, String> {

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
            params.put("pedida", "si");
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
                        String pedida = c.getString(TAG_PEDIDA);
                        String idListaCancion = c.getString(TAG_ID_LISTA_CANCION);
                        String votos = c.getString(TAG_VOTOS);

                        canciones.add(new Cancion(Integer.parseInt(idCancion), autor, titulo, album, genero, pedida,
                                Integer.parseInt(idListaCancion), Integer.parseInt(votos)));

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

                    ListView lista = (ListView) rootView.findViewById(R.id.lvSonando);
                    adapterList = new ListViewAdapterPeticiones(getActivity(), canciones);
                    lista.setAdapter(adapterList);

                    if (adapterList.isEmpty()) {
                        tvVacio.setVisibility(rootView.VISIBLE);
                        tvVacio.setText("Todavía no hay peticiones, ¡se el primero en hacer una!");
                    } else {
                        tvVacio.setVisibility(rootView.INVISIBLE);
                        tvVacio.setText("");

                        //Añadimos la canción a la lista de peticiones
                        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                                final Cancion cancion = (Cancion) adapterView.getAdapter().getItem(posicion);
                                final AlertDialog.Builder builder =
                                        new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                                builder.setTitle("¿Quieres votar esta canción?");
                                builder.setMessage(cancion.getAutor() + " - " + cancion.getTitulo());
                                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new Votar().execute(String.valueOf(cancion.getIdCancion()), String.valueOf(cancion.getIdListaCancion()));
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                }
            });
        }
    }

    class Votar extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Votando...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         */
        protected String doInBackground(String... args) {
            postPeticion(url_voto, args[0], args[1]);
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            Toast.makeText(getActivity(), "¡Voto enviado!", Toast.LENGTH_SHORT).show();
            fetchTimelineAsync();
        }
    }

    public String postPeticion(String requestURL, String idCancion, String idListaCancion) {

        HashMap<String, String> params = new HashMap<>();
        params.put("idLista", idListaCancion);
        params.put("idCancion", idCancion);
        params.put("pedida", "si");

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            System.out.println("Esto es el resultado ----------> " + result);
        }

        return result.toString();
    }


    public void cargando(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Cargando canciones. Por favor espere...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        //pDialog.show();
    }
}
