package com.example.gamethreeinrow

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gamethreeinrow.Enum.ColorType
import com.example.gamethreeinrow.GameObject.BasePebble
import com.example.gamethreeinrow.Utility.BitmapFactory
import com.example.gamethreeinrow.Utility.DrawingThread
import com.example.gamethreeinrow.Utility.GenerateObjects
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.statistic_game.*
import javax.xml.transform.sax.TemplatesHandler


class MainActivity : AppCompatActivity(), View.OnTouchListener, SurfaceHolder.Callback, DrawingThread.DrawingListener {

    private var mThread: DrawingThread? = null
    private val COINT_PEBBLE = 6
    var countGrayPebble: Int = 0
    var countBrownPebble: Int = 0
    var countBluePebble: Int = 0
    var countOriginPebble: Int = 0
    var countGreenPebble: Int = 0
    private var pebblesList: MutableList<BasePebble> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        game_play_view.setOnTouchListener(this)
        game_play_view.getHolder().addCallback(this)
        refresh.setOnClickListener {
            handleRefreshClick()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action === MotionEvent.ACTION_MOVE) {
            mThread?.handleMoveTouch(event)
        }
        if (event?.action === MotionEvent.ACTION_DOWN) {
            mThread?.handleTouch(event)
        }
        if (event?.action === MotionEvent.ACTION_UP) {
            mThread?.handleUpTouch(event)
        }
        return true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        mThread?.updateSize(width, height)
        if (pebblesList.count() == 0) {
            pebblesList = GenerateObjects.generatePebblesLevel( COINT_PEBBLE, COINT_PEBBLE, (width / COINT_PEBBLE).toFloat())
        }
        mThread?.refresh(pebblesList)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mThread?.quit()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mThread = DrawingThread(holder, COINT_PEBBLE, this)
    }

    private fun handleRefreshClick() {
        pebblesList = GenerateObjects.generatePebblesLevel( COINT_PEBBLE, COINT_PEBBLE, (game_play_view.width / COINT_PEBBLE).toFloat())
        mThread?.refresh(pebblesList)
        countGrayPebble = 0
        countBrownPebble = 0
        countBluePebble = 0
        countOriginPebble = 0
        countGreenPebble = 0
        setCountPebbleText(brown_pubble_count, countBrownPebble)
        setCountPebbleText(blue_pubble_count, countBluePebble)
        setCountPebbleText(origin_pubble_count, countOriginPebble)
        setCountPebbleText(gray_pubble_count, countGrayPebble)
        setCountPebbleText(green_pubble_count, countGreenPebble)
    }

    private fun setCountPebbleText(view: TextView, count: Int) {
        view.text = count.toString()
    }

    override fun changePebbleCountListener(listObjs: MutableList<BasePebble>) {
        for (obj in listObjs) {
            when (obj.colorType) {
                ColorType.Brown -> {
                    countBrownPebble++
                    setCountPebbleText(brown_pubble_count, countBrownPebble)
                }
                ColorType.Blue -> {
                    countBluePebble++
                    setCountPebbleText(blue_pubble_count, countBluePebble)
                }
                ColorType.Origin -> {
                    countOriginPebble++
                    setCountPebbleText(origin_pubble_count, countOriginPebble)
                }
                ColorType.Gray -> {
                    countGrayPebble++
                    setCountPebbleText(gray_pubble_count, countGrayPebble)
                }
                ColorType.Green -> {
                    countGreenPebble++
                    setCountPebbleText(green_pubble_count, countGreenPebble)
                }
            }
        }
    }
}