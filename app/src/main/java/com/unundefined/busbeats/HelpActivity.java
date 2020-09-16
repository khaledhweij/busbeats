package com.unundefined.busbeats;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class HelpActivity extends AppCompatActivity implements RatingDialogListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help3);

     findViewById(R.id.need_help_button).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String mailto = "mailto:useremail@gmail.com" +
                    "?cc=" +
                    "&subject=" + Uri.encode("your subject") +
                    "&body=" + Uri.encode("your mail body");
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(mailto));

            try {
                startActivity(emailIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Error to open email app", Toast.LENGTH_SHORT).show();
            }
        }
    });
        AppRater.app_launched(this);

        findViewById(R.id.rate_application_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            showDialog();

            }
        });
    }

    private void showDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle("Rate this application")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("This app is pretty cool !")
                .setStarColor(R.color.red)
                .setNoteDescriptionTextColor(R.color.dark_blue)
                .setTitleTextColor(R.color.dark_blue)
                .setDescriptionTextColor(R.color.gray)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.red)
                .setCommentTextColor(R.color.red)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(HelpActivity.this)
                .show();
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {

    }
}
