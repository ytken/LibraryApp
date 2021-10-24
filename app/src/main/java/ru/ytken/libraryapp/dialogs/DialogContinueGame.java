package ru.ytken.libraryapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.ytken.libraryapp.R;

public class DialogContinueGame extends Dialog {
    SharedPreferences.Editor editor;
    private Removable removable;
    Resources res;

    public DialogContinueGame(@NonNull Context context, int themeResId, SharedPreferences.Editor editor, Resources res) {
        super(context, themeResId);
        this.editor = editor;
        removable = (Removable) context;
        this.res = res;
    }

    public interface Removable {
        void remove();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continue_game_dialog);
        Button buttonYes = findViewById(R.id.buttonContinueGameYes);
        buttonYes.setOnClickListener(view -> dismiss());
        Button buttonNo = findViewById(R.id.buttonContinueGameNo);
        buttonNo.setOnClickListener(view -> {
            editor.putInt(res.getString(R.string.TAG_COUNT_LINE), 0);
            editor.putInt(res.getString(R.string.TAG_COUNT_DIALOG_CLICK), 0);
            editor.putInt(res.getString(R.string.TAG_BACKGROUND), 0);
            editor.putInt(res.getString(R.string.TAG_COUNT_DIALOG_NUM), 0);
            editor.putString(res.getString(R.string.TAG_CHAR_NAME), "");

            editor.apply();
            removable.remove();
        });
    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
