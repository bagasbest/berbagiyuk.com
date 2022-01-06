package com.project.berbagiyukcom.homepage.donate_history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.berbagiyukcom.databinding.ActivityDonateHistoryBinding

class DonateHistoryActivity : AppCompatActivity() {

    private var binding: ActivityDonateHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}