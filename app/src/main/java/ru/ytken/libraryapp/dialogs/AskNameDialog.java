package ru.ytken.libraryapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.lang.ref.WeakReference;

import ru.ytken.libraryapp.R;

public class AskNameDialog extends Dialog {
    SharedPreferences sPref; SharedPreferences.Editor editor;
    Resources res;
    String nameChar, sexChar;
    private OnFinalListener onFinalListener;

    public AskNameDialog(@NonNull Context context, int themeResId, String name, String sex, SharedPreferences sPref, Resources res) {
        super(context, themeResId);
        this.sPref = sPref;
        editor = sPref.edit();
        this.res = res;
        nameChar = name; sexChar = sex;
        onFinalListener = (OnFinalListener) context;
    }

    public interface OnFinalListener {
        void sendSignal();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setname_dialog_fragment);

        EditText editName = findViewById(R.id.editNameChar);
        editName.setText(nameChar);
        Button buttonSetName = findViewById(R.id.buttonSetCharName);
        buttonSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(res.getString(R.string.TAG_CHAR_SEX), sexChar);
                editor.putInt(res.getString(R.string.TAG_COUNT_LINE), 0);
                editor.putInt(res.getString(R.string.TAG_COUNT_DIALOG_CLICK),0);
                editor.putInt(res.getString(R.string.TAG_COUNT_DIALOG_NUM), 0);
                editor.putInt(res.getString(R.string.TAG_BACKGROUND), 0);
                Log.d("statesF", "setting states to 0");
                editor.putInt(res.getString(R.string.STATE_COURAGE), 0);
                editor.putInt(res.getString(R.string.STATE_RESISTANCE), 0);
                editor.putInt(res.getString(R.string.STATE_DETERMINATION), 0);
                editor.putInt(res.getString(R.string.STATE_ATTENTION), 0);
                editor.putInt(res.getString(R.string.TAG_ST_SEB_TRUST), 0);

                editor.putString(res.getString(R.string.TAG_CHAR_NAME), editName.getText().toString());

                editor.apply();
                onFinalListener.sendSignal();
            }
        });
        Button buttonCancelName = findViewById(R.id.buttonCancelCharName);
        buttonCancelName.setOnClickListener(view -> dismiss());
    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

}
