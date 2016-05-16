package com.proyecto.fmarti.menulateral;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.proyecto.fmarti.menulateral.FragmentsActivityTab.InfoEstFragment;
import com.proyecto.fmarti.menulateral.FragmentsActivityTab.CancionesFragment;
import com.proyecto.fmarti.menulateral.FragmentsActivityTab.PeticionesFragment;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
   // private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //Establecimientos
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_TIPO_MUSICA = "tipoMusica";
    private static final String TAG_DESCRIPCION = "descripcion";
    private static final String TAG_CIUDAD = "ciudad";
    private static final String TAG_DIRECCION = "direccion";
    private static final String TAG_IMAGEN = "rutaimagen";

    Bundle bundle = new Bundle();


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Activamos las pestañas
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Recuperamos el establecimiento seleccionado

        String idEst = getIntent().getStringExtra(TAG_ID);
        String nombre = getIntent().getStringExtra(TAG_NOMBRE);
        String tpMusica = getIntent().getStringExtra(TAG_TIPO_MUSICA);
        String descripcion = getIntent().getStringExtra(TAG_DESCRIPCION);
        String ciudad = getIntent().getStringExtra(TAG_CIUDAD);
        String direccion = getIntent().getStringExtra(TAG_DIRECCION);

        getSupportActionBar().setTitle(nombre);

        bundle.putString(TAG_ID, idEst);
        bundle.putString(TAG_NOMBRE, nombre);
        bundle.putString(TAG_TIPO_MUSICA, tpMusica);
        bundle.putString(TAG_DESCRIPCION, descripcion);
        bundle.putString(TAG_CIUDAD, ciudad);
        bundle.putString(TAG_DIRECCION, direccion);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());



        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);*/



    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(InfoEstFragment.newInstance(bundle), "Información");
        adapter.addFragment(PeticionesFragment.newInstance(bundle), "Peticiones");
        adapter.addFragment(CancionesFragment.newInstance(bundle), "Canciones");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
   /* public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a InfoEstFragment (defined as a static inner class below).

            //return PlaceholderFragment.newInstance(position + 1);
            switch (position) {
                case 0:
                    return InfoEstFragment.newInstance(position + 1);
                case 1:
                    return PeticionesFragment.newInstance(position + 1, bundle);
                case 2:
                    return CancionesFragment.newInstance(position + 1);

            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Información";
                case 1:
                    return "Sonando ahora";
                case 2:
                    return "Listas";
            }
            return null;
        }
    }*/
}
