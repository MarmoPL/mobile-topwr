package com.solvro.topwr.wear.tile

import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import com.google.android.horologist.tiles.SuspendingTileService
import com.solvro.topwr.wear.api.ParkingApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Material 3 Expressive Parking Tile
 *
 * Shows real-time parking availability in a beautiful, expressive Material 3 design.
 * Tap to open the full app for more details.
 */
class ParkingTileService : SuspendingTileService() {

    private val parkingApiService = ParkingApiService()
    private lateinit var tileRenderer: ParkingTileRenderer

    override fun onCreate() {
        super.onCreate()
        tileRenderer = ParkingTileRenderer(context = this)
    }

    override suspend fun resourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ResourceBuilders.Resources {
        return tileRenderer.produceRequestedResources(
            resourceState = Unit,
            requestParams = requestParams
        )
    }

    override suspend fun tileRequest(
        requestParams: RequestBuilders.TileRequest
    ): TileBuilders.Tile = withContext(Dispatchers.IO) {

        // Fetch parking data
        val parkingsResult = parkingApiService.fetchParkings()

        val tileState = when {
            parkingsResult.isSuccess -> {
                val parkings = parkingsResult.getOrNull()?.places ?: emptyList()
                if (parkings.isEmpty()) {
                    ParkingTileState.Empty
                } else {
                    ParkingTileState.Success(parkings)
                }
            }
            else -> {
                ParkingTileState.Error(
                    parkingsResult.exceptionOrNull()?.message ?: "Unknown error"
                )
            }
        }

        tileRenderer.renderTimeline(tileState, requestParams)
    }
}
