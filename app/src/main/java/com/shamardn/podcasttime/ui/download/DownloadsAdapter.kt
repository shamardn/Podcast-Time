package com.shamardn.podcasttime.ui.download

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ItemDownloadBinding
import com.shamardn.podcasttime.ui.BaseDiffUtil
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.util.changeDateFormat
import com.shamardn.podcasttime.util.milliSecondsToMinutes

class DownloadsAdapter(
    private var items: List<EpisodeUiState>,
    private val listener: DownloadsInteractionListener,
) : RecyclerView.Adapter<DownloadsAdapter.DownloadsViewHolder>() {

    private var currentEpisode : EpisodeUiState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsViewHolder {
        return DownloadsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_download, parent, false)
        )
    }

    fun getData(): List<EpisodeUiState> {
        return items
    }

    fun setPlayedEpisode(episode: EpisodeUiState){
        currentEpisode = episode
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    fun setData(newList: List<EpisodeUiState>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(items, newList, ::areItemsSame, ::areContentSame))
        items = newList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    private fun areItemsSame(oldItem: EpisodeUiState, newItem: EpisodeUiState) =
        oldItem.id == newItem.id

    private fun areContentSame(oldItem: EpisodeUiState, newItem: EpisodeUiState) =
        oldItem.collectionName == newItem.collectionName


    override fun onBindViewHolder(holder: DownloadsViewHolder, position: Int) {
        val currentEpisode = items[position]
        holder.binding.apply {
            Glide.with(root.context).load(currentEpisode.artworkUrl100).into(imgItemDownload)
            textItemDownloadEpisodeDate.text = currentEpisode.releaseDate.changeDateFormat()
            textItemDownloadEpisodeTitle.text = currentEpisode.trackName
            textItemDownloadPlayTime.text = currentEpisode.trackTimeMillis.milliSecondsToMinutes()

            imgItemDownloadRemove.setOnClickListener {
                listener.onDeleteEpisodeClick(currentEpisode)
            }

            root.setOnClickListener {
                listener.onEpisodeClick(currentEpisode, position)
            }
        }
    }

    class DownloadsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemDownloadBinding.bind(itemView)
    }
}
