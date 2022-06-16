package com.example.sampleexoplayer.ui.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleexoplayer.databinding.ActivityMainBinding
import com.example.sampleexoplayer.device.utility.getAudioQuality
import com.example.sampleexoplayer.device.utility.getVideoList
import com.example.sampleexoplayer.device.utility.getVideoQuality
import com.example.sampleexoplayer.device.utility.onTextChange
import com.example.sampleexoplayer.domain.Filter
import com.example.sampleexoplayer.domain.VideoLists
import com.example.sampleexoplayer.ui.view.adapter.VideoListingAdapter
import com.example.sampleexoplayer.ui.view.dialog.DialogFilter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import java.util.*

@SuppressLint("NonConstantResourceId")
@DelicateCoroutinesApi
class MainActivity : AppCompatActivity(), VideoListingAdapter.VideoClickedInteraction {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding

    private lateinit var adapter: VideoListingAdapter

    private val urlArrayList = ArrayList<VideoLists>()
    private val videoQualityArrayList = ArrayList<Filter>()
    private val audioQualityArrayList = ArrayList<Filter>()

    override fun onCreate(savedInstanceState: Bundle?): Unit = runBlocking {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                val ft = supportFragmentManager.beginTransaction()
                ft.add(dialog, null)
                ft.commitAllowingStateLoss()
            }
        }
    }

    private fun initAdapter() {
        binding.apply {
            adapter = VideoListingAdapter(this@MainActivity)
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

    override fun onVideoClick(url: String) {
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
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("URL", url)
        intent.putExtra("video", video)
        intent.putExtra("audio", audio)
        startActivity(intent)
    }

}