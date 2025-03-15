import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { useCallback, useState } from 'react';

import SafeFullScreen from '@components/SafeFullScreen';
import TabIcon from '@components/TabIcon';
import { useSafeAreaInsets } from 'react-native-safe-area-context';

import { Params, Routes } from '@navigation/Router';
import TabsNav from '@navigation/TabsNavigator';
import { TabsVisibleConstraints } from 'enums';
import useAuthStore from 'storage/stores/AuthStore';
import useEstablishmentStore from 'storage/stores/EstablishmentStore';

import { ChangeEstablishment } from '@components/ChangeEstablishment';
import { useFocusEffect } from '@react-navigation/native';
import { useTheme } from '@styles/ThemeContext';

export default function Tabs({ navigation }: PropNavigation) {
    const Tab = createBottomTabNavigator<typeof Params>();
    const theme = useTheme();
    const inserts = useSafeAreaInsets();
    const { token } = useAuthStore();
    const { establishments, selectedEstablishment } = useEstablishmentStore();
    const [authenticated, setAuthenticated] = useState(token !== null && token !== undefined);
    const [hasEstablishments, setHasEstablishments] = useState(establishments && establishments.length > 0);
    const [hasSelectedEstablishment, setHasSelectedEstablishment] = useState(selectedEstablishment !== undefined && selectedEstablishment !== null);

    useFocusEffect(
        useCallback(() => {
            setAuthenticated(token !== null && token !== undefined);
            setHasEstablishments(establishments && establishments.length > 0);
            setHasSelectedEstablishment(selectedEstablishment !== undefined && selectedEstablishment !== null);
        }, [token, establishments, selectedEstablishment])
    );

    const tabEnabled = useCallback((tabs: TabsVisibleConstraints[] | undefined) => {
        if (!tabs || tabs.length === 0) return true;
        if (tabs.includes(TabsVisibleConstraints.AUTHENTICATED) && !authenticated) return false;
        if (tabs.includes(TabsVisibleConstraints.HAS_ESTABLISHMENTS) && !hasEstablishments) return false;
        if (tabs.includes(TabsVisibleConstraints.HAS_SELECTED_ESTABLISHMENT) && !hasSelectedEstablishment) return false;
        return true;
    }, [authenticated, hasEstablishments, hasSelectedEstablishment]);

    const forceEstablishmentSelection = useCallback((tabs: TabsVisibleConstraints[] | undefined) => {
        if (!tabs || tabs.length === 0) return false;
        return tabs.includes(TabsVisibleConstraints.FORCE_ESTABLISHMENT_SELECTION);
    }, [hasSelectedEstablishment]);

    const canExecuteHeaderButton = useCallback((tab: TabsInfo) => {
        return tabEnabled(tab.visibleConstraint) && !(forceEstablishmentSelection(tab.visibleConstraint) && !hasSelectedEstablishment);
    }, [authenticated, hasEstablishments, hasSelectedEstablishment]);

    return (
        <Tab.Navigator
            initialRouteName={Routes.Home}
            screenOptions={{
                tabBarActiveTintColor: theme.colors.mainColor,
                tabBarInactiveTintColor: theme.colors.text.main,
                tabBarStyle: [{
                    height: theme.dimensions.tabHeight + inserts.bottom,
                }, theme.shadow],
            }}>
            {TabsNav && Object.keys(TabsNav).map((key) => {
                const _key = key as keyof typeof TabsNav;
                const tab = TabsNav[_key];
                if (!tab) return null;

                return (
                    <Tab.Screen
                        key={_key}
                        name={_key}
                        navigationKey={_key}
                        options={{
                            ...tab.hasHeader ? {
                                headerTitleAlign: 'center',
                                headerShadowVisible: true,
                                headerStyle: {
                                    elevation: 8,
                                    shadowColor: '#000',
                                    shadowOffset: {
                                        width: 5,
                                        height: 5,
                                    },
                                    shadowOpacity: 0.1,
                                    shadowRadius: 5,
                                }
                            } : { headerShown: false },
                            tabBarIcon: ({ focused }) => !focused ? (
                                <tab.tabicon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} />
                            ) : (
                                <tab.tabicon width={20 * theme.dimensions.absoluteWidth} height={20 * theme.dimensions.absoluteWidth} fill={theme.colors.mainColor} />
                            ),
                            tabBarLabel: tab.title,
                            headerLeft: tab.leftIcon ? () => (
                                <TabIcon
                                    left
                                    icon={tab.leftIcon}
                                    func={() => canExecuteHeaderButton(tab) && tab.leftAction && tab.leftAction(navigation)}
                                    text={tab.leftText}
                                />
                            ) : undefined,
                            headerRight: tab.rightIcon ? () => (
                                <TabIcon
                                    icon={tab.rightIcon}
                                    func={() => canExecuteHeaderButton(tab) && tab.rightAction && tab.rightAction(navigation)}
                                    text={tab.rightText}
                                />
                            ) : undefined,
                            tabBarButton: tabEnabled(tab.visibleConstraint) ? undefined : () => null,
                        }
                        }>
                        {(props) => (
                            <SafeFullScreen
                                fixedButton={
                                    (<ChangeEstablishment autoOpen={forceEstablishmentSelection(tab.visibleConstraint)} canClose={!forceEstablishmentSelection(tab.visibleConstraint)} />)}>
                                <tab.component {...props} />
                            </SafeFullScreen>)}
                    </Tab.Screen>
                );
            })}
        </Tab.Navigator >
    );
}
