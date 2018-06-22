package com.android.myorg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by anteneh on 5/25/2016.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper{

    static final String DATABASE_NAME="eshi";
    static final int DATABASE_VERSION=3;

    public static String cata_table="categorie";

    public static String cata_id="cata_id";
    public static String cata_name="cata_name";


    public static String org_table="organization";

    public static String org_name="org_name";
    public static String tell="tell";
    public static String fax="fax";
    public static String city="city";
    public static String country="country";
    public static String email="email";
    public static String sponser="sponser";
    public static String po_box="po_box";
    public static String street_name="street_name";
    public static String address="address";
    public static String logo="logo";
    public static String state="state";
    public static String org_id="org_id";
    public static String favourite="favourite";
    public static String bra_table="branch";
    public static String phone="phone";
    public static String bra_id="bra_id";
    public static String bra_name="bra_name";

    public static String latitude="latitude";
    public static String longitude="longitude";

     String cata="CREATE TABLE IF NOT EXISTS "+cata_table+"(" +
            "" +cata_id+" INTEGER PRIMARY KEY ,"+
            "" +cata_name+" TEXT,"+
             ""+logo+" INTEGER"+
            ")";


    String org="CREATE TABLE IF NOT EXISTS "+org_table+"(" +
            "" +org_id    +" INTEGER PRIMARY KEY ,"+
            "" +org_name  +" TEXT,"+
            "" +tell      +" TEXT,"+
            "" +fax       +" TEXT,"+
            "" +po_box    +" TEXT,"+
            "" +email     +" TEXT,"+
            "" +street_name+" TEXT,"+
            "" +address    +" TEXT,"+
            "" +city       +" TEXT,"+
            "" +country    +" TEXT," +
            "" +phone    +" TEXT," +
            "" +logo       +" INTEGER,"+
            ""+ sponser    +" INTEGER," +
            ""+ cata_id    +" INTEGER,"+
            ""+ state      +" INTEGER,"+
            ""+ latitude      +" TEXT,"+
            ""+ longitude      +" TEXT,"+
            ""+favourite+" TEXT DEFAULT false"+
            ")";


    String bra="CREATE TABLE IF NOT EXISTS "+bra_table+"(" +
            "" +bra_id+" INTEGER PRIMARY KEY ,"+
            "" +bra_name+" TEXT,"+
            "" +tell+" TEXT,"+
            "" +fax+" TEXT,"+
            "" +po_box+" TEXT,"+
            "" +email+" TEXT,"+
            "" +street_name+" TEXT,"+
            "" +address+" TEXT,"+
            "" +city+" TEXT,"+
            "" +country+" TEXT," +
            "" +logo+" INTEGER,"+
            "" +phone    +" TEXT," +
            ""+sponser+" INTEGER," +
            ""+org_id+" INTEGER,"+
            ""+state+" INTEGER,"+
            ""+ latitude      +" TEXT,"+
            ""+ longitude      +" TEXT,"+
            ""+favourite+" TEXT DEFAULT false"+
            ")";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          db.execSQL(cata);
        Log.i("BBB","Cata");
        db.execSQL(org);
        Log.i("BBB","org");
        db.execSQL(bra);
        Log.i("BBB","bra");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+cata_table);
        db.execSQL("DROP TABLE IF EXISTS "+org_table);
        db.execSQL("DROP TABLE IF EXISTS "+bra_table);
        onCreate(db);
    }
}
