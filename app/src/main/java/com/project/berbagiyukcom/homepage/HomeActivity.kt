package com.project.berbagiyukcom.homepage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityHomeBinding
import com.project.berbagiyukcom.homepage.create_donate.CreateDonateActivity
import com.project.berbagiyukcom.homepage.donate.DonateActivity
import com.project.berbagiyukcom.homepage.donate.DonateAdapter
import com.project.berbagiyukcom.homepage.donate.DonateViewModel
import com.project.berbagiyukcom.homepage.donate_history.DonateHistoryActivity
import com.project.berbagiyukcom.homepage.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null
    private var adapter: DonateAdapter? = null

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        checkRole()


        Glide.with(this)
            .load(R.drawable.home)
            .into(binding!!.imageView)


        Glide.with(this)
            .load(R.drawable.donasi)
            .into(binding!!.donate)

        Glide.with(this)
            .load(R.drawable.galang_dana)
            .into(binding!!.galangDana)

        Glide.with(this)
            .load(R.drawable.profile_plain)
            .into(binding!!.profile)

        Glide.with(this)
            .load(R.drawable.banner_galang_dana)
            .into(binding!!.banner)



        binding?.donateNow?.setOnClickListener {
            startActivity(Intent(this, DonateActivity::class.java))
        }

        binding?.view4?.setOnClickListener {
            startActivity(Intent(this, DonateActivity::class.java))
        }

        binding?.view6?.setOnClickListener {
            startActivity(Intent(this, CreateDonateActivity::class.java))
        }

        binding?.view5?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding?.donateHistory?.setOnClickListener {
            startActivity(Intent(this, DonateHistoryActivity::class.java))

        }



    }

    private fun checkRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val role = it.data?.get("role").toString()
                if(role == "admin") {
                    binding?.donateHistory?.visibility = View.VISIBLE
                    Glide.with(this)
                        .load(R.drawable.donate)
                        .into(binding!!.donateHistory)
                }
            }
    }


    private fun initRecyclerView() {
        binding?.rvDonate?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter = DonateAdapter()
        binding?.rvDonate?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[DonateViewModel::class.java]
        val timeNowInMillis = System.currentTimeMillis()
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListDonateLimit(timeNowInMillis)
        viewModel.getDonateList().observe(this) { alarm ->
            if (alarm.size > 0) {
                binding!!.noData.visibility = View.GONE
                adapter!!.setData(alarm)
            } else {
                binding!!.noData.visibility = View.VISIBLE
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}