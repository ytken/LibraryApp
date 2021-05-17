package ru.ytken.libraryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StoryFirstActivity extends FragmentActivity implements View.OnClickListener, DialogContinueGame.Removable {

    String line = "";
    int picId, textId;
    ImageView imageView, backgr;
    TextView wordsView;
    BufferedReader reader;
    static SharedPreferences sPref; static SharedPreferences.Editor editor;
    int countLine = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_first);
        sPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.prefs_first_story_name), MODE_PRIVATE);
        editor = sPref.edit();

        backgr = findViewById(R.id.imageBackgr);
        backgr.setOnClickListener(this);

        imageView = findViewById(R.id.imageWords);
        imageView.setOnClickListener(this);
        imageView.setBackgroundResource(R.color.transparent);
        //imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.words_author, null));

        String sex = getIntent().getStringExtra("sex");
        switch (sex) {
            case "M": textId = R.raw.man_author_1; break;
            case "W": textId = R.raw.woman_author_1; break;
            default: break;
        }

        wordsView = findViewById(R.id.textWords);
        reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(textId)));

        countLine = sPref.getInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
        backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black_screen, null));

        if (countLine > 1){
            for (int i = 0; i < countLine; i++) {
                try {
                    line = reader.readLine();
                    Log.d("listtext", line);
                    if (line.contains("Image"))
                        setBackgrIm(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            wordsView.setText(line);
            DialogFragment dialog = new DialogContinueGame(sPref, reader);
            dialog.show(getSupportFragmentManager(), "Dialog Continue Game");
        }
        else {
            countLine = 1;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!line.isEmpty())
                wordsView.setText(line);
        }
    }

    void setBackgrIm(String name) {
        String pic = name.substring(6);
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
        if (picId == 0)
            backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black_screen, null));
        else
            backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), picId, null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageBackgr:
            case R.id.imageWords:
                try {
                    line = reader.readLine();
                    countLine++;
                    if (line.contains("Image")) {
                        setBackgrIm(line);
                        imageView.setVisibility(View.INVISIBLE);
                        wordsView.setVisibility(View.INVISIBLE);
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
        setResult(RESULT_OK);
        super.onDestroy();
    }

    private void RunAnimation(View tv)
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        tv.clearAnimation();
        tv.startAnimation(a);
    }

    @Override
    public void remove() {
        this.finish();
        setResult(RESULT_CANCELED);
    }
}
