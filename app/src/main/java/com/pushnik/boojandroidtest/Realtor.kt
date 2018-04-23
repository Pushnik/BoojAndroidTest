package com.pushnik.boojandroidtest

import com.google.gson.annotations.SerializedName

/**
 * Created by John Pushnik on 4/21/2018.
 */

class Realtor {
    @SerializedName("first_name")
    var firstName: String? = null
    @SerializedName("last_name")
    var lastName: String? = null
    var id: String? = null
    var rebrand: String? = null
    var isTeam: Boolean = false
    var office: String? = null
    @SerializedName("phone_number")
    var phoneNumber: String? = null
    var photo: String? = null
    var title: String? = null
}
