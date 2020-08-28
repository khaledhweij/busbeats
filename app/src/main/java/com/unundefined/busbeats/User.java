package com.unundefined.busbeats;

import com.google.firebase.auth.FirebaseUser;

class User {
    static final String GENDER_MALE = "Male";
    static final String GENDER_FEMALE = "Female";
    static final String GENDER_NOT_SPECIFIED = "Not specified";
    static User currentUser;
    private static String defaultProfilePictureUrl;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private String gender;
    private String profilePictureUrl;
    private String facebookId;
    private boolean isFacebookConnected;

    User(String firstName, String lastName, String email, Date birthday,
         String gender, String facebookId, String profilePictureUrl) {
        this.isFacebookConnected = facebookId != null;
        this.facebookId = facebookId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        setProfilePictureUrl(profilePictureUrl);
        updateUI();
    }

    User(String firstName, String lastName, String email, Date birthday,
         String gender) {
        this(firstName, lastName, email, birthday, gender, null,
                defaultProfilePictureUrl);
    }

    User(String firstName, String lastName, String email, Date birthday
            , String gender, String profilePictureUrl) {
        this(firstName, lastName, email, birthday, gender, null,
                profilePictureUrl);
    }

    User(FirebaseUser firebaseUser) {
        // TODO
    }

    static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    static void updateUI() {
        // TODO
    }

    void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}