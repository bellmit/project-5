import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import axios from 'axios';
import Select from 'react-select';
import toaster from "toasted-notes";
import Swal from 'sweetalert2';
// import Swal from 'sweetalert2/dist/sweetalert2.js';
import "toasted-notes/src/styles.css"; 
import '../../css/VerifyProductInfo.css';
import '../../css/VerifyProductInfoV2.css';
import DocStatus from './DocStatus.js';
import BtnGroup from './BtnGroup.js';
import Skeleton from './SkeletonV2.js';
import {DOMAIN_NAME} from '../../constants/action-types'

class VerifyProductInfo extends Component {
    constructor() {
        super();
        this.state = {
            isAm: true,
            isLoading: true,
            hasDoc: [false, false, false, false, true, false, false, false, false],
            checkbox: [
                { id: 'G3 Invoice', isChecked: false }, 
                { id: 'MSDS', isChecked: false },
                { id: 'Dangerous Good', isChecked: false },
                { id: 'UN38.3', isChecked: false },
                { id: 'PPQ505', isChecked: false },
                { id: 'US FDA', isChecked: false },
                { id: 'US MID', isChecked: false },
                { id: '油脂含量聲明書', isChecked: false },
                { id: '出口稅則要求', isChecked: false }
            ],
            currentIvs: '編號',
            ivsOptions: [],
            currentSku: 'SKU#',
            skuOptions: [],
            currentBox: '箱號',
            boxOptions: [],
            targetData: {},
            baseUrl: DOMAIN_NAME
        };

        this.initLoad = this.initLoad.bind(this);
        this.onSelectSkus = this.onSelectSkus.bind(this);
        this.onSelectIvs = this.onSelectIvs.bind(this);
        this.onSelectBox = this.onSelectBox.bind(this);
        this.changeCheckboxState = this.changeCheckboxState.bind(this);
        this.onClickConfirmBtn = this.onClickConfirmBtn.bind(this);
        this.onClickRejectBtn = this.onClickRejectBtn.bind(this);
        this.onClickInitVerifyBtn = this.onClickInitVerifyBtn.bind(this);
        this.onClickInitVerifyBtn = this.onClickInitVerifyBtn.bind(this);
    }
    
    initLoad() {
        axios.post(this.state.baseUrl + '/ivs/vp/user', {
            skuCode: 13
        })
        .then(res => {
            // console.log(res.data);
            this.setState({ 
                isAm: res.data.isAm
            })
        });

        axios.post(this.state.baseUrl + '/ivs/vp/ivs', {
            skuCode: 12
        })
        .then(res => {           
            // console.log(res.data);
            
            // for temporary useage  
            const initIvs = {value: 'IVS-K598-35'};
            const initSku = {value: 'K598-ISE1-8-4T'};
            const initBox = {value: '1-1'};          
            
            const ivsOptionsList = res.data.map(item => {
                return Object.assign({}, { value: item, label: item });
            })

            this.setState({
                ivsOptions: ivsOptionsList,
                currentIvs: initIvs.value
            })

            setTimeout(() => { 
                this.onSelectIvs(initIvs);
                this.onSelectSkus(initSku);
                this.onSelectBox(initBox); 
            }, 1000);

            // this.onSelectIvs(initIvs);
            // this.onSelectSkus(initSku);
            // this.onSelectBox(initBox);
        })
    };

    onSelectIvs(selectedIvs) {
        axios.post(this.state.baseUrl + '/ivs/vp/sku', {
            ivsCode: selectedIvs.value
        })
        .then(res => {
            // console.log(res.data); 
            
            const skuOptionsList = res.data.map(item => {
                return Object.assign({}, { value: item, label: item });
            });

            this.setState({
                skuOptions: skuOptionsList,
                currentSku: skuOptionsList[0].value
            })
        })
}

    onSelectSkus(selectedSku) {
        axios.post(this.state.baseUrl + '/ivs/vp/box', {
            skuCode: selectedSku.value
        })
        .then(res => {
            // console.log(res.data);

            const boxOptionsList = res.data.map(item => {
                return Object.assign({}, { value: item, label: item });
            });

            this.setState({
                currentSku: selectedSku.value,
                boxOptions: boxOptionsList
            })
        })
        .catch(err => {
            Swal.fire({
                title: 'Error',
                text: 'No data!',
                icon: 'error',
                confirmButtonColor: '#A5B4BF',
            });
            console.log(err);
        });
    }

    onSelectBox(selectedBox) {
        axios.post(this.state.baseUrl + '/ivs/vp/data', {
            boxCode: selectedBox.value
        })
        .then(res => {
            // console.log('on select box');
            // console.log(res.data);
            this.setState({
                isLoading: true
            });

            this.setState({
                // isLoading: false,
                currentBox: selectedBox.value,
                targetData: res.data
            })
        })
        .then(() => {
            // for test
            setTimeout(() => {
                this.setState({
                    isLoading: false
                })
            }, 1000);
        })
        .catch(err => {
            Swal.fire({
                title: 'Error',
                text: 'No data!',
                icon: 'error',
                confirmButtonColor: '#A5B4BF',
            });
            console.log(err);
        });
    }

    changeCheckboxState(obj, index) {
        this.setState({
            checkbox: [
                ...this.state.checkbox.slice(0, index),
                Object.assign({}, this.state.checkbox[index], obj),
                ...this.state.checkbox.slice(index + 1)
            ]
        })
    };

    onClickConfirmBtn () {
        Swal.fire({
            title: 'Are you sure you want to confirm this SKU?',
            text: "Won't be able to revert this!",
            icon: '',
            showCancelButton: true,
            // confirmButtonColor: '#2194ED',
            confirmButtonColor: '#7ed6a5',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: 'Yes, Confirm it!'
          }).then((result) => {
            
            const fullData = Object.assign({}, this.state.targetData);
            // console.log(fullData);
            fullData.checkbox = this.state.checkbox;
            // console.log(fullData);

            if (result.value) {
                console.log('it is work!');
                axios.post(this.state.baseUrl + '/ivs/vp/confirm', {
                    skuData: fullData
                })
                .then(res => {
                    Swal.fire({
                        title: 'Confirm Succeed',
                        icon: 'success',
                        confirmButtonColor: '#7ed6a5',
                    });        
                    console.log(res.data);
                    toaster.notify(
                        <div className="alert-with-icon toaster-success-message">
                            <span data-notify="icon" className="nc-icon nc-bell-55"></span>
                            <span className="toaster-text">
                                <b>Confirm Success - </b>
                                <span>{res.data}</span>
                            </span>
                        </div>, {
                        position: "bottom-right",
                        duration: 5000
                    });
                })
                .catch(err => {
                    Swal.fire({
                        title: 'Error',
                        text: 'Oops, something went wrong!',
                        icon: 'error',
                        confirmButtonColor: '#A5B4BF',
                    });
                    console.log(err);
                });
            } else {
                toaster.notify(
                    <div className="alert-with-icon toaster-cancel-message">
                        <span data-notify="icon" className="nc-icon nc-bell-55"></span>
                        <span className="toaster-text">
                            <b>Confirm Canceled - </b>
                            <span>You canceled to confirm</span>
                        </span>
                    </div>, {
                    position: "bottom-right",
                    duration: 5000
                });
            }
          }) 
    }

    onClickRejectBtn () {
        Swal.fire({
            title: 'Are you sure you want to reject this SKU?',
            text: "Won't be able to revert this!",
            icon: '',
            showCancelButton: true,
            // confirmButtonColor: '#2194ED',
            confirmButtonColor: '#66615B',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: 'Yes, Reject it!'
          }).then((result) => {
            if (result.value) {
                axios.post(this.state.baseUrl + '/ivs/vp/reject', {
                    skuCode: this.state.currentSku
                })
                .then(res => {
                    // console.log(res.data);
                    Swal.fire({
                        title: 'Reject Succeed',
                        icon: 'success',
                        confirmButtonColor: '#66615B',
                    });
                    toaster.notify(
                        <div className="alert-with-icon toaster-reject-message">
                            <span data-notify="icon" className="nc-icon nc-bell-55"></span>
                            <span className="toaster-text">
                                <b>Reject Success - </b>
                                <span>{res.data}</span>
                            </span>
                        </div>,  {
                        position: "bottom-right",
                        duration: 5000
                    });
                })
                .catch(err => {
                    Swal.fire({
                        title: 'Error',
                        text: 'Oops, something went wrong!',
                        icon: 'error',
                        confirmButtonColor: '#A5B4BF',
                    });
                    console.log(err);
                });
            } else {
                toaster.notify(
                    <div className="alert-with-icon toaster-cancel-message">
                        <span data-notify="icon" className="nc-icon nc-bell-55"></span>
                        <span className="toaster-text">
                            <b>Reject Canceled - </b>
                            <span>You canceled to reject</span>
                        </span>
                    </div>, {
                    position: "bottom-right",
                    duration: 5000
                });
            }
          })
    }

    onClickInitVerifyBtn () {
        Swal.fire({
            title: 'Are you sure you want to initially verify this SKU?',
            text: "Won't be able to revert this!",
            icon: '',
            showCancelButton: true,
            // confirmButtonColor: '#2194ED',
            confirmButtonColor: '#7ed6a5',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: 'Yes, Verify it!'
          }).then((result) => {
            if (result.value) {
                axios.post(this.state.baseUrl + '/ivs/vp/init', {
                    skuCode: this.state.currentSku
                })
                .then(res => {
                    Swal.fire({
                        title: 'Verify Succeed',
                        icon: 'success',
                        confirmButtonColor: '#7ed6a5',
                    });        
                    // console.log(res.data);
                    toaster.notify(
                        <div className="alert-with-icon toaster-success-message">
                            <span data-notify="icon" className="nc-icon nc-bell-55"></span>
                            <span className="toaster-text">
                                <b>Verify Success - </b>
                                <span>{res.data}</span>
                            </span>
                        </div>, {
                        position: "bottom-right",
                        duration: 5000
                    });
                })
                .catch(err => {
                    Swal.fire({
                        title: 'Error',
                        text: 'Oops, something went wrong!',
                        icon: 'error',
                        confirmButtonColor: '#A5B4BF',
                    });
                    console.log(err);
                });
            } else {
                toaster.notify(
                    <div className="alert-with-icon toaster-cancel-message">
                        <span data-notify="icon" className="nc-icon nc-bell-55"></span>
                        <span className="toaster-text">
                            <b>Verify Canceled - </b>
                            <span>You canceled to verify initially!</span>
                        </span>
                    </div>, {
                    position: "bottom-right",
                    duration: 5000
                });
            }
          }) 
    }

    onClickPMPBtn () {
        Swal.fire({
            title: 'Are you going to manage this SKU?',
            text: "Will open an new window of 'Product Management'!",
            showCancelButton: true,
            confirmButtonColor: '#66615B',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: 'Yes, go to the page!'
          }).then((result) => {
            if (result.value) {
                window.open("https://access.drs.network/CoreProductInformation/BP-K504-TD-1035", '_blank');
            } else {
                toaster.notify(
                    <div className="alert-with-icon toaster-cancel-message">
                        <span data-notify="icon" className="nc-icon nc-bell-55"></span>
                        <span className="toaster-text">
                            <b>Product Mangement Canceled - </b>
                            <span>You canceled to manage the product!</span>
                        </span>
                    </div>, {
                    position: "bottom-right",
                    duration: 5000
                });
            }
          })
    }

    componentDidMount () {
        this.initLoad();
    }

    componentWillUnmount () {
        this.setState({ 
            currentIvs: '編號',
            currentSku: 'SKU#',
            currentBox: '箱號'
        })
    }

    render() {
        // for test
        // const options1 = [
        //     { value: 'chocolate', label: 'Chocolate' },
        //     { value: 'strawberry', label: 'Strawberry' },
        //     { value: 'vanilla', label: 'Vanilla' }
        // ];
        // const options2 = [
        //     { value: 'orange', label: 'orange' },
        //     { value: 'watermelon', label: 'watermelon' },
        //     { value: 'banana', label: 'banana' }
        // ];
        return (
            <div className='main-content'>
                <div className='card'>
                    <div className="row">
                        <div className='col-md-12'>
                            <div className='card-header'>
                                <h4 className='card-title'>Check Product(s)</h4>
                            </div>
                        </div>
                    </div>
                    <div className='row search-space'>
                        <div className='col-sm-12 col-lg-12 col-md-12'>
                            <div className='card-body'>
                                <div className='select-wrapper-container'>
                                    <div className='col-lg-4 col-md-9 col-sm-10 select-form-wrapper'>
                                        <span className='select-label' style={{width: '45px'}}>訂單</span>
                                        <Select
                                            className='select-form cn-text'
                                            // placeholder='編號'
                                            placeholder={this.state.currentIvs}
                                            options={this.state.ivsOptions}
                                            value={this.state.currentIvs}
                                            onChange={(e) => this.onSelectIvs(e)}
                                        />
                                    </div>
                                    
                                    <div className="col-lg-4 col-md-9 col-sm-10 select-form-wrapper">
                                        <span className='select-label cn-text' style={{width: '45px'}}>SKU#</span>
                                        <Select
                                            className='select-form cn-text'
                                            placeholder={this.state.currentSku}
                                            // placeholder='SKU#'
                                            options={this.state.skuOptions}
                                            value={this.state.currentSku}
                                            onChange={(e) => this.onSelectSkus(e)}
                                        />
                                    </div>
                                
                                    <div className="col-lg-4 col-md-9 col-sm-10 select-form-wrapper">
                                        <span className='select-label cn-text' style={{width: '45px'}}>箱號</span>
                                        <Select
                                            className='select-form cn-text'
                                            placeholder={this.state.currentBox}
                                            options={this.state.boxOptions}
                                            value={this.state.currentBox}
                                            onChange={(e) => this.onSelectBox(e)}
                                        />
                                    </div>
                                </div>
                                <div className='shipment-date-info-wrapper'>
                                    <span className='card-category shipment-date-info'>First Shipment Date
                                        <span className='date-result'>2020/01/10</span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        
                        
                    </div>
                    <div className='col-sm-12'>
                        <div className='card-title title-space-mb title-space-mt'>
                            <h6>Product Information</h6>
                            <hr></hr>
                        </div>
                    </div>       
                    <div className='row'>
                        <div className='col-sm-12'>
                            <div className='card-body'>
                                { this.state.isLoading === true
                                    ? <Skeleton
                                        tw1={"30%"}
                                        tw2={"30%"}
                                        tw3={"20%"}
                                        dw1={"40%"}
                                        dw2={"60%"}
                                        dw3={"70%"}
                                      />
                                    : <div className='info-wrapper'>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>SKU#</span>
                                                <span className='result'>{this.state.targetData.SKU}</span>                                         
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Product Name</span>
                                                <span className='result'>{this.state.targetData.ProductName}</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Invoice SKU#</span>
                                                <span className='result'>{this.state.targetData.InvoiceSKU}</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Description</span>
                                                <span className='result'>Lorem ipsum dolor sit amet, 
                                                        consectetur adipiscing elitm sed to fo eiuskjnm tempur.
                                                        Incididunt uh labore magna aliqua, entim ad min vaen quls
                                                        nostrud exercitation ulamco nisi. Lorem ipsum dolor sit amet,
                                                        consecteture venminin.</span>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Export Name</span>
                                                <span className='result'>Lorem ipsum dolor sit amet uyty</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Brand Name</span>
                                                <span className='result'>Lorem ipsum dolor sit amet uyty</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title cn-text cn-bold'>中文品名</span>
                                                <span className='result cn-text'>中文品名</span>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>COO</span>
                                                <span className='result'>COO</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Market HS Code</span>
                                                <span className='result'>Market HS Code</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>TW HS Code</span>
                                                <span className='result'>TW HS Code</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>FCA Unit Price</span>
                                                <span className='result'>FCA Unit Price</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Qty</span>
                                                <span className='result'>Qty</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>FCA Total Price</span>
                                                <span className='result'>FCA Total Price</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Inventory Placement Fees</span>
                                                <span className='result'>Inventory Fees * Quantity</span>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>N.W.(kg)</span>
                                                <span className='result'>N.W.(kg)</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Length(cm)</span>
                                                <span className='result'>Length(cm)</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Width(cm)</span>
                                                <span className='result'>Width(cm)</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Height(cm)</span>
                                                <span className='result'>Height(cm)</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>CBM</span>
                                                <span className='result'>CBM</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Oversized?</span>
                                                <span className='result'>YSE/NO</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Dangerous Good?</span>
                                                <span className='result'>YES/NO</span>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title cn-text cn-bold'>出口稅則要求文件</span>
                                                <span className='cn-result cn-text'><a href='' target='_blank'>出口稅則要求 Document Link </a></span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title cn-text cn-bold'>進口稅則要求文件</span>
                                                <span className='cn-result cn-text'><a href='' target='_blank'>進口稅則要求 Document Link </a></span>
                                            </div>
                                        </div>
                                      </div>  
                                }              
                            </div>
                        </div>
                    </div>
                </div>
                
                <div className='card'>
                    <div className='col-sm-12'>
                        <div className='card-title title-space'>
                            <h6>Documentation Requirement</h6>
                            <hr></hr>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-sm-12">
                            <div className="card-body">
                                { this.state.isLoading === true
                                    ? <div className="skeleton-row-wrapper">
                                        <Skeleton
                                            tw1={"80%"}
                                            tw2={"80%"}
                                            tw3={"70%"}
                                            dw1={"20%"}
                                            dw2={"20%"}
                                            dw3={"20%"}
                                        />
                                        <Skeleton
                                            tw1={"80%"}
                                            tw2={"80%"}
                                            tw3={"70%"}
                                            dw1={"20%"}
                                            dw2={"20%"}
                                            dw3={"20%"}
                                        />
                                        <Skeleton
                                            tw1={"80%"}
                                            tw2={"80%"}
                                            tw3={"70%"}
                                            dw1={"20%"}
                                            dw2={"20%"}
                                            dw3={"20%"}
                                        />
                                      </div>
                                    : <div className="doc-wrapper">
                                        <div className="doc-row-wrapper">
                                            <div className="doc-column-wrapper">
                                                <span className="doc-title-wrapper">
                                                    <i class="far fa-file-alt"></i>
                                                    {/* papper dashboard css 
                                                    <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title">G3 Invoice</span>
                                                </span>
                                                <span className="doc-title-wrapper">
                                                    <i class="far fa-file-alt"></i>
                                                    {/* <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title">MSDS</span>
                                                </span>
                                                <span className="doc-title-wrapper" style={{'font-size': '14px'}}>
                                                    <i class="far fa-file-alt"></i>
                                                    {/* <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title">Dangerous Good</span>
                                                </span>
                                            </div>
                                            <div className="doc-column-wrapper">
                                                <DocStatus 
                                                    hasDoc={this.state.hasDoc[0]} 
                                                    isAm={this.state.isAm}
                                                    id='G3 Invoice'
                                                    value='1'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                    // link={this.state.targetData.G3Invoice} for link data use
                                                />
                                                <DocStatus 
                                                    hasDoc={this.state.hasDoc[1]}
                                                    isAm={this.state.isAm}
                                                    id='MSDS'
                                                    value='2'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                    // link={this.state.targetData.MSDS} for link data use
                                                />
                                                <DocStatus
                                                    hasDoc={this.state.hasDoc[2]}
                                                    isAm={this.state.isAm}
                                                    id='Dangerous Good'
                                                    value='3'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                />
                                            </div>
                                        </div>
                                        <div className="doc-row-wrapper">
                                            <div className="doc-column-wrapper">
                                                <span className="doc-title-wrapper">
                                                    <i class="far fa-file-alt"></i>
                                                    {/* <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title">UN38.3</span>
                                                </span>
                                                <span className="doc-title-wrapper">
                                                    <i class="far fa-file-alt"></i>
                                                    {/* <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title">PPQ505</span>
                                                </span>
                                                <span className="doc-title-wrapper">
                                                    <i class="far fa-file-alt"></i>
                                                    {/* <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title">US FDA</span>
                                                </span>
                                            </div>
                                            <div className="doc-column-wrapper">
                                                <DocStatus
                                                    hasDoc={this.state.hasDoc[3]}
                                                    isAm={this.state.isAm}
                                                    id='UN38.3'
                                                    value='4'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                />
                                                <DocStatus
                                                    hasDoc={this.state.hasDoc[4]}
                                                    isAm={this.state.isAm}
                                                    id='PPQ505'
                                                    value='5'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                />
                                                <DocStatus
                                                    hasDoc={this.state.hasDoc[5]}
                                                    isAm={this.state.isAm}
                                                    id='US FDA'
                                                    value='6'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                />
                                            </div>
                                        </div>
                                        <div className="doc-row-wrapper">
                                            <div className="doc-column-wrapper">
                                                <span className="doc-title-wrapper">
                                                    <i class="far fa-file-alt"></i>
                                                    {/* <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title"> US MID </span>
                                                </span>
                                                <span className="doc-title-wrapper">
                                                    <i class="far fa-file-alt"></i>
                                                    {/* <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title cn-text"> 油脂含量聲明書 </span>
                                                </span>
                                                <span className="doc-title-wrapper">
                                                    <i class="far fa-file-alt"></i>
                                                    {/* <span data-notify="icon" className="nc-icon nc-paper"></span> */}
                                                    <span className="doc-title cn-text"> 出口稅則要求 </span>
                                                </span>
                                            </div>
                                            <div className="doc-column-wrapper">
                                                <DocStatus
                                                    hasDoc={this.state.hasDoc[6]}
                                                    isAm={this.state.isAm}
                                                    id='US MID'
                                                    value='7'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                />
                                                <DocStatus
                                                    hasDoc={this.state.hasDoc[7]}
                                                    isAm={this.state.isAm}
                                                    id='油脂含量聲明書'
                                                    value='8'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                />
                                                <DocStatus 
                                                    hasDoc={this.state.hasDoc[8]}
                                                    isAm={this.state.isAm}
                                                    id='出口稅則要求'
                                                    value='9'
                                                    changeCheckboxState={this.changeCheckboxState}
                                                />
                                            </div>
                                        </div>
                                    </div>
                                }
                            </div> 
                        </div>
                    </div>    
                </div>
                { this.state.isAm === true
                    ?   <BtnGroup
                            btnTextLeft='Initial Verify'
                            btnTextRight='Product Manage Page'
                            btnLeftClick={this.onClickInitVerifyBtn}
                            btnRightClick={this.onClickPMPBtn}
                        />
                    :   <BtnGroup
                            btnTextLeft='Confirm'
                            btnTextRight='Reject'
                            btnLeftClick={this.onClickConfirmBtn}
                            btnRightClick={this.onClickRejectBtn}
                        />
                }
                
            </div>
        )
    }
}

export default (VerifyProductInfo);