import React, { createContext, useContext, ReactNode } from 'react';
import { StatusBar, Dimensions, Platform, StyleProp, ViewStyle } from 'react-native';
import Constants from 'expo-constants';
import { StatusBarStyle } from 'expo-status-bar';

const getStatusBarHeight = (): number => {
    if (Platform.OS === 'ios') {
        return StatusBar.currentHeight || 25; // Default iOS status bar height
    } else if (Platform.OS === 'android') {
        return StatusBar.currentHeight || StatusBar.currentHeight === 0 || StatusBar.currentHeight === undefined ? 24 : StatusBar.currentHeight;
    }
    return 0; // Default status bar height
}

const width = Dimensions.get('window').width;
const height = Dimensions.get('window').height + Constants.statusBarHeight;
const minDimension = Math.min(width, height);
const tabHeight = 50 * height / 844;

interface Colors {
    mainColor: string;
    gradientColors: string[];
    successColor: string;
    infoColor: string;
    backgroundColor: string;
    iconBackground: string;
    imageBackground: string;
    statusBarOnHome: StatusBarStyle;
    logoBackgound: string;
    borderAlt: string;
    text: {
        main: string;
        secondary: string;
        alt: string;
        link: string;
        black: string;
        lightBlack: string;
        lightGray: string;
        lightWhite: string;
        darkBlueGray: string;
    };
    button: {
        main: string;
        alt: string;
        border: string;
    };
}

interface DimensionsData {
    width: number;
    height: number;
    minDimension: number;
    heightWithoutStatusBar: number;
    statusBarHeight: number;
    absoluteMinDimension: number;
    absoluteHeight: number;
    absoluteWidth: number;
    tabHeight: number;
    maxSnapPoint: string;
    input: {
        width: number;
        height: number;
    };
}

interface Fonts {
    size: {
        _8: number;
        _9: number;
        _10: number;
        _11: number;
        _12: number;
        _13: number;
        _14: number;
        _15: number;
        _16: number;
        _17: number;
        _18: number;
        _19: number;
        _20: number;
        _21: number;
        _22: number;
        _23: number;
        _24: number;
        _25: number;
        _27: number;
        _28: number;
        _29: number;
        _30: number;
        _31: number;
        _32: number;
        _33: number;
        _34: number;
        _35: number;
    };
}

interface Theme {
    colors: Colors;
    dimensions: DimensionsData;
    strongShadow: StyleProp<ViewStyle>;
    shadow: StyleProp<ViewStyle>;
    fonts: Fonts;
}

const ThemeContext = createContext<Theme | null>(null);

export const useTheme = (): Theme => {
    const context = useContext(ThemeContext);
    if (!context) {
        throw new Error('useTheme must be used within a ThemeProvider');
    }
    return context;
};

function rgbToHex(r: number, g: number, b: number): string {
    r = Math.max(0, Math.min(255, +r));
    g = Math.max(0, Math.min(255, +g));
    b = Math.max(0, Math.min(255, +b));

    const toHex = (n: number) => n.toString(16).padStart(2, '0');

    return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
}

export const mainColor = '3, 217, 153';
export const gradientColors = [`rgb(${mainColor})`, `rgb(3, 217, 186)`];
export const textColor = "0, 0, 0";
export const  backgroundColor = '255, 255, 255';
const textColors = textColor.split(', ');
const shadowColor = rgbToHex(+textColors[0], +textColors[1], +textColors[2]);
export const theme = "dark";
export const borderColor = "255, 255, 255";

const defaultTheme: Theme = {
    colors: {
        mainColor: `rgb(${mainColor})`,
        gradientColors: gradientColors,
        successColor: '#6FC138',
        backgroundColor: `rgb(${backgroundColor})`,
        infoColor: '#2A74CC',
        iconBackground: `rgba(${mainColor}, 0.1)`,
        imageBackground: `rgba(${mainColor}, 0.1)`,
        statusBarOnHome: theme,
        logoBackgound: "white",
        borderAlt: `rgba(${borderColor}, 0.1)`,
        text: {
            main: `rgb(${mainColor})`,
            secondary: `rgba(${textColor}, 0.4)`,
            alt: `rgb(${backgroundColor})`,
            link: `rgb(${mainColor})`,
            black: `rgb(${textColor})`,
            lightBlack: `rgba(${textColor}, 0.66)`,
            lightGray: `rgba(${textColor}, 0.2)`,
            lightWhite: `rgba(${textColor}, 0.4)`,
            darkBlueGray: `rgba(${mainColor}, 0.1)`
        },
        button: {
            main: `rgb(${mainColor})`,
            alt: `rgb(${backgroundColor})`,
            border: `rgba(${borderColor}, 1)`,
        },
    },
    dimensions: {
        width,
        height,
        minDimension,
        statusBarHeight: getStatusBarHeight(),
        heightWithoutStatusBar: (height - (getStatusBarHeight() * (Platform.OS === "ios" ? 4 : 1))) * (height / 844),
        absoluteHeight: height / 844,
        absoluteWidth: width / 390,
        absoluteMinDimension: minDimension / 390,
        tabHeight: tabHeight,
        maxSnapPoint: `${100 - Math.floor(tabHeight * 100 / height)}%`,
        input: {
            width: (320 * width) / 390,
            height: (58.75 * height) / 844,
        },
    },
    strongShadow: {
        elevation: 8,
        shadowColor: shadowColor,
        shadowOffset: {
            width: 5,
            height: 5,
        },
        shadowOpacity: 0.7,
        shadowRadius: 14,
    },
    shadow: {
        elevation: 8,
        shadowColor: shadowColor,
        shadowOffset: {
            width: 0,
            height: 2,
        },
        shadowOpacity: 0.2,
        shadowRadius: 4,
    },
    fonts: {
        size: {
            _8: 8 * minDimension / 390,
            _9: 9 * minDimension / 390,
            _10: 10 * minDimension / 390,
            _11: 11 * minDimension / 390,
            _12: 12 * minDimension / 390,
            _13: 13 * minDimension / 390,
            _14: 14 * minDimension / 390,
            _15: 15 * minDimension / 390,
            _16: 16 * minDimension / 390,
            _17: 17 * minDimension / 390,
            _18: 18 * minDimension / 390,
            _19: 19 * minDimension / 390,
            _20: 20 * minDimension / 390,
            _21: 21 * minDimension / 390,
            _22: 22 * minDimension / 390,
            _23: 23 * minDimension / 390,
            _24: 24 * minDimension / 390,
            _25: 25 * minDimension / 390,
            _27: 27 * minDimension / 390,
            _28: 28 * minDimension / 390,
            _29: 29 * minDimension / 390,
            _30: 30 * minDimension / 390,
            _31: 31 * minDimension / 390,
            _32: 32 * minDimension / 390,
            _33: 33 * minDimension / 390,
            _34: 34 * minDimension / 390,
            _35: 35 * minDimension / 390,
        }
    }
};

interface ThemeProviderProps {
    children: ReactNode;
}

export const ThemeProvider: React.FC<ThemeProviderProps> = ({ children }) => {
    return (
        <ThemeContext.Provider value={defaultTheme}>
            {children}
        </ThemeContext.Provider>
    );
};
