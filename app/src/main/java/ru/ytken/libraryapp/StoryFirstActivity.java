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

    String line = "", lineDialog = "s", lineChoice1 = "", lineChoice2 = "", nickname = "", wordsPers = "***", namePersoTalk = "", textChoice = "", modeGame="";
    int picId, textId, textDialogId;
    ImageButton buttonLeftChoice, buttonRightChoice, backButton;
    ImageView imageView, imageLeftView, imageRightView, backgr, imageGG, imageChar;
    TextView wordsView, wordsLeftView, wordsRightView, nameSpeakerView, talkingText, clicker, textLeftChoice, textRightChoice, clickerInner;
    BufferedReader reader, readerDialog, readerContinueDialog;
    static SharedPreferences sPref; static SharedPreferences.Editor editor;
    int countLine = 0, countDialogLine = 0, countContinue = 0;
    boolean talking = false, continueDialog = false, readMore = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_first);
        sPref = getApplicationContext().getSharedPreferences(getResources().getString(R.string.prefs_first_story_name), MODE_PRIVATE);
        editor = sPref.edit();

        backgr = findViewById(R.id.imageBackgr);
        backgr.setOnClickListener(this);

        backButton = findViewById(R.id.story_button_back_story);
        backButton.setOnClickListener(v -> finish());

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
            case "M": textId = R.raw.man_author_1; textDialogId = R.raw.man_man_1; nickname = "Man"; imageGG = findViewById(R.id.imageViewMan); break;
            case "W": textId = R.raw.woman_author_1; textDialogId = R.raw.woman_man_1; nickname = "Woman"; imageGG = findViewById(R.id.imageViewWoman); break;
            default: break;
        }

        wordsView = findViewById(R.id.textWords);
        wordsLeftView = findViewById(R.id.textViewLeft);
        wordsLeftView.setVisibility(View.INVISIBLE);

        wordsRightView = findViewById(R.id.textViewRight);
        wordsRightView.setVisibility(View.INVISIBLE);

        nameSpeakerView = findViewById(R.id.textViewNameSpeaker);
        nameSpeakerView.setVisibility(View.INVISIBLE);

        imageChar = findViewById(R.id.imageViewChar);

        clicker = findViewById(R.id.dialogClick);
        clickerInner = findViewById(R.id.clickerInner);

        reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(textId)));
        readerDialog = new BufferedReader(new InputStreamReader(getResources().openRawResource(textDialogId)));
        readerContinueDialog = new BufferedReader(new InputStreamReader(getResources().openRawResource(textDialogId)));

        countLine = sPref.getInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
        countDialogLine = sPref.getInt(getResources().getString(R.string.TAG_COUNT_DIALOG_LINE), 0);
        backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black_screen, null));
        Log.d("linescountyeahh", "line count " + countLine + " line dialog count " + countDialogLine);
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
            if (!line.contains("Dialog"))
                wordsView.setText(line);
            else
                continueDialog = true;
            DialogFragment dialog = new DialogContinueGame(sPref);
            dialog.show(getSupportFragmentManager(), "Dialog Continue Game");
        }
        else
            countLine = 0;

        if (countDialogLine > 1){
            if (line.contains("Dialog")) {
                for (int i = 0; i < countDialogLine; i++) {
                    try { lineDialog = readerContinueDialog.readLine();
                        if (lineDialog.contains("---------")) countContinue=i;
                    }
                    catch (IOException e) { e.printStackTrace(); }
                }
                lineDialog = "";
                for (int i = 0; i < countContinue; i++)
                    try {
                        lineDialog = readerDialog.readLine();
                        Log.d("linescountyeahh", "read");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                int helperCount = countDialogLine; countDialogLine = countContinue;
                backgr.callOnClick();
                clicker.setVisibility(View.VISIBLE);
                for (int i = countContinue; i < helperCount-1; i++) {
                    clicker.callOnClick();
                    Log.d("linescountyeahh", "click");
                }
                Log.d("continuing", "helper " + helperCount + " line " + lineDialog);
            }
            else {
                for (int i = 0; i < countDialogLine; i++) {
                    try { lineDialog = readerDialog.readLine();
                    }
                    catch (IOException e) { e.printStackTrace(); }
                }
            }

        }
        else
            try {
                lineDialog = readerDialog.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        Log.d("prefs","count dialog get " + countDialogLine + " with " + lineDialog);
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
                    if (continueDialog)
                        continueDialog = false;
                    else {
                        line = reader.readLine();
                        countLine++;
                    }
                    if (line==null) finish();
                    Log.d("dialogWords",line);
                    if (line.contains("Image")) {
                        setBackgrIm(line);
                        if (imageView.getVisibility()==View.VISIBLE) {
                            OffAnimation(imageView);
                            OffAnimation(wordsView);
                        }
                        RunAnimation(backgr);
                        countLine++;
                    }
                    else
                        if(line.contains("Dialog")) {
                            if (imageView.getVisibility()==View.VISIBLE) {
                                OffAnimation(imageView);
                                OffAnimation(wordsView);
                            }
                            clicker.setVisibility(View.VISIBLE);
                            parseDialog();
                            clicker.setOnClickListener(v1 -> {
                                try {
                                    parseDialog();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (lineDialog.contains("---------")) {
                                    clicker.setVisibility(View.GONE);
                                    if (imageLeftView.getVisibility()==View.VISIBLE) {
                                        OffAnimation(imageLeftView);
                                        OffAnimation(wordsLeftView);
                                    }
                                    if (imageRightView.getVisibility()==View.VISIBLE) {
                                        OffAnimation(imageRightView);
                                        OffAnimation(wordsRightView);
                                    }
                                    nameSpeakerView.setVisibility(View.INVISIBLE);
                                    try {
                                        lineDialog = readerDialog.readLine();
                                        countDialogLine++;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                        else {
                            if (imageView.getVisibility() == View.GONE || imageView.getVisibility() == View.INVISIBLE) {
                                imageView.setVisibility(View.VISIBLE);
                                RunAnimation(imageView);
                                wordsView.setVisibility(View.VISIBLE);
                                imageGG.setVisibility(View.INVISIBLE);
                                imageChar.setVisibility(View.INVISIBLE);
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
        if (!(lineDialog == null)) {
            if (lineDialog.contains(nickname))
                setTalkingPerso(lineDialog);
            else if (lineDialog.contains("Choice")) {
                try {
                    modeGame = "Choice";
                    //readMore = false;
                    imageLeftView.setVisibility(View.INVISIBLE);
                    wordsLeftView.setVisibility(View.INVISIBLE);
                    imageRightView.setVisibility(View.INVISIBLE);
                    wordsRightView.setVisibility(View.INVISIBLE);
                    nameSpeakerView.setVisibility(View.INVISIBLE);
                    imageGG.setVisibility(View.INVISIBLE);
                    imageChar.setVisibility(View.INVISIBLE);
                    talking = false;
                    handleChoice();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (lineDialog.contains("***")) {
                talking = false;
                lineDialog = readerDialog.readLine();
                countDialogLine++;
                setTalkingPerso(lineDialog);
                //readMore = true;
            } else if (lineDialog.isEmpty()) {
                talking = false;
                imageGG.setVisibility(View.INVISIBLE);
                imageChar.setVisibility(View.INVISIBLE);
                //readMore = true;
            } else if (talking) {
                Log.d("dialogWords", "In case talking " + lineDialog);
                talkingText.setText(lineDialog);
                RunAnimation(talkingText);
                //readMore = true;
            } else if (lineDialog.contains("------")) { //readMore = true;
            } else {
                setTalkingPerso(lineDialog);
                //readMore = true;
            }
            if (readMore)
                try {
                    lineDialog = readerDialog.readLine();
                    countDialogLine++;
                    Log.d("readDialog", "3 " + lineDialog);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    void setTalkingPerso(String name) throws IOException {
        if (name.contains(nickname)) {
            if (imageRightView.getVisibility()==View.VISIBLE) {
                OffAnimation(imageRightView);
                OffAnimation(wordsRightView);
            }
            imageLeftView.setVisibility(View.VISIBLE);
            RunAnimation(imageLeftView);
            wordsLeftView.setVisibility(View.VISIBLE);
            lineDialog = readerDialog.readLine();
            countDialogLine++;
            Log.d("readDialog","1");
            wordsLeftView.setText(lineDialog);
            RunAnimation(wordsLeftView);
            talking = true;
            talkingText = wordsLeftView;
            imageChar.setVisibility(View.INVISIBLE);
            imageGG.setVisibility(View.VISIBLE);
            nameSpeakerView.setVisibility(View.INVISIBLE);
            RunAnimation(imageGG);
        }
        else {
            if (name.contains("Dad")) {
                imageChar.setVisibility(View.VISIBLE);
                RunAnimation(imageChar);
                nameSpeakerView.setVisibility(View.INVISIBLE);
            }
            else {
                nameSpeakerView.setVisibility(View.VISIBLE);
                nameSpeakerView.setText(name);
            }
            imageGG.setVisibility(View.INVISIBLE);
            imageLeftView.setVisibility(View.INVISIBLE);
            wordsLeftView.setVisibility(View.INVISIBLE);
            imageRightView.setVisibility(View.VISIBLE);
            RunAnimation(imageRightView);
            wordsRightView.setVisibility(View.VISIBLE);
            lineDialog = readerDialog.readLine();
            countDialogLine++;
            Log.d("readDialog","2");
            Log.d("dialogWords", "In case some perso " + lineDialog);
            wordsRightView.setText(lineDialog);
            RunAnimation(wordsRightView);
            talking = true;
            talkingText = wordsRightView;
        }
    }

    void handleChoice() throws IOException {
        lineDialog = readerDialog.readLine();
        countDialogLine++;
        Log.d("readDialog","4 " + lineDialog);
        if (lineDialog.contains("1)")) {
            textLeftChoice.setText(lineDialog.substring(2));
            Log.d("dialogChoice", "set left choice " + lineDialog);
            while (!lineDialog.isEmpty()) {
                lineChoice1 += "\n" + lineDialog;
                lineDialog = readerDialog.readLine();
                countDialogLine++;
                Log.d("readDialog","5");
            }
            lineChoice1 += "\n";
            lineDialog = readerDialog.readLine();
            countDialogLine++;
            Log.d("readDialog","6");
            Log.d("dialogChoice", lineChoice1);
        }
        if (lineDialog.contains("2)")) {
            textRightChoice.setText(lineDialog.substring(2));
            Log.d("dialogChoice", "set right choice " + lineDialog);
            while (!lineDialog.isEmpty()) {
                lineChoice2 += "\n" + lineDialog;
                lineDialog = readerDialog.readLine();
                countDialogLine++;
                Log.d("readDialog","7");
            }
            lineChoice2 += "\n";
            Log.d("dialogChoice", lineChoice2);
        }

        buttonLeftChoice.setVisibility(View.VISIBLE);
        RunAnimation(buttonLeftChoice);
        textLeftChoice.setVisibility(View.VISIBLE);
        RunAnimation(textLeftChoice);
        buttonLeftChoice.setOnClickListener(v -> {
            parseChoiceAction(lineChoice1.substring(lineChoice1.indexOf("\n")+1));
        });

        buttonRightChoice.setVisibility(View.VISIBLE);
        RunAnimation(buttonRightChoice);
        textRightChoice.setVisibility(View.VISIBLE);
        RunAnimation(textRightChoice);
        buttonRightChoice.setOnClickListener(v -> {
            parseChoiceAction(lineChoice2.substring(lineChoice2.indexOf("\n")+1));
        });
    }

    void parseChoiceAction(String textAction) {
        lineChoice1=""; lineChoice2="";
        wordsPers = "***";
        modeGame = "";
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
                    //Toast.makeText(this,namePersoTalk, Toast.LENGTH_SHORT).show();
                    textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
                    Log.d("lineChoice", "namePersoTalk: " + namePersoTalk + "textChoice: " + textChoice);
                    break;
                default: break;
            }
        }
        clickerInner.setVisibility(View.VISIBLE);
        clickerInner.setOnClickListener(v -> {
            if (textChoice.isEmpty()) {
                Log.d("dialogChoice", "closing clickerInner");
                clickerInner.setVisibility(View.GONE);
                if (imageLeftView.getVisibility()==View.VISIBLE) {
                    OffAnimation(imageLeftView);
                    OffAnimation(wordsLeftView);
                    //TODO OffAnimation(imageTalking);
                }
                if (imageRightView.getVisibility()==View.VISIBLE) {
                    OffAnimation(imageRightView);
                    OffAnimation(wordsRightView);
                }
                nameSpeakerView.setVisibility(View.INVISIBLE);
                this.onClick(backgr);
            }
            else {
                if (wordsPers.contains("***")) {
                    namePersoTalk = textChoice.substring(0, textChoice.indexOf("\n"));
                    textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
                    wordsPers = textChoice.substring(0, textChoice.indexOf("\n"));
                    textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
                    if (namePersoTalk.contains(nickname)) {
                        if(imageRightView.getVisibility()==View.VISIBLE) {
                            OffAnimation(imageRightView);
                            OffAnimation(wordsRightView);
                        }
                        imageGG.setVisibility(View.VISIBLE);
                        imageLeftView.setVisibility(View.VISIBLE);
                        RunAnimation(imageLeftView);
                        wordsLeftView.setVisibility(View.VISIBLE);
                        wordsLeftView.setText(wordsPers);
                        RunAnimation(wordsLeftView);
                    } else {
                        if (imageLeftView.getVisibility() == View.VISIBLE) {
                            OffAnimation(imageLeftView);
                            OffAnimation(wordsLeftView);
                            imageGG.setVisibility(View.INVISIBLE);
                        }
                        if (namePersoTalk.contains("Dad")) {
                            imageChar.setVisibility(View.VISIBLE);
                            RunAnimation(imageChar);
                            nameSpeakerView.setVisibility(View.INVISIBLE);
                        }
                        else {
                            nameSpeakerView.setVisibility(View.VISIBLE);
                            nameSpeakerView.setText(namePersoTalk);
                        }
                        imageGG.setVisibility(View.INVISIBLE);
                        imageRightView.setVisibility(View.VISIBLE);
                        RunAnimation(imageRightView);
                        wordsRightView.setVisibility(View.VISIBLE);
                        wordsRightView.setText(wordsPers);
                        RunAnimation(wordsRightView);
                    }
                } else {
                    if (namePersoTalk.contains(nickname)) {
                        wordsLeftView.setText(wordsPers);
                        imageGG.setVisibility(View.VISIBLE);
                    }
                    else {
                        wordsRightView.setText(wordsPers);
                        imageGG.setVisibility(View.INVISIBLE);
                    }
                }
                if(textChoice.contains("\n")) {
                    wordsPers = textChoice.substring(0, textChoice.indexOf("\n"));
                    textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
                }
                Log.d("dialogChoice", "line: " + namePersoTalk + " words: " + wordsPers + "text: " + textChoice);
            }
        });
        clickerInner.callOnClick();
    }



    @Override
    protected void onDestroy() {
        editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE),countLine);
        editor.putInt(getResources().getString(R.string.TAG_COUNT_DIALOG_LINE),countDialogLine);
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

    private void OffAnimation(View tv) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scaleoff);
        a.reset();
        tv.clearAnimation();
        tv.startAnimation(a);
        tv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void remove() {
        Log.d("wai", "remove");
        setResult(RESULT_OK);
        this.finish();
    }
}
