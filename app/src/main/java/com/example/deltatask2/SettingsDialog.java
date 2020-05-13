package com.example.deltatask2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class SettingsDialog extends AppCompatDialogFragment{

    ArrayList<Button> buttons;
    ArrayList<ImageView> borders;
    ImageButton back;
    int s;
    private SettingsListener listener;

    public void setListener(SettingsListener listener) {
        this.listener = listener;
    }

    public interface SettingsListener{
        void gridSizeSelected(int tag);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity(),R.style.TransparentDialogStyle);

        View view=getActivity().getLayoutInflater().inflate(R.layout.settings_dialog,null);
        builder.setView(view);

        back=view.findViewById(R.id.sback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        buttons=new ArrayList<>();
        setupButtons(view);
        for (Button button :buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.gridSizeSelected(Integer.parseInt(v.getTag().toString()));
                    showBorder(Integer.parseInt(v.getTag().toString()));
                }
            });
        }
        borders=new ArrayList<>();
        setupBorders(view);
        showBorder(s);

        return builder.create();
    }

    private void setupButtons(View view){
        buttons.add((Button) view.findViewById(R.id.g3));
        buttons.add((Button) view.findViewById(R.id.g4));
        buttons.add((Button) view.findViewById(R.id.g5));
        buttons.add((Button) view.findViewById(R.id.g6));
        buttons.add((Button) view.findViewById(R.id.g7));
        buttons.add((Button) view.findViewById(R.id.g8));
        buttons.add((Button) view.findViewById(R.id.g9));
        buttons.add((Button) view.findViewById(R.id.g10));
    }

    private void setupBorders(View view){
        borders.add((ImageView) view.findViewById(R.id.gsB3));
        borders.add((ImageView) view.findViewById(R.id.gsB4));
        borders.add((ImageView) view.findViewById(R.id.gsB5));
        borders.add((ImageView) view.findViewById(R.id.gsB6));
        borders.add((ImageView) view.findViewById(R.id.gsB7));
        borders.add((ImageView) view.findViewById(R.id.gsB8));
        borders.add((ImageView) view.findViewById(R.id.gsB9));
        borders.add((ImageView) view.findViewById(R.id.gsB10));
    }

    private void showBorder(int tag){
        for (ImageView imageView :borders) {
            imageView.setVisibility(View.INVISIBLE);
        }
        borders.get(tag-3).setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
