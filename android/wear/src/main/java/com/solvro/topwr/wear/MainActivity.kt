package com.solvro.topwr.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.*
import com.solvro.topwr.wear.api.ParkingApiService
import com.solvro.topwr.wear.model.Parking
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    MaterialTheme {
        ParkingScreen()
    }
}

@Composable
fun ParkingScreen() {
    val parkingApiService = remember { ParkingApiService() }
    var parkings by remember { mutableStateOf<List<Parking>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun loadParkings() {
        scope.launch {
            isLoading = true
            error = null
            parkingApiService.fetchParkings()
                .onSuccess { response ->
                    parkings = response.places
                    isLoading = false
                }
                .onFailure { e ->
                    error = e.message ?: "Unknown error"
                    isLoading = false
                }
        }
    }

    LaunchedEffect(Unit) {
        loadParkings()
    }

    Scaffold(
        timeText = { TimeText() }
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.error),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { loadParkings() }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }
            parkings.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No parkings available",
                        textAlign = TextAlign.Center
                    )
                }
            }
            else -> {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        ListHeader {
                            Text(
                                text = stringResource(R.string.parkings_title),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    items(parkings) { parking ->
                        ParkingCard(parking = parking)
                    }
                }
            }
        }
    }
}

@Composable
fun ParkingCard(parking: Parking) {
    Card(
        onClick = { /* Can be expanded later for navigation */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = parking.displayName,
                style = MaterialTheme.typography.title3
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.available_spaces,
                    parking.availableSpaces.toString()
                ),
                style = MaterialTheme.typography.body2
            )
            Text(
                text = stringResource(
                    R.string.total_spaces,
                    parking.totalSpaces.toString()
                ),
                style = MaterialTheme.typography.caption2
            )
        }
    }
}
