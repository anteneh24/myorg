package com.android.myorg;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anteneh on 5/27/2016.
 */
public class orgaListMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orga);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent i=getIntent();
        setTitle(i.getStringExtra(DatabaseOpenHelper.cata_name));
        int cata_id=i.getIntExtra(DatabaseOpenHelper.cata_id,0);
        Log.i("position",cata_id+"");
        dataSource=new DataSource(this);
        dataSource.open();
        List<org> list=dataSource.findFilteredOrg(DatabaseOpenHelper.cata_id+" = "+cata_id,null,null,null,DatabaseOpenHelper.org_name);
        if(list.size()==0) {
            org orga=new org();
            orga.setOrg_name("No result");
            //orga.setLogo(R.drawable.noting);
            list.add(orga);

        }
        final List<org> list1=new ArrayList<org>();
        for(int a=0;a<list.size();a++){
            list1.add(list.get(a));
        }
        Log.i("bbb","123"+list1.size());
        Log.i("bbb","23"+list.size());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        orgRecycleAdapter adapter = new orgRecycleAdapter(this, list);
        recyclerView.setAdapter(adapter);
        orgRecycleAdapter.OnItemClickListener onItemClickListener=new orgRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i=new Intent(orgaListMain.this,ScrollingActivity.class);
                i.putExtra(DatabaseOpenHelper.org_id,list1.get(position).getOrg_id()+"");
                i.putExtra(DatabaseOpenHelper.org_name,list1.get(position).getOrg_name());
                i.putExtra(DatabaseOpenHelper.tell,list1.get(position).getTell());
                i.putExtra(DatabaseOpenHelper.fax,list1.get(position).getFax());
                i.putExtra(DatabaseOpenHelper.po_box,list1.get(position).getPo_box());
                i.putExtra(DatabaseOpenHelper.email,list1.get(position).getEmail());
                i.putExtra(DatabaseOpenHelper.street_name,list1.get(position).getStreet_name());
                i.putExtra(DatabaseOpenHelper.address,list1.get(position).getAddress());
                i.putExtra(DatabaseOpenHelper.phone,list1.get(position).getPhone());
                i.putExtra(DatabaseOpenHelper.city,list1.get(position).getCity());
                i.putExtra(DatabaseOpenHelper.country,list1.get(position).getCountry());
                i.putExtra(DatabaseOpenHelper.logo,list1.get(position).getLogo());
                i.putExtra(DatabaseOpenHelper.latitude,list1.get(position).getLatitude());
                i.putExtra(DatabaseOpenHelper.longitude,list1.get(position).getLongitude());
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);

            }
        };
        adapter.setOnItemClickListener(onItemClickListener);
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
            // Handle the camera action
            Intent i=new Intent(orgaListMain.this,MainActivity.class);
            startActivity(i);
        } else if (id == R.id.Catagorie) {
            Intent i=new Intent(orgaListMain.this,cataListMain.class);
            startActivity(i);
        } else if (id == R.id.businesses) {
            Intent i=new Intent(orgaListMain.this,orgListMain.class);
            startActivity(i);
        } else if (id == R.id.favorites) {
            Intent i=new Intent(orgaListMain.this,favuarit.class);
            startActivity(i);
        } else if (id == R.id.Settings) {

        } else if (id == R.id.Contact_Eshi) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
        finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i=new Intent(orgaListMain.this,search.class);
            startActivity(i);
            onPause();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

