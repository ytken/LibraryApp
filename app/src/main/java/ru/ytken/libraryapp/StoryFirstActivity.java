package ru.ytken.libraryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StoryFirstActivity extends AppCompatActivity implements View.OnClickListener, DialogContinueGame.Removable {

    String line = "", lineDialog = "", lineChoice = "", lineChoice1 = "", lineChoice2 = "", nickname = "", wordsPers = "***", namePersoTalk = "", textChoice = "";
    int picId, textId, textDialogId;
    ImageButton buttonLeftChoice, buttonRightChoice;
    ImageView imageView, imageLeftView, imageRightView, backgr;
    TextView wordsView, wordsLeftView, wordsRightView, nameSpeakerView, talkingText, clicker, textLeftChoice, textRightChoice, clickerInner;
    BufferedReader reader, readerDialog;
    static SharedPreferences sPref; static SharedPreferences.Editor editor;
    int countLine = 0, countDialogLine = 0;
    boolean talking = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_first);
        sPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.prefs_first_story_name), MODE_PRIVATE);
        editor = sPref.edit();

        backgr = findViewById(R.id.imageBackgr);
        backgr.setOnClickListener(this);

        imageView = findViewById(R.id.imageWords);
        imageView.setOnClickListener(this);
        imageView.setBackgroundResource(R.color.transparent);

        imageLeftView = findViewById(R.id.imageViewLeft);
        imageLeftView.setVisibility(View.INVISIBLE);

        imageRightView = findViewById(R.id.imageViewRight);
        imageRightView.setVisibility(View.INVISIBLE);

        buttonLeftChoice = findViewById(R.id.imageButtonLeftChoice);
        textLeftChoice = findViewById(R.id.textViewLeftChoice);
        buttonRightChoice = findViewById(R.id.imageButtonRightChoice);
        textRightChoice = findViewById(R.id.textViewRightChoice);

        String sex = getIntent().getStringExtra("sex");
        switch (sex) {
            case "M": textId = R.raw.man_author_1; textDialogId = R.raw.man_man_1; nickname = "Man"; break;
            case "W": textId = R.raw.woman_author_1; textDialogId = R.raw.woman_man_1; nickname = "Woman"; break;
            default: break;
        }

        wordsView = findViewById(R.id.textWords);
        wordsLeftView = findViewById(R.id.textViewLeft);
        wordsLeftView.setVisibility(View.INVISIBLE);

        wordsRightView = findViewById(R.id.textViewRight);
        wordsRightView.setVisibility(View.INVISIBLE);

        nameSpeakerView = findViewById(R.id.textViewNameSpeaker);
        nameSpeakerView.setVisibility(View.INVISIBLE);

        clicker = findViewById(R.id.dialogClick);
        clickerInner = findViewById(R.id.clickerInner);

        reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(textId)));
        readerDialog = new BufferedReader(new InputStreamReader(getResources().openRawResource(textDialogId)));

        countLine = sPref.getInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
        countDialogLine = sPref.getInt(getResources().getString(R.string.TAG_COUNT_DIALOG_LINE), 0);
        backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black_screen, null));

        if (countLine > 1){
            for (int i = 0; i < countLine; i++) {
                try {
                    line = reader.readLine();
                    Log.d("listtext", line);
                    if (line.contains("Image"))
                        setBackgrIm(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            wordsView.setText(line);
            DialogFragment dialog = new DialogContinueGame(sPref, reader);
            dialog.show(getSupportFragmentManager(), "Dialog Continue Game");
        }
        else {
            countLine = 1;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!line.isEmpty())
                wordsView.setText(line);
        }

        if (countDialogLine > 1){
            for (int i = 0; i < countDialogLine; i++) {
                try { lineDialog = readerDialog.readLine(); lineDialog = readerDialog.readLine();
                    lineDialog = readerDialog.readLine();
                    countLine+=2;}
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    void setBackgrIm(String name) {
        String pic = name.substring(6);
        switch (pic) {
            case "house": picId = R.drawable.main_pers_house; break;
            case "purple": picId = R.drawable.purple_flash; break;
            case "black": picId = R.drawable.black_screen; break;
            case "white": picId = R.drawable.while_flash; break;
            case "smoke": picId = R.drawable.smoke; break;
            case "workshop": picId = R.drawable.royal_workshop; break;
            case "bomb": picId = R.drawable.bomb_town; break;
            default: picId = 0; break;
        }
        if (picId == 0)
            backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black_screen, null));
        else
            backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), picId, null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageBackgr:
            case R.id.imageWords:
                try {
                    line = reader.readLine();
                    Log.d("dialogWords",line);
                    countLine++;
                    if (line.contains("Image")) {
                        setBackgrIm(line);
                        imageView.setVisibility(View.INVISIBLE);
                        wordsView.setVisibility(View.INVISIBLE);
                        RunAnimation(backgr);
                        countLine++;
                    }
                    else
                        if(line.contains("Dialog")) {
                            wordsView.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                            clicker.setVisibility(View.VISIBLE);
                            parseDialog(); parseDialog();
                            clicker.setOnClickListener(v1 -> {
                                try {
                                    parseDialog();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (lineDialog.contains("---------")) {
                                    clicker.setVisibility(View.GONE);
                                    imageLeftView.setVisibility(View.INVISIBLE);
                                    wordsLeftView.setVisibility(View.INVISIBLE);
                                    imageRightView.setVisibility(View.INVISIBLE);
                                    wordsRightView.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                        else {
                            if (imageView.getVisibility() == View.GONE || imageView.getVisibility() == View.INVISIBLE) {
                                imageView.setVisibility(View.VISIBLE);
                                RunAnimation(imageView);
                                wordsView.setVisibility(View.VISIBLE);
                            }
                        }
                        if (!(line.contains("Dialog") || line.contains("Image"))) {
                            wordsView.setText(line);
                            RunAnimation(wordsView);
                        }
                    } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    void parseDialog() throws IOException {
        Log.d("dialogWords", "In Dialog " + lineDialog);
        if (lineDialog.contains(nickname)) {
            imageRightView.setVisibility(View.INVISIBLE);
            wordsRightView.setVisibility(View.INVISIBLE);
            imageLeftView.setVisibility(View.VISIBLE);
            wordsLeftView.setVisibility(View.VISIBLE);
            lineDialog = readerDialog.readLine();
            wordsLeftView.setText(lineDialog);
            talking = true;
            talkingText = wordsLeftView;
        }
        else if (lineDialog.contains("Choice")) {
            try {
                imageLeftView.setVisibility(View.GONE);
                wordsLeftView.setVisibility(View.GONE);
                imageRightView.setVisibility(View.GONE);
                wordsRightView.setVisibility(View.GONE);
                handleChoice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (lineDialog.isEmpty()) {talking = false;}
        else if (talking) { Log.d("dialogWords", "In case talking " + lineDialog);
            talkingText.setText(lineDialog);}
        else {
            imageLeftView.setVisibility(View.INVISIBLE);
            wordsLeftView.setVisibility(View.INVISIBLE);
            imageRightView.setVisibility(View.VISIBLE);
            wordsRightView.setVisibility(View.VISIBLE);
            lineDialog = readerDialog.readLine();
            Log.d("dialogWords", "In case some perso " + lineDialog);
            wordsRightView.setText(lineDialog);
            talking = true;
            talkingText = wordsRightView;
        }
        try {
            lineDialog = readerDialog.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDialogLine++;
    }

    void handleChoice() throws IOException {
        lineDialog = readerDialog.readLine();
        if (lineDialog.contains("1)")) {
            textLeftChoice.setText(lineDialog);
            Log.d("dialogChoice", "set left choice " + lineDialog);
            while (!lineDialog.isEmpty()) {
                lineChoice1 += "\n" + lineDialog;
                lineDialog = readerDialog.readLine();
            }
            lineChoice1 += "\n";
            lineDialog = readerDialog.readLine();
            Log.d("dialogChoice", lineChoice1);
        }
        if (lineDialog.contains("2)")) {
            textRightChoice.setText(lineDialog);
            Log.d("dialogChoice", "set right choice " + lineDialog);
            while (!lineDialog.isEmpty()) {
                lineChoice2 += "\n" + lineDialog;
                lineDialog = readerDialog.readLine();
            }
            lineChoice2 += "\n";
            Log.d("dialogChoice", lineChoice2);
        }

        buttonLeftChoice.setVisibility(View.VISIBLE);
        textLeftChoice.setVisibility(View.VISIBLE);
        buttonLeftChoice.setOnClickListener(v -> {
            parseChoiceAction(lineChoice1.substring(lineChoice1.indexOf("\n")+1));
        });

        buttonRightChoice.setVisibility(View.VISIBLE);
        textRightChoice.setVisibility(View.VISIBLE);
        buttonRightChoice.setOnClickListener(v -> {
            parseChoiceAction(lineChoice2.substring(lineChoice2.indexOf("\n")+1));
        });
    }

    void parseChoiceAction(String textAction) {
        textChoice = textAction;
        buttonLeftChoice.setVisibility(View.GONE);
        textLeftChoice.setVisibility(View.GONE);
        buttonRightChoice.setVisibility(View.GONE);
        textRightChoice.setVisibility(View.GONE);
        textChoice = textChoice.substring(textChoice.indexOf("\n")+1);
        namePersoTalk = textChoice.substring(0,textChoice.indexOf("\n"));
        if(namePersoTalk.contains(" ")){
            switch (namePersoTalk.substring(0,namePersoTalk.indexOf(" "))) {
                case "отвага":
                case "решительность":
                case "внимательность":
                case "стойкость":
                    Toast.makeText(this,namePersoTalk, Toast.LENGTH_SHORT).show();
                    textChoice = textChoice.substring(textChoice.indexOf("\n"));
                    break;
                default: break;
            }
        }
        clickerInner.setVisibility(View.VISIBLE);
        clickerInner.setOnClickListener(v -> {
            if (textChoice.isEmpty()) {
                Log.d("dialogChoice", "closing clickerInner");
                clickerInner.setVisibility(View.GONE);
                imageLeftView.setVisibility(View.INVISIBLE);
                wordsLeftView.setVisibility(View.INVISIBLE);
                imageRightView.setVisibility(View.INVISIBLE);
                wordsRightView.setVisibility(View.INVISIBLE);
            }
            else {
                if (wordsPers.contains("***")) {
                    namePersoTalk = textChoice.substring(0, textChoice.indexOf("\n"));
                    textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
                    wordsPers = textChoice.substring(0, textChoice.indexOf("\n"));
                    textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
                    if (namePersoTalk.contains(nickname)) {
                        imageRightView.setVisibility(View.INVISIBLE);
                        wordsRightView.setVisibility(View.INVISIBLE);
                        imageLeftView.setVisibility(View.VISIBLE);
                        wordsLeftView.setVisibility(View.VISIBLE);
                        wordsLeftView.setText(wordsPers);
                    } else {
                        imageLeftView.setVisibility(View.INVISIBLE);
                        wordsLeftView.setVisibility(View.INVISIBLE);
                        imageRightView.setVisibility(View.VISIBLE);
                        wordsRightView.setVisibility(View.VISIBLE);
                        wordsRightView.setText(wordsPers);
                    }
                } else {
                    if (namePersoTalk.contains(nickname))
                        wordsLeftView.setText(wordsPers);
                    else
                        wordsRightView.setText(wordsPers);
                    wordsPers = textChoice.substring(0, textChoice.indexOf("\n"));
                    textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
                }
                Log.d("dialogChoice", "line: " + namePersoTalk + " words: " + wordsPers + "text: " + textChoice);
            }
        });
    }



    @Override
    protected void onDestroy() {
        editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE),countLine);
        editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), picId);
        editor.apply();
        StoryFirstActivity.this.setResult(RESULT_CANCELED);
        super.onDestroy();
    }

    private void RunAnimation(View tv)
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        tv.clearAnimation();
        tv.startAnimation(a);
    }

    @Override
    public void remove() {
        Log.d("wai", "remove");
        setResult(RESULT_OK);
        this.finish();
    }
}
