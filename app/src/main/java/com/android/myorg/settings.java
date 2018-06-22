package com.android.myorg;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by anteneh on 6/6/2016.
 */
public class settings extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }


}
