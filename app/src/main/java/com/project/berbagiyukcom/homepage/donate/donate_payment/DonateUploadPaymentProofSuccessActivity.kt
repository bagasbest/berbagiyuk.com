package com.project.berbagiyukcom.homepage.donate.donate_payment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityDonateUploadPaymentProofSuccessBinding
import com.project.berbagiyukcom.homepage.HomeActivity
import java.text.DecimalFormat
import java.text.NumberFormat

class DonateUploadPaymentProofSuccessActivity : AppCompatActivity() {


    private var binding: ActivityDonateUploadPaymentProofSuccessBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateUploadPaymentProofSuccessBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.donasi)
            .into(binding!!.logo)

        Glide.with(this)
            .load(intent.getStringExtra(DP))
            .into(binding!!.paymentProof)


        val nominalCurrency: NumberFormat = DecimalFormat("#,###")
        binding?.date?.setText(intent.getStringExtra(DATE))
        binding?.from?.setText(intent.getStringExtra(FROM))
        binding?.to?.setText(intent.getStringExtra(TO))
        binding?.bankName?.setText(intent.getStringExtra(BANK_NAME))
        binding?.nominal?.setText("Rp " + nominalCurrency.format(intent.getStringExtra(NOMINAL)?.toLong()))

        binding?.back?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val NOMINAL = "nominal"
        const val DATE = "date"
        const val FROM = "from"
        const val TO = "to"
        const val DP = "dp"
        const val BANK_NAME = "bank_name"
    }
}