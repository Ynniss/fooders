package com.vourourou.forklife.data.repository

import com.vourourou.forklife.data.local.dao.ProfileDao
import com.vourourou.forklife.data.local.entity.IngredientEntity
import com.vourourou.forklife.data.local.entity.ProfileEntity
import com.vourourou.forklife.data.local.entity.ProfileWithIngredients
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val profileDao: ProfileDao
) {
    val allProfiles: Flow<List<ProfileWithIngredients>> = profileDao.getAllProfiles()

    suspend fun savePdfProfile(name: String, ingredients: List<Pair<String, Int>>) {
        // 1. Save the main Profile
        val profileId = profileDao.insertProfile(
            ProfileEntity(name = name)
        )

        // 2. Map the list of pairs to Ingredient entities
        val ingredientEntities = ingredients.map { (ingName, ingLevel) ->
            IngredientEntity(
                ownerProfileId = profileId,
                name = ingName,
                level = ingLevel
            )
        }

        // 3. Save all ingredients
        profileDao.insertIngredients(ingredientEntities)
    }

    suspend fun deleteProfile(profileId: Long) {
        profileDao.deleteProfile(profileId)
    }
}