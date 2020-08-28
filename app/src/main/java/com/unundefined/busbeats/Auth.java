package com.unundefined.busbeats;

import com.google.firebase.auth.FirebaseAuth;

class Auth {
    private static FirebaseAuth mAuth;
    private static boolean setUp = false;

    static void setUp() {
        mAuth = FirebaseAuth.getInstance();
        setUp = true;
    }

    static FirebaseAuth getFirebaseAuth() {
        if (!setUp) {
            setUp();
        }
        return mAuth;
    }
}