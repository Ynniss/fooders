package com.vourourou.forklife.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileWithIngredients(
    @Embedded val profile: ProfileEntity,
    @Relation(
        parentColumn = "profileId",
        entityColumn = "ownerProfileId"
    )
    val ingredients: List<IngredientEntity>
)