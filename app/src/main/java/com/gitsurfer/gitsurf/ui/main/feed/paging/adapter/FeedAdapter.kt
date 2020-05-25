package com.gitsurfer.gitsurf.ui.main.feed.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gitsurfer.gitsurf.R
import com.gitsurfer.gitsurf.databinding.ItemFeedBinding
import com.gitsurfer.gitsurf.model.network.models.response.Feed

class FeedAdapter : PagedListAdapter<Feed, FeedAdapter.FeedViewHolder>(FEED_COMPARATOR) {

  private var feedItemsList: List<Feed> = listOf()

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): FeedViewHolder {
    return FeedViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_feed, parent, false
        )
    )
  }

  fun setItems(items: List<Feed>) {
    feedItemsList = items
  }

  override fun getItemCount(): Int {
    return feedItemsList.size
  }

  override fun onBindViewHolder(
    holder: FeedViewHolder,
    position: Int
  ) {
    val feed = getItem(position)
    feed?.let { holder.bind(it) }
  }

  class FeedViewHolder(private val binding: ItemFeedBinding) :
      RecyclerView.ViewHolder(binding.root) {

    fun bind(feed: Feed) {
      binding.feed = feed
      val action = feed.payload.action + " <b>" + feed.repo.name + "</b>"
      HtmlCompat.fromHtml(action, HtmlCompat.FROM_HTML_MODE_LEGACY)
      binding.tvAction.text = HtmlCompat.fromHtml(action, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
  }

  companion object {
    private val FEED_COMPARATOR = object : DiffUtil.ItemCallback<Feed>() {
      override fun areItemsTheSame(
        oldItem: Feed,
        newItem: Feed
      ): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(
        oldItem: Feed,
        newItem: Feed
      ): Boolean =
        newItem == oldItem
    }
  }
}