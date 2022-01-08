package com.project.berbagiyukcom.homepage.donate.donate_payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.berbagiyukcom.databinding.ActivityDonatePaymentBinding
import android.widget.Toast

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.homepage.donate.DonateModel


class DonatePaymentActivity : AppCompatActivity() {

    private var binding: ActivityDonatePaymentBinding? = null
    private lateinit var model: DonateModel
    private var bankName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonatePaymentBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        model = intent.getParcelableExtra<DonateModel>(EXTRA_DONATE) as DonateModel


        Glide.with(this)
            .load(R.drawable.donasi)
            .into(binding!!.logo)


        binding?.copyRek1?.setOnClickListener {
            val bcaTransfer = binding?.rek1?.text.toString().trim()
            bankName = "BCA Transfer"
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Anda menyalin no.rekening BCA", bcaTransfer)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                this,
                "Anda menyalin no.rekening BCA",
                Toast.LENGTH_SHORT
            ).show()
        }


        binding?.copyRek2?.setOnClickListener {
            val bniTransfer = binding?.rek2?.text.toString().trim()
            bankName = "BNI Transfer"

            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Anda menyalin no.rekening BNI", bniTransfer)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                this,
                "Anda menyalin no.rekening BNI",
                Toast.LENGTH_SHORT
            ).show()
        }


        binding?.copyRek3?.setOnClickListener {
            val bniTransfer = binding?.rek3?.text.toString().trim()
            bankName = "BRI Transfer"

            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Anda menyalin no.rekening BNI", bniTransfer)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(
                this,
                "Anda menyalin no.rekening BNI",
                Toast.LENGTH_SHORT
            ).show()
        }


        binding?.confirmBtn?.setOnClickListener {
            formValidation()
        }

        binding?.logo?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun formValidation() {
        val nominalInput = binding?.nominal?.text.toString().trim()

        if(nominalInput.isEmpty() || nominalInput == "0") {
            Toast.makeText(this, "Nominal donasi tidak boleh kosong atau 0", Toast.LENGTH_SHORT).show()
        } else if (bankName == null) {
            Toast.makeText(this, "Anda harus memilih salah satu metode pembayaran diatas dengan menekan ikon copy di bagian kanan", Toast.LENGTH_SHORT).show()
        } else {

            val intent = Intent(this, DonateUploadPaymentProofActivity::class.java)
            intent.putExtra(DonateUploadPaymentProofActivity.EXTRA_DONATE, model)
            intent.putExtra(DonateUploadPaymentProofActivity.BANK_NAME, bankName)
            intent.putExtra(DonateUploadPaymentProofActivity.NOMINAL, nominalInput)
            startActivity(intent)

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