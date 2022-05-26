package com.example.vin.petclinicappointment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = AntiFlashWhite
)

private val LightColorPalette = lightColors(
    primary = MetallicBlue,
    primaryVariant = Rackley,
    secondary = MiddleBlueGreen,
    background = Color.White,
    surface = Color.White,
    error = CGRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onError = Color.White,
)

@Composable
fun PetClinicAppointmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val dimensionsBuilder = DimensionsBuilder()
    val typographySizeSystemBuilder = TypographySizeSystemBuilder()
    val dimensions = when{
        screenWidth<=420 -> { dimensionsBuilder.createDimensionSystem(360)}
        screenWidth<=480 -> { dimensionsBuilder.createDimensionSystem(420)}
        screenWidth<=540 -> { dimensionsBuilder.createDimensionSystem(480)}
        screenWidth<=600 -> { dimensionsBuilder.createDimensionSystem(540)}
        screenWidth<=660 -> { dimensionsBuilder.createDimensionSystem(600)}
        screenWidth<=720 -> { dimensionsBuilder.createDimensionSystem(660)}
        screenWidth<=780 -> { dimensionsBuilder.createDimensionSystem(720)}
        screenWidth<=840 -> { dimensionsBuilder.createDimensionSystem(780)}
        screenWidth<=900 -> { dimensionsBuilder.createDimensionSystem(840)}
        else -> { dimensionsBuilder.createDimensionSystem(360) }
    }
    val typography = when {
        screenWidth<=420 -> { typographySizeSystemBuilder.createTypographySizeSystem(360) }
        screenWidth<=480 -> { typographySizeSystemBuilder.createTypographySizeSystem(420) }
        screenWidth<=540 -> { typographySizeSystemBuilder.createTypographySizeSystem(480) }
        screenWidth<=600 -> { typographySizeSystemBuilder.createTypographySizeSystem(540) }
        screenWidth<=660 -> { typographySizeSystemBuilder.createTypographySizeSystem(600) }
        screenWidth<=720 -> { typographySizeSystemBuilder.createTypographySizeSystem(660) }
        screenWidth<=780 -> { typographySizeSystemBuilder.createTypographySizeSystem(720) }
        screenWidth<=840 -> { typographySizeSystemBuilder.createTypographySizeSystem(780) }
        screenWidth<=900 -> { typographySizeSystemBuilder.createTypographySizeSystem(840) }
        else -> { typographySizeSystemBuilder.createTypographySizeSystem(360) }
    }

    ProvideDimensions(dimensions) {
        ProvideColors(colors) {
            ProvideTypography(typography) {
                MaterialTheme(
                    colors = colors,
                    typography = typography,
                    shapes = Shapes,
                    content = content
                )
            }
        }
    }
}

@Composable
fun ProvideTypography(typography: Typography, content: @Composable () -> Unit){
    val typographySet = remember { typography }
    CompositionLocalProvider(LocalTypography provides typographySet, content = content)
}

private val LocalTypography = staticCompositionLocalOf { sw360Typography }

@Composable
fun ProvideColors(colors: Colors, content: @Composable () -> Unit){
    val colorsSet = remember { colors }
    CompositionLocalProvider(LocalColors provides colorsSet, content = content)
}

private val LocalColors = staticCompositionLocalOf { LightColorPalette }

@Composable
fun ProvideDimensions(dimensions: Dimensions, content: @Composable () -> Unit){
    val dimensionSet = remember { dimensions }
    CompositionLocalProvider(LocalDimensions provides dimensionSet, content = content)
}

private val LocalDimensions = staticCompositionLocalOf { sw360Dimensions }

object PetClinicAppointmentTheme {
    val typography: Typography
        @Composable
        get() = LocalTypography.current

    val colors: Colors
        @Composable
        get() = LocalColors.current

    val dimensions: Dimensions
        @Composable
        get() = LocalDimensions.current
}