package com.project.berbagiyukcom.homepage.donate_history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.project.berbagiyukcom.homepage.donate.DonateViewModel

class DonateHistoryViewModel : ViewModel() {


    private val donateHistoryList = MutableLiveData<ArrayList<DonateHistoryModel>>()
    private val listItems = ArrayList<DonateHistoryModel>()
    private val TAG = DonateViewModel::class.java.simpleName

    fun setListDonateByWaiting() {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation_transaction")
                .whereEqualTo("status", "Menunggu")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateHistoryModel()
                        model.bankName = document.data["bankName"].toString()
                        model.date = document.data["date"].toString()
                        model.donateTransactionId = document.data["donateTransactionId"].toString()
                        model.donationId = document.data["donationId"].toString()
                        model.donationName = document.data["donationName"].toString()
                        model.donationOwner = document.data["donationOwner"].toString()
                        model.donationOwnerId = document.data["donationOwnerId"].toString()
                        model.paymentProof = document.data["paymentProof"].toString()
                        model.userId = document.data["userId"].toString()
                        model.username = document.data["username"].toString()
                        model.userPhone = document.data["userPhone"].toString()
                        model.status = document.data["status"].toString()
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateHistoryList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun setListDonateWaitingByUserId(userId: String) {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation_transaction")
                .whereEqualTo("status", "Menunggu")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateHistoryModel()
                        model.bankName = document.data["bankName"].toString()
                        model.date = document.data["date"].toString()
                        model.donateTransactionId = document.data["donateTransactionId"].toString()
                        model.donationId = document.data["donationId"].toString()
                        model.donationName = document.data["donationName"].toString()
                        model.donationOwner = document.data["donationOwner"].toString()
                        model.donationOwnerId = document.data["donationOwnerId"].toString()
                        model.paymentProof = document.data["paymentProof"].toString()
                        model.userId = document.data["userId"].toString()
                        model.username = document.data["username"].toString()
                        model.userPhone = document.data["userPhone"].toString()
                        model.status = document.data["status"].toString()
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateHistoryList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun setListDonateBySuccess() {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation_transaction")
                .whereEqualTo("status", "Sukses")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateHistoryModel()
                        model.bankName = document.data["bankName"].toString()
                        model.date = document.data["date"].toString()
                        model.donateTransactionId = document.data["donateTransactionId"].toString()
                        model.donationId = document.data["donationId"].toString()
                        model.donationName = document.data["donationName"].toString()
                        model.donationOwner = document.data["donationOwner"].toString()
                        model.donationOwnerId = document.data["donationOwnerId"].toString()
                        model.paymentProof = document.data["paymentProof"].toString()
                        model.userId = document.data["userId"].toString()
                        model.username = document.data["username"].toString()
                        model.userPhone = document.data["userPhone"].toString()
                        model.status = document.data["status"].toString()
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateHistoryList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun setListDonateSuccessByUserId(userId: String) {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation_transaction")
                .whereEqualTo("status", "Sukses")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateHistoryModel()
                        model.bankName = document.data["bankName"].toString()
                        model.date = document.data["date"].toString()
                        model.donateTransactionId = document.data["donateTransactionId"].toString()
                        model.donationId = document.data["donationId"].toString()
                        model.donationName = document.data["donationName"].toString()
                        model.donationOwner = document.data["donationOwner"].toString()
                        model.donationOwnerId = document.data["donationOwnerId"].toString()
                        model.paymentProof = document.data["paymentProof"].toString()
                        model.userId = document.data["userId"].toString()
                        model.username = document.data["username"].toString()
                        model.userPhone = document.data["userPhone"].toString()
                        model.status = document.data["status"].toString()
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateHistoryList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun setListDonateByFailure() {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation_transaction")
                .whereEqualTo("status", "Ditolak")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateHistoryModel()
                        model.bankName = document.data["bankName"].toString()
                        model.date = document.data["date"].toString()
                        model.donateTransactionId = document.data["donateTransactionId"].toString()
                        model.donationId = document.data["donationId"].toString()
                        model.donationName = document.data["donationName"].toString()
                        model.donationOwner = document.data["donationOwner"].toString()
                        model.donationOwnerId = document.data["donationOwnerId"].toString()
                        model.paymentProof = document.data["paymentProof"].toString()
                        model.userId = document.data["userId"].toString()
                        model.username = document.data["username"].toString()
                        model.userPhone = document.data["userPhone"].toString()
                        model.status = document.data["status"].toString()
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateHistoryList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun setListDonateFailureByUserId(userId: String) {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation_transaction")
                .whereEqualTo("status", "Ditolak")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateHistoryModel()
                        model.bankName = document.data["bankName"].toString()
                        model.date = document.data["date"].toString()
                        model.donateTransactionId = document.data["donateTransactionId"].toString()
                        model.donationId = document.data["donationId"].toString()
                        model.donationName = document.data["donationName"].toString()
                        model.donationOwner = document.data["donationOwner"].toString()
                        model.donationOwnerId = document.data["donationOwnerId"].toString()
                        model.paymentProof = document.data["paymentProof"].toString()
                        model.userId = document.data["userId"].toString()
                        model.username = document.data["username"].toString()
                        model.userPhone = document.data["userPhone"].toString()
                        model.status = document.data["status"].toString()
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateHistoryList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getDonateHistoryList() : LiveData<ArrayList<DonateHistoryModel>> {
        return donateHistoryList
    }

}