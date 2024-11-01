import React from 'react';
import HomeNav from '@navigation/HomeNavigator';
import ScheduleNavigator from './ScheduleNavigator';

export default function HomeNavigator() {
    return (
        <ScheduleNavigator Nav={HomeNav} />
    );
}
