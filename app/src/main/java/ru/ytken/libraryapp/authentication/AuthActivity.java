package ru.ytken.libraryapp.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ru.ytken.libraryapp.R;

public class AuthActivity extends AppCompatActivity {

    private EditText editLogin, editPassword;
    private Button buttonLogIn;
    private TextView textGoRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mAuth = FirebaseAuth.getInstance();

        editLogin = findViewById(R.id.editTextLogin);
        editPassword = findViewById(R.id.editTextPassword);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        textGoRegister = findViewById(R.id.textViewRegistration);

        buttonLogIn.setOnClickListener(view -> {
            String email = editLogin.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(AuthActivity.this,
                                        "signInWithEmail:success",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(AuthActivity.this,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        textGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}