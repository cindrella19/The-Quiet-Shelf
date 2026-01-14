package com.example.book;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {
    // Current Library Books
    @Query("SELECT * FROM books")
    List<Book> getAllBooks();
    @Query("SELECT * FROM books WHERE cloud_id = :cloudId LIMIT 1")
    Book getBookByCloudId(String cloudId);

    @Insert
    void insertBook(Book book);

    @Update
    void updateBook(Book book);

    @Delete
    void deleteBook(Book book);

    // Want to Read Books
    @Query("SELECT * FROM want_to_read_books")
    List<WantToReadBook> getAllWantToReadBooks();

    @Insert
    void insertWantToReadBook(WantToReadBook book);

    @Delete
    void deleteWantToReadBook(WantToReadBook book);
}