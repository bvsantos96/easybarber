import { StyleSheet } from 'react-native';
import { width, absoluteWidth, absoluteHeight } from './Main';

export const styles = StyleSheet.create({
    container: {
        position: 'absolute',
        bottom: 0,
        width: width,
        height: 530 * absoluteHeight,
        borderTopRightRadius: 50,
        borderTopLeftRadius: 50,
        zIndex: 100
    },
});
