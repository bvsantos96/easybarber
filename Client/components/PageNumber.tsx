import TabIcon from "@assets/icons/activePage.svg";
import InactiveTabIcon from "@assets/icons/inactivePage.svg";
import { useTheme } from '../styles/ThemeContext';

export default function PageNumber({ selected = false }) {
    const theme = useTheme();
    return (
        selected ? (
            <TabIcon width={ 18 * theme.dimensions.absoluteWidth} height={ 6 * theme.dimensions.absoluteWidth} style={{marginHorizontal: 3 * theme.dimensions.absoluteWidth}} />
        ) : (
            <InactiveTabIcon width={ 18 * theme.dimensions.absoluteWidth} height={ 6 * theme.dimensions.absoluteWidth} style={{marginHorizontal: 3 * theme.dimensions.absoluteWidth}} />
        )
    );
}
