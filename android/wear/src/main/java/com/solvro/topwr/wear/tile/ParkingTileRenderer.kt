package com.solvro.topwr.wear.tile

import android.content.Context
import android.content.Intent
import androidx.wear.protolayout.ActionBuilders
import androidx.wear.protolayout.ColorBuilders.argb
import androidx.wear.protolayout.DeviceParametersBuilders.DeviceParameters
import androidx.wear.protolayout.DimensionBuilders.dp
import androidx.wear.protolayout.DimensionBuilders.expand
import androidx.wear.protolayout.DimensionBuilders.wrap
import androidx.wear.protolayout.LayoutElementBuilders
import androidx.wear.protolayout.LayoutElementBuilders.Box
import androidx.wear.protolayout.LayoutElementBuilders.Column
import androidx.wear.protolayout.LayoutElementBuilders.FontStyle
import androidx.wear.protolayout.LayoutElementBuilders.Row
import androidx.wear.protolayout.LayoutElementBuilders.Spacer
import androidx.wear.protolayout.LayoutElementBuilders.Text
import androidx.wear.protolayout.ModifiersBuilders
import androidx.wear.protolayout.ModifiersBuilders.Background
import androidx.wear.protolayout.ModifiersBuilders.Corner
import androidx.wear.protolayout.ModifiersBuilders.Modifiers
import androidx.wear.protolayout.ResourceBuilders
import androidx.wear.protolayout.material3.MaterialScope
import androidx.wear.protolayout.material3.dynamicColorScheme
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.tooling.preview.Preview
import androidx.wear.tiles.tooling.preview.TilePreviewData
import androidx.wear.tiles.tooling.preview.TilePreviewHelper
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer
import com.solvro.topwr.wear.MainActivity
import com.solvro.topwr.wear.R
import com.solvro.topwr.wear.model.Parking

/**
 * Material 3 Expressive Tile Renderer
 *
 * Renders parking data in a beautiful Material 3 expressive design with:
 * - Dynamic color scheme
 * - Bold typography
 * - Rich visual hierarchy
 * - Smooth corners and modern shapes
 */
@OptIn(ExperimentalHorologistApi::class)
class ParkingTileRenderer(
    context: Context
) : SingleTileLayoutRenderer<ParkingTileState, Unit>(context) {

    override fun createTheme(): MaterialScope {
        return MaterialScope(
            context = context,
            colorScheme = dynamicColorScheme(context)
        )
    }

    override fun renderTile(
        state: ParkingTileState,
        deviceParameters: DeviceParameters
    ): LayoutElementBuilders.LayoutElement {
        return when (state) {
            is ParkingTileState.Success -> renderSuccessLayout(state.parkings)
            is ParkingTileState.Error -> renderErrorLayout(state.message)
            is ParkingTileState.Empty -> renderEmptyLayout()
            is ParkingTileState.Loading -> renderLoadingLayout()
        }
    }

    /**
     * Success layout with Material 3 expressive styling
     */
    private fun renderSuccessLayout(parkings: List<Parking>): LayoutElementBuilders.LayoutElement {
        // Take top 3 parkings with most available spaces
        val topParkings = parkings
            .sortedByDescending { it.availableSpaces }
            .take(3)

        return Box.Builder()
            .setWidth(expand())
            .setHeight(expand())
            .setModifiers(
                Modifiers.Builder()
                    .setClickable(createOpenAppClickable())
                    .setBackground(
                        Background.Builder()
                            .setColor(argb(0xFF1A1C1E.toInt())) // Material 3 dark surface
                            .setCorner(Corner.Builder().setRadius(dp(28f)).build())
                            .build()
                    )
                    .setPadding(
                        ModifiersBuilders.Padding.Builder()
                            .setAll(dp(16f))
                            .build()
                    )
                    .build()
            )
            .addContent(
                Column.Builder()
                    .setWidth(expand())
                    .setHeight(wrap())
                    .addContent(
                        // Header with expressive styling
                        Text.Builder()
                            .setText(context.getString(R.string.parkings_title))
                            .setFontStyle(
                                FontStyle.Builder()
                                    .setSize(dp(18f))
                                    .setWeight(700) // Bold
                                    .setColor(argb(0xFFE6E1E5.toInt())) // Material 3 on-surface
                                    .build()
                            )
                            .build()
                    )
                    .addContent(Spacer.Builder().setHeight(dp(12f)).build())
                    .apply {
                        // Add parking cards
                        topParkings.forEach { parking ->
                            addContent(createParkingCard(parking))
                            addContent(Spacer.Builder().setHeight(dp(8f)).build())
                        }
                    }
                    .build()
            )
            .build()
    }

    /**
     * Create a Material 3 expressive parking card
     */
    private fun createParkingCard(parking: Parking): LayoutElementBuilders.LayoutElement {
        val availabilityPercentage = if (parking.totalSpaces > 0) {
            (parking.availableSpaces.toFloat() / parking.totalSpaces.toFloat() * 100).toInt()
        } else 0

        // Dynamic color based on availability (expressive approach)
        val cardColor = when {
            availabilityPercentage > 50 -> 0xFF4CAF50.toInt() // Green - plenty available
            availabilityPercentage > 20 -> 0xFFFFA726.toInt() // Orange - some available
            availabilityPercentage > 0 -> 0xFFEF5350.toInt()  // Red - limited
            else -> 0xFF78909C.toInt() // Grey - none available
        }

        return Box.Builder()
            .setWidth(expand())
            .setHeight(wrap())
            .setModifiers(
                Modifiers.Builder()
                    .setBackground(
                        Background.Builder()
                            .setColor(argb(cardColor))
                            .setCorner(Corner.Builder().setRadius(dp(20f)).build())
                            .build()
                    )
                    .setPadding(
                        ModifiersBuilders.Padding.Builder()
                            .setAll(dp(12f))
                            .build()
                    )
                    .build()
            )
            .addContent(
                Row.Builder()
                    .setWidth(expand())
                    .setHeight(wrap())
                    .addContent(
                        // Left side: Parking name
                        Column.Builder()
                            .setWidth(wrap())
                            .setHeight(wrap())
                            .addContent(
                                Text.Builder()
                                    .setText(parking.symbol)
                                    .setFontStyle(
                                        FontStyle.Builder()
                                            .setSize(dp(16f))
                                            .setWeight(600) // Semi-bold
                                            .setColor(argb(0xFFFFFFFF.toInt()))
                                            .build()
                                    )
                                    .build()
                            )
                            .addContent(
                                Text.Builder()
                                    .setText(parking.name.take(15))
                                    .setFontStyle(
                                        FontStyle.Builder()
                                            .setSize(dp(12f))
                                            .setColor(argb(0xE0FFFFFF.toInt()))
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .addContent(Spacer.Builder().setWidth(dp(8f)).build())
                    .addContent(
                        // Right side: Large expressive number
                        Column.Builder()
                            .setWidth(wrap())
                            .setHeight(wrap())
                            .addContent(
                                Text.Builder()
                                    .setText(parking.availableSpaces.toString())
                                    .setFontStyle(
                                        FontStyle.Builder()
                                            .setSize(dp(28f)) // Large expressive size
                                            .setWeight(900) // Extra bold
                                            .setColor(argb(0xFFFFFFFF.toInt()))
                                            .build()
                                    )
                                    .build()
                            )
                            .addContent(
                                Text.Builder()
                                    .setText("available")
                                    .setFontStyle(
                                        FontStyle.Builder()
                                            .setSize(dp(10f))
                                            .setColor(argb(0xE0FFFFFF.toInt()))
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }

    /**
     * Error layout with Material 3 styling
     */
    private fun renderErrorLayout(message: String): LayoutElementBuilders.LayoutElement {
        return Box.Builder()
            .setWidth(expand())
            .setHeight(expand())
            .setModifiers(
                Modifiers.Builder()
                    .setClickable(createOpenAppClickable())
                    .setPadding(
                        ModifiersBuilders.Padding.Builder()
                            .setAll(dp(16f))
                            .build()
                    )
                    .build()
            )
            .addContent(
                Column.Builder()
                    .setWidth(expand())
                    .setHeight(wrap())
                    .addContent(
                        Text.Builder()
                            .setText("⚠️")
                            .setFontStyle(
                                FontStyle.Builder()
                                    .setSize(dp(32f))
                                    .build()
                            )
                            .build()
                    )
                    .addContent(Spacer.Builder().setHeight(dp(8f)).build())
                    .addContent(
                        Text.Builder()
                            .setText(context.getString(R.string.error))
                            .setFontStyle(
                                FontStyle.Builder()
                                    .setSize(dp(14f))
                                    .setWeight(600)
                                    .setColor(argb(0xFFEF5350.toInt()))
                                    .build()
                            )
                            .build()
                    )
                    .addContent(
                        Text.Builder()
                            .setText("Tap to retry")
                            .setFontStyle(
                                FontStyle.Builder()
                                    .setSize(dp(12f))
                                    .setColor(argb(0xB3FFFFFF.toInt()))
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }

    /**
     * Empty layout
     */
    private fun renderEmptyLayout(): LayoutElementBuilders.LayoutElement {
        return Box.Builder()
            .setWidth(expand())
            .setHeight(expand())
            .setModifiers(
                Modifiers.Builder()
                    .setPadding(
                        ModifiersBuilders.Padding.Builder()
                            .setAll(dp(16f))
                            .build()
                    )
                    .build()
            )
            .addContent(
                Column.Builder()
                    .setWidth(expand())
                    .setHeight(wrap())
                    .addContent(
                        Text.Builder()
                            .setText("🅿️")
                            .setFontStyle(
                                FontStyle.Builder()
                                    .setSize(dp(32f))
                                    .build()
                            )
                            .build()
                    )
                    .addContent(Spacer.Builder().setHeight(dp(8f)).build())
                    .addContent(
                        Text.Builder()
                            .setText("No parkings")
                            .setFontStyle(
                                FontStyle.Builder()
                                    .setSize(dp(14f))
                                    .setColor(argb(0xB3FFFFFF.toInt()))
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }

    /**
     * Loading layout
     */
    private fun renderLoadingLayout(): LayoutElementBuilders.LayoutElement {
        return Box.Builder()
            .setWidth(expand())
            .setHeight(expand())
            .addContent(
                Text.Builder()
                    .setText(context.getString(R.string.loading))
                    .setFontStyle(
                        FontStyle.Builder()
                            .setSize(dp(14f))
                            .setColor(argb(0xB3FFFFFF.toInt()))
                            .build()
                    )
                    .build()
            )
            .build()
    }

    /**
     * Create clickable action to open the main app
     */
    private fun createOpenAppClickable(): ModifiersBuilders.Clickable {
        return ModifiersBuilders.Clickable.Builder()
            .setOnClick(
                ActionBuilders.LaunchAction.Builder()
                    .setAndroidActivity(
                        ActionBuilders.AndroidActivity.Builder()
                            .setClassName(MainActivity::class.java.name)
                            .setPackageName(context.packageName)
                            .build()
                    )
                    .build()
            )
            .setId("open_app")
            .build()
    }

    override fun ResourceBuilders.Resources.Builder.produceRequestedResources(
        resourceState: Unit,
        deviceParameters: DeviceParameters,
        resourceIds: MutableList<String>
    ) {
        // No additional resources needed for this tile
    }
}

/**
 * Tile preview for development
 */
@Preview
fun parkingTilePreview(): TilePreviewData {
    return TilePreviewHelper.singleTimelineEntryTileBuilder(
        ParkingTileRenderer(context = TilePreviewHelper.fetchApplicationContext())
    ).build()
}
