import { StyleSheet, Dimensions } from 'react-native';
import {inputHeight, inputWidth, mainColor, secondColor, width, height} from './main';

const iconW = width * 0.9;

export const styles = StyleSheet.create({
    imageContainer: {
        width: iconW,
        height: iconW,
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
        borderRadius: iconW / 2,
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
    }
});
