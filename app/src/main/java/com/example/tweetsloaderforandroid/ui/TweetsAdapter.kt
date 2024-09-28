package com.example.tweetsloaderforandroid.ui

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tweetsloaderforandroid.databinding.RecyclerViewItemBinding
import com.example.tweetsloaderforandroid.model.Tweet

private object DiffCallback : DiffUtil.ItemCallback<Tweet>() {
    override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean {
        return oldItem == newItem
    }
}

class TweetsAdapter: ListAdapter<Tweet, TweetsAdapter.TweetViewHolder>(DiffCallback) {
    class TweetViewHolder(private val binding: RecyclerViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tweet: Tweet) {
            binding.createdAt.text = tweet.createdAt
            binding.originalLink.text = if (tweet.id == "sentinel") {
                ""
            } else {
                HtmlCompat.fromHtml(
                    "<a href=\"https://twitter.com/i/status/${tweet.id}\">Original Link</a>",
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                )
            }
            binding.originalLink.movementMethod = LinkMovementMethod.getInstance()
            binding.content.text = tweet.fullText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TweetViewHolder(RecyclerViewItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}