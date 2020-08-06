package com.dicoding.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserEntity(
    var id: Int = 0,
    var name: String? = null,
    var username: String? = null,
    var location: String? = null,
    var image: String? = null
) : Parcelable
