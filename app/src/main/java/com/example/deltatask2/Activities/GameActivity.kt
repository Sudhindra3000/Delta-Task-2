package com.example.deltatask2.Activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
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
import androidx.core.content.ContextCompat
import com.example.deltatask2.CustomViews.GameCanvas.CanvasListener
import com.example.deltatask2.Dialogs.QuitDialog
import com.example.deltatask2.R
import com.example.deltatask2.Utils.Result
import com.example.deltatask2.Utils.SortResultsByScore
import com.example.deltatask2.databinding.ActivityGameBinding
import com.thekhaeng.pushdownanim.PushDownAnim
import java.util.*

class GameActivity : AppCompatActivity(), OnCompletionListener {

    private var binding: ActivityGameBinding? = null

    private var n = 0
    private var currentPlayer = 0
    private var squareAdded = false
    private lateinit var scores: IntArray
    private lateinit var colors: Array<String>
    private var playerBitmaps: ArrayList<Bitmap>? = null
    private var borderBitmaps: ArrayList<Bitmap>? = null
    private var results: ArrayList<Result>? = null

    private val quitDialog = QuitDialog({
        playSoundInMedia(R.raw.tic_tock_click)
        startActivity(Intent(this@GameActivity, MenuActivity::class.java))
    }) { playSoundInMedia(R.raw.tic_tock_click) }

    private var vibrator: Vibrator? = null
    private var lastClickTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        PushDownAnim.setPushDownAnimTo(binding!!.btCancel)
            .setScale(PushDownAnim.MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC)
        PushDownAnim.setPushDownAnimTo(binding!!.btUndo)
            .setScale(PushDownAnim.MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        n = intent.getIntExtra("n", 2)
        val s = intent.getIntExtra("s", 6)
        setupIcons()
        colors = resources.getStringArray(R.array.playerColors)
        binding!!.gameCanvas.setGridSize(s)
        binding!!.gameCanvas.setPlayers(n, colors)
        binding!!.scoreBoard.setPlayers(n, playerBitmaps, borderBitmaps, colors)
        scores = IntArray(n)
        binding!!.gameCanvas.setListener(object : CanvasListener {
            override fun onGridEmpty() {
                binding!!.btUndo.visibility = View.INVISIBLE
            }

            override fun onPlayerChanged(index: Int) {
                playSoundInMedia(R.raw.wet_click)
                currentPlayer = index
                binding!!.scoreBoard.setCurrentPlayer(index)
                squareAdded = false
                binding!!.btUndo.visibility = View.VISIBLE
            }

            override fun onSquareAdded(player: Int) {
                currentPlayer = player
                scores[player]++
                binding!!.scoreBoard.setScores(scores)
                squareAdded = true
                binding!!.btUndo.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) vibrator!!.vibrate(
                    VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
                ) else vibrator!!.vibrate(200)
            }

            override fun onGridCompleted() {
                binding!!.btUndo.visibility = View.INVISIBLE
                binding!!.btCancel.visibility = View.INVISIBLE
                showResults()
            }
        })
    }

    private fun setupIcons() {
        playerBitmaps = ArrayList()
        playerBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.player1))
        playerBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.player2))
        playerBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.player3))
        playerBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.player4))
        playerBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.player5))
        playerBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.player6))
        borderBitmaps = ArrayList()
        borderBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.border1))
        borderBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.border2))
        borderBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.border3))
        borderBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.border4))
        borderBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.border5))
        borderBitmaps!!.add(getBitmapFromVectorDrawable(this, R.drawable.border6))
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
        val decreasingIndices = binding!!.gameCanvas.undo2()
        for (integer in decreasingIndices) {
            scores[integer!!]--
        }
        binding!!.scoreBoard.setScores(scores)
        if (squareAdded) if (binding!!.gameCanvas.currentPlayer == 0) binding!!.scoreBoard.setCurrentPlayer(
            n - 1
        ) else binding!!.scoreBoard.setCurrentPlayer(
            binding!!.gameCanvas.currentPlayer - 1
        ) else binding!!.scoreBoard.setCurrentPlayer(
            binding!!.gameCanvas.currentPlayer
        )
    }

    private fun showResults() {
        playSoundInMedia(R.raw.applause)
        results = ArrayList()
        setupImageId()
        for (i in 0 until n) results!![i].setScoreAndColor(scores[i], colors[i])
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
        cancel(binding!!.btCancel)
    }

    companion object {
        fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap {
            val drawable = ContextCompat.getDrawable(context!!, drawableId)
            val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }
}
