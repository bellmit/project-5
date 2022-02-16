import React, { useState, useEffect } from 'react';
import Validate from './Validate';
import {FormattedMessage} from 'react-intl';
// import { initP2MApplication } from '../../../actions';

const InputField = (props) => {
    const { id, value, placeHolder, onChange, onBlur, rule, index, type, item, custom, defaultValue,
        onCheckValue, onCheckDefault, onCheckRequire, status, disabled, bpId,  onCheckSameValue, inValid } = props;

    useEffect(() => {
        console.log('InputField initiate', 'bpId', bpId, 'value', value)
    }, [])
    
    const [checkDefault, setCheckDefault] = useState(false)
    const [showChangedMessage, setChanged] = useState(false)
    const [shouldChangeMessage, setShouldChangeMessage] = useState(<FormattedMessage id="inputField.nodefault"/>)
    
    const [showRequiredMessage, setShow] = useState(false)
    const [requiredMessage, setMessage] = useState(<FormattedMessage id="inputField.required"/>)
    const [requiredStyle, setStyle] = useState('')

    const handleShowRequiredMessage = (value) => {
        // if(type == 'productid' && item.noIdProvide != null){
        //     if(item.noIdProvide){
        //         setShow(false)
        //         setStyle('')
        //         // onCheckRequire(false)
        //     }else{
        //      setShow(true)
        //      setStyle('base-input-required')
        //     //  onCheckRequire(true)
        //     }
        // }else{



            if (id == 'brandNameCH'){
                setShow(false)
                setStyle('')
                type == 'variable' || type == 'multiThemeVariable' ? onCheckRequire(false) : null
            }else{
                if (value == '') {
                    setShow(true)
                    setStyle('base-input-required')
                    type == 'variable' || type == 'multiThemeVariable' ? onCheckRequire(true) : null
                } else {
                    setShow(false)
                    setStyle('')
                    type == 'variable' || type == 'multiThemeVariable' ? onCheckRequire(false) : null
                }
            }
    }

    const handleDefaultValueMessage = (value) => {
        if (value !== '' && value == defaultValue) {
            setChanged(true)
            onCheckDefault(true)
        } else {
            setChanged(false)
            onCheckDefault(false)
        }
    }
    const [variableWarning, setShowVariableWarning] = useState('')
    const [fakeValue, setFakeValue] = useState('')

    const [sameSkuWarning, setShowSameSkuWarning] = useState('')
    const [sameProdcutIdWarning, setShowSameProductIdWarning] = useState('')

    const handleOnChange = (value) => {
        if (type == 'multiThemeVariable') {
            // console.log('handleOnChange', value)
            // onChange(value, item.index, custom, index)
            if (onCheckValue(value) ) {
                // setFakeValue('')
                setShowVariableWarning('')
            } else {
                // setFakeValue(value)
                setShowVariableWarning('變體名稱不能重複')
            }
            onChange(value, item.index, custom, index)
            
        } else if (type == 'variable') {
            if (onCheckValue(value) ) {
                // setFakeValue('')
                setShowVariableWarning('')
            } else {
                // setFakeValue(value)
                setShowVariableWarning('變體名稱不能重複')
            }
            onChange(value, index, type) 
        } else if (type == 'sellersku') {
            // console.log(value, 'sku')
            // console.log(fakeValue, 'sku')
            if (onCheckSameValue(value, 'sku')) {
                // setFakeValue('')
                setShowSameSkuWarning('')
            } else {
                // setFakeValue(value)
                setShowSameSkuWarning('SKU名稱不能重複')
            }
            onChange(value, index, type)

        } else if (type == 'productid') {
            if (onCheckSameValue(value, 'pid')) {
                // setFakeValue('')
                setShowSameProductIdWarning('')
                onChange(value, index, type)
            } else {
                // setFakeValue(value)
                setShowSameProductIdWarning('Product ID不能重複')
            }
            onChange(value, index, type)

        } else {
            onChange(value,index, type)
        }
    }

    useEffect(() => {
        const check = rule.filter(item => item == 'variable')
        const result = check.length == 0 ? false : true
        setCheckDefault(result)
        // console.log('bpId of InputField', bpId)
        // const key = bpId == undefined ||bpId == '' ? `new-${id}`: retur `${bpId}-${id}`
        // console.log(key)
    },[])

    useEffect(() => {
        if (type == 'pid') {
            if (onCheckSameValue(value, 'pid')) {
                setShowSameProductIdWarning('')
            }
        } else if (type == 'sku') {
            if (onCheckSameValue(value, 'sku')) {
                setShowSameProductIdWarning('')
            }
        }
        
    }, [value])

    return (
        <div className="base-input-section">
            <input
                className={`base-input ${requiredStyle}`}
                // value={fakeValue !== '' ? fakeValue : value}
                value={value}
                // placeholder={value == '' ? placeHolder : value }
                placeholder={placeHolder}
                onChange={(e) => {handleOnChange(e.target.value)}}
                onBlur={(e) => { 
                    onBlur( e.target.value, rule, id, defaultValue)
                    handleShowRequiredMessage(e.target.value)
                    checkDefault ?  handleDefaultValueMessage(e.target.value) : null
                }}
                disabled={disabled}
            ></input>
            { disabled 
                ?   null
                :   <div style={{display: 'flex'}}>
                        { showRequiredMessage 
                            ?   <p className="base-input-notice drs-blue">
                                    <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                                    {requiredMessage}
                                </p>
                            :   null
                        }
                        { showChangedMessage
                            ?   <p className="base-input-notice drs-notice-red">
                                    <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                                    {shouldChangeMessage}
                                </p>
                            :   null
                        }
                        <Validate
                            status={status}
                            defaultValue={defaultValue}
                            value={value}
                            rule={rule}
                            key={bpId == undefined||bpId == '' ? `new-${id}`: `${bpId}-${id}`}
                        />
                        { inValid && variableWarning !== ''
                            ? <p className="base-input-notice drs-notice-red">
                                <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                { variableWarning}
                            </p>
                            :   null
                        }
                        
                        { inValid && sameProdcutIdWarning !== ''
                            ?   <p className="base-input-notice drs-notice-red">
                                    <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                    {sameProdcutIdWarning}
                                </p>
                            :   null
                        }
                        
                        {  inValid && sameSkuWarning !== ''
                            ?   <p className="base-input-notice drs-notice-red">
                                    <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                    {sameSkuWarning}
                                </p>
                            :   null
                        }
                    </div>
            }
            
        </div>
    )
}

export default InputField