package com.example.mda.data.local.dao

// Database access object

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.mda.data.local.entities.ActorDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActorDetailsDao {

    @Query("SELECT * FROM actor_details WHERE id = :id LIMIT 1")
    fun getDetails(id: Int): Flow<ActorDetailsEntity?>

    @Upsert
    suspend fun upsert(details: ActorDetailsEntity)
}