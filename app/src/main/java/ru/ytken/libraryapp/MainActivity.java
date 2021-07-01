package ru.ytken.libraryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.ytken.libraryapp.recycler.RecAdapter;
import ru.ytken.libraryapp.recycler.StoryItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    ImageButton buttonBack;
    TextView textCoins;
    SharedPreferences sPref; SharedPreferences.Editor editor;
    RecAdapter adapter;
    Integer coins;
    public boolean FLAG_DIALOG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sPref = getSharedPreferences(getResources().getString(R.string.prefs_first_story_name),MODE_PRIVATE);
        editor = sPref.edit();

        recycler = findViewById(R.id.recycler_main);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        adapter = new RecAdapter(new MyListener());
        recycler.setAdapter(adapter);

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        textCoins = findViewById(R.id.textViewCoins);
        coins = sPref.getInt(getResources().getString(R.string.COIN_NUMBER), -1);
        Log.d("gettingCoins", coins.toString());
        if (coins == -1) {
            DialogFragment newFragment = MyDialogFragment.newInstance();
            newFragment.show(getSupportFragmentManager(), "dialog");
            coins = 15;
            editor.putInt(getResources().getString(R.string.COIN_NUMBER), coins);
            editor.apply();
        }
        textCoins.setText(coins.toString());
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
                        Log.d( "order","StoryFirstSetNameActivity from MainActivity");
                        Intent myIntent = new Intent(MainActivity.this, StoryFirstSetNameActivity.class);
                        MainActivity.this.startActivityForResult(myIntent,1);
                    }
                    else {
                        Log.d( "order","StoryFirstActivity from MainActivity");
                        Intent myIntent = new Intent(MainActivity.this, StoryFirstActivity.class);
                        myIntent.putExtra("name", name);
                        myIntent.putExtra("sex", sex);
                        MainActivity.this.startActivityForResult(myIntent, 2);
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
        if((requestCode == 1)&&(resultCode == RESULT_OK)) {
            Intent myIntent = new Intent(MainActivity.this, StoryFirstActivity.class);
            String name = sPref.getString(getResources().getString(R.string.TAG_CHAR_NAME),"");
            String sex = sPref.getString(getResources().getString(R.string.TAG_CHAR_SEX),"");
            myIntent.putExtra("name", name);
            myIntent.putExtra("sex", sex);
            MainActivity.this.startActivityForResult(myIntent, 2);
        }
        if(requestCode == 2) {
            if (resultCode == RESULT_CANCELED) {
                if (FLAG_DIALOG) {
                    DialogFragment dialog = new DialogSaving(StoryFirstActivity.editor);
                    dialog.show(getSupportFragmentManager(), "Dialog Exit");
                }
            }
            if (resultCode == RESULT_OK) {
                coins += 10;
                textCoins.setText(coins.toString());
                //adapter.onStoryClicked(0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        editor.putInt(getResources().getString(R.string.COIN_NUMBER), coins);
        editor.apply();
        super.onDestroy();
    }

    public static class MyDialogFragment extends DialogFragment {

        static MyDialogFragment newInstance() {
            MyDialogFragment f = new MyDialogFragment();
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.coin_fragment_dialog, container, false);
            Button buttonGet15 = v.findViewById(R.id.buttonGet15);
            buttonGet15.setOnClickListener(v1 -> {
                this.dismiss();
            });
            return v;
        }

    }

}