package ru.ytken.libraryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import ru.ytken.libraryapp.recycler.RecAdapter;
import ru.ytken.libraryapp.recycler.StoryItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    ImageButton buttonBack;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sPref = getSharedPreferences(getResources().getString(R.string.prefs_first_story_name),MODE_PRIVATE);

        recycler = findViewById(R.id.recycler_main);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        RecAdapter adapter = new RecAdapter(new MyListener());
        recycler.setAdapter(adapter);

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());
    }

    public class MyListener implements RecAdapter.StoryClickedListener {
        @Override
        public void storyClicked(StoryItem item) {
            String name = sPref.getString(getResources().getString(R.string.TAG_CHAR_NAME),"");
            String sex = sPref.getString(getResources().getString(R.string.TAG_CHAR_SEX),"");
            Log.d(getResources().getString(R.string.LOG_TAG), "getting "+ getResources().getString(R.string.TAG_CHAR_NAME) + " " + name);
            switch (item.getId()) {
                case 1:
                    if (name.isEmpty()) {
                        Intent myIntent = new Intent(MainActivity.this, StoryFirstSetNameActivity.class);
                        MainActivity.this.startActivityForResult(myIntent,1);
                    }
                    else {
                        Intent myIntent = new Intent(MainActivity.this, StoryFirstActivity.class);
                        myIntent.putExtra("name", name);
                        myIntent.putExtra("sex", sex);
                        MainActivity.this.startActivity(myIntent);
                    }
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent myIntent = new Intent(MainActivity.this, StoryFirstActivity.class);
        String name = sPref.getString(getResources().getString(R.string.TAG_CHAR_NAME),"");
        String sex = sPref.getString(getResources().getString(R.string.TAG_CHAR_SEX),"");
        myIntent.putExtra("name", name);
        myIntent.putExtra("sex", sex);
        MainActivity.this.startActivity(myIntent);
    }
}