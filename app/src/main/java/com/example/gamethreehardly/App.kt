package com.example.gamethreehardly

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class App : Application() {

    companion object {
        var mContext: Context? = null

        @JvmStatic
        fun getContex(): Context? {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}