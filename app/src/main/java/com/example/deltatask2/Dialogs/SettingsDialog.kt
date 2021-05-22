package com.example.deltatask2.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2.R
import com.example.deltatask2.ui.components.SizeSelection
import com.example.deltatask2.ui.theme.MarioRed
import com.example.deltatask2.ui.utils.WithTheme
import com.example.deltatask2.ui.utils.clickableWithoutRipple

class SettingsDialog(private val gridSizeSelected: (Int) -> Unit, var s: Int) :
    AppCompatDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext(), R.style.SettingsDialog)
            .apply {
                setView(ComposeView(requireContext()).apply {
                    setContent {
                        var selectedSize by remember { mutableStateOf(s) }
                        WithTheme {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    Modifier
                                        .background(MarioRed)
                                        .fillMaxWidth()
                                        .height(66.dp)
                                ) {
                                    Image(
                                        modifier = Modifier.align(Alignment.Center),
                                        painter = painterResource(id = R.drawable.settings),
                                        contentDescription = "Settings"
                                    )
                                    Image(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .align(Alignment.CenterStart)
                                            .clickableWithoutRipple(::dismiss),
                                        painter = painterResource(id = R.drawable.ic_settings_back),
                                        contentDescription = "Back"
                                    )
                                }
                                Text(
                                    text = "GRID SIZE",
                                    fontSize = 38.sp,
                                    color = MarioRed
                                )
                                SizeSelection(
                                    Modifier
                                        .padding(horizontal = 10.dp)
                                        .padding(bottom = 20.dp),
                                    value = selectedSize,
                                    onValueChange = {
                                        selectedSize = it
                                        gridSizeSelected(selectedSize)
                                    }
                                )
                            }
                        }
                    }
                })
            }.create()

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
