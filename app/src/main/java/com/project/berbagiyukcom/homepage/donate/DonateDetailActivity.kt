package com.project.berbagiyukcom.homepage.donate

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityDonateDetailBinding
import com.project.berbagiyukcom.homepage.donate.donate_payment.DonatePaymentActivity
import java.text.DecimalFormat
import java.text.NumberFormat

class DonateDetailActivity : AppCompatActivity() {

    private var binding: ActivityDonateDetailBinding? = null
    private lateinit var model: DonateModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        getRole()

        val formatter: NumberFormat = DecimalFormat("#,###")
        model = intent.getParcelableExtra<DonateModel>(EXTRA_DONATE) as DonateModel

        Glide.with(this)
            .load(R.drawable.donasi)
            .into(binding!!.logo)

        Glide.with(this)
            .load(model.image)
            .into(binding!!.image)


        binding?.textView14?.text = model.name
        binding?.donateValue?.text = formatter.format(model.donateValue)
        binding?.nominal?.text = " / ${formatter.format(model.nominal)}"
        binding?.owner?.text = "Nama pembuat donasi : ${model.owner}"
        binding?.phone?.text = "No.Handhpone : ${model.ownerPhone}"
        binding?.to?.text = "Tujuan donasi: ${model.to}"
        binding?.description?.text = model.description

        val donateValuePercentage = model.nominal?.toDouble()
            ?.let { model.donateValue?.toDouble()?.div(it) }


        if (donateValuePercentage != null) {
            if(donateValuePercentage < 0.92) {
                (binding?.progrssDonate?.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = donateValuePercentage.toFloat()
                binding?.progrssDonate!!.requestLayout()
            } else {
                (binding?.progrssDonate?.layoutParams as ConstraintLayout.LayoutParams)
                    .matchConstraintPercentWidth = 0.92F
                binding?.progrssDonate!!.requestLayout()
            }
        }


        val currentTimeInMillis = System.currentTimeMillis()

        if(currentTimeInMillis < model.dateEnd!!) {
            val donationTimeLeftInMillis = model.dateEnd!! - currentTimeInMillis
            val donationTimeLeft = (donationTimeLeftInMillis / (1000*60*60*24)) % 24

            if(donationTimeLeft == 0L ){
                binding?.dayLeft?.text = "Hari Terakhir Donasi"
            } else {
                binding?.dayLeft?.text = "$donationTimeLeft Hari Lagi"
            }
        } else {
            binding?.dayLeft?.text = "Waktu Habis"
            binding?.donateBtn?.visibility = View.GONE
        }

        binding?.donateBtn?.setOnClickListener {
            val intent = Intent(this, DonatePaymentActivity::class.java)
            intent.putExtra(DonatePaymentActivity.EXTRA_DONATE, model)
            startActivity(intent)
        }


        binding?.logo?.setOnClickListener {
            onBackPressed()
        }

        binding?.callDonationOwner?.setOnClickListener {
            val phone = model.ownerPhone
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }


    }

    private fun getRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it.data?.get("role") == "admin") {
                    binding?.callDonationOwner?.visibility = View.VISIBLE
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DONATE = "donate"
    }
}