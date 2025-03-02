import { useTheme } from "@styles/ThemeContext";
import Pressable from "./Pressable";
import MaterialIcons from '@expo/vector-icons/MaterialIcons';

export interface Props {
    selected: boolean;
    setSelected: (value: boolean) => void;
}

export default function DeleteButton({ setSelected }: Props) {
    const theme = useTheme();
    return (
        <Pressable style={{
            alignSelf: "center",
            borderRadius: (35 / 2) * theme.dimensions.absoluteWidth,
            minWidth: 35 * theme.dimensions.absoluteWidth,
            minHeight: 35 * theme.dimensions.absoluteWidth,
            justifyContent: 'center',
            alignItems: 'center',
            borderWidth: 1,
            borderColor: theme.colors.errorColor,
        }} onPress={() => setSelected(false)}>
            <MaterialIcons name="delete-outline" size={24 * theme.dimensions.absoluteWidth} color={theme.colors.errorColor} />
        </Pressable >
    )
}
