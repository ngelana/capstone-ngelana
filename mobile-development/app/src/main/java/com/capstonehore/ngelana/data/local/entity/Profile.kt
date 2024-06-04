package com.capstonehore.ngelana.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val name: String,
    val icon: Int
) : Parcelable