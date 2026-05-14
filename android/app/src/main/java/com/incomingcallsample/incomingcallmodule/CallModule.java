package com.incomingcallfcm.incomingcallmodule;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.incomingcallfcm.MainActivity;

public class CallModule extends ReactContextBaseJavaModule {

    public CallModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CallModule";
    }

    @ReactMethod
    public void getInitialCallData(Promise promise) {
        if (MainActivity.initialMeetingId != null) {
            WritableMap map = Arguments.createMap();
            map.putString("meetingId", MainActivity.initialMeetingId);
            map.putString("meetingToken", MainActivity.initialMeetingToken);
            map.putString("meetingUrl", MainActivity.initialMeetingUrl);
            map.putString("callType", MainActivity.initialCallType);
            map.putString("subject", MainActivity.initialSubject);

            MainActivity.initialMeetingId = null;
            MainActivity.initialMeetingToken = null;
            MainActivity.initialMeetingUrl = null;
            MainActivity.initialCallType = null;
            MainActivity.initialSubject = null;

            promise.resolve(map);
        } else {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void simulateIncomingCall() {
        String meetingId = "demo-session-123";
        String callerName = "Dr. Sharma";
        String subject = "Demo consultation";
        String meetingToken = "demo-token-abc";
        String meetingUrl = "https://example.com/meeting/demo-session-123";
        String callType = "PUBLIC";

        CallFcmService.showIncomingCallNotification(
                getReactApplicationContext(),
                meetingId,
                callerName,
                subject,
                meetingToken,
                meetingUrl,
                callType
        );
    }
}
