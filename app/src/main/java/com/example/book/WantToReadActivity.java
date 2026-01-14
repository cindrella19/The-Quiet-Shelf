package com.example.book;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WantToReadActivity extends AppCompatActivity {

    LinearLayout container;
    ArrayList<String> wantBooks = new ArrayList<>();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_want_to_read);

        container = findViewById(R.id.wantListContainer);
        TextView addBtn = findViewById(R.id.addWantBook);

        prefs = getSharedPreferences("WantToReadPrefs", MODE_PRIVATE);

        loadBooks();

        addBtn.setOnClickListener(v -> showAddDialog());
    }

    // ---------------- ADD BOOK ----------------

    private void showAddDialog() {
        EditText input = new EditText(this);
        input.setHint("Book name");
        input.setTextSize(16);
        input.setPadding(30, 20, 30, 20);

        new AlertDialog.Builder(this)
                .setTitle("Add book to read")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String text = input.getText().toString().trim();
                    if (!text.isEmpty()) {
                        wantBooks.add(text);
                        saveBooks();
                        addView(text);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ---------------- UI ----------------

    private void addView(String text) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.item_want_book, container, false);

        TextView tv = view.findViewById(R.id.wantBookText);
        tv.setText(text);

        view.setOnLongClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage("Remove \"" + text + "\"?")
                    .setPositiveButton("Yes", (d, w) -> {
                        container.removeView(view);
                        wantBooks.remove(text);
                        saveBooks();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        });

        container.addView(view);
    }

    // ---------------- STORAGE ----------------

    private void saveBooks() {
        Set<String> set = new HashSet<>(wantBooks);
        prefs.edit().putStringSet("books", set).apply();
    }

    private void loadBooks() {
        Set<String> set = prefs.getStringSet("books", new HashSet<>());
        wantBooks.clear();
        wantBooks.addAll(set);

        for (String book : wantBooks) {
            addView(book);
        }
    }
}
