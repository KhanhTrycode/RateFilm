package com.example.ratefilm.db

import com.example.ratefilm.model.Result
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun upsert(result: Result): Long

    @Query("SELECT * FROM movies")
    fun getAllMovie(): LiveData<List<Result>>

    @Delete
    suspend fun deleteMovie(result: Result)
}
