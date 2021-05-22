package com.example.deltatask2.utils;

import java.util.Comparator;

public class SortResultsByScore implements Comparator<Result> {
    @Override
    public int compare(Result o1, Result o2) {
        return o2.getScore()-o1.getScore();
    }
}
