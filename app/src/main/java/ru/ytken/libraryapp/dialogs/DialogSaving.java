package ru.ytken.libraryapp.dialogs;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.ytken.libraryapp.R;

public class DialogSaving extends DialogFragment {
    SharedPreferences.Editor editor;

    public DialogSaving(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle("Сохранение")
                .setMessage("Сохранить текущий сеанс?")
                .setPositiveButton("Да", (dialog, which) -> {
                })
                .setNegativeButton("Нет", (dialog, which) -> {
                    editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE),0);
                    editor.putInt(getResources().getString(R.string.TAG_COUNT_DIALOG_CLICK), 0);
                    editor.putInt(getResources().getString(R.string.TAG_COUNT_DIALOG_NUM), 0);
                    editor.putInt(getResources().getString(R.string.TAG_BACKGROUND),0);
                    editor.apply();
                })
                .create();
    }
}
