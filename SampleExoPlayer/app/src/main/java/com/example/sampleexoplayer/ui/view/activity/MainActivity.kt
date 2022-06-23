package com.example.sampleexoplayer.ui.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.sampleexoplayer.R
import com.example.sampleexoplayer.databinding.ActivityMainBinding
import com.example.sampleexoplayer.device.utility.DataState
import com.example.sampleexoplayer.device.utility.hide
import com.example.sampleexoplayer.device.utility.show
import com.example.sampleexoplayer.domain.PlayWith
import com.example.sampleexoplayer.ui.view.models.ViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val binding get() = _binding

    private val viewModel: ViewModel by viewModels()

    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?): Unit = runBlocking {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavHostFragment()
        attachObservers()
    }

    private fun attachObservers() {
        viewModel.playWith.observe(this) {
            when (it) {
                is DataState.Success<PlayWith> -> {
                    showLoading()
                    navigationController.navigate(R.id.action_videoListFragment_to_videoDetailFragment)
                }
                is DataState.Loading -> {
                    showLoading(true)
                }
                is DataState.Error -> {
                    Log.e("MainActivity", it.exception.stackTrace.toString())
                    showLoading()
                }
            }
        }
    }

    private fun setupNavHostFragment() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navigationController = navHostFragment.navController
    }

    private fun showLoading(visibility: Boolean = false) = runOnUiThread {
        binding.apply {
            if (visibility) {
                rlLoading.show()
                lottieLoading.playAnimation()
            } else {
                rlLoading.hide()
                lottieLoading.pauseAnimation()
            }
        }
    }

}