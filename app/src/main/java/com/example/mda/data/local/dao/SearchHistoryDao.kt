package com.example.mda.data.local.dao

// Database access object

import androidx.room.*
import com.example.mda.data.local.entities.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history WHERE userId = :userId ORDER BY timestamp DESC LIMIT 10")
    fun getRecentHistory(userId: String?): Flow<List<SearchHistoryEntity>>

    @Query("SELECT * FROM search_history WHERE userId = :userId ORDER BY timestamp DESC LIMIT 10")
    suspend fun getRecentHistoryOnce(userId: String?): List<SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: SearchHistoryEntity): Long

    @Update
    suspend fun update(entity: SearchHistoryEntity)

    @Transaction
    suspend fun upsertSafe(entity: SearchHistoryEntity) {
        val id = insert(entity)
        if (id == -1L) {
            update(entity.copy(timestamp = System.currentTimeMillis()))
        }
    }

    @Query("DELETE FROM search_history WHERE `query` COLLATE NOCASE = :query AND userId = :userId")
    suspend fun delete(query: String, userId: String?)

    @Query("DELETE FROM search_history WHERE userId = :userId")
    suspend fun deleteAll(userId: String?)
}