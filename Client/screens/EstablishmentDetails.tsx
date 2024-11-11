import React, { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { Image } from 'expo-image';
import { useQuery, useQueryClient } from '@tanstack/react-query';

import { getStyles } from '../styles/EstablishmentDetails';
import { getStyles as getListStyles } from '../styles/List';
import { getStyles as getSelectionStyles } from "../styles/Selection";

import { getEstablishmentCats, getEstablishmentDetails, getEstablishmentServices, getFavoriteIds, getImageList, isFavorite, makeRequest } from '../utils/ApiRequest';
import { gotoLocation } from '../utils/Location';
import { Underline } from '../components/Underline';
import { retrieveCategories } from '../storage/ApiLongTermStorage';
import Button from '../components/Button';
import { ImageRating } from '../components/ImageRating';
import PageList from '../components/PageList';
import { defaultBarberImage } from '../utils/Constants';
import { PageListType } from '../enums';
import { NativeStackScreenProps } from '@react-navigation/native-stack';
import { Params, Routes } from '@navigation/Router';
import useFavoriteStore from 'storage/stores/FavoriteStore';
import CategoryList from '@components/CategoryList';
import { useTheme } from '@styles/ThemeContext';

export type Route = EstablishmentInfo;

export type SetSelectedRef = {
    setSelected: (selected: boolean) => Promise<boolean>;
    selected?: boolean;
};
type Props = NativeStackScreenProps<typeof Params, 'EstablishmentDetails'> & {
    setFavorite: (favorite: boolean) => void;
};

const EstablishmentDetails = forwardRef<SetSelectedRef, Props>(
    ({ route, navigation, setFavorite }, ref) => {
        const theme = useTheme();
        const texts = require("@lang/en.json");
        const styles = getStyles();
        const selectionStyles = getSelectionStyles();
        const listStyles = getListStyles();
        const _establishment: EstablishmentInfo = route.params;
        const [establishment, setEstablishment] = useState<EstablishmentInfo>(_establishment);
        const [categories, setCategories] = useState<ICategory[]>([]);
        const { setUpdateFavorites } = useFavoriteStore();

        const { data: favoriteData, refetch } = useQuery({
            queryKey: [`/establishment/${establishment?.id}/favorite`, establishment?.id],
            queryFn: async () => await isFavorite(establishment.id),
            enabled: !!establishment?.id,
            networkMode: 'offlineFirst',
            staleTime: 60000,
        });

        const setSelected = async (selected: boolean) => {
            try {
                if (selected === establishment.favorite) return false;
                await makeRequest(`establishment/${route.params.id}/favorite`, selected ? "POST" : "DELETE");
                setUpdateFavorites(true);
                await getFavoriteIds();
                refetch();
                return true;
            } catch {
                return false;
            }
        }

        useImperativeHandle(ref, () => ({
            setSelected,
        }));

        useEffect(() => {
            if (favoriteData !== undefined) {
                setFavorite(favoriteData);
                setEstablishment({ ...establishment, favorite: favoriteData });
            }
        }, [favoriteData]);

        useQuery({
            queryKey: [`/establishment/${establishment?.id}/services/list`],
            queryFn: async () => await getEstablishmentServices(establishment.id),
            enabled: !!establishment?.id,
            networkMode: 'offlineFirst',
            staleTime: 60000,
        });

        const { data } = useQuery({
            queryKey: [`establishment/${establishment.id}/details`, establishment.id],
            queryFn: async () => getEstablishmentDetails(establishment.id),
            enabled: !!(establishment.id) && !!(establishment.load),
            networkMode: 'offlineFirst',
            staleTime: 60000
        });

        useEffect(() => {
            if (!!data) {
                const est: EstablishmentInfo = {
                    id: data.id as number,
                    name: data.name,
                    description: data.description,
                    address: data.address,
                    latitude: data.latitude,
                    longitude: data.longitude,
                    distance: 0,
                    nvotes: data.nvotes,
                    sumVotes: data.sumVotes,
                    images: data.images,
                    load: false
                };
                setEstablishment(est);
            }
        }, [data]);

        useEffect(() => {
            const fetchEstablishmentServices = async () => {
                const establishmentCats = await getEstablishmentCats(establishment.id);
                let _categories: ICategory[] = [];
                if (establishmentCats) {
                    let _cats: ICategory[] = await retrieveCategories();
                    for (let cat of _cats) {
                        if (establishmentCats.includes(cat.id)) {
                            _categories.push(cat);
                        }
                    }
                }
                setCategories(_categories);
            }

            fetchEstablishmentServices();
        }, [_establishment.id]);

        return (
            <View style={selectionStyles.container}>
                <View style={styles.imageStyle} >
                    {establishment?.images?.length > 0 &&
                        <PageList<IImage>
                            preload={false}
                            type={PageListType.PAGERVIEW}
                            initialItems={establishment.images}
                            pageSize={4}
                            renderItem={
                                ({ item, index }: { item: IImage, index: number }) => {
                                    return (
                                        <Image
                                            key={index}
                                            cachePolicy="memory-disk"
                                            source={{ uri: (item.data || defaultBarberImage) }}
                                            style={listStyles.imageStyle}
                                        />
                                    )
                                }
                            }
                            requestFunction={(page: IPage<IImage>, params?: Record<string, string | number | boolean>) => getImageList(`establishment/${establishment.id}`, page, params)} />
                    }
                    <ImageRating
                        rating={establishment.nvotes > 0 ? (establishment.sumVotes / establishment.nvotes).toFixed(1) : "0.0"}
                        nvotes={establishment?.nvotes ?? 0}
                        right
                    />
                </View>
                <TouchableOpacity style={styles.nameContainer} onPress={() => { if (establishment) gotoLocation(establishment?.name, establishment?.address, establishment?.latitude, establishment.longitude) }}>
                    <Text style={styles.name}>{establishment?.name}</Text>
                    <Text style={styles.address}>{establishment?.address}</Text>
                </TouchableOpacity>
                <View style={styles.serviceTitleContainer}>
                    <Text style={styles.serviceTitle}>{texts.services.title}</Text>
                    <Underline />
                </View>
                <View style={styles.servicesContainer}>
                    <CategoryList
                        categorySize={45}
                        categories={categories}
                        maxWidth={(theme.dimensions.width * theme.dimensions.absoluteWidth) - styles.servicesContainer.left * 2} />
                </View>
                <View style={styles.aboutTitleContainer}>
                    <Text style={styles.aboutTitle}>{texts.about.title}</Text>
                    <Underline />
                </View>
                <Text style={styles.aboutText}>{establishment?.description}</Text>
                <View style={selectionStyles.button}>
                    <Button
                        stylesInput={{ width: '100%' }}
                        onPress={
                            () => {
                                navigation.navigate(Routes.ServiceSelection, { establishmentId: establishment.id });
                            }
                        } title={texts.appointments.book} />
                </View>

            </View >
        );
    });

export default EstablishmentDetails;
