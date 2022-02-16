import React, {useEffect, useState} from 'react';
import Select from 'react-select';
import {FormattedMessage} from 'react-intl';

const customStyles = {
    control: (provided, state) => ({
        ...provided,
        background: '#fff',
        borderColor: '#d7dbec',
        minHeight: '37px',
        height: '37px',
        boxShadow: state.isFocused ? null : null,
    }),

    valueContainer: (provided, state) => ({
        ...provided,
        height: '37px',
        padding: '0 6px'
    }),

    input: (provided, state) => ({
        ...provided,
        margin: '0px',
        padding: '0px',
    }),
    indicatorSeparator: state => ({
        display: 'none',
    }),
    indicatorsContainer: (provided, state) => ({
        ...provided,
        height: '100%',
    }),
};

const SelectField = (props) => {
    const { className, value, onChange, index, placeHolder, options, isDisabled} = props;
    const [showRequiredMessage, setShow] = useState(false)
    const [requiredMessage, setMessage] = useState(<FormattedMessage id="selectField.required"/>)

    useEffect(() => {
        handleShowRequiredMessage(value)
    })
    const handleShowRequiredMessage = (value) => {
        if (value == 'Select...' || value == '') {
            setShow(true)
        } else {
            setShow(false)
        }
    }
    return (
        <div className={className}>
            <Select
                styles={customStyles}
                value={value}
                onChange={(e)=> {onChange(e, index)}}
                placeholder={placeHolder ? placeHolder: value}
                options={options}
                classNamePrefix='mySelector'
                isDisabled={isDisabled}
            />
            { showRequiredMessage && !isDisabled
                ?   <p className="base-input-notice drs-blue">
                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                        {requiredMessage}
                    </p>
                :   null 
            }

        </div>
        
    )
}

export default SelectField