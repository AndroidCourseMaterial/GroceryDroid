package com.mattboutell.grocerydroid2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.mattboutell.grocerydroid2.utils.SharedPreferencesUtils;


// TODO: Replace this whole class with a real login activity, once we figure out how to do it!
public class StartActivity extends AppCompatActivity {

    private Firebase mFirebaseRef;
    private Firebase.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(getString(R.string.firebase_url));
        mFirebaseRef.authWithPassword("boutell@gmail.com", "abc", null);

        mAuthStateListener = mFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    Log.d(Constants.TAG, "User is authenticated");
                    String uid = authData.getUid();
                    mFirebaseRef.child("/owners/" + uid + "/email").setValue("boutell@gmail.com");
                    mFirebaseRef.child("/owners/" + uid + "/name").setValue("Matt");

                    SharedPreferencesUtils.setUid(StartActivity.this, uid);
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                } else {
                    Log.d(Constants.TAG, "User is NOT authenticated");
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Constants.TAG, "Stopping startActivity and removing AuthStateListener");
        mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }

}
