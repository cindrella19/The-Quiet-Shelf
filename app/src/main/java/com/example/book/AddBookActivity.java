package com.example.book;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import java.util.List;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AddBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applySettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add to Shelf");
        }

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editVibe1 = findViewById(R.id.editVibe1);
        EditText editVibe2 = findViewById(R.id.editVibe2);
        EditText editVibe3 = findViewById(R.id.editVibe3);
        EditText editQuote = findViewById(R.id.editQuote);
        SeekBar ratingSeekBar = findViewById(R.id.ratingSeekBar);
        MaterialButton btnSave = findViewById(R.id.btnSaveBook);

        btnSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String v1 = editVibe1.getText().toString().trim();
            String v2 = editVibe2.getText().toString().trim();
            String v3 = editVibe3.getText().toString().trim();
            String quote = editQuote.getText().toString().trim();
            int rating = ratingSeekBar.getProgress();

            if (title.isEmpty() || v1.isEmpty() || v2.isEmpty() || v3.isEmpty() || quote.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Book newBook = new Book(title, v1, v2, v3, quote, rating);
            FirebaseHelper.saveBook(newBook);

            // Save locally
            AppDatabase db = AppDatabase.getInstance(this);
            db.bookDao().insertBook(newBook);
            
            // Sync to Firestore
            // We fetch the book again to get the auto-generated Room ID for Firestore doc consistency
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                Map<String, Object> bookMap = new HashMap<>();
                bookMap.put("title", newBook.getTitle());
                bookMap.put("vibe1", newBook.getVibe1());
                bookMap.put("vibe2", newBook.getVibe2());
                bookMap.put("vibe3", newBook.getVibe3());
                bookMap.put("quote", newBook.getQuote());
                bookMap.put("rating", newBook.getRating());
                bookMap.put("createdAt", FieldValue.serverTimestamp());

                firestore.collection("users")
                        .document(user.getUid())
                        .collection("books")
                        .add(bookMap);
            }

            Toast.makeText(this, "Added to your shelf", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}