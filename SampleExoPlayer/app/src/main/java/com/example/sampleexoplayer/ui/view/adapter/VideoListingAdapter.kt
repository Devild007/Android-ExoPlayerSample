package com.example.sampleexoplayer.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleexoplayer.databinding.LayoutVideoListingBinding
import com.example.sampleexoplayer.device.utility.loadImage
import com.example.sampleexoplayer.domain.VideoLists

class VideoListingAdapter(private val videoClickedInteraction: VideoClickedInteraction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VideoLists>() {
        override fun areItemsTheSame(
            oldItem: VideoLists,
            newItem: VideoLists
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: VideoLists,
            newItem: VideoLists
        ): Boolean {
            return false
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            LayoutVideoListingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoListViewHolder(binding, videoClickedInteraction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoListViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: ArrayList<VideoLists>) {
        differ.submitList(list)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(list: ArrayList<VideoLists>) {
        differ.submitList(list)
        notifyDataSetChanged()
    }

    class VideoListViewHolder(
        private val binding: LayoutVideoListingBinding,
        private val videoClickedInteraction: VideoClickedInteraction?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoLists) = with(binding) {
            img.loadImage(url = item.img, centerCrop = true)
            tvTitle.text = item.title
            img.setOnClickListener { videoClickedInteraction?.onVideoClick(item.url ?: "") }
        }
    }

    interface VideoClickedInteraction {
        fun onVideoClick(
            url: String
        )
    }

}