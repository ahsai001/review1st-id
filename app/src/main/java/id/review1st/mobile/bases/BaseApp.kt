package id.review1st.mobile.bases

import com.ahsailabs.alcore.api.APIConstant
import com.ahsailabs.alcore.api.APIRequest
import com.ahsailabs.alcore.api.APIResponse
import com.ahsailabs.alcore.core.AlCoreApplication

/**
 * Created by ahmad s on 12/10/20.
 */
class BaseApp: AlCoreApplication() {
    override fun onCreate() {
        super.onCreate()
        //TODO: set this constanta
        //APIResponse.GENERIC_RESPONSE.OK = 1
        //APIConstant.setApiSendFcm()
    }
}