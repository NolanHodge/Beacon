package com.comp3004.beacon.GUI;

import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.Context;

import android.database.Cursor;

import android.net.Uri;

import android.os.Build;
import android.os.Bundle;

import android.provider.ContactsContract;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.widget.ProgressBar;
import android.util.Log;

import android.view.View;
import android.view.WindowManager;

import android.widget.Toast;

import com.comp3004.beacon.Networking.SaveSharedPreference;
import com.comp3004.beacon.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * A login screen that offers login via google
 * TODO Facebook
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private CallbackManager callbackManager;

    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar mProgress;


    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private SignInButton mGoogleSignInButton;
    private LoginButton mFacebookSignInButton;
    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setVisibility(View.INVISIBLE);

        //Fields
        mGoogleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        mFacebookSignInButton = (LoginButton) findViewById(R.id.login_button);

        //Click Listeners
        mGoogleSignInButton.setOnClickListener(this);

        // Set up the login form.
        populateAutoComplete();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        setupTokenTracker();
        setupProfileTracker();
        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            startActivity(new Intent(this, MapsActivity.class));
            //finish();
        }

        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, mFacebookCallback);
        loginButton.setReadPermissions("public_profile, user_friends");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
        }

        if(SaveSharedPreference.getUserName(LoginActivity.this).equals("google"))
        {
            mProgress.setVisibility(View.VISIBLE);
            WindowManager.LayoutParams wp = getWindow().getAttributes();
            wp.dimAmount = 1f;
            mFacebookSignInButton.setVisibility(View.GONE);
            mGoogleSignInButton.setVisibility(View.GONE);
            googleSignIn();
        }
        else if (SaveSharedPreference.getUserName(LoginActivity.this).equals("facebook"))
        {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                googleSignIn();
                break;
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                SaveSharedPreference.setUserName(this, "google");
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        if (!mayRequestStorage()) {
            return;
        }
        if (!mayRequestCamera()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {

        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    private boolean mayRequestStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {

        } else {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    private boolean mayRequestCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(CAMERA)) {

        } else {
            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
        }
        return false;
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        } else if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                finish();
            }
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }


    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(LoginActivity.this, MapsActivity.class));

                }
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("", "handleFacebookAccessToken:" + token);

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.d("User", "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(com.facebook.Profile profile, com.facebook.Profile profile1) {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {

            AccessToken accessToken = loginResult.getAccessToken();
            handleFacebookAccessToken(accessToken);
        }

        @Override
        public void onCancel() {
            Log.d("Facebook", "Cancelled");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d("Facebook", "Error " + e);
        }
    };

    public static Context getContext()
    {
        return getContext();
    }
}

