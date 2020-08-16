package com.example.deltatask2.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.deltatask2.R;
import com.example.deltatask2.databinding.SettingsDialogBinding;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_STATIC_DP;

public class SettingsDialog extends AppCompatDialogFragment {

    private SettingsDialogBinding binding;

    private ArrayList<Button> buttons;
    private ArrayList<ImageView> borders;
    private int s;
    private SettingsListener listener;

    public void setListener(SettingsListener listener) {
        this.listener = listener;
    }

    public void setS(int s) {
        this.s = s;
    }

    public interface SettingsListener {
        void gridSizeSelected(int tag);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.SettingsDialog);

        binding = SettingsDialogBinding.inflate(requireActivity().getLayoutInflater());
        builder.setView(binding.getRoot());

        binding.btBack.setOnClickListener(v -> dismiss());

        PushDownAnim.setPushDownAnimTo(binding.btBack).setScale(MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC);

        buttons = new ArrayList<>();
        initButtons();
        for (Button button : buttons) {
            button.setOnClickListener(v -> {
                listener.gridSizeSelected(Integer.parseInt(v.getTag().toString()));
                showBorder(Integer.parseInt(v.getTag().toString()));
            });
        }
        borders = new ArrayList<>();
        initBorders();
        showBorder(s);

        return builder.create();
    }

    private void initButtons() {
        buttons.add(binding.g3);
        buttons.add(binding.g4);
        buttons.add(binding.g5);
        buttons.add(binding.g6);
        buttons.add(binding.g7);
        buttons.add(binding.g8);
        buttons.add(binding.g9);
        buttons.add(binding.g10);
    }

    private void initBorders() {
        borders.add(binding.gsB3);
        borders.add(binding.gsB4);
        borders.add(binding.gsB5);
        borders.add(binding.gsB6);
        borders.add(binding.gsB7);
        borders.add(binding.gsB8);
        borders.add(binding.gsB9);
        borders.add(binding.gsB10);
    }

    private void showBorder(int tag) {
        for (ImageView imageView : borders) {
            imageView.setVisibility(View.INVISIBLE);
        }
        borders.get(tag - 3).setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }
}
