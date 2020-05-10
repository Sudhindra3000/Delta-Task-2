package com.example.deltatask2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Animation titleAnim;
    private ImageView imageView;
    private SoundPool soundPool;
    private int powerUpSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);


        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        powerUpSound = soundPool.load(this, R.raw.power_up, 1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                soundPool.play(powerUpSound, 1, 1, 0, 0, 1);
            }
        }, 100);


        titleAnim = AnimationUtils.loadAnimation(this, R.anim.title_anim_1);

        imageView = findViewById(R.id.ivTitleSplash);
        imageView.setAnimation(titleAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMenu();
            }
        }, 1000);

        getWindow().setExitTransition(null);


    }

    private void showMenu() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imageView, "gameTitle");
        startActivity(intent, optionsCompat.toBundle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
