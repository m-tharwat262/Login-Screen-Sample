package com.smmalos.tasks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smmalos.tasks.R;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Context mContext;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private FirebaseAuth mFirebaseAuth;


    private LinearLayout mProfileButton;
    private LinearLayout mLogoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // initialize the context we work at and preference.
        mContext = MainActivity.this;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = pref.edit();


        mProfileButton = findViewById(R.id.activity_main_profile_button);
        mLogoutButton= findViewById(R.id.activity_main_logout_button);


        setClickingOnProfileButton();
        setClickingOnLogout();

    }

    private void setClickingOnProfileButton() {

        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);


            }
        });

    }



    private void setClickingOnLogout() {

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isNetworkAvailable()) {
                    singOut();
                } else {
                    Toast.makeText(mContext, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void singOut() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signOut();

        // store the user data in preference.
        editor.putString(getString(R.string.user_id_Key), getString(R.string.preference_string_empty_value));
        editor.putString(getString(R.string.name_Key), getString(R.string.preference_string_empty_value));
        editor.putString(getString(R.string.email_address_Key), getString(R.string.preference_string_empty_value));
        editor.putString(getString(R.string.user_phone_number), getString(R.string.preference_string_empty_value));
        editor.putInt(getString(R.string.user_age), -1);
        editor.putString(getString(R.string.user_committee), getString(R.string.preference_string_empty_value));
        editor.apply();

        Toast.makeText(mContext, "Successfully Logout", Toast.LENGTH_SHORT).show();
        checkUserAlreadyLoggedIn();

    }

    private void checkUserAlreadyLoggedIn() {

        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

        if(firebaseUser == null) {

            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();

        }

    }


    /**
     * Check if the user is connecting to internet or not.
     *
     * @return boolean refer to connecting to internet or not.
     */
    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}