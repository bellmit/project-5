import React, { Component } from 'react';
import { FormattedMessage } from 'react-intl';
import CheckBox from './Checkbox/CheckBox';
// import Select from 'react-select';
import { connect } from 'react-redux';
import InputField from './InputField';
import SelectField from './SelectField';

const mapStateToProps = (state) => {
    return {
        pd: state.PD,
    }
}
const mapDispatchToProps = (dispatch) => {
    return {
    }
}
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

class Permutations extends Component {
    constructor(props) {
        super();
        this.state = {};
    };

    // onNoIdSelect = (item) => {
    //     // console.log(item)
    //     if (item.noIdProvide == null) {
    //         item.noIdProvide = true
    //         item.productId =''
    //     } else {
    //         if (item.noIdProvide == true) {
    //             item.noIdProvide = false
    //         } else {
    //             item.noIdProvide = true
    //             item.productId =''
    //         }
    //     }
    //     // console.log(item)
    // }
    // componentDidMount = () => {
        // this.props.pd.multiTheme[0].variables.map(variable => {
        //     const disabled = variable.index.map(i => {
        //         return this.props.pd.inputs[i].applying.length == 0 && this.props.pd.inputs[i].selling.length == 0 
        //             ?   null
        //             :   true       
        //     })
        //     console.log(disabled)
        //     if (disabled.includes(false)) {
        //         variable.disabled = false
        //     } else {
        //         variable.disabled = true
        //     }
        //     console.log(variable)
        // })
        // console.log(this.props.pd.multiTheme[0].variables)

        // this.props.pd.multiTheme[1].variables.map(variable => {
        //     const disabled = variable.index.map(i => {
        //         return this.props.pd.inputs[i].applying.length == 0 && this.props.pd.inputs[i].selling.length == 0 
        //             ?   false
        //             :   true       
        //     })
        //     console.log(disabled)
        //     if (disabled.includes(false)) {
        //         variable.disabled = false
        //     } else {
        //         variable.disabled = true
        //     }
        //     console.log(variable)
        // })
        // console.log(this.props.pd.multiTheme[1].variables)
    // }

    
    render () {
        return (
            <div className="variation-section">
                <div className="base-multi-input-wrapper multiple-rows">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label">{this.props.pd.multiTheme[0].theme}</p>
                    </div>
                    <div className="base-input-small-wrapper">
                        { this.props.pd.multiTheme[0].variables.map((item, index) => {
                            // if (this.props.status == 'addsku') {
                            //     return (
                            //         <div className="base-input-container">
                            //             <input
                            //                 className="base-input"
                            //                 placeholder={item.value}
                            //                 disabled
                            //             >
                            //             </input>
                            //         </div>
                            //     )
                            // } else {
                                return (
                                    <div className="base-input-container">
                                        <InputField
                                            id={`${index}-multi-variable-1`}
                                            key={`${index}-multi-variable-1`}
                                            value={item.value}
                                            placeHolder=''
                                            onChange={this.props.onMultiThemeVariableChange}
                                            onBlur={this.props.onBlurCheckValid}
                                            index={index}
                                            item={item}
                                            custom={0}
                                            type="multiThemeVariable"
                                            rule={['maxLen20', 'english', 'variable','variationname']}
                                            defaultValue={`variable1-${index+1}`}
                                            onCheckValue={this.props.onCheckVariationVariable}
                                            onCheckDefault={this.props.onCheckDefault}
                                            onCheckRequire={this.props.onCheckRequire}
                                            disabled={item.disabled == undefined ? false: item.disabled}
                                            inValid={this.props.handleValid('variable')}
                                        />                                        
                                    </div>
                                )
                            // }
                        })}
                        
                        <button
                            className="base-input-inline-btn"
                            onClick={() => this.props.addVariable(0)}
                        > Add </button>
                        {  this.props.firstShowMinus
                            ?   <button
                                    className="base-input-inline-btn"
                                    onClick={() => this.props.minusVariable(0)}
                                > Delete </button>
                            :   <div></div>
                        }
                        
                    </div>
                </div>
                <div className="base-multi-input-wrapper multiple-rows">
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label">{this.props.pd.multiTheme[1].theme}</p>
                    </div>
                    <div className="base-input-small-wrapper">
                        { this.props.pd.multiTheme[1].variables.map((item, index) => {
                            // console.log(item)
                            
                            // if (this.props.status == 'addsku' && item.index.length == 0) {
                            //     return (
                            //         <div className="base-input-container">
                            //             <input
                            //                 className="base-input"
                            //                 placeholder={item.value}
                            //                 disabled
                            //             >
                            //             </input>
                            //         </div>)
                            // } else {
                                return (
                                    <div className="base-input-container">
                                        <InputField
                                            id={`${index}-multi-variable-2`}
                                            key={`${index}-multi-variable-2`}
                                            value={item.value}
                                            placeHolder=''
                                            onChange={this.props.onMultiThemeVariableChange}
                                            onBlur={this.props.onBlurCheckValid}
                                            index={index}
                                            item={item}
                                            custom={1}
                                            type="multiThemeVariable"
                                            rule={['maxLen20', 'english', 'variable','variationname']}
                                            defaultValue={`variable2-${index+1}`}
                                            onCheckValue={this.props.onCheckVariationVariable}
                                            onCheckDefault={this.props.onCheckDefault}
                                            onCheckRequire={this.props.onCheckRequire}
                                            disabled={item.disabled == undefined ? false: item.disabled}
                                            inValid={this.props.handleValid('variable')}
                                        />
                                    </div>
                                )
                            // }
                        })}
                        
                        <button
                            className="base-input-inline-btn"
                            onClick={() => this.props.addVariable(1)}
                        > Add </button>
                        {  this.props.secondShowMinus
                            ?   <button
                                    className="base-input-inline-btn"
                                    onClick={() => this.props.minusVariable(1)}
                                > Delete </button>
                            :   <div></div>
                        }
                    </div>
                </div>
                <div className="base-sku-list-section">
                    <div className="base-reminder-wrapper">
                        <p className="base-reminder">
                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                        <FormattedMessage id="product.permutationsubtitle1" />
                        <br/><FormattedMessage id="product.permutationsubtitle2" /></p>
                    </div>

                { this.props.pd.inputs.map((item,index) => {
                    return (
                    <div className="base-sku-list-wrapper">
                        <div className="base-sku-list-checkbox">
                            <CheckBox
                                isSelected={item.isSelected}
                                onSelect={this.props.onSelect}
                                index={index}
                            />
                        </div>
                        { item.isSelected
                            ?   <div className="base-sku-list">
                                    <div className="base-sku-list-column" style={{'marginRight': '12px'}}>
                                        <p className="base-sku-list-column-title">{this.props.selectedTheme}</p>
                                        <p className="base-sku-list-column-text">
                                            {this.props.isTwoTheme ? `${item.variable} & ${item.secondVariable}` : item.variable}
                                        </p>
                                    </div>                                  
                                    <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">Seller SKU</p>
                                        { item.editable
                                            ?   <InputField
                                                    status={this.props.status}
                                                    id={`${index}-sellerSku`}
                                                    key={`${index}-sellerSku`}
                                                    value={item.sellerSku}
                                                    placeHolder='Seller SKU'
                                                    onChange={this.props.onInputsChange}
                                                    onBlur={this.props.onBlurCheckValid}
                                                    onCheckSameValue={this.props.onCheckSameValue}
                                                    index={index}
                                                    type="sellersku"
                                                    rule={['maxLen20', 'sellersku' , 'uniqueSku']}
                                                    bpId={this.props.bpId}
                                                    inValid={this.props.handleValid('sku')}
                                                />
                                            :   <input
                                                    className="base-input base-sku-list-input"
                                                    value={item.sellerSku}
                                                    placeholder={item.sellerSku == '' ? 'Seller SKU': item.sellereSku}
                                                    disabled
                                                ></input>
                                        }
                                    </div>
                                    <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">Product ID</p>
                                        { item.editable
                                            ?   <InputField
                                                    status={this.props.status}
                                                    id={`${index}-productId`}
                                                    key={`${index}-productId`}
                                                    value={item.productId}
                                                    placeHolder='Product ID'
                                                    onChange={this.props.onInputsChange}
                                                    onBlur={this.props.onBlurCheckValid}
                                                    onCheckSameValue={this.props.onCheckSameValue}
                                                    index={index}
                                                    type='productid'
                                                    rule={['maxLen15', 'integer','productId']}
                                                    disabled={item.noIdProvide}
                                                    bpId={this.props.bpId}
                                                    inValid={this.props.handleValid('pid')}
                                                />
                                            :   <input
                                                    className="base-input base-sku-list-input"
                                                    value={item.productId}
                                                    placeholder={item.productId == '' ? 'Product ID': item.productId}
                                                    disabled
                                                ></input>
                                        }
                                    </div>
                                    <div className="check-list-li">
                                        <CheckBox
                                            onSelect={()=>this.props.onInputsChange(!item.noIdProvide, index, 'noproductid')}
                                            isSelected={item.noIdProvide}
                                            disabled={!item.editable}
                                        />
                                        <p className="check-list-lable"><FormattedMessage id="addnewproduct.noid"/></p>
                                    </div>
                                    <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">Product ID Type</p>
                                        { item.editable
                                            ? <SelectField
                                                className="base-selector-l"
                                                value={item.productIdType}
                                                onChange={this.props.handleProductIdTypeChange}
                                                placeholder={item.productIdType == '' ? 'Select...' : item.productIdType}
                                                options={this.props.productIdTypeOptions}
                                                index={index}
                                                isDisabled={item.noIdProvide}
                                            />
                                            : <p>{item.productIdType}</p>
                                        }
                                    </div>
                                    {/* <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">HS Code/CCC Code</p>
                                        <input
                                            className="base-input base-sku-list-input"
                                            value={item.HScode}
                                            onChange={(e) => this.props.onInputsChange(e, index, 'hscode')}
                                        ></input>
                                    </div>
                                    <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">Your Price</p>
                                        <input
                                            className="base-input base-sku-list-input"
                                            value={item.price}
                                            onChange={(e) => this.props.onInputsChange(e, index, 'price')}
                                        ></input>
                                    </div>
                                    <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">Quantity</p>
                                        <input
                                            className="base-input base-sku-list-input"
                                            value={item.quantity}
                                            onChange={(e) => this.props.onInputsChange(e, index, 'quantity')}
                                        ></input>
                                    </div> */}
                                </div>
                            :   <del className="base-sku-list">
                                    <div className="base-sku-list-column" style={{'marginRight': '12px'}}>
                                        <p className="base-sku-list-column-title">{this.props.selectedTheme}</p>
                                        <p className="base-sku-list-column-text">
                                            {this.props.isTwoTheme ? `${item.variable} & ${item.secondVariable}` : item.variable}
                                        </p>
                                    </div>                                  
                                    <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">Seller SKU</p>
                                        <input
                                            className="base-input base-sku-list-input"
                                            value={item.sellerSku}
                                            disabled
                                        ></input>
                                    </div>
                                    <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">Product ID</p>
                                        <input
                                            className="base-input base-sku-list-input"
                                            value={item.productId}
                                            disabled
                                        ></input>                                     
                                    </div>
                                    <div className="base-sku-list-column">
                                        <p className="base-sku-list-column-title">Product ID Type</p>
                                        <input
                                            className="base-input base-sku-list-input"
                                            value={item.productIdType}
                                            disabled
                                        ></input> 
                                    </div>
                                </del>
                        }
                    </div>)}
                )}
            </div>
        </div>
        )
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Permutations);