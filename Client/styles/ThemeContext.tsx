import React, { createContext, useContext, ReactNode } from 'react';
import { Dimensions } from 'react-native';
import Constants from 'expo-constants';

const width = Dimensions.get('window').width;
const height = Dimensions.get('window').height + Constants.statusBarHeight;
const minDimension = Math.min(width, height);

interface Colors {
    mainColor: string;
    backgroundColor: string;
    imageBackground: string;
    text: {
        main: string;
        secondary: string;
        alt: string;
        link: string;
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
    statusBarHeight: number;
    absoluteMinDimension: number;
    absoluteHeight: number;
    absoluteWidth: number;
    input: {
        width: number;
        height: number;
    };
}

interface Shadow {
    elevation: number;
    shadowColor: string;
    shadowOffset: {
        width: number;
        height: number;
    };
    shadowOpacity: number;
    shadowRadius: number;
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
    };
}

interface Theme {
    colors: Colors;
    dimensions: DimensionsData;
    shadow: Shadow;
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
        backgroundColor: 'white',
        imageBackground: "rgba(109, 4, 4, 0.10)",
        text: {
            main: '#263238',
            secondary: 'rgba(0, 0, 0, 0.4)',
            alt: 'white',
            link: '#DF2238',
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
        statusBarHeight: Constants.statusBarHeight,
        absoluteHeight: height / 844,
        absoluteWidth: width / 390,
        absoluteMinDimension: minDimension / 390,
        input: {
            width: (320 * width) / 390,
            height: (60 * height) / 844,
        },
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
