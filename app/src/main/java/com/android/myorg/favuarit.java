package com.android.myorg;

/**
 * Created by anteneh on 7/18/2016.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class favuarit extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataSource dataSource;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private orgRecycleAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private boolean amh;
    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MainActivity.this.amh=settings.getBoolean("amh_values",true);
        setContentView(R.layout.activity_main);
        if(amh){
            Configuration config=getBaseContext().getResources().getConfiguration();
            Locale locale=new Locale("am");
            config.locale=locale;
            getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
            recreate();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        settings= PreferenceManager.getDefaultSharedPreferences(this);

        listener=new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                favuarit.this.amh=settings.getBoolean("amh_values",true);

                if(amh){
                    Configuration config=getBaseContext().getResources().getConfiguration();
                    Locale locale=new Locale("am");
                    config.locale=locale;
                    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                    recreate();
                }
                else {
                    Configuration config=getBaseContext().getResources().getConfiguration();
                    Locale locale=new Locale("en");
                    config.locale=locale;
                    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                    recreate();
                }
            }
        };
        settings.registerOnSharedPreferenceChangeListener(listener);


    }
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_main(),getResources().getString(R.string.home));
        adapter.addFragment(new fragment_catagorie(),getResources().getString(R.string.categories));
        adapter.addFragment(new fragment_business(),getResources().getString(R.string.business));
        adapter.addFragment(new fragment_favuaret(),getResources().getString(R.string.favourite));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragment=new ArrayList<>();
        private final List<String> title=new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }

        public void addFragment(Fragment fragment, String title){
            mFragment.add(fragment);
            this.title.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i=new Intent(favuarit.this,MainActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.Catagorie) {
            Intent i=new Intent(favuarit.this,cataListMain.class);
            startActivity(i);
        } else if (id == R.id.businesses) {
            Intent i=new Intent(favuarit.this,orgListMain.class);
            startActivity(i);
        } else if (id == R.id.favorites) {

        } else if (id == R.id.Settings) {
            Intent intent=new Intent(this,settings.class);
            startActivity(intent);
        } else if (id == R.id.Contact_Eshi) {

        }
        else if (id == R.id.cridet) {
            Intent i=new Intent(favuarit.this,credits.class);
            startActivity(i);
            onPause();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
