package com.unundefined.newbusbeats;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

public class openingActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.opening_layout);
        findViewById(R.id.opening_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.opening_background).setVisibility(View.GONE);
                findViewById(R.id.opening_layout).setBackgroundResource(R.drawable.drawable_purple_gradient);
                findViewById(R.id.login_layout).setVisibility(View.VISIBLE);

            }
        });
        findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));

            }
        });

/*
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    overridePendingTransition(R.anim.swipe_up,R.anim.swipe_up);*/
}
}