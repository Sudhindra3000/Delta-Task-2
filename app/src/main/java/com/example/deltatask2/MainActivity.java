package com.example.deltatask2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private Animation titleAnim;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playSoundInMedia(R.raw.power_up);
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

    private void playSoundInMedia(int resID) {
        mediaPlayer = MediaPlayer.create(MainActivity.this, resID);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}
