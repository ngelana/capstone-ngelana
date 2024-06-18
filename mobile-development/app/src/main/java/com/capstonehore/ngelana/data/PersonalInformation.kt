package com.capstonehore.ngelana.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonalInformation (
    val title: String,
    val name: String
) : Parcelable