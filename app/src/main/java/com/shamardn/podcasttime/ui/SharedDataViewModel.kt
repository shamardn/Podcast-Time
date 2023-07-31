package com.shamardn.podcasttime.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity

class SharedDataViewModel : ViewModel() {
    private val _episodes = MutableLiveData<List<EpisodeEntity>?>()
    val episodes: LiveData<List<EpisodeEntity>?> = _episodes

    private val _episode = MutableLiveData<EpisodeEntity?>()
    val episode: LiveData<EpisodeEntity?> = _episode

    fun setData(episodes: List<EpisodeEntity>) {
        _episodes.postValue(episodes)
    }

    fun setCurrentEpisode(episode: EpisodeEntity) {
        _episode.postValue(episode)
    }

}