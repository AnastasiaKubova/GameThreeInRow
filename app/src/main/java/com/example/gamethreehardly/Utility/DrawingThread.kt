package com.example.gamethreehardly.Utility

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.view.MotionEvent
import android.view.SurfaceHolder
import com.example.gamethreehardly.GameObject.BasePebble
import com.example.gamethreehardly.GameObject.Coordinate
import com.example.gamethreehardly.GameObject.OrdinaryPebble
import com.example.gamethreehardly.Utility.GenerateObjects.generatePebblesLevel
import kotlin.math.abs


class DrawingThread(private var holder: SurfaceHolder?, private val countPebble: Int, private val listener: DrawingListener) : HandlerThread("DrawingThread"), Handler.Callback {

    private var mReceiver: Handler? = null
    private var gameWidth: Int = 0
    private var gameHeight: Int = 0
    private var activePebble: BasePebble? = null
    private var pebblesList: MutableList<BasePebble> = mutableListOf()
    private var mBitmapFactory: BitmapFactory = BitmapFactory()

    override fun onLooperPrepared() {
        mReceiver = Handler(looper, this)
    }

    override fun handleMessage(msg: Message): Boolean {
        return true
    }

    /**
     * Update size for scene.
     */
    fun updateSize(width: Int, height: Int) {
        gameWidth = width
        gameHeight = height
        mBitmapFactory.initBitmaps(width / countPebble, Constants.MARGING_PEBBLE)
        mBitmapFactory.initBackground(width, height)
    }

    /**
     * Update scene with new pebbles.
     */
    fun refresh() {

        /* Generate new pebbles. */
        pebblesList = generatePebblesLevel(countPebble, countPebble, (gameWidth / countPebble).toFloat())
        clearCurrentData()
        drawScene()
    }

    /**
     * Handle move event.
     *
     * @param event - motion event.
     */
    fun handleMoveTouch(event: MotionEvent) {

        /* Check that now no any active pebbles for moving. */
        if (activePebble == null) {
            activePebble = getPebbleByRelativeCoordinate(Coordinate(event.x, event.y))
        }
        if (activePebble == null) {
            return
        }
        if (event.x > activePebble!!.size / 2
            && event.x < gameWidth - activePebble!!.size / 2
            && event.y > activePebble!!.size / 2
            && event.y < gameHeight - activePebble!!.size / 2
        ) {
            val absX = abs(event.x - (activePebble!!.coordinate.x + activePebble!!.size / 2))
            val absY = abs(event.y - (activePebble!!.coordinate.y + activePebble!!.size / 2))
            if (absX < absY) {

                if (absY < activePebble!!.size) {

                    /* Check that the object can moved by Y */
                    activePebble!!.changeY = event.y - activePebble!!.size / 2 + Constants.MARGING_PEBBLE
                    activePebble!!.changeX = activePebble!!.coordinate.x + Constants.MARGING_PEBBLE
                }
            } else {

                if (absX < activePebble!!.size) {

                    /* Check that the object can moved by X */
                    activePebble!!.changeX = event.x - activePebble!!.size / 2 + Constants.MARGING_PEBBLE
                    activePebble!!.changeY = activePebble!!.coordinate.y + Constants.MARGING_PEBBLE
                }
            }
        }
        drawScene()
    }

    /**
     * Handle up event.
     *
     * @param event - motion event.
     */
    fun handleUpTouch(event: MotionEvent) {
        if (activePebble != null) {
            if (abs(activePebble!!.coordinate.x - event.x) <= activePebble!!.size * 2
                && abs(activePebble!!.coordinate.y - event.y) <= activePebble!!.size * 2
            ) {
                val pebble = getPebbleByRelativeCoordinate(Coordinate(event.x, event.y))
                if (pebble != null) {
                    if (checkCollisions(activePebble!!, pebble)) {
                        val coords = activePebble?.coordinate
                        activePebble!!.coordinate = pebble.coordinate
                        pebble.coordinate = coords!!
                    }
                }
            }
            clearCurrentData()
        }
        drawScene()
    }

    /**
     * Handle touch event.
     *
     * @param event - motion event.
     */
    fun handleTouch(event: MotionEvent) {
    }

    /**
     * Handle touch on display on objects.
     *
     * @param pebble - pebble which was moved.
     * @param collisionPebble - pebble with which performed exchange.
     */
    private fun checkCollisions(pebble: BasePebble, collisionPebble: BasePebble) : Boolean {
        val newPebbleOne = checkCollisionAndReplacePebbles(OrdinaryPebble(pebble.colorType, collisionPebble.coordinate, pebble.size), pebble)
        val newPebbleTwo =  checkCollisionAndReplacePebbles(OrdinaryPebble(collisionPebble.colorType, pebble.coordinate, collisionPebble.size), collisionPebble)
        return newPebbleOne || newPebbleTwo
    }

    /**
     * Check collision between pebbles and replace them if collision is exist.
     *
     * @param newPebble - pebble for which search collision.
     * @param lastPebble - pebble which was moved.
     */
    private fun checkCollisionAndReplacePebbles(newPebble: BasePebble, lastPebble: BasePebble) : Boolean {
        val verticalObjects: MutableList<BasePebble> = checkCollisionByVertical(newPebble, lastPebble)
        val horizontalObjects: MutableList<BasePebble> = checkCollisionByHorizontal(newPebble, lastPebble)
        if (verticalObjects.size > 1) {
            replacePebbles(verticalObjects)
        }
        if (horizontalObjects.size > 1) {
            replacePebbles(horizontalObjects)
        }
        if (horizontalObjects.size > 1 || verticalObjects.size > 1) {
            replacePebbles(mutableListOf(lastPebble))
        }
        return verticalObjects.size > 1 || horizontalObjects.size > 1
    }

    /**
     * Check collisions by vertical orientation.
     *
     * @param newPebble - pebble for which search collision.
     * @param lastPebble - pebble which was moved.
     */
    private fun checkCollisionByVertical(newPebble: BasePebble, lastPebble: BasePebble) : MutableList<BasePebble> {
        val verticalObjects: MutableList<BasePebble> = mutableListOf()
        verticalObjects.addAll(getNeighborsPebbles(newPebble, lastPebble,0, newPebble.size.toInt()))
        verticalObjects.addAll(getNeighborsPebbles(newPebble, lastPebble,0, newPebble.size.toInt() * -1))
        return verticalObjects
    }

    /**
     * Check collisions by horizontal orientation.
     *
     * @param - pebble for which search collision.
     */
    private fun checkCollisionByHorizontal(pebble: BasePebble, lastPebble: BasePebble) : MutableList<BasePebble> {
        val verticalObjects: MutableList<BasePebble> = mutableListOf()
        verticalObjects.addAll(getNeighborsPebbles(pebble, lastPebble, pebble.size.toInt(), 0))
        verticalObjects.addAll(getNeighborsPebbles(pebble, lastPebble, pebble.size.toInt() * -1, 0))
        return verticalObjects
    }

    /**
     * Get neighbors for current pebble.
     *
     * @param pebble - pebble which for search neighbors.
     * @param incrementX - distance between neighbor along the axis X.
     * @param incrementY - distance between neighbor along the axis Y.
     */
    private fun getNeighborsPebbles(newPebble: BasePebble, lastPebble: BasePebble, incrementX: Int, incrementY: Int): MutableSet<BasePebble> {
        val verticalObjects: MutableSet<BasePebble> = mutableSetOf()
        var hasCollision = true
        var nextCoords = Coordinate(newPebble.coordinate.x, newPebble.coordinate.y)
        while (hasCollision) {
            nextCoords = Coordinate(nextCoords.x + incrementX, nextCoords.y + incrementY)
            val nextPebble = findPebbleByCoordinate(nextCoords)
            if (nextPebble != null
                && nextPebble.colorType == newPebble.colorType
                && !nextPebble.equals(lastPebble)) {
                verticalObjects.add(nextPebble)
                nextPebble.coordinate = nextCoords
            } else {
                hasCollision = false
            }
        }
        return verticalObjects
    }

    /**
     * Replace pebbles from list on new pebbles.
     *
     * @param - listObjs list with pebbles which need to replaced.
     */
    private fun replacePebbles(listObjs : MutableList<BasePebble>) {
        listener.changePebbleCountListener(listObjs)
        for (obj in listObjs) {
            for (pebble in pebblesList) {
                if (pebble.coordinate.equals(obj.coordinate)) {
                    val newPebble = GenerateObjects.generateSinglePebbleForReplacing(pebble, pebblesList)
                    pebble.colorType = newPebble.colorType
                }
            }
        }
    }

    /**
     * Draw scene.
     */
    private fun drawScene() {
        holder.let {
            var canvas: Canvas? = null
            try {
                canvas = it!!.lockCanvas()
                canvas?.drawBitmap(BitmapFactory.backGroundIcon!!, 0f, 0f, Paint())
                for (obj in pebblesList) {
                    obj.draw(canvas)
                }
            } finally {
                canvas.let {
                    holder?.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    /**
     * Clear current data.
     */
    private fun clearCurrentData() {
        activePebble?.changeX = null
        activePebble?.changeY = null
        activePebble = null
    }

    /**
     * Find pebbles by directly coordinates.
     *
     * @param - coordinate where should be pebble.
     */
    private fun findPebbleByCoordinate(coordinate: Coordinate): BasePebble? {
        pebblesList.let {
            it.forEach {
                if (it.coordinate.equals(coordinate)
                ) {
                    return it
                }
            }
        }
        return null
    }

    /**
     * Return pebble by coordinate taking into account the error in size.
     *
     * @param x - coordinate X
     * @param y - coordinate Y
     *
     * @return BasePebble object or null.
     */
    private fun getPebbleByRelativeCoordinate(coordinate: Coordinate): BasePebble? {
        pebblesList.let {
            it.forEach {
                if (it.coordinate.x <= coordinate.x
                    && (it.coordinate.x + it.size) >= coordinate.x
                    && it.coordinate.y <= coordinate.y
                    && (it.coordinate.y + it.size) >= coordinate.y
                ) {
                    return it
                }
            }
        }
        return null
    }

    interface DrawingListener {

        /**
         * Called when pebbles were changed.
         */
        fun changePebbleCountListener(listObjs: MutableList<BasePebble>)
    }
}