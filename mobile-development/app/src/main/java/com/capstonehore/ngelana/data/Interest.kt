package com.capstonehore.ngelana.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Interest (
    val name: String,
    val icon: Int
) : Parcelable