import { View } from 'react-native';
import Title, { Line } from '../components/Title';
import SubTitle from '../components/SubTitle';
import PageNumber from '../components/PageNumber';
import Button from '../components/Button';
import Divider from '../components/Divider';

import { styles } from '../styles/Screens';
import { styles as mainStyle } from '../styles/Main';

type OnboardingProps = {
    title: Line[][],
    subTitle: string[],
    image: JSX.Element,
    pageSelect: boolean[],
    button: { title: string, func: () => void }
}

export default function Onboarding({
    title = [],
    subTitle = [],
    image = <></>,
    pageSelect = [],
    button = {
        title: "No title", func: () => { alert("No function") }
    }
}: OnboardingProps) {
    return (
        <>
            <View>
                <Divider height={60} />
                {title.map((item, i) => (
                    <Title key={i} line={item} />
                ))}
                <Divider height={20} />
                {subTitle.map((item, i) => (
                    <SubTitle key={i} text={item} />
                ))}
            </View>
            <View style={styles.imageContainer}>
                <View style={styles.roundBackground} />
                {image}
            </View>
            <View style={mainStyle.w100c}>
                <View style={styles.pageSelectionContainer}>
                    {pageSelect.map((item, i) => (
                        <PageNumber key={i} selected={item} />
                    ))}
                </View>
                <Button title={button.title} onPress={button.func} />
                <Divider height={50} />
            </View>
        </>
    );
}
