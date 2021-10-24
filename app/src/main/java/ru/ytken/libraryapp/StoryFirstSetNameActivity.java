package ru.ytken.libraryapp;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.ytken.libraryapp.dialogs.AskNameDialog;
import ru.ytken.libraryapp.dialogs.MessageDialog;

public class StoryFirstSetNameActivity extends AppCompatActivity implements AskNameDialog.OnFinalListener {
    ImageButton backButton, buttonWoman, buttonMan;
    Resources res;
    SharedPreferences sPref; SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_name_story_first);
        res = getResources();
        sPref = getSharedPreferences(getResources().getString(R.string.prefs_first_story_name),MODE_PRIVATE);
        editor = sPref.edit();

        backButton = findViewById(R.id.story_button_back);
        backButton.setImageDrawable(res.getDrawable(R.drawable.button_back_exp, null));
        backButton.setOnClickListener(v -> finish());

        buttonWoman = findViewById(R.id.button_woman);
        buttonWoman.setOnClickListener(v -> {
            launchDialog(res.getString(R.string.main_woman_name), "W");
        });


        buttonMan = findViewById(R.id.button_man);
        buttonMan.setOnClickListener(v -> {
            launchDialog(res.getString(R.string.main_man_name), "M");
        });

        Dialog mesDialog = new MessageDialog(this,
                android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen,
                getResources().getString(R.string.message_smoking));
        mesDialog.show();
    }

    private void launchDialog(String name, String sex) {
        Dialog setNameDialog = new AskNameDialog(this,
                android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen,
                name, sex, sPref, res);
        setNameDialog.show();
    }

    @Override
    public void sendSignal() {
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        setResult(RESULT_CANCELED);
        super.onDestroy();
    }
}
