import { View } from 'react-native';
import Title, { Line } from '../components/Title';
import SubTitle from '../components/SubTitle';
import PageNumber from '../components/PageNumber';
import Button from '../components/Button';
import { getStyles } from '../styles/OnBoarding';

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
    const styles = getStyles();
    return (
        <>
            <View style={styles.titleContainer}>
                {title.map((item, i) => (
                    <Title key={i} line={item} />
                ))}
            </View>

            <View style={styles.subTitleContainer}>
                {subTitle.map((item, i) => (
                    <SubTitle key={i} text={item} />
                ))}
            </View>
            <View style={styles.imageContainer}>
                <View style={styles.roundBackground} />
                {image}
            </View>
            <View style={styles.pageSelectionContainer}>
                {pageSelect.map((item, i) => (
                    <PageNumber key={i} selected={item} />
                ))}
            </View>
            <View style={styles.buttonContainer}>
                <Button title={button.title} onPress={button.func} />
            </View>
        </>
    );
}
