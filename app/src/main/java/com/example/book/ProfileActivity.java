package com.example.book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private EditText profileName, profileEmail;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applySettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        MaterialButton btnSave = findViewById(R.id.btnSaveProfile);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        prefs = getSharedPreferences("ProfilePrefs", MODE_PRIVATE);

        // Load cached profile (fast, no crash risk)
        loadFromCache();

        btnSave.setOnClickListener(v -> saveProfile());
        btnLogout.setOnClickListener(v -> logout());
    }

    // ===============================
    // LOAD FROM LOCAL CACHE
    // ===============================
    private void loadFromCache() {
        profileName.setText(prefs.getString("displayName", ""));
        profileEmail.setText(prefs.getString("email", ""));
    }

    // ===============================
    // SAVE PROFILE (LOCAL + FIRESTORE)
    // ===============================
    private void saveProfile() {
        String newName = profileName.getText().toString().trim();
        String newEmail = profileEmail.getText().toString().trim();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save locally
        prefs.edit()
                .putString("displayName", newName)
                .putString("email", newEmail)
                .apply();

        // Save to Firestore via helper (CORRECT way)
        FirebaseHelper.updateProfile(
                newName,
                newEmail,
                () -> Toast.makeText(
                        ProfileActivity.this,
                        "Profile updated",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    // ===============================
    // LOGOUT
    // ===============================
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
