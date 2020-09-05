package com.example.gamethreeinrow.GameObject

import android.graphics.*
import com.example.gamethreeinrow.Enum.ColorType
import com.example.gamethreeinrow.Enum.PebbleType
import com.example.gamethreeinrow.Utility.BitmapFactory
import com.example.gamethreeinrow.Utility.Constants
import org.jetbrains.annotations.NotNull
import java.util.*

/**
 * Base class of pebble.
 *
 * @param type - type pebble.
 * @param colorType - color type of pebble.
 * @param worth - how mach cost current pebble.
 * @param coordinate - where placed current pebble.
 * @param size - size of pebble.
 */
abstract class BasePebble(val type: PebbleType,
                          var colorType: ColorType,
                          val worth: Int,
                          var coordinate: Coordinate,
                          val size: Float) {

    val id: UUID = UUID.randomUUID()

    /**
     * Has changeable X.
     */
    var changeX: Float? = null

    /**
     * Has changeable Y.
     */
    var changeY: Float? = null

    /**
     * Draw pebble.
     *
     * @param canvas - place where need to draw pebble.
     */
    fun draw(@NotNull canvas: Canvas) {
        canvas.let {
            val myPaint = Paint()
            val topX: Float
            val topY: Float

            /* Set coordinates. */
            if (changeX == null && changeY == null) {
                topX = coordinate.x + Constants.MARGING_PEBBLE
                topY = coordinate.y + Constants.MARGING_PEBBLE
            } else {
                topX = changeX!!
                topY = changeY!!
            }

            /* Set icon. */
            it.drawBitmap(getBitmap()!!, topX, topY, myPaint)
        }
    }

    /**
     * Get bitmap based on color type of object.
     */
    private fun getBitmap(): Bitmap? {
        return when (colorType) {
            ColorType.Brown -> BitmapFactory.brownPebbleIcon
            ColorType.Blue -> BitmapFactory.bluePebbleIcon
            ColorType.Origin -> BitmapFactory.originPebbleIcon
            ColorType.Gray -> BitmapFactory.grayPebbleIcon
            ColorType.Green -> BitmapFactory.greenPebbleIcon
        }
    }

    fun equals(pebble: BasePebble?): Boolean {
        return pebble != null && id == pebble.id
    }
}