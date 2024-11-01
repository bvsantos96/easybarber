import React from 'react';
import ScheduleNavigator from './ScheduleNavigator';
import FavoriteNav from '@navigation/FavoriteNavigator';

export default function FavoriteNavigator() {
    return (
        <ScheduleNavigator Nav={FavoriteNav} />
    );
}
