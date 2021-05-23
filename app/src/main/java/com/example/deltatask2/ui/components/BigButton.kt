package com.example.deltatask2.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2.ui.theme.MarioRed

@Composable
fun BigButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = MarioRed),
        contentPadding = PaddingValues(
            start = 22.dp,
            top = 8.dp,
            end = 22.dp,
            bottom = 8.dp
        )
    ) {
        Text(text = text, color = Color.White, fontSize = 30.sp)
    }
}
