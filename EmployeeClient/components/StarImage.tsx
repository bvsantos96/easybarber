import StarIcon from '@assets/icons/star.svg';
import { getStyles } from "@styles/List";
import { useTheme } from "@styles/ThemeContext";
import { View } from "react-native";

export const StarImage = ({ right }: { right?: boolean }) => {
    const styles = getStyles();
    const theme = useTheme();
    return (
        <View style={[!right ? styles.ratingContainer : styles.ratingContainerRight, theme.shadow, styles.ratingContainerNoText]} >
            <View style={[styles.ratingIconContainer, styles.center, theme.shadow, { position: "relative", left: undefined }]}>
                <StarIcon style={styles.ratingIcon} width="75%" height="75%" />
            </View>
        </View >
    );
}
