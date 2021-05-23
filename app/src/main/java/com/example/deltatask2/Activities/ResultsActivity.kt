package com.example.deltatask2.Activities

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deltatask2.R
import com.example.deltatask2.Utils.Result
import com.example.deltatask2.ui.components.BigButton
import com.example.deltatask2.ui.theme.MarioRed
import com.example.deltatask2.ui.utils.WithTheme
import com.example.deltatask2.ui.utils.toColor
import java.util.*

class ResultsActivity : AppCompatActivity(), OnCompletionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        val results = (intent.getParcelableArrayListExtra("sortedResults")
            ?: listOf<Result>()).filter { it.color != null }
        setContent {
            WithTheme {
                Box(Modifier.fillMaxSize()) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Final Scores", fontSize = 46.sp, color = MarioRed)
                        results.forEach { res ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    modifier = Modifier.size(70.dp),
                                    painter = painterResource(id = res.imageId),
                                    contentDescription = ""
                                )
                                Spacer(Modifier.width(24.dp))
                                Text(
                                    text = "${res.score}",
                                    fontSize = 36.sp,
                                    color = res.color.toColor()
                                )
                            }
                        }
                    }
                    BigButton(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        text = "Main Menu",
                        onClick = {
                            playSoundInMedia()
                            startActivity(Intent(this@ResultsActivity, MenuActivity::class.java))
                        }
                    )
                }
            }
        }
    }

    private fun playSoundInMedia() {
        val mediaPlayer = MediaPlayer.create(this@ResultsActivity, R.raw.bt_click_1)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener(this)
    }

    override fun onCompletion(mp: MediaPlayer) {
        var mp: MediaPlayer? = mp
        if (mp != null) {
            mp.release()
            mp = null
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ResultsActivity, MenuActivity::class.java))
    }
}
