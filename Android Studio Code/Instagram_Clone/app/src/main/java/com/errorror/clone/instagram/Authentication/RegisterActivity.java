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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameEditText, fullNameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameEditText = findViewById(R.id.registerActivityUsernameEditTextID);
        fullNameEditText = findViewById(R.id.registerActivityFullnameEditTextID);
        emailEditText = findViewById(R.id.registerActivityEmailEditTextID);
        passwordEditText = findViewById(R.id.registerActivityPasswordEditTextID);
        confirmPasswordEditText = findViewById(R.id.registerActivityConfirmPasswordEditTextID);
        registerButton = findViewById(R.id.registerActivityRegisterButtonID);
        loginTextView = findViewById(R.id.registerActivityLoginTextID);

        mAuth = FirebaseAuth.getInstance();

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                userRegister();
            }
        });
    }

    private void userRegister() {
        final String username = userNameEditText.getText().toString().toLowerCase();
        final String fullName = fullNameEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        //checking the validity of the first name
        if (username.isEmpty()) {
            userNameEditText.setError("Enter a username");
            userNameEditText.requestFocus();
            return;
        }

        //checking the validity of the last name
        if (fullName.isEmpty()) {
            fullNameEditText.setError("Enter your full name");
            fullNameEditText.requestFocus();
            return;
        }

        //checking the validity of the email
        if (email.isEmpty()) {
            emailEditText.setError("Enter an email address");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if (password.isEmpty()) {
            passwordEditText.setError("Enter a password");
            passwordEditText.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Enter a password");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Minimum length of a password should be 6");
            passwordEditText.requestFocus();
            confirmPasswordEditText.setError("Minimum length of a password should be 6");
            confirmPasswordEditText.requestFocus();
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
            return;
        }

        if (confirmPassword.length() < 6) {
            passwordEditText.setError("Minimum length of a password should be 6");
            passwordEditText.requestFocus();
            confirmPasswordEditText.setError("Minimum length of a password should be 6");
            confirmPasswordEditText.requestFocus();
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
            return;
        }

        if (!password.equals(confirmPassword)) {
            passwordEditText.setError("Password doesn't match");
            passwordEditText.requestFocus();
            confirmPasswordEditText.setError("Password doesn't match");
            confirmPasswordEditText.requestFocus();
            passwordEditText.setText("");
            confirmPasswordEditText.setText("");
            return;
        }

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    final FirebaseUser mUser = mAuth.getCurrentUser();
                    String userid = mUser.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("userName", username);
                    hashMap.put("fullName", fullName);
                    hashMap.put("email", email);
                    hashMap.put("bio", "");
                    hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/instagram-clone-72428.appspot.com/o/default_user.png?alt=media&token=454dca9b-1ddf-4f1b-a085-1cbe0ea84ae8");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                //set display name
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                mUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "Your registration is completed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // If sign in fails, display a message to the user.
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "User is already Registered.", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
