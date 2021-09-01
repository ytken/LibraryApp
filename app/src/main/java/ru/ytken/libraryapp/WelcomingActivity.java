package ru.ytken.libraryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomingActivity extends AppCompatActivity {
    SharedPreferences sPref; SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SystemClock.sleep(1000);
        sPref = getSharedPreferences(getResources().getString(R.string.prefs_first_story_name),MODE_PRIVATE);
        editor = sPref.edit();
        int age = sPref.getInt(getResources().getString(R.string.TAG_PERS_AGE), -1);
        Intent intent = age > 0 ? new Intent(WelcomingActivity.this, MainActivity.class)
                : new Intent(WelcomingActivity.this, AskAgeActivity.class);
        startActivity(intent);
    }
}
