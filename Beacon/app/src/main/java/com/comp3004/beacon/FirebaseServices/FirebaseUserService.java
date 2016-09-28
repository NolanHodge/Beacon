package com.comp3004.beacon.FirebaseServices;

import com.google.firebase.auth.AuthCredential;

/**
 * Created by julianclayton on 16-09-27.
 */
public class FirebaseUserService {


    FirebaseUserService firebaseUserService;

    public FirebaseUserService getInstance() {
        if (firebaseUserService == null) {
            firebaseUserService = new FirebaseUserService();
        }
        return firebaseUserService;
    }

    public void signIn(AuthCredential credential) {

    }
}
