package com.example.android.miwok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        MiwokFragmentpageAdapter miwokFragmentpageAdapter = new MiwokFragmentpageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(miwokFragmentpageAdapter);

        //TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //tabLayout.setupWithViewPager(viewPager);

    }
}
