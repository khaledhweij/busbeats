package com.unundefined.busbeats;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.widget.Toast;

        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.view.MotionEventCompat;

        import com.facebook.AccessToken;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookOperationCanceledException;
        import com.facebook.GraphRequest;
        import com.facebook.GraphRequestAsyncTask;
        import com.facebook.GraphResponse;
        import com.facebook.Profile;
        import com.facebook.login.LoginManager;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //khaled
        findViewById(R.id.opening_background).postDelayed(new Runnable() {
            @Override
            public void run() {
                khaled();   }
        }, 1500);

        //khaledend


        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = findViewById(R.id.facebook_login_button);
        loginButton.setPermissions(Arrays.asList(
                "public_profile", "email", "user_gender", "user_birthday", "user_link"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                Log.d(TAG, "registerCallback: onSuccess: Access token: " +
                        loginResult.getAccessToken());

                final GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                Log.d(TAG, "onCompleted: JSON object: " + object.toString());
                                String id;
                                String firstName;
                                String middleName;
                                String lastName;
                                String name;
                                Uri linkUri;

                                try {
                                    id = object.getString("id");
                                } catch (JSONException exception) {
                                    id = null;
                                }

                                try {
                                    firstName = object.getString("first_name");
                                } catch (JSONException exception) {
                                    firstName = null;
                                }

                                try {
                                    middleName = object.getString("middle_name");
                                } catch (JSONException exception) {
                                    middleName = null;
                                }

                                try {
                                    lastName = object.getString("last_name");
                                } catch (JSONException exception) {
                                    lastName = null;
                                }

                                try {
                                    name = object.getString("name");
                                } catch (JSONException exception) {
                                    name = null;
                                }

                                try {
                                    linkUri = Uri.parse(object.getString("link"));
                                } catch (JSONException exception) {
                                    linkUri = null;
                                }

                                Profile.setCurrentProfile(new Profile(id, firstName, middleName,
                                        lastName, name, linkUri));

                                Log.d(TAG, "onSuccess: Profile picture: " +
                                        Profile.getCurrentProfile()
                                                .getProfilePictureUri(2000, 2000)
                                                .toString());
                                synchronized (com.unundefined.busbeats.MainActivity.this) {
                                    com.unundefined.busbeats.MainActivity.this.notify();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                Thread requestThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        request.executeAndWait();
                    }
                });
                requestThread.start();
                try {
                    Log.d(TAG, "onSuccess: Here");
                    requestThread.join();
                } catch (InterruptedException exception) {
                    Log.e(TAG, "onSuccess: Interrupted exception occurred" +
                            " on request thread: " + exception.getMessage());
                    Log.w(TAG, "onSuccess: Executing request asynchronously");
                    GraphRequestAsyncTask asyncTask = request.executeAsync();
                    try {
                        synchronized (com.unundefined.busbeats.MainActivity.this) {
                            com.unundefined.busbeats.MainActivity.this.wait(3000);
                            if (!asyncTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                                // Go to error state
                                LoginManager.getInstance().logOut();
                                onError(new FacebookOperationCanceledException("Request timed" +
                                        " out"));
                                return;
                            }
                        }
                    } catch (InterruptedException interruptedWaitException) {
                        Log.e(TAG, "onSuccess: wait interrupted");

                    }
                }

                if (Profile.getCurrentProfile() == null) {
                    LoginManager.getInstance().logOut();
                    onError(new FacebookOperationCanceledException("Unknown error " +
                            "occurred"));
                    return;
                }

                // Send info to data base

                Log.d(TAG, "onSuccess: Done");
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),
                        "Facebook login was canceled, please try again",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),
                        "Error occurred, please try again.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "registerCallback: onError: " + error.getMessage());
            }
        });



    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    protected void khaled(){


        findViewById(R.id.opening_layout).setBackgroundColor(getResources().getColor(R.color.dark_blue));
        findViewById(R.id.opening_background).setVisibility(View.GONE);

        findViewById(R.id.login_layout).setVisibility(View.VISIBLE);

        /*findViewById(R.id.do_not_have_an_account).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
            });*/

    }






}




