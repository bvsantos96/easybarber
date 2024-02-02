import { styles as inputStyle } from '../styles/Input';
import { mainColor, styles as mainStyle } from '../styles/Main';

import TabIcon from "@assets/icons/inactivePage.svg";

export default function PageNumber({ selected = false }) {
    return (
        selected ? (
            <TabIcon style={[inputStyle.icon, mainStyle.hMargin3]} fill={mainColor} />
        ) : (
            <TabIcon style={[inputStyle.icon, mainStyle.hMargin3]} fill="#2632383D" />
        )
    );
}
