package com.project.berbagiyukcom.homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}