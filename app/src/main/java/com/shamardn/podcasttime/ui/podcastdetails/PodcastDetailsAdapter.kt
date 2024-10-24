package com.shamardn.podcasttime.ui.podcastdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ItemEpisodeBinding
import com.shamardn.podcasttime.ui.BaseDiffUtil
import com.shamardn.podcasttime.ui.common.uistate.EpisodeUiState
import com.shamardn.podcasttime.util.changeDateFormat
import com.shamardn.podcasttime.util.milliSecondsToMinutes

class PodcastDetailsAdapter(
    private var items: List<EpisodeUiState>,
    private val listener: PodcastDetailsInteractionListener,
) : RecyclerView.Adapter<PodcastDetailsAdapter.PodcastDetailsViewHolder>() {


    private var isPlayedEpisodeShowed : Boolean = false
    private var currentEpisode : EpisodeUiState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastDetailsViewHolder {
        return PodcastDetailsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
        )
    }

    override fun getItemCount() = items.size - 1

    fun getData(): List<EpisodeUiState> {
        return items
    }

    fun setPlayedEpisode(episode: EpisodeUiState){
        currentEpisode = episode
        notifyDataSetChanged()
    }

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

    override fun onBindViewHolder(holder: PodcastDetailsViewHolder, position: Int) {
        val currentEpisode = items[position + 1]
        val img = items[0].artworkUrl100
        val artistName = items[0].artistName
        holder.binding.apply {
            textItemEpisodePodcastName.text = currentEpisode.collectionName
            textItemEpisodeArtistName.text = artistName
            textItemEpisodeName.text = currentEpisode.trackName
            textItemEpisodeDate.text = currentEpisode.releaseDate.changeDateFormat()
            textItemEpisodeDesc.text = currentEpisode.description
            textItemEpisodePlayTime.text = currentEpisode.trackTimeMillis.milliSecondsToMinutes()
            Glide.with(root.context).load(img).into(imgItemEpisode)
            imgItemEpisodeDownload.setOnClickListener {
                listener.onClickDownload(currentEpisode)
            }
            root.setOnClickListener {
                listener.onClickEpisode(currentEpisode)

            }

        }
    }

    class PodcastDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemEpisodeBinding.bind(itemView)
    }
}
