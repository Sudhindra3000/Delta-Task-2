package com.example.deltatask2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deltatask2.databinding.ActivityResultsBinding;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private static final String TAG = "ResultsActivity";
    ActivityResultsBinding binding;
    private ArrayList<Result> results;
    private ArrayList<ImageView> imageViews;
    private ArrayList<TextView> tvScores;
    private SoundPool soundPool;
    private int bt_click_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        int n = getIntent().getIntExtra("n", 0);
        results = getIntent().getParcelableArrayListExtra("sortedResults");

        setupSound();
        setupLists();

        for (int i = 0; i < n; i++) {
            imageViews.get(i).setVisibility(View.VISIBLE);
            tvScores.get(i).setVisibility(View.VISIBLE);
            imageViews.get(i).setImageBitmap(getBitmapFromVectorDrawable(this, results.get(i).imageId));
            tvScores.get(i).setTextColor(Color.parseColor(results.get(i).color));
            tvScores.get(i).setText(String.valueOf(results.get(i).score));
        }
    }

    private void setupSound() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        bt_click_1 = soundPool.load(this, R.raw.bt_click_1, 1);
    }

    private void setupLists() {
        imageViews = new ArrayList<>();
        imageViews.add(binding.iv1);
        imageViews.add(binding.iv2);
        imageViews.add(binding.iv3);
        imageViews.add(binding.iv4);
        imageViews.add(binding.iv5);
        imageViews.add(binding.iv6);
        tvScores = new ArrayList<>();
        tvScores.add(binding.ts1);
        tvScores.add(binding.ts2);
        tvScores.add(binding.ts3);
        tvScores.add(binding.ts4);
        tvScores.add(binding.ts5);
        tvScores.add(binding.ts6);
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

    public void showMainMenu(View view) {
        soundPool.play(bt_click_1, (float) 0.4, (float) 0.4, 0, 0, 1);
        startActivity(new Intent(ResultsActivity.this, MenuActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResultsActivity.this, MenuActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
