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
    String nameChar;
    private OnFinalListener onFinalListener;

    public AskNameDialog(@NonNull Context context, int themeResId, String name, SharedPreferences sPref, Resources res) {
        super(context, themeResId);
        this.sPref = sPref;
        editor = sPref.edit();
        this.res = res;
        nameChar = name;
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
