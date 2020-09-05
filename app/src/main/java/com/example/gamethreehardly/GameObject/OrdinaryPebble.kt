package com.example.gamethreehardly.GameObject

import com.example.gamethreehardly.Enum.ColorType
import com.example.gamethreehardly.Enum.PebbleType

class OrdinaryPebble(mColor: ColorType, coords: Coordinate, mSize: Float)
    : BasePebble(PebbleType.Orinary, mColor, 10, coords, mSize) {

}