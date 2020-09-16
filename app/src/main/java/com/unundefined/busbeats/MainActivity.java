package com.unundefined.busbeats;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

     Button start_button;
//    private GoogleMap mMap;
//
//    private LatLng location;
//    private LatLng destinationLocation;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context=getApplicationContext();
        final ImageButton b = findViewById(R.id.set_location_button);
        final Button s =  findViewById(R.id.start_button);
        start_button = findViewById(R.id.start_button);
        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));


            }
        });
        // if destination selected then
        button_animation(context,b,s);


        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_button_clicked();
            }
        });

    }


    private void start_button_clicked(){

        start_button.setVisibility(View.GONE);
        findViewById(R.id.choosing_layout).setVisibility(View.VISIBLE);

        final Animation animation = new TranslateAnimation(0, 0, 700, 0);
        animation.setDuration(1000);
//for button stops in the new position.
        animation.setFillAfter(true);
        findViewById(R.id.choosing_layout).startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void button_animation(final Context context, final ImageButton b, final Button s){

        final Animation animation = new TranslateAnimation(0, (float) (-1*this.getResources().getDisplayMetrics().widthPixels / 2) + 150, 0, 0);
        animation.setDuration(1000);
//for button stops in the new position.
        animation.setFillAfter(true);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                b.setVisibility(View.GONE);
                s.setVisibility(View.VISIBLE);
                b.clearAnimation();//This Line Added

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // set Animation for 5 sec


        b.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    b.setColorFilter(Color.argb(255, 255, 255, 255));
                    b.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dark_blue,context.getTheme())));
                    b.startAnimation(animation);



                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    b.setColorFilter(Color.argb(0, 0, 0, 0));
                    b.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white,context.getTheme())));

                }

                return true;
            }


        });

    }

    private void setUpSearch() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.search_view);
        // Assumes current activity is the searchable activity
        if (searchManager == null) {
            searchView.setVisibility(View.GONE);
            Log.w(TAG, "setUpSearch: Search view could not be set up");
            Toast.makeText(getApplicationContext(), "Could not load search bar",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
    }

}