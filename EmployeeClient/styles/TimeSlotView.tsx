import { StyleSheet } from 'react-native';

export const getStyles = () => {
    return StyleSheet.create({
        "container": {
            flex: 1,
            justifyContent: "space-evenly",
        },
        "line": {
            flexDirection: "row",
            justifyContent: "space-around"
        },
        "line2": {
            flexDirection: "row",
            justifyContent: "space-around",
        },
    });
}
