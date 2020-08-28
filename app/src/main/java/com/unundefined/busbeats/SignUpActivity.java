package com.unundefined.busbeats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String passwordConfirmation;
    private String gender;
    private Date birthday;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = Auth.getFirebaseAuth();
        database = FirebaseFirestore.getInstance();

        final Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = ((EditText) findViewById(R.id.first_name_field)).getText()
                        .toString();
                lastName = ((EditText) findViewById(R.id.last_name_field)).getText()
                        .toString();
                email = ((EditText) findViewById(R.id.email_field)).getText()
                        .toString();
                password = ((EditText) findViewById(R.id.password_field)).getText()
                        .toString();
                passwordConfirmation =
                        ((EditText) findViewById(R.id.password_confirmation_field)).getText()
                                .toString();
                switch (((RadioGroup) findViewById(R.id.radio_group)).getCheckedRadioButtonId()) {
                    case R.id.radio_button_male:
                        gender = User.GENDER_MALE;
                        break;
                    case R.id.radio_button_female:
                        gender = User.GENDER_FEMALE;
                        break;
                    case R.id.radio_button_not_specified:
                    default:
                        gender = User.GENDER_NOT_SPECIFIED;
                }
                birthday = new Date(1999, 9, 9); // TODO: Create date button
                if (password.equals(passwordConfirmation)) {
                    registerUser();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void registerUser() {
        if (!validate(firstName, lastName, email, password)) {
            Log.w(TAG, "registerUser: Data is invalid");
            return;
        }
        createUserWithEmailAndPassword();
        // TODO: Show loading screen
    }

    private void createUserWithEmailAndPassword() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            assert user != null;
                            Log.d(TAG, "onComplete: UserID = " + user.getUid());
                            createUserOnDatabase();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure",
                                    task.getException());
                            Toast.makeText(SignUpActivity.this,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "registerUser: User creation failed");
                            updateUI(null);
                        }
                    }
                });
    }

    private void createUserOnDatabase() {
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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        database.collection("users").document(user.getUid()).delete();
                        user.delete();
                        Toast.makeText(getApplicationContext(),
                                "An unknown error occurred. Please try again later.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validate(String firstName, String lastName, String email, String password) {
        if (notAlpha(firstName)) {
            Toast.makeText(getApplicationContext(),
                    "First name must only contain letters or dashes", // TODO
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (notAlpha(lastName)) {
            Toast.makeText(getApplicationContext(),
                    "Last name must only contain letters or dashes",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isEmail(email)) {
            Toast.makeText(getApplicationContext(), "Email is invalid",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwordIsValid(password)) {
            Toast.makeText(getApplicationContext(), "Password is invalid",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean notAlpha(String name) {
        return !name.matches("[a-zA-Z]+|(-)");
    }

    private boolean isEmail(String email) {
        return email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
    }

    private boolean passwordIsValid(String password) {
        return password.length() >= 8
                && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    }

    private void updateUI(@Nullable FirebaseUser user) {
        if (user != null) {
            User.setCurrentUser(new User(firstName, lastName, email, birthday, gender));
            User.updateUI();
        } else {
            Log.w(TAG, "updateUI: user is null");
        }
    }
}