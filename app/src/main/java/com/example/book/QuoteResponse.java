package com.example.book;

import com.google.gson.annotations.SerializedName;

public class QuoteResponse {
    @SerializedName("q")
    public String text;
    @SerializedName("a")
    public String author;
}