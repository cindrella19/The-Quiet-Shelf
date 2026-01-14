package com.example.book;

import android.content.Intent;
import android.os.Bundle;
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

public class SignUpActivity extends AppCompatActivity {

    private EditText emailEdit, passwordEdit;
    private MaterialButton btnSignUp;
    private TextView btnBackToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applySettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        emailEdit = findViewById(R.id.signUpEmail);
        passwordEdit = findViewById(R.id.signUpPassword);
        btnSignUp = findViewById(R.id.btnDoSignUp);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnSignUp.setOnClickListener(v -> handleSignUp());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void handleSignUp() {
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        // 🔥 WRITE TO FIRESTORE HERE
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> profile = new HashMap<>();
                            profile.put("email", user.getEmail());
                            profile.put("createdAt", FieldValue.serverTimestamp());

                            db.collection("users")
                                    .document(user.getUid())
                                    .collection("profile")
                                    .document("info")
                                    .set(profile);
                        }

                        Toast.makeText(SignUpActivity.this, "Welcome to the Shelf", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(
                                SignUpActivity.this,
                                "Sign Up Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
