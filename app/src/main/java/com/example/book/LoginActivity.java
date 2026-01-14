package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText emailEdit, passwordEdit;
    private MaterialButton btnLogin;
    private TextView btnSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applySettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEdit = findViewById(R.id.loginEmail);
        passwordEdit = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(v -> handleLogin());

        btnSignUp.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class))
        );
    }

    private void handleLogin() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user == null) {
                            Toast.makeText(this, "User is null after login", Toast.LENGTH_LONG).show();
                            return;
                        }

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        Map<String, Object> profile = new HashMap<>();
                        profile.put("email", user.getEmail());
                        profile.put("lastLogin", FieldValue.serverTimestamp());

                        db.collection("users")
                                .document(user.getUid())
                                .collection("profile")
                                .document("info")
                                .set(profile)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Firestore WRITE SUCCESS");
                                    Toast.makeText(this, "Firestore write SUCCESS", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Firestore WRITE FAILED", e);
                                    Toast.makeText(this, "Firestore write FAILED: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(
                                LoginActivity.this,
                                "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
