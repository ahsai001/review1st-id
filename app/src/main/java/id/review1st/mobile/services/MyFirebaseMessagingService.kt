package id.review1st.mobile.services

import android.content.Intent
import com.ahsailabs.alcore.services.FCMIntentService
import com.ahsailabs.alcore.utils.NotificationUtil
import com.ahsailabs.alutils.PrefsData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import id.review1st.mobile.MainActivity
import id.review1st.mobile.R

/**
 * Created by ahmad s on 12/10/20.
 */
class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(newToken: String) {
        PrefsData.setPushyToken(newToken)
        PrefsData.setPushyTokenSent(false)
        PrefsData.setPushyTokenLoginSent(false)

        FCMIntentService.startSending(this,"",false,false)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val customTypeCallBackHandler = object: NotificationUtil.CustomTypeCallBackHandler{
            override fun isShownInInfoList(data: MutableMap<String, Any>?): Boolean {
                return false
            }

            override fun isShownInNotifCenter(data: MutableMap<String, Any>?): Boolean {
                return true
            }

            override fun getNextIntent(data: MutableMap<String, Any>?): Intent {
                return Intent(baseContext, MainActivity::class.java)
            }

            override fun handleCustom(data: MutableMap<String, Any>?) {

            }


            override fun getNextIntentComponentType(data: MutableMap<String, Any>?): Int {
                return NotificationUtil.INTENT_COMPONENT_TYPE_ACTIVITY
            }

            override fun getTitle(data: MutableMap<String, Any>?): String {
                return data?.get("title") as String
            }

            override fun getBody(data: MutableMap<String, Any>?): String {
                return data?.get("body") as String
            }


            override fun getPhotoUrl(data: MutableMap<String, Any>?): String {
                return data?.get("photo") as String
            }

            override fun getInfoUrl(data: MutableMap<String, Any>?): String {
                return data?.get("infoUrl") as String
            }

            override fun getTypeId(data: MutableMap<String, Any>?): Int {
                return 10
            }

        }

        val customTypeCallBackHandlerList = HashMap<String, NotificationUtil.CustomTypeCallBackHandler>()
        customTypeCallBackHandlerList["pageurl"] = customTypeCallBackHandler

        NotificationUtil.onMessageReceived(baseContext,
            remoteMessage.data,
            remoteMessage.notification?.title,
            remoteMessage.notification?.body,
            MainActivity::class.java,null,null,
            R.string.app_name,R.drawable.ic_logo,customTypeCallBackHandlerList,false)
    }
}