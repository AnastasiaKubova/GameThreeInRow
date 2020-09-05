package com.example.gamethreeinrow.Utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.gamethreeinrow.App
import com.example.gamethreeinrow.R

class BitmapFactory {

    companion object {
        var bluePebbleIcon: Bitmap? = null
        var brownPebbleIcon: Bitmap? = null
        var grayPebbleIcon: Bitmap? = null
        var originPebbleIcon: Bitmap? = null
        var greenPebbleIcon: Bitmap? = null
        var backGroundIcon:Bitmap? = null
    }

    /**
     * Scale bitmaps based on surface view size.
     */
    fun initBitmaps(size: Int, margin: Int) {
        val iconBlue = BitmapFactory.decodeResource(
            App.mContext!!.resources,
            R.drawable.blue_pubble
        )
        val iconBrown = BitmapFactory.decodeResource(
            App.mContext!!.resources,
            R.drawable.brown_pubble
        )
        val iconGray = BitmapFactory.decodeResource(
            App.mContext!!.resources,
            R.drawable.gray_pubble
        )
        val iconOrigin = BitmapFactory.decodeResource(
            App.mContext!!.resources,
            R.drawable.origin_pubble
        )
        val iconGreen = BitmapFactory.decodeResource(
            App.mContext!!.resources,
            R.drawable.green_pubble
        )
        bluePebbleIcon = Bitmap.createScaledBitmap(iconBlue, size - margin, size - margin, true)
        brownPebbleIcon = Bitmap.createScaledBitmap(iconBrown, size - margin, size - margin, true)
        grayPebbleIcon = Bitmap.createScaledBitmap(iconGray, size - margin, size - margin, true)
        originPebbleIcon = Bitmap.createScaledBitmap(iconOrigin, size - margin, size - margin, true)
        greenPebbleIcon = Bitmap.createScaledBitmap(iconGreen, size - margin, size - margin, true)
    }

    fun initBackground(width: Int, height: Int) {
        val iconBg = BitmapFactory.decodeResource(
            App.mContext!!.resources,
            R.drawable.bg
        )
        backGroundIcon = Bitmap.createScaledBitmap(iconBg, width, height, true)
    }
}