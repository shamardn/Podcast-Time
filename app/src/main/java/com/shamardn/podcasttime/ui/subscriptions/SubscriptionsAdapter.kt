package com.shamardn.podcasttime.ui.subscriptions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.databinding.ItemSubscriptionBinding
import com.shamardn.podcasttime.ui.BaseDiffUtil
import com.shamardn.podcasttime.ui.common.uistate.PodcastUiState

class SubscriptionsAdapter(
    private var items: List<PodcastUiState>,
    private val listener: SubscriptionsInteractionListener,
) : RecyclerView.Adapter<SubscriptionsAdapter.SubscriptionsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionsViewHolder {
        return SubscriptionsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_subscription, parent, false)
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
        oldItem.trackName == newItem.trackName


    override fun onBindViewHolder(holder: SubscriptionsViewHolder, position: Int) {
        val currentPodcast = items[position]
        holder.binding.apply {
            textItemSub.text = currentPodcast.trackName.trim()
            Glide.with(imgItemSub).load(currentPodcast.artworkUrl100).into(imgItemSub)

            root.setOnClickListener {
                listener.onClickPodcast(currentPodcast.trackId)
            }
            root.setOnLongClickListener {
                listener.onLongClickPodcast(currentPodcast)
                true
            }
        }
    }

    class SubscriptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSubscriptionBinding.bind(itemView)
    }
}