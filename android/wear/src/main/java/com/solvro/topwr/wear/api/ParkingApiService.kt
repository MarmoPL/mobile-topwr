package com.solvro.topwr.wear.api

import com.solvro.topwr.wear.model.ParkingResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class ParkingApiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Fetches parking data from the API.
     *
     * NOTE: Replace this URL with the actual parking API URL from your .env file
     * You can find it in lib/config/env.dart as parkingApiUrl
     */
    suspend fun fetchParkings(): Result<ParkingResponse> = withContext(Dispatchers.IO) {
        try {
            // TODO: Replace this with your actual parking API URL
            // Get it from your .env file (parkingApiUrl)
            val url = "YOUR_PARKING_API_URL_HERE"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("HTTP ${response.code}: ${response.message}")
                )
            }

            val body = response.body?.string()
                ?: return@withContext Result.failure(Exception("Empty response body"))

            val parkingResponse = json.decodeFromString<ParkingResponse>(body)
            Result.success(parkingResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
