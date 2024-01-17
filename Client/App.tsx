import {useState} from 'react';
import { View } from 'react-native';
import Button from '@components/Button';
import Input from '@components/Input';
import MobileIcon from '@icons/Mobile';

import {styles} from '@styles/main';

export default function App() {
  const [test, setTest] = useState("");
  return (
    <View style={styles.container}>
      <Input icon={()=><MobileIcon/>} type="password" placeholder="Password" onInputChange={setTest}/>
      <Button title="Test" onPress={()=>alert("test")}/>
    </View>
  );
}
