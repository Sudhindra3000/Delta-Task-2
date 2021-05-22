package com.example.deltatask2.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2.ui.theme.MarioRed

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SizeSelection(
    modifier: Modifier = Modifier,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        cells = GridCells.Fixed(4)
    ) {
        items((3..10).toList()) { size ->
            SizeButton(
                Modifier.padding(5.dp),
                text = "${size}x${size}",
                selected = value == size,
                onSelect = { onValueChange(size) }
            )
        }
    }
}

@Composable
fun SizeButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = { if (!selected) onSelect() },
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.background),
        border = BorderStroke(
            if (selected) 3.dp else 0.dp,
            MarioRed.copy(alpha = if (selected) 1f else 0f)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text(text = text, fontSize = 20.sp)
    }
}
