package com.unundefined.busbeats;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

        public class LoginActivity extends AppCompatActivity {
            private static final String TAG = "LoginActivity";
            private CallbackManager mCallbackManager;
            private LoginButton mLoginButton;
            private FirebaseAuth mAuth;
            private FirebaseFirestore database;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

                mAuth = Auth.getFirebaseAuth();
                database = FirebaseFirestore.getInstance();

                mCallbackManager = CallbackManager.Factory.create();

                mLoginButton = findViewById(R.id.facebook_login_button);

                //khaled
                findViewById(R.id.opening_background).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        khaled();   }
                }, 1500);

                findViewById(R.id.do_not_have_an_account).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    }
                });
                //khaledend

                setUpLoginButton();

                TextView signUpButton = findViewById(R.id.do_not_have_an_account);
                signUpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                    }
                });
            }

            @Override
            public void onStart() {
                super.onStart();
                // Debugging
                mAuth.signOut();
                // Check if user is signed in (non-null) and update UI accordingly. Important.
                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUI(currentUser);
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                super.onActivityResult(requestCode, resultCode, data);
            }

            private void updateUI(@Nullable FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    User.setCurrentUser(new User(firebaseUser));
                    User.updateUI();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Log.d(TAG, "updateUI: Moved to next activity");
                }
            }

            private boolean signInWithEmailAndPassword(String email, String password) {
                Task<AuthResult> signInTask = mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
                return signInTask.isSuccessful();
            }

            private void setUpLoginButton() {
                mLoginButton.setPermissions(Arrays.asList(
                        "public_profile", "email", "user_gender", "user_birthday", "user_link"));

                mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                        Log.d(TAG, "registerCallback: onSuccess: Access token: " +
                                loginResult.getAccessToken());

                        handleFacebookAccessToken(loginResult.getAccessToken());

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

            private void createUserOnDatabase(final FirebaseUser user) {
                final GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // TODO: If any crucial data is missing, throw exception
                                Log.v("LoginActivity", response.toString());
                                Log.d(TAG, "onCompleted: JSON object: " + object.toString());
                                String id;
                                String firstName;
                                String middleName;
                                String lastName;
                                String email;
                                String gender;
                                Date birthday;
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
                                    email = object.getString("email");
                                } catch (JSONException exception) {
                                    email = null;
                                }

                                try {
                                    gender = object.getString("gender");
                                } catch (JSONException exception) {
                                    gender = null;
                                }

                                try {
                                    birthday = Date.toDate(object.getString("birthday"));
                                } catch (JSONException exception) {
                                    birthday = null;
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

                                Log.w(TAG, "createUserOnDatabase: Creating database info");
                                final Map<String, Object> userMap = new HashMap<>();
                                userMap.put("first_name", firstName);
                                userMap.put("last_name", lastName);
                                userMap.put("email", email);
                                userMap.put("gender", gender);
                                userMap.put("birthday", birthday.toString());
                                userMap.put("is_facebook_connected", false);

                                database.collection("users")
                                        .document(user.getUid())
                                        .set(userMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                                startActivity(new Intent(getApplicationContext()
                                                        , MainActivity.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                                database.collection("users")
                                                        .document(user.getUid()).delete();
                                                user.delete();
                                                Toast.makeText(getApplicationContext(),
                                                        "An unknown error occurred." +
                                                                " Please try again later.",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            private void updateUserOnDatabase(FirebaseUser user) {
                // TODO
            }

            private void handleFacebookAccessToken(AccessToken token) {
                Log.d(TAG, "handleFacebookAccessToken:" + token);

                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    checkAndCreateDAta();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                                    Toast.makeText(LoginActivity.this,
                                            "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }

            private void checkAndCreateDAta() {
                database.collection("users")
                        .document("")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            assert document != null;
                            if (document.exists()) {
                                Log.d(TAG, "User already exists");
                                updateUserOnDatabase(user);
                            } else {
                                Log.d(TAG, "User did not exist");
                                createUserOnDatabase(user);
                            }
                            updateUI(user);
                        } else {
                            Log.w(TAG, "checkAndCreateData: onComplete: failure");
                        }
                    }
                });
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




