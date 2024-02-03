import { iconSize } from '../styles/Input';
import { absoluteWidth, styles as mainStyle } from '../styles/Main';

import TabIcon from "@assets/icons/activePage.svg";
import InactiveTabIcon from "@assets/icons/inactivePage.svg";

export default function PageNumber({ selected = false }) {
    return (
        selected ? (
            <TabIcon width={iconSize * absoluteWidth} height={iconSize * absoluteWidth} style={mainStyle.hMargin3} />
        ) : (
            <InactiveTabIcon width={iconSize * absoluteWidth} height={iconSize * absoluteWidth} style={mainStyle.hMargin3} />
        )
    );
}
