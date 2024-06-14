package com.capstonehore.ngelana.data.local.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val code: String,
    val name: String,
    val description: String,
    val image: String
) : Parcelable