package com.example.deltatask2.Activities;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.deltatask2.CustomViews.GameCanvas;
import com.example.deltatask2.Dialogs.QuitDialog;
import com.example.deltatask2.R;
import com.example.deltatask2.Utils.Result;
import com.example.deltatask2.Utils.SortResultsByScore;
import com.example.deltatask2.databinding.ActivityGameBinding;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;
import java.util.Collections;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_STATIC_DP;

public class GameActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private ActivityGameBinding binding;

    private int n;
    private int currentPlayer = 0;
    private boolean squareAdded = false;
    private int[] scores;
    private String[] colors;
    private ArrayList<Bitmap> playerBitmaps, borderBitmaps;
    private ArrayList<Result> results;

    private Vibrator vibrator;

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
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);


        PushDownAnim.setPushDownAnimTo(binding.btCancel).setScale(MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC);
        PushDownAnim.setPushDownAnimTo(binding.btUndo).setScale(MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        n = getIntent().getIntExtra("n", 2);
        int s = getIntent().getIntExtra("s", 6);

        setupIcons();
        colors = getResources().getStringArray(R.array.playerColors);
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

    public void showGameOverToast(String color) {
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
        showGameOverToast(colors[currentPlayer]);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
            Collections.sort(results, new SortResultsByScore());
            intent.putExtra("n", n);
            intent.putExtra("sortedResults", results);
            startActivity(intent);
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

    private void playSoundInMedia(int resID) {
        MediaPlayer mediaPlayer = MediaPlayer.create(GameActivity.this, resID);
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
        cancel(binding.btCancel);
    }
}
