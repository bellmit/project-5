import React, { Component } from 'react';
import Variation from '../common/Variation';
import Permutations from '../common/Permutations';
import { connect } from 'react-redux';
import { updateCheckValid, onBrandNameChChange, onBrandNameEnChange, onProductNameChChange, onProductNameEnChange, onManufacturerChChange, onManufacturerEnChange } from '../../../actions/index'; 
import InputField from '../common/InputField';
import SelectField from '../common/SelectField';
import {FormattedMessage} from 'react-intl';
import { Tooltip } from '@material-ui/core';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import image1 from '../../../assets/images/image1.png';
import Modal from '@material-ui/core/Modal';
// import axios from 'axios';
// import { DOMAIN_NAME } from '../../../../navbar/constants';

const productnamenote =<FormattedMessage id="basedata.productnamenote"/>
const manufacturernote =<FormattedMessage id="basedata.manufacturernote"/>

const mapStateToProps = (state) => {
    return {
        pd: state.PD
    }
}

const mapDispatchToProps = (dispatch) => {
    return { 
        onUpdateCheckValid: (arr) => dispatch(updateCheckValid(arr)),
        onPDNameChChangeHandler: (value) => dispatch(onProductNameChChange(value)),
        onPDNameEnChangeHandler: (value) => dispatch(onProductNameEnChange(value)),
        onBDNameChChangeHandler: (value) => dispatch(onBrandNameChChange(value)),
        onBDNameEnChangeHandler: (value) => dispatch(onBrandNameEnChange(value)),
        onManuChChangeHandler: (value) => dispatch(onManufacturerChChange(value)),
        onManuEnChangeHandler: (value) => dispatch(onManufacturerEnChange(value))
    }
}

class BaseData extends Component {
    constructor(props) {
        super()
        this.state = {
            variationNote: false
        }
    }

    onBrandNameChChange = (value) => {
        console.log('onBrandNameChChange')
        this.props.onBDNameChChangeHandler(value)
    }
    onBrandNameEnChange = (value) => {
        this.props.onBDNameEnChangeHandler(value)
        this.props.showNoticeChange()
    }
    onProductNameChChange = (value) => {
        this.props.onPDNameChChangeHandler(value)
        this.props.showNoticeChange()
    }
    onProductNameEnChange = (value) => {
        this.props.onPDNameEnChangeHandler(value)
        this.props.showNoticeChange()
    }
    onManufacturerChChange = (value) => {
        this.props.onManuChChangeHandler(value)
        this.props.showNoticeChange()
    }
    onManufacturerEnChange = (value) => {
        this.props.onManuEnChangeHandler(value)
        this.props.showNoticeChange()
    }

    onBlurCheckValid = (value, rule, id, defaultValue) => {  
        // console.log('onBlurCheckValid', value, rule, id, defaultValue)
        const checkArr = rule.map(item => {
            switch(item) {
                case 'variable':
                    return value == defaultValue ? false: true
                case 'sellersku':
                    var rex = /^[A-Z0-9-]+$/i;
                    return value !== '' && !rex.test(value) ? false : true
                case 'chinese':
                    var rex = /[^\u4e00-\u9fa5]/;
                    return value !== '' && rex.test(value) ? false : true
                case 'english':
                    var rex = /^[A-Z0-9-./%,_ ]+$/i;
                    return value !== '' && !rex.test(value) ? false : true
                case 'integer':
                    var rex = /^[0-9- ]+$/;
                    return value !== '' && !rex.test(value) ? false : true
                case 'maxLen50':
                    return value.length < 51 ? true : false
                case 'maxLen20':
                    return value.length < 21 ? true : false
                case 'maxLen10':
                    return value.length < 11 ? true : false
                case 'maxLen15':
                    return value.length < 16 ? true : false
                default:
                    return true
            }
        })
        const inValid = checkArr.includes(false);

        // HANDLE CHECKVALID BEFORE CONFIRM CREATE or UPDATE
        this.handleUpdateCheckValid(id, inValid)
    
        // const arr = this.props.pd.checkValid
        // if (inValid) {
        //     const obj = {key: key, valid: false}
        //     const target = this.props.pd.checkValid.filter(item => item.key == key)

        //     if (target.length == 0) {
        //         arr.push(obj)
        //     } else {
        //         const index = this.props.pd.checkValid.indexOf(target[0])
        //         arr[index] = obj;
        //     }
        // } else {
        //     const obj = {key: key, valid: true}
        //     const target = this.props.pd.checkValid.filter(item => item.key == key)

        //     if (target.length == 0) {
        //         arr.push(obj)
        //     } else {
        //         const index = this.props.pd.checkValid.indexOf(target[0])
        //         arr[index] = obj;
        //     }
        // }
        
        // console.log(arr, 'on blur check')
        // this.props.onUpdateCheckValid(arr)
    }

    handleUpdateCheckValid = (id, inValid) => {
        // console.log(id, inValid)
        const arr = this.props.pd.checkValid;
        const target = this.props.pd.checkValid.filter(item => item.key == id)
        let obj = {}

        if (inValid) {
            obj = {key: id, valid: false}
            if (target.length == 0) {
                arr.push(obj)
            } else {
                const index = this.props.pd.checkValid.indexOf(target[0])
                arr[index] = obj;
            }
        } else {
            obj = {key: id, valid: true}
            if (target.length == 0) {
                arr.push(obj)
            } else {
                const index = this.props.pd.checkValid.indexOf(target[0])
                arr[index] = obj;
            }
        }
        console.log(arr, 'onCheckValid')
        this.props.onUpdateCheckValid(arr)
    }

    handleCheckDefault = (boolean) => {
        this.handleUpdateCheckValid('variationNameNotDefault', boolean)
    }
    handleCheckRequire = (boolean) => {
        this.handleUpdateCheckValid('variationNameReqiured', boolean)
    }

    handleCheckSingleThemeVV = (value) => {
        const checkResult = this.props.pd.inputs.map(item => {
            if (item.variable == value) {
                return false
            }
            return true
        })
        const inValid = checkResult.includes(false)

        // HANDLE CHECKVALID BEFORE CONFIRM CREATE or UPDATE
        this.handleUpdateCheckValid('variationNameNotDuplicate', inValid)

        return !inValid
    }

    handleCheckMultiThemeVV = (value) => {
        let checkResult = this.props.pd.multiTheme[0].variables.map(item => {
            if (item.value == value) {
                return false 
            }
            return true
        })
        this.props.pd.multiTheme[1].variables.map(item => {
            if (item.value == value) {
                return checkResult.push(false)  
            }
            return checkResult.push(true)
        })
        const inValid = checkResult.includes(false)

        // HANDLE CHECKVALID BEFORE CONFIRM CREATE or UPDATE
        this.handleUpdateCheckValid('variationNameNotDuplicate', inValid)
        
        return !inValid
    }

    handleCheckSameValue = (value, id) => {
        if (id == 'sku') {
            let checkResult = this.props.pd.inputs.map(item => {
                if (item.sellerSku == value) {
                    return false
                }
                return true
            })
            const inValid = checkResult.includes(false)
            this.handleUpdateCheckValid('thesameSku', inValid)
            return !inValid
        } else if (id == 'pid') {
            let checkResult = this.props.pd.inputs.map(item => {
                if (item.productId == value) {
                    return false
                }
                return true
            })
            const inValid = checkResult.includes(false)
            this.handleUpdateCheckValid('thesamePid', inValid)
            return !inValid
        }
    }
    handleValid = (id) => {
        switch(id) {
            case 'variable':
                const sameVariableValid = this.props.pd.checkValid.filter(item => item.key == 'variationNameNotDuplicate')
                console.log(sameVariableValid)
                return sameVariableValid.length == 0 ? false: !sameVariableValid[0].valid
            case 'sku':
                const sameSkuValid = this.props.pd.checkValid.filter(item => item.key == 'thesameSku')
                console.log(sameSkuValid)
                return sameSkuValid.length == 0 ? false: !sameSkuValid[0].valid
            case 'pid':
                const samePidValid = this.props.pd.checkValid.filter(item => item.key == 'thesamePid')
                console.log(samePidValid)
                return samePidValid.length == 0 ? false: !samePidValid[0].valid
            default: 
                return false
        }    
    }

    handleVariationNoteOpen = () => {
        this.setState({variationNote: true});
    };

    handleVariationNoteClose = () => {
        this.setState({variationNote: false});
    };

    learnMoreModal = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
            borderRadius: '4px', padding : '2%'}}>
            <div className="modal-close-wrapper" style={{position: 'absolute',top: '-5%', left: '0%'}}>
                <button className="modal-close-btn" onClick={() => this.handleVariationNoteClose()}>
                    <img src={closeIcon} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                </button>
            </div>
        <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700'}} ><FormattedMessage id="basedata.variationtheme" /></p>
          <p style={{lineHeight : '1.5rem'}}>
           <p><FormattedMessage id="basedata.remindervariationtheme" /></p>
           <img src={image1} alt="image1" style={{width: '80%', margin:'auto', marginBottom:'3%', marginTop:'3%'}}/>
           <p><FormattedMessage id="basedata.remindervariationthemesubtitle" /></p>
           <p style={{marginTop:'2%'}}><FormattedMessage id="basedata.remindersubtitle2" /></p>
           <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
            <li><FormattedMessage id="basedata.remindersubtitle2-1" /></li>
            <li><FormattedMessage id="basedata.remindersubtitle2-2" /></li>
            <li><FormattedMessage id="basedata.remindersubtitle2-3" /></li>
           </ul>
          </p>
        </div>


    );

    render () {
        return (
            <div className="base-info-form">
                <div className="base-input-wrapper">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label"><FormattedMessage id="basedata.productcategory"/></p>
                    </div>
                    <div className="base-input-section">
                        <p className="selected-product-category"> {this.props.category}</p>                        
                    </div>
                </div>
                <div className="base-input-wrapper">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label"><FormattedMessage id="basedata.brandnamechinese"/></p>
                    </div>
                    { this.props.type == 'addsku' 
                        ?   <div className="base-input-section">
                                <p className="selected-product-category">{this.props.product.brandNameCH}</p>
                            </div>
                        :   <FormattedMessage  id="basedata.brandnamechinese">
                                { placeholder =>
                                    <InputField
                                        id='brandNameCH'
                                        key='brandNameCH'
                                        value={this.props.pd.brandNameCH}
                                        placeHolder={placeholder}
                                        onChange={this.onBrandNameChChange}
                                        onBlur={this.onBlurCheckValid}
                                        rule={['chinese', 'maxLen50']}
                                    />
                                }
                            </FormattedMessage>
                    }
                </div>
                <div className="base-input-wrapper">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label"><FormattedMessage id="basedata.brandnameenglish"/></p>
                    </div>
                    { this.props.type == 'addsku'
                        ?   <div className="base-input-section">
                                <p className="selected-product-category">{this.props.product.brandNameEN}</p>
                            </div>
                        :   <FormattedMessage  id="basedata.brandnameenglish">
                                { placeholder =>
                                    <InputField
                                        id='brandNameEN'
                                        key='brandNameEN'
                                        value={this.props.pd.brandNameEN}
                                        placeHolder={placeholder}
                                        onChange={this.onBrandNameEnChange}
//                                        onChange={this.props.showNoticeChange}
                                        onBlur={this.onBlurCheckValid}
                                        rule={['english', 'maxLen50']}
                                    />
                                }
                            </FormattedMessage>
                    }
                </div>
                <div className="base-input-wrapper">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label" style={{display: 'flex',justifyContent: 'flex-end'}}>
                            <FormattedMessage id="basedata.productnamechinese"/>
                            <Tooltip title={
                                <div style={{fontSize: '15px', lineHeight : '1.5rem'}}>
                                    <FormattedMessage  id="basedata.productnamenote"/>
                                </div>
                                } arrow placement="bottom-end">
                                <span>
                                    <i class="fa fa-question-circle" style={{fontSize: '18px', marginLeft: '5px'}}></i>
                                </span>
                            </Tooltip>
                        </p>
                    </div>
                    { this.props.type == 'addsku'
                        ?   <div className="base-input-section"> 
                                <p className="selected-product-category">{this.props.product.productNameCH}</p>
                            </div>
                        :   <FormattedMessage  id="basedata.productnamechinese">
                                { placeholder =>
                                    <InputField
                                        id='productNameCH'
                                        key='productNameCH'
                                        value={this.props.pd.productNameCH}
                                        placeHolder={placeholder}
                                        onChange={this.onProductNameChChange}
//                                        onChange={this.props.showNoticeChange}
                                        onBlur={this.onBlurCheckValid}
                                        rule={['maxLen50']}
                                    />
                                }
                            </FormattedMessage>
                    }
                </div>
                <div className="base-input-wrapper">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label" style={{display: 'flex',justifyContent: 'flex-end'}}>
                            <FormattedMessage id="basedata.productnameenglish"/>
                            <Tooltip title={
                                <div style={{fontSize: '15px', lineHeight : '1.5rem'}}>
                                    <FormattedMessage  id="basedata.productnamenote"/>
                                </div>
                                } arrow placement="bottom-end">
                                <span>
                                    <i class="fa fa-question-circle" style={{fontSize: '18px', marginLeft: '5px'}}></i>
                                </span>
                            </Tooltip>
                        </p>
                    </div>
                    { this.props.type == 'addsku'
                        ?   <div className="base-input-section">
                                <p className="selected-product-category">{this.props.product.productNameEN}</p>
                            </div>
                        :   <FormattedMessage  id="basedata.productnameenglish">
                                { placeholder =>
                                    <InputField
                                        id='productNameEN'
                                        key='productNameEN'
                                        value={this.props.pd.productNameEN}
                                        placeHolder={placeholder}
                                        onChange={this.onProductNameEnChange}
//                                        onChange={this.props.showNoticeChange}
                                        onBlur={this.onBlurCheckValid}
                                        rule={['english', 'maxLen50']}
                                    />
                                }
                            </FormattedMessage>
                    }                    
                </div>
                <div className="base-input-wrapper">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label" style={{display: 'flex',justifyContent: 'flex-end'}}>
                            <FormattedMessage id="basedata.manufacturerchinese"/>
                            <Tooltip title={
                                <div style={{fontSize: '15px', lineHeight : '1.5rem'}}>
                                    <FormattedMessage  id="basedata.manufacturernote"/>
                                </div>
                                } arrow placement="bottom-end">
                                <span>
                                    <i class="fa fa-question-circle" style={{fontSize: '18px', marginLeft: '5px'}}></i>
                                </span>
                            </Tooltip>
                        </p>
                    </div>
                    { this.props.type == 'addsku'
                        ?   <div className="base-input-section"> 
                                <p className="selected-product-category">{this.props.product.manufacturerCH}</p>
                            </div>
                        :   <FormattedMessage  id="basedata.manufacturerchinese">
                                { placeholder =>
                                    <InputField
                                        id='manufacturerCH'
                                        key='manufacturerCH'
                                        value={this.props.pd.manufacturerCH}
                                        placeHolder={placeholder}
                                        onChange={this.onManufacturerChChange}
//                                        onChange={this.props.showNoticeChange}
                                        onBlur={this.onBlurCheckValid}
                                        rule={['maxLen50']}
                                    />
                                }
                            </FormattedMessage>
                    }
                </div>
                <div className="base-input-wrapper">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label" style={{display: 'flex',justifyContent: 'flex-end'}}>
                            <FormattedMessage id="basedata.manufacturerenglish"/>
                            <Tooltip title={
                                <div style={{fontSize: '15px', lineHeight : '1.5rem'}}>
                                   <FormattedMessage  id="basedata.manufacturernote"/>
                                </div>
                                } arrow placement="bottom-end">
                                <span>
                                    <i class="fa fa-question-circle" style={{fontSize: '18px', marginLeft: '5px'}}></i>
                                </span>
                            </Tooltip>
                        </p>
                    </div>
                    { this.props.type == 'addsku'
                        ?   <div className="base-input-section">
                                <p className="selected-product-category">{this.props.product.manufacturerEN}</p>
                            </div>
                        :   <FormattedMessage  id="basedata.manufacturerenglish">
                                { placeholder =>
                                    <InputField
                                        id='manufacturerEN'
                                        key='manufacturerEN'
                                        value={this.props.pd.manufacturerEN}
                                        placeHolder={placeholder}
                                        onChange={this.onManufacturerEnChange}
//                                        onChange={this.props.showNoticeChange}
                                        onBlur={this.onBlurCheckValid}
                                        rule={['english', 'maxLen50']}
                                    />
                                }
                            </FormattedMessage>
                    }                    
                </div>
                <div className="base-selector-wrapper">
                    <div className="base-selector-label-wrapper">
                        <p className="base-selector-label" style={{display: 'flex',justifyContent: 'flex-end'}}>
                            <FormattedMessage id="basedata.variationtheme"/>
                            <Tooltip  title={
                                <div style={{fontSize: '15px', lineHeight : '1.5rem'}}>
                                    <FormattedMessage  id="basedata.variationnote"/>
                                    <span style={{color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{this.handleVariationNoteOpen()}}>
                                        <FormattedMessage id ="basedata.learnmore"/>
                                    </span>
                                </div>
                                } arrow interactive placement="bottom-end">
                                <span>
                                    <i class="fa fa-question-circle" style={{fontSize: '18px', marginLeft: '5px'}}></i>
                                </span>
                            </Tooltip>
                        </p>
                    </div>

                    <Modal
                        open={this.state.variationNote}
                        onClose={this.handleVariationNoteClose}
                        aria-labelledby="simple-modal-title"
                        aria-describedby="simple-modal-description"
                    >
                      {this.learnMoreModal}
                    </Modal>

                    { this.props.type == 'addsku'
                        ?   <div className="base-input-section">
                                <p className="selected-product-category">{this.props.product.variationTheme}</p>
                            </div>
                        :   <SelectField
                                key='variationTheme'
                                value={this.props.selectedTheme}
                                onChange={this.props.handleThemeChange}
                                options={this.props.themeOptions}
                                className='base-selector'
                            />
                    }
                </div>
                {   this.props.showVariationVariable && !this.props.isTwoTheme
                    ?   <Variation
                            status={this.props.type}
                            selectedTheme={this.props.selectedTheme}
                            onInputsChange={this.props.onInputsChange}
                            addVariable={this.props.addVariable}
                            minusVariable={this.props.minusVariable}
                            showMinus={this.props.showMinus}
                            productIdTypeOptions={this.props.productIdTypeOptions}
                            handleProductIdTypeChange={this.props.handleProductIdTypeChange}
                            onBlurCheckValid={this.onBlurCheckValid}
                            onCheckVariationVariable={this.handleCheckSingleThemeVV}
                            onCheckSameValue={this.handleCheckSameValue}
                            onCheckDefault={this.handleCheckDefault}
                            onCheckRequire={this.handleCheckRequire}
                            bpId={this.props.bpId}
                            handleValid={this.handleValid}
                        />
                    :   <div></div>
                }
                { this.props.showVariationVariable && this.props.isTwoTheme
                    ?   <Permutations
                            status={this.props.type}
                            selectedTheme={this.props.selectedTheme}
                            onInputsChange={this.props.onInputsChange}
                            addVariable={this.props.addVariable}
                            minusVariable={this.props.minusVariable}
                            isTwoTheme={this.props.isTwoTheme}
                            onMultiThemeVariableChange={this.props.onMultiThemeVariableChange}
                            firstShowMinus={this.props.firstShowMinus}
                            secondShowMinus={this.props.secondShowMinus}
                            handleCheckboxChange={this.props.handleCheckboxChange}
                            onSelect={this.props.onSelect}
                            productIdTypeOptions={this.props.productIdTypeOptions}
                            handleProductIdTypeChange={this.props.handleProductIdTypeChange}
                            onBlurCheckValid={this.onBlurCheckValid}
                            onCheckVariationVariable={this.handleCheckMultiThemeVV}
                            onCheckSameValue={this.handleCheckSameValue}
                            onCheckDefault={this.handleCheckDefault}
                            onCheckRequire={this.handleCheckRequire}
                            bpId={this.props.bpId}
                            handleValid={this.handleValid}
                        />
                    : <div></div>
                }
            </div>
        )
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(BaseData);
