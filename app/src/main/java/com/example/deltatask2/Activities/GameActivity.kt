package com.example.deltatask2.Activities

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.deltatask2.CustomViews.GameCanvas.CanvasListener
import com.example.deltatask2.Dialogs.QuitDialog
import com.example.deltatask2.R
import com.example.deltatask2.Utils.Result
import com.example.deltatask2.Utils.SortResultsByScore
import com.example.deltatask2.databinding.ActivityGameBinding
import com.example.deltatask2.ui.components.ScoreBoard
import com.example.deltatask2.ui.utils.WithTheme
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

// TODO: 5/24/2021 Scoreboard UI is converted to Compose Perfectly. But Game logic is fucked now
class GameActivity : AppCompatActivity(), OnCompletionListener {

    private lateinit var binding: ActivityGameBinding

    // Game
    private var n = 0
    private var squareAdded = false

    // Scoreboard
    private var currentPlayer = MutableStateFlow(0)
    private lateinit var scores: MutableStateFlow<List<Int>>

    private lateinit var colors: Array<String>

    private var results: ArrayList<Result>? = null

    private val quitDialog = QuitDialog(
        onYesClicked = {
            playSoundInMedia(R.raw.tic_tock_click)
            startActivity(Intent(this@GameActivity, MenuActivity::class.java))
        },
        onNoClicked = { playSoundInMedia(R.raw.tic_tock_click) }
    )

    private var vibrator: Vibrator? = null
    private var lastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        PushDownAnim.setPushDownAnimTo(binding.btCancel)
            .setScale(PushDownAnim.MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC)
        PushDownAnim.setPushDownAnimTo(binding.btUndo)
            .setScale(PushDownAnim.MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        n = intent.getIntExtra("n", 2)
        val s = intent.getIntExtra("s", 6)
        colors = resources.getStringArray(R.array.playerColors)
        binding.gameCanvas.setGridSize(s)
        binding.gameCanvas.setPlayers(n, colors)
        scores = MutableStateFlow(IntArray(n).toList())
        binding.scoreBoard.setContent {
            WithTheme {
                val currentPlayerIndex by currentPlayer.collectAsState()
                val scoresValue by scores.collectAsState()
                LaunchedEffect(currentPlayerIndex) {
                    println(currentPlayerIndex)
                }
                ScoreBoard(currentPlayerIndex = currentPlayerIndex, scores = scoresValue)
            }
        }
        binding.gameCanvas.setListener(object : CanvasListener {
            override fun onGridEmpty() {
                binding.btUndo.visibility = View.INVISIBLE
            }

            override fun onPlayerChanged(index: Int) {
                println("onPlayerChanged: index = $index")
                playSoundInMedia(R.raw.wet_click)
                currentPlayer.value = index
                squareAdded = false
                binding.btUndo.visibility = View.VISIBLE
            }

            override fun onSquareAdded(player: Int) {
                currentPlayer.value = player
                scores.value = scores.value.toMutableList().apply {
                    this[player]++
                }.toList()
                squareAdded = true
                binding.btUndo.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrator!!.vibrate(
                    VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
                ) else vibrator!!.vibrate(200)
            }

            override fun onGridCompleted() {
                binding.btUndo.visibility = View.INVISIBLE
                binding.btCancel.visibility = View.INVISIBLE
                showResults()
            }
        })
    }

    fun cancel(view: View?) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 400) return
        lastClickTime = SystemClock.elapsedRealtime()
        playSoundInMedia(R.raw.cancel_game_sound)
        quitDialog.show(supportFragmentManager, "quitDialog")
    }

    fun undo(view: View?) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 300) return
        lastClickTime = SystemClock.elapsedRealtime()
        playSoundInMedia(R.raw.bt_click_1)
        val decreasingIndices = binding.gameCanvas.undo2()
        for (integer in decreasingIndices) {
            scores.value = scores.value.toMutableList().apply {
                this[integer!!]--
            }.toList()
        }

        if (squareAdded) {
            if (binding.gameCanvas.currentPlayer == 0)
                currentPlayer.value = n - 1
            else
                currentPlayer.value = binding.gameCanvas.currentPlayer - 1
        } else currentPlayer.value = binding.gameCanvas.currentPlayer
    }

    private fun showResults() {
        playSoundInMedia(R.raw.applause)
        results = ArrayList()
        setupImageId()
        for (i in 0 until n) results!![i].setScoreAndColor(scores.value[i], colors[i])
        // TODO: 5/23/2021 Show Toast with Toast Library
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@GameActivity, ResultsActivity::class.java)
        Collections.sort(results, SortResultsByScore())
        intent.putExtra("n", n)
        intent.putExtra("sortedResults", results)
        startActivity(intent)
    }

    private fun setupImageId() {
        results!!.add(Result(R.drawable.player1))
        results!!.add(Result(R.drawable.player2))
        results!!.add(Result(R.drawable.player3))
        results!!.add(Result(R.drawable.player4))
        results!!.add(Result(R.drawable.player5))
        results!!.add(Result(R.drawable.player6))
    }

    private fun playSoundInMedia(resID: Int) {
        val mediaPlayer = MediaPlayer.create(this@GameActivity, resID)
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
        cancel(binding.btCancel)
    }
}
