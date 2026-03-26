package com.vourourou.forklife.data.local.entity

class PdfProcessor {
    fun extractData(fullText: String): Pair<String, List<Pair<String, Int>>> {
        // Find name after "Name:" keyword
        val nameRegex = Regex("(?i)Name:\\s*(.*)")
        val profileName = nameRegex.find(fullText)?.groupValues?.get(1)?.trim() ?: "Unknown Profile"

        val foundIngredients = mutableListOf<Pair<String, Int>>()

        // Find ingredients after "Level 1:", "Level 2:", etc.
        val levelRegex = Regex("(?i)Level\\s*(\\d):\\s*([^\\n]*)")
        levelRegex.findAll(fullText).forEach { match ->
            val level = match.groupValues[1].toInt()
            val items = match.groupValues[2].split(",").map { it.trim() }
            items.filter { it.isNotEmpty() }.forEach { item ->
                foundIngredients.add(item to level)
            }
        }

        return profileName to foundIngredients
    }
}