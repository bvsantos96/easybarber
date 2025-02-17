import { View } from 'react-native';
import { useRef, useState } from 'react';

import Divider from '@components/Divider';
import PageList, { PageListRef } from '@components/PageList';
import EstablishmentItem from '@components/EstablishmentItem';
import SlidingItem, { SlidingButton } from '@components/SlidingItem';

import { ButtonType } from "enums";
import { Routes } from '@navigation/Router';
import { getEstablishments } from 'utils/ApiRequest';

import { getStyles } from '@styles/Establishments';
import useEstablishmentStore from 'storage/stores/EstablishmentStore';

const Establishments = ({ navigation }: PropNavigation) => {
    const styles = getStyles();
    const [resetSearch, _] = useState(false);
    const pageListRef = useRef<PageListRef<EstablishmentInfo>>(null);
    const { setSelectedEstablishment } = useEstablishmentStore();

    const loadMore = async (page?: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>) => {
        return await getEstablishments(page, params);
    }

    const selectEstablishment = (establishment: EstablishmentInfo) => {
        setSelectedEstablishment(establishment);
        navigation.navigate(Routes.Home);
    }

    return (
        <View style={[styles.container]}>
            <View style={[styles.listContainer]}>
                <Divider size={10} />
                <PageList<EstablishmentInfo>
                    reset={resetSearch}
                    ref={pageListRef}
                    renderItem={({ item }: { item: EstablishmentInfo }) =>
                        <SlidingItem
                            items={
                                [
                                    <SlidingButton
                                        key={1}
                                        onPress={() => navigation.navigate(Routes.Establishment, item)}
                                        backgroundColor={styles.THEME.backgroundColor}
                                        color={styles.THEME.color}
                                        name="form-select"
                                        type={ButtonType.MaterialCommunityIcons}
                                    />,
                                    <SlidingButton
                                        key={2}
                                        onPress={() => selectEstablishment(item)}
                                        backgroundColor={styles.THEME_green.backgroundColor}
                                        color={styles.THEME.color}
                                        name="start"
                                        type={ButtonType.MaterialIcons}
                                    />
                                ]
                            }
                        >
                            <EstablishmentItem establishment={item} />
                        </SlidingItem>
                    }
                    requestFunction={loadMore} />
            </View>
        </View>
    );
}

export default Establishments;
