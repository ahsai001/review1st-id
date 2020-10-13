package id.review1st.mobile.interfaces

import android.text.TextUtils
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.ahsailabs.alcore.core.BaseActivity
import com.ahsailabs.alutils.CommonUtil

/**
 * Created by ahmad s on 11/10/20.
 */
class WebAppInterface(var activity: FragmentActivity) {

    @JavascriptInterface
    fun showToast(toast: String) {
        CommonUtil.showToast(activity, toast)
    }

    @JavascriptInterface
    fun showInfo(title: String, info: String) {
        CommonUtil.showInfo(activity, title, info)
    }

    @JavascriptInterface
    fun setTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    @JavascriptInterface
    fun shareContent(shareTitle: String, subject: String, body: String) {
        CommonUtil.shareContent(activity,shareTitle, subject, body)
    }

    @JavascriptInterface
    fun bookmark() {

    }

}