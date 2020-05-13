package com.example.deltatask2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameCanvas extends View {

    private final int HORIZONTAL = 135, VERTICAL = 468;
    private static final String TAG = "GameCanvas";
    private final String RED = "#FF6160", BLACK = "#494849";
    private int nDots, nLines, maxSquares, numOfPlayers, currentPlayer;
    private int oS, nS, a;
    private float rC, wL;
    private double oX, oY, x, y, rx, ry, rxS, ryS, X, Y;
    private boolean squareAdded = false;
    private int[] scores;
    private Paint paintGrid, paintLine;
    private Point[][] gridPattern;
    private ArrayList<Line> lines;
    private ArrayList<Square> squares;
    private Line line;
    private CanvasListener listener;
    private String[] colors;
    public boolean two = false;

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        lines = new ArrayList<>();
        squares = new ArrayList<>();

        paintGrid = new Paint();
        paintGrid.setStrokeCap(Paint.Cap.ROUND);
        paintGrid.setStrokeJoin(Paint.Join.ROUND);
        paintGrid.setAntiAlias(true);
        paintGrid.setColor(Color.parseColor(BLACK));

        paintLine = new Paint();
        paintLine.setStrokeCap(Paint.Cap.ROUND);
        paintLine.setStrokeJoin(Paint.Join.ROUND);
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.parseColor(RED));
    }

    public void setListener(CanvasListener listener) {
        this.listener = listener;
    }

    public interface CanvasListener {
        void onGridEmpty();

        void onPlayerChanged(int index);

        void onSquareAdded(int player);

        void onGridCompleted();
    }

    public void setGridSize(int n) {
        nDots = n;
        nLines = nDots - 1;
        maxSquares = nLines * nLines;
        gridPattern = new Point[nDots][nDots];
    }

    public void setPlayers(int numOfPlayers, String[] colors) {
        this.numOfPlayers = numOfPlayers;
        this.colors = new String[numOfPlayers];
        scores = new int[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++)
            this.colors[i] = colors[i];
        currentPlayer = numOfPlayers - 1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        oS = w;
        rC = (float) (oS * 0.02);
        wL = (float) (oS * 0.015);
        paintLine.setStrokeWidth(wL);
        Log.i(TAG, "onSizeChanged: s=" + oS);
        nS = (int) (oS - 2 * rC);
        a = nS / nLines;
        for (int i = 0; i <= nLines; i++) {
            for (int j = 0; j <= nLines; j++) {
                gridPattern[i][j] = new Point(a * i, a * j);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Square square : squares) {
            canvas.drawRect(square.rectF, square.paint);
        }
        for (Line line : lines) {
            canvas.drawLine(line.startX + rC, line.startY + rC, line.stopX + rC, line.stopY + rC, line.getPaint());
        }
        Point point;
        for (int i = 0; i <= nLines; i++) {
            for (int j = 0; j <= nLines; j++) {
                point = gridPattern[i][j];
                canvas.drawCircle(point.getX() + rC, point.getY() + rC, rC, paintGrid);
            }
        }
        if (squares.size() == maxSquares)
            listener.onGridCompleted();
        if (lines.size() == 0)
            listener.onGridEmpty();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        oX = event.getX();
        oY = event.getY();
        x = oX - rC;
        y = oY - rC;
        rx = x / a;
        ry = y / a;
        rxS = Math.floor(rx);
        ryS = Math.floor(ry);
        X = rx - rxS;
        Y = ry - ryS;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            line = new Line(paintLine);
            if (rxS < 5 && ryS < 5) {
                if (X >= Y) {
                    if (X + Y >= 1) {  //RIGHT
                        line.startX = (float) ((rxS + 1) * a);
                        line.startY = (float) (ryS * a);
                        line.stopX = (float) ((rxS + 1) * a);
                        line.stopY = (float) ((ryS + 1) * a);
                        line.setOrientation(VERTICAL);
                    } else if (X + Y < 1) {  //BOTTOM
                        line.startX = (float) (rxS * a);
                        line.startY = (float) (ryS * a);
                        line.stopX = (float) ((rxS + 1) * a);
                        line.stopY = (float) (ryS * a);
                        line.setOrientation(HORIZONTAL);
                    }
                } else if (X < Y) {
                    if (X + Y >= 1) {  //TOP
                        line.startX = (float) (rxS * a);
                        line.startY = (float) ((ryS + 1) * a);
                        line.stopX = (float) ((rxS + 1) * a);
                        line.stopY = (float) ((ryS + 1) * a);
                        line.setOrientation(HORIZONTAL);
                    } else if (X + Y < 1) {  //LEFT
                        line.startX = (float) (rxS * a);
                        line.startY = (float) (ryS * a);
                        line.stopX = (float) (rxS * a);
                        line.stopY = (float) ((ryS + 1) * a);
                        line.setOrientation(VERTICAL);
                    }
                }
                boolean found = false;
                for (Line line1 : lines) {
                    if (line.equals(line1)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    if (!squareAdded)
                        nextPlayer();
                    line.playerIndex=currentPlayer;
                    line.setColor(colors[currentPlayer]);
                    lines.add(line);
                    checkForSquare(line);
                    if (!squareAdded)
                        listener.onPlayerChanged(currentPlayer);
                    postInvalidate();
                    return true;
                }
            }
//            }
        }
        return true;
    }

    private void checkForSquare(Line line) {
        Square s1, s2;
        boolean exists1 = false, exists2 = false;
        float startX = line.startX, startY = line.startY, stopX = line.stopX, stopY = line.stopY;
        if (line.getOrientation() == HORIZONTAL) {
            Line tL = new Line(startX, startY - a, startX, startY);
            Line tR = new Line(stopX, stopY - a, stopX, stopY);
            Line tT = new Line(startX, startY - a, stopX, stopY - a);
            Line bL = new Line(startX, startY + a, startX, startY);
            Line bR = new Line(stopX, stopY + a, stopX, stopY);
            Line bB = new Line(startX, startY + a, stopX, stopY + a);
            for (Line line1 : lines) {
                if (tL.equals(line1)) {

                    for (Line line2 : lines) {
                        if (tR.equals(line2)) {

                            for (Line line3 : lines) {
                                if (tT.equals(line3)) {
                                    exists1 = true;
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            if (exists1) {
                s1 = new Square(startX + rC + wL, startY - a + rC + wL, stopX + rC - wL, startY + rC - wL, colors[currentPlayer]);
                s1.lineIndex = lines.indexOf(line);
                squares.add(s1);
            }
            for (Line line1 : lines) {
                if (bL.equals(line1)) {

                    for (Line line2 : lines) {
                        if (bR.equals(line2)) {

                            for (Line line3 : lines) {
                                if (bB.equals(line3)) {
                                    exists2 = true;
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            if (exists2) {
                s2 = new Square(startX + rC + wL, startY + rC + wL, stopX + rC - wL, startY + a + rC - wL, colors[currentPlayer]);
                s2.lineIndex = lines.indexOf(line);
                squares.add(s2);
            }
        } else {
            Line lT = new Line(startX - a, startY, startX, startY);
            Line lL = new Line(startX - a, startY, stopX - a, stopY);
            Line lB = new Line(stopX - a, stopY, stopX, stopY);
            Line rT = new Line(startX, startY, startX + a, startY);
            Line rR = new Line(startX + a, startY, stopX + a, stopY);
            Line rB = new Line(stopX, stopY, stopX + a, stopY);

            for (Line line1 : lines) {
                if (lT.equals(line1)) {

                    for (Line line2 : lines) {
                        if (lL.equals(line2)) {

                            for (Line line3 : lines) {
                                if (lB.equals(line3)) {
                                    exists1 = true;
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            if (exists1) {
                s1 = new Square(startX - a + rC + wL, startY + rC + wL, startX + rC - wL, stopY + rC - wL, colors[currentPlayer]);
                s1.lineIndex = lines.indexOf(line);
                squares.add(s1);
            }
            for (Line line1 : lines) {
                if (rT.equals(line1)) {

                    for (Line line2 : lines) {
                        if (rR.equals(line2)) {

                            for (Line line3 : lines) {
                                if (rB.equals(line3)) {
                                    exists2 = true;
                                    break;
                                }
                            }

                        }
                    }
                }
            }
            if (exists2) {
                s2 = new Square(startX + rC + wL, startY + rC + wL, startX + a + rC - wL, stopY + rC - wL, colors[currentPlayer]);
                s2.lineIndex = lines.indexOf(line);
                squares.add(s2);
            }
        }
        if (exists1 | exists2) {
            if (exists1 && exists2)
                two = true;
            else
                two = false;
            squareAdded = true;
            listener.onSquareAdded(currentPlayer);
            if (exists1 && exists2)
                listener.onSquareAdded(currentPlayer);
        } else
            squareAdded = false;
    }

    private void nextPlayer() {
        if (currentPlayer != numOfPlayers - 1)
            currentPlayer++;
        else
            currentPlayer = 0;
    }

    private void previousPlayer() {
        if (currentPlayer != 0)
            currentPlayer--;
        else
            currentPlayer = numOfPlayers - 1;
    }

    public ArrayList<Integer> undo2() {
        int lastLineIndex = lines.size() - 1;
        ArrayList<Square> removingSquares = new ArrayList<>();
        ArrayList<Integer> decreasingPlayerIndices = new ArrayList<>();
        for (Square square : squares) {
            if (square.lineIndex == lastLineIndex) {
                removingSquares.add(square);
                decreasingPlayerIndices.add(lines.get(lastLineIndex).playerIndex);
            }
        }
        if (removingSquares.size()==0){
            previousPlayer();
            Log.i(TAG, "undo2: previousPlayer");
        }
        squares.removeAll(removingSquares);
        lines.remove(lines.size() - 1);
        Log.i(TAG, "undo2: currentPlayer="+currentPlayer);
        postInvalidate();
        return decreasingPlayerIndices;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
}
