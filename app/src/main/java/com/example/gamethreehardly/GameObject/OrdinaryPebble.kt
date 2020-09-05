package com.example.gamethreeinrow.GameObject

import com.example.gamethreeinrow.Enum.ColorType
import com.example.gamethreeinrow.Enum.PebbleType

class OrdinaryPebble(mColor: ColorType, coords: Coordinate, mSize: Float)
    : BasePebble(PebbleType.Orinary, mColor, 10, coords, mSize) {

}