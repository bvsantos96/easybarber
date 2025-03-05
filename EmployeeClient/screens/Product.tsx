import { useEffect, useRef, useState } from "react";
import { View } from "react-native";
import { Image } from 'expo-image';
import FontAwesome6 from '@expo/vector-icons/FontAwesome6';
import * as ImagePicker from 'expo-image-picker';
import { Alert } from 'react-native';

import { replaceMainImage, storeImage, storeProductDetails, updateProductDetails } from "utils/ApiRequest";
import { getStyles } from "@styles/Service";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { Params } from "@navigation/Router";
import Input from "@components/Input";
import texts from "@lang/en.json";
import Button from "@components/Button";
import Pressable from "@components/Pressable";
import useUpdateStore from "storage/stores/UpdateStore";
import { AlertType } from "@components/Alert";
import useAlertStore from "storage/stores/AlertStore";
import { ServiceAction } from "enums";

export type Route = {
    product?: ProductDetails;
};

type Props = NativeStackScreenProps<typeof Params, 'Product'>;

export default function Product({ route }: Props) {
    const { toUpdate, setToUpdate } = useUpdateStore();
    const { alert, setAlertVisible } = useAlertStore();
    const { product: _product } = route.params;
    const styles = getStyles();
    const getDefaultProduct = (item?: ProductDetails): ProductDetails => {
        if (item) {
            return item;
        }
        return {
            id: 0,
            name: "",
            description: "",
            productTypeIds: [],
            images: [],
            price: 0,
            employeeId: 0,
            establishmentId: 0
        }
    }

    const getHash = (productDetails?: ProductDetails): string => {
        const _productDetails = getDefaultProduct(productDetails);
        return `1:${_productDetails.name}2:${_productDetails.description}3:${_productDetails.employeeId};4:${_productDetails.price}5:${_productDetails.productTypeIds.join(',')}6:${_productDetails.establishmentId}7:${_productDetails.images[0].data}`;
    }

    const initialItem = useRef<string>(getHash(_product));
    const [changed, setChanged] = useState<boolean>(false);
    const [product, setProduct] = useState<ProductDetails>(getDefaultProduct(_product));
    const [image, setImage] = useState<string>(product.images[0].data);

    useEffect(() => {
        //const canStore = service.name.length > 0 && service.description.length > 0 && +service.duration > 0 && service.serviceType !== undefined && service.price !== undefined && +service.price > 0;
        const canStore = true;
        setChanged(canStore && initialItem.current !== getHash(product));
    }, [product]);

    const chooseImage = async () => {
        const { status: cameraStatus } = await ImagePicker.requestCameraPermissionsAsync();
        const { status: galleryStatus } = await ImagePicker.requestMediaLibraryPermissionsAsync();

        if (cameraStatus !== 'granted' || galleryStatus !== 'granted') {
            Alert.alert(texts.permissionRequired, texts.cameraAndGalleryPermissionRequired);
            return;
        }

        Alert.alert(
            texts.chooseOption,
            texts.newPhotoOrGalery,
            [
                {
                    text: texts.takePhoto,
                    onPress: async () => {
                        const result = await ImagePicker.launchCameraAsync({
                            mediaTypes: ImagePicker.MediaTypeOptions.Images,
                            allowsEditing: true,
                            quality: 1,
                            base64: true
                        });
                        if (!result.canceled && result.assets[0].base64) {
                            setProduct({ ...product, images: [{ data: result.assets[0].uri, id: 0 }] });
                            setImage(result.assets[0].base64);
                        }
                    },
                },
                {
                    text: texts.pickFromGallery,
                    onPress: async () => {
                        const result = await ImagePicker.launchImageLibraryAsync({
                            mediaTypes: ImagePicker.MediaTypeOptions.Images,
                            allowsEditing: true,
                            quality: 1,
                            base64: true
                        });
                        if (!result.canceled && result.assets[0].base64) {
                            setProduct({ ...product, images: [{ data: result.assets[0].uri, id: 0 }] });
                            setImage(result.assets[0].base64);
                        }
                    },
                },
                { text: texts.dismiss, style: 'cancel' },
            ]
        );
    }

    const storeService = async () => {
        alert({ type: AlertType.Loading, message: "" });
        if (!product.id) {
            const response = await storeProductDetails(product);

            if (!response.success || !response.data) {
                setAlertVisible(false);
                return;
            }

            product.id = response.data.id;

            if (product.images && product.images.length > 0 && product.images[0].data !== "") {
                if (await storeImage("service", +product.id, image, true)) {
                    initialItem.current = getHash(product);
                }
            }
            initialItem.current = getHash(product);
            setChanged(false);
            setToUpdate({
                action: ServiceAction.REFRESH
            });
            setAlertVisible(false);
            return;
        }
        const _initialItem = initialItem.current.split("7:");
        const _item = getHash(product).split("7:");
        let valid = true;
        if (_initialItem[0] !== _item[0]) {
            if (!await updateProductDetails(product)) {
                valid = false;
            }
        }
        if (_initialItem[1] !== _item[1]) {
            valid &&= await replaceMainImage("service", +product.id, image);
        }

        if (valid) {
            initialItem.current = getHash(product);
            setChanged(false);
            if (toUpdate === undefined || toUpdate.action !== ServiceAction.REFRESH) {
                setToUpdate({ action: ServiceAction.UPDATE, obj: product, id: product.id });
            }
        }
        setAlertVisible(false);
    }

    return (
        <View style={styles.container}>
            <View style={styles.imageContainer} >
                {(product.images && product.images.length>0) ? (
                    <Image
                        cachePolicy="memory-disk"
                        source={{ uri: product.images[0].data }}
                        style={styles.imageStyle}
                    />
                ) : (
                    <View style={[styles.imageStyle, styles.noImage]} />
                )
                }
                <Pressable style={[styles.addIcon, styles.iconContainer]} onPress={chooseImage}>
                    {(product.images && product.images.length > 0) ? <FontAwesome6 name="edit" size={styles.icon.width} color={styles.addIcon.color} /> : <FontAwesome6 name="plus" size={styles.icon.width} color={styles.addIcon.color} />}
                </Pressable>
                <View style={styles.inputContainer}>
                    <Input hideTitleIfNoValue placeholder={texts.name} containerStyle={styles.input} title={texts.name} round={false} defaultValue={product.name} onInputChange={(name) => { setProduct({ ...product, name: name }) }} />
                    <Input hideTitleIfNoValue placeholder={texts.description} containerStyle={styles.input} title={texts.description} round={false} defaultValue={product.description} onInputChange={(description) => setProduct({ ...product, description: description })} />
                    {/*<ServiceTypeCombobox defaultValue={product.productTypeIds} onInputChange={(_serviceType: ICategory) => {
                        setProduct({ ...product, serviceType: _serviceType });
                    }} />*/}
                    <Input hideTitleIfNoValue placeholder={texts.price} containerStyle={styles.input} title={texts.price} round={false} defaultValue={`${product.price}`} onInputChange={(price) => setProduct({ ...product, price: +price })} type={"numeric"} />
                    {/*<Input hideTitleIfNoValue placeholder={texts.durationInMinutes} containerStyle={styles.input} title={texts.durationInMinutes} round={false} defaultValue={`${product.duration}`} onInputChange={(duration) => setProduct({ ...product, duration: +duration })} type={"numeric"} />*/}
                    <Button disabled={!changed} title={texts.save} onPress={storeService} />
                </View>
            </View>
        </View>
    );
}
