package com.example.deltatask2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.deltatask2.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";
    private ActivityMenuBinding binding;
    private Animation multAnim;
    private int n = 2, s;
    private SharedPreferences sharedPreferences;
    private SettingsDialog settingsDialog;
    private SoundPool soundPool;
    private int bt_click_1, menu_click, tick_tok_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        getWindow().setEnterTransition(null);
        getWindow().getSharedElementEnterTransition().setDuration(400);

        sharedPreferences = MenuActivity.this.getSharedPreferences("pref", MODE_PRIVATE);
        s = sharedPreferences.getInt("s", 6);

        settingsDialog = new SettingsDialog();

        multAnim = AnimationUtils.loadAnimation(this, R.anim.mult_anim_2);
        binding.btMultiplayer.setAnimation(multAnim);

        setupSounds();

        binding.helpButton.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                revealHelpButton();
            }
        });

        binding.settingsButton.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                revealSettingsButton();
            }
        });
    }

    private void setupSounds() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        bt_click_1 = soundPool.load(this, R.raw.bt_click_1, 1);
        menu_click = soundPool.load(this, R.raw.menu_click, 1);
        tick_tok_click = soundPool.load(this, R.raw.tic_tock_click, 1);
    }


    public void showHelp(View view) {
        soundPool.play(menu_click, 1, 1, 0, 0, 1);
        Intent intent = new Intent(MenuActivity.this, HelpActivity.class);
        startActivity(intent);
    }

    public void showSettings(View view) {
        soundPool.play(menu_click, 1, 1, 0, 0, 1);
        settingsDialog.s = s;
        settingsDialog.setListener(new SettingsDialog.SettingsListener() {
            @Override
            public void gridSizeSelected(int tag) {
                soundPool.play(tick_tok_click, 1, 1, 0, 0, 1);
                s = tag;
                sharedPreferences.edit().putInt("s", s).apply();
            }
        });
        settingsDialog.show(getSupportFragmentManager(), "settings");
    }

    public void openNOPDialog(View view) {
        soundPool.play(bt_click_1, 1, 1, 0, 0, 1);
        NumOfPlayersDialog dialog = new NumOfPlayersDialog();
        dialog.setListener(new NumOfPlayersDialog.Listener() {
            @Override
            public void onNSelected(int nop) {
                n = nop;
            }

            @Override
            public void ok() {
                soundPool.play(tick_tok_click, 1, 1, 0, 0, 1);
                startGame();
            }
        });
        dialog.show(getSupportFragmentManager(), "nopDialog");
    }

    private void startGame() {
        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
        intent.putExtra("n", n);
        intent.putExtra("s", s);
        startActivity(intent);
    }

    private void revealHelpButton() {
        int x = binding.helpButton.getWidth() / 2;
        int y = binding.helpButton.getHeight() / 2;

        float finalRadius = (float) Math.hypot(x, y);

        Animator animator = ViewAnimationUtils.createCircularReveal(binding.helpButton, x, y, 0, finalRadius);
        animator.setDuration(500);
        animator.start();
    }

    private void revealSettingsButton() {
        int x = binding.settingsButton.getWidth() / 2;
        int y = binding.settingsButton.getHeight() / 2;

        float finalRadius = (float) Math.hypot(x, y);

        Animator animator = ViewAnimationUtils.createCircularReveal(binding.settingsButton, x, y, 0, finalRadius);
        animator.setDuration(500);
        animator.start();
    }

    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
