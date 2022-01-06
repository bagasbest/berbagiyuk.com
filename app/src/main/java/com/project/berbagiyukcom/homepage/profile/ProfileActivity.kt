package com.project.berbagiyukcom.homepage.profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityProfileBinding
import com.project.berbagiyukcom.homepage.donate.DonateViewModel
import java.text.DecimalFormat
import java.text.NumberFormat

class ProfileActivity : AppCompatActivity() {

    private var binding: ActivityProfileBinding? = null
    private var username: String? = null
    private var dp: String? = null
    private var address: String? = null
    private var phone: String? = null
    private var nominal: Long? = 0
    private var adapter: ProfileAdapter? = null


    override fun onResume() {
        super.onResume()
        getUserProfileFromDatabase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initRecyclerView()
        initViewModel()

        Glide.with(this)
            .load(R.drawable.profile)
            .into(binding!!.logo)


        getTotalDonation()
        getCurrentProject()
        getFinishedProject()


        binding?.logo?.setOnClickListener {
            onBackPressed()
        }


        binding?.editBtn?.setOnClickListener {
            val intent = Intent(this, ProfileEditActivity::class.java)
            intent.putExtra(ProfileEditActivity.EXTRA_USERNAME, username)
            intent.putExtra(ProfileEditActivity.EXTRA_DP, dp)
            intent.putExtra(ProfileEditActivity.EXTRA_ADDRESS, address)
            intent.putExtra(ProfileEditActivity.EXTRA_PHONE, phone)
            startActivity(intent)
        }

    }

    private fun initRecyclerView() {
        binding?.currentProjectRv?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        adapter = ProfileAdapter()
        binding?.currentProjectRv?.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[DonateViewModel::class.java]
        val timeNowInMillis = System.currentTimeMillis()
        binding?.progressBar?.visibility = View.VISIBLE
        viewModel.setListDonateCurrentProject(timeNowInMillis)
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

    @SuppressLint("SetTextI18n")
    private fun getFinishedProject() {
        val myUid = FirebaseAuth.getInstance().currentUser?.uid
        val timeNowInMillis = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("donation")
            .whereEqualTo("ownerId", myUid)
            .whereLessThan("dateEnd", timeNowInMillis)
            .get()
            .addOnSuccessListener { documents ->
                binding?.finishedProject?.text = "${documents.size()}\n\nProject\nSelesai"
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun getCurrentProject() {
        val myUid = FirebaseAuth.getInstance().currentUser?.uid
        val timeNowInMillis = System.currentTimeMillis()
        FirebaseFirestore.getInstance().collection("donation")
            .whereEqualTo("ownerId", myUid)
            .whereGreaterThan("dateEnd", timeNowInMillis)
            .get()
            .addOnSuccessListener { documents ->
                binding?.currentProject?.text = "${documents.size()}\n\nSedang\nBerjalan"
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserProfileFromDatabase() {
        val myUid = FirebaseAuth.getInstance().currentUser?.uid
        if (myUid != null) {
            FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(myUid)
                .get()
                .addOnSuccessListener { document ->
                    dp = document.data?.get("dp")?.toString()
                    username = document.data?.get("username")?.toString()
                    address = document.data?.get("address")?.toString()
                    phone = document.data?.get("phone")?.toString()

                    if(dp != "") {
                        binding?.let {
                            Glide.with(this)
                                .load(dp)
                                .into(it.dp)
                        }
                    } else {
                        Glide.with(this)
                            .load(R.drawable.ic_baseline_face_24)
                            .into(binding!!.dp)
                    }

                    binding?.username?.text = "Username: $username"
                    if(address == "") {
                        binding?.location?.text = "Location: Belum diisi"
                    } else {
                        binding?.location?.text = "Location: $address"
                    }
                    binding?.phone?.text = "No.Telepon: $phone"


                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalDonation() {
        val myUid = FirebaseAuth.getInstance().currentUser?.uid
        val formatter: NumberFormat = DecimalFormat("#,###")
        FirebaseFirestore.getInstance().collection("donatur")
            .whereEqualTo("userId", myUid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    nominal = (document.data["nominal"] as Long?)?.let { nominal?.plus(it) }
                }
                binding?.totalProject?.text = "Rp ${formatter.format(nominal)}\n\nTotal\nDonasi"
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}