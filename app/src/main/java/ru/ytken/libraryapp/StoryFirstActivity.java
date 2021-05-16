package ru.ytken.libraryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StoryFirstActivity extends AppCompatActivity implements View.OnClickListener {

    String line = "";
    int picId, textId;
    View view;
    ImageView imageView, backgr;
    TextView wordsView;
    BufferedReader reader;
    InputStream inputStream;
    static SharedPreferences sPref; static SharedPreferences.Editor editor;
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

        wordsView = findViewById(R.id.textWords);
        reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(textId)));
        if (sPref.getInt(getResources().getString(R.string.TAG_COUNT_LINE), 0) > 1){
            DialogFragment dialog = new DialogContinueGame(sPref, reader, backgr, wordsView);
            dialog.show(getSupportFragmentManager(), "Dialog Continue Game");
        }
        else {
            countLine = 1;
            backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.black_screen, null));
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!line.isEmpty())
                wordsView.setText(line);
        }
    }

    public void setCountLine(int countLine) { this.countLine = countLine; }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                            backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black_screen, null));
                        else
                            backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), picId, null));
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
