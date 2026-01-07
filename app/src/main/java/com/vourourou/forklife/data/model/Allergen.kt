package com.vourourou.forklife.data.model

import androidx.annotation.StringRes
import com.vourourou.forklife.R

/**
 * Represents common food allergens as defined by food safety regulations.
 * Each allergen has a unique tag used by OpenFoodFacts and a display name resource.
 */
enum class Allergen(
    val tag: String,
    @StringRes val displayNameRes: Int
) {
    GLUTEN("en:gluten", R.string.allergen_gluten),
    MILK("en:milk", R.string.allergen_milk),
    EGGS("en:eggs", R.string.allergen_eggs),
    NUTS("en:nuts", R.string.allergen_nuts),
    PEANUTS("en:peanuts", R.string.allergen_peanuts),
    SOY("en:soybeans", R.string.allergen_soy),
    FISH("en:fish", R.string.allergen_fish),
    SHELLFISH("en:crustaceans", R.string.allergen_shellfish),
    SESAME("en:sesame-seeds", R.string.allergen_sesame),
    MUSTARD("en:mustard", R.string.allergen_mustard),
    CELERY("en:celery", R.string.allergen_celery),
    SULFITES("en:sulphur-dioxide-and-sulphites", R.string.allergen_sulfites),
    LUPIN("en:lupin", R.string.allergen_lupin),
    MOLLUSCS("en:molluscs", R.string.allergen_molluscs);

    companion object {
        /**
         * Returns all allergen tags as a set for easy lookup
         */
        val allTags: Set<String> = entries.map { it.tag }.toSet()

        /**
         * Finds an Allergen by its OpenFoodFacts tag
         */
        fun fromTag(tag: String): Allergen? {
            return entries.find { it.tag.equals(tag, ignoreCase = true) }
        }

        /**
         * Finds allergens from a list of OpenFoodFacts tags
         */
        fun fromTags(tags: List<String>): List<Allergen> {
            return tags.mapNotNull { fromTag(it) }
        }
    }
}
