package id.review1st.mobile.ui.brand.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Brand(
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("total_device")
    val totalDevice: Int?,
    @SerializedName("url")
    val url: String?
)