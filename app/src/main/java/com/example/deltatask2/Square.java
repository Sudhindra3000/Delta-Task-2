package com.example.deltatask2;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Square {

    public RectF rectF;
    public Paint paint;

    public Square(float left,float top,float right,float bottom,String color){
        rectF=new RectF();
        paint=new Paint();
        paint.setColor(Color.parseColor(color));
        rectF.set(left, top, right, bottom);
    }
}
