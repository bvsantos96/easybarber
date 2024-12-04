import { useTheme } from "@styles/ThemeContext";
import { TouchableOpacity, Text } from "react-native";

const TabIcon = ({ left, icon: Icon, text, func }: { left?: boolean, icon?: React.FC<any>, text?: string, func?: () => void }) => {
    const theme = useTheme();
    return (
        <TouchableOpacity style={[left ? { left: 20 * theme.dimensions.absoluteWidth } : { right: 20 * theme.dimensions.absoluteWidth }, { bottom: 5 * theme.dimensions.absoluteHeight, justifyContent: "center", alignItems: "center" }]} onPress={func}>
            {Icon && <Icon width={27 * theme.dimensions.absoluteWidth} />}
            {text && text.length > 0 && <Text>{text}</Text>}
        </TouchableOpacity>
    );
};

export default TabIcon;
