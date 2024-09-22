import React, { useState } from 'react';
import { View, Text, TextInput, Button, Alert } from 'react-native';
import { confirmMobileCode } from '../utils/ApiRequest';
import { getStyles } from '../styles/MobileConfirmation';
import { PropNavigation } from '../App';
import Input from '../components/Input';

export default function MobileConfiamtion({ navigation, mobileNr }: { navigation:PropNavigation, mobileNr:string }) {
    const styles = getStyles();
    const [code, setCode] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleConfirm = async () => {
        setIsSubmitting(true);
        try {
            const result = await confirmMobileCode(mobileNr, code);
            if (result) {
                Alert.alert('Success', 'Code confirmed successfully!');
            } else {
                Alert.alert('Error', 'Invalid code. Please try again.');
            }
        } catch (error) {
            Alert.alert('Error', 'An error occurred. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <View style={styles.container} >
            <View style={styles.inputsContainer}>
                <Input
                type="text"
                onInputChange={setCode}
                />
            </View>
            <View style={styles.buttonContainer}>
                <Button  title="Confirm"  onPress={handleConfirm} />
            </View>
        </View>
    );
}
