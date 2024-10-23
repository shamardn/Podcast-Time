package com.shamardn.podcasttime.data.datasource.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shamardn.podcasttime.data.datasource.local.database.entity.EpisodeEntity

class RoomTypeConverters {
    @TypeConverter
    fun fromEpisodeEntityList(episodes: MutableList<EpisodeEntity>): String {
        return Gson().toJson(episodes)
    }

    @TypeConverter
    fun toEpisodeEntityList(episodesString: String): MutableList<EpisodeEntity> {
        val listType = object : TypeToken<MutableList<EpisodeEntity>>() {}.type
        return Gson().fromJson(episodesString, listType)
    }
}