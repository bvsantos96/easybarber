import React, { ReactNode } from 'react';
import { Pressable as DefaultPressable } from 'react-native';

interface MyPressableProps {
  style?: any; // You can replace 'any' with a more specific type if needed
  onPress: () => void;
  children: ReactNode | ReactNode[];
}

export default function Pressable({style = {}, onPress = ()=> {} , children = <></>}: MyPressableProps ) {
    return (
        <DefaultPressable style={({pressed})=>[style, {opacity: pressed ? 0.5 : 1}]} onPress={onPress}>
            {Array.isArray(children) ? children : [children]}
        </DefaultPressable>
    );
}

