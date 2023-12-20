package com.example.aplic.activities;


import static android.content.ContentValues.TAG;
import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplic.R;

import com.example.aplic.User;
import com.example.aplic.VibrationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    private EditText etName, etMail, etPass, etConfPass;
    private String userName, mail, password, confPassword;
    private ImageButton ibBack;
    private Button btSignUp;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etName = findViewById(R.id.etNewName);
        etMail = findViewById(R.id.etNewMail);
        etPass = findViewById(R.id.etNewPass);
        etConfPass = findViewById(R.id.etConfNewPass);
        btSignUp = findViewById(R.id.btSignUp);
        btSignUp.setOnClickListener(this);
        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = firebaseDatabase.getReference("Users").push();
    }

    @Override
    public void onClick(View view) {
        if (view == ibBack)
            finish();
        if (view == btSignUp)
        {
            if (isConnectedToInternet(this)) {
                startService(new Intent(this, VibrationService.class));
                userName = etName.getText().toString();
                mail = etMail.getText().toString();
                password = etPass.getText().toString();
                confPassword = etConfPass.getText().toString();
                signUp();
            }
            else
                Toast.makeText(this,
                        "Please connect to the internet!",
                        Toast.LENGTH_SHORT).show();
        }
    }
    // checks if information is acceptable
    public void signUp() {
        if (TextUtils.isEmpty(userName)|| TextUtils.isEmpty(mail)||
                TextUtils.isEmpty(password)||TextUtils.isEmpty(confPassword)) {
            Toast.makeText(SignUpActivity.this,
                    "Please enter all required details!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(getApplicationContext(),
                    "E-Mail is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!Objects.equals(password, confPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseDatabase.getReference("Users").addValueEventListener(this);
    }
    // adds a user to firebase database
    public void addUserDetails() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String key = userRef.getKey();
        user = new User(uid,mail,userName,key);
        userRef.setValue(user);
        finish();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        // looks for if username or email account are already being used
        userRef.removeEventListener(this);
        firebaseDatabase.getReference("Users").removeEventListener(this);
        for (DataSnapshot data : snapshot.getChildren()) {
            User tempUser = data.getValue(User.class);
            assert tempUser != null;
            Log.i(TAG, "onDataChange: checking user " + tempUser.uid);
            if (Objects.equals(tempUser.mail, etMail.getText().toString())) {
                Log.i(TAG, "onDataChange: email exists");
                Toast.makeText(SignUpActivity.this, "Email account already used", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                if (Objects.equals(tempUser.userName, etName.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Username already used", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onDataChange: username exists");
                    return;
                }
            }
        }
        // creates a firebase user
        firebaseAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this,
                                "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                        addUserDetails();
                    } else {
                        Toast.makeText(SignUpActivity.this,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}