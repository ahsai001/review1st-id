package id.review1st.mobile

import android.os.Bundle
import com.ahsailabs.alcore.activities.BaseSplashActivity

class SplashActivity : BaseSplashActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackgroundPaneColor(R.color.colorPrimaryDark)
        setImageIcon(R.drawable.ic_logo)
        setTitleTextView(getString(R.string.app_name), R.color.colorAccent)
        setBottomTextView("${getString(R.string.app_name)} v${BuildConfig.VERSION_NAME}", R.color.colorAccent)

    }
    override fun getMinimumSplashTimeInMS(): Int {
        return 2000
    }

    override fun isMeidIncluded(): Boolean {
        return false
    }

    override fun doNextAction(): Boolean {
        MainActivity.start(this)
        finish()
        return true
    }

    override fun getCheckVersionUrl(): String? {
        return null
    }
}