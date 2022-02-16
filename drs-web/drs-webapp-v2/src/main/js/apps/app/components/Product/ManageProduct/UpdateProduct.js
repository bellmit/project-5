import React, { Component } from 'react';
import { connect } from 'react-redux';
import BaseData from './BaseData';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import { updateMultiTheme, updateProduct, updateInputs, updateCheckValid, 
    onBrandNameChChange, onBrandNameEnChange, onProductNameChChange, onProductNameEnChange, onManufacturerChChange, onManufacturerEnChange } from '../../../actions/index';
import { FormattedMessage } from 'react-intl';

const productCategory = [
    { category: 'A',
      theme: [
          { value: 'Color', label: 'Color' }, 
          { value: 'Size', label: 'Size'}, 
          { value: 'Package Count', label: 'Package Count'},
          { value: 'Color & Size', label: 'Color & Size'}
        ]
    },
    { category: 'B',
      theme: [
          { value: 'Color', label: 'Color' }, 
          { value: 'Package Count', label: 'Package Count' }
        ]
    },
    { category: 'C',
      theme: [
          { value: 'Color', label: 'Color' }, 
          { value: 'Size', label: 'Size'}, 
          { value: 'Package Count', label: 'Package Count'},
          { value: 'Package Quantity', label: 'Package Quantity'}
        ]
    }
];
const productIdTypeOptions = [
    // {value: 'GTIN', label: 'GTIN'},
    {value: 'EAN', label: 'EAN'},
    // {value: 'GCID', label: 'GCID'},
    {value: 'UPC', label: 'UPC'},
    // {value: 'ASIN', label: 'ASIN'}
];

const mapStateToProps = (state) => {
    return {
        pd: state.PD,
        d: state.d
    }
}

const mapDispatchToProps =  (dispatch) => {
    return {
        onUpdateCheckValid: (boolean) => dispatch(updateCheckValid(boolean)),
        onUpdateProduct: (supplierId, bpId, bpObj, message) => dispatch(updateProduct(supplierId, bpId, bpObj, message)),
        onUpdateInputs: (inputs) => dispatch(updateInputs(inputs)),
        onUpdateMultiTheme: (multiTheme) => dispatch(updateMultiTheme(multiTheme)),
        onPDNameChChangeHandler: (value) => dispatch(onProductNameChChange(value)),
        onPDNameEnChangeHandler: (value) => dispatch(onProductNameEnChange(value)),
        onManuChChangeHandler: (value) => dispatch(onManufacturerChChange(value)),
        onManuEnChangeHandler: (value) => dispatch(onManufacturerEnChange(value)),
        onBDNameChChangeHandler: (value) => dispatch(onBrandNameChChange(value)),
        onBDNameEnChangeHandler: (value) => dispatch(onBrandNameEnChange(value))
    }
}

class UpdateProduct extends Component {
    constructor(props) {
        super()
        this.state = {
            currentProduct: {},
            isTwoTheme: false,
            selectedTheme: '',
            firstShowMinus: true,
            secondShowMinus: true,
            showMinus: true,
            themeOptions: productCategory[0].theme,
            currentCategory: '',
            brandNameCH: '',
            brandNameEN: '',
            productNameCH: '',
            productNameEN: '',
            ManufacturerCH: '',
            ManufacturerEN: '',
            currentIndex: 0,
            productIdTypeOptions: productIdTypeOptions,
            showNotice: false,
            showVariationCheckWarning: false
        }
    }

    componentDidMount = () => {
        const result = this.props.pd.products.filter(item => item._id.$oid == this.props.bpId)
        // console.log('result',result)
        const boolean = result[0].variationTheme.includes('&') ? true : false;
        const theme = result[0].variationTheme;
        this.setState({ 
            selectedTheme: theme,
            isTwoTheme: boolean,
            currentProduct: result[0],
            currentCategory: result[0].category,
            currentIndex: result[0].currentIndex,
            showMinus: result[0].skus.length > 1 ? true: false
        });
        this.props.onPDNameChChangeHandler(result[0].productNameCH)
        this.props.onPDNameEnChangeHandler(result[0].productNameEN)
        this.props.onManuChChangeHandler(result[0].manufacturerCH)
        this.props.onManuEnChangeHandler(result[0].manufacturerEN)
        this.props.onBDNameChChangeHandler(result[0].brandNameCH)
        this.props.onBDNameEnChangeHandler(result[0].brandNameEN)

        if (boolean) {
            const inputs = result[0].skus.map(item => {
                const arr = item.variable.value.split('&');
                const variable = arr[0].trim();
                const secondVariable = arr[1].trim();
                return { 
                    variable: variable,
                    secondVariable: secondVariable,
                    sellerSku: item.sellerSku,
                    productId: item.productId.value,
                    productIdType: item.productIdType.value,
                    noIdProvide: item.noIdProvide,
                    editable: item.editable,
                    isSelected: item.isSelected,
                    applying : item.applying,
                    selling : item.selling
                }
            })
            
            this.props.onUpdateInputs(inputs)
            this.props.onUpdateMultiTheme(result[0].multiTheme)
        } else {
            const inputs = result[0].skus.map(item => {
                return { 
                    variable: item.variable.value,
                    sellerSku: item.sellerSku,
                    productId: item.productId.value,
                    productIdType: item.productIdType.value,
                    noIdProvide: item.noIdProvide,
                    editable: item.editable,
                    applying : item.applying,
                    selling : item.selling
                }
            })

            this.props.onUpdateInputs(inputs)
        }
    }

    handleThemeChange = (value) => {
       if (confirm('Making changes to the variation theme will delete all variations of the current ASIN. Do you wish to continue?')){ 
        if ( value.value.includes('&') ) {
            const str = value.value.split(' & ');
            const str1 = str[0];
            const str2 = str[1];
            const multiTheme = [
                {theme: str1, variables: [{ value: 'variable1-1', index: [0,1] }, { value: 'variable1-2', index: [2,3] }]}, 
                {theme: str2, variables: [{ value: 'variable2-1', index: [0,2] }, { value: 'variable2-2', index: [1,3] }]}
            ]
            const inputs = [
                // { variable: 'variable1-1', secondVariable: 'variable2-1', sellerSku: '', productId: '', productIdType: '', HScode: '', price: '', quantity: '' },
                { variable: 'variable1-1', secondVariable: 'variable2-1', sellerSku: '', productId: '', noIdProvide: false,productIdType: '', editable: true, isSelected: true },
                { variable: 'variable1-1', secondVariable: 'variable2-2', sellerSku: '', productId: '', noIdProvide: false,productIdType: '', editable: true, isSelected: true },
                { variable: 'variable1-2', secondVariable: 'variable2-1', sellerSku: '', productId: '', noIdProvide: false,productIdType: '', editable: true, isSelected: true },
                { variable: 'variable1-2', secondVariable: 'variable2-2', sellerSku: '', productId: '', noIdProvide: false,productIdType: '', editable: true, isSelected: true },
            ]

            this.setState({ isTwoTheme: true })
            this.props.onUpdateInputs(inputs)
            this.props.onUpdateMultiTheme(multiTheme)
        } else {
            this.setState({ isTwoTheme: false})

            const inputs = [
                { variable: 'variable1', sellerSku: '', productId: '',noIdProvide: false, productIdType: '', editable: true, isSelected: true }, 
                { variable: 'variable2', sellerSku: '', productId: '',noIdProvide: false, productIdType: '', editable: true, isSelected: true }
            ]

            this.props.onUpdateInputs(inputs)
        }
        this.setState({
            selectedTheme: value.value,
            showVariationVariable: true,
        })
    }
    };

    addVariable = (i) => {
        if (this.state.isTwoTheme) {
            const index = i == 0 ? 1 : 0;
            const changed = this.props.pd.multiTheme;
            const len = changed[i].variables.length;
            const newVariable = { value: `variable${index == 0 ? 2 : 1}-${len + 1}`, index: []}
            changed[i].variables.push(newVariable);
            const result = this.props.pd.inputs;
            
            changed[index].variables.map(item => {
                const obj = { 
                    variable:`${item.value}`,
                    secondVariable: `variable${index == 0 ? 2 : 1}-${len + 1}`,
                    sellerSku: '',
                    productId: '',
                    noIdProvide: false,
                    productIdType: '',
                    editable: true,
                    isSelected: true
                }
                const obj2 = { 
                    variable: `variable${index == 0 ? 2 : 1}-${len + 1}`,
                    secondVariable: `${item.value}`,
                    sellerSku: '',
                    productId: '',
                    noIdProvide: false,
                    productIdType: '',
                    editable: true,
                    isSelected: true
                }
                result.push(index == 0 ? obj : obj2);
                const addedItemIndex = result.indexOf(index == 0 ? obj : obj2);
                changed[i].variables[len].index.push(addedItemIndex);
                item.index.push(addedItemIndex)
            })

            let firstBoolean = true;
            let secondBoolean = true;
            if (changed[0].variables.length < 2 && changed[1].variables.length < 2) {
                firstBoolean = false;
                secondBoolean = false;
            } else if (changed[0].variables.length < 2 && changed[1].variables.length < 2) {
                firstBoolean = false;
                secondBoolean = false;
            } else if (changed[0].variables.length < 2) {
                firstBoolean = false;
            } else if (changed[1].variables.length < 2) {
                secondBoolean = false;
            };

            this.setState({ 
                firstShowMinus: firstBoolean,
                secondShowMinus: secondBoolean
            })
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
            const obj = {
                variable: `variable${counts}`,
                sellerSku: '',
                productId: '',
                noIdProvide: false,
                productIdType: '',
                editable: true,
                isSelected: true
            };
            const inputs = [...this.props.pd.inputs, obj ];
            this.props.onUpdateInputs(inputs)
        }
    }

    minusVariable = (index) => {
        if (this.state.isTwoTheme) {
            const len = this.props.pd.multiTheme[index].variables.length;
            const result = this.props.pd.inputs.filter(input => (index == 0 ? input.variable: input.secondVariable) !== this.props.pd.multiTheme[index].variables[len-1].value);

            const changed = this.props.pd.multiTheme;
            changed[index].variables.splice(len-1, 1);

            let firstBoolean = true;
            let secondBoolean = true;

            if (changed[0].variables.length < 2 && changed[1].variables.length < 2) {
                firstBoolean = false;
                secondBoolean = false;
            } else if (changed[0].variables.length < 2 && changed[1].variables.length < 2) {
                firstBoolean = false;
                secondBoolean = false;
            } else if (changed[0].variables.length < 2) {
                firstBoolean = false;
            } else if (changed[1].variables.length < 2) {
                secondBoolean = false;
            };

            this.setState({
                firstShowMinus: firstBoolean,
                secondShowMinus: secondBoolean
            })
            this.props.onUpdateInputs(result)
            this.props.onUpdateMultiTheme(changed)

        } else {
            const i = this.props.pd.inputs.length -1;
            const arr = this.props.pd.inputs;

            let showMinusBoolean = true;
            
            if (arr.length > 1) {
                arr.splice(i, 1);
            }
            if (arr.length < 2) {
                showMinusBoolean = false;
            } 
            this.setState({
                showMinus: showMinusBoolean
            })
            this.props.onUpdateInputs(arr)
        }
    }

    onMultiThemeVariableChange = (value, indexArr, param, index ) => {
        const arr = this.props.pd.multiTheme;
        arr[param].variables[index].value = value;
        const inputs = this.props.pd.inputs;

        indexArr.map( i => {
            return param == 0 ? inputs[i].variable = value : inputs[i].secondVariable = value
        })
        this.props.onUpdateInputs(inputs)
        this.props.onUpdateMultiTheme(arr)
    }

    handleProductIdTypeChange = (e, index) => {
        const arr = this.props.pd.inputs;
        arr[index].productIdType = e.value;
        this.props.onUpdateInputs(arr)
    }

    onInputsChange = (value,index,target) => {
        // console.log(value)
        const inputs = [...this.props.pd.inputs];
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
    checkInputChangedHandler = () => {
        const keyArr =  [
            { key: 'brandNameCH' , value: this.props.pd.brandNameCH },
            { key: 'brandNameEN', value: this.props.pd.brandNameEN },
            { key: 'productNameCH', value: this.props.pd.productNameCH },
            { key: 'productNameEN', value: this.props.pd.productNameEN },
            { key: 'manufacturerCH', value: this.props.pd.manufacturerCH },
            { key: 'manufacturerEN', value: this.props.pd.manufacturerEN },
        ]

        this.props.pd.inputs.map((item, index) => {
            if (item.isSelected) {
                if (item.noIdProvide != null && item.noIdProvide){
                    return keyArr.push({
                        key: `${index}-sellerSku`,
                        value: item.sellerSku
                    })
                } else {
                    return keyArr.push({
                        key: `${index}-sellerSku`,
                        value: item.sellerSku
                    }, {
                        key:  `${index}-productId`,
                        value: item.productId
                    })
                }
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
            if (!item.noIdProvide && item.isSelected) {
                return keyArr.push({
                    key: `${index}-productIdType`,
                    value: item.productIdType
                })
            }
        })
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
        console.log(arr, 'onCheckValid')
        this.props.onUpdateCheckValid(arr)
    }

    // IF NEVER CHANGE THE VAIRATION NAME INPUT
    checkVariableChanged = () => {
        if (this.state.isTwoTheme) {
            let checkResult = this.props.pd.multiTheme[0].variables.map((item,index) => {
                if (item.value == `variable1-${index+1}`) {
                    return false
                }
                return true
            })
            this.props.pd.multiTheme[1].variables.map((item, index) => {
                if (item.value == `variable2-${index+1}`) {
                    return checkResult.push(false)  
                }
                return checkResult.push(true)
            })
            const inValid = checkResult.includes(false)
            if(inValid) {
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
            if(inValid) {
                this.setState({ showVariationCheckWarning: true })
            } else {
                this.setState({ showVariationCheckWarning: false })
            }
            this.handleUpdateCheckValid('variationNameNotDefault', inValid)
        }
    }
    
    updateProduct = () => {
        this.checkInputChangedHandler()
        this.checkSelectChangeHandler()
        this.checkVariableChanged()

        const supplierId = this.props.d.u.cid;
        const skuArr = this.props.pd.inputs.map((item,index) => {
            const variableValue = this.state.isTwoTheme ? `${item.variable} & ${item.secondVariable}` : item.variable;
            const counter = index + 1; 
            let indexOfSku = 1;
            if (counter > 5) {
                indexOfSku = counter % 5 == 0 ? counter/5 : parseInt(counter/5) + 1
            }
            return Object.assign({ 
                sellerSku: item.sellerSku,
                productId: { name: 'Product ID', value: item.productId }, 
                productIdType: { name: 'Product ID Type',value: item.productIdType },
                noIdProvide: item.noIdProvide,
                variable: { name: 'Variable', value: variableValue },
                variationTheme: {name: 'Variation Theme' , value: this.state.selectedTheme},
                retailPrice: '未定價', 
                // salesVolume: 0,
                settlementsPeriodOrder: 0,
                // openPosition: 0,
                lastSevenDaysOrder: 0,
                fbaQuantity: 0,
                applying : item.applying == null ? [] : item.applying,
                selling : item.selling == null ? [] : item.selling,
                status: 'unapplied',
                actions: ['申請銷售','修改'],
                pageIndex: indexOfSku,
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

        console.log(this.props.pd)
        const currentBp = this.props.pd.products.filter(item => item._id.$oid == this.props.bpId)
        console.log('currentBp', currentBp)

        const bpObj = Object.assign({}, currentBp[0], {
            // bpId: this.props.bpId,
            supplierId: supplierId,
            category: this.state.currentCategory,
            brandNameCH: this.props.pd.brandNameCH,
            brandNameEN: this.props.pd.brandNameEN,
            productNameCH: this.props.pd.productNameCH,
            productNameEN: this.props.pd.productNameEN,
            manufacturerCH: this.props.pd.manufacturerCH,
            manufacturerEN: this.props.pd.manufacturerEN,
            variationTheme: this.state.selectedTheme,
            totalSkus: skuArr.length,
            // pageSize: 5,
            // currentIndex: this.state.currentIndex,
            multiTheme: this.props.pd.multiTheme,
            // bpStatus: 'unapplied',
            skus : skuArr
        })

        // const bpObj ={
        //     bpId: this.props.bpId,
        //     supplierId: supplierId,
        //     category: this.state.currentCategory,
        //     brandNameCH: this.props.pd.brandNameCH,
        //     brandNameEN: this.props.pd.brandNameEN,
        //     productNameCH: this.props.pd.productNameCH,
        //     productNameEN: this.props.pd.productNameEN,
        //     variationTheme: this.state.selectedTheme,
        //     totalSkus: skuArr.length,
        //     pageSize: 5,
        //     currentIndex: this.state.currentIndex,
        //     multiTheme: this.props.pd.multiTheme,
        //     bpStatus: 'unapplied',
        //     skus : skuArr
        // }

        console.log('update bpobj', bpObj)

        const valid = this.props.pd.checkValid.map(item => {
            return item.valid
        })
        if (valid.includes(false)) {
            console.log(valid.includes(false), 'valid includes false')
            this.setState({ showNotice: true })
        } else {
            const message = <FormattedMessage id="updateproduct.updated"/>
            this.props.onUpdateProduct(supplierId, this.props.bpId, bpObj,message)
            this.setState({ showNotice: false })
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

    render () {
        return (
            <div id="update-product" className="drs-modal-container">
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
                                    type='updateProduct'
                                    bpId={this.props.bpId}
                                    category={this.state.currentProduct.category}
                                    themeOptions={this.state.themeOptions}
                                    product={this.state.currentProduct}
                                    isTwoTheme={this.state.isTwoTheme}
                                    selectedTheme={this.state.selectedTheme}
                                    showVariationVariable={true}
                                    addVariable={this.addVariable}
                                    minusVariable={this.minusVariable}
                                    onInputsChange={this.onInputsChange}
                                    onMultiThemeVariableChange={this.onMultiThemeVariableChange}
                                    firstShowMinus={this.state.firstShowMinus}
                                    secondShowMinus={this.state.secondShowMinus}
                                    showMinus={this.state.showMinus}
                                    handleThemeChange={this.handleThemeChange}
                                    onSelect={this.onSelect}
                                    productIdTypeOptions={this.state.productIdTypeOptions}
                                    handleProductIdTypeChange={this.handleProductIdTypeChange}
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
                                    <button
                                        className="drs-btn drs-btn-normal"
                                        onClick={() => {this.updateProduct()}}
                                    >確認修改</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(UpdateProduct)