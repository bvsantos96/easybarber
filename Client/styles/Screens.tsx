import { StyleSheet, DimensionValue, ViewStyle } from 'react-native';
import { secondColor, width, height, absoluteWidth, absoluteHeight } from './Main';

const iconSize = Math.min(width, height) * 0.9;

export const styles = StyleSheet.create({
    imageContainer: {
        position: 'absolute',
        width: 348 * absoluteWidth,
        height: 348 * absoluteWidth,
        top: 258 * absoluteHeight,
        justifyContent: 'center',
        alignItems: 'center',
    },
    bigImage: {
        width: 348 * absoluteWidth,
        height: 348 * absoluteWidth,
    },
    roundBackground: {
        backgroundColor: secondColor,
        overflow: 'hidden',
        justifyContent: 'center',
        alignItems: 'center',
        width: '90%',
        height: '90%',
        borderRadius: iconSize / 2,
        position: 'absolute',
    },
    pageSelectionContainer: {
        position: 'absolute',
        width: "100%",
        top: 685.41 * absoluteHeight,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
    },
    titleContainer: {
        position: "absolute",
        width: 300 * absoluteWidth,
        height: 66 * absoluteHeight,
        top: 91.92 * absoluteHeight,
        justifyContent: 'center',
        alignItems: 'center',
    },
    subTitleContainer: {
        position: "absolute",
        width: 300 * absoluteWidth,
        height: 66 * absoluteHeight,
        top: 160 * absoluteHeight,
        justifyContent: 'center',
        alignItems: 'center',
    },
    buttonContainer: {
        position: "absolute",
        width: 313 * absoluteWidth,
        height: 58 * absoluteHeight,
        top: 728 * absoluteHeight,
        justifyContent: 'center',
        alignItems: 'center',
    },
    centeredLogo: {
        position: 'absolute',
        width: 264.965 * absoluteWidth,
        height: 264.965 * absoluteWidth,
        top: 238.52 * absoluteHeight,
        alignItems: 'center',
        alignSelf: 'center',
        justifyContent: 'center',
    }
});
