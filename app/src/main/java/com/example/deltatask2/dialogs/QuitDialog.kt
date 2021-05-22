package com.example.deltatask2.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2.R
import com.example.deltatask2.ui.components.DialogButton
import com.example.deltatask2.ui.theme.LuigiGreen
import com.example.deltatask2.ui.utils.WithTheme

class QuitDialog(
    private val onYesClicked: () -> Unit,
    private val onNoClicked: () -> Unit
) : AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context, R.style.QuitDialog)
        builder.setView(ComposeView(requireContext()).apply {
            setContent {
                WithTheme {
                    Column(
                        Modifier
                            .fillMaxHeight(0.35f)
                            .background(LuigiGreen),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Do you want to Quit the Game?",
                            color = Color.White,
                            fontSize = 56.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(36.dp))
                        Row(
                            Modifier.fillMaxWidth(0.8f),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            DialogButton(text = "Yes", onClick = onYesClicked)
                            DialogButton(text = "No", onClick = onNoClicked)
                        }
                    }
                }
            }
        })
        return builder.create()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val decorView = requireActivity().window.decorView
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
