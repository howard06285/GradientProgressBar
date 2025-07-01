package com.shigaga.gradientprogressbar

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shigaga.gradientprogressbar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupProgressBars()
        setupClickListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupProgressBars() {
        // All progress bars are already configured in XML
        // This demonstrates that the library can be used entirely through XML attributes
    }

    private fun setupClickListeners() {
        binding.btnIncrease.setOnClickListener {
            val currentProgress = binding.progressBarInteractive.getProgress()
            val newProgress = (currentProgress + 10).coerceAtMost(100)
            binding.progressBarInteractive.setProgress(newProgress, true)
            binding.progressBarInteractive.setText("Interactive ${newProgress}%")
        }

        binding.btnDecrease.setOnClickListener {
            val currentProgress = binding.progressBarInteractive.getProgress()
            val newProgress = (currentProgress - 10).coerceAtLeast(0)
            binding.progressBarInteractive.setProgress(newProgress, true)
            binding.progressBarInteractive.setText("Interactive ${newProgress}%")
        }

        binding.btnAnimate.setOnClickListener {
            binding.progressBarInteractive.setProgress(100, true)
            binding.progressBarInteractive.setText("Interactive 100%")
        }

        binding.btnReset.setOnClickListener {
            binding.progressBarInteractive.setProgress(0, true)
            binding.progressBarInteractive.setText("Interactive 0%")
        }
    }
}