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
                name, sPref, res);
        setNameDialog.show();

        editor.putString(getResources().getString(R.string.TAG_CHAR_SEX), sex);
        editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
        editor.putInt(getResources().getString(R.string.TAG_COUNT_DIALOG_CLICK),0);
        editor.putInt(getResources().getString(R.string.TAG_COUNT_DIALOG_NUM), 0);
        editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), 0);
        Log.d("statesF", "setting states to 0");
        editor.putInt(getResources().getString(R.string.STATE_COURAGE), 0);
        editor.putInt(getResources().getString(R.string.STATE_RESISTANCE), 0);
        editor.putInt(getResources().getString(R.string.STATE_DETERMINATION), 0);
        editor.putInt(getResources().getString(R.string.STATE_ATTENTION), 0);
        editor.putInt(getResources().getString(R.string.TAG_ST_SEB_TRUST), 0);
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
