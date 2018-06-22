package com.android.myorg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by anteneh on 9/22/2016.
 */
public class credits extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        Toolbar b=(Toolbar)findViewById(R.id.toolbara);
        setSupportActionBar(b);
        b.setTitle("Credits ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
    }
}
