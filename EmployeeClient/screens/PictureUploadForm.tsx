import React, { useState } from 'react';
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    ScrollView,
    Image,
    StyleSheet,
    Platform
} from 'react-native';
import * as ImagePicker from 'expo-image-picker';
import { Ionicons } from '@expo/vector-icons';
import { useTheme } from '@styles/ThemeContext';


const theme = useTheme();
const PictureUploadForm: React.FC = () => {
    const [name, setName] = useState<string>('');
    const [description, setDescription] = useState<string>('');
    const [address, setAddress] = useState<string>('');
    const [images, setImages] = useState<ImagePicker.ImagePickerAsset[]>([]);

    const pickImage = async () => {
        if (Platform.OS !== 'web') {
            const { status } = await ImagePicker.requestMediaLibraryPermissionsAsync();
            if (status !== 'granted') {
                alert('Sorry, we need camera roll permissions to make this work!');
                return;
            }
        }

        try {
            let result = await ImagePicker.launchImageLibraryAsync({
                mediaTypes: ImagePicker.MediaTypeOptions.Images,
                allowsMultipleSelection: true,
                quality: 0.8,
            });

            if (!result.canceled) {
                const pickedAssets = result.assets || [];
                setImages(prevImages => [...prevImages, ...pickedAssets]);
            }
        } catch (error) {
            console.error('Error picking image:', error);
        }
    };

    const removeImage = (indexToRemove: number) => {
        setImages(prevImages =>
            prevImages.filter((_, index) => index !== indexToRemove)
        );
    };

    const handleSubmit = () => {
        if (!name || !description || !address || images.length === 0) {
            alert('Please fill in all fields and select at least one image');
            return;
        }

        console.log('Form Submitted', { name, description, address, images });
        alert('Form Submitted Successfully!');
    };

    return (
        <ScrollView style={styles.container}>
            <Text style={styles.title}>Upload Pictures and Details</Text>

            <View style={styles.imageUploadContainer}>
                <TouchableOpacity
                    style={styles.imagePickerButton}
                    onPress={pickImage}
                >
                    <Ionicons name="camera" size={24} color="white" />
                    <Text style={styles.imagePickerButtonText}>
                        {images.length > 0 ? `Add More (${images.length})` : 'Pick Images'}
                    </Text>
                </TouchableOpacity>

                <ScrollView
                    horizontal
                    showsHorizontalScrollIndicator={false}
                    style={styles.imagePreviewContainer}
                >
                    {images.map((image, index) => (
                        <View key={index} style={styles.imagePreviewWrapper}>
                            <Image
                                source={{ uri: image.uri }}
                                style={styles.imagePreview}
                            />
                            <TouchableOpacity
                                style={styles.removeImageButton}
                                onPress={() => removeImage(index)}
                            >
                                <Ionicons name="close" size={16} color="white" />
                            </TouchableOpacity>
                        </View>
                    ))}
                </ScrollView>
            </View>

            <TextInput
                style={styles.input}
                placeholder="Name"
                value={name}
                onChangeText={setName}
            />

            <TextInput
                style={[styles.input, styles.multilineInput]}
                placeholder="Description"
                value={description}
                onChangeText={setDescription}
                multiline
                numberOfLines={4}
            />

            <TextInput
                style={styles.input}
                placeholder="Address"
                value={address}
                onChangeText={setAddress}
            />

            <TouchableOpacity
                style={styles.submitButton}
                onPress={handleSubmit}
            >
                <Text style={styles.submitButtonText}>Submit</Text>
            </TouchableOpacity>
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 20,
        backgroundColor: '#f5f5f5',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
        textAlign: 'center',
    },
    imageUploadContainer: {
        marginBottom: 20,
    },
    imagePickerButton: {
        backgroundColor: '#007bff',
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        padding: 15,
        borderRadius: 10,
    },
    imagePickerButtonText: {
        color: 'white',
        marginLeft: 10,
        fontWeight: 'bold',
    },
    imagePreviewContainer: {
        marginTop: 10,
    },
    imagePreviewWrapper: {
        position: 'relative',
        marginRight: 10,
    },
    imagePreview: {
        width: 100,
        height: 100,
        borderRadius: 10,
    },
    removeImageButton: {
        position: 'absolute',
        top: 5,
        right: 5,
        backgroundColor: theme.colors.text.lightBlack,
        borderRadius: 15,
        padding: 2,
    },
    input: {
        backgroundColor: 'white',
        borderWidth: 1,
        borderColor: '#ddd',
        borderRadius: 10,
        padding: 15,
        marginBottom: 15,
    },
    multilineInput: {
        height: 100,
        textAlignVertical: 'top',
    },
    submitButton: {
        backgroundColor: '#28a745',
        padding: 15,
        borderRadius: 10,
        alignItems: 'center',
    },
    submitButtonText: {
        color: 'white',
        fontWeight: 'bold',
        fontSize: 18,
    },
});

export default PictureUploadForm;
