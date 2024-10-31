package com.shamardn.podcasttime.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ItemPodcastBinding
import com.shamardn.podcasttime.ui.BaseDiffUtil
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState
import com.shamardn.podcasttime.util.changeDateFormat

class HomeAdapter(
    private var items: List<PodcastUiState>,
    private val listener: HomeInteractionListener,
) : RecyclerView.Adapter<HomeAdapter.PodcastViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        return PodcastViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_podcast, parent, false)
        )
    }

    override fun getItemCount() = items.size

    fun setData(newList: List<PodcastUiState>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(items, newList, ::areItemsSame, ::areContentSame))
        items = newList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }

    private fun areItemsSame(oldItem: PodcastUiState, newItem: PodcastUiState) =
        oldItem.trackId == newItem.trackId

    private fun areContentSame(oldItem: PodcastUiState, newItem: PodcastUiState) =
        oldItem.collectionName == newItem.collectionName


    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        val currentPodcast = items[position]
        holder.binding.apply {
            textItemPodcastArtistName.text = currentPodcast.artistName.trim()
            textItemPodcastTrackName.text = currentPodcast.trackName.trim()
            textItemPodcastGenreName.text = currentPodcast.primaryGenreName.trim()
            textItemPodcastReleaseDate.text = currentPodcast.releaseDate.changeDateFormat()
            textItemPodcastTrackCount.text = this.root.resources.getString(R.string.trackCount, currentPodcast.trackCount.toString())
            Glide.with(root.context).load(currentPodcast.artworkUrl100).into(imgItemPodcast)

            root.setOnClickListener {
                listener.onClickPodcast(currentPodcast)
            }
        }
    }

    class PodcastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemPodcastBinding.bind(itemView)
    }
}
