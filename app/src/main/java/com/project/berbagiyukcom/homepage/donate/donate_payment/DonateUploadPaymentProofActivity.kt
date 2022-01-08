package com.project.berbagiyukcom.homepage.donate.donate_payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityDonateUploadPaymentProofBinding
import com.project.berbagiyukcom.homepage.donate.DonateModel
import java.text.SimpleDateFormat

class DonateUploadPaymentProofActivity : AppCompatActivity() {

    private var binding: ActivityDonateUploadPaymentProofBinding? = null
    private lateinit var model: DonateModel
    private var dp: String? = null
    private val REQUEST_FROM_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateUploadPaymentProofBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        model = intent.getParcelableExtra<DonateModel>(EXTRA_DONATE) as DonateModel


        Glide.with(this)
            .load(R.drawable.donasi)
            .into(binding!!.logo)

        // KLIK TAMBAH GAMBAR
        binding?.imageTemp?.setOnClickListener{
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_GALLERY);
        }

        binding?.confirmBtn?.setOnClickListener {
            formValidation()
        }

        binding?.logo?.setOnClickListener {
            onBackPressed()
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun formValidation() {
        if(dp == null) {
            Toast.makeText(this, "Anda harus mengunggah bukti donasi terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else {

            binding?.progressBar?.visibility = View.VISIBLE
            /// ambil nama donatur
            val myUid = FirebaseAuth.getInstance().currentUser?.uid
            if (myUid != null) {
                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(myUid)
                    .get()
                    .addOnSuccessListener {
                        val username = it.data?.get("username").toString()
                        val userPhone = it.data?.get("phone").toString()

                        /// simpan riwayat donasi dan bukti donasi, kemudian akan diverikasi oleh admin
                        val donateId = System.currentTimeMillis().toString()
                        val nominal = intent.getStringExtra(NOMINAL)
                        val bankName = intent.getStringExtra(BANK_NAME)

                        val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss")
                        val format: String = formatter.format(donateId.toLong())


                        val donated = mapOf(
                            "donateTransactionId" to donateId,
                            "userId" to myUid,
                            "userPhone" to userPhone,
                            "username" to username,
                            "nominal" to nominal?.toLong(),
                            "donationName" to model.name,
                            "donationId" to model.uid,
                            "donationOwnerId" to model.ownerId,
                            "paymentProof" to dp,
                            "bankName" to bankName,
                            "date" to format,
                            "status" to "Menunggu",
                            "note" to ""
                        )
                        FirebaseFirestore
                            .getInstance()
                            .collection("donate_transaction")
                            .document(donateId)
                            .set(donated)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    binding?.progressBar?.visibility = View.GONE

                                    val intent = Intent(this, DonateUploadPaymentProofSuccessActivity::class.java)
                                    intent.putExtra(DonateUploadPaymentProofSuccessActivity.NOMINAL, nominal)
                                    intent.putExtra(DonateUploadPaymentProofSuccessActivity.DATE, format)
                                    intent.putExtra(DonateUploadPaymentProofSuccessActivity.FROM, username)
                                    intent.putExtra(DonateUploadPaymentProofSuccessActivity.TO, model.owner)
                                    intent.putExtra(DonateUploadPaymentProofSuccessActivity.BANK_NAME,bankName)
                                    intent.putExtra(DonateUploadPaymentProofSuccessActivity.DP,dp)
                                    startActivity(intent)


                                } else {
                                   binding?.progressBar?.visibility = View.GONE
                                    showFailureDialog()
                                }
                            }
                    }
            }
        }
    }

    /// tampilkan dialog box ketika gagal donasi
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Melakukan Donasi")
            .setMessage("Terdapat kesalahan, Silahkan periksa koneksi internet anda, dan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY) {
                uploadArticleDp(data?.data)
            }
        }
    }


    /// fungsi untuk mengupload foto kedalam cloud storage
    private fun uploadArticleDp(data: Uri?) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "payment_proof/data_" + System.currentTimeMillis() + ".png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        mProgressDialog.dismiss()
                        dp = uri.toString()
                        Glide
                            .with(this)
                            .load(dp)
                            .into(binding!!.paymentProof)
                    }
                    .addOnFailureListener { e: Exception ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "Gagal mengunggah gambar",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("imageDp: ", e.toString())
                    }
            }
            .addOnFailureListener { e: Exception ->
                mProgressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Gagal mengunggah gambar",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.d("imageDp: ", e.toString())
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DONATE = "donate"
        const val BANK_NAME = "bank_name"
        const val NOMINAL = "nominal"
    }
}