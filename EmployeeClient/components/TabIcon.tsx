import { useTheme } from "@styles/ThemeContext";
import { Text, TouchableOpacity } from "react-native";

const TabIcon = ({ left, icon: Icon, text, func }: { left?: boolean, icon?: React.FC<any>, text?: string, func?: () => void }) => {
    const theme = useTheme();
    return (
        <TouchableOpacity style={[left ? { left: 20 * theme.dimensions.absoluteWidth } : { right: 20 * theme.dimensions.absoluteWidth }, { bottom: 5 * theme.dimensions.absoluteHeight, justifyContent: "center", alignItems: "center" }]} onPress={func}>
            {Icon && <Icon fill={theme.colors.mainColor} width={20 * theme.dimensions.absoluteWidth} />}
            {text && text.length > 0 && <Text style={{ color: theme.colors.mainColor }}>{text}</Text>}
        </TouchableOpacity>
    );
};

export const TabIconNoPadding = ({ icon: Icon, text, func }: { icon?: React.FC<any>, text?: string, func?: () => void }) => {
    const theme = useTheme();
    return (
        <TouchableOpacity style={{ alignItems: "center" }} onPress={func}>
            {Icon && <Icon fill={theme.colors.mainColor} width={20 * theme.dimensions.absoluteWidth} />}
            {text && text.length > 0 && <Text style={{ color: theme.colors.mainColor }}>{text}</Text>}
        </TouchableOpacity>
    );
}

export default TabIcon;
