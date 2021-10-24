package ru.ytken.libraryapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ru.ytken.libraryapp.R;

public class MessageDialog extends Dialog {
    private String mesToShow;

    public MessageDialog(@NonNull Context context, int themeResId, String mes) {
        super(context, themeResId);
        mesToShow = mes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_dialog_fragment);
        TextView message = findViewById(R.id.messageInMessageDialog);
        message.setText(mesToShow);
        Button buttonDismiss = findViewById(R.id.buttonDismissInMessageDialog);
        buttonDismiss.setOnClickListener(view1 -> dismiss());
    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
