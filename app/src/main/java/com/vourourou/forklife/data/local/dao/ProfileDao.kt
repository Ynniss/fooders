package com.vourourou.forklife.data.local.dao

import androidx.room.*
import com.vourourou.forklife.data.local.entity.IngredientEntity
import com.vourourou.forklife.data.local.entity.ProfileEntity
import com.vourourou.forklife.data.local.entity.ProfileWithIngredients
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Transaction
    @Query("SELECT * FROM profiles")
    fun getAllProfiles(): Flow<List<ProfileWithIngredients>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Query("DELETE FROM profiles WHERE profileId = :id")
    suspend fun deleteProfile(id: Long)
}