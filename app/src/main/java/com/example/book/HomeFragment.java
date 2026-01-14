package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            refreshList();
        }

        // Add Book now goes to AddBookActivity (Manual Entry)
        View fab = view.findViewById(R.id.fabAddBookHome);
        if (fab != null) {
            fab.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddBookActivity.class)));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList(); // Update list when returning from AddBookActivity
    }

    private void refreshList() {
        if (getContext() == null || recyclerView == null) return;
        bookList = AppDatabase.getInstance(getContext()).bookDao().getAllBooks();
        if (adapter == null) {
            adapter = new BookAdapter(bookList, book -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Remove \"" + book.getTitle() + "\"?")
                        .setPositiveButton("Yes", (d, w) -> {
                            AppDatabase.getInstance(getContext()).bookDao().deleteBook(book);
                            refreshList();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateBooks(bookList);
        }
    }
}