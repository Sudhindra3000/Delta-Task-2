package com.example.deltatask2.ui.utils

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.deltatask2.ui.theme.DotsAndBoxesTheme

@Composable
fun WithTheme(content: @Composable () -> Unit) {
    DotsAndBoxesTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}
