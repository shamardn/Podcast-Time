package com.shamardn.podcasttime.ui.subscriptions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.local.database.entity.PodcastEntity
import com.shamardn.podcasttime.databinding.ItemSubscriptionBinding

class SubscriptionsAdapter(
    private val items: List<PodcastEntity>,
    private val listener: SubscriptionsInteractionListener,
) : RecyclerView.Adapter<SubscriptionsAdapter.SubscriptionsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionsViewHolder {
        return SubscriptionsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_subscription, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SubscriptionsViewHolder, position: Int) {
        val currentPodcast = items[position]
        holder.binding.apply {
            textItemSub.text = currentPodcast.trackName.trim()
            Glide.with(imgItemSub).load(currentPodcast.artworkUrl100).into(imgItemSub)

            root.setOnClickListener {
                listener.onClickPodcast(currentPodcast.trackId)
            }
        }
    }

    class SubscriptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSubscriptionBinding.bind(itemView)
    }
}