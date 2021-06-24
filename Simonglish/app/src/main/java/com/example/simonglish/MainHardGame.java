package com.example.simonglish;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class MainHardGame extends AppCompatActivity implements View.OnClickListener{
    final static int level = 5;
    ImageButton red, blue, green, yellow,purple, settings;
    VocalButton[] vocalButtonArr;
    ImageButton[] arr;
    TextView rip,wait_for_your_turn;
    AudioManager am;
    int turn = 1;
    Button restart, back_to_menu;
    Dialog losing_Dialog, setting_Dialog;
    ArrayList<Pair> allPairs = new ArrayList<>();
    TextToSpeech textToSpeech;
    Intent menu,exit;


    ArrayList<ImageButton> botPlays = new ArrayList<>();
    ArrayList<ImageButton> theirPlays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hard_game);
        red = findViewById(R.id.red);
        blue = findViewById(R.id.blue);
        yellow = findViewById(R.id.yellow);
        green = findViewById(R.id.green);
        purple=findViewById(R.id.purple);
        rip = findViewById(R.id.rip);
        settings = findViewById(R.id.settings_button);
        wait_for_your_turn=findViewById(R.id.turn);


        red.setOnClickListener(this);
        blue.setOnClickListener(this);
        settings.setOnClickListener(this);
        yellow.setOnClickListener(this);
        green.setOnClickListener(this);
        purple.setOnClickListener(this);


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == textToSpeech.LANG_MISSING_DATA || result == textToSpeech.LANG_NOT_SUPPORTED)
                        Log.d("lo", "onInit:not supported ");
                    else
                        Log.d("All good", "onInit: ");

                } else
                    Log.d("ken", "onInit: ");
            }


        });

        vocalButtonArr = new VocalButton[level];
        arr = new ImageButton[level];
        arr[0] = red;
        arr[1] = blue;
        arr[2] = yellow;
        arr[3] = green;
        arr[4] = purple;
        createPairs();


        three_two_one();
    }

    @Override
    public void onClick(View view) {
        if (view == settings) {
            settings.setEnabled(false);
            dialog_setting_things();
            settings.setEnabled(true); }



        if (view == restart) {
            losing_Dialog.dismiss();
            reset();

            gameOn(); }


        if (view == back_to_menu) {
            losing_Dialog.dismiss();
            menu= new Intent(MainHardGame.this, MainMenu.class);
            menu.putExtra("email",getIntent().getExtras().getString("email"));
            startActivity(menu);
        }

        if (view == blue || view == green || view == yellow || view == red || view == purple) {
            for (int m = 0; m < vocalButtonArr.length; m++) {
                if (vocalButtonArr[m].getImageButton() == view)
                    textToSpeech.speak(vocalButtonArr[m].getHebrewWord(), TextToSpeech.QUEUE_FLUSH, null);
            }
            animation((ImageButton) view);

            theirPlays.add((ImageButton) view);
            checkIfRight();


        }
    }
    public void gameOn() {
        //plays the bot

        Handler handler = new Handler();
        botPlays.add(arr[(int) (Math.random() * level)]);
        disableButtons();

        for (int i = 0; i < turn; i++) {


            final int finalI = i;
            try {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animation(botPlays.get(finalI));
                        for (int k = 0; k < vocalButtonArr.length; k++) {
                            if (vocalButtonArr[k].getImageButton() == botPlays.get(finalI)) {
                                Log.d("TAG", vocalButtonArr[k].getEnglishWord());
                                textToSpeech.speak(vocalButtonArr[k].getEnglishWord(), TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                    }
                }, 2000 * i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableButtons();
            }
        }, 2000*turn );

        theirPlays.clear();


    }




    public void checkIfRight() {
        //checks if the user has clicked the right button
        Handler handler = new Handler();

        if (!(theirPlays.size() > botPlays.size())) {

            if (theirPlays.get(theirPlays.size() - 1) != botPlays.get(theirPlays.size() - 1))
                inCaseYouLost();
            else if (theirPlays.size() == botPlays.size()) {
                turn++;
                Log.d("well", theirPlays.toString());
                rip.setText("Score:" + (turn - 1));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gameOn();

                    }
                }, 1400);
            }
        }
    }




    public void animation(final ImageButton imageButton) {
        //makes the animation

        imageButton.setAlpha((float) 0.22);

        new CountDownTimer(600, 60) {

            @Override
            public void onTick(long arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {

                imageButton.setAlpha((float) 1);


            }

        }.start();


    }

    public void reset() {
        createPairs();

        rip.setText("Good luck");
        botPlays.clear();
        theirPlays.clear();
        turn = 1;

    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    public void createPairs() {
        //creates all pairs of words and associate each button with a random pair
        allPairs.clear();
        allPairs.add(new Pair("opaque", "atoom"));
        allPairs.add(new Pair(" to demonstrate", "le haf ghin"));
        allPairs.add(new Pair("to depict", "letaher"));
        allPairs.add(new Pair(" inhabitant ", "toe shav"));
        allPairs.add(new Pair("scholar", "meloomud"));
        allPairs.add(new Pair("cheap", "zol"));
        allPairs.add(new Pair("daughter", "but"));
        allPairs.add(new Pair("era", "ydan"));
        for (int i=0;i<level;i++) {
            int placement= (int)(Math.random()*(allPairs.size()));
            vocalButtonArr[i] = new VocalButton(arr[i], allPairs.get(placement));
            allPairs.remove(placement);
        }


    }

    public void inCaseYouLost()
    {
        rip.setText("you lost");
        savingScore(turn-1);
        losing_Dialog = new Dialog(this);
        losing_Dialog.setContentView(R.layout.defeat_screen);
        TextView score = losing_Dialog.findViewById(R.id.score);
        restart = losing_Dialog.findViewById(R.id.restart);

        back_to_menu = losing_Dialog.findViewById(R.id.return_main);
        back_to_menu.setOnClickListener(this);
        restart.setOnClickListener(this);
        score.setText("" + (turn - 1));
        losing_Dialog.setCancelable(false);
        losing_Dialog.show();

    }

    public void disableButtons() {
        wait_for_your_turn.setText("Computer's turn");
        for (int t = 0; t < arr.length; t++) {
            arr[t].setOnClickListener(null);
            Log.d("gj", "" + t);
        }

    }

    public void enableButtons() {
        wait_for_your_turn.setText("Your turn");
        for (int t = 0; t < arr.length; t++) {
            arr[t].setOnClickListener(this);
            Log.d("gj", "" + t);
        }


    }

    public void dialog_setting_things() {
        setting_Dialog = new Dialog(this);
        setting_Dialog.setContentView(R.layout.setting_dialog);
        SeekBar volumeBar = setting_Dialog.findViewById(R.id.volume_id);
        volumeBar.setMax(15);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int amStreamMusicCurrentVol = am.getStreamVolume(am.STREAM_MUSIC);
        volumeBar.setProgress(amStreamMusicCurrentVol);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                am.setStreamVolume(am.STREAM_MUSIC, progress, 0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final Button back_to_game = setting_Dialog.findViewById(R.id.back);
        Button quit = setting_Dialog.findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit = new Intent(MainHardGame.this, MainMenu.class);
                exit.putExtra("email",getIntent().getExtras().getString("email"));
                startActivity(exit);
                finish();
            }
        });

        back_to_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_Dialog.dismiss();

            }
        });
        setting_Dialog.setCancelable(true);
        setting_Dialog.show();

    }

    public void three_two_one()
    //you can guess what it does
    {
        for (int i = 0; i < arr.length; i++) {
            arr[i].setVisibility(View.INVISIBLE);
        }
        final TextView txt=findViewById(R.id.lets_go);
        final int[] count_seconds = {3};
        new CountDownTimer(4400, 1000) {
            @Override
            public void onTick(long arg0) {
                if (count_seconds[0] !=0)
                {
                    txt.setText(count_seconds[0]+"");
                    count_seconds[0]--;
                }
                else
                    txt.setText("GO");


            }

            @Override
            public void onFinish() {
                txt.setText("");
                for (int i = 0; i < arr.length; i++)
                    arr[i].setVisibility(View.VISIBLE);
                gameOn();


            }
        }.start();


    }
    private void savingScore(int score) {

        JSONObject sendingData = new JSONObject();

        try {
            if (!getIntent().getExtras().getString("email").equals("")) {
                sendingData.put("request", "saveHard");
                sendingData.put("score", score);
                sendingData.put("email", getIntent().getExtras().getString("email"));
                SocketStuff dataTransportTask = new SocketStuff(sendingData);
                dataTransportTask.execute();
                System.out.println(sendingData);
                if (dataTransportTask.get().getString("response").equals("updated")) {
                    int highest_score = dataTransportTask.get().getInt("score");
                    String highest_user = dataTransportTask.get().getString("username");
                    if (highest_score == score)
                        notify_them_all(highest_user, score);
                }
            }

        } catch (JSONException | InterruptedException | ExecutionException e) {
            Log.e("Exception", e.toString());
        }
    }
    public void notify_them_all(String name,int record)
    //creates notification
    {
        System.out.println("hey");
        int icon = R.drawable.breaking_records_icon;
        long when = System.currentTimeMillis();
        String title = "New Leader";
        String text=name+ " just achieved the highest score in the hard mode with a score of"+ record+ " points!!!";

        Intent intent = new Intent(this, Leaderboards.class);
        intent.putExtra("email", getIntent().getExtras().getString("email"));
        intent.putExtra("position",2);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "787");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "787";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(icon).setWhen(when)
                .setAutoCancel(true).setContentTitle(title)
                .setContentText(text).build();
        notificationManager.notify(1, notification);
    }



}


