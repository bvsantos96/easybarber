import { FlatList, View, Text } from "react-native";
import { Routes } from "@navigation/Router";
import { useEffect, useState } from "react";

import Selection from "@screens/Selection";
import Divider from "@components/Divider";
import SelectionItem from "@components/SelectionItems";

import { getStyles } from "@styles/ServiceSelection";

import texts from "@lang/en.json";
import useEstablishmentStore from "storage/stores/EstablishmentStore";

export default function EstablishmentSelection({ navigation }: PropNavigation) {
    const styles = getStyles();
    const [topPadding, setTopPadding] = useState(0);
    const { selectedEstablishment, establishments } = useEstablishmentStore();
    const [establishmentId, setEstablishmentId] = useState<number | undefined>(selectedEstablishment ? +selectedEstablishment.id : undefined);

    useEffect(() => {
        if (establishments.length === 1) {
            navigation.navigate(Routes.EmployeeSelection, { establishmentId: selectedEstablishment?.id });
        }
    }, [establishments]);

    return (
        <Selection
            setTopPadding={setTopPadding}
            buttonText={texts.appointments.selectEmployee}
            selectionText={texts.appointments.selectEstablishment}
            selected={!!establishmentId}
            onButtonPress={
                async () => {
                    if (!!establishmentId) {
                        navigation.navigate(Routes.EmployeeSelection, { establishmentId: establishmentId });
                        return;
                    }
                }
            }>
            <View style={styles.listContainer}>
                <Divider size={topPadding} horizontal={false} />
                {establishments && establishments.length > 0 && (
                    <FlatList
                        data={establishments || []}
                        renderItem={
                            ({ item }: { item: EstablishmentBase }) =>
                                <SelectionItem
                                    key={item.id}
                                    image={item.image}
                                    selected={item.id == establishmentId}
                                    onPress={() => {
                                        setEstablishmentId(+item.id);
                                    }}>
                                    <View style={styles.titleContainer}>
                                        <View style={{
                                            justifyContent: 'center',
                                            height: styles.titleContainer.height
                                        }}>
                                            <Text style={styles.singleTitle}>{item.name}</Text>
                                            <View style={{ flexDirection: 'row' }}>
                                                {item.admin && (
                                                    <Text style={[styles.description]}>{texts.isAdmin}</Text>
                                                )}
                                            </View>
                                        </View>
                                    </View>
                                </SelectionItem>
                        }
                        keyExtractor={(item) => item.id.toString()}
                        showsVerticalScrollIndicator={false}
                        showsHorizontalScrollIndicator={false}
                        contentContainerStyle={styles.listContentContainer}
                    />
                )}
            </View>
        </Selection >
    );
}
