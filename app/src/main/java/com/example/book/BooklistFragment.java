package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class BooklistFragment extends Fragment {

    private RecyclerView recyclerView;
    private WantToReadAdapter adapter;
    private List<WantToReadBook> bookList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booklist, container, false);

        recyclerView = view.findViewById(R.id.booklistRecyclerView);
        // Changed to LinearLayoutManager for a list feel, different from Home's Grid
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshList();

        View fab = view.findViewById(R.id.fabAddBook);
        if (fab != null) {
            fab.setOnClickListener(v -> showAddWantToReadDialog());
        }

        return view;
    }

    private void showAddWantToReadDialog() {
        if (getContext() == null) return;

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText titleInput = new EditText(getContext());
        titleInput.setHint("Book Title");
        layout.addView(titleInput);

        final EditText authorInput = new EditText(getContext());
        authorInput.setHint("Author Name");
        layout.addView(authorInput);

        new AlertDialog.Builder(getContext())
                .setTitle("Add to Reading List")
                .setView(layout)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = titleInput.getText().toString().trim();
                    String author = authorInput.getText().toString().trim();
                    if (!title.isEmpty() && !author.isEmpty()) {
                        WantToReadBook newBook = new WantToReadBook(title, author);
                        AppDatabase.getInstance(getContext()).bookDao().insertWantToReadBook(newBook);
                        refreshList();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        if (getContext() == null || recyclerView == null) return;
        
        bookList = AppDatabase.getInstance(getContext()).bookDao().getAllWantToReadBooks();
        if (adapter == null) {
            adapter = new WantToReadAdapter(bookList, book -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Remove from list?")
                        .setMessage(book.getTitle())
                        .setPositiveButton("Yes", (d, w) -> {
                            AppDatabase.getInstance(getContext()).bookDao().deleteWantToReadBook(book);
                            refreshList();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(bookList);
        }
    }
}