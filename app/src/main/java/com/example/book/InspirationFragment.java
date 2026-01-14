package com.example.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InspirationFragment extends Fragment {

    private TextView quoteText, authorText;
    private QuotesService quotesService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspiration, container, false);

        quoteText = view.findViewById(R.id.inspirationQuote);
        authorText = view.findViewById(R.id.inspirationAuthor);
        MaterialButton btnRefresh = view.findViewById(R.id.btnRefresh);

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://zenquotes.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            quotesService = retrofit.create(QuotesService.class);
        } catch (Exception ignored) {}

        fetchQuote();

        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(v -> fetchQuote());
        }

        return view;
    }

    private void fetchQuote() {
        if (quotesService == null || quoteText == null) return;
        
        quoteText.setText("Finding words...");
        if (authorText != null) authorText.setText("");
        
        quotesService.getRandomQuote().enqueue(new Callback<List<QuoteResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuoteResponse>> call, @NonNull Response<List<QuoteResponse>> response) {
                if (!isAdded() || getContext() == null) return;
                
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    QuoteResponse q = response.body().get(0);
                    if (quoteText != null) quoteText.setText("“" + q.text + "”");
                    if (authorText != null) authorText.setText("— " + q.author);
                } else {
                    if (quoteText != null) quoteText.setText("The shelf is quiet for now.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<QuoteResponse>> call, @NonNull Throwable t) {
                if (!isAdded() || getContext() == null) return;
                if (quoteText != null) quoteText.setText("Connection lost in the library.");
            }
        });
    }
}