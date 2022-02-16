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
import Skeleton from './Skeleton.js';

import '../../css/paper-dashboard.css';
import {DOMAIN_NAME} from '../../constants/action-types'

class VerifyProductInfo extends Component {
    constructor() {
        super();
        this.state = {
            isLogistics: true,
            isLoading: true,
            isLineItemLoading: true,
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
            currentBoxNum: '',
            currentMixLineSeq:'',
            checkNote: {},
            productInfo: {},
            lineItemInfo: {},
            boxSize: '',
            baseUrl: DOMAIN_NAME,
            status: 'New',
            qty: '',
            dc: '',
            shippingDate:''
        };

        this.initLoad = this.initLoad.bind(this);
        this.onSelectSkus = this.onSelectSkus.bind(this);
        this.onSelectIvs = this.onSelectIvs.bind(this);
        this.onSelectBox = this.onSelectBox.bind(this);
        this.changeCheckboxState = this.changeCheckboxState.bind(this);
        this.handleVpStatus = this.handleVpStatus.bind(this);
        this.handleDocRequirement = this.handleDocRequirement.bind(this);
        this.getLineItemData = this.getLineItemData.bind(this);
      
        this.onClickConfirmBtn = this.onClickConfirmBtn.bind(this);
        this.onClickRejectBtn = this.onClickRejectBtn.bind(this);
        this.onClickInitVerifyBtn = this.onClickInitVerifyBtn.bind(this);
        this.onClickPMPBtn = this.onClickPMPBtn.bind(this);
        this.onClickBack = this.onClickBack.bind(this);
    }
    
    initLoad() {
        axios.post(this.state.baseUrl + '/ivs/vp/pld', {})
        .then(res => {
            const initIvs = {value: res.data.ivsName};
            const initSku = {value: res.data.sku};
            const initKcode = {value: res.data.ivsName.slice(4,8)};

            this.setState({ 
                isLogistics: res.data.isLogistics,
                status: res.data.vpStatus,
                currentIvs: initIvs.value,
                currentSku: initSku.value,
                currentBoxNum: res.data.boxNum,
                currentMixLineSeq: res.data.mixBoxLineSeq,
                qty:res.data.qty,
                dc:res.data.dc
            });

            axios.post(this.state.baseUrl + '/ivs/vp/ivs', {
                kcode: initKcode.value
            })
            .then(res => {
                const ivsOptionsList = res.data.map(item => {
                    return Object.assign({}, { value: item, label: item });
                });
                this.setState({
                    ivsOptions: ivsOptionsList
                });
                this.onSelectIvs(initIvs , true);
            })
        })
        .catch(err => {
            console.log(err);
        })
    };

    onSelectIvs(selectedIvs , isInit) {
        this.setState({
            currentIvs: selectedIvs.value
        })
        if(!isInit) {
            this.setState({
                isLoading: true,
                isLineItemLoading: true
            })
        }
        axios.all([
            axios.post(this.state.baseUrl + '/ivs/vp/sd', {ivsNumber: selectedIvs.value}),
            axios.post(this.state.baseUrl + '/ivs/vp/sku', {ivsNumber: selectedIvs.value})
        ])
        .then(axios.spread((res1, res2) => {
            console.log(res1, res2);
            this.setState({
                shippingDate: res1.data.shippingDate
            })
            if (res2.data.length == 0) {
                this.setState({
                    skuOptions: [],
                    currentIvs: selectedIvs.value,
                    currentSku: 'SKU#',
                    currentBox: '箱號',
                    boxOptions: [],
                    currentBoxNum: '',
                    currentMixLineSeq:'',
                });
                Swal.fire({
                    title: 'Error',
                    text: 'There is no SKU# data!',
                    icon: 'error',
                    confirmButtonColor: '#A5B4BF',
                });
            } else {
                const skuOptionsList = res2.data.map(item => {
                    return Object.assign({}, { value: item, label: item });
                });
                this.setState({
                    skuOptions: skuOptionsList,
                    currentIvs: selectedIvs.value
                });
                if (!isInit) {
                    this.setState({
                        currentSku: res2.data[0]
                    });
                }
               const selectedSku = Object.assign({}, { value: this.state.currentSku });
               this.onSelectSkus(selectedIvs.value, selectedSku);
            }
        }))
        .catch(err => console.log(err))

        // axios.post(this.state.baseUrl + '/ivs/vp/sd', {
        //     ivsNumber: selectedIvs.value
        // })
        // .then(res => {
        //     this.setState({shippingDate: res.data.shippingDate});
        // })
        // .catch(err =>console.log(err));

        // axios.post(this.state.baseUrl + '/ivs/vp/sku', {
        //     ivsNumber: selectedIvs.value
        // })
        // .then(res => {
        //     // CHECK if no data
        //     if (res.data.length == 0) {
        //         // console.log('res.data == []');
        //         this.setState({
        //             skuOptions: [],
        //             currentIvs: selectedIvs.value,
        //             currentSku: 'SKU#',
        //             currentBox: '箱號',
        //             boxOptions: [],
        //             currentBoxNum: '',
        //             currentMixLineSeq:'',
        //         });
        //         Swal.fire({
        //             title: 'Error',
        //             text: 'There is no SKU# data!',
        //             icon: 'error',
        //             confirmButtonColor: '#A5B4BF',
        //         });

        //     } else {
        //         const skuOptionsList = res.data.map(item => {
        //             return Object.assign({}, { value: item, label: item });
        //         });

        //         if (isInit) {
        //             this.setState({
        //                 skuOptions: skuOptionsList,
        //                 currentIvs: selectedIvs.value
        //             });
        //         } else {
        //             this.setState({
        //                 skuOptions: skuOptionsList,
        //                 currentIvs: selectedIvs.value,
        //                 currentSku: skuOptionsList[0].value
        //             });
        //         }
        //        const selectedSku = Object.assign({}, { value: this.state.currentSku });
        //        this.onSelectSkus(selectedIvs.value, selectedSku);
        //     }
        // })
        // .catch(err => console.log(err));
    }

    handleVpStatus(aryBox) {
        // const aryBox = selectedBoxVal.split("-");
        axios.post(this.state.baseUrl + '/ivs/vp/vpst', {
            ivsName: this.state.currentIvs,
            boxNum: aryBox[0],
            mixLineSeq: aryBox[1]
        })
        .then(res => {
            this.setState({
                status: res.data.status
            })
        })
        .catch(err => console.log(err));
    }
    
    handleDocRequirement(aryBox) {
        // const aryBox = selectedBoxVal.split("-");
        axios.post(this.state.baseUrl + '/ivs/vp/dr', {
            ivsName: this.state.currentIvs,
            boxNum: aryBox[0],
            mixLineSeq: aryBox[1]
        })
        .then(res => {
            if (res.data.length >0) {
                const dr = res.data[0];
                this.setState({
                    checkbox: [
                        { id: 'G3 Invoice', isChecked: dr.g3Invoice },
                        { id: 'MSDS', isChecked: dr.msds },
                        { id: 'Dangerous Good', isChecked: dr.dangerousGoods },
                        { id: 'UN38.3', isChecked: dr.us383 },
                        { id: 'PPQ505', isChecked: dr.ppq505 },
                        { id: 'US FDA', isChecked: dr.usFDA },
                        { id: 'US MID', isChecked: dr.usMID },
                        { id: '油脂含量聲明書', isChecked: dr.fatContentSpecification },
                        { id: '出口稅則要求', isChecked: dr.exportTariffRequirement }
                    ],
                    isLoading: false
                 })
            } else {
                this.setState({
                    checkbox: [
                        { id: 'G3 Invoice', isChecked: false },
                        { id: 'MSDS', isChecked: false },
                        { id: 'Dangerous Good', isChecked: false },
                        { id: 'UN38.3', isChecked: false },
                        { id: 'PPQ505', isChecked: false },
                        { id: 'US FDA', isChecked: false },
                        { id: 'US MID', isChecked: false },
                        { id: '油脂含量聲明書', isChecked: false },
                        { id: '出口稅則要求', isChecked:false }
                    ],
                    isLoading: false
                 })  
            }
        })
        .catch(err => console.log(err));
    }

    onSelectSkus(selectedIvs, selectedSku) {
        this.setState({
            currentSku: selectedSku.value,
            isLoading: true,
            isLineItemLoading: true
        });

        axios.post(this.state.baseUrl + '/ivs/vp/box', {
            ivsNumber: selectedIvs,
            sku: selectedSku.value
        })
        .then(res => {

            if(res.data.length == 0) {
                this.setState({
                    currentBox: '箱號',
                    boxOptions: [],
                    currentBoxNum: '',
                    currentMixLineSeq:'',
                });
                Swal.fire({
                    title: 'Error',
                    text: 'There is no Box data!',
                    icon: 'error',
                    confirmButtonColor: '#A5B4BF',
                });
            } else {
                const boxOptionsList = res.data.map(item => {
                    const aryItem = item.split("/")
                    return Object.assign({}, { value: aryItem[1], label: aryItem[0] });
                });
                const aryBox =  boxOptionsList[0].value.split("-");
                this.setState({
                    boxOptions: boxOptionsList,
                    currentBox: boxOptionsList[0].label,
                    currentBoxNum :aryBox[0],
                    currentMixLineSeq : aryBox[1]
                })
                this.handleVpStatus(aryBox)
                this.handleDocRequirement(aryBox)
                this.getLineItemData(aryBox)
            }
        }).catch(err => console.log(err));
        
        axios.post(this.state.baseUrl + '/ivs/vp/data', {
            sku: selectedSku.value,
            dc: this.state.dc,
            ivs: selectedIvs
        })
        .then(res => {
            this.setState({
                productInfo: res.data
            });
        })
        .catch(err => {
            Swal.fire({
                title: 'Error',
                text: 'There is no Product data!',
                icon: 'error',
                confirmButtonColor: '#A5B4BF',
            });
            console.log(err);
        });

               axios.post(this.state.baseUrl + '/ivs/vp/data', {
                    sku: selectedSku.value,
                    dc: this.state.dc,
                    ivs: selectedIvs
                })
                .then(res => {
                    this.setState({
                        productInfo: res.data
                    });
                })
                .catch(err => {
                    Swal.fire({
                        title: 'Error',
                        text: 'There is no Product data!',
                        icon: 'error',
                        confirmButtonColor: '#A5B4BF',
                    });
                    console.log(err);
                });

    }

    onSelectBox(selectedBox) {
        this.setState({
            isLoading: true,
            isLineItemLoading: true
        });
        const aryBox = selectedBox.value.split("-");

        this.handleVpStatus(aryBox)
        this.handleDocRequirement(aryBox)

        this.setState({
            currentBox: selectedBox.label,
            currentBoxNum :aryBox[0],
            currentMixLineSeq : aryBox[1],
        })
        this.getLineItemData(aryBox);
    }

    getLineItemData(aryBox) {
        axios.post(this.state.baseUrl + '/ivs/vp/lineitem', {
            ivsNumber: this.state.currentIvs,
            boxNum: aryBox[0],
            mixLineSeq: aryBox[1]
        })
        .then(res => {
            if (res.data.length == 0) {
                this.setState({
                    lineItemInfo: {}
                })
                Swal.fire({
                    title: 'Error',
                    text: 'There is no Line Item data!',
                    icon: 'error',
                    confirmButtonColor: '#A5B4BF',
                });
            } else {
                const arr = [];
                arr.push(res.data.cartonDimensionCm1);
                arr.push(res.data.cartonDimensionCm2);
                arr.push(res.data.cartonDimensionCm3);
                const str = `${arr[0]} x ${arr[1]} x ${arr[2]}`;
                this.setState({
                    lineItemInfo: res.data,
                    boxSize: str,
                    isLineItemLoading: false
                })
            }
        })
        .catch(err => {
            console.log(err)
        })
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
            title: 'Sure to Confirm?',
            text: `SKU#: ${this.state.currentSku}, Box: ${this.state.currentBox}. Won't be able to revert this!`,
            icon: '',
            showCancelButton: true,
            // confirmButtonColor: '#2194ED',
            confirmButtonColor: '#7ed6a5',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: 'Yes, Confirm it!'
        }).then((result) => {

            // get the fullData of specific IVS, sku, box thats' document checked or not
            //const fullData = Object.assign({}, this.state.checkNote);
            //fullData.currentIvs = this.state.currentIvs;
            //fullData.currentSku = this.state.currentSku;
            //fullData.currentBox = this.state.currentBox;
            //fullData.checkbox = this.state.checkbox;

            if (result.value) {
                // console.log('it is work!');
                axios.post(this.state.baseUrl + '/ivs/vp/confirm', {
                    ivsName: this.state.currentIvs,
                    boxNum: this.state.currentBoxNum,
                    mixLineSeq: this.state.currentMixLineSeq,
                    docReqData: JSON.stringify(this.state.checkbox)
                })
                .then(res => {
                    Swal.fire({
                        title: 'Confirm Succeed',
                        icon: 'success',
                        confirmButtonColor: '#7ed6a5',
                    });

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

                    this.setState({
                        status: "Confirmed"
                    })
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
            title: 'Sure to reject?',
            text: `SKU#: ${this.state.currentSku}, Box: ${this.state.currentBox}. Won't be able to revert this!`,
            icon: '',
            showCancelButton: true,
            // confirmButtonColor: '#2194ED',
            confirmButtonColor: '#66615B',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: 'Yes, Reject it!'
          }).then((result) => {

             // get the fullData of specific IVS, sku, box thats' document checked or not
             //const fullData = Object.assign({}, this.state.checkNote);
             //fullData.currentIvs = this.state.currentIvs;
             //fullData.currentSku = this.state.currentSku;
             //fullData.currentBox = this.state.currentBox;
             //fullData.checkbox = this.state.checkbox;
             //console.log(fullData);

            if (result.value) {
                axios.post(this.state.baseUrl + '/ivs/vp/reject', {
                    ivsName: this.state.currentIvs,
                    boxNum: this.state.currentBoxNum,
                    mixLineSeq: this.state.currentMixLineSeq,
                    docReqData: JSON.stringify(this.state.checkbox)
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
                    this.setState({
                       status: "Invalid"
                     })
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
            title: 'Sure to initially verify?',
            text: `SKU#: ${this.state.currentSku}, Box: ${this.state.currentBox}. Won't be able to revert this!`,
            icon: '',
            showCancelButton: true,
            // confirmButtonColor: '#2194ED',
            confirmButtonColor: '#7ed6a5',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: 'Yes, Verify it!'
          }).then((result) => {
            if (result.value) {

                axios.post(this.state.baseUrl + '/ivs/vp/init', {
                    ivsName: this.state.currentIvs,
                    boxNum: this.state.currentBoxNum,
                    mixLineSeq: this.state.currentMixLineSeq
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
                    this.setState({
                        status: 'To be confirmed'
                    })
                })
                .catch(err => {
                    Swal.fire({
                        title: 'Error',
                        text: 'Oops, something went wrong!',
                        icon: 'error',
                        confirmButtonColor: '#A5B4BF',
                    });
                    //console.log(err);
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

    onClickPMPBtn (baseProductCode) {
        Swal.fire({
            title: 'Sure to go to the manage page?',
            text: "Will open an new window.",
            showCancelButton: true,
            confirmButtonColor: '#66615B',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: 'Yes, go to the page!'
          }).then((result) => {
            if (result.value) {
                window.open("https://access.drs.network/CoreProductInformation/" + baseProductCode, '_blank');
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

    onClickBack () {
       window.open(this.state.baseUrl + "/InventoryShipments/" + this.state.currentIvs , '_self');
    }

    componentDidMount () {
        this.initLoad();
    }

    componentWillUnmount () {
        this.setState({
            currentIvs: '編號',
            currentSku: 'SKU#',
            currentBox: '箱號',
            isLogistics: false
        })
    }

    render() {
        let statusClass = 'product-status';
        if (this.state.status === 'New') {
            statusClass += ' status-new';
        } else if (this.state.status === 'To be confirmed') {
            statusClass += ' status-tobeconfirmed';
        } else if (this.state.status === 'Confirmed') {
            statusClass += ' status-confirmed';
        } else if (this.state.status === 'Invalid') {
            statusClass += ' status-invalid';
        };

        return (
            <div className='main-content' style={{padding: '2%'}}>
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
                                        <span className='select-label' style={{width: '45px'}}>Name</span>
                                        <Select
                                            className='select-form cn-text'
                                            placeholder={this.state.currentIvs}
                                            options={this.state.ivsOptions}
                                            value={this.state.currentIvs}
                                            onChange={(e) => this.onSelectIvs(e , false)}
                                        />
                                    </div>

                                    <div className="col-lg-4 col-md-9 col-sm-10 select-form-wrapper">
                                        <span className='select-label cn-text' style={{width: '45px'}}>SKU#</span>
                                        <Select
                                            className='select-form cn-text'
                                            placeholder={this.state.currentSku}
                                            options={this.state.skuOptions}
                                            value={this.state.currentSku}
                                            onChange={(e) => this.onSelectSkus(this.state.currentIvs,e)}
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
                                    <span className='card-category'>First Shipment Date
                                        <span className='date-result'> {this.state.shippingDate}</span>
                                    </span>
                                </div>
                                {/* product-status: version 2 */}
                                {/* <div className='product-status-wrapper'>
                                    <div className='product-status-display'>
                                        <span className='card-category cn-text' style={{'font-size': '16px'}}>產品確認狀態</span>
                                        <span className={statusClass} style={{margin: '5px 2px 0 20px'}}>{this.state.status}</span>
                                    </div>
                                </div> */}
                            </div>
                        </div>

                    </div>


                    <div className='col-sm-12'>
                        <div className='card-title title-space-mb title-space-mt'>
                            <div className="title-wrapper">
                                <h6>LineItem Info</h6>
                            </div>
                            <hr></hr>
                        </div>
                    </div>
                    <div className='row'>
                        <div className='col-sm-12'>
                            <div className='card-body'>
                                { this.state.isLineItemLoading === true
                                    ? <div className="spinner-wrapper">
                                        <div className="spinner"></div>
                                        <div className="spinner-text"><span>Loading...</span></div>
                                      </div>
                                    : <div className='info-wrapper'>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>外箱尺寸 [cm x cm x cm]</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.boxSize}</span>
                                                </div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>單箱毛重 [公斤]</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.lineItemInfo.perCartonGrossWeightKg}</span>
                                                </div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>單箱數量</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.lineItemInfo.perCartonUnits}</span>
                                                </div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>箱數</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.lineItemInfo.cartonCounts}</span>
                                                </div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>產品數量</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.lineItemInfo.quantity}</span>
                                                </div>
                                            </div>
                                        </div>
                                      </div>
                                }
                            </div>
                        </div>
                    </div>

                    <div className='col-sm-12'>
                        <div className='card-title title-space-mb title-space-mt'>
                            <div className="title-wrapper">
                                <h6>Product Information</h6>
                                <div className={statusClass}>
                                    {this.state.status}
                                </div>
                            </div>
                            <hr></hr>
                        </div>
                    </div>
                    <div className='row'>
                        <div className='col-sm-12'>
                            <div className='card-body'>
                                { this.state.isLoading === true
                                    ? <div className="spinner-wrapper">
                                        <div className="spinner"></div>
                                        <div className="spinner-text"><span>Loading...</span></div>
                                      </div>
                                    : <div className='info-wrapper'>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>SKU#</span>
                                                <span className='result'>{this.state.productInfo.sku}</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Product Name/Description</span>
                                                <span className='result'>{this.state.productInfo.productName}</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Invoice SKU#</span>
                                                <span className='result'>{this.state.productInfo.baseProductCode}</span>
                                            </div>

                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Export Name</span>
                                                <span className='result'>{this.state.productInfo.exportName}</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Brand Name</span>
                                                <span className='result'>{this.state.productInfo.brandEng}</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title cn-text cn-bold'>中文品名</span>
                                                <span className='result cn-text'>{this.state.productInfo.productNameLocal}</span>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>COO</span>
                                                <span className='result'>{this.state.productInfo.countryOfOrigin}</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Market HS Code</span>
                                                <span className='result'>{this.state.productInfo.marketHsCode}</span>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>TW HS Code</span>
                                                <span className='result'>{this.state.productInfo.hsCode}</span>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper margin-top-30'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>FCA Unit Price</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.productInfo.fcaPrice}</span>
                                                </div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Qty</span>
                                                <div className='result-wrapper'><span className='result'>{this.state.qty}</span></div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>FCA Total Price</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.productInfo.fcaTotal}</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Inventory Placement Fees</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.productInfo.inventoryPlacementFee}</span>
                                                </div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>N.W.(kg)</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.productInfo.netWeight}</span>
                                                </div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Length(cm)</span>
                                                <div className='result-wrapper'><span className='result'>{this.state.productInfo.length}</span></div>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Width(cm)</span>
                                                <div className='result-wrapper'><span className='result'>{this.state.productInfo.width}</span></div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Height(cm)</span>
                                                <div className='result-wrapper'><span className='result'>{this.state.productInfo.height}</span></div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>CBM</span>
                                                <div className='result-wrapper'><span className='result'>{this.state.productInfo.boxCbm}</span></div>
                                            </div>
                                        </div>
                                        <div className='info-column-wrapper'>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Oversized?</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.productInfo.oversized === false ? 'NO': 'YES'}</span>
                                                </div>
                                            </div>
                                            <div className='info-row-wrapper'>
                                                <span className='info-title'>Dangerous Good?</span>
                                                <div className='result-wrapper'>
                                                    <span className='result'>{this.state.productInfo.dangerousGoods == false ? 'NO': 'YES'}</span>
                                                </div>
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
                            { this.state.isLoading === true
                                ? <div className="spinner-wrapper">
                                    <div className="spinner"></div>
                                    <div className="spinner-text"><span>Loading...</span></div>
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
                                                isLogistics={this.state.isLogistics}
                                                id='G3 Invoice'
                                                value='1'
                                                docChecked = {this.state.checkbox[0].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
                                                // link={this.state.....G3Invoice} for link data use
                                            />
                                            <DocStatus
                                                hasDoc={this.state.hasDoc[1]}
                                                isLogistics={this.state.isLogistics}
                                                id='MSDS'
                                                value='2'
                                                docChecked = {this.state.checkbox[1].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
                                                // link={this.state.....MSDS} for link data use
                                            />
                                            <DocStatus
                                                hasDoc={this.state.hasDoc[2]}
                                                isLogistics={this.state.isLogistics}
                                                id='Dangerous Good'
                                                value='3'
                                                docChecked = {this.state.checkbox[2].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
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
                                                isLogistics={this.state.isLogistics}
                                                id='UN38.3'
                                                value='4'
                                                docChecked = {this.state.checkbox[3].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
                                            />
                                            <DocStatus
                                                hasDoc={this.state.hasDoc[4]}
                                                isLogistics={this.state.isLogistics}
                                                id='PPQ505'
                                                value='5'
                                                docChecked = {this.state.checkbox[4].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
                                            />
                                            <DocStatus
                                                hasDoc={this.state.hasDoc[5]}
                                                isLogistics={this.state.isLogistics}
                                                id='US FDA'
                                                value='6'
                                                docChecked = {this.state.checkbox[5].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
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
                                                isLogistics={this.state.isLogistics}
                                                id='US MID'
                                                value='7'
                                                docChecked = {this.state.checkbox[6].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
                                            />
                                            <DocStatus
                                                hasDoc={this.state.hasDoc[7]}
                                                isLogistics={this.state.isLogistics}
                                                id='油脂含量聲明書'
                                                value='8'
                                                docChecked = {this.state.checkbox[7].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
                                            />
                                            <DocStatus
                                                hasDoc={this.state.hasDoc[8]}
                                                isLogistics={this.state.isLogistics}
                                                id='出口稅則要求'
                                                value='9'
                                                docChecked = {this.state.checkbox[8].isChecked}
                                                changeCheckboxState={this.changeCheckboxState}
                                                vpStatus={this.state.status}
                                            />
                                        </div>
                                    </div>
                                </div>
                            }

                        </div>
                    </div>
                </div>
                  <div className='confirm-btn-wrapper'>
                { this.state.isLogistics === false && ( this.state.status == "New" || this.state.status == "Invalid")
                    ?   <BtnGroup
                            btnTextLeft='Initial Verify'
                            btnTextRight='Product Manage Page'
                            btnLeftClick={this.onClickInitVerifyBtn}
                            btnRightClick={this.onClickPMPBtn}
                            isLoading={this.state.isLoading}
                            baseProductCode = {this.state.productInfo.baseProductCode}
                        />
                    :
                    [  ( this.state.isLogistics ?
                               (this.state.status == "To be confirmed"  ?
                                                    <BtnGroup
                                                            btnTextLeft='Confirm'
                                                            btnTextRight='Reject'
                                                            btnLeftClick={this.onClickConfirmBtn}
                                                            btnRightClick={this.onClickRejectBtn}
                                                            isLoading={this.state.isLoading}
                                                            baseProductCode = {this.state.productInfo.baseProductCode}
                                                        />

                                                        :null
                              ) : null

                      )

                    ]

                }

                     <button className='btn btn-default'  onClick={() => this.onClickBack()}>
                       Back
                  </button>
                </div>



            </div>
        )
    }
}

export default (VerifyProductInfo);