package ru.ytken.libraryapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class SplashActivity extends AppCompatActivity {

    private DatePicker mDatePicker;
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
        mDatePicker = findViewById(R.id.datePicker);
        mDatePicker.setCalendarViewShown(false);
        mDatePicker.setSpinnersShown(true);
        Calendar today = Calendar.getInstance();

        mInfoTextView.setText("Ваша дата рождения:\n" + new StringBuilder()
                .append(today.get(Calendar.DAY_OF_MONTH)).append(".")
                .append(today.get(Calendar.MONTH)+1).append(".")
                .append(today.get(Calendar.YEAR)-18));
        day = today.get(Calendar.DAY_OF_MONTH); month = today.get(Calendar.MONTH)+1; year = today.get(Calendar.YEAR)-18;

        mDatePicker.init(year, month-1, day, (view, year, monthOfYear, dayOfMonth) -> {
                    day = mDatePicker.getDayOfMonth();
                    month = mDatePicker.getMonth() + 1;
                    this.year = mDatePicker.getYear();
                    mInfoTextView.setText("Ваша дата рождения:\n" + new StringBuilder()
                            .append(day).append(".")
                            .append(month).append(".")
                            .append(year));
                });

        Button changingDateButton = findViewById(R.id.buttonSetDate);
        changingDateButton.setOnClickListener(v -> {
            age = getAge(year, month, day);
            if ((month == today.get(Calendar.MONTH)+1) && (day < today.get(Calendar.DAY_OF_MONTH)))
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
        });

    }

    private Integer getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){ age--;}
        Integer ageInt = new Integer(age);

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