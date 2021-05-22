package com.example.deltatask2.Activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.deltatask2.R
import com.example.deltatask2.Utils.Result
import com.example.deltatask2.databinding.ActivityResultsBinding
import java.util.*

class ResultsActivity : AppCompatActivity(), OnCompletionListener {

    private lateinit var binding: ActivityResultsBinding
    private lateinit var imageViews: ArrayList<ImageView>
    private lateinit var tvScores: ArrayList<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
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
        val n = intent.getIntExtra("n", 0)
        val results = intent.getParcelableArrayListExtra<Result>("sortedResults") ?: arrayListOf()
        setupLists()
        for (i in 0 until n) {
            imageViews[i].visibility = View.VISIBLE
            tvScores[i].visibility = View.VISIBLE
            imageViews[i].setImageBitmap(getBitmapFromVectorDrawable(this, results[i].imageId))
            tvScores[i].setTextColor(Color.parseColor(results[i].color))
            tvScores[i].text = results[i].score.toString()
        }
    }

    private fun setupLists() {
        imageViews = ArrayList()
        imageViews.add(binding.iv1)
        imageViews.add(binding.iv2)
        imageViews.add(binding.iv3)
        imageViews.add(binding.iv4)
        imageViews.add(binding.iv5)
        imageViews.add(binding.iv6)
        tvScores = ArrayList()
        tvScores.add(binding.ts1)
        tvScores.add(binding.ts2)
        tvScores.add(binding.ts3)
        tvScores.add(binding.ts4)
        tvScores.add(binding.ts5)
        tvScores.add(binding.ts6)
    }

    fun showMainMenu(view: View?) {
        playSoundInMedia()
        startActivity(Intent(this@ResultsActivity, MenuActivity::class.java))
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
