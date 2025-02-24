package com.example.foodorder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.foodorder.Domain.User;
import com.example.foodorder.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends BaseActivity {
    private ImageView comebackBtn;
    private EditText fullnameUser;
    private EditText phoneUser;
    private EditText addressUser;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        comebackBtn = findViewById(R.id.combackBtn);
        comebackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "Go back to MainActivity");
                setResult(MainActivity.RESULT_OK, returnIntent);
                finish();
            }
        });

        fullnameUser = findViewById(R.id.fullnameUser);
        phoneUser = findViewById(R.id.phoneUser);
        addressUser = findViewById(R.id.addressUser);
        saveBtn = findViewById(R.id.saveBtn);

        // Set click listener for saveBtn
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String fullName = fullnameUser.getText().toString();
                String phone = phoneUser.getText().toString();
                String address = addressUser.getText().toString();

                // Create user object
                User user = new User(fullName, phone, address);

                // Save user data to Firebase Realtime Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String currentUid = ((BaseActivity) UserProfileActivity.this).getCurrentUserUid();
                if (currentUid == null) {
                    // User is not logged in, redirect to login page
                    startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                    finish();
                    return;
                }
                Toast.makeText(UserProfileActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                DatabaseReference myRef = database.getReference("users").child(currentUid);
                myRef.setValue(user);
            }
        });

        // Load user data from Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String currentUid = ((BaseActivity) UserProfileActivity.this).getCurrentUserUid();
        if (currentUid == null) {
            // User is not logged in, redirect to login page
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }
        DatabaseReference myRef = database.getReference("users").child(currentUid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the user data exists in the database
                if (dataSnapshot.exists()) {
                    // Get user data from DataSnapshot
                    User user = dataSnapshot.getValue(User.class);

                    // Display user data on UserProfile Activity
                    if (user != null) {
                        fullnameUser.setText(user.getFullName());
                        phoneUser.setText(user.getPhone());
                        addressUser.setText(user.getAddress());
                    }
                } else {
                    // User data does not exist in the database, create a new user object
                    User user = new User("", "", "");
                    fullnameUser.setText(user.getFullName());
                    phoneUser.setText(user.getPhone());
                    addressUser.setText(user.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Set up text changed listeners for input fields
        fullnameUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputFields();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });


        // Check input fields on activity creation
        checkInputFields();
    }

    private void checkInputFields() {
        String fullName = fullnameUser.getText().toString();
        String phone = phoneUser.getText().toString();
        String address = addressUser.getText().toString();

        if (!fullName.isEmpty() && !phone.isEmpty() && !address.isEmpty()) {
            // Input fields are not empty, enable save button
            saveBtn.setEnabled(true);
        } else {
            // Input fields are empty,
        }
    }
}