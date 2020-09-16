package com.unundefined.busbeats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeneralSettingsActivity extends AppCompatActivity {

    private String selectedLanguage;
    private SharedPreferences sharedPreferences;
    Spinner spinner;
    Locale myLocale;
    String currentLanguage = "English", currentLang;
    String text;
    Boolean darkMode;
    SwitchCompat aSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general3);

        aSwitch = findViewById(R.id.notifications_switch);

        sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);

       String language = sharedPreferences.getString("language", "en");
         toggleLanguage(language);


    }

//    private void  enableNotifications(){
//
//        SharedPreferences sharedPreferences = getSharedPreferences("key", 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("switchValue", aSwitch.isChecked());
//        editor.apply();
//
//        Boolean showNotifications = editor.getBoolean("key",false);
//        if (aSwitch.isChecked() != showNotifications) {
//            editor.putBoolean("key",!showNotifications).apply();
//        }
//    }

    private void darkmode() {
        SharedPreferences settings0 = this.getSharedPreferences("Light", 0);
        boolean lightMode = settings0.getBoolean("key0", true);

        //retrieve selected mode
        if (lightMode) {

            //light mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        } else {

            //dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        SwitchCompat switch0 = findViewById(R.id.dark_mode_switch);
        switch0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (darkMode) {

                    text = "Mode: light";

                    //light mode
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                    darkMode = false;

                } else {

                    text = "Mode: dark";

                    //dark mode
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                    darkMode = true;
                }

                //save music preferences
                SharedPreferences setting0 = getSharedPreferences("dark", 0);
                SharedPreferences.Editor editor0 = setting0.edit();
                editor0.putBoolean("key0", darkMode);
                editor0.apply();
            }
        });

    }


    private void toggleLanguage(final String language) {
      //  selectedLanguage=language;
        currentLanguage = getIntent().getStringExtra(currentLang);

        spinner = findViewById(R.id.language_spinner);

        List<String> list = new ArrayList<>();

        list.add("Select language");
        list.add("English");
        list.add("Arabic");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        selectedLanguage=language;
                        break;

                    case 1:
                        setLocale("en");
                        selectedLanguage="en";
                        break;
                    case 2:
                        setLocale("ar");
                        selectedLanguage="ar";
                        break;

                }


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("language", selectedLanguage);
                editor.apply();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //setLocale("en");

            }
        });
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.setLocale(myLocale);//locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(GeneralSettingsActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);

    }
}


