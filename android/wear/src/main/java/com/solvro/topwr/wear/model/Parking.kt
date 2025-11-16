package com.solvro.topwr.wear.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Parking(
    @SerialName("id") val id: String,
    @SerialName("parking_id") val parkingId: String,
    @SerialName("liczba_miejsc") val numberOfPlaces: String,
    @SerialName("symbol") val symbol: String,
    @SerialName("nazwa") val name: String,
    @SerialName("open_hour") val openHour: String?,
    @SerialName("close_hour") val closeHour: String?,
    @SerialName("places") val places: String,
    @SerialName("geo_lan") val geoLan: String,
    @SerialName("geo_lat") val geoLat: String,
    @SerialName("address") val address: String,
    @SerialName("trend") val trend: String,
    @SerialName("aktywny") val active: String
) {
    val availableSpaces: Int
        get() = numberOfPlaces.toIntOrNull()?.takeIf { it >= 0 } ?: 0

    val totalSpaces: Int
        get() = places.toIntOrNull() ?: 0

    val displayName: String
        get() = if (name.startsWith("Parking")) name else "Parking $name"
}

@Serializable
data class ParkingResponse(
    @SerialName("places") val places: List<Parking>
)
