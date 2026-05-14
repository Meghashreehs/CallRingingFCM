import React, {useEffect} from "react";
import {
  AppState,
  DeviceEventEmitter,
  NativeModules,
  Platform,
} from "react-native";
import {
  NavigationContainer,
  createNavigationContainerRef,
} from "@react-navigation/native";
import {createNativeStackNavigator} from "@react-navigation/native-stack";
import HomeScreen from "./src/screens/HomeScreen";
import VideoCallScreen from "./src/screens/VideoCallScreen";

type CallPayload = {
  meetingId: string;
  meetingToken?: string;
  meetingUrl?: string;
  callType?: string;
  subject?: string;
};

type RootStackParamList = {
  Home: undefined;
  VideoCall: {
    meetingId: string;
    meetingToken?: string;
    meetingUrl?: string;
    callType?: string;
    subject?: string;
  };
};

const Stack = createNativeStackNavigator<RootStackParamList>();
const navigationRef = createNavigationContainerRef<RootStackParamList>();
const {CallModule} = NativeModules;

function App(): React.JSX.Element {
  useEffect(() => {
    const handleCallNavigation = (data?: CallPayload | null) => {
      if (!data?.meetingId || !navigationRef.isReady()) {
        return;
      }

      navigationRef.resetRoot({
        index: 0,
        routes: [
          {
            name: "VideoCall",
            params: {
              meetingId: data.meetingId,
              meetingToken: data.meetingToken,
              meetingUrl: data.meetingUrl,
              callType: data.callType,
              subject: data.subject,
            },
          },
        ],
      });
    };

    const syncCallData = async () => {
      if (Platform.OS !== "android" || !CallModule) {
        return;
      }

      try {
        const data = await CallModule.getInitialCallData();
        handleCallNavigation(data);
      } catch (error) {
        console.error("Failed to sync initial call data", error);
      }
    };

    const callAcceptedSubscription = DeviceEventEmitter.addListener(
      "CALL_ACCEPTED",
      (data: CallPayload) => {
        handleCallNavigation(data);
      },
    );

    const appStateSubscription = AppState.addEventListener(
      "change",
      nextAppState => {
        if (nextAppState === "active") {
          syncCallData();
        }
      },
    );

    return () => {
      callAcceptedSubscription.remove();
      appStateSubscription.remove();
    };
  }, []);

  return (
    <NavigationContainer
      ref={navigationRef}
      onReady={() => {
        if (Platform.OS === "android" && CallModule) {
          CallModule.getInitialCallData()
            .then((data: CallPayload | null) => {
              if (data?.meetingId) {
                navigationRef.resetRoot({
                  index: 0,
                  routes: [{name: "VideoCall", params: data}],
                });
              }
            })
            .catch((error: unknown) => {
              console.error("Failed to read call data on startup", error);
            });
        }
      }}>
      <Stack.Navigator>
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="VideoCall" component={VideoCallScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
