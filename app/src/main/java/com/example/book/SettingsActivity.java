package com.example.book;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply settings before layout
        ThemeHelper.applySettings(this);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("Settings", MODE_PRIVATE);

        SwitchMaterial themeSwitch = findViewById(R.id.themeSwitch);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            // Re-apply immediately
            ThemeHelper.applySettings(this);
        });

        RadioGroup textSizeGroup = findViewById(R.id.textSizeGroup);
        String currentSize = prefs.getString("text_size", "Medium");
        if (currentSize.equals("Small")) textSizeGroup.check(R.id.sizeSmall);
        else if (currentSize.equals("Large")) textSizeGroup.check(R.id.sizeLarge);
        else textSizeGroup.check(R.id.sizeMedium);

        textSizeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String size = "Medium";
            if (checkedId == R.id.sizeSmall) size = "Small";
            else if (checkedId == R.id.sizeLarge) size = "Large";
            prefs.edit().putString("text_size", size).apply();
            
            // Text size requires activity restart to apply fontScale globally
            recreate();
        });
    }
}