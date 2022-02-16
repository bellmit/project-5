import React, { useState, useEffect } from 'react';
import {FormattedMessage} from 'react-intl';
import axios from 'axios';
import { DOMAIN_NAME } from '../../../../navbar/constants';


const Validate = (props) => {
    const { defaultValue, value, rule, status} = props;
    
    useEffect(()=> {
        console.log('Validate initiate', 'value', value)
    }, [])
    const [eanIsValid,setEanIsValid] = useState(true);
    const [skuIsValid,setSkuIsValid] = useState(true);
    const [editDefaultValue,setEditDefaultValue] = useState(value);

    let contentArr = [];

    // if (defaultValue !== '') {
    //     console.log(defaultValue, 'defaultValue')
    // }

    const verifyProductId = (value) => {
        if (value !== '' && value !== editDefaultValue) {
            axios.post(DOMAIN_NAME + '/product/mdbp/vec',{
                pi: value
            },{ 
                headers: { 
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                }
            }).then(res => {
                if (res !== null) {
                    // const eanIsValid = res.data
                    setEanIsValid(res.data)
                    
                }
            }).catch(err => console.log(err))
        } 
    }

    const verifySku = (value) => {
        console.log(value, editDefaultValue)
        if (value !== '' && value !== editDefaultValue) {
            axios.post(DOMAIN_NAME + '/product/mdbp/vsc',{
                sku:value
            },{ 
                headers: { 
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                }
            }).then(res => {
                console.log('res verify sku', res.data)
                if (res !== null) {
                    setSkuIsValid(res.data)
                    // const skuIsValid = res.data
                   
                }
            }).catch(err => console.log(err))
        } 
    }

    // useEffect(() => {
    //     if (status == 'addsku' || status == 'updateProduct') {
            // if (editDefaultValue == '') {
            //     value !== null && value !== '' ?  setEditDefaultValue(value) : setEditDefaultValue(null)
            // }
    //     }
    // }, [])

    
    if (rule && rule.length !== 0) {
        contentArr = rule.map(item => {
            switch(item) {
                // case 'variable':
                //     return value == defaultValue ? '請輸入非預設之值' : null
                case 'sellersku':
                    var rex = /^[A-Z0-9-]+$/i;
                    return value !== '' && !rex.test(value) ? 'validate.usehyphen' : null
                case 'chinese':
                    var rex = /[^\u4e00-\u9fa5]/;
                    return value !== '' && rex.test(value) ?  'validate.traditionalchinese': null
                case 'english':
                    var rex = /^[A-Z0-9-./%,_ ]+$/i;
                    return value !== '' && !rex.test(value) ? 'validate.englishornumbers' : null
                case 'variationname':
                    var rex = /^[A-Z0-9-]+$/i;
                    return value !== '' && !rex.test(value) ? 'validate.variationname' : null
                case 'integer':
                    var rex = /^[0-9-]+$/;
                    return value !== '' && !rex.test(value) ? 'validate.numbers' : null
                case 'maxLen50':
                    return value.length < 51 ? null : 'validate.50characters'
                case 'maxLen20':
                    return value.length < 21 ? null : 'validate.20characters'
                // case 'len12':
                //     return value.length < 13 && value.length > 11 ? null : '長度應為 12 位'
                // case 'len13':
                //     return value.length < 14 && value.length > 12 ? null : '長度應為 13 位'
                case 'maxLen10':
                    return value.length < 11 ? null : 'validate.10characters'
                case 'maxLen15':
                    return value.length < 16 ? null : 'validate.15characters'
                case 'productId':
                    verifyProductId(value)
                    return eanIsValid ? null : 'validate.productidused' 
                case 'uniqueSku':
                    verifySku(value)
                    return skuIsValid ? null : 'validate.productskuused'
                default:
                    return ''
            }
        })
    }
    // console.log(contentArr, 'contentArr')
    
    // to see if it was not an empty string
    const str = contentArr.toString();
    let notice = '';
    // delete ','
    for(let i = 0; i < str.length; i ++) {
        if (str.charAt(i)!==',') {
            notice = notice + str.charAt(i);
        }
    }

    return notice == ''
        ?   <p className="base-input-notice" style={{paddingTop: '13px'}}></p>
        :   <p className="base-input-notice drs-notice-red">
                <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                {/* {notice} */}
                { contentArr.map(item => {
                    if(item) {
                        return ( <FormattedMessage id={item}/>)
                    }}
                )}
                
            </p>
}

export default Validate
