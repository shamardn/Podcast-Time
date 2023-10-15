package com.shamardn.podcasttime.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ItemHistoryBinding
import com.shamardn.podcasttime.ui.history.uistate.PodcastUiState

class HistoryAdapter(
    private val items: List<PodcastUiState>,
    private val listener: HistoryInteractionListener,
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentPodcast = items[position]
        holder.binding.apply {
            textItemHistory.text = currentPodcast.trackName.trim()
            Glide.with(imgItemHistory).load(currentPodcast.artworkUrl100).into(imgItemHistory)

            root.setOnClickListener {
                listener.onClickPodcast(currentPodcast.trackId)
            }
            root.setOnLongClickListener {
                listener.onDeletePodcastFromHistoryClick(currentPodcast)
                true
            }
        }
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemHistoryBinding.bind(itemView)
    }
}