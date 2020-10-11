package id.review1st.mobile.ui.compare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import id.review1st.mobile.Configs
import id.review1st.mobile.R
import id.review1st.mobile.bases.GeneralWebViewFragment
import id.review1st.mobile.interfaces.WebAppInterface

class CompareFragment : GeneralWebViewFragment() {
    private lateinit var compareViewModel: CompareViewModel

    override fun setupWebview(webView: WebView) {
        super.setupWebview(webView)
        webView.addJavascriptInterface(WebAppInterface(this.requireActivity()), getString(R.string.app_name).replace(" ","").toLowerCase());
    }

    override fun onGeolocationPermissionsShowPrompt(
        origin: String?,
        callback: GeolocationPermissions.Callback?
    ): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val url = arguments?.getString("url")
        val headerMaps = HashMap<String, String>()
        headerMaps["X-Type"] = "mobile"
        setArg(-1, Configs.BASE_URL, url?:Configs.COMPARE_URL, "",android.R.color.black,  false,false,null,null,null,headerMaps,null)
        super.onCreate(savedInstanceState)
    }

    override fun handleCustomInternalLink(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
    }

    override fun handleCustomInternalLink(view: WebView?, url: String?): Boolean {
        return false
    }

    override fun isPreserveHistoryInsteadOfUsingHeaderWhenFollowLink(): Boolean {
        return false
    }

    override fun getCustomInfoTextView(): Int {
        return -1
    }


    override fun getCustomProgressBar(): View? {
        return null
    }

    override fun handleCustomExternalLink(view: WebView?, request: WebResourceRequest?): Boolean {
        return false
    }

    override fun handleCustomExternalLink(view: WebView?, url: String?): Boolean {
        return false
    }

    override fun getCustomInfoView(): View? {
        return null
    }
}