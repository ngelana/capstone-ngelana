package com.capstonehore.ngelana.data.remote.response.users

import com.capstonehore.ngelana.data.remote.response.UserInformationItem
import com.google.gson.annotations.SerializedName

data class UserResponse(

        @field:SerializedName("data")
        val data: UserInformationItem? = null,

        @field:SerializedName("message")
        val message: String? = null
)