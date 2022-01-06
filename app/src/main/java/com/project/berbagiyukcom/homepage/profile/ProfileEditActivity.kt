package com.project.berbagiyukcom.homepage.profile

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityProfileEditBinding

class ProfileEditActivity : AppCompatActivity() {

    private var binding: ActivityProfileEditBinding? = null
    private var dp: String? = null
    private val REQUEST_FROM_GALLERY = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.profile)
            .into(binding!!.logo)


        if(intent.getStringExtra(EXTRA_DP) != ""){
            Glide.with(this)
                .load(intent.getStringExtra(EXTRA_DP))
                .into(binding!!.donateImage)
        }

        binding?.username?.setText(intent.getStringExtra(EXTRA_USERNAME))
        binding?.address?.setText(intent.getStringExtra(EXTRA_ADDRESS))
        binding?.phone?.setText(intent.getStringExtra(EXTRA_PHONE))

        binding?.logo?.setOnClickListener {
            onBackPressed()
        }

        binding?.save?.setOnClickListener {
            formValidation()
        }

        // KLIK TAMBAH GAMBAR
        binding?.imageTemp?.setOnClickListener{
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_GALLERY);
        }

    }

    private fun formValidation() {
        val username = binding?.username?.text.toString().trim()
        val address = binding?.address?.text.toString().trim()
        val phone = binding?.phone?.text.toString().trim()


        when {
            username.isEmpty() -> {
                Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            address.isEmpty() -> {
                Toast.makeText(this, "Alamat atau Lokasi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            phone.isEmpty() -> {
                Toast.makeText(this, "No.Telepon tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()


        val myUid = FirebaseAuth.getInstance().currentUser?.uid
        if(dp == null) {
            val data = mapOf(
                "username" to username,
                "address" to address,
                "phone" to phone,
            )
            if (myUid != null) {
                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(myUid)
                    .update(data)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            mProgressDialog.dismiss()
                            showSuccessDialog()
                        } else {
                            mProgressDialog.dismiss()
                            showFailureDialog()
                        }
                    }
            }
        } else {
            val data = mapOf(
                "username" to username,
                "address" to address,
                "phone" to phone,
                "dp" to dp,
            )
            if (myUid != null) {
                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(myUid)
                    .update(data)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            mProgressDialog.dismiss()
                            showSuccessDialog()
                        } else {
                            mProgressDialog.dismiss()
                            showFailureDialog()
                        }
                    }
            }
        }

    }

    /// tampilkan dialog box ketika gagal memperbarui profil
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Memperbarui Profil")
            .setMessage("Terdapat kesalahan ketika memperbarui profil, silahkan periksa koneksi internet anda, dan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }

    /// tampilkan dialog box ketika sukses memperbarui profil
    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Memperbarui Profil")
            .setMessage("Profil anda akan segera berubah")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, i ->
                dialogInterface.dismiss()
                onBackPressed()
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
        val imageFileName = "profile/data_" + System.currentTimeMillis() + ".png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        mProgressDialog.dismiss()
                        dp = uri.toString()
                        Glide
                            .with(this)
                            .load(dp)
                            .into(binding!!.donateImage)
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
        const val EXTRA_USERNAME = "username"
        const val EXTRA_DP = "dp"
        const val EXTRA_ADDRESS= "address"
        const val EXTRA_PHONE= "phone"
    }
}