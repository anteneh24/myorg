package com.android.myorg;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsSatellite;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private Marker mCurrLocationMarker,destantionMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION=99;
    private ArrayList<LatLng> MarkerPoints;
    private double latitiude=0,longtiude=0;
    private ProgressDialog progressDialog;
    LatLng dest;
    TextView distance_duration;
    ConnectivityManager cm ;
    NetworkInfo activeNetwork ;
    String answer="";
    boolean terran=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cm= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork= cm.getActiveNetworkInfo();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                answer="You are connected to a WiFi Network";
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                answer="You are connected to a Mobile Network";
        }
        else {
            // answer="no internet connectivity";

            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(MapsActivity.this);
            myAlertDialog.setTitle("Connection Alert-NO CONNECTION");
            myAlertDialog.setMessage("Go To Mobile Data Settings  "  );
            myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);

                    startActivity(intent);
                    // do something when the OK button is clicked
                }});
            myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // answer = "you are  not connected to a network";
                    // do something when the Cancel button is clicked
                }});
            myAlertDialog.show();


        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();

        Intent i=getIntent();
        latitiude=i.getDoubleExtra("lat",0.0);
        longtiude=i.getDoubleExtra("lng",0);
        Log.i("destnation",latitiude+","+longtiude);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            checkLocationPermisssion();

        MarkerPoints=new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        dest=new LatLng(latitiude,longtiude);
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(dest);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        // Add a marker in Sydney and move the camera
        /* sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
       /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (MarkerPoints.size()>1){
                    MarkerPoints.clear();
                    mMap.clear();
                }

                MarkerPoints.add(point);
                MarkerOptions options=new MarkerOptions();
                options.position(point);

                if (MarkerPoints.size()==1){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                else if(MarkerPoints.size()==2){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                mMap.addMarker(options);

                if (MarkerPoints.size()>=2){
                    LatLng origin=MarkerPoints.get(0);
                    LatLng dest=MarkerPoints.get(1);

                    String url=getUrl(origin,dest);
                    Log.d("onMapClick",url.toString());
                    FetchUrl fetchUrl=new FetchUrl();
                    fetchUrl.execute(url);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                }

            }
        });*/

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }else{
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    private String getUrl(LatLng origin,LatLng dest){
        String str_orgin="origin="+origin.latitude+","+origin.longitude;
        String str_dest="destination="+dest.latitude+","+dest.longitude;

        String sensor="sensor=false&mode=driving&alternatives=true";
        String parameters=str_orgin+"&"+str_dest+"&"+sensor;

        String output="json";
        String url="https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }

    public void change(View view) {
        FloatingActionButton f=(FloatingActionButton)view.findViewById(R.id.imagefab);
        if (terran){
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            terran=false;
            f.setImageResource(R.drawable.ic_action_satellite);
        }else {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            terran=true;
            f.setImageResource(R.drawable.ic_action_terrain);
        }
    }

    private class FetchUrl extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... url) {
            String data="";

            try {
                data=downloadUrl(url[0]);
                Log.d("Background TAsk data",data.toString());
            } catch (IOException e) {
                Log.d("Background Task",e.toString());
            }
            return data;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Fetching route, Please wait...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.hide();
            ParserTask parserTask=new ParserTask();
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data="";
        InputStream istream=null;
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            istream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(istream));

            StringBuffer sb=new StringBuffer();
            String line="";
            while ((line=br.readLine())!=null){
                sb.append(line);
            }
            data=sb.toString();
            Log.d("downloadUrl",data.toString());
            br.close();
        }catch (Exception e){
            Log.d("Exception",e.toString());
        }finally {
            istream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>>{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes=null;

            try{
                jObject=new JSONObject(jsonData[0]);
                Log.d("Parser Task",jsonData[0].toString());
                DataParser parser=new DataParser();
                Log.d("Parser Task",parser.toString());

                routes=parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());
            }catch (Exception e){
                Log.d("Parser Task",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            super.onPostExecute(result);
            ArrayList<LatLng> points;
            PolylineOptions lineOptions=null;
            String distance="",duration="";

            for (int i=0;i<result.size();i++){
                points=new ArrayList<>();
                lineOptions=new PolylineOptions();
                List<HashMap<String, String>> path=result.get(i);

                for (int j=0;j<path.size();j++){
                    HashMap<String,String> point=path.get(j);

                  /*  if (j==0){
                        distance=(String)point.get("distances");
                        continue;
                    }else if(j==1){
                        duration=(String)point.get("duration");
                        continue;
                    }*/
                    double lat=Double.parseDouble(point.get("lat"));
                    double lng=Double.parseDouble(point.get("lng"));

                    LatLng position=new LatLng(lat,lng);
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.parseColor("#05b1fb"));
                lineOptions.geodesic(true);
                Log.d("onPostExecute","onPostExecute lineoption");

            }
            if (distance!=null&duration!=null)
//                distance_duration.setText("Distance: "+distance+"  Duration: "+duration);

                if (lineOptions!=null){
                    mMap.addPolyline(lineOptions);
                }
                else {
                    Log.d("onPostExecute","without Polylines drawn");
                }
        }
    }

    public class DataParser{

        public List<List<HashMap<String, String>>> parse(JSONObject jObject){
            List<List<HashMap<String, String>>> routes=new ArrayList<>();
            JSONArray jRoutes;
            JSONArray jLegs;
            JSONArray jSteps;
            // JSONObject jDuration;
            //JSONObject jDistance;

            try{
                jRoutes=jObject.getJSONArray("routes");
                for (int i=0;i<jRoutes.length();i++){
                    jLegs=((JSONObject)jRoutes.get(i)).getJSONArray("legs");
                    List path=new ArrayList<>();

                    for (int j=0;j<jLegs.length();j++){
                        /*jDistance=((JSONObject)jLegs.get(j)).getJSONObject("distance");
                        HashMap<String,String> hmDistance=new HashMap<>();
                        hmDistance.put("distance",jDistance.getString("text"));
                        path.add(jDistance);

                        jDuration=((JSONObject)jLegs.get(j)).getJSONObject("duration");
                        HashMap<String,String> hmDuration=new HashMap<>();
                        hmDistance.put("duration",jDuration.getString("text"));
                        path.add(jDuration);*/

                        jSteps=((JSONObject)jLegs.get(j)).getJSONArray("steps");

                        for (int k=0;k<jSteps.length();k++){
                            String polyline="";
                            polyline=(String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                            List<LatLng> list=decodePoly(polyline);

                            for (int l=0;l<list.size();l++){
                                HashMap<String,String> hm=new HashMap<>();
                                hm.put("lat",Double.toString((list.get(l)).latitude));
                                hm.put("lng",Double.toString((list.get(l)).longitude));
                                path.add(hm);
                            }
                        }
                        routes.add(path);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        private List<LatLng> decodePoly(String encoded){
            List<LatLng> poly=new ArrayList<>();
            int index=0,len=encoded.length();
            int lat=0,lng=0;

            while (index<len){
                int b,shift=0,result=0;
                do {
                    b=encoded.charAt(index++)-63;
                    result |=(b&0x1f)<<shift;
                    shift+=5;
                }while (b>=0x20);
                int dlat=((result &1)!= 0 ? ~(result>>1):(result >> 1));

                lat+=dlat;

                shift=0;
                result=0;

                do {
                    b=encoded.charAt(index++)-63;
                    result |=(b&0x1f)<<shift;
                    shift+=5;
                }while (b>=0x20);
                int dlng=((result&1)!=0 ? ~(result>>1):(result >> 1));
                lng+=dlng;

                LatLng p=new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
                poly.add(p);
            }
            return poly;
        }
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation=location;
        if (null == activeNetwork) {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(MapsActivity.this);
            myAlertDialog.setTitle("Connection Alert-NO CONNECTION");
            myAlertDialog.setMessage("Go To Mobile Data Settings  "  );
            myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    onPause();
                    // do something when the OK button is clicked
                }});
            myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // answer = "you are  not connected to a network";
                    // do something when the Cancel button is clicked
                }});
            myAlertDialog.show();
        }
        else {

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng origin = latLng;


            Log.i("destnation", dest.latitude + "," + dest.longitude);
            Log.i("onMapClick", ": reach");
            String url = getUrl(origin, dest);
            Log.d("onMapClick", url.toString());
            FetchUrl fetchUrl = new FetchUrl();
            fetchUrl.execute(url);
            Log.i("bbb", "finished u must get the path");
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        }

        if (mGoogleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        }


    }


    public boolean checkLocationPermisssion(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);

            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);

            }
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        if (mGoogleApiClient==null)
                            buildGoogleApiClient();

                        mMap.setMyLocationEnabled(true);
                    }
                }else{
                    Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT);
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
