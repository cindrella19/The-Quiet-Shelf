package com.example.book;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface QuotesService {
    // Using ZenQuotes API which returns a list with a single quote object
    @GET("api/random")
    Call<List<QuoteResponse>> getRandomQuote();
}