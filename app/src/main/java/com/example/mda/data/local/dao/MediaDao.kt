package com.example.mda.data.local.dao

// Database access object

import androidx.room.*
import com.example.mda.data.local.entities.MediaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Dao
interface MediaDao {

    @Query("SELECT * FROM media_items ORDER BY timestamp DESC")
    fun getAll(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavorites(): Flow<List<MediaEntity>>

    @Query("SELECT * FROM media_items WHERE isInWatchlist = 1 ORDER BY timestamp DESC")
    fun getWatchlist(): Flow<List<MediaEntity>>

    fun getByGenre(genreId: Int): Flow<List<MediaEntity>> =
        getAll().map { list ->
            list.filter { it.genreIds?.contains(genreId) == true }
        }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: MediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MediaEntity>)

    @Delete
    suspend fun delete(item: MediaEntity)

    @Query("DELETE FROM media_items")
    suspend fun clearAll()

    @Query("DELETE FROM media_items WHERE isFavorite = 0 AND isInWatchlist = 0")
    suspend fun clearNonSaved()

    suspend fun clearGenreById(genreId: Int) {
        val all = getAll().first()
        val toDelete = all.filter { it.genreIds?.contains(genreId) == true }
        toDelete.forEach { delete(it) }
    }

    @Query("SELECT * FROM media_items WHERE id = :id AND mediaType = :type LIMIT 1")
    suspend fun getById(id: Int, type: String): MediaEntity?

    @Query("SELECT * FROM media_items WHERE id = :id LIMIT 1")
    suspend fun getByIdOnly(id: Int): MediaEntity?

    @Query("UPDATE media_items SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM media_items WHERE id = :id LIMIT 1")
    suspend fun isFavorite(id: Int): Boolean?

    @Query("SELECT * FROM media_items")
    suspend fun getAllMediaOnce(): List<MediaEntity>
}
