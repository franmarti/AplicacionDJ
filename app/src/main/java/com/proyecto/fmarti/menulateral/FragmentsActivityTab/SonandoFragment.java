package com.proyecto.fmarti.menulateral.FragmentsActivityTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.proyecto.fmarti.menulateral.ListViewAdapterSimple;
import com.proyecto.fmarti.menulateral.R;

/**
 * Created by fmarti on 07/04/2016.
 */


public class SonandoFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    ListViewAdapterSimple adapterList;

    String[] titulo = new String[]{
            "Sitio 1",
            "Sitio 2",
            "Sitio 3",
            "Sitio 4",
    };

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SonandoFragment newInstance(int sectionNumber) {
        SonandoFragment fragment = new SonandoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_sonando, container, false);

        ListView lista = (ListView) rootView.findViewById(R.id.lvSonando);
        adapterList = new ListViewAdapterSimple(getActivity(), titulo);
        lista.setAdapter(adapterList);

        //((TabActivity) getActivity()).getSupportActionBar().setTitle("Sonando");
        /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }


}
