package com.addcn.flutterperferences

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserInfo : Serializable {
    @SerializedName("data")
    val data: DataBean? = null
}

class DataBean : Serializable {
    @SerializedName("access_token")
    val accessToken: String = ""
    @SerializedName("refresh_token")
    val refreshToken: String = ""
}