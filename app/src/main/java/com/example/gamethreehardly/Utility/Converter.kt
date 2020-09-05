package com.example.gamethreehardly.Utility

import android.content.Context
import android.util.DisplayMetrics
import com.example.gamethreehardly.App

object Converter {

    fun convertDpToPixel(dp: Int): Float {
        return convertDpToPixel(dp.toFloat())
    }

    fun convertDpToPixel(dp: Float): Float {
        return dp * (App.getContex()!!.resources!!.displayMetrics!!.densityDpi.toFloat()
                / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Int): Float {
        return convertPixelsToDp(px.toFloat())
    }

    fun convertPixelsToDp(px: Float): Float {
        return px / (App.getContex()!!.resources!!.displayMetrics!!.densityDpi.toFloat()
                / DisplayMetrics.DENSITY_DEFAULT)
    }
}