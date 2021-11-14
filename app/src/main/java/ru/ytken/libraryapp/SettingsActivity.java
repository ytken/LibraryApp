package ru.ytken.libraryapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.ytken.libraryapp.recycler.RecAdapter;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        Button changeTheme = findViewById(R.id.button_change_theme);
        changeTheme.setOnClickListener(view -> {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    // Night mode is not active, we're using the light theme
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active, we're using dark theme
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }
        });

        Button buttonLinkInst = findViewById(R.id.button_link_instagram);
        buttonLinkInst.setOnClickListener(view -> {
            Uri uri = Uri.parse("http://instagram.com/_u/_ytken_");
            Intent insta = new Intent(Intent.ACTION_VIEW, uri);
            insta.setPackage("com.instagram.android");

            if (isIntentAvailable(getApplicationContext(), insta)){
                startActivity(insta);
            } else{
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_ytken_")));
            }
        });

        Button buttonLinkVk = findViewById(R.id.button_link_vk);

        buttonLinkVk.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://vk.com/_u/n.ovechka");
            Intent vk = new Intent(Intent.ACTION_VIEW, uri);
            vk.setPackage("com.vk.android");

            if (isIntentAvailable(getApplicationContext(), vk)){
                startActivity(vk);
            } else{
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/n.ovechka")));
            }
        });

    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}