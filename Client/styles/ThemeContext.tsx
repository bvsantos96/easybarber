import React, { createContext, useContext, ReactNode } from 'react';
import { StatusBar, Dimensions, Platform, StyleProp, ViewStyle } from 'react-native';
import Constants from 'expo-constants';
import { StatusBarStyle } from 'expo-status-bar';

const getStatusBarHeight = (): number => {
    if (Platform.OS === 'ios') {
        return Constants.statusBarHeight || 25;
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
    successColor: string;
    infoColor: string;
    backgroundColor: string;
    iconBackground: string;
    imageBackground: string;
    statusBarOnHome: StatusBarStyle;
    borderAlt: string;
    text: {
        main: string;
        secondary: string;
        alt: string;
        link: string;
        black: string;
        lightBlack: string;
        lightGray: string;
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

const defaultTheme: Theme = {
    colors: {
        mainColor: '#DF2238',
        successColor: '#6FC138',
        infoColor: '#2A74CC',
        backgroundColor: 'white',
        iconBackground: 'rgba(223, 34, 56, 0.1)',
        imageBackground: "rgba(109, 4, 4, 0.10)",
        statusBarOnHome: "light",
        borderAlt: 'rgba(109, 4, 4, 0.1)',
        text: {
            main: '#263238',
            secondary: 'rgba(0, 0, 0, 0.4)',
            alt: 'white',
            link: '#DF2238',
            black: 'black',
            lightBlack: 'rgba(0, 0, 0, 0.66)',
            lightGray: 'rgba(0, 0, 0, 0.2)',
            darkBlueGray: '#263238'
        },
        button: {
            main: '#DF2238',
            alt: 'white',
            border: 'rgba(0, 0, 0, 0.08)',
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
        shadowColor: '#000',
        shadowOffset: {
            width: 5,
            height: 5,
        },
        shadowOpacity: 0.7,
        shadowRadius: 14,
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
