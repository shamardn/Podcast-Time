package com.shamardn.podcasttime.ui.download

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.local.database.entity.EpisodeEntity
import com.shamardn.podcasttime.databinding.ItemDownloadBinding
import com.shamardn.podcasttime.util.changeDateFormat
import com.shamardn.podcasttime.util.milliSecondsToMinutes

class DownloadsAdapter(
    private val items: List<EpisodeEntity>,
    private val listener: DownloadsInteractionListener,
) : RecyclerView.Adapter<DownloadsAdapter.DownloadsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsViewHolder {
        return DownloadsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_download, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: DownloadsViewHolder, position: Int) {
        val currentEpisode = items[position]
        holder.binding.apply {
            Glide.with(root.context).load(currentEpisode.artworkUrl160).into(imgItemDownload)
            textItemDownloadEpisodeDate.text = currentEpisode.releaseDate.changeDateFormat()
            textItemDownloadEpisodeTitle.text = currentEpisode.trackName
            textItemDownloadPlayTime.text = currentEpisode.trackTimeMillis.milliSecondsToMinutes()
            imgItemDownloadDownloaded.setOnClickListener {

            }

            root.setOnClickListener {
                listener.onEpisodeClick(
                    episodeUrl = currentEpisode.episodeUrl,
                    artworkUrl = currentEpisode.artworkUrl160,
                    podcastTitle = currentEpisode.collectionName,
                    episode = currentEpisode.trackName,
                )
            }
        }
    }

    class DownloadsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemDownloadBinding.bind(itemView)
    }
}
