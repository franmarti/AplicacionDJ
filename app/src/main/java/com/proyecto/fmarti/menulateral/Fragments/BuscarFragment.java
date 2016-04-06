package com.proyecto.fmarti.menulateral.Fragments;

/**
 * Created by fmarti on 10/03/2016.
 */
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.proyecto.fmarti.menulateral.MainActivity;
import com.proyecto.fmarti.menulateral.R;


public class BuscarFragment extends Fragment {

    MenuItem menuItem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_buscar, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Buscar");


        ArrayAdapter<CharSequence> adapter;

        Spinner spProvincias = (Spinner) view.findViewById(R.id.spProvincias);
        Spinner spMusica = (Spinner) view.findViewById(R.id.spMusica);

        SearchView searchView = (SearchView) view.findViewById(R.id.svBusqueda);/*
        searchView.setIconifiedByDefault(false);*/

        //Asignas el origen de datos desde los recursos
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Provincias,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvincias.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Musica,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMusica.setAdapter(adapter);




        return view;
    }




}
