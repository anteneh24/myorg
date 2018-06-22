package com.android.myorg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private orgRecycleAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private boolean amh;
    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    List<org> list;
    int updateOrg;
    ProgressBar bar;
    TextView text;
    String org_name;
    String tel;
    String mobile;
    String fax;
    String po_box;
    String email;
    String website;
    String street_name;
    String address;
    String city;
    String country;
    String latitude;
    String longitude;
    String logo;
    int cata_id;
    String visibility;
    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MainActivity.this.amh=settings.getBoolean("amh_values",true);
        setContentView(R.layout.activity_main);

        DisplayImageOptions defaultOptions=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration configa=new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();


        ImageLoader.getInstance().init(configa);
        if(amh){
            Configuration config=getBaseContext().getResources().getConfiguration();
            Locale locale=new Locale("am");
            config.locale=locale;
            getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
            recreate();
        }
        dataSource=new DataSource(this);
        dataSource.open();
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
                MainActivity.this.amh=settings.getBoolean("amh_values",true);

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
        new JSONTaskcata().execute("http://192.168.42.126/file/filecategories.php");
        new JSONTask().execute("http://192.168.42.126/file/fileupload.php");
        new JSONTaskbra().execute("http://192.168.42.126/file/filebranch.php");

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);    }

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
            // Handle the camera action
        } else if (id == R.id.Catagorie) {
            Intent i=new Intent(MainActivity.this,cataListMain.class);
            startActivity(i);
        } else if (id == R.id.businesses) {
            Intent i=new Intent(MainActivity.this,orgListMain.class);
            startActivity(i);
        } else if (id == R.id.favorites) {
           Intent i=new Intent(MainActivity.this,favuarit.class);
            startActivity(i);
        } else if (id == R.id.Settings) {
            Intent intent=new Intent(this,settings.class);
            startActivity(intent);
        } else if (id == R.id.Contact_Eshi) {

        }else if (id == R.id.cridet) {
            Intent i=new Intent(MainActivity.this,credits.class);
            startActivity(i);
            onPause();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            try {
                URL url=new URL(params[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream stream=urlConnection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer=new StringBuffer();
                String line="";
                while((line=reader.readLine())!=null){
                    buffer.append(line);
                }
                String linebuffer= buffer.toString();
                JSONObject parentObject=new JSONObject(linebuffer);

                JSONArray parentorg=parentObject.getJSONArray("organizations");

                Log.i("size",parentorg.length()+"");
                JSONObject finalobject=null;
                for (int i=0;i<parentorg.length();i++){
                    finalobject=parentorg.getJSONObject(i);
                    org org=new org();
                    int org_id=finalobject.getInt("org_id");
                    org.setOrg_id(finalobject.getInt("org_id"));
                    org_name=finalobject.getString("org_name");
                    org.setOrg_name(org_name);
                    tel=finalobject.getString("tel");
                    org.setTell(tel);
                    mobile=finalobject.getString("mobile");
                    org.setMobile(mobile);
                    fax=finalobject.getString("fax");
                    org.setFax(fax);
                    po_box=finalobject.getString("po_box");
                    org.setPo_box(po_box);
                    email=finalobject.getString("email");
                    org.setEmail(email);
                    website=finalobject.getString("website");
                    org.setWebsite(website);
                    street_name=finalobject.getString("street_name");
                    org.setStreet_name(street_name);
                    address=finalobject.getString("address");
                    org.setAddress(address);
                    city=finalobject.getString("city");
                    org.setCity(city);
                    org.setPhone(finalobject.getString("mobile"));
                    country=finalobject.getString("country");
                    org.setCountry(country);
                    latitude=finalobject.getString("latitude");
                    org.setLatitude(latitude);
                    longitude=finalobject.getString("longitude");
                    org.setLongitude(longitude);
                    logo=finalobject.getString("logo");
                    org.setLogo("http://192.168.42.126/file/image/"+logo);
                    ImageLoader.getInstance().loadImageSync(org.getLogo());
                    cata_id=finalobject.getInt("cata_id");
                    org.setCata_id(cata_id);
                    visibility=finalobject.getString("visibility");
                    List<org> check=dataSource.findFilteredOrg(null,null,null,null,null);
                    for(int ch=0;ch<check.size();ch++){
                        if(check.get(ch).getOrg_id()!=org_id)
                            dataSource.create_org(org);
                        if(check.get(ch).getOrg_id()==org_id)
                            updateOrg=dataSource.updateOrg(org_id+"",visibility);
                    }

                    if (dataSource.findFilteredOrg(null,null,null,null,null).size()==0) {
                        dataSource.create_org(org);
                    }

                }

                return finalobject.getString("org_name");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection!=null)
                    urlConnection.disconnect();
                try {
                    if (reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return  null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    public class JSONTaskcata extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            try {
                URL url=new URL(params[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream stream=urlConnection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer=new StringBuffer();
                String line="";
                while((line=reader.readLine())!=null){
                    buffer.append(line);
                }
                String linebuffer= buffer.toString();
                JSONObject parentObject=new JSONObject(linebuffer);
                JSONArray parentcata=parentObject.getJSONArray("categories");

                JSONObject finalobject=null;

                for (int i=0;i<parentcata.length();i++){
                    finalobject=parentcata.getJSONObject(i);
                    cata org=new cata();
                    org.setCata_id(finalobject.getInt("cata_id"));
                    cata_id=finalobject.getInt("cata_id");
                    org_name=finalobject.getString("category_name");
                    org.setCata_name(finalobject.getString("category_name"));
                    logo=finalobject.getString("clogo");
                    org.setLogo("http://192.168.42.126/file/image/"+logo);
                    if(dataSource.findFilteredCata(null,null,null,null,null)!=null) {
                        List<cata> check = dataSource.findFilteredCata(null, null, null, null, null);
                        for (int ch = 0; ch < check.size(); ch++) {
                            if (check.get(ch).getCata_id() != cata_id)
                                dataSource.create_cata(org);
                            if (check.get(ch).getCata_id() == cata_id)
                                updateOrg = dataSource.updateOrg(cata_id + "", visibility);
                        }

                        if (dataSource.findFilteredOrg(null, null, null, null, null).size() == 0) {
                            dataSource.create_cata(org);
                        }
                    }
                    // updateOrg=dataSource.updatecata(cata_id+"",org_name);
                    //if (updateOrg==0)
                    //dataSource.create_cata(org);
                }
                return finalobject.getString("category_name");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection!=null)
                    urlConnection.disconnect();
                try {
                    if (reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return  null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //
        }
    }

    public class JSONTaskbra extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            try {
                URL url=new URL(params[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream stream=urlConnection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer=new StringBuffer();
                String line="";
                while((line=reader.readLine())!=null){
                    buffer.append(line);

                }

                String linebuffer= buffer.toString();
                JSONObject parentObject=new JSONObject(linebuffer);

                JSONArray parentbra=parentObject.getJSONArray("branchs");
                JSONObject finalobject=null;

                for (int i=0;i<parentbra.length();i++){
                    finalobject=parentbra.getJSONObject(i);
                    bra org=new bra();
                    org.setBra_id(finalobject.getInt("branch_id"));
                    int bra_id=finalobject.getInt("branch_id");
                    org_name=finalobject.getString("branch_name");
                    org.setBra_name(org_name);
                    tel=finalobject.getString("tel");
                    org.setTell(tel);
                    mobile=finalobject.getString("mobile");
                    org.setMobile(mobile);
                    fax=finalobject.getString("fax");
                    org.setFax(fax);
                    po_box=finalobject.getString("po_box");
                    org.setPo_box(po_box);
                    email=finalobject.getString("email");
                    org.setEmail(email);
                    //website=finalobject.getString("website");
                    street_name=finalobject.getString("street_name");
                    org.setStreet_name(street_name);
                    address=finalobject.getString("address");
                    org.setAddress(address);
                    city=finalobject.getString("city");
                    org.setCity(city);
                    country=finalobject.getString("country");
                    org.setCountry(country);
                    latitude=finalobject.getString("latitude");
                    org.setLatitude(latitude);
                    longitude=finalobject.getString("longitude");
                    org.setLongitude(longitude);
                    org.setPhone(finalobject.getString("mobile"));
//                    logo=finalobject.getString("logo");
                    //org.setLogo(logo);
                    cata_id=finalobject.getInt("org_id");
                    visibility=finalobject.getString("visibility");
                    org.setVisiblity(visibility);

                    updateOrg=dataSource.updatebra(bra_id+"",org_name,tel,fax,po_box,email,
                            street_name,address,city,country,logo,cata_id+"",visibility,latitude,longitude);
                    if (updateOrg==0)
                        dataSource.create_bra(org);
                }

                return finalobject.getString("branch_name");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection!=null)
                    urlConnection.disconnect();
                try {
                    if (reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return  null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //
        }
    }

}
