package com.vourourou.forklife.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true) val profileId: Long = 0,
    val name: String, // Extracted from PDF "Name:"
    val sourcePdfName: String? = null
)