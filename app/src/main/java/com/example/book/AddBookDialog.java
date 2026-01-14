package com.example.book;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddBookDialog extends DialogFragment {

    public interface AddBookListener {
        void onBookAdded(Book book);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() == null) return super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_add_book, null);

        EditText titleInput = view.findViewById(R.id.inputTitle);
        EditText vibeInput = view.findViewById(R.id.inputVibe);
        EditText quoteInput = view.findViewById(R.id.inputQuote);
        EditText ratingInput = view.findViewById(R.id.inputRating);

        return new AlertDialog.Builder(requireContext())
                .setTitle("Add a new book")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {

                    String title = titleInput.getText().toString().trim();
                    String vibe = vibeInput.getText().toString().trim();
                    String quote = quoteInput.getText().toString().trim();

                    int rating = 0;
                    try {
                        String rStr = ratingInput.getText().toString().trim();
                        if (!rStr.isEmpty()) {
                            rating = Integer.parseInt(rStr);
                        }
                        if (rating < 0) rating = 0;
                        if (rating > 10) rating = 10;
                    } catch (Exception ignored) {}

                    if (!title.isEmpty()) {
                        String v1 = vibe, v2 = "", v3 = "";
                        if (vibe.contains(",")) {
                            String[] parts = vibe.split(",");
                            if (parts.length >= 1) v1 = parts[0].trim();
                            if (parts.length >= 2) v2 = parts[1].trim();
                            if (parts.length >= 3) v3 = parts[2].trim();
                        } else if (vibe.contains("\n")) {
                            String[] parts = vibe.split("\n");
                            if (parts.length >= 1) v1 = parts[0].trim();
                            if (parts.length >= 2) v2 = parts[1].trim();
                            if (parts.length >= 3) v3 = parts[2].trim();
                        }

                        final Book book = new Book(title, v1, v2, v3, quote, rating);
                        
                        // Safety check for context
                        if (getContext() != null) {
                            AppDatabase.getInstance(getContext()).bookDao().insertBook(book);
                            if (getActivity() instanceof AddBookListener) {
                                ((AddBookListener) getActivity()).onBookAdded(book);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}