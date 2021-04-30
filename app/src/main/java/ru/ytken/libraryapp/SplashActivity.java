package ru.ytken.libraryapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class SplashActivity extends AppCompatActivity {

    private EditText editTextSetBirthdate;
    private TextView mInfoTextView;
    private Integer day, month, year;
    SharedPreferences sPref; SharedPreferences.Editor editor;
    Integer age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_check);

        sPref = getSharedPreferences(getResources().getString(R.string.prefs_first_story_name),MODE_PRIVATE);
        editor = sPref.edit();
        age = sPref.getInt(getResources().getString(R.string.TAG_PERS_AGE), -1);
        if (age>0) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
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
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        DialogFragment dialog = new DialogExit();
                        dialog.show(getSupportFragmentManager(), "Dialog Exit");
                    }
                }
                else {
                    Toast.makeText(SplashActivity.this, "Неверная дата!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(SplashActivity.this, "Введите корректную дату", Toast.LENGTH_SHORT).show();
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

    public static class DialogExit extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            return new AlertDialog.Builder(getContext())
                    .setTitle("Отказ")
                    .setMessage("Вход с 18 лет")
                    .setNeutralButton("Выход", (dialog, which) -> Process.killProcess(Process.myPid()))
                    .create();
        }
    }

}