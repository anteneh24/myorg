package com.android.myorg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

/**
 * Created by anteneh on 5/20/2016.
 */
public class starter extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starter);
        final ProgressBar bar=(ProgressBar)findViewById(R.id.progressBar1);
        bar.setBackgroundColor(2);

        bar.setMax(1000);
        Thread timer=new Thread(){
            public void run(){
                try{
                    int i=0;

                    for(int ia=0;ia<1000;ia++){
                        sleep(2);
                        bar.setProgress(ia);
                    }
                }catch(InterruptedException e){

                }finally {
                    Intent intent=new Intent(starter.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
