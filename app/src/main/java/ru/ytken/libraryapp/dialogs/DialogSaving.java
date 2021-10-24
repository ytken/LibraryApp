package ru.ytken.libraryapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import ru.ytken.libraryapp.R;

public class DialogSaving extends Dialog {
    SharedPreferences.Editor editor;
    Resources res;

    public DialogSaving(@NonNull Context context, int themeResId, SharedPreferences.Editor editor, Resources res) {
        super(context, themeResId);
        this.editor = editor;
        this.res = res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_game_dialog);
        Button buttonYes = findViewById(R.id.buttonSaveGameYes);
        buttonYes.setOnClickListener(view -> dismiss());
        Button buttonNo = findViewById(R.id.buttonSaveGameNo);
        buttonNo.setOnClickListener(view -> {
            editor.putInt(res.getString(R.string.TAG_COUNT_LINE),0);
            editor.putInt(res.getString(R.string.TAG_COUNT_DIALOG_CLICK), 0);
            editor.putInt(res.getString(R.string.TAG_COUNT_DIALOG_NUM), 0);
            editor.putInt(res.getString(R.string.TAG_BACKGROUND),0);
            editor.apply();
            dismiss();
        });
    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
