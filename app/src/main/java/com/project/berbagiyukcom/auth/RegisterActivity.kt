package com.project.berbagiyukcom.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshurawat.hasher.HashType
import com.himanshurawat.hasher.Hasher
import com.project.berbagiyukcom.R
import com.project.berbagiyukcom.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var binding: ActivityRegisterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Glide.with(this)
            .load(R.drawable.logo)
            .into(binding!!.logo)


        binding?.back?.setOnClickListener {
            onBackPressed()
        }

        binding?.register?.setOnClickListener {
            formValidation()
        }
    }

    private fun formValidation() {
        val username = binding?.username?.text.toString().trim()
        val email = binding?.email?.text.toString().trim()
        val password = binding?.password?.text.toString().trim()
        val phone = binding?.phone?.text.toString().trim()


        when {
            username.isEmpty() -> {
                Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            email.isEmpty() -> {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            phone.isEmpty() -> {
                Toast.makeText(this, "No.Telepon tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            else -> {


                binding?.progressBar?.visibility = View.VISIBLE
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            saveUserData(username, email, phone, password)
                        } else {
                            binding?.progressBar?.visibility = View.GONE
                            showFailureDialog()
                        }

                    }

            }
        }
    }

    private fun saveUserData(username: String, email: String, phone: String, password: String) {

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val data = mapOf(
            "uid" to uid,
            "username" to username,
            "email" to email,
            "phone" to phone,
            "password" to Hasher.hash(password, HashType.SHA_512),
            "role" to "user"
        )


        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .set(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding?.progressBar?.visibility = View.GONE
                    showSuccessDialog()
                } else {
                    binding?.progressBar?.visibility = View.GONE
                    showFailureDialog()
                }
            }


    }

    /// jika sukses register
    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Berhasil Daftar")
            .setMessage("Silahkan login")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }

    /// jika gagal register, munculkan alert dialog gagal
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Daftar")
            .setMessage("Terdapat kesalahan ketika login, silahkan periksa koneksi internet anda, dan coba lagi nanti")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}