import { useRef, useState } from 'react';
import { View } from 'react-native';

import Divider from '@components/Divider';
import EstablishmentItem from '@components/EstablishmentItem';
import PageList, { PageListRef } from '@components/PageList';
import { getEstablishments } from 'utils/ApiRequest';

import { Routes } from '@navigation/Router';
import { getStyles } from '@styles/Establishments';

const Establishments = ({ navigation }: PropNavigation) => {
    const styles = getStyles();
    const [resetSearch, _] = useState(false);
    const pageListRef = useRef<PageListRef<EstablishmentInfo>>(null);

    const loadMore = async (page?: IPage<EstablishmentInfo>, params?: Record<string, string | number | boolean>) => {
        return await getEstablishments(page, params);
    }

    return (
        <View style={[styles.container]}>
            <View style={[styles.listContainer]}>
                <Divider size={10} />
                <PageList<EstablishmentInfo>
                    reset={resetSearch}
                    ref={pageListRef}
                    renderItem={({ item }: { item: EstablishmentInfo }) =>
                        <EstablishmentItem
                            onPress={() => navigation.navigate(Routes.Establishment, { Establishment: item })}
                            establishment={item} />
                    }
                    requestFunction={loadMore} />
            </View>
        </View>
    );
}

export default Establishments;
