import React, { Component } from 'react';
import Category from './Category';
import StatusBar from '../common/StatusBar';
import BaseData from './BaseData';
import { connect } from 'react-redux';
import { addNewProduct, updateMultiTheme, updateInputs, updateCheckValid, 
    onBrandNameChChange, onBrandNameEnChange, onProductNameChChange, onProductNameEnChange, onManufacturerChChange, onManufacturerEnChange} from '../../../actions/index';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import {FormattedMessage} from 'react-intl';
import Modal from '@material-ui/core/Modal';

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
]

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
        onAddNewProduct: (supplierId, obj, message) => dispatch(addNewProduct(supplierId, obj, message)),
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

class AddNewProduct extends Component {
    constructor (props) {
        super()
        this.state = {
            show: 'category',
            isPreviousPageExist: false,
            isNextPageExist: true,
            currentCategory: 'A',
            themeOptions: productCategory[0].theme,
            steps: [
                { name: <FormattedMessage id="addnewproduct.selsct"/>,
                  state: 'active'
                },{
                  name: <FormattedMessage id="addnewproduct.build"/>,
                  state: ''
                },{
                  name: <FormattedMessage id="addnewproduct.complete"/>,
                  state: ''
                },
            ],
            selectedTheme: 'Select...',
            showVariationVariable: false,
            isTwoTheme: false,
            firstShowMinus: true,
            secondShowMinus: true,
            showMinus: true,
            productIdTypeOptions: productIdTypeOptions,
            showNotice: false,
            categoryShowNext: false,
            showVariationCheckWarning: false,
            test: false,
            open: false,
            more: true
        }
    }

    componentDidMount = () => {
        this.props.onPDNameChChangeHandler('')
        this.props.onPDNameEnChangeHandler('')
        this.props.onBDNameChChangeHandler('')
        this.props.onBDNameEnChangeHandler('')
        this.props.onManuChChangeHandler('')
        this.props.onManuEnChangeHandler('')
    }

    showNoticeChange = () => {
        this.setState({ showNotice: false })
    }

    handleThemeChange = (value) => {
        if( value.value.includes('&') ) {
            const str = value.value.split(' & ');
            const str1 = str[0];
            const str2 = str[1];
            const multiTheme = [
                {theme: str1, variables: [{ value: 'vairable1-1', index: [0,1] }, { value: 'variable1-2', index: [2,3] }]}, 
                {theme: str2, variables: [{ value: 'variable2-1', index: [0,2] }, { value: 'variable2-2', index: [1,3] }]}
            ];
            const inputs = [
                // { variable: 'variable1-1', secondVariable: 'variable2-1', sellerSku: '', productId: '', productIdType: '', HScode: '', price: '', quantity: '' },
                { variable: 'variable1-1', secondVariable: 'variable2-1', sellerSku: '', productId: '', noIdProvide: false, productIdType: '', editable: true, isSelected: true },
                { variable: 'variable1-1', secondVariable: 'variable2-2', sellerSku: '', productId: '', noIdProvide: false, productIdType: '', editable: true, isSelected: true },
                { variable: 'variable1-2', secondVariable: 'variable2-1', sellerSku: '', productId: '', noIdProvide: false, productIdType: '', editable: true, isSelected: true },
                { variable: 'variable1-2', secondVariable: 'variable2-2', sellerSku: '', productId: '', noIdProvide: false, productIdType: '', editable: true, isSelected: true },
            ]
            this.setState({
                isTwoTheme: true,
            })
            this.props.onUpdateMultiTheme(multiTheme);
            this.props.onUpdateInputs(inputs)
        } else {
            this.setState({   
                isTwoTheme: false,
            });
            const inputs = [
                { variable: 'variable1', sellerSku: '', productId: '', noIdProvide: false, productIdType: '', editable: true, isSelected: true }, 
                { variable: 'variable2', sellerSku: '', productId: '', noIdProvide: false, productIdType: '', editable: true, isSelected: true }
            ];
            this.props.onUpdateInputs(inputs)
        }
        this.setState({
            selectedTheme: value.value,
            showVariationVariable: true,
            showNotice: false,
        })        
    };

    addVariable = (i) => {

        if (this.state.isTwoTheme) {
            const index = i == 0 ? 1 : 0;
            const changed = this.props.pd.multiTheme;
            const len = changed[i].variables.length;
            const newVariable = { value: `variable${index == 0 ? 2 : 1}-${len + 1}`, index: [] }
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
        console.log(arr, 'this.props.pd.inputs')
        arr[index].productIdType = e.value;
        this.setState({ showNotice: false })
        this.props.onUpdateInputs(arr)
    }

    onInputsChange = (value,index,target) => {
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

    
// NEED TO REFACTOR
    checkInputChangedHandler = () => {
        const keyArr =  [
            { key: 'brandNameCH' , value: this.props.pd.brandNameCH },
            { key: 'brandNameEN', value: this.props.pd.brandNameEN },
            { key: 'productNameCH', value: this.props.pd.productNameCH },
            { key: 'productNameEN', value: this.props.pd.productNameEN },
            { key: 'ManufacturerCH', value: this.props.pd.manufacturerCH },
            { key: 'ManufacturerEN', value: this.props.pd.manufacturerEN },
        ]

        this.props.pd.inputs.map((item, index) => {
            if (item.isSelected == null || item.isSelected) {
                if (item.noIdProvide != null && item.noIdProvide){
                    return keyArr.push({
                        key: `${index}-sellerSku`,
                        value: item.sellerSku
                    }, {
                        key:  `${index}-productId`,
                        value: 'productId'
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

//        const arr = this.props.pd.checkValid;
        const arr = []

        keyArr.map(key => {
            if (key.key != 'brandNameCH' && key.value == '') {
                const obj = {key: key.key, valid: false }
                const target = this.props.pd.checkValid.filter(item => item.key == key.key)
                return arr.push(obj)
//                if (target.length == 0) {
//                    // console.log( 'already existed')
//                    return arr.push(obj)
//                } else {
//                    const index = this.props.pd.checkValid.indexOf(target[0])
//                    console.log(index)
//                    return arr[index] = obj;
            } else {
                const obj = {key: key.key, valid: true }
                const target = this.props.pd.checkValid.filter(item => item.key == key.key)
                return arr.push(obj)
//                if (target.length == 0) {
//                    // console.log( 'already existed')
//                    return arr.push(obj)
//                } else {
//                    const index = this.props.pd.checkValid.indexOf(target[0])
//                    console.log(index)
//                    return arr[index] = obj;
//                }
            }
        })
        console.log(arr, 1111111)
        const valid = arr.map(item => {
            return item.valid
        })
        if (valid.includes(false)) {
            return false
        } else {
            return true
        }
        
        this.props.onUpdateCheckValid(arr)
    }

    checkSelectChangeHandler = () => {
        const keyArr = [
           { key: 'variationTheme', value: this.state.selectedTheme }
        ]
        
        this.props.pd.inputs.map((item, index) => {
            if (item.isSelected == null || item.isSelected) {
                if (!item.noIdProvide) {
                    return keyArr.push({
                        key: `${index}-productIdType`,
                        value: item.productIdType
                    })
                } else {
                    return keyArr.push({
                        key: `${index}-productIdType`,
                        value: 'productIdType'
                    })
                }
            }
        })
//        const arr = this.props.pd.checkValid
        const arr = []

        keyArr.map(key => {
            if (key.value == 'Select...'|| key.value == '') {
                const obj = { key: key.key, valid: false }
                const target = this.props.pd.checkValid.filter(item => item.key == key.key)
                return arr.push(obj)
//                if (target.length == 0) {
//                    return arr.push(obj)
//                } else {
//                    const index = this.props.pd.checkValid.indexOf(target[0])
//                    return arr[index] = obj
//                }
            } else {
                const obj = { key: key.key, valid: true }
                const target = this.props.pd.checkValid.filter(item => item.key == key.key)
                return arr.push(obj)
//                if (target.length == 0) {
//                    return arr.push(obj)
//                } else {
//                    const index = this.props.pd.checkValid.indexOf(target[0])
//                    return arr[index] = obj
//                }
            }
        })
        const valid = arr.map(item => {
            return item.valid
        })
        if (valid.includes(false)) {
            return false
        } else {
            return true
        }


        this.props.onUpdateCheckValid(arr)
    }

    handleUpdateCheckValid = (id, inValid) => {
        // console.log(id, inValid)
//        const arr = this.props.pd.checkValid;
        const arr = []
        const target = this.props.pd.checkValid.filter(item => item.key == id)
        let obj = {}

        if (inValid) {
            obj = {key: id, valid: false}
            arr.push(obj)
//            if (target.length == 0) {
//                arr.push(obj)
//            } else {
//                const index = this.props.pd.checkValid.indexOf(target[0])
//                arr[index] = obj;
//            }
        } else {
            obj = {key: id, valid: true}
            arr.push(obj)
//            if (target.length == 0) {
//                arr.push(obj)
//            } else {
//                const index = this.props.pd.checkValid.indexOf(target[0])
//                arr[index] = obj;
//            }
        }
        console.log(arr, 'onCheckValid')
        const valid = arr.map(item => {
            return item.valid
        })
        if (valid.includes(false)) {
            return false
        } else {
            return true
        }
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
                return false
            } else {
                this.setState({ showVariationCheckWarning: false })
                return true
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
                return false
            } else {
                this.setState({ showVariationCheckWarning: false })
                return true
            }
            this.handleUpdateCheckValid('variationNameNotDefault', inValid)
        }
    }

    createProduct = () => {
        this.checkInputChangedHandler()
        this.checkSelectChangeHandler()
        this.checkVariableChanged()
        
        const supplierId = this.props.d.u.cid;
        const dataArr = this.props.pd.inputs.map((item,index) => {
            const variableValue = this.state.isTwoTheme ? `${item.variable} & ${item.secondVariable}` : item.variable;
            const counter = index + 1; 
            let indexOfSku = 1;
            if (counter > 5) {
                indexOfSku = counter % 5 == 0 ? counter/5 : parseInt(counter/5) + 1
            }
            return Object.assign({ 
                sellerSku: item.sellerSku,
                productId: { name: 'Product ID', value: item.productId }, 
                productIdType: { name: 'Product ID Type', value: item.productIdType },
                noIdProvide: item.noIdProvide,
                variationTheme: { name: 'Variation Theme', value: this.state.selectedTheme},
                variable: { name: 'Variable', value: variableValue },
                retailPrice: '未定價', 
                // salesVolume: 0,
                settlementsPeriodOrder: 0,
                // openPosition: 0,
                lastSevenDaysOrder: 0,
                fbaQuantity: 0,
                applying : [],
                selling : [],
                status: '',
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


        const bpObj = {
            supplierId: supplierId,
            category: this.props.pd.currentCategory,
            brandNameCH: this.props.pd.brandNameCH,
            brandNameEN: this.props.pd.brandNameEN,
            productNameCH: this.props.pd.productNameCH,
            productNameEN: this.props.pd.productNameEN,
            manufacturerCH: this.props.pd.manufacturerCH,
            manufacturerEN: this.props.pd.manufacturerEN,
            variationTheme: this.state.selectedTheme,
            totalSkus: dataArr.length,
            pageSize: 5,
            multiTheme: this.props.pd.multiTheme,
            bpStatus: 'unapplied',
            skus : dataArr
        }

        const valid = this.props.pd.checkValid.map(item => {
            return item.valid
        })
        console.log(this.checkInputChangedHandler())
        console.log(this.checkSelectChangeHandler())
        console.log(this.checkVariableChanged())
        if (!this.checkInputChangedHandler() || !this.checkSelectChangeHandler() || !this.checkVariableChanged()) {
            this.setState({ showNotice: true })
        } else {
            const message = <FormattedMessage id="addnewproduct.created"/>
            this.props.onAddNewProduct(supplierId, bpObj, message)
            this.goToNextPage(this.state.show)
            this.setState({ showNotice: false })
        }
        
    }

    updateSteps = (index,forward) => {
        const arr = [...this.state.steps];
        const obj = arr[index];
        if (forward) {
            obj.state = 'active';
        } else {
            obj.state = '';
        }
        arr.splice(index,1, obj);
        this.setState({
            steps: arr
        })
    }

    goToNextPage(show) {
        if (show == 'category') {
            this.setState({ 
                show: 'basedata',
                isPreviousPageExist: true
            })
            this.updateSteps( 1, true)
        } else if (show == 'basedata') {
            this.setState({ 
                show: 'complete',
                isPreviousPageExist: false,
                isNextPageExist: false,
                showNotice: false
            })
            this.updateSteps( 2, true)
        }
    }

    goToPrePage(show) {
        if (show == 'basedata') {
            this.setState({
                show: 'category',
                isPreviousPageExist: false,
                showNotice: false
            })
            this.updateSteps(1, false);
        } else if (show == 'complete') {
            this.setState({
                isPreviousPageExist: false,
                showNotice: false
            })
        }
    }

    onSelect = (index) => {
        const inputs = this.props.pd.inputs;
        if (inputs[index].isSelected) {
            inputs[index].isSelected = false;
        } else {
            inputs[index].isSelected = true;
        }
        this.props.onUpdateInputs(inputs)
    }

    updateShow = (param) => {
        console.log(param)
        this.setState({ categoryShowNext: param })
    }


    handleOpen = () => {
      this.setState({open: true});
    };
  
    handleClose = () => {
        this.setState({open: false});
    };
  
    
    learnMoreModal = (
        
        <div
        style={{top: '50%' , left : '50%' , position : 'absolute' ,
        transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
        borderRadius: '4px', padding : '2%'}}>
         <div className="modal-close-wrapper" style={{position: 'absolute',top: '-10%', left: '0%'}}>
                        <button className="modal-close-btn" onClick={() => this.handleClose()}>
                            <img src={closeIcon} alt="closeIcon" style={{width: '14px'}}/>
                            <span>
                                <FormattedMessage id="addnewproduct.close"/>
                            </span>
                        </button>
         </div>    
        <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700'}} ><FormattedMessage id="basedata.reminderbrandname" /></p>
          <p style={{lineHeight : '1.5rem'}}>
           <FormattedMessage id="basedata.remindersubtitle1" />
           <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
            <li><FormattedMessage id="basedata.remindersubtitle1-1" /></li>
            <li><FormattedMessage id="basedata.remindersubtitle1-2" /></li>
            <li><FormattedMessage id="basedata.remindersubtitle1-3" /></li>
            <li><FormattedMessage id="basedata.remindersubtitle1-4" /></li>
           </ul>
          </p>
        </div>
    );

    handleModalClose = () => {
        this.props.onClose(false)
        this.props.onUpdateInputs([])
    }
    
    
    render() {
        return(
            <div id="addNewProduct" className="drs-modal-container">
            <div className="drs-modal">
                <div className="app-sub-pannel">
                    <div className="modal-close-wrapper">
                        <button className="modal-close-btn" onClick={() => this.handleModalClose()}>
                            <img src={closeIcon} alt="closeIcon" style={{width: '14px'}}/>
                            <span>
                                <FormattedMessage id="addnewproduct.close"/>
                            </span>
                        </button>
                    </div>
                    <StatusBar steps={this.state.steps}/>

                    <div className="operation-pannel">
                        <div className="operation-pannel-main">
                            {this.state.show == 'category'
                                ?  <Category updateShow={this.updateShow}/>
                                :  <div></div>
                            }

                        
                            {this.state.show == 'basedata'
                                ?  <div style={{width: '100%'}}>
                                    <div className="base-reminder-wrapper">
                                        <p className="base-reminder">
                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                                        <FormattedMessage id ="basedata.brandnamereminder"></FormattedMessage>
                                        {/* <button >open</button> */}
                                        <span style={{color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{this.handleOpen()}}>
                                            <FormattedMessage id ="basedata.learnmore"></FormattedMessage>    
                                        </span>
                                        </p>
                                    </div>
                                
                                    { this.state.more
                                        ?  <Modal
                                            open={this.state.open}
                                            onClose={this.handleClose}
                                            aria-labelledby="simple-modal-title"
                                            aria-describedby="simple-modal-description"
                                        >
                                          {this.learnMoreModal}
                                        </Modal> 
                                        :  null
                                    }
                                    <BaseData
                                        type='addNewProduct'
                                        category={this.props.pd.currentCategory}
                                        themeOptions={this.state.themeOptions}
                                        isTwoTheme={this.state.isTwoTheme}
                                        onMultiThemeVariableChange={this.onMultiThemeVariableChange}
                                        handleThemeChange={this.handleThemeChange}
                                        showNoticeChange={this.showNoticeChange}
                                        addVariable={this.addVariable}
                                        minusVariable={this.minusVariable}
                                        selectedTheme={this.state.selectedTheme}
                                        showVariationVariable={this.state.showVariationVariable}
                                        onInputsChange={this.onInputsChange}
                                        firstShowMinus={this.state.firstShowMinus}
                                        secondShowMinus={this.state.secondShowMinus}
                                        showMinus={this.state.showMinus}
                                        handleCheckboxChange={this.handleCheckboxChange}
                                        onSelect={this.onSelect}
                                        productIdTypeOptions={this.state.productIdTypeOptions}
                                        handleProductIdTypeChange={this.handleProductIdTypeChange}
                                        bpId={this.props.bpId}
                                    />
                                    </div>
                                :  <div></div>
                            }
                            
                            {this.state.show == 'complete'
                                ?   <div className="complete-board">
                                        <p><FormattedMessage id="addnewproduct.congratulations"/></p>
                                        <p><FormattedMessage id="addnewproduct.text1"/></p>
                                        <p><FormattedMessage id="addnewproduct.text2"/></p>
                                    </div>
                                :   <div></div>
                            }
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
                                {this.state.isPreviousPageExist
                                    ?   <button
                                            className="drs-btn drs-btn-normal"
                                            onClick={()=>{this.goToPrePage(this.state.show)}}
                                        >
                                            <FormattedMessage id="addnewproduct.reselectcategory"/>
                                        </button>
                                    :   <div></div>
                                }
                                {this.state.show == 'category'
                                    ?   <button
                                            className="drs-btn drs-btn-normal"
                                            onClick={()=>{
                                                // this.updateProduct();
                                                // {this.state.show == 'category' ? this.goToNextPage(this.state.show): null}
                                                {this.state.categoryShowNext == 'next' ? this.goToNextPage(this.state.show): null}
                                                //{this.state.show == 'basedata' ? this.createProduct() : null}
                                            }}
                                        >
                                            <span>
                                                {this.state.show == 'category'
                                                    ?   <FormattedMessage id="addnewproduct.confirmcategory"/>
                                                    :   <FormattedMessage id="addnewproduct.confirmestablishment"/>
                                                }
                                            </span>
                                        </button>
                                    :  <div></div>
                                }
                                {this.state.show == 'basedata'
                                    ?   <button
                                            className="drs-btn drs-btn-normal"
                                            onClick={()=>{
                                                // this.updateProduct();
                                                // {this.state.show == 'category' ? this.goToNextPage(this.state.show): null}
                                                //{this.state.categoryShowNext == 'next' ? this.goToNextPage(this.state.show): null}
                                                {this.state.show == 'basedata' ? this.createProduct() : null}
                                            }}
                                        >
                                            <span><FormattedMessage id="addnewproduct.confirmestablishment"/></span>
                                        </button>
                                    :  <div></div>
                                }
                                {this.state.show == 'complete'
                                    ?   <button className="drs-btn drs-btn-normal" onClick={() => {this.handleModalClose()}}>
                                            <FormattedMessage id="addnewproduct.confirm"/> 
                                        </button>
                                    : <div></div>
                                }
                            </div>
                        </div>
                    </div>
                </div>
                </div>
            </div>
        )
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(AddNewProduct);
