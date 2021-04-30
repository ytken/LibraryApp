package ru.ytken.libraryapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.lang.ref.WeakReference;

public class AskNameDialog extends DialogFragment {
    private EditText editName;
    SharedPreferences sPref; SharedPreferences.Editor editor;


    public AskNameDialog(SharedPreferences sPref) {
        this.sPref = sPref;
        editor = sPref.edit();
    }

    public interface OnFinalListener {
        void sendSignal();
    }

    private OnFinalListener onFinalListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder createProjectAlert = new AlertDialog.Builder(getActivity());
        createProjectAlert.setTitle(getResources().getString(R.string.askforname));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.setname_dialog_fragment, null);
        editor.putString(getResources().getString(R.string.TAG_CHAR_SEX), getArguments().getString("sex"));
        createProjectAlert.setView(view)
                .setPositiveButton(R.string.setname, (dialog, id) -> {
                    editor.putString(getResources().getString(R.string.TAG_CHAR_NAME), editName.getText().toString());
                    editor.apply();

                    onFinalListener.sendSignal();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {

                });
        editName = view.findViewById(R.id.char_name);
        editName.setText(getArguments().getString("name"));

        return createProjectAlert.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onFinalListener = (OnFinalListener) getActivity();
    }
}
