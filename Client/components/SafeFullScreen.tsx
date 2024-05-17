import { SafeAreaView, useSafeAreaInsets } from "react-native-safe-area-context";
import { useTheme } from "../styles/ThemeContext";

export default function SafeFullScreen({ children = <></> }) {
    const theme = useTheme();
    const insets = useSafeAreaInsets();
    return (
        <SafeAreaView style={{
            flex: 1,
            width: theme.dimensions.width,
            backgroundColor: theme.colors.backgroundColor,
            paddingTop: insets.top,
            paddingBottom: insets.bottom,
        }}>
            {children}
        </SafeAreaView>
    );
}
