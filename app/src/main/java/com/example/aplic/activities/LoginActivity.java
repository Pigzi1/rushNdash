package com.example.aplic.activities;

import static android.content.ContentValues.TAG;
import static com.example.aplic.Statics.isConnectedToInternet;
import static com.example.aplic.Statics.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    private CheckBox cbSharedPreferences;
    private SharedPreferences sp;
    private Button btQuit, btLogin, btForgot, btNew;
    private EditText etName, etMail, etPass;
    private TextView tvNotSignIn;
    private String userName, mail, password;
    private GifImageView loading;
    private Intent intent;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cbSharedPreferences = findViewById(R.id.cbSharedPreferences);
        sp = getSharedPreferences("loginDetails", 0);
        btQuit = findViewById(R.id.btQuit);
        btLogin = findViewById(R.id.btLogin);
        btForgot = findViewById(R.id.btForgot);
        btNew = findViewById(R.id.btNew);
        tvNotSignIn = findViewById(R.id.tvNotSignIn);
        etName = findViewById(R.id.etName);
        etMail = findViewById(R.id.etMail);
        etPass = findViewById(R.id.etPass);
        btQuit.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btNew.setOnClickListener(this);
        btForgot.setOnClickListener(this);
        tvNotSignIn.setOnClickListener(this);
        loading = findViewById(R.id.gifLoading);
        loading.setVisibility(View.INVISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        // sets the details from shared preferences if they exist
        userName = sp.getString("userName", null);
        mail = sp.getString("mail", null);
        password = sp.getString("password", null);
        if (userName != null && !userName.equals("")) {
            etName.setText(userName);
            etMail.setText(mail);
            etPass.setText(password);
            cbSharedPreferences.setChecked(true);
        }
    }
    @Override
    public void onClick(View view) {
        if (view == btQuit) {
            finish();
        }
        else if (view == btLogin)
        {
            startService(new Intent(this, VibrationService.class));
            if (isConnectedToInternet(this)) {
                userName = etName.getText().toString();
                mail = etMail.getText().toString();
                password = etPass.getText().toString();
                if (checkLoginInformation()) {
                    userRef.addValueEventListener(this);
                    loading.setVisibility(View.VISIBLE);
                    changeClickables(false);
                }
            }
            else {
                Toast.makeText(this,"Please check your connection!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (view == btNew)
        {
            intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }
        else if (view == btForgot)
        {
            intent = new Intent(this, ForgotActivity.class);
            startActivity(intent);
        }
        else if (view == tvNotSignIn) {
            if (isConnectedToInternet(this)) {
                user = new User();
                user.InitializeAllValues();
                intent = new Intent(this, GameScreenActivity.class);
                intent.putExtra("LevelID", 0);
                Log.i(TAG, "onClick: LEVEL - " + 0);
                startActivity(intent);
            }
            else
                Toast.makeText(this, "Please connect to the internet first!", Toast.LENGTH_SHORT).show();
        }
    }
    // checks if information is acceptable
    public boolean checkLoginInformation() {
        if (TextUtils.isEmpty(userName)||
                TextUtils.isEmpty(mail)||TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this,
                    "Please enter all required details!",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onClick: empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(LoginActivity.this,
                    "E-Mail is invalid.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onClick: email is not valid");
            return false;
        }
        return true;
    }
    public void loginAccount() {
        // logs in the account if the password is correct
        firebaseAuth.signInWithEmailAndPassword(mail,password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // saves the information to shared preferences if checkbox is ticked
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sp.edit();
                        if (cbSharedPreferences.isChecked()) {
                            editor.putString("userName", userName);
                            editor.putString("mail", mail);
                            editor.putString("password", password);
                        }
                        else {
                            editor.putString("userName", "");
                            editor.putString("mail", "");
                            editor.putString("password", "");

                        }
                        editor.apply();
                        Toast.makeText(LoginActivity.this,
                                "Login successful",
                                Toast.LENGTH_SHORT).show();
                        intent = new Intent(LoginActivity.this, HubActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,
                                "Details incorrect!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // goes through each account and looks for the one with
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Log.i(TAG, "onDataChange: onDataChange");
        userRef.removeEventListener(this);
        for (DataSnapshot data : snapshot.getChildren()) {
            User tempUser = data.getValue(User.class);
            assert tempUser != null;
            Log.i(TAG, "onDataChange: checking user " + tempUser.uid);
            if (Objects.equals(tempUser.mail, etMail.getText().toString())) {
                Log.i(TAG, "onDataChange: email correct");
                if (Objects.equals(tempUser.userName, etName.getText().toString())) {
                    Log.i(TAG, "onDataChange: CORRECT");
                    user = data.getValue(User.class);
                    loginAccount();
                    loading.setVisibility(View.INVISIBLE);
                    changeClickables(true);
                    return;
                }
                else Log.i(TAG, "onDataChange: incorrect 2");
            }
            else Log.i(TAG, "onDataChange: incorrect 1");
        }
        Toast.makeText(LoginActivity.this,
                "Details incorrect!",
                Toast.LENGTH_SHORT).show();
        loading.setVisibility(View.INVISIBLE);
        changeClickables(true);
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.i(TAG, "onCancelled: ERROR");
    }
    // makes it so other button aren't clickable when firebase is checking the information
    public void changeClickables(boolean b) {
        btLogin.setClickable(b);
        btForgot.setClickable(b);
        btNew.setClickable(b);
    }
}