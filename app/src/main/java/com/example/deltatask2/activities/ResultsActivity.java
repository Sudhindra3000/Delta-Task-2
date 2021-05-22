package com.example.deltatask2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deltatask2.R;
import com.example.deltatask2.utils.Result;
import com.example.deltatask2.databinding.ActivityResultsBinding;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private ActivityResultsBinding binding;
    private ArrayList<ImageView> imageViews;
    private ArrayList<TextView> tvScores;

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
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        int n = getIntent().getIntExtra("n", 0);
        ArrayList<Result> results = getIntent().getParcelableArrayListExtra("sortedResults");

        setupLists();

        for (int i = 0; i < n; i++) {
            imageViews.get(i).setVisibility(View.VISIBLE);
            tvScores.get(i).setVisibility(View.VISIBLE);
            imageViews.get(i).setImageBitmap(getBitmapFromVectorDrawable(this, results.get(i).getImageId()));
            tvScores.get(i).setTextColor(Color.parseColor(results.get(i).getColor()));
            tvScores.get(i).setText(String.valueOf(results.get(i).getScore()));
        }
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
        playSoundInMedia();
        startActivity(new Intent(ResultsActivity.this, MenuActivity.class));
    }

    private void playSoundInMedia() {
        MediaPlayer mediaPlayer = MediaPlayer.create(ResultsActivity.this, R.raw.bt_click_1);
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
        startActivity(new Intent(ResultsActivity.this, MenuActivity.class));
    }
}
