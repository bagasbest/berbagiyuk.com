package com.project.berbagiyukcom.homepage.donate_history

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityDonateHistoryDetailBinding
import java.text.DecimalFormat
import java.text.NumberFormat

class DonateHistoryDetailActivity : AppCompatActivity() {

    private var binding: ActivityDonateHistoryDetailBinding? = null
    private var model: DonateHistoryModel? = null
    private var note: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.donate_new)
            .into(binding!!.logo)

        checkRole()
        val formatter: NumberFormat = DecimalFormat("#,###")
        model = intent.getParcelableExtra<DonateHistoryModel>(EXTRA_DONATE) as DonateHistoryModel

        Glide.with(this)
            .load(model?.paymentProof)
            .into(binding!!.paymentProof)

        binding?.username?.text = "Username  : ${model?.username}"
        binding?.phone?.text = "No.Handphone : ${model?.userPhone}"
        binding?.date?.text = "Tangal Donasi : ${model?.date}"
        binding?.bankName?.text = "Bank : ${model?.bankName}"
        binding?.date?.text = "Nominal Donasi: ${formatter.format(model?.nominal)}"

        binding?.donationName?.text = "Nama Donasi : ${model?.donationName}"
        binding?.date?.text = "Penyelenggara Donasi : ${model?.donationOwner}"


        binding?.accBtn?.setOnClickListener {
            note = binding?.note?.text.toString().trim()
            if (note == null) {
                Toast.makeText(this, "Berikan catatan anda untuk donatur ini", Toast.LENGTH_SHORT)
                    .show()
            } else {
                showConfirmationDialog(
                    "Konfirmasi Menerima Bukti Donasi",
                    "Apakah anda yakin nominal donasi yang dikirimkan oleh ${model?.username} sudah masuk ke rekening admin ?"
                )
            }
        }

        binding?.declineBtn?.setOnClickListener {

            note = binding?.note?.text.toString().trim()
            if (note == null) {
                Toast.makeText(this, "Berikan catatan anda untuk donatur ini", Toast.LENGTH_SHORT)
                    .show()
            } else {
                showConfirmationDialog(
                    "Konfirmasi Menolak Bukti Donasi",
                    "Apakah anda yakin nominal donasi yang dikirimkan oleh ${model?.username} tidak masuk ke rekening admin, selama 1 x 24 Jam ?"
                )
            }

        }


        binding?.logo?.setOnClickListener {
            onBackPressed()
        }

    }

    private fun showConfirmationDialog(title: String, description: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(description)
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                if (title == "Konfirmasi Menolak Bukti Donasi") {
                    declineDonation()
                } else {
                    acceptDonation()
                }
            }
            .show()
    }

    private fun acceptDonation() {
        binding?.progressBar?.visibility = View.VISIBLE

        val data = mapOf(
            "status" to "Sukses",
            "note" to note
        )

        model?.donateTransactionId?.let {
            FirebaseFirestore
                .getInstance()
                .collection("donate_transaction")
                .document(it)
                .update(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveDonationHistory()
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        showFailureDialog(
                            "Gagal Menerima Bukti Donasi",
                            "Periksa koneksi internet anda dan coba lagi nanti"
                        )
                    }
                }
        }
    }

    private fun saveDonationHistory() {
        /// simpan riwayat donasi (untuk memperoleh total donasi dari user tersebut pada halaman profil)
        val donateId = System.currentTimeMillis().toString()
        val donated = mapOf(
            "donateId" to donateId,
            "userId" to model?.userId,
            "username" to model?.username,
            "nominal" to model?.nominal,
            "donateName" to model?.donationName,
            "ownerId" to model?.donationOwnerId,
        )
        FirebaseFirestore
            .getInstance()
            .collection("donatur")
            .document(donateId)
            .set(donated)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {


                    model?.donationId?.let {
                        FirebaseFirestore
                            .getInstance()
                            .collection("donation")
                            .document(it)
                            .get()
                            .addOnSuccessListener { doc ->

                                val donateValue = doc.data?.get("donateValue") as Long

                                /// update dana terkumpul pada donasi
                                val donatedValue = model?.nominal?.plus(donateValue)
                                model?.donationId?.let { it1 ->
                                    FirebaseFirestore
                                        .getInstance()
                                        .collection("donation")
                                        .document(it1)
                                        .update("donateValue", donatedValue)
                                        .addOnCompleteListener { updateDonatedValue ->

                                            if (updateDonatedValue.isSuccessful) {
                                                binding?.progressBar?.visibility = View.GONE
                                                showSuccessDialog(
                                                    "Sukses Menerima Bukti Donasi",
                                                    "Anda menerima bukti donasi yang dikirimkan oleh ${model?.username}"
                                                )
                                            } else {
                                                binding?.progressBar?.visibility = View.GONE
                                                showFailureDialog(
                                                    "Gagal Menerima Bukti Donasi",
                                                    "Periksa koneksi internet anda dan coba lagi nanti"
                                                )
                                            }
                                        }

                                }
                            }

                    }
                } else {
                    binding?.progressBar?.visibility = View.GONE
                    showFailureDialog(
                        "Gagal Menerima Bukti Donasi",
                        "Periksa koneksi internet anda dan coba lagi nanti"
                    )
                }
            }
    }

    private fun declineDonation() {

        binding?.progressBar?.visibility = View.VISIBLE

        val data = mapOf(
            "status" to "Ditolak",
            "note" to note
        )

        model?.donateTransactionId?.let {
            FirebaseFirestore
                .getInstance()
                .collection("donate_transaction")
                .document(it)
                .update(data)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding?.progressBar?.visibility = View.GONE
                        showSuccessDialog(
                            "Sukses Menolak Bukti Donasi",
                            "Anda menolak bukti donasi yang dikirimkan oleh ${model?.username}"
                        )
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        showFailureDialog(
                            "Gagal Menolak Bukti Donasi",
                            "Periksa koneksi internet anda dan coba lagi nanti"
                        )
                    }
                }
        }
    }

    private fun checkRole() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance().collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if (it.data?.get("role") == "admin") {
                    binding?.accBtn?.visibility = View.VISIBLE
                    binding?.declineBtn?.visibility = View.VISIBLE
                }
            }
    }


    private fun showSuccessDialog(title: String, description: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(description)
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }

    private fun showFailureDialog(title: String, description: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(description)
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DONATE = "donate"
    }
}