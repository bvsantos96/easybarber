import React, { useEffect, useState } from 'react';
import { TouchableOpacity, View, Text } from 'react-native';
import { retrieveLocations } from '../storage/ApiLongTermStorage';
import { ILocation } from '../declarations';

export default function ChangeLocation() {
    const [locations, setLocations] = useState<ILocation[]>();
    useEffect(() => {
        retrieveLocations().then((_locations) => {
            setLocations(_locations);
        });
    }, []);
    return (
        <View>
            <TouchableOpacity onPress={async () => { console.log(await retrieveLocations()) }}>
                {locations?.map((location, idx) => {
                    return (
                        <View key={idx}>
                            <Text>{location.address}</Text>
                        </View>
                    );
                })}
            </TouchableOpacity>
        </View>
    );
}
