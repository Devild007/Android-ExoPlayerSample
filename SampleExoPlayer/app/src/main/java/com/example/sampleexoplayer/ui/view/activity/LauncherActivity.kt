package com.example.sampleexoplayer.ui.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sampleexoplayer.databinding.ActivityLauncherBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class LauncherActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLauncherBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            delay(3000)
            startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
            finish()
        }
    }
}