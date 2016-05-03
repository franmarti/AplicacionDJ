package com.proyecto.fmarti.menulateral.FragmentsActivityTab;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyecto.fmarti.menulateral.R;

/**
 * Created by fmarti on 07/04/2016.
 */
public class ListaFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "3";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ListaFragment newInstance(int sectionNumber) {
        ListaFragment fragment = new ListaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static ListaFragment newInstance(Bundle arguments){
        ListaFragment f = new ListaFragment();
        if(arguments != null){
            f.setArguments(arguments);
        }
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_listas, container, false);



        //((TabActivity) getActivity()).getSupportActionBar().setTitle("Listas");
        /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }
}
