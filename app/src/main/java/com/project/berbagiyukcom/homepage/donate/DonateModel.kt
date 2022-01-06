package com.project.berbagiyukcom.homepage.donate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DonateModel(
    var name: String? = null,
    var owner: String? = null,
    var ownerId: String? = null,
    var ownerAddress: String? = null,
    var ownerPhone: String? = null,
    var nominal: Long? = null,
    var to: String? = null,
    var description: String? = null,
    var dateStart: Long? = null,
    var dateEnd: Long? = null,
    var image: String? = null,
    var uid: String? = null,
    var donateValue: Long? = null,
) : Parcelable