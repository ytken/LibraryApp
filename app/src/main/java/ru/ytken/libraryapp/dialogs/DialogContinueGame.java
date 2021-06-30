package ru.ytken.libraryapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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

import ru.ytken.libraryapp.R;

public class DialogContinueGame extends DialogFragment {
    SharedPreferences sPref; SharedPreferences.Editor editor;
    private Removable removable;

    public DialogContinueGame(SharedPreferences sPref) {
        this.sPref = sPref;
        editor = sPref.edit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        removable = (Removable) context;
    }

    public interface Removable {
        void remove();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Отказ")
                .setMessage("Продолжить последнюю игру?")
                .setPositiveButton("Да", (dialog, which) -> {
                })
                .setNegativeButton("Начать новую", (dialog, which) -> {
                    editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
                    editor.putInt(getResources().getString(R.string.TAG_COUNT_DIALOG_LINE), 0);
                    editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), 0);
                    editor.putString(getResources().getString(R.string.TAG_CHAR_NAME), "");
                    editor.apply();
                    removable.remove();
                })
                .create();
    }
}
