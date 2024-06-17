package com.capstonehore.ngelana.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserInformationItem (

        //ini untuk 	3.- Create User(NT)
        //4.	List User (NT)
        //5.	Read User by ID (NT)
        //6.	Update User by ID (NT)


        @field:SerializedName("createdAt")
            val createdAt: String? = null,

            @field:SerializedName("password")
            val password: String? = null,

            @field:SerializedName("birthdate")
            val birthdate: Any? = null,

            @field:SerializedName("gender")
            val gender: Any? = null,

            @field:SerializedName("phone")
            val phone: Any? = null,

            @field:SerializedName("name")
            val name: String? = null,

            @field:SerializedName("id")
            val id: String? = null,

            @field:SerializedName("email")
            val email: String? = null,

            @field:SerializedName("updatedAt")
            val updatedAt: String? = null
    )

