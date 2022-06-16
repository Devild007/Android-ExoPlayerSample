package com.example.sampleexoplayer.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleexoplayer.databinding.LayoutFilterBinding
import com.example.sampleexoplayer.device.utility.hide
import com.example.sampleexoplayer.device.utility.invis
import com.example.sampleexoplayer.device.utility.show
import com.example.sampleexoplayer.domain.Filter

@SuppressLint("SetTextI18n")
class FilterSelectionAdapter(
    private val filterInteraction: FilterInteraction? = null,
    private val tag: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Filter>() {
        override fun areItemsTheSame(
            oldItem: Filter,
            newItem: Filter
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: Filter,
            newItem: Filter
        ): Boolean {
            return false
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            LayoutFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return FilterViewHolder(binding, filterInteraction, tag)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilterViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Filter>) {
        differ.submitList(list)
    }

    class FilterViewHolder(
        private val binding: LayoutFilterBinding,
        private val filterInteraction: FilterInteraction?,
        private val tag: String
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Filter) = with(binding) {
            imgCheck.invis()
            if (item.selected) {
                imgCheck.show()
            }
            tvTitle.text = item.name
            parent.setOnClickListener {
                filterInteraction?.onFilterClick(adapterPosition, tag)
            }
        }
    }

    interface FilterInteraction {
        fun onFilterClick(
            position: Int,
            tag: String
        )
    }

}