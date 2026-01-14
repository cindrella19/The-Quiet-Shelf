package com.example.book;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WantToReadAdapter extends RecyclerView.Adapter<WantToReadAdapter.ViewHolder> {

    private List<WantToReadBook> books;
    private OnLongClickListener longClickListener;

    public interface OnLongClickListener {
        void onLongClick(WantToReadBook book);
    }

    public WantToReadAdapter(List<WantToReadBook> books, OnLongClickListener longClickListener) {
        this.books = books;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_want_to_read, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WantToReadBook book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onLongClick(book);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateList(List<WantToReadBook> newBooks) {
        this.books = newBooks;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, author;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.wantTitle);
            author = itemView.findViewById(R.id.wantAuthor);
        }
    }
}