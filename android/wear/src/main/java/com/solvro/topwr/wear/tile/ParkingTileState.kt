package com.solvro.topwr.wear.tile

import com.solvro.topwr.wear.model.Parking

/**
 * State for the Parking Tile
 */
sealed class ParkingTileState {
    data class Success(val parkings: List<Parking>) : ParkingTileState()
    data class Error(val message: String) : ParkingTileState()
    data object Empty : ParkingTileState()
    data object Loading : ParkingTileState()
}
