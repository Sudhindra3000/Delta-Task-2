package com.example.deltatask2.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.deltatask2.adapters.NOPAdapter;
import com.example.deltatask2.R;
import com.example.deltatask2.databinding.NumOfPlayersDialogBinding;

public class NumOfPlayersDialog extends AppCompatDialogFragment {

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.NOPDialog);

        NumOfPlayersDialogBinding binding = NumOfPlayersDialogBinding.inflate(requireActivity().getLayoutInflater());
        builder.setView(binding.getRoot());

        String[] strings = requireContext().getResources().getStringArray(R.array.nop);

        NOPAdapter nopAdapter = new NOPAdapter(requireContext(), strings);
        binding.spinner.setAdapter(nopAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onNSelected(position + 2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btOk.setOnClickListener(v -> listener.ok());

        return builder.create();
    }

    public interface Listener {
        void onNSelected(int n);

        void ok();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
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
