package com.example.deltatask2.Activities;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_STATIC_DP;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.deltatask2.Dialogs.NumOfPlayersDialog;
import com.example.deltatask2.Dialogs.SettingsDialog;
import com.example.deltatask2.R;
import com.example.deltatask2.databinding.ActivityMenuBinding;
import com.thekhaeng.pushdownanim.PushDownAnim;

import kotlin.Unit;

public class MenuActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private ActivityMenuBinding binding;

    private SharedPreferences sharedPreferences;

    private SettingsDialog settingsDialog;

    private int n = 2, s;
    private long lastClickTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
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
        getWindow().setEnterTransition(null);
        getWindow().getSharedElementEnterTransition().setDuration(400);


        PushDownAnim.setPushDownAnimTo(binding.settingsButton).setScale(MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC);
        PushDownAnim.setPushDownAnimTo(binding.helpButton).setScale(MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC);

        sharedPreferences = MenuActivity.this.getSharedPreferences("pref", MODE_PRIVATE);
        s = sharedPreferences.getInt("s", 6);

        settingsDialog = new SettingsDialog(this::gridSizeSelected);

        Animation multAnim = AnimationUtils.loadAnimation(this, R.anim.mult_anim_2);
        binding.btMultiPlayer.setAnimation(multAnim);

        binding.helpButton.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> revealHelpButton());

        binding.settingsButton.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> revealSettingsButton());
    }

    public void showHelp(View view) {
        playSoundInMedia(R.raw.menu_click);
        Intent intent = new Intent(MenuActivity.this, HelpActivity.class);
        startActivity(intent);
    }

    public void showSettings(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 800)
            return;
        lastClickTime = SystemClock.elapsedRealtime();
        playSoundInMedia(R.raw.menu_click);
        settingsDialog.setS(s);
        settingsDialog.show(getSupportFragmentManager(), "settings");
    }

    private Unit gridSizeSelected(int tag) {
        playSoundInMedia(R.raw.tic_tock_click);
        s = tag;
        sharedPreferences.edit().putInt("s", s).apply();
        return null;
    }

    public void openNOPDialog(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 800)
            return;
        lastClickTime = SystemClock.elapsedRealtime();
        playSoundInMedia(R.raw.bt_click_1);
        final NumOfPlayersDialog dialog = new NumOfPlayersDialog();
        dialog.setListener(new NumOfPlayersDialog.Listener() {
            @Override
            public void onNSelected(int nop) {
                n = nop;
            }

            @Override
            public void ok() {
                playSoundInMedia(R.raw.tic_tock_click);
                dialog.dismiss();
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

    private void playSoundInMedia(int resID) {
        MediaPlayer mediaPlayer = MediaPlayer.create(MenuActivity.this, resID);
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

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
