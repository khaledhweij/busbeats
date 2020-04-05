package com.unundefined.newbusbeats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Delayed;

public class openingActivity extends FragmentActivity {

    private Handler handler = new Handler();



    private int counter=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.opening_layout);
        findViewById(R.id.opening_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.opening_background).setVisibility(View.GONE);
                findViewById(R.id.opening_layout).setBackgroundColor(getResources().getColor(R.color.dark_blue));
                findViewById(R.id.login_layout).setVisibility(View.VISIBLE);

            }
        });
        findViewById(R.id.do_not_have_an_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));

            }
        });


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(((EditText) findViewById(R.id.email_field)).getText().toString(), ((EditText) findViewById(R.id.password_field)).getText().toString());


            }
        });


/*
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    overridePendingTransition(R.anim.swipe_up,R.anim.swipe_up);*/
}

    private void validate(String userName, String password){

        if(userName.equals("admin") && password.equals("00100")){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
	   else {
        counter--;

        if(counter == 0 ){
            Toast.makeText(openingActivity.this,"no ",Toast.LENGTH_SHORT).show();

            findViewById(R.id.sign_in_button).setEnabled(false);

            handler.postDelayed(runnable,50000);
        }

            findViewById(R.id.sign_in_button).setEnabled(true);




    }
    }

        private Runnable runnable = new Runnable(){
            @Override
            public void run(){






            }

        };
}

