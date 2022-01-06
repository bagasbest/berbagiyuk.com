package com.project.berbagiyukcom.homepage.donate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class DonateViewModel : ViewModel() {


    private val donateList = MutableLiveData<ArrayList<DonateModel>>()
    private val listItems = ArrayList<DonateModel>()
    private val TAG = DonateViewModel::class.java.simpleName

    fun setListDonate() {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateModel()
                        model.uid = document.data["uid"].toString()
                        model.name = document.data["name"].toString()
                        model.dateStart = document.data["dateStart"] as Long?
                        model.dateEnd = document.data["dateEnd"] as Long?
                        model.description = document.data["description"].toString()
                        model.to = document.data["to"].toString()
                        model.image = document.data["image"].toString()
                        model.owner = document.data["owner"].toString()
                        model.ownerId = document.data["ownerId"].toString()
                        model.ownerAddress = document.data["ownerAddress"].toString()
                        model.ownerPhone = document.data["ownerPhone"].toString()
                        model.donateValue = document.data["donateValue"] as Long?
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun setListDonateLimit(timeNowInMillis: Long) {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation")
                .whereGreaterThan("dateEnd", timeNowInMillis)
                .limit(5)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateModel()
                        model.uid = document.data["uid"].toString()
                        model.name = document.data["name"].toString()
                        model.dateStart = document.data["dateStart"] as Long?
                        model.dateEnd = document.data["dateEnd"] as Long?
                        model.description = document.data["description"].toString()
                        model.image = document.data["image"].toString()
                        model.to = document.data["to"].toString()
                        model.owner = document.data["owner"].toString()
                        model.ownerId = document.data["ownerId"].toString()
                        model.ownerAddress = document.data["ownerAddress"].toString()
                        model.ownerPhone = document.data["ownerPhone"].toString()
                        model.donateValue = document.data["donateValue"] as Long?
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun setListDonateCurrentProject(timeNowInMillis: Long) {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("donation")
                .whereGreaterThan("dateEnd", timeNowInMillis)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DonateModel()
                        model.uid = document.data["uid"].toString()
                        model.name = document.data["name"].toString()
                        model.dateStart = document.data["dateStart"] as Long?
                        model.dateEnd = document.data["dateEnd"] as Long?
                        model.description = document.data["description"].toString()
                        model.image = document.data["image"].toString()
                        model.to = document.data["to"].toString()
                        model.owner = document.data["owner"].toString()
                        model.ownerId = document.data["ownerId"].toString()
                        model.ownerAddress = document.data["ownerAddress"].toString()
                        model.ownerPhone = document.data["ownerPhone"].toString()
                        model.donateValue = document.data["donateValue"] as Long?
                        model.nominal = document.data["nominal"] as Long?

                        listItems.add(model)
                    }
                    donateList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }


    fun getDonateList() : LiveData<ArrayList<DonateModel>> {
        return donateList
    }
}