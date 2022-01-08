package com.project.berbagiyukcom.homepage.donate_history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DonateHistoryModel(
    var bankName: String? = null,
    var donateTransactionId: String? = null,
    var donationId: String? = null,
    var donationName: String? = null,
    var donationOwner: String? = null,
    var donationOwnerId: String? = null,
    var nominal: Long? = 0L,
    var paymentProof: String? = null,
    var status: String? = null,
    var userId: String? = null,
    var userPhone: String? = null,
    var username: String? = null,
    var date: String? = null,
) : Parcelable