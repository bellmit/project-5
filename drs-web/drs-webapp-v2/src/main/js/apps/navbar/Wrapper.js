import React, {useEffect, useState} from 'react';
import {IntlProvider} from 'react-intl';
import TW from './lang/zh-tw.json';
import EN from './lang/english.json';
import axios from 'axios';
import {DOMAIN_NAME} from './constants/index'

export const Context = React.createContext();

//const local = navigator.language;
// const local = 'zh-TW'

// let lang;
// if (local === 'en-US') {
//     // console.log(local)
//     lang = EN;
// }else {
//     // console.log(local)
//     if (local === 'zh-TW') {
//         lang = TW;
//         // console.log(lang)
//     }
// }
const Wrapper = (props) => {
    const [locale, setLocale] = useState('zh-TW');
    const [messages, setMessages] = useState(TW);

    useEffect(()=>{
        axios.post(DOMAIN_NAME + '/c/u',{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
          }).then(res => {
            if (res != null) {
                console.log(res.data)
                if (res.data.locale == 'zh_TW') {
                    setLocale('zh-TW')
                    setMessages(TW)
                } else if (res.data.locale == 'en_US') {
                    setMessages(EN)
                    setLocale('en-US')
                }
            }
          })
    }, [])
    
    function selectLanguage(e) {
        console.log(e.target.value)
        // const newLocale = e.target.value;
        // setLocale(newLocale);
        // if (newLocale === 'en-US') {
        //     setMessages(EN);
        // } else {
        //     if (newLocale === 'zh-TW') {
        //         setMessages(TW);
        //     }
        // }
    }

    return (
        <Context.Provider value = {{locale, selectLanguage}}>
            <IntlProvider messages={messages} locale={locale}>
                {props.children}
            </IntlProvider>
        </Context.Provider>

    );
}


export default Wrapper;