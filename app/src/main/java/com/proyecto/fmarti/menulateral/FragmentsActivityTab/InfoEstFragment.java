package com.proyecto.fmarti.menulateral.FragmentsActivityTab;

/**
 * Created by fmarti on 07/04/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.proyecto.fmarti.menulateral.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class InfoEstFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "1";

    private View rootView;


    public InfoEstFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static InfoEstFragment newInstance(Bundle bundle) {
        InfoEstFragment fragment = new InfoEstFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.fragment_tab_info, container, false);

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.fragment_tab_info, container, false);

            // Initialise your layout here

        }

        /*TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));*/
        return rootView;
    }
}
