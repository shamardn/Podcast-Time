package com.shamardn.podcasttime.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.CarouselItemContainerBinding
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

class SearchAdapter(
    private val items: List<PodcastUiState>,
    private val listener: SearchInteractionListener,
) : RecyclerView.Adapter<SearchAdapter.PodcastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        return PodcastViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.carousel_item_container, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        val currentPodcast = items[position]
        holder.binding.apply {
            Glide.with(root.context).load(currentPodcast.artworkUrl100).into(carouselImageView)

            textItemCarouselTitle.text = currentPodcast.trackName
            root.setOnClickListener {
                listener.onClickPodcast(currentPodcast.trackId)
            }
        }
    }

    class PodcastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = CarouselItemContainerBinding.bind(itemView)
    }
}
