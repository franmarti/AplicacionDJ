package com.proyecto.fmarti.menulateral.FragmentsActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proyecto.fmarti.menulateral.MainActivity;
import com.proyecto.fmarti.menulateral.R;
import com.proyecto.fmarti.menulateral.TabActivity;

/**
 * Created by fmarti on 07/04/2016.
 */
public class ListaFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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
