import React from 'react';
import ScheduleNavigator from './ScheduleNavigator';
import AppointmentNav from '@navigation/AppointmentNavigator';

export default function AppointmentNavigator() {
    return (
        <ScheduleNavigator Nav={AppointmentNav} />
    );
}
