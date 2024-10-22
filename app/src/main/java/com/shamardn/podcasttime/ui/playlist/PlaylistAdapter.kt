package com.shamardn.podcasttime.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shamardn.podcasttime.BR
import com.shamardn.podcasttime.R
import com.shamardn.podcasttime.data.datasource.local.database.entity.PlaylistEntity
import com.shamardn.podcasttime.ui.BaseDiffUtil

class PlaylistAdapter(
    private var list: List<PlaylistEntity>,
    private var listener: PlaylistsListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(newList: List<PlaylistEntity>) {
        val diffResult =
            DiffUtil.calculateDiff(BaseDiffUtil(list, newList, ::areItemsSame, ::areContentSame))
        list = newList
        diffResult.dispatchUpdatesTo(this)

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == (list.size - 1)) {
            R.layout.item_add_playlist
        } else {
            R.layout.item_playlist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), viewType, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        if (position != (list.size - 1)) {
            holder.binding.run {
                setVariable(
                    BR.viewModel, list[position]
                )
                setVariable(
                    BR.listener, listener
                )
            }
        } else {
            holder.binding.run {
                setVariable(
                    BR.listener, listener
                )
            }
        }
    }

    override fun getItemCount(): Int = list.size
    private fun areItemsSame(oldItem: PlaylistEntity, newItem: PlaylistEntity) =
        oldItem.playlistName == newItem.playlistName

    private fun areContentSame(oldItem: PlaylistEntity, newItem: PlaylistEntity) =
        oldItem.playlistName == newItem.playlistName
}

interface PlaylistsListener {
    fun onPlaylistClick(playlist: PlaylistEntity)
    fun onAddNewPlaylistClick()
}

