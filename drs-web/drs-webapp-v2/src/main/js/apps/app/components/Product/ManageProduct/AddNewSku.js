import React, { Component } from 'react';
import { connect } from 'react-redux';
import { addNewSku, updateMultiTheme, updateInputs,updateCheckValid } from '../../../actions/index';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import BaseData from './BaseData';
import { FormattedMessage } from 'react-intl';

const productIdTypeOptions = [
    // {value: 'GTIN', label: 'GTIN'},
    {value: 'EAN', label: 'EAN'},
    // {value: 'GCID', label: 'GCID'},
    {value: 'UPC', label: 'UPC'},
    // {value: 'ASIN', label: 'ASIN'}
] 

const mapStateToProps = (state) => {
    return {
        pd: state.PD,
        d: state.d
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        onUpdateCheckValid: (boolean) => dispatch(updateCheckValid(boolean)),
        onAddNewSku: (supplierId, bpId, bpObj, message) => dispatch(addNewSku(supplierId, bpId, bpObj,message)),
        onUpdateInputs: (inputs) => dispatch(updateInputs(inputs)),
        onUpdateMultiTheme: (multiTheme) => dispatch(updateMultiTheme(multiTheme))
    }
}

class AddNewSku extends Component {
    constructor(props) {
        super()
        this.state = {
            currentProduct: {},
            isTwoTheme: false,
            selectedTheme: '',
            // inputs: [],
            // multiTheme: [],
            firstShowMinus: false,
            secondShowMinus: false,
            showMinus: false,
            originalVariableLen: 0,
            productIdTypeOptions: productIdTypeOptions,
            showNotice: false,
            showVariationCheckWarning: false
        }
    }
    componentDidMount = () => {
        // console.log('did mount!')
        const result = this.props.pd.products.filter(item => item._id.$oid == this.props.bpId)
        // console.log('result',result)
        const boolean = result[0].variationTheme.includes('&') ? true : false;
        const theme = result[0].variationTheme;
        this.setState({ 
            selectedTheme: theme,
            isTwoTheme: boolean,
            currentProduct: result[0]
        });

        if (boolean) {
            const inputs = result[0].skus.map(item => {
                // console.log(item, 'componentDidMount')
                const checkEditable = item.applying.length == 0 && item.selling.length == 0
                const arr = item.variable.value.split('&');
                const variable = arr[0].trim();
                const secondVariable = arr[1].trim();
                return { 
                    variable: variable,
                    secondVariable: secondVariable,
                    sellerSku: item.sellerSku,
                    productId: item.productId.value,
                    productIdType: item.productIdType.value,
                    // editable: item.editable,
                    noIdProvide: item.noIdProvide,
                    editable: checkEditable,
                    isSelected: item.isSelected,
                    applying : item.applying,
                    selling : item.selling
                }
            })

            result[0].multiTheme[0].variables.map(variable => {
                const disabled = variable.index.map(i => {
                    return inputs[i].applying.length == 0 && inputs[i].selling.length == 0 
                        ?   null
                        :   true       
                })
                // console.log(disabled)
                if (disabled.includes(false)) {
                    variable.disabled = false
                } else {
                    variable.disabled = true
                }
                // console.log(variable)
            })
            // console.log(result[0].multiTheme[0].variables)
    
            result[0].multiTheme[1].variables.map(variable => {
                const disabled = variable.index.map(i => {
                    return inputs[i].applying.length == 0 && inputs[i].selling.length == 0 
                        ?   false
                        :   true       
                })
                // console.log(disabled)
                if (disabled.includes(false)) {
                    variable.disabled = false
                } else {
                    variable.disabled = true
                }
                // console.log(variable)
            })
            // console.log(result[0].multiTheme[1].variables)
            this.props.onUpdateInputs(inputs)
            this.props.onUpdateMultiTheme(result[0].multiTheme)

        } else {
            const inputs = result[0].skus.map(item => {
                const checkEditable = item.applying.length == 0 && item.selling.length == 0
                return { 
                    variable: item.variable.value,
                    sellerSku: item.sellerSku,
                    productId: item.productId.value,
                    productIdType: item.productIdType.value,
                    noIdProvide: item.noIdProvide,
                    editable: checkEditable,
                    // editable: item.editable
                    applying : item.applying,
                    selling : item.selling
                }
            })
            this.setState({
                originalVariableLen: inputs.length
            })
            this.props.onUpdateInputs(inputs)
        }
    }

    addVariable = (i) => {
        if (this.state.isTwoTheme) {
            // console.log('theme includes & ')
            const index = i == 0 ? 1 : 0;
            // const changed = this.state.multiTheme;
            const changed = this.props.pd.multiTheme
            // console.log(changed, 'changed')
            const len = changed[i].variables.length;
            const newVariable = { value: `variable${index == 0 ? 2 : 1}-${len + 1}`, index: []}
            changed[i].variables.push(newVariable);
            // const result = this.state.inputs;
            const result = this.props.pd.inputs
            
            changed[index].variables.map(item => {
                const obj = { 
                    variable:`${item.value}`,
                    secondVariable: `variable${index == 0 ? 2 : 1}-${len + 1}`,
                    sellerSku: '',
                    productId: '', 
                    productIdType: '',
                    noIdProvide: false,
                    editable: true,
                    isSelected: true
                }
                const obj2 = { 
                    variable: `variable${index == 0 ? 2 : 1}-${len + 1}`,
                    secondVariable: `${item.value}`,
                    sellerSku: '',
                    productId: '',
                    productIdType: '',
                    noIdProvide: false,
                    editable: true,
                    isSelected: true
                }
                result.push(index == 0 ? obj : obj2);
                const addedItemIndex = result.indexOf(index == 0 ? obj : obj2);
                changed[i].variables[len].index.push(addedItemIndex);
                item.index.push(addedItemIndex)
            })


            // if (changed[0].variables.length < 2 && changed[1].variables.length < 3) {
            //     firstBoolean = false;
            //     secondBoolean = false;
            // } else if (changed[0].variables.length < 3 && changed[1].variables.length < 2) {
            //     firstBoolean = false;
            //     secondBoolean = false;
            // } else if (changed[0].variables.length < 2) {
            //     firstBoolean = false;
            // } else if (changed[1].variables.length < 2) {
            //     secondBoolean = false;
            // }

            const variableDisabled = changed[i].variables.map(item => {
                // console.log(item, 'add')
                return item.disabled == undefined ? false : item.disabled
            })
            // console.log('vairableDisabled',variableDisabled)

            if (variableDisabled.includes(false)) {
                i == 0 
                    ? this.setState({ firstShowMinus: true })
                    : this.setState({ secondShowMinus: true })
            } else {
                i == 0 
                    ? this.setState({ firstShowMinus: false })
                    : this.setState({ secondShowMinus: false })
            }
           
            this.props.onUpdateInputs(result)
            this.props.onUpdateMultiTheme(changed)

        } else {
            const counts = this.props.pd.inputs.length + 1;
            let showMinusBoolean = true;

            // if (this.props.pd.inputs.length < 2 ) {
            //     showMinusBoolean = false;
            // } 
            this.setState({
                showMinus: showMinusBoolean,
            })
            const obj = { variable: `variable${counts}`, sellerSku: '', productId: '', noIdProvide: false,productIdType: '', editable: true, isSelected: true };
            const inputs = [...this.props.pd.inputs, obj ];
            this.props.onUpdateInputs(inputs)
        }
    }

    minusVariable = ( index) => {
        
        if (this.state.isTwoTheme) {
            const len = this.props.pd.multiTheme[index].variables.length;
            const result = this.props.pd.inputs.filter(input => (index == 0 ? input.variable: input.secondVariable) !== this.props.pd.multiTheme[index].variables[len-1].value);
            
            const changed = this.props.pd.multiTheme;
            changed[index].variables.splice(len-1, 1);

            // let firstBoolean = false;
            // let secondBoolean = false;
            // if (changed[0].variables.length < 2 && changed[1].variables.length < 3) {
            //     firstBoolean = false;
            //     secondBoolean = false;
            // } else if (changed[0].variables.length < 3 && changed[1].variables.length < 2) {
            //     firstBoolean = false;
            //     secondBoolean = false;
            // } else if (changed[0].variables.length < 2) {
            //     firstBoolean = false;
            // } else if (changed[1].variables.length < 2) {
            //     secondBoolean = false;
            // };

            // this.setState({ 
            //     inputs: result,
            //     multiTheme: changed,
            //     // firstShowMinus: firstBoolean,
            //     // secondShowMinus: secondBoolean
            // })
            
            const variableDisabled =  changed[index].variables.map(item => {
                // console.log(item, 'add')
                return item.disabled == undefined ? false : item.disabled
            })
            // console.log('variableDisabled', variableDisabled)
            if (variableDisabled.includes(false)) {
                index == 0 
                    ? this.setState({ firstShowMinus: true })
                    : this.setState({ secondShowMinus: true })
            } else {
                index == 0 
                    ? this.setState({ firstShowMinus: false })
                    : this.setState({ secondShowMinus: false })
            }

            this.props.onUpdateInputs(result)
            this.props.onUpdateMultiTheme(changed)

        } else {
            const i = this.props.pd.inputs.length - 1;
            const arr = [...this.props.pd.inputs];
            let showMinusBoolean = true;
        
            if (arr.length > 1) {
                arr.splice(i, 1);
            }
            if (arr.length < this.state.originalVariableLen + 1 ) {
                showMinusBoolean = false;
            } 
            this.setState({
                showMinus: showMinusBoolean
            })
            this.props.onUpdateInputs(arr)
        }
    }
    
    handleProductIdTypeChange = (e, index) => {
        // console.log(e.value, index)
        // const arr = [...this.state.inputs];
        const arr = [...this.props.pd.inputs]
        arr[index].productIdType = e.value;
        this.props.onUpdateInputs(arr)
    }

    onInputsChange = (value,index,target) => {
        // console.log('oninputschange', value)
        const inputs = [...this.props.pd.inputs];
        this.setState({ showNotice: false })
        switch (target) {                
            case 'variable':
                inputs[index].variable = value;
                return this.props.onUpdateInputs(inputs)
            case 'sellersku':
                inputs[index].sellerSku = value;
                return this.props.onUpdateInputs(inputs)
            case 'productid':
                inputs[index].productId = value;
                return this.props.onUpdateInputs(inputs)
            case 'noproductid':
                inputs[index].noIdProvide = value;
                inputs[index].productId = ''
                inputs[index].productIdType = ''
                return this.props.onUpdateInputs(inputs)
            default:
                return this.props.onUpdateInputs(inputs)
        }
    }
    onMultiThemeVariableChange = (e, indexArr, param, index ) => {
        const arr = [...this.props.pd.multiTheme];
        console.log(e.target)
        console.log("222222"+e)
        arr[param].variables[index].value = e;
        const inputs = [...this.props.pd.inputs];

        indexArr.map( i => {
            return param == 0 ? inputs[i].variable = e : inputs[i].secondVariable = e
        })

        this.props.onUpdateInputs(inputs)
        this.props.onUpdateMultiTheme(arr)
    }

    checkInputChangedHandler = () => {
        const keyArr =  [
            // { key: 'brandNameCH' , value: this.props.pd.brandNameCH },
            // { key: 'brandNameEN', value: this.props.pd.brandNameEN },
            // { key: 'productNameCH', value: this.props.pd.productNameCH },
            // { key: 'productNameEN', value: this.props.pd.productNameEN },
        ]

        this.props.pd.inputs.map((item, index) => {
            if (item.noIdProvide != null && item.noIdProvide) {
                return keyArr.push({
                    key: `${index}-sellerSku`,
                    value: item.sellerSku
                })
            } else {
                return keyArr.push({
                    key: `${index}-sellerSku`,
                    value: item.sellerSku
                }, {
                    key: `${index}-productId`,
                    value: item.productId
                })
            }
        })
        const arr = this.props.pd.checkValid;

        keyArr.map(key => {
            if (key.value == '') {
                const obj = {key: key.key, valid: false }
                const target = this.props.pd.checkValid.filter(item => item.key == key.key)

                if (target.length == 0) {
                    return arr.push(obj)
                } else {
                    const index = this.props.pd.checkValid.indexOf(target[0])
                    console.log(index)
                    return arr[index] = obj;
                }
            } else {
                const obj = {key: key.key, valid: true }
                const target = this.props.pd.checkValid.filter(item => item.key == key.key)

                if (target.length == 0) {
                    return arr.push(obj)
                } else {
                    const index = this.props.pd.checkValid.indexOf(target[0])
                    // console.log(index)
                    return arr[index] = obj;
                }
            }
        })
        console.log(arr, 'checkInputChangedHandler')
        this.props.onUpdateCheckValid(arr)
    }

    checkSelectChangeHandler = () => {
        const keyArr = [
           { key: 'variationTheme', value: this.state.selectedTheme }
        ]
        
        this.props.pd.inputs.map((item, index) => {
            if (!item.noIdProvide) {
                return keyArr.push({
                    key: `${index}-productIdType`,
                    value: item.productIdType
                })
            }
        })
        // console.log(keyArr, 'checkSelectChangeHandler keyArr')
        const arr = this.props.pd.checkValid

        keyArr.map(key => {
            if (key.value == 'Select...'|| key.value == '') {
                const obj = { key: key.key, valid: false }
                const target = this.props.pd.checkValid.filter(item => item.key == key.key)
                if (target.length == 0) {
                    return arr.push(obj)
                } else {
                    const index = this.props.pd.checkValid.indexOf(target[0])
                    return arr[index] = obj
                }
            } else {
                const obj = { key: key.key, valid: true }
                const target = this.props.pd.checkValid.filter(item => item.key == key.key)
                if (target.length == 0) {
                    return arr.push(obj)
                } else {
                    const index = this.props.pd.checkValid.indexOf(target[0])
                    return arr[index] = obj
                }
            }
        })
        console.log(arr, 'checkselectchangedHandler')
        this.props.onUpdateCheckValid(arr)
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
        // console.log(arr, 'onCheckValid')
        this.props.onUpdateCheckValid(arr)
    }
    // IF NEVER CHANGE THE VAIRATION NAME INPUT
    checkVariableChanged = () => {
        if (this.state.isTwoTheme) {
            let checkResult = this.props.pd.multiTheme[0].variables.map((item, index) => {
                if (item.value == `variable1-${index+1}`) {
                    return false
                }
                return true
            })
            this.props.pd.multiTheme[1].variables.map((item,index) => {
                if (item.value == `variable2-${index+1}`) {
                    return checkResult.push(false)  
                }
                return checkResult.push(true)
            })
            const inValid = checkResult.includes(false)
            if (inValid) {
                this.setState({ showVariationCheckWarning: true })
            } else {
                this.setState({ showVariationCheckWarning: false })
            }
            this.handleUpdateCheckValid('variationNameNotDefault', inValid)

        } else {
            const checkResult = this.props.pd.inputs.map((item,index) => {
                if (item.variable == `variable${index+1}`) {
                    return false
                }
                return true
            })
            const inValid = checkResult.includes(false)
            if (inValid) {
                this.setState({ showVariationCheckWarning: true })
            } else {
                this.setState({ showVariationCheckWarning: false })
            }
            this.handleUpdateCheckValid('variationNameNotDefault', inValid)
        }
    }

    createSku = () => {
        this.checkInputChangedHandler()
        this.checkSelectChangeHandler()
        this.checkVariableChanged()

        // const supplierId = 190;
        // console.log('on create sku')
        const supplierId = this.props.d.u.cid
        const dataArr = this.props.pd.inputs.map((item, index) => {
            // console.log(item, 'handle adding')
            const variableValue = this.state.isTwoTheme ? `${item.variable} & ${item.secondVariable}` : item.variable;
            const counter = index + 1; 
            let indexOfSku = 1;
            if (counter > 5) {
                indexOfSku = counter % 5 == 0 ? counter/5 : parseInt(counter/5) + 1
            }

            return Object.assign({
                sellerSku: item.sellerSku,
                productId: {name: 'Product ID', value: item.productId },
                productIdType: { name: 'Product ID Type', value: item.productIdType },
                noIdProvide: item.noIdProvide,
                variable: { name: 'Variable', value: variableValue },
                variationTheme: { name: 'Variation Theme', value: this.state.selectedTheme},
                retailPrice: '未定價',
                // salesVolume: 0,
                settlementsPeriodOrder: 0,
                // openPosition: 0,
                lastSevenDaysOrder: 0,
                fbaQuantity: 0,
                applying : item.applying == null ? [] : item.applying,
                selling : item.selling == null ? [] : item.selling,
                status: 'unapplied',
                actions: ['申請銷售', '修改'],
                pageIndex: indexOfSku,
                // editable: true,
                editable: item.editable,
                // NEED TO REFACTOR: IF NOT MULTI THEME
                isSelected: item.isSelected,
                retailPriceInUs: '未定價',
                retailPriceInTts: '未定價',
                retailPriceInUk: '未定價',
                retailPriceInCa: '未定價',
                retailPriceInDe: '未定價',
                retailPriceInFr: '未定價',
                retailPriceInIt: '未定價',
                retailPriceInEs: '未定價',
            })
        })
        const addedBpObj =  this.state.currentProduct;
        addedBpObj.multiTheme = this.props.pd.multiTheme;
        addedBpObj.totalSkus = dataArr.length;
        addedBpObj.skus = dataArr;
        // console.log(dataArr)

        // CHECK VAILD
        const valid = this.props.pd.checkValid.map(item => {
            return item.valid
        })
        if (valid.includes(false)) {
            // console.log(valid.includes(false), 'valid includes false')
            this.setState({ showNotice: true })
        } else {
            this.setState({ showNotice: false })
            const message = <FormattedMessage id="addnewsku.created"/>
            this.props.onAddNewSku(supplierId, this.props.bpId, addedBpObj, message);
            this.handleModalClose()
        }
    }

    onSelect = (index) => {
        const inputs = this.props.pd.inputs

        if (inputs[index].isSelected) {
            inputs[index].isSelected = false;
        } else {
            inputs[index].isSelected = true;
        }
        this.props.onUpdateInputs(inputs)
    }

    handleModalClose = () => {
        this.props.onClose(false)
        this.props.onUpdateInputs([])
    }

    render() {
        return (
            <div id="add-new-sku" className="drs-modal-container">
                <div className="drs-modal">
                    <div className="app-sub-pannel">
                        <div className="modal-close-wrapper">
                            <button className="modal-close-btn" onClick={() => this.handleModalClose()}>
                                <img src={closeIcon} alt="closeIcon" style={{width: '14px'}}/>
                                <span>關閉</span>
                            </button>
                        </div>
                        <div className="operation-pannel">
                            <div className="operation-pannel-main">
                                <BaseData
                                    type='addsku'
                                    bpId={this.props.bpId}
                                    category={this.state.currentProduct.category}
                                    product={this.state.currentProduct}
                                    isTwoTheme={this.state.isTwoTheme}
                                    selectedTheme={this.state.selectedTheme}
                                    // inputs={this.state.inputs}
                                    showVariationVariable={true}
                                    // multiTheme={this.state.multiTheme}
                                    addVariable={this.addVariable}
                                    minusVariable={this.minusVariable}
                                    onInputsChange={this.onInputsChange}
                                    onMultiThemeVariableChange={this.onMultiThemeVariableChange}
                                    firstShowMinus={this.state.firstShowMinus}
                                    secondShowMinus={this.state.secondShowMinus}
                                    showMinus={this.state.showMinus}
                                    brandNameCH={this.state.currentProduct.brandNameCH}
                                    brandNameEN={this.state.currentProduct.brandNameEN}
                                    productNameCH={this.state.currentProduct.productNameCH}
                                    productNameEN={this.state.currentProduct.productNameEN}
                                    manufacturerCH={this.state.currentProduct.manufacturerCH}
                                    manufacturerEN={this.state.currentProduct.manufacturerEN}
                                    productIdTypeOptions={this.state.productIdTypeOptions}
                                    handleProductIdTypeChange={this.handleProductIdTypeChange}
                                    onSelect={this.onSelect}
                                />
                            </div>
                            <div className="operation-pannel-footer">
                                { this.state.showNotice == false
                                    ?   <p></p>   
                                    :   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                            <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                                <FormattedMessage id="addnewproduct.warning"/>
                                                {this.state.showVariationCheckWarning ? <FormattedMessage id="addnewproduct.checkvariablewarning"/>: ''}
                                            </p>
                                        </div>
                                }
                                <div className="operation-btn-group" style={{justifyContent: 'center'}}>
                                    <button className="drs-btn drs-btn-normal" onClick={() => this.createSku()}>確認</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(AddNewSku)