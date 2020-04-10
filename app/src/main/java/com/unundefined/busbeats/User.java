package com.unundefined.busbeats;

import java.util.Date;

class User {
    private static final String GENDER_MALE = "Male";
    private static final String GENDER_FEMALE = "Female";
    private static final String GENDER_NOT_SPECIFIED = "Not specified";
    static User currentUser;
    private String facebookId;
    private boolean isFacebookConnected;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;
    private String gender;
    private String profilePictureUrl;

}