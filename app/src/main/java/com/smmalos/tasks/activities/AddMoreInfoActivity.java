package com.smmalos.tasks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smmalos.tasks.R;

import java.util.ArrayList;

public class AddMoreInfoActivity extends AppCompatActivity {


    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private Context mContext;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private EditText mPhoneNumberEditText;
    private EditText mAgeEditText;
    private Spinner mCommitteeSpinner;
    private TextView mFirstPersonNameTextView;
    private LinearLayout mSaveButton;
    private ProgressBar mProgressBar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ChildEventListener mUsersChildEventListener;

    private String mCurrentUserId;

//    private int mUserPosition;
    private ArrayList<String> mAllUsersIds = new ArrayList<>();
    private ArrayList<String> mAllUsersNames = new ArrayList<>();

    private ArrayAdapter<String> mUserSpinnerAdapter;

    private String mPhoneNumber;
    private int mAge;
    private String mCommitteeName;

    private int mCommitteePosition = 1;

    private ArrayList<String> mCommitteeArrayList;


    private static final int VIEWS_NUMBER = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_moer_info);


        mContext = AddMoreInfoActivity.this;
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = pref.edit();



        mPhoneNumberEditText = findViewById(R.id.activity_add_more_info_phone_number_field);
        mAgeEditText = findViewById(R.id.activity_add_more_info_age_field);
        mCommitteeSpinner = findViewById(R.id.activity_add_more_info_committee_spinner);
        mSaveButton = findViewById(R.id.activity_add_more_info_save_button);
        mProgressBar = findViewById(R.id.activity_add_more_info_progress_bar);


        // make the phone number field add th country code at first when user start typing his number.
        mPhoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable s) {

                Log.i(LOG_TAG, "the Editable contains (" + s.toString() + ")");

                if(!s.toString().startsWith("+20 ") ) {

                    if (s.toString().startsWith("+20")) {
                        s.insert(3, " ");
                    } else {
                        s.insert(0, "+20 ");
                    }

                }
            }
        });


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        mUsersDatabaseReference.keepSynced(true);



        mCurrentUserId = pref.getString(getString(R.string.user_id_Key), getString(R.string.preference_string_empty_value));


        initializeCommitteeArrayList();


        setUpSpinner();


        setClickingOnAddTaskButton();


    }

    private void initializeCommitteeArrayList() {

        mCommitteeArrayList = new ArrayList<>();

        mCommitteeArrayList.add("select one");
        mCommitteeArrayList.add("Android App");
        mCommitteeArrayList.add("Academy");
        mCommitteeArrayList.add("BD");
        mCommitteeArrayList.add("Logistics");
        mCommitteeArrayList.add("Organizing");
        mCommitteeArrayList.add("Extracurricular");
        mCommitteeArrayList.add("E4ME");
        mCommitteeArrayList.add("Magazine Editing");
        mCommitteeArrayList.add("IR");
        mCommitteeArrayList.add("HRM");
        mCommitteeArrayList.add("HRD");
        mCommitteeArrayList.add("IT Web");
        mCommitteeArrayList.add("Offline marketing");
        mCommitteeArrayList.add("Social Media");
        mCommitteeArrayList.add("Multimedia");

    }


    private void setClickingOnAddTaskButton() {

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDataValid()) {
                    insertInsideFirebase();
                }


            }
        });

    }






    private boolean isDataValid()  {

        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        String age = mAgeEditText.getText().toString().trim();
        String committeeName = mCommitteeSpinner.getSelectedItem().toString();

        if (phoneNumber.isEmpty()) {
            mPhoneNumberEditText.setError("enter your phone number");
            mPhoneNumberEditText.requestFocus();
            return false;
        } else if (age.isEmpty()) {
            mAgeEditText.setError("enter your age");
            mAgeEditText.requestFocus();
            return false;
        } else if (committeeName.isEmpty() || committeeName.equals("select one")) {
            TextView errorText = (TextView) mCommitteeSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("select your committee ");//changes the selected item text to this
            mCommitteeSpinner.requestFocus();
            return false;
        } else {

            mPhoneNumber = phoneNumber;
            mAge = Integer.parseInt(age);
            mCommitteeName = committeeName;
            return true;
        }


    }


    private void setUpSpinner() {


        mUserSpinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mCommitteeArrayList);
        mUserSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mCommitteeSpinner.setAdapter(mUserSpinnerAdapter);
        mCommitteeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mCommitteePosition = position - 1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCommitteePosition = -1;
            }
        });

    }













    private void insertInsideFirebase() {

        mProgressBar.setVisibility(View.VISIBLE);


        mUsersDatabaseReference.child(mCurrentUserId).child("phoneNumber")
                .setValue(mPhoneNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                mUsersDatabaseReference.child(mCurrentUserId).child("committee")
                        .setValue(mCommitteeName);


                mUsersDatabaseReference.child(mCurrentUserId).child("age")
                        .setValue(mAge);



                editor.putString(getString(R.string.user_phone_number), mPhoneNumber);
                editor.putString(getString(R.string.user_committee), mCommitteeName);
                editor.putInt(getString(R.string.user_age), mAge);
                editor.apply();


                Intent intent = new Intent(AddMoreInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(mContext, "Saved Successfully", Toast.LENGTH_SHORT).show();

                mProgressBar.setVisibility(View.GONE);

            }
        });





    }







}