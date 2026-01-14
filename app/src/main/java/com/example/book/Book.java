package com.example.book;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cloud_id")
    private String cloudId;

    private String title = "";
    private String vibe1 = "";
    private String vibe2 = "";
    private String vibe3 = "";
    private String quote = "";
    private int rating = 0;

    // ✅ Required empty constructor for Room
    public Book() {
    }

    // ✅ Ignore constructor (used by app logic)
    @Ignore
    public Book(String title, String vibe1, String vibe2, String vibe3, String quote, int rating) {
        this.title = title != null ? title : "";
        this.vibe1 = vibe1 != null ? vibe1 : "";
        this.vibe2 = vibe2 != null ? vibe2 : "";
        this.vibe3 = vibe3 != null ? vibe3 : "";
        this.quote = quote != null ? quote : "";
        this.rating = rating;
    }

    // 🔹 ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // 🔹 Cloud ID
    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    // 🔹 Title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    // 🔹 Vibes
    public String getVibe1() {
        return vibe1;
    }

    public void setVibe1(String vibe1) {
        this.vibe1 = vibe1 != null ? vibe1 : "";
    }

    public String getVibe2() {
        return vibe2;
    }

    public void setVibe2(String vibe2) {
        this.vibe2 = vibe2 != null ? vibe2 : "";
    }

    public String getVibe3() {
        return vibe3;
    }

    public void setVibe3(String vibe3) {
        this.vibe3 = vibe3 != null ? vibe3 : "";
    }

    // 🔹 Quote
    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote != null ? quote : "";
    }

    // 🔹 Rating
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
