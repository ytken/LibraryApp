package ru.ytken.libraryapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ru.ytken.libraryapp.dialogs.DialogContinueGame;

public class StoryFirstActivity extends AppCompatActivity implements View.OnClickListener, DialogContinueGame.Removable {

    String line = "", lineDialog = "s", lineChoice1 = "", lineChoice2 = "", nickname = "", wordsPers = "***", namePersoTalk = "", textChoice = "", modeGame="";
    int picId, textId, textDialogId;
    ImageButton backButton;
    ImageView imageView, imageLeftView, imageRightView, buttonLeftChoice, buttonRightChoice, backgr, imageGG, imageChar;
    TextView wordsView, wordsLeftView, wordsRightView, talkingText, clicker, textLeftChoice, textRightChoice, clickerInner;
    BufferedReader reader, readerDialog;
    static SharedPreferences sPref; static SharedPreferences.Editor editor;
    int countLine = 0, countDialogClick = 0, countDialogNum = 0;
    int st_courage = 0, st_determ = 0, st_atten = 0, st_resist = 0;
    boolean talking = false, continueDialog = false, readMore = true;
    int coins;
    int linearWidth = 0, linearHeight = 0;

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


        editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE),0);
        editor.putInt(getResources().getString(R.string.TAG_COUNT_DIALOG_CLICK),0);
        editor.apply();

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

        imageChar = findViewById(R.id.imageViewChar);

        clicker = findViewById(R.id.dialogClick);
        clickerInner = findViewById(R.id.clickerInner);

        reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(textId)));
        readerDialog = new BufferedReader(new InputStreamReader(getResources().openRawResource(textDialogId)));

        st_courage = sPref.getInt(getResources().getString(R.string.STATE_COURAGE), 0);
        st_atten = sPref.getInt(getResources().getString(R.string.STATE_ATTENTION), 0);
        st_determ = sPref.getInt(getResources().getString(R.string.STATE_DETERMINATION), 0);
        st_resist = sPref.getInt(getResources().getString(R.string.STATE_RESISTANCE), 0);

        coins = sPref.getInt(getResources().getString(R.string.COIN_NUMBER), 0);

        countLine = sPref.getInt(getResources().getString(R.string.TAG_COUNT_LINE), 0);
        countDialogClick = sPref.getInt(getResources().getString(R.string.TAG_COUNT_DIALOG_CLICK), 0);
        countDialogNum = sPref.getInt(getResources().getString(R.string.TAG_COUNT_DIALOG_NUM), 0);

        backgr.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.black_screen, null));
        Log.d("linescountyeahh", "line count " + countLine + " click dialog count " + countDialogClick + " num dialog count " + countDialogNum);
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

        if ((countDialogClick > 0) || (countDialogNum > 0)){
            Log.d("linescountyeahh", "in if and line = " + line);
            int p = 0;
            while (p < countDialogNum) {
                try { lineDialog = readerDialog.readLine();
                    Log.d("linescountyeahh", lineDialog);
                }
                catch (IOException e) { e.printStackTrace(); }
                if (lineDialog.contains("---------")) p++;
            }
            try { lineDialog = readerDialog.readLine();
                Log.d("linescountyeahh", "dop = " + lineDialog);
            }
            catch (IOException e) { e.printStackTrace(); }
            if (countDialogClick > 0) {
                lineDialog = "";
                backgr.callOnClick();
                clicker.setVisibility(View.VISIBLE);
                /*try {
                    parseDialog();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                int helperClick = countDialogClick; countDialogClick = 0;
                for (int i = 0; i < helperClick; i++) {
                    clicker.callOnClick();
                    Log.d("linescountyeahh", "click");
                }
            }
        }
        else
            try {
                lineDialog = readerDialog.readLine();
            } catch (IOException e) {
                e.printStackTrace();

            }
        Log.d("prefs","count dialog get " + countDialogClick + " with " + lineDialog);
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
                    if (line==null) showClosingScreen();
                    else {
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
                                countDialogClick++;
                                try {
                                    parseDialog();
                                } catch (IOException e) {
                                    e.printStackTrace();
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
                        } }
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
            } else if (lineDialog.contains("------")) {
                countDialogNum ++;
                countDialogClick = 0;
                clicker.setVisibility(View.GONE);
                if (imageLeftView.getVisibility()==View.VISIBLE) {
                    OffAnimation(imageLeftView);
                    OffAnimation(wordsLeftView);
                }
                if (imageRightView.getVisibility()==View.VISIBLE) {
                    OffAnimation(imageRightView);
                    OffAnimation(wordsRightView);
                }
            } else {
                setTalkingPerso(lineDialog);
                //readMore = true;
            }
            if (readMore)
                try {
                    lineDialog = readerDialog.readLine();
                    Log.d("readDialog", "3 " + lineDialog);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        else showClosingScreen();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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
            Log.d("readDialog","1");
            wordsLeftView.setText(lineDialog);
            RunAnimation(wordsLeftView);
            talking = true;
            talkingText = wordsLeftView;
            imageChar.setVisibility(View.INVISIBLE);
            imageGG.setVisibility(View.VISIBLE);
            RunAnimation(imageGG);
        }
        else {
            if (name.contains("Dad")) {
                imageChar.setImageDrawable(getResources().getDrawable(R.drawable.stiven_cut, null));
                imageChar.setVisibility(View.VISIBLE);
                RunAnimation(imageChar);
            }
            else {
                if (name.contains("Sebastian")) {
                    imageChar.setImageDrawable(getResources().getDrawable(R.drawable.sebastian_cut, null));
                    imageChar.setVisibility(View.VISIBLE);
                    RunAnimation(imageChar);
                }
            }
            imageGG.setVisibility(View.INVISIBLE);
            imageLeftView.setVisibility(View.INVISIBLE);
            wordsLeftView.setVisibility(View.INVISIBLE);
            imageRightView.setVisibility(View.VISIBLE);
            RunAnimation(imageRightView);
            wordsRightView.setVisibility(View.VISIBLE);
            lineDialog = readerDialog.readLine();

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
        Log.d("readDialog","4 " + lineDialog);
        if (lineDialog.contains("1)")) {
            textLeftChoice.setText(lineDialog.substring(2));
            Log.d("dialogChoice", "set left choice " + lineDialog);
            while (!lineDialog.isEmpty()) {
                lineChoice1 += "\n" + lineDialog;
                lineDialog = readerDialog.readLine();
                Log.d("readDialog","5");
            }
            lineChoice1 += "\n";
            lineDialog = readerDialog.readLine();
            Log.d("readDialog","6");
            Log.d("dialogChoice", lineChoice1);
        }
        if (lineDialog.contains("2)")) {
            textRightChoice.setText(lineDialog.substring(2));
            Log.d("dialogChoice", "set right choice " + lineDialog);
            while (!lineDialog.isEmpty()) {
                lineChoice2 += "\n" + lineDialog;
                lineDialog = readerDialog.readLine();
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

    void switchStateAction() {
        //Toast.makeText(this,namePersoTalk, Toast.LENGTH_SHORT).show();
        textChoice = textChoice.substring(textChoice.indexOf("\n") + 1);
        Log.d("lineChoice", "namePersoTalk: " + namePersoTalk + "textChoice: " + textChoice);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
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
                case "отвага": st_courage += Integer.parseInt(namePersoTalk.substring(namePersoTalk.indexOf(" ")+1)); switchStateAction(); break;
                case "решительность": st_determ += Integer.parseInt(namePersoTalk.substring(namePersoTalk.indexOf(" ")+1)); switchStateAction(); break;
                case "внимательность": st_atten += Integer.parseInt(namePersoTalk.substring(namePersoTalk.indexOf(" ")+1)); switchStateAction(); break;
                case "стойкость": st_resist += Integer.parseInt(namePersoTalk.substring(namePersoTalk.indexOf(" ")+1)); switchStateAction(); break;
                default: break;
            }
        }
        clickerInner.setVisibility(View.VISIBLE);
        Log.d("dialogChoice", "opening clickerInner");
        clickerInner.setOnClickListener(v -> {
            countDialogClick++;
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
                            imageChar.setImageDrawable(getResources().getDrawable(R.drawable.stiven_cut, null));
                            imageChar.setVisibility(View.VISIBLE);
                            RunAnimation(imageChar);
                        }
                        else {
                            if (namePersoTalk.contains("Sebastian")) {
                                imageChar.setImageDrawable(getResources().getDrawable(R.drawable.sebastian_cut, null));
                                imageChar.setVisibility(View.VISIBLE);
                                RunAnimation(imageChar);
                            }
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

    private void showClosingScreen() {
        LayoutInflater li = LayoutInflater.from(StoryFirstActivity.this);
        LinearLayout linear = (LinearLayout)li.inflate(R.layout.linear, null);
        linearWidth = linear.getWidth(); linearHeight = linear.getHeight();
        float values[] = {st_courage, st_atten, st_resist, st_determ};

        values=calculateData(values);
        linear.addView(new MyGraphview(this,values));

        AlertDialog.Builder alert = new AlertDialog.Builder(StoryFirstActivity.this);
        alert.setView(linear);
        alert.setPositiveButton("ОК", (dialog, which) -> {
            StoryFirstActivity.this.remove();
        }).show();
    }

    private float[] calculateData(float[] data) {
        float total=0;
        for(int i=0;i<data.length;i++) {
            total+=data[i];
        }
        for(int i=0;i<data.length;i++) {
            data[i]=360*(data[i]/total);
        }
        return data;
    }

    public class MyGraphview extends View
    {
        private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        private float[] value_degree;
        private int[] COLORS={Color.BLUE,Color.GREEN,Color.GRAY,Color.CYAN,Color.RED};
        RectF rectf = new RectF(10,10,1000,1000);

        public MyGraphview(Context context, float[] values) {
            super(context);
            value_degree=new float[values.length];
            for(int i=0;i<values.length;i++) {
                value_degree[i]=values[i];
            }
            //rectf.top = 10; rectf.left = 10; rectf.bottom = linearWidth - 10; rectf.right = linearWidth - 10;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int temp = 0;

            for (int i = 0; i < value_degree.length; i++) {//values2.length; i++) {
                if (i == 0) {
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, 0, value_degree[i], true, paint);
                }
                else {
                    temp += (int) value_degree[i - 1];
                    paint.setColor(COLORS[i]);
                    canvas.drawArc(rectf, temp, value_degree[i], true, paint);
                }
                if (value_degree[i] > 0) {
                    canvas.drawRect(10,1010+i*220, 210, 1210+i*220, paint);
                    String text = "";
                    switch (i) {
                        case 0: text = "Отвага"; break;
                        case 1: text = "Внимательность"; break;
                        case 2: text = "Стойкость"; break;
                        case 3: text = "Решительность"; break;
                    }
                    paint.setColor(getResources().getColor(R.color.primaryBlack, null));
                    paint.setTextSize(getResources().getDimension(R.dimen.names_text_size));
                    canvas.drawText(text, 250, 1110+i*220,paint);
                }

            }

        }
    }

    @Override
    protected void onDestroy() {
        editor.putInt(getResources().getString(R.string.TAG_COUNT_LINE),countLine);
        editor.putInt(getResources().getString(R.string.TAG_COUNT_DIALOG_CLICK),countDialogClick);
        editor.putInt(getString(R.string.TAG_COUNT_DIALOG_NUM), countDialogNum);
        editor.putInt(getResources().getString(R.string.TAG_BACKGROUND), picId);
        editor.putInt(getString(R.string.STATE_COURAGE), st_courage);
        editor.putInt(getString(R.string.STATE_ATTENTION), st_atten);
        editor.putInt(getString(R.string.STATE_DETERMINATION), st_determ);
        editor.putInt(getString(R.string.STATE_RESISTANCE), st_resist);
        editor.putInt(getString(R.string.COIN_NUMBER), coins);
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

    public interface shouldSave {
        void setFlag(boolean flag);
    }

}
