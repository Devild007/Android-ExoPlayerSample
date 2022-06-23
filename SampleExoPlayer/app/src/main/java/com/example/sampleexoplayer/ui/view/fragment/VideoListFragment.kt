package com.example.sampleexoplayer.ui.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.sampleexoplayer.databinding.FragmentVideoListBinding
import com.example.sampleexoplayer.device.utility.getAudioQuality
import com.example.sampleexoplayer.device.utility.getVideoList
import com.example.sampleexoplayer.device.utility.getVideoQuality
import com.example.sampleexoplayer.device.utility.onTextChange
import com.example.sampleexoplayer.domain.Filter
import com.example.sampleexoplayer.domain.PlayWith
import com.example.sampleexoplayer.domain.VideoLists
import com.example.sampleexoplayer.ui.view.adapter.VideoListingAdapter
import com.example.sampleexoplayer.ui.view.dialog.DialogFilter
import com.example.sampleexoplayer.ui.view.models.ViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import java.util.*

@SuppressLint("NonConstantResourceId")
@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class VideoListFragment : Fragment(), VideoListingAdapter.VideoClickedInteraction {

    private lateinit var _binding: FragmentVideoListBinding
    private val binding get() = _binding

    private val viewModel: ViewModel by activityViewModels()

    private lateinit var adapter: VideoListingAdapter

    private val urlArrayList = ArrayList<VideoLists>()
    private val videoQualityArrayList = ArrayList<Filter>()
    private val audioQualityArrayList = ArrayList<Filter>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() = runBlocking {
        binding.apply {
            etSearch.onTextChange {
                filter(it)
            }

            urlArrayList.clear()
            videoQualityArrayList.clear()
            audioQualityArrayList.clear()
            urlArrayList.addAll(getVideoList())
            videoQualityArrayList.addAll(getVideoQuality())
            audioQualityArrayList.addAll(getAudioQuality())

            initAdapter()

            imgFilter.setOnClickListener {
                val dialog = DialogFilter.newInstance(videoQualityArrayList, audioQualityArrayList)
                val ft = childFragmentManager.beginTransaction()
                ft.add(dialog, null)
                ft.commitAllowingStateLoss()
            }
        }
    }


    private fun initAdapter() {
        binding.apply {
            adapter = VideoListingAdapter(this@VideoListFragment)
            recyclerView.adapter = adapter
            adapter.submitList(urlArrayList)
        }
    }

    private fun filter(string: String) {
        val stringArrayList = ArrayList<VideoLists>()
        for (item in getVideoList()) {
            when {
                item.title?.lowercase(Locale.getDefault())
                    ?.contains(string.lowercase(Locale.getDefault())) == true -> {
                    stringArrayList.add(item)
                }
            }
        }
        adapter.filterList(stringArrayList)
    }

    override fun onVideoClick(videoLists: VideoLists) {
        viewModel.playLoading()
        var video = 0
        var audio = 0
        videoQualityArrayList.forEach {
            if (it.selected) {
                video = it.id ?: 0
            }
        }
        audioQualityArrayList.forEach {
            if (it.selected) {
                audio = it.id ?: 0
            }
        }
        val playWith = PlayWith(video, audio, videoLists.url ?: "")
        viewModel.playDetails(playWith, videoLists)
    }

}