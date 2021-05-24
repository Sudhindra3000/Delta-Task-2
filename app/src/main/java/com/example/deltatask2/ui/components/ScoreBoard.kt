package com.example.deltatask2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import com.example.deltatask2.R
import com.example.deltatask2.ui.utils.toColor

private val playerIcons = listOf(
    R.drawable.player1,
    R.drawable.player2,
    R.drawable.player3,
    R.drawable.player4,
    R.drawable.player5,
    R.drawable.player6
)

private val borders = listOf(
    R.drawable.border1,
    R.drawable.border2,
    R.drawable.border3,
    R.drawable.border4,
    R.drawable.border5,
    R.drawable.border6,
)

private val playerColors: List<Color>
    @Composable
    get() = stringArrayResource(R.array.playerColors).map { it.toColor() }

@Composable
fun ScoreBoard(
    modifier: Modifier = Modifier,
    currentPlayerIndex: Int,
    scores: List<Int>
) {
    Row(modifier, horizontalArrangement = Arrangement.SpaceEvenly) {
        scores.forEachIndexed { index, score ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.weight(1.5f)) {
                    Image(
                        painter = painterResource(id = playerIcons[index]),
                        contentDescription = "Player ${index + 1}"
                    )
                    if (index == currentPlayerIndex)
                        Image(
                            painter = painterResource(id = borders[index]),
                            contentDescription = "Current Player"
                        )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = "$score",
                    color = playerColors[index]
                )
            }
        }
    }
}
