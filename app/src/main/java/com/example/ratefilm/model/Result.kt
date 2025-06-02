package com.example.ratefilm.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "movies"
)

data class Result(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val adult: Boolean,
    val runtime: Int,
    val backdrop_path: String,
    val genres: List<Genre>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
): Serializable