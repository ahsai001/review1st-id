package id.review1st.mobile

import androidx.lifecycle.ViewModel
import id.review1st.mobile.ui.brand.models.Brand

class MainViewModel : ViewModel() {
    val brandList: ArrayList<Brand> = ArrayList()
}