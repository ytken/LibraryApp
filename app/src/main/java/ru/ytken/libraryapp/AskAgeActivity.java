package ru.ytken.libraryapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Process;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import ru.ytken.libraryapp.dialogs.MessageDialog;

public class AskAgeActivity extends AppCompatActivity {

    private EditText editTextSetBirthdate;
    private TextView mInfoTextView;
    private Integer day, month, year;
    SharedPreferences sPref; SharedPreferences.Editor editor;
    Integer age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_age_check);

        sPref = getSharedPreferences(getResources().getString(R.string.prefs_first_story_name),MODE_PRIVATE);
        editor = sPref.edit();
        mInfoTextView = findViewById(R.id.textAgeDate);
        Calendar today = Calendar.getInstance();
        mInfoTextView.setText("Введите вашу дату рождения:");
        day = today.get(Calendar.DAY_OF_MONTH); month = today.get(Calendar.MONTH)+1; year = today.get(Calendar.YEAR)-18;

        editTextSetBirthdate = findViewById(R.id.editTextSetDate);
        editTextSetBirthdate.addTextChangedListener(new MaskWatcher("##.##.####"));

        Button changingDateButton = findViewById(R.id.buttonSetDate);
        changingDateButton.setOnClickListener(v -> {
            String dateBirth = editTextSetBirthdate.getText().toString();
            if(Pattern.matches("\\d{2}\\.\\d{2}\\.\\d{4}", dateBirth)) {
                day = Integer.parseInt(dateBirth.substring(0,2));
                month = Integer.parseInt(dateBirth.substring(3,5));
                year = Integer.parseInt(dateBirth.substring(6));
                if ((year >= 1900) && (year <= today.get(Calendar.YEAR)) && (month <= 12) && (day <= 31)) {
                    age = getAge(year, month, day);
                    if ((month == today.get(Calendar.MONTH)+1) && (day < today.get(Calendar.DAY_OF_MONTH)) || (month == today.get(Calendar.MONTH)))
                        age ++;
                    if (age >= 18) {
                        editor.putInt(getResources().getString(R.string.TAG_PERS_AGE), age);
                        editor.apply();
                        Intent intent = new Intent(AskAgeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Dialog mesDialog = new MessageDialog(this,
                                android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen,
                                getResources().getString(R.string.message_age));
                        mesDialog.show();
                        editTextSetBirthdate.setText("");
                    }
                }
                else {
                    Toast.makeText(AskAgeActivity.this, "Неверная дата!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(AskAgeActivity.this, "Введите корректную дату", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Integer getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){ age--;}
        Integer ageInt = Integer.valueOf(age);

        return ageInt;
    }
}