package com.example.deltatask2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.deltatask2.R;

public class NOPAdapter extends ArrayAdapter<String> {

    public NOPAdapter(@NonNull Context context, String[] strings) {
        super(context, 0, strings);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initDropdownView(position, convertView, parent);
    }

    private View initDropdownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_dropdown_item, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.tvSpinnerDropdown);
        String currentItem = (String) getItem(position);

        if (currentItem != null)
            textViewName.setText(currentItem);

        return convertView;
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.tvSpinner);
        String currentItem = (String) getItem(position);

        if (currentItem != null)
            textViewName.setText(currentItem);

        return convertView;
    }

}
