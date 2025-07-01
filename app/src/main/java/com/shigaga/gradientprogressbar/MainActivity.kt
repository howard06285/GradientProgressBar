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

        binding.customGradientProgressBar.setProgressValue(50, true)
        binding.customGradientProgressBar.setProgressBarCornerRadius(10)
        binding.customGradientProgressBar.setProgressBarText("Progress bar demo")
//            .setStartColor(getColor(R.color.purple_200))
//            .setEndColor(getColor(R.color.purple_500))
//            .setTextSize(16f)
//            .setTextColor(getColor(R.color.white))
//            .setShowText(true)
//            .setShowProgress(true)
//            .setProgressWidth(20f)
//            .setCornerRadius(10f)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}