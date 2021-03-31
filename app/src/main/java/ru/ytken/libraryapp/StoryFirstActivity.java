package ru.ytken.libraryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StoryFirstActivity extends AppCompatActivity {

    TextView textView;
    Button refreshButton;
    SharedPreferences sPref; SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_first);
        sPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.prefs_first_story_name), MODE_PRIVATE);
        editor = sPref.edit();

        textView = findViewById(R.id.textTest);
        textView.setText(getIntent().getStringExtra("name") + " " + getIntent().getStringExtra("sex"));

        refreshButton = findViewById(R.id.buttonRefresh);
        refreshButton.setOnClickListener(v -> {
            editor.putString(getResources().getString(R.string.TAG_CHAR_NAME), "");
            editor.putInt(getResources().getString(R.string.TAG_PERS_AGE), -1);
            editor.apply();
        });
    }
}
