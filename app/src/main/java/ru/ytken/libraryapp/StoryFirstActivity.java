package ru.ytken.libraryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StoryFirstActivity extends AppCompatActivity implements View.OnClickListener {

    View view;
    Button refreshPrefs;
    ImageView imageView, backgr;
    TextView wordsView;
    BufferedReader reader;
    InputStream inputStream;
    SharedPreferences sPref; SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_first);

        view = findViewById(android.R.id.content).getRootView();
        sPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.prefs_first_story_name), MODE_PRIVATE);
        editor = sPref.edit();
        imageView = findViewById(R.id.imageWords);
        imageView.setOnClickListener(this);
        imageView.setBackgroundResource(R.color.transparent);

        backgr = findViewById(R.id.imageBackgr);
        /*ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("myWords", "were in global listener");
                try {
                    String line = reader.readLine();
                    wordsView.setText(line);
                    Log.d("myWords", line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/

        inputStream = getResources().openRawResource(R.raw.man_author_1);
        wordsView = findViewById(R.id.textWords);
        reader = new BufferedReader(new InputStreamReader(inputStream));

        refreshPrefs = findViewById(R.id.buttonRefresh);
        refreshPrefs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRefresh:
                editor.putString(getResources().getString(R.string.TAG_CHAR_NAME), "");
                editor.putInt(getResources().getString(R.string.TAG_PERS_AGE), -1);
                editor.apply();
                break;

            case R.id.imageWords:
                try {
                    String line = reader.readLine();
                    int picId = 0;
                    if (line.contains("Image")) {
                        String pic = line.substring(6);
                        switch (pic) {
                            case "house": picId = R.drawable.main_pers_house; break;
                            case "purple": picId = R.drawable.purple_flash; break;
                            case "black": picId = R.drawable.black_screen; break;
                            case "white": picId = R.drawable.while_flash; break;
                            case "smoke": picId = R.drawable.smoke; break;
                            case "workshop": picId = R.drawable.royal_workshop; break;
                            default: picId = 0; break;
                        }
                        if (picId == 0)
                            backgr.setColorFilter(getResources().getColor(R.color.primaryWhite));
                        else
                            backgr.setImageDrawable(getResources().getDrawable(picId));
                    }
                    else {
                        wordsView.setText(line);
                        Log.d("myWords", line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }
}
