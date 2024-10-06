import React, { useEffect, useRef } from 'react';
import { View, Text, Animated } from 'react-native';
import { ALERT_TYPE, Dialog, Toast } from 'react-native-alert-notification';
import { getStyles } from '../styles/Alert';
import Button from './Button';
import Success from '@assets/images/success.svg';

export enum AlertType {
    Success = 'success'
}

export interface AlertProps {
    message: string;
    onPress?: () => void;
    buttonText?: string;
    type?:AlertType;
}

interface Props extends AlertProps{
    visible: boolean;
    setVisible: Function,
    children: React.ReactNode
}

export const CustomAlert: React.FC<Props> = ({
    children,
    visible,
    setVisible,
    message,
    onPress,
    buttonText,
    type
  }) => {
    const fadeAnim = useRef(new Animated.Value(0)).current;
    const scaleAnim = useRef(new Animated.Value(0.8)).current;
  
    useEffect(() => {
      if (visible) {
        Animated.parallel([
          Animated.timing(fadeAnim, {
            toValue: 1,
            duration: 300,
            useNativeDriver: true,
          }),
          Animated.spring(scaleAnim, {
            toValue: 1,
            friction: 8,
            tension: 40,
            useNativeDriver: true,
          }),
        ]).start();
      } else {
        Animated.parallel([
          Animated.timing(fadeAnim, {
            toValue: 0,
            duration: 200,
            useNativeDriver: true,
          }),
          Animated.spring(scaleAnim, {
            toValue: 0.8,
            friction: 8,
            tension: 40,
            useNativeDriver: true,
          }),
        ]).start();
      }
    }, [visible, fadeAnim, scaleAnim]);
  
    const handleClose = () => {
      setVisible(false);
      if (onPress) onPress();
    };
  
    const styles = getStyles();
  
    return (
      < >
        {children}
        {visible && (
          <Animated.View 
            style={[
              styles.modalOverlay,
              {
                opacity: fadeAnim,
              },
            ]}
          >
            <Animated.View 
            style={[
                styles.alertBox,
                {
                  transform: [{ scale: scaleAnim }],
                },
              ]}
            >
                <Success style={styles.image}/>
                <View style={styles.messageContainer}>
                    <Text style={styles.message}>{message}</Text>
                </View>
                <Button
                    title={buttonText}
                    onPress={handleClose}
                    stylesInput={styles.button}
                />
            </Animated.View>
          </Animated.View>
        )}
      </>
    );
  };

export const Banner = ({ type, title, message, onPress }: { type: ALERT_TYPE, title?: string, message: string, onPress?: () => void }) =>
    Toast.show({
        type: type,
        title: title || type,
        textBody: message,
        onPress: onPress || Toast.hide,
    });

export default CustomAlert;