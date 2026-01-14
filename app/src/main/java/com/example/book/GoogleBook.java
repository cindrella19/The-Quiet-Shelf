package com.example.book;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GoogleBook {
    @SerializedName("items")
    public List<Item> items;

    public static class Item {
        @SerializedName("volumeInfo")
        public VolumeInfo volumeInfo;
    }

    public static class VolumeInfo {
        @SerializedName("title")
        public String title;
        @SerializedName("authors")
        public List<String> authors;
        @SerializedName("description")
        public String description;
        @SerializedName("pageCount")
        public Integer pageCount;
        @SerializedName("imageLinks")
        public ImageLinks imageLinks;
    }

    public static class ImageLinks {
        @SerializedName("thumbnail")
        public String thumbnail;
    }
}