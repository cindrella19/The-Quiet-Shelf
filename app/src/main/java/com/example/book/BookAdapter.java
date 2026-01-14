package com.example.book;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> books;
    private OnBookLongClickListener longClickListener;

    public interface OnBookLongClickListener {
        void onBookLongClick(Book book);
    }

    public BookAdapter(List<Book> books, OnBookLongClickListener longClickListener) {
        this.books = books;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.title.setText(book.getTitle());
        
        // Formatted Vibes
        String vibes = String.format("%s • %s • %s", 
                book.getVibe1(), book.getVibe2(), book.getVibe3());
        holder.vibes.setText(vibes);

        // Single Quote
        holder.quote.setText("“" + book.getQuote() + "”");

        // Rating
        holder.rating.setText("Rating: " + book.getRating() + "/10");

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onBookLongClick(book);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateBooks(List<Book> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, vibes, quote, rating;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitle);
            vibes = itemView.findViewById(R.id.bookVibes);
            quote = itemView.findViewById(R.id.bookQuote);
            rating = itemView.findViewById(R.id.bookRating);
        }
    }
}