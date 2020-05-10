package com.example.deltatask2;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable{

    int score;
    String color;
    int imageId;

    public Result(int score, String color) {
        this.score = score;
        this.color = color;
    }

    public Result(int imageId) {
        this.imageId = imageId;
    }


    protected Result(Parcel in) {
        score = in.readInt();
        color = in.readString();
        imageId = in.readInt();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public void setScoreAndColor(int score, String color) {
        this.score = score;
        this.color=color;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
        dest.writeString(color);
        dest.writeInt(imageId);
    }
}
