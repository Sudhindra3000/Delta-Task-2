package com.example.deltatask2.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.deltatask2.R;
import com.example.deltatask2.databinding.QuitDialogBinding;

public class QuitDialog extends AppCompatDialogFragment {

    private quitDialogListener listener;

    public void setListener(quitDialogListener listener) {
        this.listener = listener;
    }

    public interface quitDialogListener {
        void onYesClicked();

        void onNoClicked();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.QuitDialog);

        com.example.deltatask2.databinding.QuitDialogBinding binding = QuitDialogBinding.inflate(requireActivity().getLayoutInflater());
        builder.setView(binding.getRoot());

        binding.btY.setOnClickListener(v -> listener.onYesClicked());

        binding.btN.setOnClickListener(v -> listener.onNoClicked());

        return builder.create();
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
