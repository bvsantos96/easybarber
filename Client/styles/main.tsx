import { StyleSheet, Dimensions } from 'react-native';

export const inputHeight = Dimensions.get("window").height * 0.07;
export const inputWidth = '85%';
export const mainColor = "#DF2238";
export const secondColor = "rgba(109, 4, 4, 0.10)"

export const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'white',
    },
});
