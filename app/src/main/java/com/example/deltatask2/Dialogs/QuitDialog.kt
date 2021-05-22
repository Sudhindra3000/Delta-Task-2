package com.example.deltatask2.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.deltatask2.R
import com.example.deltatask2.databinding.QuitDialogBinding

class QuitDialog(
    private val onYesClicked: () -> Unit,
    private val onNoClicked: () -> Unit
) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context, R.style.QuitDialog)
        val binding = QuitDialogBinding.inflate(requireActivity().layoutInflater)
        builder.setView(binding.root)
        binding.btY.setOnClickListener { onYesClicked() }
        binding.btN.setOnClickListener { onNoClicked() }
        return builder.create()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val decorView = activity!!.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    }
}
