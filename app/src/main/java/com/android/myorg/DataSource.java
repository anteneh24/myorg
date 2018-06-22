package com.android.myorg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anteneh on 5/25/2016.
 */
public class DataSource {

    static SQLiteOpenHelper dbhelper;
    static SQLiteDatabase database;

    private static final String[] orgColumns={
            DatabaseOpenHelper.org_id,DatabaseOpenHelper.org_name,
            DatabaseOpenHelper.tell,DatabaseOpenHelper.fax,
            DatabaseOpenHelper.po_box,DatabaseOpenHelper.email,
            DatabaseOpenHelper.street_name,DatabaseOpenHelper.address,
            DatabaseOpenHelper.city, DatabaseOpenHelper.country,
            DatabaseOpenHelper.phone, DatabaseOpenHelper.logo,
            DatabaseOpenHelper.sponser, DatabaseOpenHelper.cata_id,
            DatabaseOpenHelper.state, DatabaseOpenHelper.latitude,
            DatabaseOpenHelper.longitude,DatabaseOpenHelper.favourite
    };
    private static final String[] braColumns={DatabaseOpenHelper.bra_id,DatabaseOpenHelper.bra_name,DatabaseOpenHelper.tell,DatabaseOpenHelper.fax,
            DatabaseOpenHelper.po_box,DatabaseOpenHelper.email,DatabaseOpenHelper.street_name,DatabaseOpenHelper.address,DatabaseOpenHelper.city,
            DatabaseOpenHelper.country,DatabaseOpenHelper.logo,DatabaseOpenHelper.phone,DatabaseOpenHelper.sponser,DatabaseOpenHelper.org_id,
            DatabaseOpenHelper.state,DatabaseOpenHelper.latitude,DatabaseOpenHelper.longitude,DatabaseOpenHelper.favourite
    };
    private static final String[] cataColumns={DatabaseOpenHelper.cata_id,DatabaseOpenHelper.cata_name,DatabaseOpenHelper.logo};

    public DataSource(Context context){
          dbhelper=new DatabaseOpenHelper(context);
    }

    public void open(){
        database=dbhelper.getWritableDatabase();
        Log.i("BBb","opend");
    }
    public void close(){
        dbhelper.close();
        Log.i("BBb","closed");
    }

    public void create_cata(cata cata){
        ContentValues cv=new ContentValues();
        cv.put(DatabaseOpenHelper.cata_id,cata.getCata_id());
        cv.put(DatabaseOpenHelper.cata_name,cata.getCata_name());
        cv.put(DatabaseOpenHelper.logo,cata.getLogo());
        database.insert(DatabaseOpenHelper.cata_table,null,cv);
    }

    public void create_org(org org){
        ContentValues cv=new ContentValues();
        cv.put(DatabaseOpenHelper.org_id,org.getOrg_id());
        cv.put(DatabaseOpenHelper.org_name,org.getOrg_name());
        cv.put(DatabaseOpenHelper.tell,org.getTell());
        cv.put(DatabaseOpenHelper.fax,org.getFax());
        cv.put(DatabaseOpenHelper.po_box,org.getPo_box());
        cv.put(DatabaseOpenHelper.email,org.getEmail());
        cv.put(DatabaseOpenHelper.street_name,org.getStreet_name());
        cv.put(DatabaseOpenHelper.address,org.getAddress());
        cv.put(DatabaseOpenHelper.city,org.getCity());
        cv.put(DatabaseOpenHelper.country,org.getCountry());
        cv.put(DatabaseOpenHelper.phone,org.getPhone());
        cv.put(DatabaseOpenHelper.logo,org.getLogo());
        cv.put(DatabaseOpenHelper.sponser,org.getSponser());
        cv.put(DatabaseOpenHelper.cata_id,org.getCata_id());
        cv.put(DatabaseOpenHelper.state,org.getState());
        cv.put(DatabaseOpenHelper.latitude,org.getLatitude());
        cv.put(DatabaseOpenHelper.longitude,org.getLongitude());
        cv.put(DatabaseOpenHelper.favourite,"false");
        database.insert(DatabaseOpenHelper.org_table,null,cv);
    }
    public void create_bra(bra org){
        ContentValues cv=new ContentValues();
        cv.put(DatabaseOpenHelper.bra_id,org.getBra_id());
        cv.put(DatabaseOpenHelper.bra_name,org.getBra_name());
        cv.put(DatabaseOpenHelper.tell,org.getTell());
        cv.put(DatabaseOpenHelper.fax,org.getFax());
        cv.put(DatabaseOpenHelper.po_box,org.getPo_box());
        cv.put(DatabaseOpenHelper.email,org.getEmail());
        cv.put(DatabaseOpenHelper.street_name,org.getStreet_name());
        cv.put(DatabaseOpenHelper.address,org.getAddress());
        cv.put(DatabaseOpenHelper.city,org.getCity());
        cv.put(DatabaseOpenHelper.country,org.getCountry());
        cv.put(DatabaseOpenHelper.logo,org.getLogo());
        cv.put(DatabaseOpenHelper.phone,org.getMobile());
        Log.i("bbb"+org.getBra_name(),org.getPhone());
        cv.put(DatabaseOpenHelper.sponser,org.getSponser());
        cv.put(DatabaseOpenHelper.org_id,org.getorg_id());
        cv.put(DatabaseOpenHelper.state,org.getState());
        cv.put(DatabaseOpenHelper.latitude,org.getLatitude());
        cv.put(DatabaseOpenHelper.longitude,org.getLongitude());
        cv.put(DatabaseOpenHelper.favourite,"false");
        database.insert(DatabaseOpenHelper.bra_table,null,cv);
    }


    public List<cata> findFilteredCata(String selection,String[] selectionargs,String groupby,String havingby,String orderby){
        List<cata> data=new ArrayList<cata>();
        Cursor cv=database.query(DatabaseOpenHelper.cata_table,cataColumns,selection,selectionargs,groupby,havingby,orderby);

        if(cv.getCount()>0){
            while (cv.moveToNext()){
            cata org=new cata();
            org.setCata_id(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.cata_id)));
            org.setCata_name(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.cata_name)));
            org.setLogo(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.logo)));
            data.add(org);
            }
        }

        return data;
    }
    public String findFilteredorgfav(String id){
        String data="";
        Cursor cv=database.query(DatabaseOpenHelper.org_table,orgColumns,DatabaseOpenHelper.org_id+" = '"+id+"'",null,null,null,null);
        if (cv.getCount()>0){
            while (cv.moveToNext()){
                data=cv.getString(cv.getColumnIndex(DatabaseOpenHelper.favourite));

            }
        }

        return data;
    }
    public String findFilteredbrafav(String id){
        String data="";
        Cursor cv=database.query(DatabaseOpenHelper.bra_table,braColumns,DatabaseOpenHelper.bra_id+" = '"+id+"'",null,null,null,null);

        if (cv.getCount()>0){
            while (cv.moveToNext()){
                data=cv.getString(cv.getColumnIndex(DatabaseOpenHelper.favourite));

            }
        }

        return data;
    }
    public List<org> findFilteredOrg(String selection,String[] selectionargs,String groupby,String havingby,String orderby){
        List<org> data=new ArrayList<org>();
        Cursor cv=database.query(DatabaseOpenHelper.org_table,orgColumns,selection,selectionargs,groupby,havingby,orderby);

        if(cv.getCount()>0){
            while (cv.moveToNext()) {
                org org = new org();
                org.setOrg_id(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.org_id)));
                org.setOrg_name(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.org_name)));
                Log.i("position",org.getOrg_name());
                org.setTell(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.tell)));
                org.setFax(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.fax)));
                org.setPo_box(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.po_box)));
                org.setEmail(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.email)));
                org.setStreet_name(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.street_name)));
                org.setAddress(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.address)));
                org.setCity(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.city)));
                org.setCountry(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.country)));
                org.setLogo(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.logo)));
                org.setSponser(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.sponser)));
                org.setPhone(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.phone)));
                org.setCata_id(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.cata_id)));
                org.setState(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.state)));
                org.setLatitude(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.latitude)));
                org.setLongitude(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.longitude)));
                org.setFavourite(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.favourite)));
                data.add(org);
            }
        }


        return data;
    }
    public void updateOrgFav(String org_id,String fav){

        ContentValues cv=new ContentValues();
        //cv.put(DatabaseOpenHelper.org_id,org_id);
        cv.put(DatabaseOpenHelper.favourite,fav);
        database.update(DatabaseOpenHelper.org_table,cv,DatabaseOpenHelper.org_id+" = "+org_id,null);
        //String upd="UPDATE "+DataSource.orgColumns+" SET "+DatabaseOpenHelper.favourite+" = "+fav+" WHERE "+DatabaseOpenHelper.org_id+" = "+org_id;
    }

    public void updateBraFav(String org_id,String fav){

        ContentValues cv=new ContentValues();
        cv.put(DatabaseOpenHelper.favourite,fav);
        database.update(DatabaseOpenHelper.bra_table,cv,DatabaseOpenHelper.bra_id+" = "+org_id,null);
        Log.i("bbb",fav);
    }
    public int updatecata(String cata_id,String cata_name){
        ContentValues cv=new ContentValues();
        cv.put(DatabaseOpenHelper.cata_id,cata_id);
        cv.put(DatabaseOpenHelper.cata_name,cata_name);

        return database.update(DatabaseOpenHelper.cata_table,cv,DatabaseOpenHelper.cata_id+" = "+cata_id,null);
    }
    public int updateOrg(String org_id,String state     ){

        ContentValues cv=new ContentValues();

        cv.put(DatabaseOpenHelper.state,state);

        return database.update(DatabaseOpenHelper.org_table,cv,DatabaseOpenHelper.org_id+" = "+org_id,null);

    }

    public int updatebra(String bra_id, String bra_name, String tell, String fax
            , String po_box, String email, String street_name,
                         String address, String city, String country,
                         String logo, String org_id, String state,String latitude,String longtiude){

        ContentValues cv=new ContentValues();
        cv.put(DatabaseOpenHelper.bra_name,bra_name);
        cv.put(DatabaseOpenHelper.tell,tell);
        cv.put(DatabaseOpenHelper.fax,fax);
        cv.put(DatabaseOpenHelper.po_box,po_box);
        cv.put(DatabaseOpenHelper.email,email);
        cv.put(DatabaseOpenHelper.street_name,street_name);
        cv.put(DatabaseOpenHelper.address,address);
        cv.put(DatabaseOpenHelper.city,city);
        cv.put(DatabaseOpenHelper.country,country);
        cv.put(DatabaseOpenHelper.logo,logo);
        cv.put(DatabaseOpenHelper.org_id,org_id);
        cv.put(DatabaseOpenHelper.state,state);
        cv.put(DatabaseOpenHelper.latitude,latitude);
        cv.put(DatabaseOpenHelper.longitude,longtiude);
        return database.update(DatabaseOpenHelper.bra_table,cv,DatabaseOpenHelper.bra_id+" = "+bra_id,null);
    }
    public void updateorgFavourite(String org_id,String favourite){
        ContentValues cv=new ContentValues();
        cv.put(DatabaseOpenHelper.favourite,favourite);
        database.update(DatabaseOpenHelper.org_table,cv,DatabaseOpenHelper.org_id+" = "+org_id,null);
    }

    public void updatebraFavourite(String org_id,String favourite){
        ContentValues cv=new ContentValues();
        cv.put(DatabaseOpenHelper.favourite,favourite);
        database.update(DatabaseOpenHelper.bra_table,cv,DatabaseOpenHelper.bra_id+" = "+org_id,null);
        Log.i("bbb","update to true");
    }
    public String findlogoforbra(String id){
        List<bra> data=new ArrayList<bra>();
        Cursor cv=database.query(DatabaseOpenHelper.org_table,orgColumns,null,null,null,null,null);
        String logo="";
        if (cv.getCount()>0){
            while (cv.moveToNext()){
                logo=cv.getString(cv.getColumnIndex(DatabaseOpenHelper.logo));
            }
        }
return logo;
    }

    public List<bra> findFilteredBra(String selection,String[] selectionargs,String groupby,String havingby,String orderby){
        List<bra> data=new ArrayList<bra>();
        Cursor cv=database.query(DatabaseOpenHelper.bra_table,braColumns,selection,selectionargs,groupby,havingby,orderby);

        if(cv.getCount()>0){
            while (cv.moveToNext()) {
                bra org = new bra();
                org.setBra_id(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.bra_id)));
                org.setBra_name(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.bra_name)));
                org.setTell(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.tell)));
                org.setFax(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.fax)));
                org.setPo_box(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.po_box)));
                org.setEmail(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.email)));
                org.setStreet_name(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.street_name)));
                org.setAddress(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.address)));
                org.setCity(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.city)));
                org.setCountry(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.country)));
                org.setSponser(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.sponser)));
                org.setorg_id(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.org_id)));
                org.setState(cv.getInt(cv.getColumnIndex(DatabaseOpenHelper.state)));
                org.setLatitude(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.latitude)));
                org.setLongitude(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.longitude)));
                org.setPhone(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.phone)));
                org.setFavourite(cv.getString(cv.getColumnIndex(DatabaseOpenHelper.favourite)));
                data.add(org);
            }
        }


        return data;
    }
}
