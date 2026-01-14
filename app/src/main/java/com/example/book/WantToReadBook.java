package com.example.book;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "want_to_read_books")
public class WantToReadBook {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String title;
    private String author;

    public WantToReadBook(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}