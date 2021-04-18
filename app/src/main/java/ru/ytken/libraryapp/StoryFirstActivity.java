package ru.ytken.libraryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StoryFirstActivity extends AppCompatActivity implements View.OnClickListener {

    String line = "";
    int picId, textId;
    View view;
    Button refreshPrefs;
    ImageView imageView, backgr;
    TextView wordsView;
    BufferedReader reader;
    InputStream inputStream;
    SharedPreferences sPref; SharedPreferences.Editor editor;
    int countLine = 0;

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
        backgr.setOnClickListener(this);

        String sex = getIntent().getStringExtra("sex");
        if (sex.equals("M"))
            textId = R.raw.man_author_1;
        else
            textId = R.raw.woman_author_1;

        inputStream = getResources().openRawResource(textId);
        wordsView = findViewById(R.id.textWords);
        reader = new BufferedReader(new InputStreamReader(inputStream));
        countLine = sPref.getInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
        for (int i = 0; i < countLine; i++) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), 0);
        //editor.apply();
        picId = sPref.getInt(getResources().getString(R.string.TAG_BACKGROUND), 0);
        if (picId == 0)
            backgr.setImageDrawable(getResources().getDrawable(R.drawable.black_screen));
        else
            backgr.setImageDrawable(getResources().getDrawable(picId));
        if (!line.isEmpty())
            wordsView.setText(line);
        refreshPrefs = findViewById(R.id.buttonRefresh);
        refreshPrefs.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonRefresh:
                editor.putString(getResources().getString(R.string.TAG_CHAR_NAME), "");
                editor.putInt(getResources().getString(R.string.TAG_PERS_AGE), -1);
                editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
                editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), 0);
                editor.apply();
                Toast.makeText(StoryFirstActivity.this, "Данные обновлены", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageBackgr:
            case R.id.imageWords:
                try {
                    line = reader.readLine();
                    countLine++;
                    if (line.contains("Image")) {
                        String pic = line.substring(6);
                        switch (pic) {
                            case "house": picId = R.drawable.main_pers_house; break;
                            case "purple": picId = R.drawable.purple_flash; break;
                            case "black": picId = R.drawable.black_screen; break;
                            case "white": picId = R.drawable.while_flash; break;
                            case "smoke": picId = R.drawable.smoke; break;
                            case "workshop": picId = R.drawable.royal_workshop; break;
                            case "bomb": picId = R.drawable.bomb_town; break;
                            default: picId = 0; break;
                        }
                        imageView.setVisibility(View.INVISIBLE);
                        wordsView.setVisibility(View.INVISIBLE);

                        if (picId == 0)
                            backgr.setImageDrawable(getResources().getDrawable(R.drawable.black_screen));
                        else
                            backgr.setImageDrawable(getResources().getDrawable(picId));
                        line = reader.readLine();

                        RunAnimation(backgr);
                        countLine++;
                    }
                    else {
                        if (imageView.getVisibility() == View.INVISIBLE) {
                            imageView.setVisibility(View.VISIBLE);
                            RunAnimation(imageView);
                            wordsView.setVisibility(View.VISIBLE);
                        }

                        wordsView.setText(line);
                        RunAnimation(wordsView);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE),countLine);
        editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), picId);
        editor.apply();
        super.onDestroy();
    }

    private void RunAnimation(View tv)
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        tv.clearAnimation();
        tv.startAnimation(a);
    }

}
