import React from 'react';
import { Image } from 'react-native';
import { styles } from '../styles/input';

const MobileIcon = () => {
  return (
        <Image 
          style={styles.icon}
          source={require('../assets/icons/mobile.png')}
        />
  );
};

export default MobileIcon;
