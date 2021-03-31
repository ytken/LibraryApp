package ru.ytken.libraryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class StoryFirstSetNameActivity extends AppCompatActivity implements AskNameDialog.OnFinalListener {
    ImageView imageBackground;
    ImageButton backButton, buttonWoman, buttonMan;
    Resources res;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_name_story_first);
        res = getResources();

        backButton = findViewById(R.id.story_button_back);
        backButton.setImageDrawable(res.getDrawable(R.drawable.button_back));
        backButton.setOnClickListener(v -> finish());

        imageBackground = findViewById(R.id.story_image);
        imageBackground.setImageDrawable(res.getDrawable(R.drawable.char_choice));

        buttonWoman = findViewById(R.id.button_woman);
        buttonWoman.setImageDrawable(res.getDrawable(R.drawable.words_left_pers));
        buttonWoman.setOnClickListener(v -> {
            launchDialog(res.getString(R.string.main_woman_name), "W");
        });


        buttonMan = findViewById(R.id.button_man);
        buttonMan.setImageDrawable(res.getDrawable(R.drawable.words_right_pers));
        buttonMan.setOnClickListener(v -> {
            launchDialog(res.getString(R.string.main_man_name), "M");
        });
    }

    private void launchDialog(String name, String sex) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("sex", sex);
        AskNameDialog dialog = new AskNameDialog(getApplicationContext().getSharedPreferences(res.getString(R.string.prefs_first_story_name), MODE_PRIVATE));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), res.getString(R.string.TAG_DIALOG));
    }

    @Override
    public void sendSignal() {
        this.finish();
    }
}
