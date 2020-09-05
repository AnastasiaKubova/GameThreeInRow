package com.example.gamethreeinrow.Utility

import com.example.gamethreeinrow.Enum.ColorType
import com.example.gamethreeinrow.Enum.NeighborSide
import com.example.gamethreeinrow.GameObject.BasePebble
import com.example.gamethreeinrow.GameObject.Coordinate
import com.example.gamethreeinrow.GameObject.OrdinaryPebble
import kotlin.random.Random

object GenerateObjects {

    val randomizer = Random(System.currentTimeMillis())
    val colorsList: MutableList<ColorType> = mutableListOf(ColorType.Gray, ColorType.Origin, ColorType.Brown, ColorType.Blue, ColorType.Green)

    /**
     * Generate pebbles matrix
     *
     * @param n - horizonral size matrix.
     * @param m - vertical size matrix.
     * @param size - size of pebble.
     */
    fun generatePebblesLevel(n: Int, m: Int, size: Float): MutableList<BasePebble> {
        val pebbles: MutableList<BasePebble> = mutableListOf()
        for (i in 0..n) {
            for (j in 0..m) {

                /* Generate new pebble object. */
                val objCoords = Coordinate(i.toFloat() * size , j.toFloat() * size)
                val color: ColorType = getFreeColorTypeBasedOnNeighbors(objCoords, size.toInt(), pebbles)
                pebbles.add(OrdinaryPebble(color, objCoords, size))
            }
        }
        return pebbles
    }

    /**
     * Generate new pebble.
     *
     * @param size - size of pebble.
     * @param coords - where pebble is placed.
     */
     fun generateSinglePebbleForReplacing(oldPebble: BasePebble, pebbles: MutableList<BasePebble>) : BasePebble {
        val color = getFreeColorTypeBasedOnNeighbors(oldPebble.coordinate, oldPebble.size.toInt(), pebbles)
        return OrdinaryPebble(color, oldPebble.coordinate, oldPebble.size)
    }

    /**
     * Get free color based on neighbors colors.
     *
     * @param coords - coordinate for pebble;
     * @param size - size of pebble.
     * @param pebbles - pebbles list where need search neighbors.
     */
    private fun getFreeColorTypeBasedOnNeighbors(coords: Coordinate, size: Int, pebbles: MutableList<BasePebble>) : ColorType {
        val tempColorList: MutableList<ColorType> = colorsList.toMutableList()

        /* Check neighbors colors and remove if his colors repeat. */
        val topNeighbors: BasePebble? = checkNeighbors(coords, size, pebbles, NeighborSide.Top)
        val leftNeighbors: BasePebble? = checkNeighbors(coords, size, pebbles, NeighborSide.Left)
        val rightNeighbors: BasePebble? = checkNeighbors(coords, size, pebbles, NeighborSide.Right)
        val bottomNeighbors: BasePebble? = checkNeighbors(coords, size, pebbles, NeighborSide.Bottom)
        val removeAll = tempColorList.removeAll(
            mutableListOf(
                topNeighbors?.colorType,
                leftNeighbors?.colorType,
                rightNeighbors?.colorType,
                bottomNeighbors?.colorType
            )
        )

        /* Generate new pebble object. */
        return tempColorList[randomizer.nextInt(tempColorList.size)]
    }

    /**
     * Check if pebble by this coordinate has a neighbor.
     *
     * @param objCoords - coordinate for current object.
     * @param side - size of object.
     * @param pebbles - list with pebbles where need to found neighbor.
     * @param size - in the which side need to found the neighbor.
     */
    private fun checkNeighbors(objCoords: Coordinate, size: Int, pebbles: MutableList<BasePebble>, side: NeighborSide): BasePebble? {
        val neighborCoords: Coordinate = when(side) {
            NeighborSide.Bottom -> {
                Coordinate(objCoords.x, objCoords.y - size)
            }
            NeighborSide.Left -> {
                Coordinate(objCoords.x - size, objCoords.y)
            }
            NeighborSide.Right -> {
                Coordinate(objCoords.x + size, objCoords.y)
            }
            NeighborSide.Top -> {
                Coordinate(objCoords.x, objCoords.y + size)
            }
        }
        pebbles.forEach {
            if (it.coordinate.x == neighborCoords.x
                && it.coordinate.y == neighborCoords.y)
                return it
        }
        return null
    }

    /**
     * Generate colors for pebbles.
     */
    private fun generateColor() : ColorType {
        val rnd = Random(System.currentTimeMillis())
        val num = rnd.nextInt(1, 10)
        return when {
            num in 0..2 -> {
                ColorType.Blue
            }
            num in 3..4 -> {
                ColorType.Brown
            }
            num in 5..6 -> {
                ColorType.Gray
            }
            num in 7..8 -> {
                ColorType.Green
            }
            else -> {
                ColorType.Origin
            }
        }
    }
}