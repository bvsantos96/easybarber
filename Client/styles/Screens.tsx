import { StyleSheet } from 'react-native';
import { absoluteWidth, absoluteHeight } from './Main';

export const styles = StyleSheet.create({
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
