import Constants from "expo-constants";
import { Dimensions } from "react-native";

const width = Dimensions.get("window").width;
const height = Dimensions.get("window").height;

const defaultTheme ={
    colors: {
        mainColor: '#DF2238',
        backgroundColor: 'white',
        text: {
            main: '#263238',
            secondary: 'rgba(0, 0, 0, 0.4)',
            alt: 'white',
            link: '#DF2238'
        },
        button: {
            main: '#DF2238',
            alt:  'white'
        }
    },
    dimensions: {
        width: width,
        height: height,
        statusBarHeight: Constants.statusBarHeight,
        absoluteHeight: height / 844,
        absoluteWidth: width / 390,
        minDimention:  Math.min(width, height),
        input: {
            width: 320 * width / 390,
            height: 60 * height / 844
        }
    },
    shadow: {
        elevation: 8,
        shadowColor: '#000',
        shadowOffset: {
            width: 0,
            height: 2,
        },
        shadowOpacity: 0.2,
        shadowRadius: 4,
    }
}
