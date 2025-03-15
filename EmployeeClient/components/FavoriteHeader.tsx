import MaterialIcons from '@expo/vector-icons/MaterialIcons';
import { useTheme } from "@styles/ThemeContext";
import Pressable from "./Pressable";

export interface Props {
    selected: boolean;
    setSelected: (value: boolean) => void;
}

export default function FavoriteHeader({ selected, setSelected }: Props) {
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
            borderColor: theme.colors.mainColor,
        }} onPress={() => setSelected(!selected)}>
            {
                selected ?
                    <MaterialIcons name="favorite" size={24 * theme.dimensions.absoluteWidth
                    } color={theme.colors.mainColor} /> :
                    <MaterialIcons name="favorite-outline" size={24 * theme.dimensions.absoluteWidth} color={theme.colors.mainColor} />}
        </Pressable >
    )
}
