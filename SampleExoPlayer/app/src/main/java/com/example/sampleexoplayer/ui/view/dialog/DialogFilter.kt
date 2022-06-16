package com.example.sampleexoplayer.ui.view.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.sampleexoplayer.R
import com.example.sampleexoplayer.databinding.DialogFilterBinding
import com.example.sampleexoplayer.device.utility.hide
import com.example.sampleexoplayer.device.utility.show
import com.example.sampleexoplayer.domain.Filter
import com.example.sampleexoplayer.ui.view.adapter.FilterSelectionAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking

@SuppressLint("NonConstantResourceId")
@DelicateCoroutinesApi
class DialogFilter : DialogFragment(), FilterSelectionAdapter.FilterInteraction {

    private lateinit var _binding: DialogFilterBinding
    private val binding get() = _binding

    private var videoQualityArrayList: ArrayList<Filter> = ArrayList()
    private var audioArrayList: ArrayList<Filter> = ArrayList()

    private lateinit var adapterVideo: FilterSelectionAdapter
    private lateinit var adapterAudio: FilterSelectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_SampleExoPlayer)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val params = window!!.attributes
        params.dimAmount = 0.6f
        window.attributes = params
        window.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFilterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?): Unit = runBlocking {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    private fun initializeView() = runBlocking {
        try {
            binding.apply {
                rlBack.setOnClickListener { dismissAllowingStateLoss() }

                niceBottomBar.onItemSelected = { integer: Int ->
                    when (integer) {
                        0 -> {
                            viewVideoQuality()
                        }
                        1 -> {
                            viewAudioQuality()
                        }
                    }
                }
                niceBottomBar.onItemReselected = { integer: Int ->
                    when (integer) {
                        0 -> {
                            viewVideoQuality()
                        }
                        1 -> {
                            viewAudioQuality()
                        }
                    }
                }
            }
            viewVideoQuality()
            initAdapter()
        } catch (e: Exception) {
            Log.e("initializeView", "DialogFilter : ${e.printStackTrace()}")
        }
    }

    private fun viewAudioQuality() {
        binding.apply {
            recyclerViewVideo.hide()
            recyclerViewAudio.show()
        }
    }

    private fun viewVideoQuality() {
        binding.apply {
            recyclerViewVideo.show()
            recyclerViewAudio.hide()
        }
    }

    private fun initAdapter() {
        adapterVideo = FilterSelectionAdapter(this, resources.getString(R.string.title_video))
        adapterAudio = FilterSelectionAdapter(this, resources.getString(R.string.title_audio))

        binding.recyclerViewVideo.adapter = adapterVideo
        binding.recyclerViewAudio.adapter = adapterAudio

        adapterVideo.submitList(videoQualityArrayList)
        adapterAudio.submitList(audioArrayList)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onFilterClick(position: Int, tag: String) {
        if (tag == resources.getString(R.string.title_audio)) {
            audioArrayList.forEach { it.selected = false }
            audioArrayList[position].selected = true
            adapterAudio.notifyDataSetChanged()
        } else {
            videoQualityArrayList.forEach { it.selected = false }
            videoQualityArrayList[position].selected = true
            adapterVideo.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            videoQualityArrayList: ArrayList<Filter>,
            audioArrayList: ArrayList<Filter>
        ) = DialogFilter().apply {
            this.videoQualityArrayList = videoQualityArrayList
            this.audioArrayList = audioArrayList
        }
    }

}