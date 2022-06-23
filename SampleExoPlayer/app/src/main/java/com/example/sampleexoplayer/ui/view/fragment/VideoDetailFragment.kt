package com.example.sampleexoplayer.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.sampleexoplayer.databinding.FragmentVideoDetailBinding
import com.example.sampleexoplayer.device.utility.SessionManager
import com.example.sampleexoplayer.device.utility.isNotNull
import com.example.sampleexoplayer.device.utility.loadImage
import com.example.sampleexoplayer.ui.view.activity.PlayerActivity
import com.example.sampleexoplayer.ui.view.models.ViewModel
import kotlinx.coroutines.runBlocking

class VideoDetailFragment : Fragment() {

    private lateinit var _binding: FragmentVideoDetailBinding
    private val binding get() = _binding

    private val viewModel: ViewModel by activityViewModels()

    private val sessionManager = SessionManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeView()
    }

    @SuppressLint("StaticFieldLeak")
    private fun initializeView() = runBlocking {
        binding.apply {
            img.loadImage(viewModel.videoLists.img)
            tvTitle.text = viewModel.videoLists.title

            object : YouTubeExtractor(requireContext()) {
                override fun onExtractionComplete(
                    ytFiles: SparseArray<YtFile>?,
                    videoMeta: VideoMeta?
                ) {
                    if (ytFiles.isNotNull()) {
                        tvDescription.text = videoMeta?.shortDescription ?: ""
                    }
                }
            }.extract(viewModel.videoLists.url)

            rlBack.setOnClickListener { findNavController().navigateUp() }
            rlImg.setOnClickListener { playVideo() }
            materialButton.setOnClickListener { playVideo() }
        }
    }

    private fun playVideo() {

        sessionManager.keyPlayWith = viewModel.getPlayWith()
        startActivity(Intent(requireContext(), PlayerActivity::class.java))
    }
}