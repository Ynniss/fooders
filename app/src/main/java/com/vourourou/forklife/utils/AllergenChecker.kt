package com.vourourou.forklife.utils

import com.vourourou.forklife.data.model.Allergen
import com.vourourou.forklife.data.remote.model.Product

/**
 * Utility class for checking allergens in products against user preferences.
 */
object AllergenChecker {

    /**
     * Result of allergen detection containing matched allergens
     */
    data class AllergenCheckResult(
        val hasAllergens: Boolean,
        val detectedAllergens: List<Allergen>
    ) {
        companion object {
            val NONE = AllergenCheckResult(hasAllergens = false, detectedAllergens = emptyList())
        }
    }

    /**
     * Checks a product for allergens that match the user's selected allergens.
     *
     * @param product The product to check
     * @param selectedAllergenTags The user's selected allergen tags (from DataStore)
     * @return AllergenCheckResult containing detected allergens
     */
    fun checkProduct(product: Product, selectedAllergenTags: Set<String>): AllergenCheckResult {
        if (selectedAllergenTags.isEmpty()) {
            return AllergenCheckResult.NONE
        }

        val productAllergenTags = product.allergens_tags ?: emptyList()

        if (productAllergenTags.isEmpty()) {
            return AllergenCheckResult.NONE
        }

        // Find allergens that match both product allergens and user preferences
        val detectedAllergens = productAllergenTags
            .filter { productTag ->
                selectedAllergenTags.any { selectedTag ->
                    productTag.equals(selectedTag, ignoreCase = true)
                }
            }
            .mapNotNull { tag -> Allergen.fromTag(tag) }
            .distinct()

        return AllergenCheckResult(
            hasAllergens = detectedAllergens.isNotEmpty(),
            detectedAllergens = detectedAllergens
        )
    }

}
