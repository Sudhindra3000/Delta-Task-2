package com.example.deltatask2.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.deltatask2.R
import com.example.deltatask2.databinding.SettingsDialogBinding
import com.thekhaeng.pushdownanim.PushDownAnim
import java.util.*

class SettingsDialog(private val gridSizeSelected: (Int) -> Unit) : AppCompatDialogFragment() {

    private var binding: SettingsDialogBinding? = null
    private var buttons: ArrayList<Button>? = null
    private var borders: ArrayList<ImageView>? = null
    private var s = 0

    fun setS(s: Int) {
        this.s = s
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity, R.style.SettingsDialog)
        binding = SettingsDialogBinding.inflate(requireActivity().layoutInflater)
        builder.setView(binding!!.root)
        binding!!.btBack.setOnClickListener { v: View? -> dismiss() }
        PushDownAnim.setPushDownAnimTo(binding!!.btBack)
            .setScale(PushDownAnim.MODE_STATIC_DP, PushDownAnim.DEFAULT_PUSH_STATIC)
        buttons = ArrayList()
        initButtons()
        for (button in buttons!!) {
            button.setOnClickListener { v: View ->
                gridSizeSelected(v.tag.toString().toInt())
                showBorder(v.tag.toString().toInt())
            }
        }
        borders = ArrayList()
        initBorders()
        showBorder(s)
        return builder.create()
    }

    private fun initButtons() {
        buttons!!.add(binding!!.g3)
        buttons!!.add(binding!!.g4)
        buttons!!.add(binding!!.g5)
        buttons!!.add(binding!!.g6)
        buttons!!.add(binding!!.g7)
        buttons!!.add(binding!!.g8)
        buttons!!.add(binding!!.g9)
        buttons!!.add(binding!!.g10)
    }

    private fun initBorders() {
        borders!!.add(binding!!.gsB3)
        borders!!.add(binding!!.gsB4)
        borders!!.add(binding!!.gsB5)
        borders!!.add(binding!!.gsB6)
        borders!!.add(binding!!.gsB7)
        borders!!.add(binding!!.gsB8)
        borders!!.add(binding!!.gsB9)
        borders!!.add(binding!!.gsB10)
    }

    private fun showBorder(tag: Int) {
        for (imageView in borders!!) {
            imageView.visibility = View.INVISIBLE
        }
        borders!![tag - 3].visibility = View.VISIBLE
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val decorView = requireActivity().window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    }
}
