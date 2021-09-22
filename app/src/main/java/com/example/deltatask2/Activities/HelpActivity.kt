package com.example.deltatask2.Activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2.R
import com.example.deltatask2.ui.theme.LuigiGreen
import com.example.deltatask2.ui.theme.MarioRed
import com.example.deltatask2.ui.utils.WithTheme

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WithTheme {
                Scaffold(topBar = {
                    Box {
                        TopAppBar(
                            modifier = Modifier.height(70.dp),
                            title = {},
                            actions = {
                                IconButton(onClick = { this@HelpActivity.onBackPressed() }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.cancel_icon),
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                }
                            },
                            backgroundColor = MarioRed
                        )
                        Image(
                            modifier = Modifier.align(Alignment.Center),
                            painter = painterResource(id = R.drawable.help_image),
                            contentDescription = "Help"
                        )
                    }
                }) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "How to Play",
                            style = MaterialTheme.typography.h4.copy(fontSize = 36.sp),
                            color = LuigiGreen
                        )
                        ProvideTextStyle(
                            TextStyle(
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp
                            )
                        ) {
                            Text(text = stringResource(id = R.string.htp1))
                            Text(text = stringResource(id = R.string.htp2))
                            Text(text = stringResource(id = R.string.htp3))
                        }
                    }
                }
            }
        }
    }
}
