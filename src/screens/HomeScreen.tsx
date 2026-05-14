import React from "react";
import {
  Button,
  NativeModules,
  Platform,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  View,
} from "react-native";

const {CallModule} = NativeModules;

function HomeScreen(): React.JSX.Element {
  const triggerDebugCall = () => {
    if (Platform.OS === "android" && CallModule?.simulateIncomingCall) {
      CallModule.simulateIncomingCall();
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <View style={styles.card}>
        <Text style={styles.title}>Incoming Call Sample</Text>
        <Text style={styles.body}>
          Send an FCM data message with type INCOMING_CALL to show the Android
          ringing notification.
        </Text>
        <Text style={styles.code}>
          Payload keys: type, callId, callerName, subject, meetingToken,
          meetingUrl, callType
        </Text>
        <Button title="Simulate Incoming Call" onPress={triggerDebugCall} />
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f3f5f7",
    justifyContent: "center",
    padding: 24,
  },
  card: {
    backgroundColor: "#ffffff",
    borderRadius: 16,
    padding: 24,
    gap: 12,
    shadowColor: "#09111f",
    shadowOpacity: 0.08,
    shadowRadius: 16,
    shadowOffset: {width: 0, height: 8},
    elevation: 4,
  },
  title: {
    fontSize: 24,
    fontWeight: "700",
    color: "#122033",
  },
  body: {
    fontSize: 16,
    color: "#455468",
    lineHeight: 22,
  },
  code: {
    fontSize: 14,
    color: "#0b5fff",
    lineHeight: 20,
  },
});

export default HomeScreen;
