import React from 'react';
import { TouchableOpacity, View, Text } from 'react-native';
import { retrieveLocations } from '../storage/ApiLongTermStorage';

export default function ChangeLocation() {
    return (
        <View>
            <TouchableOpacity onPress={async () => { console.log(await retrieveLocations()) }}>
                <Text>List locations</Text>
            </TouchableOpacity>
        </View>
    );
}
