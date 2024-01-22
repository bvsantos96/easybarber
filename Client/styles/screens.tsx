import { StyleSheet, DimensionValue, ViewStyle } from 'react-native';
import {secondColor, width, height} from './Main';

const iconSize = Math.min( width, height ) * 0.9;

export const styles = StyleSheet.create({
    imageContainer: {
        width: iconSize,
        height: iconSize,
        maxWidth: 360,
        maxHeight: 360,
        justifyContent: 'center',
        alignItems: 'center',
    },
    roundBackground: {
        backgroundColor: secondColor,
        overflow: 'hidden',
        justifyContent: 'center',
        alignItems: 'center',
        width: '100%',
        height: '100%',
        borderRadius: iconSize / 2,
        position: 'absolute',
        top: 0,
        left: 0,
    },
    bigImage: {
        position: 'absolute',
        width: '100%',
        height: '100%',
        top: 0,
        left: 0,
    },
    pageSelectionContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
    },
    centeredLogo: {
        flexShrink: 0,
        width: '264.965px' as DimensionValue,
        height: '264.965px' as DimensionValue
    }
});
