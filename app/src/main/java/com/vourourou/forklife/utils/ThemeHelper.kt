package com.vourourou.forklife.utils

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.vourourou.forklife.R

object ThemeHelper {

    data class ThemeColors(
        val primary: Int,
        val primaryLight: Int,
        val secondary: Int,
        val secondaryLight: Int
    )

    fun getThemeColors(context: Context, theme: String): ThemeColors {
        return when (theme) {
            "Avocado" -> ThemeColors(
                primary = ContextCompat.getColor(context, R.color.md_theme_light_primary_green),
                primaryLight = ContextCompat.getColor(
                    context,
                    R.color.md_theme_light_primaryContainer_green
                ),
                secondary = ContextCompat.getColor(context, R.color.md_theme_light_secondary_green),
                secondaryLight = ContextCompat.getColor(
                    context,
                    R.color.md_theme_light_secondaryContainer_green
                )
            )

            "Cherry" -> ThemeColors(
                primary = ContextCompat.getColor(context, R.color.md_theme_light_primary_red),
                primaryLight = ContextCompat.getColor(
                    context,
                    R.color.md_theme_light_primaryContainer_red
                ),
                secondary = ContextCompat.getColor(context, R.color.md_theme_light_secondary_red),
                secondaryLight = ContextCompat.getColor(
                    context,
                    R.color.md_theme_light_secondaryContainer_red
                )
            )

            else -> ThemeColors(
                primary = ContextCompat.getColor(context, R.color.md_theme_light_primary),
                primaryLight = ContextCompat.getColor(
                    context,
                    R.color.md_theme_light_primaryContainer
                ),
                secondary = ContextCompat.getColor(context, R.color.md_theme_light_secondary),
                secondaryLight = ContextCompat.getColor(
                    context,
                    R.color.md_theme_light_secondaryContainer
                )
            )
        }
    }

    fun createGradientDrawable(
        startColor: Int,
        endColor: Int,
        cornerRadius: Float = 0f,
        orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TL_BR
    ): GradientDrawable {
        return GradientDrawable(
            orientation,
            intArrayOf(startColor, endColor)
        ).apply {
            if (cornerRadius > 0f) {
                setCornerRadius(cornerRadius)
            }
        }
    }

    fun createCardGradient(context: Context, theme: String, cardIndex: Int): GradientDrawable {
        val colors = getThemeColors(context, theme)

        val (startColor, endColor) = when (cardIndex % 2) {
            0 -> Pair(colors.primary, colors.secondary)
            else -> Pair(colors.secondary, colors.primary)
        }

        return createGradientDrawable(
            startColor = startColor,
            endColor = endColor,
            cornerRadius = 60f,
            orientation = GradientDrawable.Orientation.TL_BR
        )
    }
}
