import React, { Component } from 'react';
import { FormattedMessage } from 'react-intl';
import { connect } from 'react-redux';
import CheckBox from './Checkbox/CheckBox';
import InputField from './InputField';
import SelectField from './SelectField';
import { Tooltip } from '@material-ui/core';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import Modal from '@material-ui/core/Modal';

const mapStateToProps = (state) => {
    return {
        productState: state.PD,
    }
}
const mapDispatchToProps = (dispatch) => {
    return {}
}
    
class Variation extends Component {
    constructor() {
        super();
        this.state = {
            productIDNote: false
        }
    }

    handleProductIdNote = (boolean) => {
        this.setState({productIDNote: boolean});
    }

    learnMoreModal = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
            borderRadius: '4px', padding : '2%'}}>
            <div className="modal-close-wrapper" style={{position: 'absolute',top: '-10%', left: '0%'}}>
                <button className="modal-close-btn" onClick={() => this.handleProductIdNote(false)}>
                    <img src={closeIcon} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                </button>
            </div>
        <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700'}} >Product ID</p>
          <p style={{lineHeight : '1.5rem'}}>
           <p style={{marginTop:'2%'}}><FormattedMessage id="addnewsku.reminderproductidnote1" /></p>
           <p style={{marginTop:'2%'}}><FormattedMessage id="addnewsku.reminderproductidnote2" /></p>
           <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
            <p style={{marginLeft:'2%'}}><FormattedMessage id="addnewsku.reminderproductidnote2-1" /></p>
            <p style={{marginTop:'2%',marginLeft:'2%'}}><FormattedMessage id="addnewsku.reminderproductidnote2-2" /></p>
           </ul>
          </p>
        </div>


    );
    
    // noidOnSelect = (item) => {
    //     console.log(item)
    //     if (item.noIdProvide == null) {
    //         item.noIdProvide = true
    //         item.productId = ''
    //     } else {
    //         if (item.noIdProvide) {
    //             item.noIdProvide = false
    //         } else {
    //             item.noIdProvide = true
    //             item.productId =''
    //         }
    //     }
    //     console.log(item)
    // }

    render () {
        return (
            <div className="variation-section">
                <div className="base-multi-input-wrapper multiple-rows" style={{marginBottom: '1%'}}>
                    <div className="base-input-label-wrapper">
                        <p className="base-input-label">{this.props.selectedTheme}</p>
                    </div>
                    <div className="base-input-small-wrapper">
                        { this.props.productState.inputs.map((item, index) => {
                            if (item.editable) {
                                return (
                                    <div className="base-input-container">
                                        <InputField
                                            id={`${index}-variable`}
                                            key={`${index}-variable`}
                                            value={item.variable}
                                            placeHolder=''
                                            onChange={this.props.onInputsChange}
                                            onBlur={this.props.onBlurCheckValid}
                                            type='variable'
                                            index={index}
                                            rule={['maxLen20', 'english', 'variable','variationname']}
                                            defaultValue={`variable${index+1}`}
                                            onCheckValue={this.props.onCheckVariationVariable}
                                            onCheckDefault={this.props.onCheckDefault}
                                            onCheckRequire={this.props.onCheckRequire}
                                            inValid={this.props.handleValid('variable')}
                                        />
                                    </div>
                                )
                            } else {
                                return (
                                    <div className="base-input-container">
                                        <input 
                                            className="base-input"
                                            placeholder={item.variable}
                                            disabled
                                        ></input>
                                    </div>
                                )
                            }
                        })}
                        <button
                            className="base-input-inline-btn"
                            onClick={() => this.props.addVariable()}
                        > Add </button>
                        {   this.props.showMinus
                            ?   <button
                                    className="base-input-inline-btn"
                                    onClick={() => this.props.minusVariable()}
                                > Delete </button>
                            :   <div></div>
                        }
                    </div>
                </div>
                <div className="base-sku-list-wrapper column-wrapper">
                { this.props.productState.inputs.map((item,index) => {
                    return (
                        <div className="base-sku-list">
                            <div className="base-sku-list-column" style={{'marginRight': '12px'}}>
                                <p className="base-sku-list-column-title">{this.props.selectedTheme}</p>
                                <p className="base-sku-list-column-text">{item.variable}</p>
                            </div>
                            <div className="base-sku-list-column">
                                <p className="base-sku-list-column-title" style={{display: 'flex',justifyContent: 'flex-end'}}>
                                    Seller SKU
                                    <Tooltip title={
                                        <div style={{fontSize: '15px', lineHeight : '1.5rem'}}>
                                            <FormattedMessage  id="addnewsku.sellerskunote"/>
                                        </div>
                                        } arrow placement="bottom">
                                        <span>
                                            <i class="fa fa-question-circle" style={{fontSize: '18px', marginLeft: '5px'}}></i>
                                        </span>
                                    </Tooltip>
                                </p>
                                { item.editable
                                    ?   <InputField
                                            status={this.props.status}
                                            id={`${this.props.bpId}-${index}-sellerSku`}
                                            key={`${this.props.bpId}-${index}-sellerSku`}
                                            value={item.sellerSku}
                                            placeHolder='Seller SKU'
                                            onChange={this.props.onInputsChange}
                                            onBlur={this.props.onBlurCheckValid}
                                            onCheckSameValue={this.props.onCheckSameValue}
                                            index={index}
                                            type='sellersku'
                                            rule={['maxLen20', 'sellersku', 'uniqueSku']}
                                            bpId={this.props.bpId}
                                            inValid={this.props.handleValid('sku')}
                                        />
                                    :  <input
                                            className="base-input base-sku-list-input"
                                            value={item.sellerSku}
                                            placeholder={item.sellerSku == '' ? 'Seller SKU' : item.sellerSku}
                                            disabled
                                        ></input>
                                }
                            </div>
                            <div className="base-sku-list-column">
                                <p className="base-sku-list-column-title">
                                    Product ID
                                    <Tooltip title={
                                        <div style={{fontSize: '15px', lineHeight : '1.5rem'}}>
                                            <FormattedMessage  id="addnewsku.productidnote"/>
                                            <span style={{color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{this.handleProductIdNote(true)}}>
                                                <FormattedMessage id ="basedata.learnmore"/>
                                            </span>
                                        </div>
                                        } arrow interactive placement="bottom">
                                        <span>
                                            <i class="fa fa-question-circle" style={{fontSize: '18px', marginLeft: '5px'}}></i>
                                        </span>
                                    </Tooltip>
                                </p>

                                <Modal
                                    open={this.state.productIDNote}
                                    onClose={this.handleProductIDNoteClose}
                                    aria-labelledby="simple-modal-title"
                                    aria-describedby="simple-modal-description"
                                >
                                  {this.learnMoreModal}
                                </Modal>
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
                                            rule={['maxLen20', 'integer','productId']}
                                            index={index}
                                            type='productid'
                                            item={item}
                                            disabled={item.noIdProvide}
                                            bpId={this.props.bpId}
                                            inValid={this.props.handleValid('pid')}                  
                                        />
                                    :   <input
                                            className="base-input base-sku-list-input"
                                            value={item.productId}
                                            placeholder={item.productId == '' ? '由 DRS 提供': item.productId}
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
                                    ?   <SelectField
                                            className="base-selector-l"
                                            value={item.productIdType}
                                            onChange={this.props.handleProductIdTypeChange}
                                            placeHolder={item.productIdType == '' ? 'Select...' : item.productIdType}
                                            options={this.props.productIdTypeOptions}
                                            index={index}
                                            isDisabled={item.noIdProvide}
                                        />
                                    : <p>{item.productIdType}</p>
                                }
                            </div>
                        </div>
                        )
                    }
                )}
            </div>
        </div>
        )
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Variation);