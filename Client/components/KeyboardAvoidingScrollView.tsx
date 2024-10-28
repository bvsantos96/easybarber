import { Animated, Easing, Keyboard, Platform, ScrollView, TextInput, View } from 'react-native';
import { getStyles } from '../styles/KeyboardAvoidingScrollView';
import { ReactElement, ReactNode, RefObject, Children, cloneElement, createRef, isValidElement, useEffect, useMemo, useState, useRef } from 'react';
import React from 'react';
import PhoneInput from './PhoneInput';
import Input from './Input';
import { useTheme } from '@styles/ThemeContext';

export default function KeyboardAvoidingScrollView({ fixedTopComponent, children, fixedBottomComponent, maxHeight, keyboardShow, keyboardHide }: { fixedTopComponent?: ReactNode, children: ReactNode, fixedBottomComponent?: ReactNode, maxHeight: number, keyboardShow?: () => void, keyboardHide?: () => void }) {
    const theme = useTheme();
    if (maxHeight !== undefined) {
        maxHeight = maxHeight * theme.dimensions.absoluteHeight;
    }
    const styles = getStyles();
    const scrollViewRef = useRef<ScrollView>(null);
    const [textInputs, setTextInputs] = useState<RefObject<any>[]>([]);
    const [keyboardHeight, setKeyboardHeight] = useState<number>(0);
    const [topHeight, setTopHeight] = useState(0);
    const [bottomHeight, setBottomHeight] = useState(0);
    const [selectedInput, setSelectedInput] = useState<number | null>(null);
    const animatedMaxHeight = useRef(new Animated.Value(maxHeight)).current;

    const handleTopLayout = (event: any) => {
        if (event.nativeEvent.layout.height > 0)
            setTopHeight(event.nativeEvent.layout.height);
    }

    const handleFixedBottom = (event: any) => {
        if (event.nativeEvent.layout.height > 0)
            setBottomHeight(event.nativeEvent.layout.height);
    }

    useEffect(() => {
        const _onKeyboardShow = (e: any) => {
            const height: number = e.endCoordinates.height;
            if (maxHeight !== undefined) {
                const newMaxHeight = maxHeight + Math.min(
                    theme.dimensions.heightWithoutStatusBar - topHeight - maxHeight - height,
                    0
                );

                Animated.timing(animatedMaxHeight, {
                    toValue: newMaxHeight,
                    duration: 300,
                    easing: Easing.out(Easing.ease),
                    useNativeDriver: false,
                }).start();
            }
            setKeyboardHeight(height);
        }

        const _onKeyboardHide = () => {
            if (maxHeight !== undefined) {
                const newMaxHeight = maxHeight + Math.min(
                    theme.dimensions.heightWithoutStatusBar - topHeight - maxHeight,
                    0
                );

                Animated.timing(animatedMaxHeight, {
                    toValue: newMaxHeight,
                    duration: 300,
                    easing: Easing.out(Easing.ease),
                    useNativeDriver: false,
                }).start();
            }
            setSelectedInput(null);
            setKeyboardHeight(0);
            keyboardHide && keyboardHide()
        };


        if (Platform.OS === "android") {
            const changeFrame = Keyboard.addListener('keyboardDidChangeFrame', _onKeyboardShow);
            const hideListener = Keyboard.addListener('keyboardDidHide', _onKeyboardHide);
            return () => {
                changeFrame.remove();
                hideListener.remove();
            };
        } else {
            const changeFrame = Keyboard.addListener('keyboardWillChangeFrame', _onKeyboardShow);
            const hideListener = Keyboard.addListener('keyboardWillHide', _onKeyboardHide);
            return () => {
                changeFrame.remove();
                hideListener.remove();
            };
        }

    }, [topHeight]);

    const cloneWithRefsRecursive = (element: ReactNode, refsArray: RefObject<TextInput>[]): ReactNode => {
        if (!isValidElement(element)) {
            return element;
        }

        if (element.type === TextInput || element.type === Input || element.type === PhoneInput) {
            const inputRef: RefObject<any> = createRef();
            refsArray.push(inputRef);
            setTextInputs(refsArray);
            const idx = refsArray.length - 1;
            return cloneElement(element as ReactElement, {
                ref: inputRef,
                onFocus: () => {
                    keyboardShow && keyboardShow();
                    setSelectedInput(idx);
                },
                onBlur: () => {
                    keyboardHide && keyboardHide();
                }
            });
        }

        const clonedChildren = Children.map(element.props.children, (child) =>
            cloneWithRefsRecursive(child, refsArray)
        );

        return cloneElement(element as ReactElement, { children: clonedChildren });
    };

    const clonedChildrenWithRefs = useMemo(() => {
        const refsArray: RefObject<TextInput>[] = [];

        const clonedChildren = Children.map(children, (child) =>
            cloneWithRefsRecursive(child, refsArray)
        );

        return clonedChildren;
    }, [children]);

    useEffect(() => {
        if (selectedInput !== null && keyboardHeight > 0) {
            scrollToInput(textInputs[selectedInput]);
        }
    }, [keyboardHeight, selectedInput]);

    const scrollToInput = (inputRef: RefObject<TextInput>) => {
        if (inputRef?.current && scrollViewRef?.current && keyboardHeight > 0) {
            inputRef.current.measure((_x, y) => {
                scrollViewRef.current?.scrollTo({
                    x: 0,
                    y: y,
                    animated: true,
                });
            });
        } else {
            setTimeout(() => {
                scrollToInput(inputRef);
            }, 100);
        }
    };

    return (
        <View style={styles.avoidingKeyboard} >
            <View onLayout={handleTopLayout}>
                {fixedTopComponent && fixedTopComponent}
            </View>
            <Animated.ScrollView
                ref={scrollViewRef}
                scrollEnabled={keyboardHeight > 0}
                showsHorizontalScrollIndicator={false}
                showsVerticalScrollIndicator={false}
                style={[
                    styles.scrollContainer,
                    maxHeight ? {
                        maxHeight: animatedMaxHeight,
                    } : {},
                ]}>
                {clonedChildrenWithRefs}
                {keyboardHeight > 0 && <View style={Platform.OS === "ios" ? { paddingBottom: bottomHeight } : { height: bottomHeight }} />}
            </Animated.ScrollView>
            {keyboardHeight <= 0 && fixedBottomComponent && <View onLayout={handleFixedBottom}>{fixedBottomComponent}</View>}
            {keyboardHeight > 0 && fixedBottomComponent && <View style={[styles.fixBottom]}>{fixedBottomComponent}</View>}
        </View>
    );
}
