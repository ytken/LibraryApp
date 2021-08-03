package ru.ytken.libraryapp;

import android.app.Application;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // cold launches take some time
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2000));
    }
}
