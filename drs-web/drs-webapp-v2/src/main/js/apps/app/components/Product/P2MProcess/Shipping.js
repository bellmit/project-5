import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Link, Redirect,useParams } from 'react-router-dom';
import CheckBox from '../common/Checkbox/CheckBox'
import RadioButton from '../common/RadioButton'
import { initP2MShipping, saveP2MShipping, updateSh, getExactApplication } from '../../../actions';
import {FormattedMessage} from 'react-intl';
import Comment from './common/Comment';
import RateReviewIcon from '@material-ui/icons/RateReview';
import { toast } from 'react-toastify';
import '../../../sass/table.scss';
import P2MApplicationInfo from './P2MApplicationInfo';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import Modal from '@material-ui/core/Modal';

const Shipping = () => {
    const dispatch = useDispatch()
    // const { id } = useParams()
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state => state.d)

    const toaster = <FormattedMessage id = "managep2maction.savedsuccessfully"/>;
    const savingMsg = <FormattedMessage id = "managep2m.savingMessage"/>

    useEffect(() => {
        const p2mId = p2m.currentAp._id.$oid;
        
        if (!p2mId) {
            console.log('refresh get')
            // dispatch(getExactApplication(id))
            // dispatch(initP2MShipping(id));
        } else {
            dispatch(initP2MShipping(p2mId));
        }
        
        if (p2m.smoothScroll[0]) {
            smoothScroll(document.getElementById(p2m.smoothScroll[1]))
        } 
    }, []);

    const onCheckReturn = () => {
        let boolean = true;
        if (p2m.sh.checkReturn) {
            boolean = false;
        }
        const targetSubAp = p2m.sh
        targetSubAp.checkReturn = boolean;

        dispatch(updateSh(targetSubAp))
    }
    const handleCountryChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.country =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleCityChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.city =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleStateOrProvinceChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.stateorprovince =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleZipOrPostalcodeChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.ziporpostalcode =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAddress1Change = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.address1 =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAddress2Change = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.address2 =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleFullnameChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.fullname =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handlePhonenumberChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.phonenumber =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleEmailaddressChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.returnAddr.emailaddress =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const checkHasLocalWarehouse = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.haslocalWarehouse =event.target.value;
        let newUploadInput = {
            country: '',
            city: '',
            stateorprovince: '',
            ziporpostalcode: '',
            address1: '',
            address2: '',
            fullname: '',
            phonenumber: '',
            emailaddress: ''
        };
        //        newUploadInput = targetSubAp.returnAddr
        targetSubAp.returnAddr  = newUploadInput
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailableCountryChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availableCountry =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailableCityChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availableCity =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailableStateOrProvinceChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availableStateorprovince =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailableZipOrPostalcodeChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availableZiporpostalcode =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailableAddress1Change = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availableAddress1 =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailableAddress2Change = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availableAddress2 =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailableFullnameChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availableFullname =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailablePhonenumberChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availablePhonenumber =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const handleAvailableEmailaddressChange = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableReturnAddr.availableEmailaddress =event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const checkAvailableHasLocalWarehouse = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.availableHaslocalWarehouse =event.target.value;
        let newUploadInput = {
            availableCountry: '',
            availableCity: '',
            availableStateorprovince: '',
            availableZiporpostalcode: '',
            availableAddress1: '',
            availableAddress2: '',
            availableFullname: '',
            availablePhonenumber: '',
            availableEmailaddress: ''
        };
        //        newUploadInput = targetSubAp.availableReturnAddr
        targetSubAp.availableReturnAddr  = newUploadInput
        dispatch(updateSh(targetSubAp))
    }
    const checkPSAllSkuApplied = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.psAllSkuApplied = event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const checkPPSAllSkuApplied = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.ppsAllSkuApplied = event.target.value;
        dispatch(updateSh(targetSubAp))
    }
    const checkPPWAllSkuApplied = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.ppwAllSkuApplied = event.target.value;
        dispatch(updateSh(targetSubAp))
    }

    const checkPNWAllSkuApplied = (event) => {
        const targetSubAp = p2m.sh
        targetSubAp.pnwAllSkuApplied = event.target.value;
        dispatch(updateSh(targetSubAp))
    }

    const [saveTimeout, setSaveTimeout] = useState(false)
    
    const handleSave = (status) => {
        setSaveTimeout(true)
        toast(savingMsg);
        const shipping = p2m.sh;
        shipping.p2mApplicationId = p2m.currentAp._id;
        shipping.status = status;
        const isComment = false;
        // const redirect = isComment ? '' : `/product/application/${id}`;
        const redirect = isComment ? '': '/product/application'

        setTimeout(() => {
            setSaveTimeout(false)
            dispatch(saveP2MShipping(shipping,toaster,redirect))
        }, 3000)

    }

    const onFcaPriceChange = (e,index) => {

        const targetSubAp = p2m.sh;
        targetSubAp.appliedSku[index].fcaPrice = e.target.value
        dispatch(updateSh(targetSubAp))
    }

    const onIncludePackageWeightChange = (e,index) => {

        const targetSubAp = p2m.sh;
        if ( p2m.sh.ppwAllSkuApplied == 'yes' ) {
            targetSubAp.appliedSku.map(item => {
                item.includePackageWeight = e.target.value
            })
            targetSubAp.allIncludePackageWeight = e.target.value
        } else {
            targetSubAp.appliedSku[index].includePackageWeight = e.target.value
        }
        setShowValidWarning(false)
        dispatch(updateSh(targetSubAp))
    }

    const onIncludePackageHeightChange = (e, index) => {
        const targetSubAp = p2m.sh;
        if ( p2m.sh.ppsAllSkuApplied == 'yes') {
            targetSubAp.appliedSku.map(item => {
                item.includePackageHeight = e.target.value
            })
            targetSubAp.allIncludePackageHeight = e.target.value
        } else {
            targetSubAp.appliedSku[index].includePackageHeight = e.target.value
        }
        setShowValidWarning(false)
        dispatch(updateSh(targetSubAp))
    }
    const onIncludePackageWidthChange = (e,index) => {
        const targetSubAp = p2m.sh;
        if (p2m.sh.ppsAllSkuApplied == 'yes') {
            targetSubAp.appliedSku.map(item => {
                item.includePackageWidth = e.target.value
            })
            targetSubAp.allIncludePackageWidth = e.target.value
        } else {
            targetSubAp.appliedSku[index].includePackageWidth = e.target.value
        }
        setShowValidWarning(false)
        dispatch(updateSh(targetSubAp))
    }

    const onIncludePackageLengthChange = (e, index) => {
        const targetSubAp = p2m.sh;
        if(p2m.sh.ppsAllSkuApplied == 'yes') {
            targetSubAp.appliedSku.map(item => {
                item.includePackageLength = e.target.value
            })
            targetSubAp.allIncludePackageLength = e.target.value
        } else {
            targetSubAp.appliedSku[index].includePackageLength = e.target.value
        }
        setShowValidWarning(false)
        dispatch(updateSh(targetSubAp))
    }

    const onNetWeightChange = (e, index) => {
        const targetSubAp = p2m.sh;
        if ( p2m.sh.pnwAllSkuApplied == 'yes') {
            targetSubAp.appliedSku.map(item => {
                item.netWeight = e.target.value
            })
            targetSubAp.allNetWeight = e.target.value
        } else {
            targetSubAp.appliedSku[index].netWeight = e.target.value
        }
        setShowValidWarning(false)
        dispatch(updateSh(targetSubAp))
    }

    const onNetHeightChange = (e, index) => {
        const targetSubAp = p2m.sh;
        if ( p2m.sh.psAllSkuApplied == 'yes') {
            targetSubAp.appliedSku.map(item => {
                item.netHeight = e.target.value
            })
            targetSubAp.allNetHeight = e.target.value
        } else {
            console.log(p2m.sh.appliedSku[index].netHeight,"5555555555")
            targetSubAp.appliedSku[index].netHeight = e.target.value
        }
        setShowValidWarning(false)
        dispatch(updateSh(targetSubAp))
    }
    const onNetLengthChange = (e, index) => {
        const targetSubAp = p2m.sh;
        if ( p2m.sh.psAllSkuApplied == 'yes') {
            targetSubAp.appliedSku.map(item => {
                item.netLength = e.target.value
            })
            targetSubAp.allNetLength = e.target.value
        } else {
            targetSubAp.appliedSku[index].netLength = e.target.value
        }
        setShowValidWarning(false)
        dispatch(updateSh(targetSubAp))
    }
    const onNetWidthChange = (e, index) => {
        const targetSubAp = p2m.sh;
        if ( p2m.sh.psAllSkuApplied == 'yes') {
            targetSubAp.appliedSku.map(item => {
                item.netWidth = e.target.value
            })
            targetSubAp.allNetWidth =e.target.value
        } else {
            targetSubAp.appliedSku[index].netWidth = e.target.value
        }
        setShowValidWarning(false)
        dispatch(updateSh(targetSubAp))
    }

    const handleSaveComment = (content, index, target) => {
        const shipping = p2m.sh;
        shipping.p2mApplicationId = p2m.currentAp._id;
        const isComment = true;
        // const redirect = isComment ? '' : `/product/application/${id}`;
        const redirect = isComment ? '': '/product/application'

        if (target == 'shippingMethod') {
            shipping.comment.shippingMethod = content
        } else if (target == 'shippingInfo') {
            shipping.comment.shippingInfo = content
        }
        dispatch(saveP2MShipping(shipping,toaster,redirect))

    }
    const handleCommentChange = (content, index, target) => {
        const targetSubAp = p2m.sh;

        if (target == 'shippingMethod') {
            targetSubAp.comment.shippingMethod = content
        } else if (target == 'shippingInfo') {
            targetSubAp.comment.shippingInfo = content
        }

        dispatch(updateSh(targetSubAp))
    }

    const [showShippingInfo, setShowShippingInfoEditor] = useState(false)
    const [showShippingMethod, setShowShippingMethodEditor] = useState(false)

    const handleShowEditor = (target) => {
        switch(target) {
            case 'shippingMethod':
                return showShippingMethod ? setShowShippingMethodEditor(false): setShowShippingMethodEditor(true)
            case 'shippingInfo':
                return showShippingInfo ? setShowShippingInfoEditor(false) : setShowShippingInfoEditor(true)
            default:
                return null
        }
    }

    const smoothScroll = (target) => {
        var scrollContainer = target;
        do { //find scroll container
            scrollContainer = scrollContainer.parentNode;
            if (!scrollContainer) return;
            scrollContainer.scrollTop += 1;
        } while (scrollContainer.scrollTop == 0);

        var targetY = 0;
        do { //find the top of target relatively to the container
            if (target == scrollContainer) break;
            targetY += target.offsetTop;
            targetY = targetY - 50
        } while (target = target.offsetParent);

        scroll = function(c, a, b, i) {
            i++; if (i > 30) return;
            c.scrollTop = a + (b - a) / 30 * i;
            setTimeout(function(){ scroll(c, a, b, i); }, 20);
        }
        // start scrolling
        scroll(scrollContainer, scrollContainer.scrollTop, targetY, 0);
    }

    const [showValidWarning, setShowValidWarning] = useState(false)

    const handleCheckValid = () => {
        let checkValid = []
        console.log(p2m.sh.psAllSkuApplied)
        console.log(p2m.sh.ppsAllSkuApplied)
        console.log(p2m.sh.pnwAllSkuApplied)
        console.log(p2m.sh.ppwAllSkuApplied)
        if (p2m.sh.psAllSkuApplied === 'no'){
            p2m.sh.appliedSku.map(item => {
                item.netLength == ''|| item.netWidth == ''|| item.netHeight == ''
                    ? checkValid.push(false): null
            })
        }else{
            p2m.sh.allNetLength == '' || p2m.sh.allNetWidth == '' || p2m.sh.allNetHeight == ''
                    ? checkValid.push(false): null
        }
        if (p2m.sh.ppsAllSkuApplied === 'no'){
            p2m.sh.appliedSku.map(item => {
                item.includePackageLength == ''|| item.includePackageWidth == ''||
                    item.includePackageHeight == ''
                    ? checkValid.push(false): null
            })
        }else{
            p2m.sh.allIncludePackageLength == '' || p2m.sh.allIncludePackageWidth == '' || p2m.sh.allIncludePackageHeight == ''
                    ? checkValid.push(false): null
        }
        if (p2m.sh.pnwAllSkuApplied === 'no'){
            p2m.sh.appliedSku.map(item => {
                item.netWeight == ''
                    ? checkValid.push(false): null
            })
        }else{
            p2m.sh.allNetWeight == ''
                    ? checkValid.push(false): null
        }
        if (p2m.sh.ppwAllSkuApplied === 'no'){
            p2m.sh.appliedSku.map(item => {
                item.includePackageWeight == ''
                    ? checkValid.push(false): null
            })
        }else{
            p2m.sh.allIncludePackageWeight == ''
                    ? checkValid.push(false): null
        }

//        for (let index = 0; index < p2m.sh.appliedSku.length; index++) {
//            if(p2m.sh.appliedSku[index].netLength == ''){
//                checkValid.push(false)
//            }
//            if(p2m.sh.appliedSku[index].netWidth == ''){
//                checkValid.push(false)
//            }
//            if(p2m.sh.appliedSku[index].netHeight == ''){
//                checkValid.push(false)
//            }
//            if(p2m.sh.appliedSku[index].includePackageLength == ''){
//                checkValid.push(false)
//            }
//            if(p2m.sh.appliedSku[index].includePackageWidth == ''){
//                checkValid.push(false)
//            }
//            if(p2m.sh.appliedSku[index].includePackageHeight == ''){
//                checkValid.push(false)
//            }
//            if(p2m.sh.appliedSku[index].netWeight == ''){
//                checkValid.push(false)
//            }
//            if(p2m.sh.appliedSku[index].includePackageWeight == ''){
//                checkValid.push(false)
//            }
//        }
        if (checkValid.length !== 0) {
            setShowValidWarning(true)
        }
        if ((checkValid.length == 0)&&(p2m.sh.checkReturn !== false)) {
            setShowValidWarning(false)
            handleSave("Pending")
        }

    }
    const [check1reminder, setCheck1Reminder] = useState(false);
    const [check2reminder, setCheck2Reminder] = useState(false);
    const [reminder, setReminder] = useState(false);


    const handleCheck1Open = () => {
            setCheck1Reminder(true);
        }

    const handleCheck1Close = () => {
            setCheck1Reminder(false);
        }

    const handleCheck2Open = () => {
        setCheck2Reminder(true);
    }

    const handleCheck2Close = () => {
        setCheck2Reminder(false);
    }

    const handleReminderOpen = () => {
        setReminder(true);
    }

    const handleReminderClose = () => {
        setReminder(false);
    }

    const check1learnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-10%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleCheck1Close()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
                <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700', marginTop:'2%'}}><FormattedMessage id="shipping.notice" /></p>
            <p style={{lineHeight : '1.5rem'}}>
                <div className="section-line" style={{marginTop: '2%'}}></div>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li><FormattedMessage id="shipping.notice1" /></li>
                    <li><FormattedMessage id="shipping.notice2" /></li>
                    <li><FormattedMessage id="shipping.notice3" /></li>
                    <li><FormattedMessage id="shipping.notice4" /></li>
                    <li><FormattedMessage id="shipping.notice5" /></li>
                    <li><FormattedMessage id="shipping.notice6" /></li>
                </ul>
            </p>
        </div>

        );

    const check2learnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-10%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleCheck2Close()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
                <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700', marginTop:'2%'}}><FormattedMessage id="shipping.notice" /></p>
            <p style={{lineHeight : '1.5rem'}}>
                <div className="section-line" style={{marginTop: '2%'}}></div>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li><FormattedMessage id="shipping.notice1" /></li>
                    <li><FormattedMessage id="shipping.notice2" /></li>
                    <li><FormattedMessage id="shipping.notice3" /></li>
                    <li><FormattedMessage id="shipping.notice4" /></li>
                    <li><FormattedMessage id="shipping.notice5" /></li>
                    <li><FormattedMessage id="shipping.notice6" /></li>
                </ul>
            </p>
        </div>

        );

    const learnMoreModal = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '1%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-13%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleReminderClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
            <p style={{lineHeight : '1.5rem'}}>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li>如 SKU 各自擁有不同尺寸重量，請在「各 SKU 尺寸相同？」欄位點選「否」後，個別輸入 SKU 資料。</li>
                    <li>請務必在出貨前再次檢查此頁面資料。如您提供的資料與實際貨品不符，導致物流作業耗時、併貨與清關作業困難，我們將會給予警告。多次警告後我們將暫停您出貨。</li>
                    <li>由於通關、物流及市場倉儲作業中，會有海關相關或物流裝卸倉儲作業處理的評估及認定，DRS為有效及加速處理貨運中的所有問題，DRS擁有產品資料的最後裁量權。</li>
                    <li>依合約，如您違反而造成延遲損失、物流處理費及海關費用，這些費用將由貴公司支付。</li>
                </ul>
            </p>
        </div>

        );


    if (p2m.redirect !== '') {
        return <Redirect to={p2m.redirect}/>
    }

    return (
        <div>
            { p2m.isPending == 'loading' || saveTimeout || p2m.result == 'loading'
                ?   <div className="spinner-wrapper">
                        <div className="spinner"></div>
                        <div className="spinner-text"><span>Loading...</span></div>
                    </div>
                :   null
            }
            <div className="application-wrapper">
                <div className="application">
                    <div className="application-title-wrapper">
                        <p className="application-subtitle">
                            <FormattedMessage id="p2mapplication.form"/>
                            {p2m.currentAp.name}-<FormattedMessage id="subApplication.shippingTitle"/>
                        </p>
                        <div className="application-subtitle-info-wrapper">
                            <div className="application-info-column" style={{width: '48%'}}>
                                <div className="application-info">
                                    <p className="application-info-title" style={{width: '50%'}}>
                                        <FormattedMessage id="p2mapplication.formtype"/>
                                    </p>
                                    <p className="application-info-content">
                                        <FormattedMessage id="subApplication.shipping"/>
                                    </p>
                                </div>
                            </div>

                        </div>
                    </div>
                    <div className="section-line"></div>
                    <P2MApplicationInfo/>

                    <div className="section-line"></div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-title" style={{display: 'flex', justifyContent: 'space-between'}}>
                            <span>
                                <FormattedMessage id="shipping.title1"/>
                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                    *
                                </span>
                            </span>
                            { d.u.isSp
                                ? null
                                : p2m.currentAp.status == p2m.stMap.get(1)
                                    ? <button onClick={() => {
                                            handleShowEditor('shippingMethod');
                                            showShippingMethod ? null : smoothScroll(document.getElementById('shippingMethod'))
                                        }}><RateReviewIcon/></button>
                                    : null
                            }
                        </p>
                        <div className="application-intro-wrapper" style={{marginBottom : '20px'}}>
                            <p className="application-intro-paragraph">
                               <p> <FormattedMessage id="shipping.title1-1"/> </p>
                            <br/>
                                <p>目前暫不提供國際退運服務。</p>
                                    <p style={{marginTop: '1%'}}>存貨是否可供銷售由 FBA 當地倉庫判定，只要產品包裝外觀有損傷，不適合以「新品」狀態出售，會被 FBA 視為「不可供銷售庫存」。</p>
                            </p>
                        </div>
                        <div className="application-intro-wrapper">
                            <div className="application-terms-of-use">
                                <p><FormattedMessage id="shipping.paragraph1-1"/></p>
                                <div className="radio-btn-container" style={{ display: "flex", margin: '1.5% 1%'}}>
                                    <FormattedMessage  id="shipping.label1">
                                        { label =>
                                            <RadioButton
                                                changed={(e)=>checkHasLocalWarehouse(e) }
                                                id="1"
                                                isSelected={ p2m.sh.haslocalWarehouse === "no" }
                                                label={label}
                                                value="no"
                                                disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                            />
                                        }
                                    </FormattedMessage>
                                </div>
                                <div className="radio-btn-container" style={{display: 'flex', flexDirection: 'column', alignItem: 'center', margin: '1.5% 1%'}}>
                                    <div style={{display: 'flex'}}>
                                    <FormattedMessage  id="shipping.label2">
                                        { label =>
                                            <RadioButton
                                                changed={(e)=>checkHasLocalWarehouse(e) }
                                                id="2"
                                                isSelected={ p2m.sh.haslocalWarehouse === "yes" }
                                                label={label}
                                                value="yes"
                                                disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                            />
                                        }
                                    </FormattedMessage>
                                    <span style={{color: '#51cbce' , cursor : 'pointer', marginTop: '0.5%' , fontSize: '15px'}} onClick={()=>{handleCheck1Open()}}>
                                        <i class="fa fa-question-circle"></i>
                                        {/* <FormattedMessage id ="basedata.learnmore"></FormattedMessage>  */}
                                        <FormattedMessage id="basedata.instruction"></FormattedMessage>
                                    </span>
                                    </div>

                                    <Modal
                                        open={check1reminder}
                                        onClose={handleCheck1Close}
                                        aria-labelledby="simple-modal-title"
                                        aria-describedby="simple-modal-description"
                                        style={{overflow: 'scroll'}}
                                    >
                                      {check1learnMore}
                                    </Modal>
                                    { p2m.sh.haslocalWarehouse === 'yes'
                                        ?
                                        <div>
                                        <div style={{width: '100%', backgroundColor: '#f5f6fb',borderRadius: '4px',border: '1px solid #e6e9ed',padding: '3%', marginTop: '3%'}}>
                                                <div style={{paddingBottom: '5px', borderBottom: '1px solid #e6e9ed',margin: '1% 0 4% 0'}}>
                                                    <p style={{fontSize: '14px', lineHeight: '1.5rem', color: '#5a607f'}}>收件地址</p>
                                                </div>
                                                <div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.address1"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.address1">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.address1}
                                                                            onChange={(e) => handleAddress1Change(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.address2"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.address2">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.address2}
                                                                            onChange={(e) => handleAddress2Change(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.city"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.city">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.city}
                                                                            onChange={(e) => handleCityChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.stateorprovince"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.stateorprovince">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.stateorprovince}
                                                                            onChange={(e) => handleStateOrProvinceChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.ziporpostalcode"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.ziporpostalcode">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.ziporpostalcode}
                                                                            onChange={(e) => handleZipOrPostalcodeChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.country"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.country">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.country}
                                                                            onChange={(e) => handleCountryChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div style={{paddingBottom: '5px', borderBottom: '1px solid #e6e9ed', margin: '2% 0 4% 0'}}>
                                                    <p style={{fontSize: '14px', lineHeight: '1.5rem',color: '#5a607f'}}>收件人聯繫資料</p>
                                                </div>
                                                <div className="base-input-column-wrapper">
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.fullname"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.fullname">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.fullname}
                                                                            onChange={(e) => handleFullnameChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.phonenumber"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.phonenumber">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.phonenumber}
                                                                            onChange={(e) => handlePhonenumberChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.emailaddress"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.emailaddress">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.returnAddr.emailaddress}
                                                                            onChange={(e) => handleEmailaddressChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            </div>
                                        :   null
                                    }
                                </div>
                            </div>
                        </div>
                        <p style={{marginTop: '2%', marginLeft:'1%',fontSize:'16px', marginBottom : '12px'}}>
                            如果您希望對於「可供銷售存貨」提供不同地址與代理人，請在下方輸入，如果地址相同請輸入與「不可供銷售存貨」一樣之收件地址即可：
                        </p>

                        <div className="application-intro-wrapper">
                            <div className="application-terms-of-use">
                                <p><FormattedMessage id="shipping.paragraph1-2"/></p>
                                <div className="radio-btn-container" style={{ display: "flex", margin: '1.5% 1%'}}>
                                    <FormattedMessage  id="shipping.label1">
                                        { label =>
                                            <RadioButton
                                                changed={(e)=>checkAvailableHasLocalWarehouse(e) }
                                                id="3"
                                                isSelected={ p2m.sh.availableHaslocalWarehouse === "no" }
                                                label={label}
                                                value="no"
                                                disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                            />
                                        }
                                    </FormattedMessage>
                                </div>
                                <div className="radio-btn-container" style={{display: 'flex', flexDirection: 'column', alignItem: 'center', margin: '1.5% 1%'}}>
                                    <div style={{display: 'flex'}}>
                                    <FormattedMessage  id="shipping.label2">
                                        { label =>
                                            <RadioButton
                                                changed={(e)=>checkAvailableHasLocalWarehouse(e) }
                                                id="4"
                                                isSelected={ p2m.sh.availableHaslocalWarehouse === "yes" }
                                                label={label}
                                                value="yes"
                                                disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                            />
                                        }
                                    </FormattedMessage>
                                    <span style={{color: '#51cbce' , cursor : 'pointer', marginTop: '0.5%' , fontSize: '15px'}} onClick={()=>{handleCheck2Open()}}>
                                        <i class="fa fa-question-circle"></i>
                                        <FormattedMessage id ="basedata.learnmore"></FormattedMessage>
                                    </span>
                                    </div>

                                    <Modal
                                        open={check2reminder}
                                        onClose={handleCheck2Close}
                                        aria-labelledby="simple-modal-title"
                                        aria-describedby="simple-modal-description"
                                        style={{overflow: 'scroll'}}
                                    >
                                      {check2learnMore}
                                    </Modal>
                                    { p2m.sh.availableHaslocalWarehouse === 'yes'
                                        ?
                                        <div>
                                        <div style={{width: '100%', backgroundColor: '#f5f6fb',borderRadius: '4px',border: '1px solid #e6e9ed',padding: '3%', marginTop: '3%'}}>
                                                <div style={{paddingBottom: '5px', borderBottom: '1px solid #e6e9ed',margin: '1% 0 4% 0'}}>
                                                    <p style={{fontSize: '14px', lineHeight: '1.5rem', color: '#5a607f'}}>收件地址</p>
                                                </div>
                                                <div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.address1"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.address1">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availableAddress1}
                                                                            onChange={(e) => handleAvailableAddress1Change(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.address2"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.address2">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availableAddress2}
                                                                            onChange={(e) => handleAvailableAddress2Change(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.city"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.city">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availableCity}
                                                                            onChange={(e) => handleAvailableCityChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.stateorprovince"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.stateorprovince">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availableStateorprovince}
                                                                            onChange={(e) => handleAvailableStateOrProvinceChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.ziporpostalcode"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.ziporpostalcode">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availableZiporpostalcode}
                                                                            onChange={(e) => handleAvailableZipOrPostalcodeChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.country"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.country">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availableCountry}
                                                                            onChange={(e) => handleAvailableCountryChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div style={{paddingBottom: '5px', borderBottom: '1px solid #e6e9ed', margin: '2% 0 4% 0'}}>
                                                    <p style={{fontSize: '14px', lineHeight: '1.5rem',color: '#5a607f'}}>收件人聯繫資料</p>
                                                </div>
                                                <div className="base-input-column-wrapper">
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.fullname"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.fullname">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availableFullname}
                                                                            onChange={(e) => handleAvailableFullnameChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.phonenumber"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.phonenumber">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availablePhonenumber}
                                                                            onChange={(e) => handleAvailablePhonenumberChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="shipping.emailaddress"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.emailaddress">
                                                                { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            placeholder={placeholder}
                                                                            value={p2m.sh.availableReturnAddr.availableEmailaddress}
                                                                            onChange={(e) => handleAvailableEmailaddressChange(e)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            </div>
                                        :   null
                                    }
                                </div>
                            </div>

                            <div className="checkbox-inline-wrapper" style={{width: '100%',justifyContent: 'flex-end', marginTop: '1%',paddingRight: '1%'}}>
                                <CheckBox
                                    isSelected={p2m.sh.checkReturn}
                                    onSelect={onCheckReturn}
                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                />
                                <span className="checkbox-inline-text">
                                    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                        *
                                    </span>
                                    <FormattedMessage id="shipping.confirm"/>
                                </span>
                                { p2m.sh.checkReturn === false
                                    ?   <div style={{marginLeft: '10px'}}>
                                            <p className="base-input-notice drs-blue">
                                                <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                                                <FormattedMessage id="selectField.required"/>
                                            </p>
                                        </div>
                                    :   null
                                }
                            </div>
                        </div>
                        <div style={{padding: '2%'}} id="shippingMethod">
                            <Comment
                                comment={p2m.sh.comment.shippingMethod}
                                handleSaveComment={handleSaveComment}
                                handleCommentChange={handleCommentChange}
                                edit={showShippingMethod}
                                handleShow={handleShowEditor}
                                target='shippingMethod'
                            />
                        </div>
                    </div>
                    <div className="section-line"></div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-title" style={{display: 'flex', justifyContent: 'space-between'}}>
                            <span>
                                <FormattedMessage id="shipping.title2"/>
                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                    *
                                </span>
                            </span>
                            { d.u.isSp
                                ? null
                                : p2m.currentAp.status == p2m.stMap.get(1)
                                    ? <button onClick={() => {
                                        handleShowEditor('shippingInfo');
                                        showShippingInfo ? null : smoothScroll(document.getElementById('shippingInfo'))
                                        }}><RateReviewIcon/></button>
                                    : null
                            }
                        </p>
                        <div className="application-intro-wrapper">
                            <p className="application-intro-paragraph">
                                <FormattedMessage id="shipping.paragraph2"/>
                                <span style={{color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{handleReminderOpen()}}>
                                    <i class="fa fa-question-circle"></i>
                                    <FormattedMessage id ="basedata.learnmore"></FormattedMessage>
                                </span>
                            </p>
                        </div>

                        <Modal
                            open={reminder}
                            onClose={handleReminderClose}
                            aria-labelledby="simple-modal-title"
                            aria-describedby="simple-modal-description"
                        >
                          {learnMoreModal}
                        </Modal>
                        <div className="application-info-section column align-center mt-1">
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        <FormattedMessage id="shipping.subtitle1"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    <div className="radio-btn-container" style={{ display: "flex" }}>
                                        <span><FormattedMessage id="shipping.question1"/></span>
                                        <FormattedMessage id="shipping.labelno">
                                            { label =>
                                                <RadioButton
                                                    changed={ (e)=> checkPSAllSkuApplied(e)}
                                                    id="11"
                                                    isSelected={ p2m.sh.psAllSkuApplied === "no" }
                                                    label={label}
                                                    value="no"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                                />
                                            }
                                        </FormattedMessage>
                                        <FormattedMessage id="shipping.labelyes">
                                            { label =>
                                                <RadioButton
                                                    changed={ (e)=> checkPSAllSkuApplied(e)}
                                                    id="12"
                                                    isSelected={ p2m.sh.psAllSkuApplied === "yes" }
                                                    label={label}
                                                    value="yes"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                                />
                                            }
                                        </FormattedMessage>
                                    </div>
                                </p>
                                { p2m.sh.psAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper">
                                        {   p2m.sh.appliedSku.map((item,index) => {
                                            return (
                                                <div className="application-input-section">
                                                    <div>
                                                        <div className="application-input-section-body" style={{width: '100%'}}>
                                                            <div className="application-input-section-head" style={{width: '30%'}}>
                                                                <p>{item.skuCode}</p>
                                                            </div>
                                                            <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                                <FormattedMessage id="shipping.length">
                                                                    { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            style={{width: '80%'}}
                                                                            placeholder={placeholder}
                                                                            value={item.netLength}
                                                                            onChange={(e) => onNetLengthChange(e, index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                    }
                                                                </FormattedMessage>
                                                                <span className="base-input-label-inline" style={{fontSize: '13px', width: '18%'}}>
                                                                    <FormattedMessage id="shipping.centimeter"/>
                                                                </span>
                                                                <span style={{marginRight: '8px'}}>x</span>
                                                            </div>
                                                            <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                                <FormattedMessage  id="shipping.width">
                                                                    { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            style={{width: '80%'}}
                                                                            placeholder={placeholder}
                                                                            value={item.netWidth}
                                                                            onChange={(e) => onNetWidthChange(e, index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                    }
                                                                </FormattedMessage>
                                                                <span className="base-input-label-inline" style={{fontSize: '13px', width: '18%'}}>
                                                                    <FormattedMessage id="shipping.centimeter"/>
                                                                </span>
                                                                <span style={{marginRight: '8px'}}>x</span>
                                                            </div>
                                                            <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                                <FormattedMessage  id="shipping.height">
                                                                    { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            style={{width: '80%'}}
                                                                            placeholder={placeholder}
                                                                            value={item.netHeight}
                                                                            onChange={(e) => onNetHeightChange(e, index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                    }
                                                                </FormattedMessage>
                                                                <span className="base-input-label-inline" style={{fontSize: '13px', width: '18%'}}>
                                                                    <FormattedMessage id="shipping.centimeter"/>
                                                                </span>
                                                            </div>
                                                            { item.netLength == '' || item.netWidth == '' || item.netHeight == ''
                                                                ?   <span className="drs-blue" style={{width: '25%', fontSize: '14px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="inputField.required"/>
                                                                    </span>
                                                                :   null
                                                            }
                                                        </div>
                                                        <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                            <div style={{marginLeft: '82px', width: '100%'}}>
                                                                {item.netLength!== '' && !/^[0-9-. ]+$/.test(item.netLength)
                                                                    ?   <p className="base-input-notice drs-notice-red">
                                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                                請輸入數字
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                            <div style={{marginLeft: '3px', width: '100%'}}>
                                                                {item.netWidth!== '' && !/^[0-9-. ]+$/.test(item.netWidth)
                                                                    ?   <p className="base-input-notice drs-notice-red">
                                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                                請輸入數字
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                            <div style={{width: '100%'}}>
                                                                {item.netHeight!== '' && !/^[0-9-. ]+$/.test(item.netHeight)
                                                                    ?   <p className="base-input-notice drs-notice-red">
                                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                                請輸入數字
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            )
                                        })}
                                        </div>
                                    :   <div className="application-input-section-wrapper">
                                            <div className="application-input-section">
                                                <div>
                                                    <div className="application-input-section-body" style={{width: '100%'}}>
                                                        <div className="application-input-section-head" style={{width: '30%'}}>
                                                            <p><FormattedMessage id="shipping.allsku"/></p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage  id="shipping.length">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        style={{width: '80%'}}
                                                                        placeholder={placeholder}
                                                                        value={p2m.sh.allNetLength}
                                                                        onChange={(e)=> onNetLengthChange(e)}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline" style={{fontSize: '13px', width: '18%'}}>
                                                                <FormattedMessage id="shipping.centimeter"/>
                                                            </span>
                                                            <span style={{marginRight: '8px'}}>x</span>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.width">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        style={{width: '80%'}}
                                                                        placeholder={placeholder}
                                                                        value={p2m.sh.allNetWidth}
                                                                        onChange={(e) => onNetWidthChange(e)}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline" style={{fontSize: '13px', width: '18%'}}>
                                                                <FormattedMessage id="shipping.centimeter"/>
                                                            </span>
                                                            <span style={{marginRight: '8px'}}>x</span>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.height">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        style={{width: '80%'}}
                                                                        placeholder={placeholder}
                                                                        value={p2m.sh.allNetHeight}
                                                                        onChange={(e) => onNetHeightChange(e)}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline" style={{fontSize: '13px', width: '18%'}}>
                                                                <FormattedMessage id="shipping.centimeter"/>
                                                            </span>
                                                        </div>
                                                        { p2m.sh.allNetLength == '' || p2m.sh.allNetWidth == '' || p2m.sh.allNetHeight == ''
                                                            ?   <span className="drs-blue" style={{width: '25%', fontSize: '14px'}}>
                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                    <FormattedMessage id="inputField.required"/>
                                                                </span>
                                                            :   null
                                                        }
                                                    </div>
                                                    <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                        <div style={{marginLeft: '82px', width: '100%'}}>
                                                            {p2m.sh.allNetLength!== '' && !/^[0-9-. ]+$/.test(p2m.sh.allNetLength)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                        <div style={{marginLeft: '3px', width: '100%'}}>
                                                            {p2m.sh.allNetWidth!== '' && !/^[0-9-. ]+$/.test(p2m.sh.allNetWidth)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                        <div style={{width: '100%'}}>
                                                            {p2m.sh.allNetHeight!== '' && !/^[0-9-. ]+$/.test(p2m.sh.allNetHeight)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    }

                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        <FormattedMessage id="shipping.subtitle2"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    <div className="radio-btn-container" style={{ display: "flex" }}>
                                        <span><FormattedMessage id="shipping.question2"/></span>
                                        <FormattedMessage id="shipping.labelno">
                                            { label =>
                                                <RadioButton
                                                    changed={ (e) => checkPPSAllSkuApplied(e)}
                                                    id="5"
                                                    isSelected={ p2m.sh.ppsAllSkuApplied === "no" }
                                                    label={label}
                                                    value="no"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                                />
                                            }
                                        </FormattedMessage>
                                        <FormattedMessage id="shipping.labelyes">
                                            { label =>
                                                <RadioButton
                                                    changed={ (e)=> checkPPSAllSkuApplied(e)}
                                                    id="6"
                                                    isSelected={ p2m.sh.ppsAllSkuApplied === "yes" }
                                                    label={label}
                                                    value="yes"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                                />
                                            }
                                        </FormattedMessage>
                                    </div>

                                </p>
                                { p2m.sh.ppsAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper">
                                        {   p2m.sh.appliedSku.map((item, index) => {
                                            return (
                                                <div className="application-input-section">
                                                    <div>
                                                        <div className="application-input-section-body" style={{width: '100%'}}>
                                                            <div className="application-input-section-head" style={{width: '30%'}}>
                                                                <p>{item.skuCode}</p>
                                                            </div>
                                                            <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                                <FormattedMessage  id="shipping.length">
                                                                    { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            style={{width: '80%'}}
                                                                            placeholder={placeholder}
                                                                            value={item.includePackageLength}
                                                                            onChange={(e) => onIncludePackageLengthChange(e, index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                    }
                                                                </FormattedMessage>
                                                                <span className="base-input-label-inline" style={{width: '18%', fontSize: '13px'}}>
                                                                    <FormattedMessage id="shipping.centimeter"/>
                                                                </span>
                                                                <span style={{marginRight: '8px'}}>x</span>
                                                            </div>
                                                            <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                                <FormattedMessage id="shipping.width">
                                                                    { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            style={{width: '80%'}}
                                                                            placeholder={placeholder}
                                                                            value={item.includePackageWidth}
                                                                            onChange={(e) => onIncludePackageWidthChange(e, index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                    }
                                                                </FormattedMessage>
                                                                <span className="base-input-label-inline" style={{width: '18%', fontSize: '13px'}}>
                                                                    <FormattedMessage id="shipping.centimeter"/>
                                                                </span>
                                                                <span style={{marginRight: '8px'}}>x</span>
                                                            </div>
                                                            <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                                <FormattedMessage id="shipping.height">
                                                                    { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            style={{width: '80%'}}
                                                                            placeholder={placeholder}
                                                                            value={item.includePackageHeight}
                                                                            onChange={(e) => onIncludePackageHeightChange(e, index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                    }
                                                                </FormattedMessage>
                                                                <span className="base-input-label-inline" style={{fontSize: '13px', width: '18%'}}>
                                                                    <FormattedMessage id="shipping.centimeter"/>
                                                                </span>
                                                            </div>
                                                            { item.includePackageLength == '' || item.includePackageWidth == '' || item.includePackageHeight == ''
                                                                ?   <span className="drs-blue" style={{width: '25%', fontSize: '14px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="inputField.required"/>
                                                                    </span>
                                                                :   null
                                                            }
                                                        </div>
                                                        <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                            <div style={{marginLeft: '82px', width: '100%'}}>
                                                                {item.includePackageLength!== '' && !/^[0-9-. ]+$/.test(item.includePackageLength)
                                                                    ?   <p className="base-input-notice drs-notice-red">
                                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                                請輸入數字
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                            <div style={{marginLeft: '3px', width: '100%'}}>
                                                                {item.includePackageWidth!== '' && !/^[0-9-. ]+$/.test(item.includePackageWidth)
                                                                    ?   <p className="base-input-notice drs-notice-red">
                                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                                請輸入數字
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                            <div style={{width: '100%'}}>
                                                                {item.includePackageHeight!== '' && !/^[0-9-. ]+$/.test(item.includePackageHeight)
                                                                    ?   <p className="base-input-notice drs-notice-red">
                                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                                請輸入數字
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            )
                                        })}
                                        </div>
                                    :   <div className="application-input-section-wrapper">
                                            <div className='application-input-section'>
                                                <div>
                                                    <div className="application-input-section-body" style={{width: '100%'}}>
                                                        <div className="application-input-section-head" style={{width: '30%'}}>
                                                            <p><FormattedMessage id="shipping.allsku"/></p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.length">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        width={{width: '80%'}}
                                                                        placeholder={placeholder}
                                                                        value={p2m.sh.allIncludePackageLength}
                                                                        onChange={(e) => onIncludePackageLengthChange(e)}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline" style={{width: '18%', fontSize: '13px'}}>
                                                                <FormattedMessage id="shipping.centimeter"/>
                                                            </span>
                                                            <span style={{marginRight: '8px'}}>x</span>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.width">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        style={{width: '80%'}}
                                                                        placeholder={placeholder}
                                                                        value={p2m.sh.allIncludePackageWidth}
                                                                        onChange={(e) => onIncludePackageWidthChange(e)}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline" style={{width: '18%', fontSize: '13px'}}>
                                                                <FormattedMessage id="shipping.centimeter"/>
                                                            </span>
                                                            <span style={{marginRight: '8px'}}>x</span>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage id="shipping.height">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        style={{width: '80%'}}
                                                                        placeholder={placeholder}
                                                                        onChange={(e) => onIncludePackageHeightChange(e)}
                                                                        value={p2m.sh.allIncludePackageHeight}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline" style={{width: '18%', fontSize: '13px'}}>
                                                                <FormattedMessage id="shipping.centimeter"/>
                                                            </span>
                                                        </div>
                                                        { p2m.sh.allIncludePackageLength == '' || p2m.sh.allIncludePackageWidth == '' || p2m.sh.allIncludePackageHeight == ''
                                                            ?   <span className="drs-blue" style={{width: '25%', fontSize: '14px'}}>
                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                    <FormattedMessage id="inputField.required"/>
                                                                </span>
                                                            :   null
                                                        }
                                                    </div>
                                                    <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                        <div style={{marginLeft: '82px', width: '100%'}}>
                                                            {p2m.sh.allIncludePackageLength!== '' && !/^[0-9-. ]+$/.test(p2m.sh.allIncludePackageLength)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                        <div style={{marginLeft: '3px', width: '100%'}}>
                                                            {p2m.sh.allIncludePackageWidth!== '' && !/^[0-9-. ]+$/.test(p2m.sh.allIncludePackageWidth)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                        <div style={{width: '100%'}}>
                                                            {p2m.sh.allIncludePackageHeight!== '' && !/^[0-9-. ]+$/.test(p2m.sh.allIncludePackageHeight)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                }
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        <FormattedMessage id="shipping.subtitle3"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    <div className="radio-btn-container" style={{ display: "flex" }}>
                                        <span>
                                            <FormattedMessage id="shipping.question3"/>
                                        </span>
                                        <FormattedMessage id="shipping.labelno">
                                            { label =>
                                                <RadioButton
                                                    changed={ (e) => checkPNWAllSkuApplied(e) }
                                                    id="9"
                                                    isSelected={ p2m.sh.pnwAllSkuApplied === "no" }
                                                    label={label}
                                                    value="no"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                                />
                                            }
                                        </FormattedMessage>
                                        <FormattedMessage id="shipping.labelyes">
                                            { label =>
                                                <RadioButton
                                                    changed={ (e) => checkPNWAllSkuApplied(e) }
                                                    id="10"
                                                    isSelected={p2m.sh.pnwAllSkuApplied === "yes" }
                                                    label={label}
                                                    value="yes"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                                />
                                            }
                                        </FormattedMessage>
                                    </div>

                                </p>
                                { p2m.sh.pnwAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper">
                                        {   p2m.sh.appliedSku.map((item, index) => {
                                            return (
                                                <div className="application-input-section">
                                                    <div style={{width: '40%'}}>
                                                        <div className="application-input-section-body" style={{width: '100%'}}>
                                                            <div className="application-input-section-head" style={{width: '50%'}}>
                                                                <p>{item.skuCode}</p>
                                                            </div>
                                                            <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                                <FormattedMessage  id="shipping.weight">
                                                                    { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            style={{width: '80%'}}
                                                                            placeholder={placeholder}
                                                                            value={item.netWeight}
                                                                            onChange={(e) => onNetWeightChange(e,index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                    }
                                                                </FormattedMessage>
                                                                <span className="base-input-label-inline" style={{width: '18%', fontSize: '13px'}}>
                                                                    <FormattedMessage id="shipping.gram"/>
                                                                </span>
                                                            </div>
                                                            { item.netWeight == ''
                                                                ?   <span className="drs-blue" style={{width: '25%',fontSize: '14px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="inputField.required"/>
                                                                    </span>
                                                                :   null
                                                            }
                                                        </div>
                                                        <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                            <div style={{marginLeft: '82px', width: '100%'}}>
                                                                {item.netWeight!== '' && !/^[0-9-. ]+$/.test(item.netWeight)
                                                                    ?   <p className="base-input-notice drs-notice-red">
                                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                                請輸入數字
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            )
                                        })}
                                        </div>

                                    :   <div className="application-input-section-wrapper">
                                            <div className='application-input-section'>
                                                <div>
                                                    <div className="application-input-section-body" style={{width: '100%'}}>
                                                        <div className="application-input-section-head" style={{width: '31%'}}>
                                                            <p><FormattedMessage id="shipping.allsku"/></p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage  id="shipping.weight">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        style={{width: '80%'}}
                                                                        placeholder={placeholder}
                                                                        value={p2m.sh.allNetWeight}
                                                                        onChange={(e) => onNetWeightChange(e)}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline" style={{width: '18%', fontSize: '13px'}}>
                                                                <FormattedMessage id="shipping.gram"/>
                                                            </span>
                                                        </div>
                                                        { p2m.sh.allNetWeight == ''
                                                            ?   <span className="drs-blue" style={{width: '25%', fontSize: '14px'}}>
                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                    <FormattedMessage id="inputField.required"/>
                                                                </span>
                                                            :   null
                                                        }
                                                    </div>
                                                    <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                        <div style={{marginLeft: '82px', width: '100%'}}>
                                                            {p2m.sh.allNetWeight!== '' && !/^[0-9-. ]+$/.test(p2m.sh.allNetWeight)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                }
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        <FormattedMessage id="shipping.subtitle4"/>
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                    <div className="radio-btn-container" style={{ display: "flex" }}>
                                        <span>
                                            <FormattedMessage id="shipping.question4"/>
                                        </span>
                                        <FormattedMessage id="shipping.labelno">
                                            { label =>
                                                <RadioButton
                                                    changed={ (e) => checkPPWAllSkuApplied(e) }
                                                    id="7"
                                                    isSelected={ p2m.sh.ppwAllSkuApplied === "no" }
                                                    label={label}
                                                    value="no"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                                />
                                            }
                                        </FormattedMessage>
                                        <FormattedMessage id="shipping.labelyes">
                                            { label =>
                                                <RadioButton
                                                    changed={ (e) => checkPPWAllSkuApplied(e) }
                                                    id="8"
                                                    isSelected={p2m.sh.ppwAllSkuApplied === "yes" }
                                                    label={label}
                                                    value="yes"
                                                    disabled={p2m.currentAp.status !== p2m.stMap.get(0) || d.u.isSp == false}
                                                />
                                            }
                                        </FormattedMessage>
                                    </div>
                                </p>
                                { p2m.sh.ppwAllSkuApplied === 'no'
                                    ?   <div className="application-input-section-wrapper">
                                        {   p2m.sh.appliedSku.map((item, index) => {
                                            return (
                                                <div className="application-input-section">
                                                    <div style={{width: '40%'}}>
                                                        <div className="application-input-section-body" style={{width: '100%'}}>
                                                            <div className="application-input-section-head" style={{width: '50%'}}>
                                                                <p>{item.skuCode}</p>
                                                            </div>
                                                            <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                                <FormattedMessage  id="shipping.weight">
                                                                    { placeholder =>
                                                                        <input
                                                                            className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            style={{width: '80%'}}
                                                                            placeholder={placeholder}
                                                                            value={item.includePackageWeight}
                                                                            onChange={(e) => onIncludePackageWeightChange(e,index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </input>
                                                                    }
                                                                </FormattedMessage>
                                                                <span className="base-input-label-inline" style={{width: '18%', fontSize: '13px'}}>
                                                                    <FormattedMessage id="shipping.gram"/>
                                                                </span>
                                                            </div>
                                                            { item.includePackageWeight == ''
                                                                ?   <span className="drs-blue" style={{width: '25%',fontSize: '14px'}}>
                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                        <FormattedMessage id="inputField.required"/>
                                                                    </span>
                                                                :   null
                                                            }
                                                        </div>
                                                        <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                            <div style={{marginLeft: '82px', width: '100%'}}>
                                                                {item.includePackageWeight!== '' && !/^[0-9-. ]+$/.test(item.includePackageWeight)
                                                                    ?   <p className="base-input-notice drs-notice-red">
                                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                                請輸入數字
                                                                        </p>
                                                                    :   null
                                                                }
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                )
                                            })}
                                        </div>

                                    :   <div className="application-input-section-wrapper">
                                            <div className='application-input-section'>
                                                <div>
                                                    <div className="application-input-section-body" style={{width: '100%'}}>
                                                        <div className="application-input-section-head" style={{width: '31%'}}>
                                                            <p><FormattedMessage id="shipping.allsku"/></p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <FormattedMessage  id="shipping.weight">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        style={{width: '80%'}}
                                                                        placeholder={placeholder}
                                                                        value={p2m.sh.allIncludePackageWeight}
                                                                        onChange={(e) => onIncludePackageWeightChange(e)}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    >
                                                                    </input>
                                                                }
                                                            </FormattedMessage>
                                                            <span className="base-input-label-inline" style={{fontSize: '13px', width: '18%'}}>
                                                                <FormattedMessage id="shipping.gram"/>
                                                            </span>
                                                        </div>
                                                        { p2m.sh.allIncludePackageWeight == ''
                                                            ?   <span className="drs-blue" style={{width: '25%', fontSize: '14px'}}>
                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                    <FormattedMessage id="inputField.required"/>
                                                                </span>
                                                            :   null
                                                        }
                                                    </div>
                                                    <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                        <div style={{marginLeft: '82px', width: '100%'}}>
                                                            {p2m.sh.allIncludePackageWeight!== '' && !/^[0-9-. ]+$/.test(p2m.sh.allIncludePackageWeight)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                }
                            </div>
                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                <p className="application-intro-paragraph enhance space-between" style={{marginBottom: '0px'}}>
                                    <span>
                                        產品FCA價格（未稅）
                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </span>
                                </p>
                                <div className="application-input-section-wrapper">
                                    {   p2m.sh.appliedSku.map((item, index) => {
                                        return (
                                            <div className="application-input-section">
                                                <div style={{width: '40%'}}>
                                                    <div className="application-input-section-body" style={{width: '100%'}}>
                                                        <div className="application-input-section-head" style={{width: '50%'}}>
                                                            <p>{item.skuCode}</p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper" style={{width: '100%'}}>
                                                            <span className="base-input-label-inline" style={{width: '12%', fontSize: '15px',marginRight: '0px'}}>
                                                                NT$
                                                            </span>
                                                            <input
                                                                className={`base-input base-input-inline ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                style={{width: '80%'}}
                                                                value={item.fcaPrice}
                                                                onChange={(e) => onFcaPriceChange(e,index)}
                                                                readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                            >
                                                            </input>
                                                        </div>
                                                        {/*{ item.fcaPrice == ''
                                                            ?   <span className="drs-blue" style={{width: '25%',fontSize: '14px'}}>
                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                    <FormattedMessage id="inputField.required"/>
                                                                </span>
                                                            :   null
                                                        }*/}
                                                    </div>
                                                    <div className="application-input-section-body" style={{marginBottom: '1%', width: '100%'}}>
                                                        <div style={{marginLeft: '170px', width: '100%'}}>
                                                            {/*{item.fcaPrice!== '' && !/^[0-9-. ]+$/.test(item.fcaPrice)
                                                                ?   <p className="base-input-notice drs-notice-red">
                                                                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px', color: '#F0142F'}} aria-hidden="true"></i>
                                                                            請輸入數字
                                                                    </p>
                                                                :   null
                                                            }*/}
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            )
                                        })}
                                </div>
                            </div>
                        </div>
                        <div style={{padding: '2%'}} id="shippingInfo">
                            <Comment
                                comment={p2m.sh.comment.shippingInfo}
                                handleSaveComment={handleSaveComment}
                                handleCommentChange={handleCommentChange}
                                edit={showShippingInfo}
                                handleShow={handleShowEditor}
                                target='shippingInfo'
                            />
                        </div>
                        <div style={{borderTop: '1px dashed #1976f5',margin: '3% 1%'}}></div>
                        <div className="application-info-row" style={{padding: '0 1%'}}>
                            <table role="table" className="table-main table">
                                <thead>
                                    <tr className="application-table-thead-tr">
                                        <th>SKUs</th>
                                        <th><FormattedMessage id="shipping.productsize"/></th>
                                        <th><FormattedMessage id="shipping.thesize"/></th>
                                        <th><FormattedMessage id="shipping.netweight"/></th>
                                        <th><FormattedMessage id="shipping.theweight"/></th>
                                        <th>FCA價格（未稅）</th>

                                    </tr>
                                </thead>
                                <tbody className="table-tbody" role="rowgroup">
                                    { p2m.sh.appliedSku.map(item => (
                                        <tr className="table-tbody-tr">
                                            <td>{item.skuCode}</td>
                                            { p2m.sh.psAllSkuApplied === 'no'
                                                ? <td>{`${item.netLength}cm x ${item.netWidth}cm x ${item.netHeight}cm`}</td>
                                                : <td>{`${p2m.sh.allNetLength}cm x ${p2m.sh.allNetWidth}cm x ${p2m.sh.allNetHeight}cm`}</td>
                                            }
                                            { p2m.sh.ppsAllSkuApplied === 'no'
                                                ? <td>{`${item.includePackageLength}cm x ${item.includePackageWidth}cm x ${item.includePackageHeight}cm`}</td>
                                                : <td>{`${p2m.sh.allIncludePackageLength}cm x ${p2m.sh.allIncludePackageWidth}cm x ${p2m.sh.allIncludePackageHeight}cm`}</td>
                                            }
                                            { p2m.sh.pnwAllSkuApplied === 'no'
                                                ? <td>{item.netWeight} g</td>
                                                : <td>{p2m.sh.allNetWeight} g</td>
                                            }
                                            { p2m.sh.ppwAllSkuApplied === 'no'
                                                ? <td>{item.includePackageWeight} g</td>
                                                : <td>{p2m.sh.allIncludePackageWeight} g</td>
                                            }
                                            <td>NT$ {item.fcaPrice}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div className="section-line"></div>
                    <div className="operation-pannel-footer">
                            { p2m.sh.checkReturn == false
                                ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                        <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                            <FormattedMessage id="shipping.warning"/>
                                        </p>
                                    </div>
                                :   <p></p>
                            }
                            {showValidWarning
                                ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                        <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                            <FormattedMessage id="shipping.warning2"/>
                                        </p>
                                    </div>
                                :   null
                            }
                        <div className="operation-btn-group"  style={{display:'flex'}}>
                            { d.u.isSp && p2m.currentAp.status == p2m.stMap.get(0)
                                ?   <button className="drs-btn" style={{backgroundColor:'#0085CC'}} onClick={() => {handleSave("Draft")}}>
                                        儲存草稿
                                    </button>
                                :   null
                            }
                            { d.u.isSp && p2m.currentAp.status == p2m.stMap.get(0)
                                ?   <button className="drs-btn drs-btn-cta" onClick={() => {handleCheckValid()}}>
                                        <FormattedMessage id="save"/>
                                    </button>
                                // :   <Link to={`/product/application/${id}`}>
                                :   <Link to='/product/application'>
                                        <button className="drs-btn drs-btn-normal">
                                            <FormattedMessage id="back"/>
                                        </button>
                                    </Link>
                            }
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
    )
}
export default Shipping