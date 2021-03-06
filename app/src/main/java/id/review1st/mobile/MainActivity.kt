package id.review1st.mobile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ahsailabs.alcore.activities.MessageListActivity
import com.ahsailabs.alcore.services.FCMIntentService
import com.ahsailabs.alutils.EventsUtil
import com.ahsailabs.alutils.PrefsData
import com.zaitunlabs.zlcore.activities.BookmarkListActivity
import com.zaitunlabs.zlcore.events.ShowBookmarkInfoEvent
import id.review1st.mobile.events.BrowseEvent
import id.review1st.mobile.events.CompareEvent
import id.review1st.mobile.ui.search.SearchActivity
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_brand, R.id.navigation_compare))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        EventsUtil.register(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.main_menu_action_search){
            SearchActivity.start(this,Configs.BASE_URL,Configs.SEARCH_URL,"Cari","",android.R.color.black, "search")
            return true
        } else if(item.itemId == R.id.main_menu_action_message_list){
            MessageListActivity.start(this,false)
        } else if(item.itemId == R.id.main_menu_action_bookmark_list){
            BookmarkListActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    @Subscribe
    fun onEvent(event: CompareEvent){
        Handler(Looper.myLooper()!!).postDelayed({
            findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_compare, Bundle().apply { putString("url",event.url) })
        }, 500)
    }

    @Subscribe
    fun onEvent(event: BrowseEvent){
        Handler(Looper.myLooper()!!).postDelayed({
            findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_home, Bundle().apply { putString("url",event.url) })
        }, 500)
    }

    @Subscribe
    fun onEvent(event: ShowBookmarkInfoEvent){
        var navId = R.id.navigation_home
        if(event.link.startsWith(Configs.COMPARE_URL)){
            navId = R.id.navigation_compare
        }
        Handler(Looper.myLooper()!!).postDelayed({
            findNavController(R.id.nav_host_fragment).navigate(navId, Bundle().apply { putString("url",event.link) })
        }, 500)

    }

    override fun onResume() {
        super.onResume()
        val toke = PrefsData.getPushyToken()
        Log.e("ahmad", toke)
        FCMIntentService.startSending(this,"",false,false)
    }

    override fun onDestroy() {
        EventsUtil.unregister(this)
        super.onDestroy()
    }

    companion object{
        fun start(context: Context){
            val mainIntent = Intent(context, MainActivity::class.java)
            context.startActivity(mainIntent)
        }
    }
}