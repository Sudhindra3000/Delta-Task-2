package com.example.deltatask2.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.example.deltatask2.R;
import com.example.deltatask2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        new Handler().postDelayed(this::playSoundInMedia, 100);


        Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.title_anim_1);

        binding.ivTitleSplash.setAnimation(titleAnim);

        new Handler().postDelayed(this::showMenu, 1000);

        getWindow().setExitTransition(null);


    }

    private void showMenu() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, binding.ivTitleSplash, "gameTitle");
        startActivity(intent, optionsCompat.toBundle());
    }

    private void playSoundInMedia() {
        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.power_up);
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
