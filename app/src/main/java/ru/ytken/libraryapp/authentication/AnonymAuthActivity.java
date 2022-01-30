package ru.ytken.libraryapp.authentication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.ytken.libraryapp.R;

public class AnonymAuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String TAG = "SignIn";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonym_auth);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                        Toast.makeText(AnonymAuthActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
        else updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        TextView textView = findViewById(R.id.textViewAnonymUser);
        if (user != null) {
            textView.setText(user.getUid());
            ImageButton imageButton = findViewById(R.id.imageButtonCopyUid);
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager)
                            getSystemService(Context.CLIPBOARD_SERVICE);
                    // Creates a new text clip to put on the clipboard
                    ClipData clip = ClipData.newPlainText("simple text", textView.getText());
                    // Set the clipboard's primary clip.
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(AnonymAuthActivity.this, "ID скопирован!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        else
            textView.setText("No user");
    }
}
