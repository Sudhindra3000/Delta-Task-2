package com.example.deltatask2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.deltatask2.databinding.ActivityGameBinding;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

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
    private SoundPool soundPool;
    private int bt_click_1, wet_click, applause;

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

        setupSounds();
        setupIcons();
        setupColors();
        binding.gameCanvas.setGridSize(s);
        binding.gameCanvas.setPlayers(n, colors);
        binding.scoreBoard.setPlayers(n, playerBitmaps, borderBitmaps, colors);
        scores = new int[n];
        binding.gameCanvas.setListener(new GameCanvas.CanvasListener() {
            @Override
            public void onPlayerChanged(int index) {
                soundPool.play(wet_click, 1, 1, 0, 0, 1);
                currentPlayer = index;
                binding.scoreBoard.setCurrentPlayer(index);
                squareAdded = false;
                Log.i(TAG, "onPlayerChanged: currentPlayer=" + index);
                binding.btUndo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSquareAdded(int player) {
                currentPlayer = player;
                scores[player]++;
                binding.scoreBoard.setScores(scores);
                squareAdded = true;
                Log.i(TAG, "onSquareAdded: squareAdded");
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
        wet_click = soundPool.load(this, R.raw.wet_click, 1);
        applause = soundPool.load(this, R.raw.applause, 1);
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
        final QuitDialog quitDialog = new QuitDialog();
        quitDialog.setListener(new QuitDialog.quitDialogListener() {
            @Override
            public void onYesClicked() {
                soundPool.play(bt_click_1, 1, 1, 0, 0, 1);
                startActivity(new Intent(GameActivity.this, MenuActivity.class));
            }

            @Override
            public void onNoClicked() {
                soundPool.play(bt_click_1, 1, 1, 0, 0, 1);
                quitDialog.dismiss();
            }
        });
        quitDialog.show(getSupportFragmentManager(), "quitDialog");
    }

    public void undo(View view) {
        soundPool.play(bt_click_1, 1, 1, 0, 0, 1);
        binding.btUndo.setVisibility(View.INVISIBLE);
        if (squareAdded) {
            Log.i(TAG, "undo: scores[currentPlayer]=" + scores[currentPlayer]);
            if (two)
                scores[currentPlayer] = scores[currentPlayer] - 2;
            else
                scores[currentPlayer]--;
            Log.i(TAG, "undo: scores[currentPlayer]=" + scores[currentPlayer]);
        }
        binding.scoreBoard.undo(squareAdded, two);
        binding.gameCanvas.undo(squareAdded);
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
        soundPool.play(applause, 1, 1, 0, 0, 1);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
