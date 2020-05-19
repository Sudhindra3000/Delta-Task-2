package com.example.deltatask2.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.deltatask2.Utils.Point;
import com.example.deltatask2.R;

import java.util.ArrayList;

public class ScoreBoard extends View {

    private static final String TAG = "ScoreBoard";
    private int nP, currentPlayer = 0;
    private float w;
    private float h;
    private float a;
    private int s;
    private float r;
    private float tS;
    private float lS;
    private String[] textColors;
    private Point circleOrigin;
    private Paint textPaint;
    private RectF borderRectF;
    private ArrayList<Integer> scores;
    private ArrayList<Point> scoreOrigins;
    private ArrayList<Bitmap> playerBitmaps, borderBitmaps;
    private ArrayList<RectF> rectFS;
    private final String BORDER = "#494849";

    public ScoreBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        playerBitmaps = new ArrayList<>();
        borderBitmaps = new ArrayList<>();
        borderRectF = new RectF();
        rectFS = new ArrayList<>();
        scoreOrigins = new ArrayList<>();
        circleOrigin = new Point();
        textPaint = new Paint();
        scores = new ArrayList<>();
        textPaint.setColor(Color.parseColor(BORDER));
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Typeface typeface= ResourcesCompat.getFont(getContext(), R.font.bangers);
        textPaint.setTypeface(typeface);
    }

    public void setPlayers(int n, ArrayList<Bitmap> players, ArrayList<Bitmap> borders, String[] colors) {
        nP = n;
        textColors = new String[n];
        for (int i = 0; i < n; i++) {
            playerBitmaps.add(players.get(i));
            borderBitmaps.add(borders.get(i));
            scores.add(i, 0);
            textColors[i] = colors[i];
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        a = w / nP;
        s = h * 3 / 5;
        r = s;
        tS = h / 5;
        lS = (a - s) / 2;
        for (int i = 0; i < nP; i++) {
            rectFS.add(new RectF(lS + i * a, tS, lS + i * a + s, tS + s));
            scoreOrigins.add(new Point((float) ((i + 0.5) * a ), tS + s + 20f + 10f));
            scores.add(i);
        }
    }

    public void setScores(int[] scoresArray) {
        for (int i = 0; i < nP; i++)
            scores.set(i, scoresArray[i]);
        postInvalidate();
    }

    public void setCurrentPlayer(int index) {
        if (index + 1 != nP)
            currentPlayer = index + 1;
        else
            currentPlayer = 0;
        Log.i(TAG, "setCurrentPlayer: currentPlayer="+currentPlayer);
        postInvalidate();
    }

    private void circleAt(int index) {
        circleOrigin.setCoordinates(lS + index * a + s / 2, tS + s / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circleAt(2);
        for (int i = 0; i < nP; i++) {
            canvas.drawBitmap(playerBitmaps.get(i), null, rectFS.get(i), null);
            textPaint.setColor(Color.parseColor(textColors[i]));
            canvas.drawText(String.valueOf(scores.get(i)), scoreOrigins.get(i).getX(), scoreOrigins.get(i).getY(), textPaint);
        }
        borderRectF = rectFS.get(currentPlayer);
        borderRectF.set(borderRectF.left, borderRectF.top, borderRectF.right, borderRectF.bottom);
        canvas.drawBitmap(borderBitmaps.get(currentPlayer), null, borderRectF, null);
    }
}
