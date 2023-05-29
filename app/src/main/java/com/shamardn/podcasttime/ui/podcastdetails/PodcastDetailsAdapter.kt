package com.shamardn.podcasttime.ui.podcastdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ItemEpisodeBinding
import com.shamardn.podcasttime.domain.entity.Episode
import com.shamardn.podcasttime.util.changeDateFormat
import com.shamardn.podcasttime.util.milliSecondsToMinutes

class PodcastDetailsAdapter(
    private val items: List<Episode>,
    private val listener: PodcastDetailsInteractionListener,
) : RecyclerView.Adapter<PodcastDetailsAdapter.PodcastDetailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastDetailsViewHolder {
        return PodcastDetailsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
        )
    }

    override fun getItemCount() = items.size - 1

    override fun onBindViewHolder(holder: PodcastDetailsViewHolder, position: Int) {
        val currentEpisode = items[position + 1]
        holder.binding.apply {
            textItemEpisodePodcastName.text = currentEpisode.collectionName
            textItemEpisodeArtistName.text = items[0].artistName
            textItemEpisodeName.text = currentEpisode.trackName
            textItemEpisodeDate.text = currentEpisode.releaseDate.changeDateFormat()
            textItemEpisodeDesc.text = currentEpisode.description
            textItemEpisodePlayTime.text = currentEpisode.trackTimeMillis.milliSecondsToMinutes()
            Glide.with(root.context).load(currentEpisode.artworkUrl60).into(imgItemEpisode)

            root.setOnClickListener {
                listener.onClickEpisode(currentEpisode.trackViewUrl)
            }
        }
    }

    class PodcastDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemEpisodeBinding.bind(itemView)
    }
}
