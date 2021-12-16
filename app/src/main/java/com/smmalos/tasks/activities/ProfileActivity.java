package com.smmalos.tasks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.smmalos.tasks.R;

public class ProfileActivity extends AppCompatActivity {


    private static final String LOG_TAG = ProfileActivity.class.getSimpleName();
    private Context mContext;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private TextView mNameTextView;
    private TextView mEmailAddressTextView;
    private TextView mPhoneNumberTextView;
    private TextView mAgeTextView;
    private TextView mCommitteeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // initialize the context we work at and preference.
        mContext = ProfileActivity.this;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = pref.edit();


        mNameTextView = findViewById(R.id.activity_profile_name_field);
        mEmailAddressTextView = findViewById(R.id.activity_profile_email_address_field);
        mPhoneNumberTextView = findViewById(R.id.activity_profile_phone_number_field);
        mAgeTextView = findViewById(R.id.activity_profile_age_field);
        mCommitteeTextView = findViewById(R.id.activity_profile_committee_field);


        addDataToFields();


    }

    private void addDataToFields() {


        String phoneNumber = pref.getString(getString(R.string.user_phone_number), "");
        String committee = pref.getString(getString(R.string.user_committee), "");
        int age = pref.getInt(getString(R.string.user_age), -1);
        String name = pref.getString(getString(R.string.name_Key), "");
        String email = pref.getString(getString(R.string.email_address_Key), "");

        mNameTextView.setText(name);
        mEmailAddressTextView.setText(email);
        mPhoneNumberTextView.setText(phoneNumber);
        mAgeTextView.setText(String.valueOf(age));
        mCommitteeTextView.setText(committee);
    }
}