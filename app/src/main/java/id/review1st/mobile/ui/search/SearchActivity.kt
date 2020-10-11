package id.review1st.mobile.ui.search

import android.content.Context
import android.content.Intent
import android.net.MailTo
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.webkit.GeolocationPermissions
import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.ahsailabs.alcore.constants.AlCoreConstanta
import com.ahsailabs.alcore.core.BaseActivity
import com.ahsailabs.alutils.CommonUtil
import id.review1st.mobile.Configs
import id.review1st.mobile.R
import id.review1st.mobile.bases.GeneralWebViewFragment
import id.review1st.mobile.events.BrowseEvent
import id.review1st.mobile.events.CompareEvent
import id.review1st.mobile.interfaces.WebAppInterface
import org.greenrobot.eventbus.EventBus

/**
 * Created by ahmad s on 3/17/2016.
 */
class SearchActivity : BaseActivity() {
    private var newFragment: WebViewFragment? = null
    private var baseUrl: String? = null
    private var url: String? = null
    private var title: String? = null
    private var bgColor = 0
    private var defaultMessage: String? = null
    private var pageTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_webview)
        baseUrl = CommonUtil.getStringIntent(
            intent,
            PARAM_BASE_URL,
            null
        )
        url = CommonUtil.getStringIntent(
            intent,
            PARAM_URL,
            null
        )
        title = CommonUtil.getStringIntent(
            intent,
            PARAM_TITLE,
            null
        )
        bgColor = CommonUtil.getIntIntent(
            intent,
            PARAM_BG_COLOR,
            -1
        )
        defaultMessage = CommonUtil.getStringIntent(
            intent,
            PARAM_DEFAULT_MESSAGE,
            null
        )
        pageTag = CommonUtil.getStringIntent(
            intent,
            PARAM_PAGE_TAG,
            null
        )
        val toolbar =
            findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        enableUpNavigation()
        supportActionBar!!.title = title

        val usedTag =
            (if (TextUtils.isEmpty(pageTag)) com.ahsailabs.alcore.fragments.GeneralWebViewFragment.FRAGMENT_TAG else pageTag)!!

        val oldFragment =
            supportFragmentManager.findFragmentByTag(usedTag) as WebViewFragment?

        var transaction: FragmentTransaction? =
            supportFragmentManager.beginTransaction()

        if (oldFragment != null) {
            transaction!!.remove(oldFragment)
        }
        transaction!!.commit()
        transaction = supportFragmentManager.beginTransaction()
        newFragment = WebViewFragment()

        val headerMaps = HashMap<String, String>()
        headerMaps["X-Type"] = "mobile"
        newFragment?.setArg(-1, Configs.BASE_URL, "${Configs.SEARCH_URL}", "",android.R.color.black,  false,false,null,null,null,headerMaps,null)

        transaction.replace(R.id.webview_main_fragment, newFragment!!, usedTag)
        transaction.commit()

        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        val searchView = menu.findItem(R.id.main_menu_action_search).actionView as  SearchView
        searchView.setIconifiedByDefault(false)
        searchView.queryHint = getString(R.string.query_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                newFragment?.openNewLinkOrContent("${Configs.SEARCH_URL}$query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        newFragment = null
        super.onDestroy()
    }

    class WebViewFragment : GeneralWebViewFragment() {
        override fun getCustomProgressBar(): View? {
            return null
        }

        override fun getCustomInfoView(): View? {
            return null
        }

        override fun getCustomInfoTextView(): Int {
            return 0
        }

        override fun setupWebview(webView: WebView) {
            super.setupWebview(webView)
            webView.addJavascriptInterface(WebAppInterface(this.requireActivity()), getString(R.string.app_name).replace(" ","").toLowerCase());
        }

        override fun isPreserveHistoryInsteadOfUsingHeaderWhenFollowLink(): Boolean {
            return false
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun handleCustomInternalLink(view: WebView?, request: WebResourceRequest?): Boolean {
            return handleCustomInternalLink(view, request?.url?.toString())
        }

        override fun handleCustomInternalLink(view: WebView?, url: String?): Boolean {
            url?.let {
                if(url.startsWith(Configs.COMPARE_URL)){
                    EventBus.getDefault().post(CompareEvent(url))
                } else {
                    EventBus.getDefault().post(BrowseEvent(url))
                }
                activity?.finish()
                return true
            }
            return false
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun handleCustomExternalLink(
            view: WebView,
            request: WebResourceRequest
        ): Boolean {
            return handleCustomLink(view.context, request.url.toString())
        }

        override fun handleCustomExternalLink(
            view: WebView,
            url: String
        ): Boolean {
            return handleCustomLink(view.context, url)
        }

        override fun onGeolocationPermissionsShowPrompt(
            origin: String,
            callback: GeolocationPermissions.Callback
        ): Boolean {
            return false
        }

        private fun handleCustomLink(
            context: Context,
            url: String
        ): Boolean {
            if (url.startsWith(AlCoreConstanta.TELEPHONE_SCHEMA)) {
                val number = url.replace(AlCoreConstanta.TELEPHONE_SCHEMA, "")
                return CommonUtil.callNumber(context, number)
            } else if (url.startsWith(AlCoreConstanta.MAILTO_SCHEMA)) {
                val mailTo = MailTo.parse(url)
                CommonUtil.sendEmail(
                    context,
                    "send email",
                    mailTo.subject,
                    mailTo.body,
                    arrayOf(mailTo.to),
                    arrayOf(mailTo.cc)
                )
            }
            return false
        }


    }

    companion object {
        const val PARAM_BASE_URL = "param_base_url"
        const val PARAM_URL = "param_url"
        const val PARAM_TITLE = "param_title"
        const val PARAM_BG_COLOR = "param_bg_color"
        const val PARAM_DEFAULT_MESSAGE = "param_default_message"
        const val PARAM_PAGE_TAG = "param_page_tag"


        fun start(
            context: Context,
            baseUrl: String?,
            urlOrHtmlContent: String?,
            title: String?,
            defaultMessage: String?,
            bgColor: Int,
            pageTag: String?
        ) {
            val webviewIntent =
                Intent(context, SearchActivity::class.java)
            webviewIntent.putExtra(
                PARAM_BASE_URL,
                baseUrl
            )
            webviewIntent.putExtra(
                PARAM_URL,
                urlOrHtmlContent
            )
            webviewIntent.putExtra(
                PARAM_TITLE,
                title
            )
            webviewIntent.putExtra(
                PARAM_BG_COLOR,
                bgColor
            )
            webviewIntent.putExtra(
                PARAM_DEFAULT_MESSAGE,
                defaultMessage
            )
            webviewIntent.putExtra(
                PARAM_PAGE_TAG,
                pageTag
            )
            context.startActivity(webviewIntent)
        }

    }
}