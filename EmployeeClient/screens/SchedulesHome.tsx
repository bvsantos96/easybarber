import Header from "@components/Header";
import WeekViewIcon from "@components/icons/WeekViewIcon";
import { Params } from "@navigation/Router";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import texts from '@lang/en.json';
import TimeSheetIcon from "@components/icons/TimeSheetIcon";
import { TabIconNoPadding } from "@components/TabIcon";
import { useEffect, useState } from "react";
import SchedulesIcon from "@components/icons/SchedulesIcon";
import TimeSheet from "./TimeSheet";
import WeekView from "./WeekView";
import SafeFullScreen from "@components/SafeFullScreen";
import React from "react";
import Schedules from "./Schedules";

export type Route = {
    establishmentId?: number;
};

export type Props = NativeStackScreenProps<typeof Params, 'Schedules'>;

export default function SchedulesHome({ route, navigation }: Props) {
    let establishmentId: number | undefined = undefined;
    if (route.params) {
        const { establishmentId: _establishmentId } = route.params;
        establishmentId = _establishmentId;
    }
    const states: { title: string; component: React.FC<Props>; leftIcon: React.FC<any>; left: number; rightIcon: React.FC<any>; right: number }[] = [
        {
            title: texts.navigation.tabs.schedules.name,
            component: Schedules,
            leftIcon: TimeSheetIcon,
            left: 1,
            rightIcon: WeekViewIcon,
            right: 2
        },
        {
            title: texts.navigation.tabs.schedules.timesheet,
            component: TimeSheet,
            leftIcon: SchedulesIcon,
            left: 0,
            rightIcon: WeekViewIcon,
            right: 2
        },
        {
            title: texts.navigation.tabs.schedules.weekview,
            component: WeekView,
            leftIcon: SchedulesIcon,
            left: 0,
            rightIcon: TimeSheetIcon,
            right: 1
        }
    ];
    const [selectedIndex, setSelectedIndex] = useState(0);
    const SelectedComponent = states[selectedIndex].component;

    useEffect(() => {
        console.log("ScheduleHome useEffect[establishmentId]: ", establishmentId);
    }, [establishmentId]);

    return (
        <>
            <Header
                hasGoBack={false}
                title={states[selectedIndex].title}
                navigation={navigation}
                firstHeader={() =>
                    <TabIconNoPadding
                        icon={states[selectedIndex].leftIcon}
                        func={() => setSelectedIndex(states[selectedIndex].left)}
                        text={states[states[selectedIndex].left].title}
                    />
                }
                secondHeader={() =>
                    <TabIconNoPadding
                        icon={states[selectedIndex].rightIcon}
                        func={() => setSelectedIndex(states[selectedIndex].right)}
                        text={states[states[selectedIndex].right].title}
                    />
                }
            />
            <SafeFullScreen>
                <SelectedComponent route={route} navigation={navigation} />
            </SafeFullScreen>
        </>
    );
}
