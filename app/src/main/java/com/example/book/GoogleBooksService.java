package com.example.book;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleBooksService {
    @GET("volumes")
    Call<GoogleBook> searchBooks(@Query("q") String query);
}