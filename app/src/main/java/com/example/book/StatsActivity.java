package com.example.book;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeHelper.applySettings(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView totalBooksText = findViewById(R.id.statTotalBooks);
        TextView avgRatingText = findViewById(R.id.statAvgRating);

        // Room allows main thread queries here as configured in AppDatabase
        List<Book> books = AppDatabase.getInstance(this).bookDao().getAllBooks();
        
        int total = books.size();
        double sum = 0;
        for (Book b : books) {
            sum += b.getRating();
        }
        double avg = total > 0 ? sum / total : 0.0;

        totalBooksText.setText(String.valueOf(total));
        avgRatingText.setText(String.format("%.1f", avg));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}