package com.example.aplic.activities;

import static android.content.ContentValues.TAG;

import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    private EditText etName, etMail;
    private String email;
    private Button btChange;
    private ImageButton ibBack;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        etName = findViewById(R.id.etForgotName); etMail = findViewById(R.id.etForgotMail);
        btChange = findViewById(R.id.btChange);
        ibBack = findViewById(R.id.ibBack);
        btChange.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view == btChange)
        {
            startService(new Intent(this, VibrationService.class));
            if (isConnectedToInternet(this))
                userRef.addValueEventListener(this);
            else
                Toast.makeText(this, "Please connect to the internet!", Toast.LENGTH_SHORT).show();
            }
        else if (view == ibBack) {
            finish();
        }
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        userRef.removeEventListener(this);
        String userName = etName.getText().toString();
        email = etMail.getText().toString();

        // looks for the user and sends an email to the correct account
        for (DataSnapshot data : snapshot.getChildren()) {
            User tempUser = data.getValue(User.class);
            assert tempUser != null;
            Log.i(TAG, "onDataChange: " + tempUser.mail+" "+email+" "+tempUser.userName+" "+userName);
            if (Objects.equals(tempUser.mail, email)) {
                if (Objects.equals(tempUser.userName, userName)) {
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotActivity.this, "Email sent to " + email, Toast.LENGTH_SHORT).show();
                                }
                            });            finish();
                    return;
                }
            }
        }
        Toast.makeText(this, "Unable to find your account, please verify your information", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }
}