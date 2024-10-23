package com.shamardn.podcasttime.util

import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState


sealed class PlayerEvents {
    data class  AddPlaylist(val episodes: List<EpisodeUiState> , val isUpdatePlaylistRequired:Boolean): PlayerEvents()
    data class  PlayNewEpisode(val episode: EpisodeUiState): PlayerEvents()

//    data class  SeekProgress(val value: Long): PlayerEvents()
    data class  MoveToSpecificPosition(val position:Long) : PlayerEvents()
    data class GoToSpecificItem(val index:Int): PlayerEvents()
    data class GetThePositionOfSpecificEpisodeInsideThePlaylist(val id:String) : PlayerEvents()
    data class AddEpisodeToPlayNext(val id:String):PlayerEvents()

    object  PausePlay: PlayerEvents()
    object  Previous : PlayerEvents()
    object  Next : PlayerEvents()
    object  Shuffle : PlayerEvents()
    object  Repeat : PlayerEvents()
    object  SeekForward : PlayerEvents()
    object  SeekBack : PlayerEvents()
    object ClearMediaItems : PlayerEvents()
}
