package ru.ytken.libraryapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import ru.ytken.libraryapp.recycler.RecAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    Toolbar toolbar;
    Spinner spinner;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item,
                getResources().getStringArray(R.array.options));
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(spinAdapter);
        spinner.setSelection(spinAdapter.getPosition(getResources().getStringArray(R.array.options)[0]));

        recycler = findViewById(R.id.recycler_main);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        RecAdapter adapter = new RecAdapter();
        recycler.setAdapter(adapter);


    }
}