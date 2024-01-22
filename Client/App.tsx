import {ReactNode, useEffect, useState} from 'react';
import AccountTypeSelection from './screens/AccountTypeSelection'
import * as Font from 'expo-font';

export default function App() {
  const [test, setTest] = useState("");
  useEffect(() => {
    
    Font.loadAsync({
      'Mazzard': require('./assets/fonts/mazzard/MazzardM-Regular.otf'),
    });
  }, []);

  return (
    <AccountTypeSelection icon={function (): ReactNode {
      throw new Error('Function not implemented.');
    } }/>
  );
}
