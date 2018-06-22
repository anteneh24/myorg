package com.android.myorg;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ScrollingActivity extends AppCompatActivity {
    private Dialog dialog;
    private String phone;
    private String email;
    DataSource dataSource;
    private String id;
    boolean branch=false;
    Intent i;
    ImageView imageView;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_main);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();


        ImageLoader.getInstance().init(config);
        dataSource = new DataSource(this);
        dataSource.open();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        i = getIntent();
        String latitude=i.getStringExtra(DatabaseOpenHelper.latitude);
        String longtiude=i.getStringExtra(DatabaseOpenHelper.longitude);
        final String location=latitude+""+longtiude;
        Log.i("bbbb location",location);
        ImageView imageView = (ImageView) findViewById(R.id.imageview_detail);

        ImageLoader.getInstance().displayImage(i.getStringExtra(DatabaseOpenHelper.logo), imageView);
        try{


        id = i.getStringExtra(DatabaseOpenHelper.org_id);
        Log.i("bbb id scr", i.getStringExtra(DatabaseOpenHelper.org_id) + "");
        if (id == null) {
            id = i.getStringExtra(DatabaseOpenHelper.bra_id);
            Log.i("bbb", i.getStringExtra(DatabaseOpenHelper.bra_id));
            branch = true;
        }

        phone = i.getStringExtra(DatabaseOpenHelper.phone);
       fab = (FloatingActionButton) findViewById(R.id.fab);

        TextView bra_name = (TextView) findViewById(R.id.branch_name);
        if (i.getStringExtra(DatabaseOpenHelper.org_name) != null){
            bra_name.setText(i.getStringExtra(DatabaseOpenHelper.org_name));
            setTitle(i.getStringExtra(DatabaseOpenHelper.org_name));}
        if (i.getStringExtra(DatabaseOpenHelper.bra_name) != null){
            bra_name.setText(i.getStringExtra(DatabaseOpenHelper.bra_name));
            setTitle(i.getStringExtra(DatabaseOpenHelper.bra_name));}
        TextView fax = (TextView) findViewById(R.id.fax);
        fax.setText(fax.getText() + i.getStringExtra(DatabaseOpenHelper.fax) + "\n" + " Post_Box: " + i.getStringExtra(DatabaseOpenHelper.po_box) + ", " + i.getStringExtra(DatabaseOpenHelper.address) + "," +
                i.getStringExtra(DatabaseOpenHelper.street_name) + ", " + i.getStringExtra(DatabaseOpenHelper.city));
        TextView phone_view = (TextView) findViewById(R.id.phone);

        phone_view.setText(phone_view.getText() + i.getStringExtra(DatabaseOpenHelper.phone) + "");
//        Log.i("bbb",phone);
        email = i.getStringExtra(DatabaseOpenHelper.email);
        TextView email_view = (TextView) findViewById(R.id.email);
        email_view.setText(email_view.getText() + i.getStringExtra(DatabaseOpenHelper.email));
        Log.i("bbb", email);
        }catch (NullPointerException e){}
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location.equals("----")||location.equals("null"))
                    dialog=customDialogWithOutMap();
                else
                dialog=CustomDialog();
                final ImageView imageView=(ImageView)dialog.findViewById(R.id.dialog_fav);
                if(!branch) {
                    if (dataSource.findFilteredorgfav(i.getStringExtra(DatabaseOpenHelper.org_id)).equals("true")) {

                        imageView.setImageResource(R.drawable.cross_icon_select);

                    }
                    else
                        imageView.setImageResource(R.drawable.cross_icon);
                }
                else{
                    if (dataSource.findFilteredbrafav(i.getStringExtra(DatabaseOpenHelper.bra_id)).equals("true"))
                        imageView.setImageResource(R.drawable.cross_icon_select);
                    else
                        imageView.setImageResource(R.drawable.cross_icon);
                }
            }
        });
    }
    public void message(View v){


        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"+phone)));

    }
    public void email(View v){

        Intent emailIntent=new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"+email));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,i.getStringExtra(DatabaseOpenHelper.email));
        Log.i("bbb email",email);
        try{
            startActivity(Intent.createChooser(emailIntent,"Send email"));
        }catch (android.content.ActivityNotFoundException e){}
    }
    public Dialog CustomDialog(){
        dialog=new Dialog(ScrollingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(4));
        Window window=dialog.getWindow();

        WindowManager.LayoutParams param=window.getAttributes();
        param.gravity= Gravity.RIGHT;
        dialog.setCanceledOnTouchOutside(true);

        View demo=(View)dialog.findViewById(R.id.demo);


        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
    public Dialog customDialogWithOutMap(){
        dialog=new Dialog(ScrollingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox_with_outmap);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(4));
        Window window=dialog.getWindow();

        WindowManager.LayoutParams param=window.getAttributes();
        param.gravity= Gravity.RIGHT;
        dialog.setCanceledOnTouchOutside(true);

        View demo=(View)dialog.findViewById(R.id.demo);


        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
    public void visual(View v){

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
    public void call(View view){
        Intent phone_call=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
       try{
           startActivity(phone_call);
       }catch (android.content.ActivityNotFoundException e){}

    }
   public void favourite(View v){
            if (branch==true) {

                if(dataSource.findFilteredbrafav(i.getStringExtra(DatabaseOpenHelper.bra_id + "")).equals("false")) {
                    dataSource.updateBraFav(i.getStringExtra(DatabaseOpenHelper.bra_id + ""), "true");

                }
                else
                    dataSource.updateBraFav(i.getStringExtra(DatabaseOpenHelper.bra_id + ""), "false");
            }
       else {if(dataSource.findFilteredorgfav(i.getStringExtra(DatabaseOpenHelper.org_id + "")).equals("false"))
                dataSource.updateOrgFav(id, "true");
                else
                dataSource.updateOrgFav(i.getStringExtra(DatabaseOpenHelper.org_id + ""), "false");

            }
       imageView=(ImageView)v.findViewById(R.id.dialog_fav);
       if(!branch) {
           if (dataSource.findFilteredorgfav(i.getStringExtra(DatabaseOpenHelper.org_id)).equals("true")) {

               imageView.setImageResource(R.drawable.cross_icon_select);

           }
           else
               imageView.setImageResource(R.drawable.cross_icon);
       }
       else{
           if (dataSource.findFilteredbrafav(i.getStringExtra(DatabaseOpenHelper.bra_id)).equals("true"))
               imageView.setImageResource(R.drawable.cross_icon_select);
           else
               imageView.setImageResource(R.drawable.cross_icon);
       }



    }

    public double changeToDouble(String value){
        double number=0.0;
        number=Double.parseDouble(value);
     return number;
    }

    public void map(View view) {
       double latitude=changeToDouble(i.getStringExtra(DatabaseOpenHelper.latitude));
        double longtiude=changeToDouble(i.getStringExtra(DatabaseOpenHelper.longitude));
        Log.i("bbbb",latitude+" "+longtiude);
        Intent i=new Intent(this, MapsActivity.class);
        i.putExtra("lat",latitude);
        i.putExtra("lng",longtiude);
        if (latitude!=0.0&&longtiude!=0.0)
        startActivity(i);
    }
}
