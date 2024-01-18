import {View, Image} from 'react-native';
import FirstLogin from '@components/FirstLogin';

import {styles} from '@styles/screens';
import {styles as mainStyle} from '@styles/main';

export default function Page1() {
  const texts = require("@lang/en.json");
  return (
    <View style={mainStyle.container}>
      <FirstLogin 
        title={[
          [{text: texts.firstPage.title1, highlight: false}],
          [{text: texts.firstPage.title1_1, highlight: false},{text: texts.firstPage.title1_2, highlight: true}]
        ]}
        subTitle={[
          texts.firstPage.subTitle1,
          texts.firstPage.subTitle1_1
        ]}
        image={<Image style={styles.bigImage} source={require("@assets/images/firstPage1.png")}/>}
        pageSelect={[true,false]}
        button={{title: texts.continue, func: ()=>{alert("Continue to next page")}}}
      />
    </View>
  );
}
