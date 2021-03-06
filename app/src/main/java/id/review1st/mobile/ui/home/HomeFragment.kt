package id.review1st.mobile.ui.home

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import id.review1st.mobile.Configs
import id.review1st.mobile.R
import id.review1st.mobile.bases.GeneralWebViewFragment
import id.review1st.mobile.interfaces.WebAppInterface

class HomeFragment : GeneralWebViewFragment() {
    private lateinit var homeViewModel: HomeViewModel



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
        setArg(-1, Configs.BASE_URL, url?:Configs.HOME_URL, "",android.R.color.black,  true,false,null,null,null,headerMaps,null)
        super.onCreate(savedInstanceState)
    }

    override fun getCustomInfoTextView(): Int {
        return -1
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun handleCustomInternalLink(view: WebView?, request: WebResourceRequest?): Boolean {
        return handleCustomInternalLink(view, request?.url?.toString())
    }

    override fun handleCustomInternalLink(view: WebView?, url: String?): Boolean {
        url?.let {
            if(url.startsWith(Configs.COMPARE_URL)){
                findNavController().navigate(R.id.navigation_compare, Bundle().apply { putString("url", url) })
                return true
            }
        }
        return false
    }

    override fun isPreserveHistoryInsteadOfUsingHeaderWhenFollowLink(): Boolean {
        return false
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