# Incoming call FCM Notification (React Native)

This is a small React Native app that implements the Android incoming-call notification

## What it demonstrates

- `FirebaseMessagingService` receives an FCM data message
- Android shows a high-priority call-style ringing notification
- Accept launches `MainActivity` with call extras
- `MainActivity` stores the initial payload and emits `CALL_ACCEPTED` to React Native
- JS reads `CallModule.getInitialCallData()` on cold start and listens for `CALL_ACCEPTED` on warm start
- React Native navigates into a `VideoCall` screen
- A local debug button can trigger the same native ringing flow without Firebase

## Project structure

- `android/app/src/main/java/com/incomingcallfcm/incomingcallmodule`
  - `CallFcmService.java`
  - `NotificationHelper.java`
  - `CallActionReceiver.java`
  - `CallModule.java`
  - `CallPackage.java`
- `android/app/src/main/java/com/incomingcallfcm`
  - `MainApplication.kt`
  - `MainActivity.kt`
- `App.tsx`
  - JS listener and navigation handoff

## Before running

1. Add your Firebase `google-services.json` to:
   `android/app/google-services.json`
2. From this sample folder, install dependencies if you want it fully isolated:
   `npm install`

This repository already has compatible React Native dependencies at the workspace root, so the sample also includes Metro and Gradle paths that can resolve shared workspace `node_modules`.

## Quick demo without Firebase

1. Launch the sample app
2. Tap `Simulate Incoming Call`
3. Answer from the Android notification UI
4. The app will navigate to the `VideoCall` screen using the same native handoff as a real push

## FCM payload format

Send a data message with these keys:

```json
{
  "type": "INCOMING_CALL",
  "callId": "session-123",
  "callerName": "Dr. Sharma",
  "subject": "Consultation reminder",
  "meetingToken": "sample-token",
  "meetingUrl": "https://example.com/meeting/session-123",
  "callType": "PUBLIC"
}
```

## Native flow

1. `CallFcmService.onMessageReceived()` checks `type === INCOMING_CALL`
2. `showIncomingCallNotification()` builds a call-style notification with accept and decline actions
3. Accept launches `MainActivity` using action `ACTION_ACCEPT_CALL`
4. `MainActivity.handleCallIntent()` stores the call extras and emits `CALL_ACCEPTED`
5. `CallModule.getInitialCallData()` covers the cold-start case
6. `App.tsx` navigates to the `VideoCall` screen

## Decline flow

Decline dismisses the notification and writes a log line. You can replace that with analytics or an API callback in your real app.
