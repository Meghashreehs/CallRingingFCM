import React from "react";
import {Button, SafeAreaView, StyleSheet, Text, View} from "react-native";

type VideoCallScreenProps = {
  navigation: {
    replace: (screen: string) => void;
  };
  route: {
    params: {
      meetingId: string;
      meetingToken?: string;
      meetingUrl?: string;
      callType?: string;
    };
  };
};

function VideoCallScreen({
  route,
  navigation,
}: VideoCallScreenProps): React.JSX.Element {
  const {meetingId, meetingToken, meetingUrl, callType} = route.params;

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.card}>
        <Text style={styles.title}>Video Call Screen</Text>
        <Text style={styles.body}>meetingId: {meetingId}</Text>
        <Text style={styles.body}>meetingToken: {meetingToken || "-"}</Text>
        <Text style={styles.body}>meetingUrl: {meetingUrl || "-"}</Text>
        <Text style={styles.body}>callType: {callType || "-"}</Text>
        <Button title="Back to Home" onPress={() => navigation.replace("Home")} />
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
});

export default VideoCallScreen;
