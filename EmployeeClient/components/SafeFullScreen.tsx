import { BottomSheetModalProvider } from "@gorhom/bottom-sheet/src";
import React from "react";
import { View } from "react-native";
import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";
import { useTheme } from "../styles/ThemeContext";

export default function SafeFullScreen({ children = <></>, fixedButton }: { children?: React.ReactNode, fixedButton?: React.ReactNode }) {
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
                {fixedButton && (
                    <View style={{ position: "absolute", zIndex: 999, right: 15 * theme.dimensions.absoluteWidth, bottom: 15 * theme.dimensions.absoluteHeight }}>
                        {fixedButton}
                    </View>
                )}
            </BottomSheetModalProvider>
        </SafeAreaView>
    );
}
