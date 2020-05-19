package com.example.deltatask2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.deltatask2.CustomViews.GameCanvas;
import com.example.deltatask2.Dialogs.QuitDialog;
import com.example.deltatask2.R;
import com.example.deltatask2.Utils.Result;
import com.example.deltatask2.Utils.SortResultsByScore;
import com.example.deltatask2.databinding.ActivityGameBinding;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    ActivityGameBinding binding;
    private int n, s, currentPlayer = 0;
    private boolean squareAdded = false, two;
    private static final String TAG = "MainActivity";
    private int[] scores;
    private String c1 = "#FF6160", c2 = "#38DF9C", c3 = "#60C5FF", c4 = "#FF60AA", c5 = "#8060FF", c6 = "#4545F3";
    private String[] colors;
    private ArrayList<Bitmap> playerBitmaps, borderBitmaps;
    private ArrayList<Result> results;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        n = getIntent().getIntExtra("n", 2);
        s = getIntent().getIntExtra("s", 6);

        setupIcons();
        setupColors();
        binding.gameCanvas.setGridSize(s);
        binding.gameCanvas.setPlayers(n, colors);
        binding.scoreBoard.setPlayers(n, playerBitmaps, borderBitmaps, colors);
        scores = new int[n];
        binding.gameCanvas.setListener(new GameCanvas.CanvasListener() {
            @Override
            public void onGridEmpty() {
                binding.btUndo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPlayerChanged(int index) {
                playSoundInMedia(R.raw.wet_click);
                currentPlayer = index;
                binding.scoreBoard.setCurrentPlayer(index);
                squareAdded = false;
                binding.btUndo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSquareAdded(int player) {
                currentPlayer = player;
                scores[player]++;
                binding.scoreBoard.setScores(scores);
                squareAdded = true;
                binding.btUndo.setVisibility(View.VISIBLE);
                two = binding.gameCanvas.two;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                else
                    vibrator.vibrate(200);

            }

            @Override
            public void onGridCompleted() {
                binding.btUndo.setVisibility(View.INVISIBLE);
                binding.btCancel.setVisibility(View.INVISIBLE);
                showResults();
            }
        });
    }

    private void setupIcons() {
        playerBitmaps = new ArrayList<>();
        playerBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.player1));
        playerBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.player2));
        playerBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.player3));
        playerBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.player4));
        playerBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.player5));
        playerBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.player6));
        borderBitmaps = new ArrayList<>();
        borderBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.border1));
        borderBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.border2));
        borderBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.border3));
        borderBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.border4));
        borderBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.border5));
        borderBitmaps.add(getBitmapFromVectorDrawable(this, R.drawable.border6));
    }

    private void setupColors() {
        colors = new String[6];
        colors[0] = c1;
        colors[1] = c2;
        colors[2] = c3;
        colors[3] = c4;
        colors[4] = c5;
        colors[5] = c6;
    }

    public void cancel(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 400)
            return;
        lastClickTime = SystemClock.elapsedRealtime();
        final QuitDialog quitDialog = new QuitDialog();
        playSoundInMedia(R.raw.cancel_game_sound);
        quitDialog.setListener(new QuitDialog.quitDialogListener() {
            @Override
            public void onYesClicked() {
                playSoundInMedia(R.raw.tic_tock_click);
                startActivity(new Intent(GameActivity.this, MenuActivity.class));
            }

            @Override
            public void onNoClicked() {
                playSoundInMedia(R.raw.tic_tock_click);
                quitDialog.dismiss();
            }
        });
        quitDialog.show(getSupportFragmentManager(), "quitDialog");
    }

    public void undo(View view) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 300)
            return;
        lastClickTime = SystemClock.elapsedRealtime();
        playSoundInMedia(R.raw.bt_click_1);
        ArrayList<Integer> decreasingIndices = binding.gameCanvas.undo2();
        for (Integer integer : decreasingIndices) {
            scores[integer]--;
        }
        binding.scoreBoard.setScores(scores);
        if (squareAdded)
            if (binding.gameCanvas.getCurrentPlayer() == 0)
                binding.scoreBoard.setCurrentPlayer(n - 1);
            else
                binding.scoreBoard.setCurrentPlayer(binding.gameCanvas.getCurrentPlayer() - 1);
        else
            binding.scoreBoard.setCurrentPlayer(binding.gameCanvas.getCurrentPlayer());
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void showGOToast(String color) {
        View toastLayout = getLayoutInflater().inflate(R.layout.go_toast_layout, (ViewGroup) findViewById(R.id.goToastRoot));
        ConstraintLayout constraintLayout = toastLayout.findViewById(R.id.toastBack);
        constraintLayout.setBackgroundColor(Color.parseColor(color));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }

    private void showResults() {
        playSoundInMedia(R.raw.applause);
        results = new ArrayList<>();
        setupImageId();
        for (int i = 0; i < n; i++)
            results.get(i).setScoreAndColor(scores[i], colors[i]);
        showGOToast(colors[currentPlayer]);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
                Collections.sort(results, new SortResultsByScore());
                intent.putExtra("n", n);
                intent.putExtra("sortedResults", results);
                startActivity(intent);
            }
        }, 2000);
    }

    private void setupImageId() {
        results.add(new Result(R.drawable.player1));
        results.add(new Result(R.drawable.player2));
        results.add(new Result(R.drawable.player3));
        results.add(new Result(R.drawable.player4));
        results.add(new Result(R.drawable.player5));
        results.add(new Result(R.drawable.player6));
    }

    @Override
    public void onBackPressed() {
        cancel(binding.btCancel);
    }

    private void playSoundInMedia(int resID) {
        mediaPlayer = MediaPlayer.create(GameActivity.this, resID);
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
