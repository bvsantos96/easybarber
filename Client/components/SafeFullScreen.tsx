import React from "react";
import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";
import { useTheme } from "../styles/ThemeContext";
import { BottomSheetModalProvider } from "@gorhom/bottom-sheet";

export default function SafeFullScreen({ children = <></> }) {
    const theme = useTheme();
    const insets = useSafeAreaInsets();
    return (
        <SafeAreaView style={{
            flex: 1,
            width: theme.dimensions.width,
            height: theme.dimensions.height,
            backgroundColor: theme.colors.backgroundColor,
            paddingTop: -insets.top,
        }}>
            <BottomSheetModalProvider>
                {children}
            </BottomSheetModalProvider>
        </SafeAreaView>
    );
}
