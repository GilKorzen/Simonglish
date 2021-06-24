package com.example.simonglish;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;


public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    AudioManager am;
    Dialog setting_Dialog;
    Button playButton,leaderboardsButton;
    ImageButton settings;
    Intent gameIntent,leaderboardsIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        leaderboardsButton=findViewById(R.id.enter_leaderboard);
        playButton=findViewById(R.id.enter_game);
        leaderboardsButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        settings=findViewById(R.id.settings_button);
        settings.setOnClickListener(this);

        startService(new Intent(MainMenu.this,PlayService.class));

    }

    @Override
    public void onClick(View view) {
        if (view==playButton) {
            try {

              gameIntent = new Intent(MainMenu.this, DifficultyDecider.class);
              gameIntent.putExtra("email",getIntent().getExtras().getString("email"));
              startActivity(gameIntent);
            } catch (Exception e) {
                Log.d("", e.toString());
            }
        }
        if (view==leaderboardsButton)
        {
            leaderboardsIntent = new Intent(MainMenu.this, Leaderboards.class);
            leaderboardsIntent.putExtra("email",getIntent().getExtras().getString("email"));
            leaderboardsIntent.putExtra("position",1);
            startActivity(leaderboardsIntent);
        }
        if (view==settings)
            dialog_setting_things();
    }
    public void dialog_setting_things()
    {
        setting_Dialog=new Dialog(this);
        setting_Dialog.setContentView(R.layout.setting_dialog);
        SeekBar volumeBar=setting_Dialog.findViewById(R.id.volume_id);

        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        volumeBar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        Log.d("TAG", am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)+"");
        int amStreamMusicCurrentVol = am.getStreamVolume(am.STREAM_MUSIC);
        volumeBar.setProgress(amStreamMusicCurrentVol);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                am.setStreamVolume(am.STREAM_MUSIC, progress, 0);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        final Button back_to_game=setting_Dialog.findViewById(R.id.back);
        back_to_game.setText("Return");
        Button quit=setting_Dialog.findViewById(R.id.quit);
        if (getIntent().getExtras().getString("email").equals(""))
            quit.setText("quit");
        else
            quit.setText("Log out");
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this,FirstScreen.class));
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
}
