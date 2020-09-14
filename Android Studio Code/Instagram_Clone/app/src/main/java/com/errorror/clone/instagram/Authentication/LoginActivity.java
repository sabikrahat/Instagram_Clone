package com.errorror.clone.instagram.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.errorror.clone.instagram.MainActivity;
import com.errorror.clone.instagram.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLoginEditText, passwordLoginEditText;
    private Button loginButton;
    private TextView registerTextView;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLoginEditText = findViewById(R.id.loginActivityEmailEditTextID);
        passwordLoginEditText = findViewById(R.id.loginActivityPasswordEditTextID);
        loginButton = findViewById(R.id.loginActivityLoginButtonID);
        registerTextView = findViewById(R.id.loginActivityRegisterTextID);

        mAuth = FirebaseAuth.getInstance();

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                userLogin();
            }
        });

    }

    private void userLogin() {
        String email = emailLoginEditText.getText().toString();
        String password = passwordLoginEditText.getText().toString();

        //checking the validity of the email
        if (email.isEmpty()) {
            emailLoginEditText.setError("Enter an email address");
            emailLoginEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLoginEditText.setError("Enter a valid email address");
            emailLoginEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if (password.isEmpty()) {
            passwordLoginEditText.setError("Enter a password");
            passwordLoginEditText.requestFocus();
            return;
        }

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}