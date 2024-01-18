import {useState} from 'react';
import AccountTypeSelection from '@screens/AccountTypeSelection'

export default function App() {
  const [test, setTest] = useState("");
  return (
    <AccountTypeSelection/>
  );
}
