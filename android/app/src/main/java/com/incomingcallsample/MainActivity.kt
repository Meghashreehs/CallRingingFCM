package com.incomingcallfcm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.facebook.react.modules.core.DeviceEventManagerModule

class MainActivity : ReactActivity() {

    companion object {
        @JvmField
        var initialMeetingId: String? = null

        @JvmField
        var initialMeetingToken: String? = null

        @JvmField
        var initialMeetingUrl: String? = null

        @JvmField
        var initialCallType: String? = null

        @JvmField
        var initialSubject: String? = null
    }

    override fun getMainComponentName(): String = "incomingcallfcm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        handleCallIntent(intent)
    }

    override fun createReactActivityDelegate(): ReactActivityDelegate =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleCallIntent(intent)
    }

    private fun handleCallIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        val action = intent.action
        val meetingId = intent.getStringExtra("meetingId")

        if ("ACTION_ACCEPT_CALL" == action && meetingId != null) {
            NotificationManagerCompat.from(this).cancel(2001)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                setShowWhenLocked(true)
                setTurnScreenOn(true)
            }

            initialMeetingId = meetingId
            initialMeetingToken = intent.getStringExtra("meetingToken")
            initialMeetingUrl = intent.getStringExtra("meetingUrl")
            initialCallType = intent.getStringExtra("callType")
            initialSubject = intent.getStringExtra("subject")

            sendEventToJs()
        }
    }

    private fun sendEventToJs() {
        val reactContext: ReactContext? = reactInstanceManager.currentReactContext
        if (reactContext != null) {
            val params: WritableMap = Arguments.createMap().apply {
                putString("meetingId", initialMeetingId)
                putString("meetingToken", initialMeetingToken)
                putString("meetingUrl", initialMeetingUrl)
                putString("callType", initialCallType)
                putString("subject", initialSubject)
            }

            reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit("CALL_ACCEPTED", params)
        }
    }
}
