package ru.ytken.libraryapp;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DialogContinueGame extends DialogFragment {
    int countLine, picId;
    String line, pic;
    BufferedReader reader;
    SharedPreferences sPref; SharedPreferences.Editor editor;
    ImageView backgr;
    TextView wordsView;

    public DialogContinueGame(SharedPreferences sPref, BufferedReader reader, ImageView backgr, TextView wordsView) {
        this.sPref = sPref;
        this.backgr = backgr;
        this.wordsView = wordsView;
        this.reader = reader;
        editor = sPref.edit();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        StoryFirstActivity activity = (StoryFirstActivity) getActivity();
        return new AlertDialog.Builder(getContext())
                .setTitle("Отказ")
                .setMessage("Продолжить последнюю игру?")
                .setPositiveButton("Да", (dialog, which) -> {
                    line = ""; pic = "";
                    countLine = sPref.getInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
                    for (int i = 0; i < countLine; i++) {
                        try {
                            line = reader.readLine();
                            Log.d("listtext", line);
                            if (line.contains("Image"))
                                pic = line.substring(6);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), 0);
                    //editor.apply();
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
                    if (!line.isEmpty())
                        wordsView.setText(line);
                    activity.setCountLine(countLine);
                })
                .setNegativeButton("Начать новую", (dialog, which) -> {
                    editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
                    editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), 0);
                    editor.apply();
                    countLine = 1;
                    backgr.setImageDrawable(getResources().getDrawable(R.drawable.black_screen));
                    try {
                        line = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!line.isEmpty())
                        wordsView.setText(line);
                    activity.setCountLine(countLine);
                })
                .create();
    }
}