package id.review1st.mobile.ui.brand.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class BrandResponse(
    @SerializedName("data")
    val data: List<Brand>?,
    @SerializedName("status")
    val status: Boolean
)