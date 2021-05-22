package com.example.deltatask2.ui.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import com.example.deltatask2.ui.theme.DotsAndBoxesTheme

@Composable
fun WithTheme(content: @Composable () -> Unit) {
    DotsAndBoxesTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

/**
 * Add Clickable Modifier without ripple
 */
fun Modifier.clickableWithoutRipple(
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickableWithoutRipple"
        properties["onClick"] = onClick
    }
) {
    clickable(
        MutableInteractionSource(),
        indication = null,
        onClick = onClick
    )
}
