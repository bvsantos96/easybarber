import React, { createContext, useContext, ReactNode } from 'react';
import { Dimensions } from 'react-native';
import Constants from 'expo-constants';

const width = Dimensions.get('window').width;
const height = Dimensions.get('window').height;

interface Colors {
  mainColor: string;
  backgroundColor: string;
  text: {
    main: string;
    secondary: string;
    alt: string;
    link: string;
  };
  button: {
    main: string;
    alt: string;
  };
}

interface DimensionsData {
  width: number;
  height: number;
  statusBarHeight: number;
  absoluteHeight: number;
  absoluteWidth: number;
  minDimension: number;
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

interface Theme {
  colors: Colors;
  dimensions: DimensionsData;
  shadow: Shadow;
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
    text: {
      main: '#263238',
      secondary: 'rgba(0, 0, 0, 0.4)',
      alt: 'white',
      link: '#DF2238',
    },
    button: {
      main: '#DF2238',
      alt: 'white',
    },
  },
  dimensions: {
    width,
    height,
    statusBarHeight: Constants.statusBarHeight,
    absoluteHeight: height / 844,
    absoluteWidth: width / 390,
    minDimension: Math.min(width, height),
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
