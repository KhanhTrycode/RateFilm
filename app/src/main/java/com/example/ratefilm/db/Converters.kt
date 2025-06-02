package com.example.ratefilm.db

import androidx.room.TypeConverter
import com.example.ratefilm.model.Cast
import com.example.ratefilm.model.Crew
import com.example.ratefilm.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromGenreList(genreList: List<Genre>?): String? {
        if (genreList == null) return null
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun toGenreList(genreString: String?): List<Genre>? {
        if (genreString == null) return null
        val type = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson(genreString, type)
    }

    @TypeConverter
    fun fromCrewList(crewList: List<Crew>?): String? {
        if (crewList == null) return null
        return Gson().toJson(crewList)
    }

    @TypeConverter
    fun toCrewList(crewList: String?): List<Crew>? {
        if (crewList == null) return null
        val type = object : TypeToken<List<Crew>>() {}.type
        return Gson().fromJson(crewList, type)
    }

    @TypeConverter
    fun fromCastList(castList: List<Cast>?): String? {
        if (castList == null) return null
        return Gson().toJson(castList)
    }

    @TypeConverter
    fun toCastList(castList: String?): List<Cast>? {
        if (castList == null) return null
        val type = object : TypeToken<List<Cast>>() {}.type
        return Gson().fromJson(castList, type)
    }
}
