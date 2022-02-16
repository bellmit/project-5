import React, { useEffect, useState, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import JoditEditor from "jodit-react";
import CheckBox from '../common/Checkbox/CheckBox'
import RadioButton from '../common/RadioButton'
import Select from 'react-select';
import DatePicker from 'react-datepicker'
import "react-datepicker/dist/react-datepicker.css";
import { initP2MInsurance, saveP2MInsurance, updateIns, getExactApplication } from '../../../actions';
import {injectIntl,FormattedMessage} from 'react-intl';
import { Link, Redirect, useParams } from 'react-router-dom';
import axios from 'axios';
import {DOMAIN_NAME} from '../../../constants/action-types'
import { toast } from 'react-toastify';
import FileUploader from './common/FileUploader';
import CheckBoxValidationWords from './common/CheckBoxValidationWords';
import P2MApplicationInfo from './P2MApplicationInfo';

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

const Insurance = (props) => {
	const dispatch = useDispatch()
	// const { id } = useParams()
	const p2m = useSelector(state => state.P2M)
	const d = useSelector(state => state.d )
	const editor = useRef(null)
	const comment = useRef(null)

	const [saveTimeout, setSaveTimeout] = useState(false)

	const readonlyConfig = {
		toolbar: false,
		readonly: true
	}
	const config = {
		buttons: [
			// 'source', '|',
			'bold',
			'strikethrough',
			'underline',
			'italic', '|',
			'ul',
			'ol', '|',
			'outdent', 'indent',  '|',
			'font',
			'fontsize',
			// 'brush',
			// 'paragraph', '|',
			// 'image',
			// 'video',
			'table',
			'link', '|',
			// 'align', 'undo', 'redo', '|',
			// 'hr',
			// 'eraser',
			// 'copyformat', '|',
			// 'symbol',
			'preview',
			'fullsize',
			// 'print',
			// 'about'
		],
		askBeforePasteHTML: false,
		askBeforePasteFromWord: false,
		defaultActionOnPaste: "insert_clear_html",
		toolbar: true,
		readonly: false
	}

	const handleBlurAreaChange = (newContent) => {
		const targetSubAp = p2m.ins
        targetSubAp.reviewOfInsurance = newContent.target.innerHTML
        dispatch(updateIns(targetSubAp))
	};

	const handleReturnMsgChange = (newContent) => {
		const targetSubAp = p2m.ins
        targetSubAp.returnStat.returnMsg = newContent.target.innerHTML
        dispatch(updateIns(targetSubAp))
	};
    
    const toaster = <FormattedMessage id = "insurance.submitted"/>;
	const savingMsg = <FormattedMessage id = "managep2m.savingMessage"/>

    const worldwide = props.intl.formatMessage({id: "insurance.worldwide"}),
        us = props.intl.formatMessage({id: "insurance.us"}),
        ca = props.intl.formatMessage({id: "insurance.ca"}),
        uk = props.intl.formatMessage({id: "insurance.uk"}),
        de = props.intl.formatMessage({id: "insurance.de"}),
        fr = props.intl.formatMessage({id: "insurance.fr"}),
        es = props.intl.formatMessage({id: "insurance.es"}),
        it = props.intl.formatMessage({id: "insurance.it"}),
        nl = props.intl.formatMessage({id: "insurance.nl"}),
        mx = props.intl.formatMessage({id: "insurance.mx"}),
        jp = props.intl.formatMessage({id: "insurance.jp"}),
        au = props.intl.formatMessage({id: "insurance.au"});
    // console.log(worldwide, 'worldwide')

    const [regionOptions, setRegionOptions ] = useState([
        { value: 'Worldwide', label: worldwide },
        { value: 'US', label: us },
        { value: 'CA', label: ca },
        { value: 'UK', label: uk },
        { value: 'DE', label: de },
        { value: 'FR', label: fr },
        { value: 'ES', label: es },
        { value: 'IT', label: it },
        { value: 'NL', label: nl },
        { value: 'MX', label: mx },
        { value: 'JP', label: jp },
        { value: 'AU', label: au },
	]);

    useEffect(() => {
		const p2mId = p2m.currentAp._id.$oid;
        
        if (!p2mId) {
            console.log('refresh get')
            // dispatch(getExactApplication(id))
            // dispatch(initP2MInsurance(id));
        } else {
			dispatch(initP2MInsurance(p2mId));
        }
	}, []);

	// handle steps
	const updateSteps = (index) => {
		const steps = [...p2m.ins.steps];
		const updates = steps.map((item, i) => {
			if (i < index) {
				item.state = 'active'
			} else {
				item.state = ''
			}
			return item
		})
		const targetSubAp = p2m.ins;
		targetSubAp.steps = updates;
		dispatch(updateIns(targetSubAp))
	}

	// handle process
	const handleProcess = (ph,index) => {
		updateSteps(index);
		const targetSubAp = p2m.ins;
		targetSubAp.process = ph;
		dispatch(updateIns(targetSubAp))
	}

    const checkHasInsured = () => {
        let value = 'yes'
        if (p2m.ins.hasInsured == 'yes') {
            value = 'no'
        }
        const targetSubAp = p2m.ins
        targetSubAp.hasInsured = value;
        dispatch(updateIns(targetSubAp))
	}
	
    // handle selected sku change
    const handleSelectSku = (e, index, target) => {
        const targetSubAp = p2m.ins;
        let arr = [];
        switch(target) {
            case 'insurance':
                arr = targetSubAp.insuranceFile
                e ? arr[index].appliedSku = e : arr[index].appliedSku = []
                targetSubAp.insuranceFile = arr
				setShowValidWarning(false)
                return dispatch(updateIns(targetSubAp))
            case 'updatedInsurance':
                arr = targetSubAp.updatedInsuranceFile
                e ? arr[index].appliedSku = e : arr[index].appliedSku = []
                targetSubAp.insuranceFile = arr
                setShowPh5ValidWarning(false)
                return dispatch(updateIns(targetSubAp))
            default:
                return null
        }
    }

	const upload = (input) =>{
        //todo arthur not finish , have to refactor
		
        var formData = new FormData();
		formData.append('p2mName' , p2m.currentAp.name )
        formData.append('file', input);

        axios({
            method: "POST",
            url: DOMAIN_NAME + '/p2m/upf',
            data: formData,
            headers: {
                "Content-Type": "multipart/form-data"
            }
            })            
    }

    // insurance files
    const handleInsuranceFile = (action, index, event) => {
		setCheckPh1(false)
        const targetSubAp = p2m.ins
        const arr = targetSubAp.insuranceFile;
        if (action == 'upload') {
            setShowValidWarning(false)
            arr[index].name = event.target.files[0].name;
			upload(event.target.files[0]);
        } else if (action == 'minus') {
            arr.length > 1 ? null: alert('必須上傳一份文件')
			arr.splice(index, 1);
		} else if (action == 'drop') {
			arr[index].name = 'Choose a file'
        } else if (action == 'add'){
            let newUploadInput = {
                name: 'Choose a file',
                appliedSku: [],
                insuredProductName: '',
                insuredRegions: [],
                insuredValidDate: new Date(),
                agreement: false,
            };
            arr.push(newUploadInput)
        }
        targetSubAp.insuranceFile = arr;
        dispatch(updateIns(targetSubAp))
    }

    // updated insurance file
    const handleUpdateInsuranceFile = (action, index, event) => {
		setCheckPh5(false)
        const targetSubAp = p2m.ins
        const arr = targetSubAp.updatedInsuranceFile;
        if (action == 'upload') {
            setShowPh5ValidWarning(false)
            arr[index].name = event.target.files[0].name;
			upload(event.target.files[0]);
        } else if (action == 'minus') {
            arr.length > 1 ? null: alert('必須上傳一份文件')
			arr.splice(index, 1)
		} else if (action == 'drop') {
			arr[index].name = 'Choose a file'
        } else if (action == 'add') {
            let newUploadInput = {
                name: 'Choose a file',
                appliedSku: [],
                insuredProductName: '',
                insuredRegions: [],
                insuredValidDate: new Date(),
                agreement: false,
            };
            arr.push(newUploadInput)
        }
        targetSubAp.updatedInsuranceFile;
        
        if (arr[0].appliedSku.length > 0) {
            targetSubAp.appliedSku.map(item => {
                item.updatedSupplierInsuranceFile = []
            })
        }
        dispatch(updateIns(targetSubAp))
    }

    // inquiryform
    const handleInquiryForm = (action, index,event) => {
		
        const targetSubAp = p2m.ins
        const arr = targetSubAp.inquiryForm;

        if (action == 'upload') {
            setShowPh2ValidWarning(false)
            arr[0].name = event.target.files[0].name;
			upload(event.target.files[0]);
        } else if (action == 'drop') {
            arr[0].name = 'Choose a file'
        }
		
        targetSubAp.inquiryForm = arr;
        dispatch(updateIns(targetSubAp))
    }
	const handleQuotationFromDrs = (action,index, event) => {
		
		const targetSubAp = p2m.ins
		const arr = targetSubAp.quotationFromDrs;
		if (action == 'upload') {
		    setShowPh3ValidWarning(false)
			arr[0].name = event.target.files[0].name;
			upload(event.target.files[0]);
		} else if (action == 'drop') {
			arr[0].name = 'Choose a file'
		}
		targetSubAp.quotationFromDrs = arr;
		dispatch(updateIns(targetSubAp))
	}
	const handleSignedQuotation = (action,index, event) => {
		const targetSubAp = p2m.ins
		const arr = targetSubAp.signedQuotation;
		if (action == 'upload') {
		    setShowPh52ValidWarning(false)
			arr[0].name = event.target.files[0].name;
			upload(event.target.files[0]);
		} else if (action == 'drop') {
			arr[0].name = 'Choose a file'
		}
		targetSubAp.signedQuotation = arr;
		dispatch(updateIns(targetSubAp))
	}
    const handleInsuredProductNameChange = (e, index) => {
        const targetSubAp = p2m.ins
        targetSubAp.insuranceFile[index].insuredProductName = e.target.value
        setShowValidWarning(false)
        dispatch(updateIns(targetSubAp))
    }
    const onInsuredRegionChange = (e, index) => {
		const targetSubAp = p2m.ins
		if (e) {
			targetSubAp.insuranceFile[index].insuredRegions = e
		} else {
			targetSubAp.insuranceFile[index].insuredRegions = []
		}
		setShowValidWarning(false)
        dispatch(updateIns(targetSubAp))
    }
    const onInsuredValidDateChange = (date, index) => {
        const targetSubAp = p2m.ins
        targetSubAp.insuranceFile[index].insuredValidDate = date;
        dispatch(updateIns(targetSubAp))
    }
    const onAgreeOfAdditionalInsured = (index) => {
        const targetSubAp = p2m.ins
        let boolean = true;
        if (targetSubAp.insuranceFile[index].agreeOfAdditionalInsured) {
            boolean = false;
        }
        targetSubAp.insuranceFile[index].agreeOfAdditionalInsured = boolean;
        dispatch(updateIns(targetSubAp))
	}
	const onAgreeOfAdditionalFee = (index) => {
        const targetSubAp = p2m.ins
        let boolean = true;
        if (targetSubAp.insuranceFile[index].agreeOfAdditionalFee) {
            boolean = false;
        }
        targetSubAp.insuranceFile[index].agreeOfAdditionalFee = boolean;
        dispatch(updateIns(targetSubAp))
	}

	const onUpdateAgreeOfAdditionalInsured = (index) => {
        const targetSubAp = p2m.ins
        let boolean = true;
        if (targetSubAp.updatedInsuranceFile[index].agreeOfAdditionalInsured) {
            boolean = false;
        }
        targetSubAp.updatedInsuranceFile[index].agreeOfAdditionalInsured = boolean;
        dispatch(updateIns(targetSubAp))
	}
	const onUpdateAgreeOfAdditionalFee = (index) => {
        const targetSubAp = p2m.ins
        let boolean = true;
        if (targetSubAp.updatedInsuranceFile[index].agreeOfAdditionalFee) {
            boolean = false;
        }
        targetSubAp.updatedInsuranceFile[index].agreeOfAdditionalFee = boolean;
        dispatch(updateIns(targetSubAp))
	}

    const handleUpdatedInsuredProductNameChange = (e, index) => {
        const targetSubAp = p2m.ins
        targetSubAp.updatedInsuranceFile[index].insuredProductName = e.target.value
        setShowPh5ValidWarning(false)
        dispatch(updateIns(targetSubAp))
    }
    const onUpdatedInsuredRegionChange = (e, index) => {
        const targetSubAp = p2m.ins
		if (e) {
			targetSubAp.updatedInsuranceFile[index].insuredRegions = e
		} else {
			targetSubAp.updatedInsuranceFile[index].insuredRegions = []
		}
        setShowPh5ValidWarning(false)
        dispatch(updateIns(targetSubAp))
    }
    const onUpdatedInsuredValidDateChange = (date, index) => {
        const targetSubAp = p2m.ins
        targetSubAp.updatedInsuranceFile[index].insuredValidDate = date;
        dispatch(updateIns(targetSubAp))
    }
	const checkTypeOfInsure = (e) => {
		const targetSubAp = p2m.ins;
		targetSubAp.caseTypeOfInsure = e.target.value;
		setShowPh3ValidWarning(false)
		dispatch(updateIns(targetSubAp))
	}

	const returnUpdate = () =>{
		const targetSubAp =p2m.ins;
		targetSubAp.returnStat.returnStatus = true;
		dispatch(updateIns(targetSubAp));
	}

	const returnSubmit = () =>{
		const targetSubAp =p2m.ins;
		// targetSubAp.returnStat.returnStatus = true;
		dispatch(updateIns(targetSubAp));
	}

    //save
    const handleSave = () => {
		setSaveTimeout(true)
        toast(savingMsg)
        const insurance = p2m.ins
        insurance.p2mApplicationId = p2m.currentAp._id;
    
        //----- handle appliedSku ----
        p2m.ins.insuranceFile.map((item,index) => {
            item.appliedSku.map(sku => {
                insurance.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.supplierInsuranceFile[index] = {
                        name: item.name,
                        insuredProductName: item.insuredProductName,
                        insuredRegions: item.insuredRegions,
                        insuredValidDate: item.insuredValidDate,
                        agreeOfAdditionalInsured: item.agreeOfAdditionalInsured,
                        agreeOfAdditionalFee: item.agreeOfAdditionalFee
                    } : null
                })
            })
        })
        p2m.ins.updatedInsuranceFile.map((item,index) => {
            item.appliedSku.map(sku => {
                insurance.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.updatedSupplierInsuranceFile[index] = {
                        name: item.name,
                        insuredProductName: item.insuredProductName,
                        insuredRegion: item.insuredRegion,
                        insuredValidDate: item.insuredValidDate,
                        agreeOfAdditionalInsured: item.agreeOfAdditionalInsured,
                        agreeOfAdditionalFee: item.agreeOfAdditionalFee
                    } : null
                })
            })
		})
		
        //----- end ----
		
		setTimeout(() => {
            setSaveTimeout(false)
            dispatch(saveP2MInsurance(insurance,toaster))
        }, 3000)    
        
	}
	const [checkPh1, setCheckPh1] = useState(false)
	const [checkPh5, setCheckPh5] = useState(false)
	const [showValidWarning, setShowValidWarning] = useState(false)
	const [showPh2ValidWarning, setShowPh2ValidWarning] = useState(false)
	const [showPh3ValidWarning, setShowPh3ValidWarning] = useState(false)
	const [showPh5ValidWarning, setShowPh5ValidWarning] = useState(false)
	const [showPh52ValidWarning, setShowPh52ValidWarning] = useState(false)

	const checkIfValid = (phase) => {			
		if (phase == 'ph-1') {
			if (p2m.ins.hasInsured == 'yes') {
				setCheckPh1(true)
				const handleValid = ( ) => {
					handleProcess('ph-2', 2)
				}
				let check = []
				let checkValid = []

				p2m.ins.insuranceFile.map(item => {
                    item.appliedSku == '' || item.name == 'Choose a file' ||
                        item.insuredProductName == '' || item.insuredRegions == ''
                        ? checkValid.push(false): null
                })

//				for (let index = 0; index < p2m.ins.insuranceFile.length; index++) {
//                    if(p2m.ins.insuranceFile[index].appliedSku == ''){
//                        checkValid.push(false)
//                    }
//                    if(p2m.ins.insuranceFile[index].name == 'Choose a file'){
//                        checkValid.push(false)
//                    }
//                    if(p2m.ins.insuranceFile[index].insuredProductName == ''){
//                        checkValid.push(false)
//                    }
//                    if(p2m.ins.insuranceFile[index].insuredRegions == ''){
//                        checkValid.push(false)
//                    }
//                }
				p2m.ins.insuranceFile.map((item) => {
					if (item.agreeOfAdditionalFee && item.agreeOfAdditionalInsured) {
						check.push(true)
					} else {
						check.push(false)
					}
				})
				const valid = check.filter(item => item == false)
				if ((checkValid.length !== 0)&&(valid.length !== 0)) {
                    setShowValidWarning(true)
                    alert('請檢查有無少勾選須確認項目？')
                } else if((checkValid.length == 0)&&(valid.length !== 0)) {
                    alert('請檢查有無少勾選須確認項目？')
                } else if((checkValid.length !== 0)&&(valid.length == 0)) {
                    setShowValidWarning(true)
                } else if ((checkValid.length == 0)&&(valid.length == 0)) {
                    setShowValidWarning(false)
                    handleValid()
                }
			} else {
				handleProcess('ph-2', 2)
			}
			
		} else if (phase == 'ph-2') {
            const handleValid = () => {
                handleProcess("ph-3", 3)
                handleSave()
            }
            if (p2m.ins.inquiryForm[0].name == 'Choose a file') {
                setShowPh2ValidWarning(true)
            } else {
                setShowPh2ValidWarning(false)
                handleValid()
            }
        } else if (phase == 'ph-3') {
			const handleValid = () => {
				handleProcess("ph-4", 4)
				handleSave()
			}
			if (p2m.ins.caseTypeOfInsure == ''){
				alert('必須選一個廠商投保狀況類型')
				setShowPh3ValidWarning(true)
			} else if (p2m.ins.caseTypeOfInsure == 'B' || p2m.ins.caseTypeOfInsure == 'C') {
				if (p2m.ins.quotationFromDrs[0].name == 'Choose a file') {
					setShowPh3ValidWarning(true)
				} else {
                    if (p2m.ins.caseTypeOfInsure == 'B'){
                        if (confirm("選擇的廠商投保狀況類型為： B. 高風險+有保險\n"+"確認回覆嗎？")){
                            handleValid()
                        }
                    } else if (p2m.ins.caseTypeOfInsure == 'C'){
                        if (confirm("選擇的廠商投保狀況類型為： C. 非高風險+未保險\n"+"確認回覆嗎？")){
                            handleValid()
                        }
                    }
                }
			}else{
                if (p2m.ins.caseTypeOfInsure == 'A'){
                    if (confirm("選擇的廠商投保狀況類型為： A. 非高風險+有保險\n"+"確認回覆嗎？")){
                        handleValid()
                    }
                } else if (p2m.ins.caseTypeOfInsure == 'D'){
                    if (confirm("選擇的廠商投保狀況類型為： D. DRS 應拒絕受理\n"+"確認回覆嗎？")){
                        handleValid()
                    }
                }
			}
		} else if (phase== 'ph-5') {
			if(p2m.ins.returnStat.returnStatus == true){
				p2m.ins.returnStat.returnStatus = false
			}
			if (p2m.ins.caseTypeOfInsure == 'C') {
                const handleValid = () => {
                    handleProcess("ph-6", 6)
                    handleSave()
                }
                if (p2m.ins.signedQuotation[0].name == 'Choose a file') {
                    setShowPh52ValidWarning(true)
                } else {
                    setShowPh52ValidWarning(false)
                    handleValid()
                }
            } else if (p2m.ins.hasInsured == 'yes' && p2m.ins.caseTypeOfInsure !== 'C') {
				setCheckPh5(true)
				const handleValid = () => {
					handleProcess("ph-6", 6)
					handleSave()
				}
				let check = []
				let checkValid = []

				p2m.ins.updatedInsuranceFile.map(item => {
                    item.appliedSku == '' || item.name == 'Choose a file' ||
                        item.insuredProductName == '' || item.insuredRegions == ''
                        ? checkValid.push(false): null
                })
//                for (let index = 0; index < p2m.ins.updatedInsuranceFile.length; index++) {
//                    if(p2m.ins.updatedInsuranceFile[index].appliedSku == ''){
//                        checkValid.push(false)
//                    }
//                    if(p2m.ins.updatedInsuranceFile[index].name == 'Choose a file'){
//                        checkValid.push(false)
//                    }
//                    if(p2m.ins.updatedInsuranceFile[index].insuredProductName == ''){
//                        checkValid.push(false)
//                    }
//                    if(p2m.ins.updatedInsuranceFile[index].insuredRegions == ''){
//                        checkValid.push(false)
//                    }
//                }
				p2m.ins.updatedInsuranceFile.map((item) => {
					if (item.agreeOfAdditionalFee && item.agreeOfAdditionalInsured) {
						check.push(true)
					} else {
						check.push(false)
					}
				})
				const valid = check.filter(item => item == false)
				if (p2m.ins.caseTypeOfInsure == 'A') {
                    if (checkValid.length !== 0){
                        setShowPh5ValidWarning(true)
                    }
                    if(valid.length !== 0) {
                        alert('請檢查有無少勾選須確認項目？')
                    }
                    if ((checkValid.length == 0)&&(valid.length == 0)) {
                        setShowPh5ValidWarning(false)
                        handleValid()
                    }
                } else if (p2m.ins.caseTypeOfInsure == 'B') {
                    if (checkValid.length !== 0) {
                        setShowPh5ValidWarning(true)
                    }
                    if (valid.length !== 0) {
                        alert('請檢查有無少勾選須確認項目？')
                    }
                    if (p2m.ins.signedQuotation[0].name == 'Choose a file') {
                        setShowPh52ValidWarning(true)
                    }
                    if ((checkValid.length == 0)&&(valid.length == 0)&&(p2m.ins.signedQuotation[0].name !== 'Choose a file')) {
                        setShowPh5ValidWarning(false)
                        setShowPh52ValidWarning(false)
                        handleValid()
                    }
                }
			} else {
				handleProcess("ph-6", 6)
				handleSave()
			}
		}
	}
	
	if (p2m.redirect !== '') {
        return <Redirect to={p2m.redirect}/>
	}
	
    return (
        <div>
            { p2m.isPending == 'loading' || saveTimeout ||p2m.result == 'loading'
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
							{p2m.currentAp.name}-<FormattedMessage id="subApplication.insuranceTitle"/>
                        </p>
                        <div className="application-subtitle-info-wrapper">
                            <div className="application-info-column" style={{width: '48%'}}>
                                <div className="application-info">
                                    <p className="application-info-title" style={{width: '50%'}}>
                                        <FormattedMessage id="p2mapplication.formtype"/>
                                    </p>
                                    <p className="application-info-content">
                                        <FormattedMessage id="subApplication.insurance"/>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="section-line"></div>
					<P2MApplicationInfo/>
                    
                    <div className="section-line"></div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-title">
                            <FormattedMessage id="insurance.productinsurance"/>
                        </p>
                        <div className="application-intro-wrapper">
                            <div className="application-intro-paragraph">
                                <p><FormattedMessage id="insurance.paragraph1"/></p>
                                <p><FormattedMessage id="insurance.paragraph2"/></p>
                                <p><FormattedMessage id="insurance.paragraph3"/></p>
                                <p><FormattedMessage id="insurance.paragraph4"/></p>
                                <p><FormattedMessage id="insurance.paragraph5"/></p>
                                <p><FormattedMessage id="insurance.paragraph6"/></p>
                                <p style={{fontSize: '13px', color: '#f0142f', fontWeight: '500', marginTop: '12px'}}>
                                    <FormattedMessage id="insurance.paragraph7"/>
                                </p>
                            </div>
                        </div>
                        
                        <div className="application-info-section column align-center" style={{padding: '0'}}>
                            <div className="statusbar-wrapper-inline">
                                <div className="statusbar-inline">
                                    <ul className="progressbar">
                                        {p2m.ins.steps.map(step => (
                                            <li className={step.state}>
												<FormattedMessage id={step.name}/>
											</li>
                                        ))}
                                    </ul>
                                </div>
                            </div>
                        </div>
                        
                        { p2m.ins.process == 'ph-1'
                            ?   <div className="application-info-section column align-center mt-1">
                                    <p className="application-info-section-subtitle">
                                        <span>
                                            <FormattedMessage id="insurance.paragraph8"/>
                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                *
                                            </span>
                                        </span>
                                        
                                        <div className="radio-btn-container" style={{display: 'flex', margin: '1.5% 1%'}}>
                                            <FormattedMessage id="insurance.labelyes">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='1'
                                                        isSelected={p2m.ins.hasInsured ==='yes'}
                                                        label={label}
                                                        value='yes'
                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0) && !d.u.isSp}
                                                    />
                                                }
                                            </FormattedMessage>
                                            <FormattedMessage id="insurance.labelno">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='2'
                                                        isSelected={p2m.ins.hasInsured === 'no'}
                                                        label={label}
                                                        value='no'
                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0) && d.u.isSp == false}
                                                    />
                                                }
                                            </FormattedMessage>
                                        </div>
                                    </p>
                                    
                                    { p2m.ins.hasInsured === 'yes'
                                        ?   <div className="application-info-sub-section">
                                                <div className="application-info-row">
                                                    <div className="application-intro-paragraph enhance space-between">
                                                        <span>
                                                            <FormattedMessage id="insurance.paragraph9"/>
                                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                *
                                                            </span>
                                                        </span>
                                                        { p2m.ins.process == 'ph-1' && d.u.isSp == true
                                                            ? <div className="paragraph-inline-btn-wrapper">
                                                                    <span style={{marginRight: '5px'}}>
                                                                        <FormattedMessage id="insurance.adduploadspace"/>
                                                                    </span>
																	<button className="paragraph-inline-btn"
																		onClick={() => handleInsuranceFile('add')}
																		disabled={!d.u.isSp}
																	>
                                                                        <i class="fa fa-plus" aria-hidden="true"></i>
                                                                    </button>
                                                                </div>
                                                            : null
                                                        }
                                                    </div>
                                                    { p2m.ins.insuranceFile.map((item, index) => (
                                                        <div className="application-upload-section-wrapper">
															{ p2m.ins.insuranceFile.length > 1
																?	<div className="paragraph-inline-btn-wrapper" >
																		<span style={{marginRight: '5px'}}>
																			移除此項
																		</span>
																		<button className="paragraph-inline-btn"
																			onClick={() => handleInsuranceFile('minus', index)}
																			disabled={p2m.ins.insuranceFile.length > 1 ? false: true}
																		>
																			<i class="fa fa-minus-square" aria-hidden="true"></i>
																		</button>
																	</div>
															 	:	<div></div>
															}
                                                            <div className="base-input-column-wrapper">
                                                                <div className="base-input-row-wrapper">
                                                                    <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%'}}>
                                                                        <div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
                                                                            <span style={{fontSize: '14px'}}>
                                                                                <FormattedMessage id="insurance.applytosku"/>
                                                                            </span>
                                                                        </div>
                                                                        <FormattedMessage id="insurance.select">
																		{ placeholder =>
																			<Select
																				className={`base-selector ${d.u.isSp ? '' : 'base-field-readonly'}`}
																				styles={customStyles}
																				value={item.appliedSku}
																				placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
																				onChange={(e) => handleSelectSku(e, index,'insurance')}
																				options={p2m.skuOptions}
																				classNamePrefix='drsSelector'
																				isMulti
																				isDisabled={!d.u.isSp}
																			/>
																		}
                                                                        </FormattedMessage>
                                                                        { item.appliedSku == ''
                                                                            ?   <p className="drs-blue" style={{width: '10%', marginLeft: '10px', fontSize: '13px'}}>
                                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                                    <FormattedMessage id="selectField.required"/>
                                                                                </p>
                                                                            :   null
                                                                        }
                                                                    </div>
                                                                    <div className="base-input-file-wrapper" style={{width: '55%', marginTop: '1%'}}>
                                                                        <div className="base-input-file-column" style={{width: '100%'}}>
																			<FileUploader
																				fileName={item.name}
																				index={index}
																				href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${item.name}`}
																				onChange={handleInsuranceFile}
																				disabled={!d.u.isSp}
																			/>
                                                                        </div>
                                                                    </div>
                                                                    { item.name == 'Choose a file'
                                                                      ?   <div className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px'}}>
                                                                              <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                              <FormattedMessage id="file.required"/>
                                                                          </div>
                                                                      :   null
                                                                    }
                                                                </div>
                                                                <p className="application-intro-sub-paragraph">
                                                                    <FormattedMessage id="insurance.paragraph10"/>
                                                                </p>
                                                                <div className="base-input-row-wrapper" style={{marginTop: '1%'}}>
                                                                    <div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
                                                                        <div className="application-input-wrapper align-center">
                                                                            <div className="base-input-label-wrapper">
                                                                                <p className="application-input-label">
                                                                                    <FormattedMessage id="insurance.subparagraph1"/>
                                                                                    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                        *
                                                                                    </span>
                                                                                </p>
                                                                            </div>
                                                                            <div className="base-input-section">
																				<FormattedMessage id="insurance.productname">
																				{ placeholder =>
																					<input
																						className={`base-input ${d.u.isSp ? '':'base-field-readonly'}`}
																						placeHolder={placeholder}
																						value={item.insuredProductName}
																						onChange={(e) => handleInsuredProductNameChange(e, index)}
																						readOnly={!d.u.isSp}
																					>
																					</input>
																				}
																				</FormattedMessage>
																				{ item.insuredProductName == ''
                                                                                    ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                                            <FormattedMessage id="inputField.required"/>
                                                                                        </p>
                                                                                    :   null
                                                                                }
                                                                            </div>
                                                                        </div>
                                                                        <div className="application-input-wrapper align-center">
                                                                            <div className="base-input-label-wrapper">
                                                                                <p className="application-input-label">
                                                                                    <FormattedMessage id="insurance.subparagraph2"/>
                                                                                    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                        *
                                                                                    </span>
                                                                                </p>
                                                                            </div>
                                                                                {/*<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
                                                                                    <span style={{fontSize: '14px'}}>
                                                                                        <FormattedMessage id="insurance.area"/>
                                                                                    </span>
                                                                                </div>*/}
                                                                            <div className="base-selector">
                                                                                <FormattedMessage id="insurance.select">
                                                                                { placeholder =>
                                                                                    <Select
                                                                                        className={`base-selector ${d.u.isSp ? '' : 'base-field-readonly'}`}
                                                                                        styles={customStyles}
                                                                                        value={item.insuredRegions}
                                                                                        placeholder={item.insuredRegions.length == 0 ? placeholder: item.insuredRegions}
                                                                                        onChange={(e) => onInsuredRegionChange(e, index)}
                                                                                        options={regionOptions}
                                                                                        classNamePrefix='drsSelector'
                                                                                        isMulti
                                                                                        isDisabled={!d.u.isSp}                                                                                  />
                                                                                }
                                                                                </FormattedMessage>
                                                                                { item.insuredRegions == ''
                                                                                    ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                                            <FormattedMessage id="selectField.required"/>
                                                                                        </p>
                                                                                    :   null
                                                                                }
                                                                            </div>
                                                                        </div>
                                                                        <div className="application-input-wrapper align-center">
                                                                            <div className="base-input-label-wrapper">
                                                                                <p className="application-input-label">
                                                                                    <FormattedMessage id="insurance.paragraph11"/>
                                                                                    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                        *
                                                                                    </span>
                                                                                </p>
                                                                            </div>
                                                                            <div className="base-selector">
                                                                                <DatePicker
                                                                                    selected={new Date(item.insuredValidDate)}
                                                                                    onChange={date => onInsuredValidDateChange(date,index)}
                                                                                    dateFormat="yyyy/MM/dd"
																					className="base-input date-picker"
																					disabled={!d.u.isSp}
                                                                                />
                                                                            </div>
                                                                        </div>                                                                
                                                                    </div>
                                                                </div>
															    <p className="application-intro-sub-paragraph">
																	保險聲明
																</p>
																<div className="base-input-column-wrapper">
																	<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																		<CheckBox
																			isSelected={item.agreeOfAdditionalInsured}
																			onSelect={()=> onAgreeOfAdditionalInsured(index)}
																			disabled={!d.u.isSp}
																		/>
																		<span className="checkbox-inline-text">
																			<FormattedMessage id="insurance.paragraph12"/>
                                                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                *
                                                                            </span>
																		</span>
																	</div>
																	{	checkPh1 ?
																		<CheckBoxValidationWords
																			show={!item.agreeOfAdditionalInsured}
																		/>
																		: null
																	}
																	<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																		<CheckBox
																			isSelected={item.agreeOfAdditionalFee}
																			onSelect={()=> onAgreeOfAdditionalFee(index)}
																			disabled={!d.u.isSp}
																		/>
																		<span className="checkbox-inline-text">
																			若我未投保該市場地區的產品責任險， 我同意分擔 DRS 的產品責任險費用以加入 DRS 通路。如果我已為產品在該市場地區投保，但其風險等級、銷售額或是產品屬性超過 DRS 現有保險可承擔的範圍時，我同意 DRS 有權利提供額外報價。										
                                                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                *
                                                                            </span>
																		</span>
																	</div>
																	{	checkPh1 ?
																		<CheckBoxValidationWords
																			show={!item.agreeOfAdditionalFee}
																		/>
																		: null
																	}
																</div>
															</div>

                                                        </div>
                                                    ))}
                                                </div>
                                                <div>
                                                    {showValidWarning
                                                        ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                                                <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                                                    <FormattedMessage id="insurance.warning"/>
                                                                </p>
                                                            </div>
                                                        :   null
                                                    }
                                                </div>
                                            </div>
                                        :   null
                                    }
                                    
                                </div>
                            :   null
                        }

                        { p2m.ins.process == 'ph-2'
                            ?   <div className="application-info-section column align-center mt-1">
                                    <p className="application-info-section-subtitle" style={{marginBottom:'15px'}}>
                                        <span>
                                            <FormattedMessage id="insurance.paragraph8-2"/>
                                        </span>
                                        
                                        <div className="radio-btn-container" style={{display: 'flex', margin: '1.5% 1%'}}>
                                            <FormattedMessage id="insurance.labelyes">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='1'
                                                        isSelected={p2m.ins.hasInsured ==='yes'}
                                                        label={label}
                                                        value='yes'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                            <FormattedMessage id="insurance.labelno">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='2'
                                                        isSelected={p2m.ins.hasInsured === 'no'}
                                                        label={label}
                                                        value='no'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                        </div>
                                    </p>
									<p className="application-info-section-subtitle">
                                        <FormattedMessage id="insurance.download"/>
										<a href="https://access.drs.network/resources/files/insurance_template.docx" download>
											<FormattedMessage id="insurance.application"/> 
										</a>
										<FormattedMessage id="insurance.upload"/>
										<span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                            *
                                        </span>
                                    </p>
									<div className="application-info-row">
										<p className="application-intro-paragraph enhance">
											{/* <FormattedMessage id="insurance.pleasedownload"/>
											<FormattedMessage id="insurance.uploadpdf"/> */}
											請上傳填妥的產品責任保險詢問表(pdf)
											<span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                *
                                            </span>
										</p>
										<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
											<div className="application-upload-section-wrapper" style={{width: '100%', alignItems: 'center', display:'flex'}}>
												<FileUploader
													fileName={p2m.ins.inquiryForm[0].name }
													index={null}
													href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${p2m.ins.inquiryForm[0].name }`}
													onChange={handleInquiryForm}
													disabled={!d.u.isSp}
												/>
												{ p2m.ins.inquiryForm[0].name == 'Choose a file'
                                                    ?   <div className="drs-blue" style={{width: '13%', marginLeft: '15px', fontSize: '14px'}}>
                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                            <FormattedMessage id="file.required"/>
                                                        </div>
                                                    :   null
                                                }
											</div>

										</div>
									</div>
									<div>
                                        {showPh2ValidWarning
                                            ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                                    <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                                        <FormattedMessage id="insurance.warning-2"/>
                                                    </p>
                                                </div>
                                            :   null
                                        }
                                    </div>
                                </div>
                            :   null
                        }
                        
                        { p2m.ins.process == 'ph-3'
                            ?   d.u.isSp
									? 	<div>
											<div className="application-info-section column align-center mt-1">
												<p className="application-info-section-subtitle">
													<FormattedMessage id="insurance.evaluating"/> 
												</p>
											</div>
										</div>
								
									: <div>
										<div className="application-info-section column align-center mt-1">
											<p className="application-info-section-subtitle" style={{marginBottom:'15px'}}>
												<span>
													<FormattedMessage id="insurance.paragraph8-2"/>
												</span>
												
												<div className="radio-btn-container" style={{display: 'flex', margin: '1.5% 1%'}}>
													<FormattedMessage id="insurance.labelyes">
														{ label =>
															<RadioButton
																changed={checkHasInsured}
																id='1'
																isSelected={p2m.ins.hasInsured ==='yes'}
																label={label}
																value='yes'
																disabled
															/>
														}
													</FormattedMessage>
													<FormattedMessage id="insurance.labelno">
														{ label =>
															<RadioButton
																changed={checkHasInsured}
																id='2'
																isSelected={p2m.ins.hasInsured === 'no'}
																label={label}
																value='no'
																disabled
															/>
														}
													</FormattedMessage>
												</div>
											</p>
											<p className="application-info-section-subtitle">
												廠商提供資料
											</p>
											{p2m.ins.hasInsured == 'no'
												?	<div></div>
												:	<div className="application-info-row">
														<p className="application-intro-paragraph enhance space-between">
															<FormattedMessage id="insurance.paragraph9-2"/>
														</p>
														{ p2m.ins.insuranceFile.map((item, index) => (
															<div className="application-upload-section-wrapper">
																<div className="base-input-column-wrapper">
																	<div className="base-input-row-wrapper">
																		<div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%'}}>
																			<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
																				<span style={{fontSize: '14px'}}>
																					<FormattedMessage id="insurance.applytosku"/>
																				</span>
																			</div>
																			<FormattedMessage id="insurance.select">
																				{ placeholder =>
																					<Select
																						className={`base-selector ${p2m.ins.process == 'ph-3' ? 'base-field-readonly':''}`}
																						styles={customStyles}
																						value={item.appliedSku}
																						placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
																						onChange={(e) => handleSelectSku(e, index,'insurance')}
																						options={p2m.skuOptions}
																						classNamePrefix='drsSelector'
																						isMulti
																						isDisabled={p2m.ins.process == 'ph-3'}
																					/>
																				}
																			</FormattedMessage>
																		</div>
																		<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
																			<div className="base-input-file-column">
																				<FileUploader
																					fileName={item.name}
																					index={index}
																					href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${item.name}`}
																					onChange={handleInsuranceFile}
																					disabled={p2m.ins.process == 'ph-3'}
																				/>
																			</div>
																		</div>
																	</div>
																	<p className="application-intro-sub-paragraph">
																		<FormattedMessage id="insurance.paragraph10"/>
																	</p>
																	<div className="base-input-row-wrapper" style={{marginTop: '1%'}}>
																		<div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
																			<div className="application-input-wrapper align-center">
																				<div className="base-input-label-wrapper">
																					<p className="application-input-label">
																						<FormattedMessage id="insurance.subparagraph1"/>
																					</p>
																				</div>
																				<div className="base-input-inline-wrapper">
																				<FormattedMessage  id="insurance.productname">
																					{ placeholder =>
																					<input
																						className={`base-input ${p2m.ins.process == 'ph-3'? 'base-field-readonly':''}`}
																						placeHolder={placeholder}
																						value={item.insuredProductName}
																						onChange={(e) => handleInsuredProductNameChange(e, index)}
																						readOnly={p2m.ins.process == 'ph-3'? true: false}
																					>
																					</input>
																					}
																				</FormattedMessage>
																				</div>
																			</div>
																			<div className="application-input-wrapper align-center">
																				<div className="base-input-label-wrapper">
																					<p className="application-input-label">
																						<FormattedMessage id="insurance.subparagraph2"/>
																					</p>
																				</div>
																				<div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%'}}>
																					<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
																						<span style={{fontSize: '14px'}}>
																							<FormattedMessage id="insurance.area"/>
																						</span>
																					</div>
																					<FormattedMessage id="insurance.select">
																						{ placeholder =>
																							<Select
																								className={`base-selector ${p2m.ins.process == 'ph-3' ? 'base-field-readonly':''}`}
																								styles={customStyles}
																								value={item.insuredRegions}
																								placeholder={item.insuredRegions.length == 0 ? placeholder: item.insuredRegions}
																								onChange={(e) => onInsuredRegionChange(e, index)}
																								options={regionOptions}
																								classNamePrefix='drsSelector'
																								isMulti
																								isDisabled={p2m.ins.process == 'ph-3'}
																							/>
																						}
																					</FormattedMessage>
																				</div>
																			</div>
																			
																			<div className="application-input-wrapper align-center">
																				<div className="base-input-label-wrapper">
																					<p className="application-input-label">
																						<FormattedMessage                                
																							id="insurance.paragraph11"
																						/>
																					</p>
																				</div>
																				<div className="base-input-inline-wrapper">
																					<DatePicker
																						selected={new Date(item.insuredValidDate)}
																						onChange={date => onInsuredValidDateChange(date,index)}
																						dateFormat="yyyy/MM/dd"
																						className="base-input date-picker"
																						disabled={p2m.ins.process == 'ph-3' ? true: false}
																					/>
																				</div>
																			</div>                                                                
																		</div>
																	</div>
																	
																	<p className="application-intro-sub-paragraph">
																		保險聲明
																	</p>
																	<div className="base-input-column-wrapper">
																		<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																			<CheckBox
																				isSelected={item.agreeOfAdditionalInsured}
																				onSelect={()=> onAgreeOfAdditionalInsured(index)}
																				disabled={p2m.ins.process == 'ph-3'}
																			/>
																			<span className="checkbox-inline-text">
																				<FormattedMessage id="insurance.paragraph12"/>
																			</span>
																		</div>
																		<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																			<CheckBox
																				isSelected={item.agreeOfAdditionalFee}
																				onSelect={()=> onAgreeOfAdditionalFee(index)}
																				disabled={p2m.ins.process == 'ph-3'}
																			/>
																			<span className="checkbox-inline-text">
																				若我未投保該市場地區的產品責任險， 我同意分擔 DRS 的產品責任險費用以加入 DRS 通路。如果我已為產品在該市場地區投保，但其風險等級、銷售額或是產品屬性超過 DRS 現有保險可承擔的範圍時，我同意 DRS 有權利提供額外報價。										
																			</span>
																		</div>
																	</div>
																</div>
															</div>
														))}
													</div>
											}
											<div className="application-info-row">
												<p className="application-intro-paragraph enhance space-between">
													廠商所填寫產品責任保險詢問單
												</p>
												<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
													<div className="base-input-file-column">
														<FileUploader
															fileName={p2m.ins.inquiryForm[0].name}
															index={null}
															href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${p2m.ins.inquiryForm[0].name}`}
															onChange={handleInquiryForm}
															disabled={p2m.ins.process == 'ph-3'}
														/>
													</div>
												</div>
											</div>
										</div>
										<div style={{borderTop: '1px dashed #1976f5',margin: '3% 1%'}}></div>
										<div className="application-info-section column align-center mt-1">
											<p className="application-info-section-subtitle">
												DRS 回覆內容
												<span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                    *
                                                </span>
											</p>
												<div className="application-info-row">
													<p className="application-intro-paragraph enhance" style={{display:'flex'}}>
														請選擇廠商投保狀況類型
														<span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                            *
                                                        </span>
                                                        { p2m.ins.caseTypeOfInsure == ''
                                                            ?   <p className="drs-blue" style={{width: '13%', marginLeft: '15px', fontSize: '14px'}}>
                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                    <FormattedMessage id="selectField.required"/>
                                                                </p>
                                                            :   null
                                                        }
													</p>
													<div className="radio-btn-container column" style={{display: 'flex', margin: '1.5% 1%'}}>
														<div style={{margin: '1% 0'}}>
															<FormattedMessage  id="insurance.insuredType1">
															{ label =>
																<RadioButton
																	changed={checkTypeOfInsure}
																	id='3'
																	isSelected={p2m.ins.caseTypeOfInsure === 'A'}
																	label={label}
																	value='A'
																	disabled={p2m.ins.hasInsured == 'no'}
																/>
															}
															</FormattedMessage>
														</div>
														<div style={{margin: '1% 0'}}>
															<FormattedMessage  id="insurance.insuredType2">
															{ label =>
																<RadioButton
																	changed={checkTypeOfInsure}
																	id='4'
																	isSelected={ p2m.ins.caseTypeOfInsure === 'B'}
																	label={label}
																	value='B'
																	disabled={p2m.ins.hasInsured == 'no'}
																/>
															}
															</FormattedMessage>
														</div>
														<div style={{margin: '1% 0'}}>
															<FormattedMessage  id="insurance.insuredType3">
															{ label =>
																<RadioButton
																	changed={checkTypeOfInsure}
																	id='5'
																	isSelected={ p2m.ins.caseTypeOfInsure === 'C'}
																	label={label}
																	value='C'
																	disabled={p2m.ins.hasInsured == 'yes'}
																/>
															}
															</FormattedMessage>
														</div>
														<div style={{margin: '1% 0'}}>
															<FormattedMessage  id="insurance.insuredType4">
															{ label =>
																<RadioButton
																	changed={checkTypeOfInsure}
																	id='6'
																	isSelected={ p2m.ins.caseTypeOfInsure === 'D'}
																	label={label}
																	value='D'
																	disabled={p2m.ins.hasInsured == ''}
																/>
															}
															</FormattedMessage>
														</div>
													</div>
												</div>
												<div className="application-info-row">
													<p className="application-intro-paragraph enhance space-between">
														請填寫審核結果與其他建議廠商注意事項
													</p>
													
													<JoditEditor
														ref={editor}
														// onChange={handleTextAreaChange}
														onBlur={handleBlurAreaChange}
														value={p2m.ins.reviewOfInsurance}
														config={config}
														tabIndex={1}
													/>
												</div>
											{ p2m.ins.caseTypeOfInsure == 'D' ?
												null 
												:
												<div className="application-info-row">
												<p className="application-intro-paragraph enhance">
													如有需要提供廠商報價，請上傳報價單
                                                    { p2m.ins.caseTypeOfInsure == 'B'||p2m.ins.caseTypeOfInsure == 'C'
                                                        ?   <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                *
                                                            </span>
                                                        :   null
                                                    }
												</p>
												<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
													<div className="application-upload-section-wrapper" style={{width: '100%', alignItems: 'center', display:'flex'}}>
														<FileUploader
                                                            fileName={p2m.ins.quotationFromDrs[0].name}
                                                            index={null}
                                                            href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${p2m.ins.quotationFromDrs[0].name}`}
                                                            onChange={handleQuotationFromDrs}
                                                            disabled={false}
                                                        />
                                                        { p2m.ins.caseTypeOfInsure == 'B' || p2m.ins.caseTypeOfInsure == 'C'
                                                            ?   p2m.ins.quotationFromDrs[0].name == 'Choose a file'
                                                                    ?   <p className="drs-blue" style={{width: '13%', marginLeft: '15px', fontSize: '14px'}}>
                                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                            <FormattedMessage id="file.required"/>
                                                                        </p>
                                                                    :   null
                                                            :   null
                                                        }
													</div>
												</div>
											</div>
											}
										</div>
										<div>
											{showPh3ValidWarning
												?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
														<p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
															<FormattedMessage id="insurance.warning-3"/>
														</p>
													</div>
												:   null
											}
                                    	</div>
                                	</div>
                            :   null
                        }
                        
                        { p2m.ins.process == 'ph-4'
                            ?   <div className="application-info-section column align-center mt-1">
									<div className="application-info-row">
									<p className="application-info-section-subtitle" style={{marginBottom:'15px'}}>
                                        <span>
                                            <FormattedMessage id="insurance.paragraph8-2"/>
                                        </span>
                                        
                                        <div className="radio-btn-container" style={{display: 'flex', margin: '1.5% 1%'}}>
                                            <FormattedMessage id="insurance.labelyes">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='1'
                                                        isSelected={p2m.ins.hasInsured ==='yes'}
                                                        label={label}
                                                        value='yes'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                            <FormattedMessage id="insurance.labelno">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='2'
                                                        isSelected={p2m.ins.hasInsured === 'no'}
                                                        label={label}
                                                        value='no'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                        </div>
                                    </p>
										<p className="application-info-section-subtitle">
											{/* <FormattedMessage id="insurance.subparagraph3"/>  
											<strong><FormattedMessageid="insurance.quotation"/> </strong>
											<FormattedMessage id="insurance.subparagraph4"/>  */}
											DRS 建議
										</p>
										<div className="application-info-row">
											<JoditEditor
												ref={comment}
												// onChange={handleTextAreaChange}
												onBlur={handleBlurAreaChange}
												value={p2m.ins.reviewOfInsurance}
												config={readonlyConfig}
												tabIndex={1}
											/>
										</div>
									</div>
									{ p2m.ins.caseTypeOfInsure === 'B' || p2m.ins.caseTypeOfInsure === 'C'
										?	<div className="application-info-row">
												<p className="application-info-section-subtitle">
													產品責任險報價單
												</p>														
												<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
													<div className="application-upload-section-wrapper" style={{width: '100%'}}>
														<div className="base-input-file-column">
															<FileUploader
																fileName={p2m.ins.quotationFromDrs[0].name}
																index={null}
																href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${p2m.ins.quotationFromDrs[0].name}`}
																onChange={handleQuotationFromDrs}
																disabled={true}
															/>
														</div>
													</div>
												</div>
											</div>
										:	null
									}
									
									<div className="application-info-row">
										<p className="application-info-section-subtitle">
											評估結果
										</p>
										{ p2m.ins.caseTypeOfInsure == 'A'
											?	<div className="application-info-row" style={{margin: '12px 0'}}>
													<p style={{lineHeight:'1.5rem'}}>由於您同意將 DRS 加入額外的被保險人（Additional Insured），且願意持續為此產品在銷售市場投保產品責任險及提供更新後的保單給 DRS，請將「善恩創新股份有限公司」加入您的保單，並上傳更新後的保單檔案。</p>
												</div>
											:	null
										}
										{ p2m.ins.caseTypeOfInsure == 'B'
											?	<div className="application-info-row" style={{margin: '12px 0'}}>
													
													<p style={{lineHeight: '1.5rem'}}>由於您產品屬於高風險產品，需請您於下一步完成兩個動作： </p>
													<p style={{lineHeight: '1.5rem'}}>高風險產品需額外支付價金，請下載報價單並簽署後上傳(pdf)，此報價單內容為支付 DRS 產品責任險費用。</p>
													<p style={{lineHeight: '1.5rem'}}>由於您同意將 DRS 加入額外的被保險人（Additional Insured），且願意持續為此產品在銷售市場投保產品責任險及提供更新後的保單給 DRS，請將「善恩創新股份有限公司」加入您的保單，並上傳更新後的保單檔案。</p>
												</div>
											:	null
										}
										{p2m.ins.caseTypeOfInsure == 'C'
											?	<div className="application-info-row" style={{margin: '12px 0'}}>
													<p style={{lineHeight: '1.5rem'}}>由於您未投保產品責任險，請下載報價單並簽署後上傳(pdf），此報價單內容為支付 DRS 產品責任險費用。</p>
												</div>
											:	null
										}
										{p2m.ins.caseTypeOfInsure == 'D'
											?	<div className="application-info-row" style={{margin: '12px 0'}}>
													<p style={{lineHeight: '1.5rem'}}>經過評估，很抱歉通知您，您申請的產品目前未在 DRS 服務範圍。</p>
												</div>
											:	null
										}
									</div>
                                </div>
                            :   null
                        }
                        
                        { p2m.ins.process == 'ph-5'
                            ?   <div className="application-info-section column align-center mt-1">
								<p className="application-info-section-subtitle" style={{marginBottom:'15px'}}>
                                        <span> <FormattedMessage id="insurance.paragraph8-2"/> </span>
                                        
                                        <div className="radio-btn-container" style={{display: 'flex', margin: '1.5% 1%'}}>
                                            <FormattedMessage id="insurance.labelyes">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='1'
                                                        isSelected={p2m.ins.hasInsured ==='yes'}
                                                        label={label}
                                                        value='yes'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                            <FormattedMessage id="insurance.labelno">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='2'
                                                        isSelected={p2m.ins.hasInsured === 'no'}
                                                        label={label}
                                                        value='no'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                        </div>
                                    </p>
									{ p2m.ins.returnStat.returnStatus == true
									?
									<div style={{display : 'contents'}}>
										<p className="application-info-section-subtitle">
                                            DRS退回原因
                                        </p>
										<div className="application-info-row">
                                            <JoditEditor
                                                ref={comment}
                                                // onChange={handleTextAreaChange}
                                                onBlur={handleReturnMsgChange}
                                                value={p2m.ins.returnStat.returnMsg}
                                                config={readonlyConfig}
                                                tabIndex={1}
                                            />
                                        </div>
									</div>
									:<div></div>
									}
									<p className="application-info-section-subtitle">
										{ p2m.ins.caseTypeOfInsure == 'A'
											?	<div className="application-info-row">
													<p>由於您同意將 DRS 加入額外的被保險人（Additional Insured），且願意持續為此產品在銷售市場投保產品責任險及提供更新後的保單給 DRS，請將「善恩創新股份有限公司」加入您的保單，並上傳更新後的保單檔案。</p>
												</div>
											:	null
										}
										{ p2m.ins.caseTypeOfInsure == 'B'
											?	<div className="application-info-row">
													
													<p>由於您產品屬於高風險產品，需請您於下一步完成兩個動作： </p>
													<p>高風險產品需額外支付價金，請下載報價單並簽署後上傳(pdf)，此報價單內容為支付 DRS 產品責任險費用。</p>
													<p>由於您同意將 DRS 加入額外的被保險人（Additional Insured），且願意持續為此產品在銷售市場投保產品責任險及提供更新後的保單給 DRS，請將「善恩創新股份有限公司」加入您的保單，並上傳更新後的保單檔案。</p>
												</div>
											:	null
										}
										{p2m.ins.caseTypeOfInsure == 'C'
											?	<div className="application-info-row">
													<p>由於您未投保產品責任險，請下載報價單並簽署後上傳(pdf），此報價單內容為支付 DRS 產品責任險費用。</p>
												</div>
											:	null
										}
										{p2m.ins.caseTypeOfInsure == 'D'
											?	<div className="application-info-row">
													<p>經過評估，很抱歉通知您，您申請的產品目前未在 DRS 服務範圍。</p>
												</div>
											:	null
										}
									</p>
									{/* upload signed qutation */}
									{ p2m.ins.caseTypeOfInsure == 'B'|| p2m.ins.caseTypeOfInsure == 'C'
										?	<div className="application-info-row">
												<p className="application-intro-paragraph enhance">
													請上傳簽署後的報價單(pdf)
													<span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                        *
                                                    </span>
												</p>
												<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
													<div className="application-upload-section-wrapper" style={{width: '100%', alignItems: 'center', display:'flex'}}>
                                                        <FileUploader
                                                            fileName={p2m.ins.signedQuotation[0].name}
                                                            index={null}
                                                            href={`${DOMAIN_NAME}/p2m/r/f/${p2m.currentAp.name}/${p2m.ins.signedQuotation[0].name}`}
                                                            onChange={handleSignedQuotation}
															disabled={!d.u.isSp}
                                                        />
                                                        { p2m.ins.signedQuotation[0].name == 'Choose a file'
                                                            ?   <p className="drs-blue" style={{width: '13%', marginLeft: '15px', fontSize: '14px'}}>
                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                    <FormattedMessage id="file.required"/>
                                                                </p>
                                                            :   null
                                                        }
													</div>
												</div>
												<div>
                                                    {showPh52ValidWarning
                                                        ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                                                <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                                                    <FormattedMessage id="insurance.warning-2"/>
                                                                </p>
                                                            </div>
                                                        :   null
                                                    }
                                                </div>
											</div>
										:	null
									}
									
									{/* update insurance files */}
									{ p2m.ins.caseTypeOfInsure == 'A' || p2m.ins.caseTypeOfInsure == 'B'
										?	<div className="application-info-row">
												<div className="application-intro-paragraph enhance space-between">
												    <span>
                                                        <FormattedMessage id="insurance.uploadtheupdatedpolicy"/>
                                                        <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                            *
                                                        </span>
                                                    </span>
													{ d.u.isSp == true
														? <div className="paragraph-inline-btn-wrapper">
															<span style={{marginRight: '5px'}}>
																<FormattedMessage id="insurance.adduploadspace"/> 
															</span>
															<button className="paragraph-inline-btn" onClick={() => handleUpdateInsuranceFile('add')}>
																<i class="fa fa-plus" aria-hidden="true"></i>
															</button>
														</div>
														: null
													}
												</div>
												{	p2m.ins.updatedInsuranceFile.map((item, index) => (
													<div className="application-upload-section-wrapper">
														{ p2m.ins.updatedInsuranceFile.length > 1
															?	<div className="paragraph-inline-btn-wrapper" >
																	<span style={{marginRight: '5px'}}>
																		移除此項
																	</span>
																	<button className="paragraph-inline-btn"
																		onClick={() => handleUpdateInsuranceFile('minus', index)}
																		disabled={p2m.ins.updatedInsuranceFile.length > 1 ? false : true}
																	>
																		<i class="fa fa-minus-square" aria-hidden="true"></i>
																	</button>
																</div>
															:	<div></div>
														}
														<div className="base-input-column-wrapper">
															<div className="base-input-row-wrapper">
																<div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%'}}>
																	<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
																		<span style={{fontSize: '14px'}}>
																			<FormattedMessage id="insurance.applytosku"/> 
																		</span>
																	</div>
																	<FormattedMessage id="insurance.select">
																		{ placeholder =>
																			<Select
																				className={`base-selector ${d.u.isSp ? '':'base-field-readonly'}`}
																				styles={customStyles}
																				value={item.appliedSku}
																				placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
																				onChange={(e) => handleSelectSku(e, index, 'updatedInsurance')}
																				options={p2m.skuOptions}
																				classNamePrefix='drsSelector'
																				isMulti
																				isDisabled={!d.u.isSp}
																			/>
																		}
																	</FormattedMessage>
                                                                    { item.appliedSku == ''
                                                                        ?   <p className="drs-blue" style={{width: '10%', marginLeft: '10px', fontSize: '13px'}}>
                                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                                <FormattedMessage id="selectField.required"/>
                                                                            </p>
                                                                        :   null
                                                                    }
																</div>
																<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
																	<div className="base-input-file-column" style={{width: '100%'}}>
																		<FileUploader
																			fileName={item.name}
																			index={index}
																			href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${item.name}`}
																			onChange={handleUpdateInsuranceFile}
																			disabled={!d.u.isSp}
																		/>
																	</div>
																</div>
                                                                { item.name == 'Choose a file'
                                                                  ?   <div className="drs-blue" style={{width: '10%', alignItems: 'center', marginLeft: '10px', fontSize: '13px'}}>
                                                                          <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                          <FormattedMessage id="file.required"/>
                                                                      </div>
                                                                  :   null
                                                                }
															</div>
															<p className="application-intro-sub-paragraph">
																<FormattedMessage id="insurance.paragraph10"/> 
															</p>
															<div className="base-input-row-wrapper" style={{marginTop: '1%'}}>
																<div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
																	<div className="application-input-wrapper align-center">
																		<div className="base-input-label-wrapper">
																			<p className="application-input-label">
																				<FormattedMessage id="insurance.subparagraph1"/>
                                                                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                    *
                                                                                </span>
																			</p>
																		</div>
																		<div className="base-input-section">
                                                                            <FormattedMessage  id="insurance.productname">
                                                                                { placeholder =>
                                                                                    <input
                                                                                        className={`base-input ${d.u.isSp ? '':'base-field-readonly'}`}
                                                                                        placeholder={placeholder}
                                                                                        value={item.insuredProductName}
                                                                                        onChange={(e) => handleUpdatedInsuredProductNameChange(e, index)}
                                                                                        readOnly={!d.u.isSp}
                                                                                    >
                                                                                    </input>
                                                                                }
                                                                            </FormattedMessage>
                                                                            { item.insuredProductName == ''
                                                                                ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                                        <FormattedMessage id="inputField.required"/>
                                                                                    </p>
                                                                                :   null
                                                                            }
																		</div>
																	</div>
																	<div className="application-input-wrapper align-center">
																		<div className="base-input-label-wrapper">
																			<p className="application-input-label">
																				<FormattedMessage id="insurance.subparagraph2"/>
                                                                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                    *
                                                                                </span>
																			</p>
																		</div>
																			{/*<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
																				<span style={{fontSize: '14px'}}>
																					<FormattedMessage id="insurance.area"/>
																				</span>
																			</div>*/}
																		<div className="base-selector">
																			<FormattedMessage id="insurance.select">
																			{ placeholder =>
																				<Select
																					className={`base-selector ${d.u.isSp ? '':'base-field-readonly'}`}
																					styles={customStyles}
																					value={item.insuredRegions}
																					placeholder={item.insuredRegions.length == 0 ? placeholder: item.insuredRegions}
																					onChange={(e) => onUpdatedInsuredRegionChange(e, index)}
																					options={regionOptions}
																					classNamePrefix='drsSelector'
																					isMulti
																					isDisabled={!d.u.isSp}
																				/>
																			}
																			</FormattedMessage>
                                                                            { item.insuredRegions == ''
                                                                                ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                                        <FormattedMessage id="selectField.required"/>
                                                                                    </p>
                                                                                :   null
                                                                            }
																		</div>
																	</div>
																	
																	<div className="application-input-wrapper align-center">
                                                                        <div className="base-input-label-wrapper">
                                                                            <p className="application-input-label">
                                                                                <FormattedMessage id="insurance.paragraph11"/>
                                                                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                    *
                                                                                </span>
                                                                            </p>
                                                                        </div>
																		<div className="base-selector">
																			<DatePicker
																				selected={new Date(item.insuredValidDate)}
																				onChange={date => onUpdatedInsuredValidDateChange(date,index)}
																				dateFormat="yyyy/MM/dd"
																				className="base-input date-picker"
																				disabled={!d.u.isSp}
																			/>
																		</div>
																	</div>                                                                
																</div>
															</div>
															<p className="application-intro-sub-paragraph">
																保險聲明
															</p>
															<div className="base-input-column-wrapper">
																<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																	<CheckBox
																		isSelected={item.agreeOfAdditionalInsured}
																		onSelect={()=> onUpdateAgreeOfAdditionalInsured(index)}
																		disabled={!d.u.isSp}
																	/>
																	<span className="checkbox-inline-text">
																		<FormattedMessage id="insurance.paragraph12"/>
																		<span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                            *
                                                                        </span>
																	</span>
																</div>
																{ checkPh5 
																	?	<CheckBoxValidationWords show={!item.agreeOfAdditionalInsured}/>
																	:	null
																}
																<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																	<CheckBox
																		isSelected={item.agreeOfAdditionalFee}
																		onSelect={()=> onUpdateAgreeOfAdditionalFee(index)}
																		disabled={!d.u.isSp}
																	/>
																	<span className="checkbox-inline-text">
																		若我未投保該市場地區的產品責任險， 我同意分擔 DRS 的產品責任險費用以加入 DRS 通路。如果我已為產品在該市場地區投保，但其風險等級、銷售額或是產品屬性超過 DRS 現有保險可承擔的範圍時，我同意 DRS 有權利提供額外報價。										
																	    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                            *
                                                                        </span>
																	</span>
																</div>
																{ checkPh5 
																	?	<CheckBoxValidationWords show={!item.agreeOfAdditionalFee}/>
																	:	null
																}
															</div>
														</div>
													</div>
												))}
												<div>
                                                    {showPh5ValidWarning
                                                        ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                                                <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                                                    <FormattedMessage id="insurance.warning"/>
                                                                </p>
                                                            </div>
                                                        :   null
                                                    }
                                                </div>
											</div>
										:	null
									}
                                    
                                </div>
                            :   null
                        }

                        { p2m.ins.process == 'ph-6'
							?   d.u.isSp
								?	<div className="application-info-section column align-center mt-1">
										<p className="application-info-section-subtitle" style={{marginBottom:'15px'}}>
											<span> <FormattedMessage id="insurance.paragraph8-2"/></span>
											
											<div className="radio-btn-container" style={{display: 'flex', margin: '1.5% 1%'}}>
												<FormattedMessage id="insurance.labelyes">
													{ label =>
														<RadioButton
															changed={checkHasInsured}
															id='1'
															isSelected={p2m.ins.hasInsured ==='yes'}
															label={label}
															value='yes'
															disabled
														/>
													}
												</FormattedMessage>
												<FormattedMessage id="insurance.labelno">
													{ label =>
														<RadioButton
															changed={checkHasInsured}
															id='2'
															isSelected={p2m.ins.hasInsured === 'no'}
															label={label}
															value='no'
															disabled
														/>
													}
												</FormattedMessage>
											</div>
										</p>
										<p className="application-info-section-subtitle">
											<FormattedMessage id="insurance.confirming"/>
										</p>
									</div>
								:  	
								<div className="application-info-section column align-center mt-1">
									<p className="application-info-section-subtitle" style={{marginBottom:'15px'}}>
                                        <span> <FormattedMessage id="insurance.paragraph8-2"/></span>
                                        
                                        <div className="radio-btn-container" style={{display: 'flex', margin: '1.5% 1%'}}>
                                            <FormattedMessage id="insurance.labelyes">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='1'
                                                        isSelected={p2m.ins.hasInsured ==='yes'}
                                                        label={label}
                                                        value='yes'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                            <FormattedMessage id="insurance.labelno">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='2'
                                                        isSelected={p2m.ins.hasInsured === 'no'}
                                                        label={label}
                                                        value='no'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                        </div>
                                    </p>
									
									<div className="application-info-row">
													<p className="application-intro-paragraph enhance space-between">
														廠商投保狀況類型
													</p>
													<div className="radio-btn-container column" style={{display: 'flex', margin: '1.5% 1%'}}>
														<div style={{margin: '1% 0'}}>
															<FormattedMessage  id="insurance.insuredType1">
															{ label =>
																<RadioButton
																	changed={checkTypeOfInsure}
																	id='3'
																	isSelected={p2m.ins.caseTypeOfInsure === 'A'}
																	label={label}
																	value='A'
																	disabled
																/>
															}
															</FormattedMessage>
														</div>
														<div style={{margin: '1% 0'}}>
															<FormattedMessage  id="insurance.insuredType2">
															{ label =>
																<RadioButton
																	changed={checkTypeOfInsure}
																	id='4'
																	isSelected={ p2m.ins.caseTypeOfInsure === 'B'}
																	label={label}
																	value='B'
																	disabled
																/>
															}
															</FormattedMessage>
														</div>
														<div style={{margin: '1% 0'}}>
															<FormattedMessage  id="insurance.insuredType3">
															{ label =>
																<RadioButton
																	changed={checkTypeOfInsure}
																	id='5'
																	isSelected={ p2m.ins.caseTypeOfInsure === 'C'}
																	label={label}
																	value='C'
																	disabled
																/>
															}
															</FormattedMessage>
														</div>
														<div style={{margin: '1% 0'}}>
															<FormattedMessage  id="insurance.insuredType4">
															{ label =>
																<RadioButton
																	changed={checkTypeOfInsure}
																	id='6'
																	isSelected={ p2m.ins.caseTypeOfInsure === 'D'}
																	label={label}
																	value='D'
																	disabled
																/>
															}
															</FormattedMessage>
														</div>
													</div>
												</div>
												
										<p className="application-info-section-subtitle">
											廠商更新內容
										</p>
										<div className="application-info-row">
											<p className="application-intro-paragraph enhance space-between">
												回傳已簽署報價單
											</p>
											<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
												<div className="base-input-file-column">
													<FileUploader
														fileName={p2m.ins.signedQuotation[0].name}
														index={null}
														href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${p2m.ins.signedQuotation[0].name}`}
														onChange={handleSignedQuotation}
														disabled={true}
													/>
												</div>
											</div>
										</div>
										{/* update insurance files */}
										{	p2m.ins.caseTypeOfInsure !== 'C'
											?	<div className="application-info-row">
													<p className="application-intro-paragraph enhance space-between">
														更新產品責任保險單
													</p>
													{ p2m.ins.updatedInsuranceFile.map((item, index) => (
														<div className="application-upload-section-wrapper">
															<div className="base-input-column-wrapper">
																<div className="base-input-row-wrapper">
																	<div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%'}}>
																		<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
																			<span style={{fontSize: '14px'}}>
																				<FormattedMessage id="insurance.applytosku"/> 
																			</span>
																		</div>
																		<FormattedMessage id="insurance.select">
																		{ placeholder =>
																			<Select
																				className="base-selector base-field-readonly"
																				styles={customStyles}
																				value={item.appliedSku}
																				placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
																				onChange={(e) => handleSelectSku(e, index, 'updatedInsurance')}
																				options={p2m.skuOptions}
																				classNamePrefix='drsSelector'
																				isMulti
																				isDisabled={true}
																			/>
																		}
																		</FormattedMessage>
																	</div>
																	<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
																		<div className="base-input-file-column">
																			<FileUploader
																				fileName={item.name}
																				index={index}
																				href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${item.name}`}
																				onChange={handleUpdateInsuranceFile}
																				disabled={true}
																			/>
																		</div>
																	</div>
																</div>
																<p className="application-intro-sub-paragraph">
																	<FormattedMessage id="insurance.paragraph10"/> 
																</p>
																<div className="base-input-row-wrapper" style={{marginTop: '1%'}}>
																	<div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
																		<div className="application-input-wrapper align-center">
																			<div className="base-input-label-wrapper">
																				<p className="application-input-label">
																					<FormattedMessage id="insurance.subparagraph1"/> 
																				</p>
																			</div>
																			<div className="base-input-inline-wrapper">
																			<FormattedMessage id="insurance.productname">
																				{ placeholder =>
																					<input
																						className="base-input base-field-readonly"
																						placeholder={placeholder}
																						value={item.insuredProductName}
																						onChange={(e) => handleUpdatedInsuredProductNameChange(e, index)}
																						readOnly={true}
																					>
																					</input>
																				}
																			</FormattedMessage>
																			</div>
																		</div>
																		<div className="application-input-wrapper align-center">
																			<div className="base-input-label-wrapper">
																				<p className="application-input-label">
																					<FormattedMessage id="insurance.subparagraph2"/> 
																				</p>
																			</div>
																			<div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%'}}>
																				<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
																					<span style={{fontSize: '14px'}}>
																						<FormattedMessage id="insurance.area"/> 
																					</span>
																				</div>
																				<FormattedMessage id="insurance.select">
																					{ placeholder =>
																						<Select
																							className="base-selector base-field-readonly"
																							styles={customStyles}
																							value={item.insuredRegions}
																							placeholder={item.insuredRegions.length == 0 ? placeholder: item.insuredRegions}
																							onChange={(e) => onUpdatedInsuredRegionChange(e, index)}
																							options={regionOptions}
																							classNamePrefix='drsSelector'
																							isMulti
																							isDisabled={true}
																						/>
																					}
																				</FormattedMessage>
																			</div>
																		</div>
																		
																		<div className="application-input-wrapper align-center">
																			<div className="base-input-label-wrapper">
																				<p className="application-input-label">
																					<FormattedMessage id="insurance.paragraph11"/>
																				</p>
																			</div>
																			<div className="base-input-inline-wrapper">
																				<DatePicker
																					selected={new Date(item.insuredValidDate)}
																					onChange={date => onUpdatedInsuredValidDateChange(date,index)}
																					dateFormat="yyyy/MM/dd"
																					className="base-input date-picker"
																					disabled={true}
																				/>
																			</div>
																		</div>                                                                
																	</div>
																</div>
																<p className="application-intro-sub-paragraph">
																	保險聲明
																</p>
																<div className="base-input-column-wrapper">
																	<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																		<CheckBox
																			isSelected={item.agreeOfAdditionalInsured}
																			onSelect={()=> onUpdateAgreeOfAdditionalInsured(index)}
																			disabled={true}
																		/>
																		<span className="checkbox-inline-text">
																			<FormattedMessage id="insurance.paragraph12"/>
																		</span>
																	</div>
																	<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																		<CheckBox
																			isSelected={item.agreeOfAdditionalFee}
																			onSelect={()=> onUpdateAgreeOfAdditionalFee(index)}
																			disabled={true}
																		/>
																		<span className="checkbox-inline-text">
																			若我未投保該市場地區的產品責任險， 我同意分擔 DRS 的產品責任險費用以加入 DRS 通路。如果我已為產品在該市場地區投保，但其風險等級、銷售額或是產品屬性超過 DRS 現有保險可承擔的範圍時，我同意 DRS 有權利提供額外報價。										
																		</span>
																	</div>
																</div>
															</div>
														</div>
													))}
												</div>
											:	null
										}
										{d.u.isSp == false 
										? 
										 p2m.ins.returnStat != null
										? 
										 p2m.ins.returnStat.returnStatus == true
										? 
										<div className="application-info-row">
										<p className="application-intro-paragraph enhance space-between">
											請填寫退回原因
										</p>
										
										<JoditEditor
											ref={editor}
											// onChange={handleTextAreaChange}
											onBlur={handleReturnMsgChange}
											value={p2m.ins.returnStat.returnMsg}
											config={config}
											tabIndex={1}
										/>
										<div className="operation-btn-group">
											<button className="drs-btn drs-btn-warning"
												onClick={()=>{ returnSubmit(); handleProcess("ph-5", 5); handleSave() }}>
												送出
											</button>
										</div>
									</div>
									: <div></div>
									: <div></div>		
									

										: <div></div>
										}
										
									</div>
                            :   null
                        }

                        { p2m.ins.process == 'ph-7'
                            ?   <div className="application-info-section column align-center mt-1">
                                    <p className="application-info-section-subtitle"  style={{marginBottom:'15px',fontSize: '19px',fontWeight: '600'}}>
										<FormattedMessage id="insurance.congratulations"/>
                                    </p>
                                    <p className="application-info-section-subtitle" style={{margin:'15px 0'}}>
                                        <span>
                                            <FormattedMessage id="insurance.paragraph8-2"/>
                                        </span>

                                        <div className="radio-btn-container" style={{display: 'flex', margin: '1.5% 1%'}}>
                                            <FormattedMessage id="insurance.labelyes">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='1'
                                                        isSelected={p2m.ins.hasInsured ==='yes'}
                                                        label={label}
                                                        value='yes'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                            <FormattedMessage id="insurance.labelno">
                                                { label =>
                                                    <RadioButton
                                                        changed={checkHasInsured}
                                                        id='2'
                                                        isSelected={p2m.ins.hasInsured === 'no'}
                                                        label={label}
                                                        value='no'
                                                        disabled
                                                    />
                                                }
                                            </FormattedMessage>
                                        </div>
                                    </p>

									{ p2m.ins.caseTypeOfInsure == 'C'
										?   null
										:   <div className="application-info-row">
												<p className="application-intro-paragraph enhance space-between">
													DRS 保險單與廠商保單
												</p> 
											</div>
									}
									{ p2m.ins.caseTypeOfInsure == 'C'
										?   null
										:   p2m.ins.updatedInsuranceFile.map((item, index) => (
												<div className="application-upload-section-wrapper">
													<div className="base-input-column-wrapper">
														<div className="base-input-row-wrapper">
															<div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%'}}>
																<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
																	<span style={{fontSize: '14px'}}>
																		<FormattedMessage id="insurance.applytosku"/> 
																	</span>
																</div>
																<FormattedMessage id="insurance.select">
																{ placeholder =>
																	<Select
																		className="base-selector base-field-readonly"
																		styles={customStyles}
																		value={item.appliedSku}
																		placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
																		onChange={(e) => handleSelectSku(e, index, 'updatedInsurance')}
																		options={p2m.skuOptions}
																		classNamePrefix='drsSelector'
																		isMulti
																		isDisabled={true}
																	/>
																}
																</FormattedMessage>
															</div>
															<div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
																<div className="base-input-file-column">
																	<FileUploader
																		fileName={item.name}
																		index={index}
																		href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${item.name}`}
																		onChange={handleUpdateInsuranceFile}
																		disabled={true}
																	/>
																</div>
															</div>
														</div>
														<p className="application-intro-sub-paragraph">
															<FormattedMessage id="insurance.paragraph10"/> 
														</p>
														<div className="base-input-row-wrapper" style={{marginTop: '1%'}}>
															<div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
																<div className="application-input-wrapper align-center">
																	<div className="base-input-label-wrapper">
																		<p className="application-input-label">
																			<FormattedMessage id="insurance.subparagraph1"/> 
																		</p>
																	</div>
																	<div className="base-input-inline-wrapper">
																	<FormattedMessage id="insurance.productname">
																		{ placeholder =>
																			<input
																				className="base-input base-field-readonly"
																				placeholder={placeholder}
																				value={item.insuredProductName}
																				onChange={(e) => handleUpdatedInsuredProductNameChange(e, index)}
																				readOnly={true}
																			>
																			</input>
																		}
																	</FormattedMessage>
																	</div>
																</div>
																<div className="application-input-wrapper align-center">
																	<div className="base-input-label-wrapper">
																		<p className="application-input-label">
																			<FormattedMessage id="insurance.subparagraph2"/> 
																		</p>
																	</div>
																	<div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%'}}>
																		<div className="selector-inline-head" style={{height: '37px', width: '27%'}}>
																			<span style={{fontSize: '14px'}}>
																				<FormattedMessage id="insurance.area"/> 
																			</span>
																		</div>
																		<FormattedMessage id="insurance.select">
																			{ placeholder =>
																				<Select
																					className="base-selector base-field-readonly"
																					styles={customStyles}
																					value={item.insuredRegions}
																					placeholder={item.insuredRegions.length == 0 ? placeholder: item.insuredRegions}
																					onChange={(e) => onUpdatedInsuredRegionChange(e, index)}
																					options={regionOptions}
																					classNamePrefix='drsSelector'
																					isMulti
																					isDisabled={true}
																				/>
																			}
																		</FormattedMessage>
																	</div>
																</div>
																
																<div className="application-input-wrapper align-center">
																	<div className="base-input-label-wrapper">
																		<p className="application-input-label">
																			<FormattedMessage id="insurance.paragraph11"/>
																		</p>
																	</div>
																	<div className="base-input-inline-wrapper">
																		<DatePicker
																			selected={new Date(item.insuredValidDate)}
																			onChange={date => onUpdatedInsuredValidDateChange(date,index)}
																			dateFormat="yyyy/MM/dd"
																			className="base-input date-picker"
																			disabled={true}
																		/>
																	</div>
																</div>                                                                
															</div>
														</div>
														<p className="application-intro-sub-paragraph">
															保險聲明
														</p>
														<div className="base-input-column-wrapper">
															<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																<CheckBox
																	isSelected={item.agreeOfAdditionalInsured}
																	onSelect={()=> onUpdateAgreeOfAdditionalInsured(index)}
																	disabled={true}
																/>
																<span className="checkbox-inline-text">
																	<FormattedMessage id="insurance.paragraph12"/>
																</span>
															</div>
															<div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}> 
																<CheckBox
																	isSelected={item.agreeOfAdditionalFee}
																	onSelect={()=> onUpdateAgreeOfAdditionalFee(index)}
																	disabled={true}
																/>
																<span className="checkbox-inline-text">
																	若我未投保該市場地區的產品責任險， 我同意分擔 DRS 的產品責任險費用以加入 DRS 通路。如果我已為產品在該市場地區投保，但其風險等級、銷售額或是產品屬性超過 DRS 現有保險可承擔的範圍時，我同意 DRS 有權利提供額外報價。										
																</span>
															</div>
														</div>
													</div>
												</div>
											))
							 		}
							 		<div className="application-info-row">
                                        <p className="application-intro-paragraph enhance space-between">
                                            廠商所填寫產品責任保險詢問單
                                        </p>
                                        <div className="base-input-file-wrapper" style={{marginTop: '1%'}}>
                                            <div className="base-input-file-column">
                                                <FileUploader
                                                    fileName={p2m.ins.inquiryForm[0].name}
                                                    index={null}
                                                    href={`${DOMAIN_NAME}/p2m/is/f/${p2m.currentAp.name}/${p2m.ins.inquiryForm[0].name}`}
                                                    onChange={handleInquiryForm}
                                                    disabled={p2m.ins.process == 'ph-7'}
                                                />
                                            </div>
                                        </div>
                                    </div>
                                </div>
									
                            :   null
                        }
                    </div>
                    
                    <div className="operation-pannel-footer">
                        <div className="operation-btn-group">
							{ p2m.ins.process == 'ph-1'
								?  <button className="drs-btn drs-btn-cta"
										onClick={()=>{ checkIfValid('ph-1')}}>
										<FormattedMessage id="insurance.next"/>
									</button>
								:	null
							}
							{ p2m.ins.process == 'ph-2'
								?   <div>
										<button className="drs-btn drs-btn-normal" onClick={()=>{handleProcess("ph-1",1)}}>
											<FormattedMessage id="insurance.previous"/>
										</button>
										{ d.u.isSp == true
											?   <button className="drs-btn drs-btn-cta" onClick={()=>{ checkIfValid('ph-2')}}>
												<FormattedMessage id="insurance.submit"/>
												</button>
											:	null
										}
									</div>
								:   null
							}
							{ p2m.ins.process == 'ph-3'
                                ?   <div>
										{/* <Link to={`/product/application/${id}`}> */}
										<Link to="/product/application">
											<button className="drs-btn drs-btn-normal">
												<FormattedMessage id="back"/>
											</button>
										</Link>
										{ d.u.isSp == false
											?   <button className="drs-btn drs-btn-cta"
													onClick={()=>{ 
														checkIfValid('ph-3')
														// handleProcess("ph-4", 4);
														// handleSave();
													}}
												>
													<FormattedMessage id="insurance.reply"/>
												</button>
											: 	null
										}
									</div>
                                :   null
                            }
							{ p2m.ins.process == 'ph-4'
								?	d.u.isSp == true && p2m.ins.caseTypeOfInsure !== 'D'
										? 	<button className="drs-btn drs-btn-cta" onClick={()=>{handleProcess("ph-5", 5)}}>
												<FormattedMessage id="insurance.next"/>
											</button>
										:   null
								:	null
							}
							{ p2m.ins.process == 'ph-5'
								?	<div>
										<button className="drs-btn drs-btn-normal" onClick={()=>{handleProcess("ph-4",4)}}>
											<FormattedMessage id="insurance.previous"/>
										</button>
										{ d.u.isSp == true 
											?	<button className="drs-btn drs-btn-cta" onClick={()=>{ checkIfValid('ph-5')}}>
													<FormattedMessage id="insurance.submit"/>
												</button>
											:  null
										} 
									</div>
								:	null
							}
							{ p2m.ins.process == 'ph-6'
								?	<div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
										{/* <Link to={`/product/application/${id}`}> */}
										<Link to="/product/application">
											<button className="drs-btn drs-btn-normal">
												<FormattedMessage id="back"/>
											</button>
										</Link>
										{ d.u.isSp == false
											?  	<div style={{display: 'flex', alignItems: 'center', justifyContent: 'center'}}>
													<button className="drs-btn drs-btn-cta" onClick={()=>{ handleProcess("ph-7", 7); handleSave() }}>
														<FormattedMessage id="insurance.approve"/>
													</button>
													<button className="drs-btn drs-btn-warning" onClick={()=>{ returnUpdate();  }}>
														<FormattedMessage id="insurance.return"/>
													</button>
												</div>
											: 	null
										}
									</div>
								:	null
							}
                            { p2m.ins.process == 'ph-7'
                                // ?  	<Link to={`/product/application/${id}`}>
								?   <Link to="/product/application">
										<button className="drs-btn drs-btn-normal">
											<FormattedMessage id="back"/>
                                    	</button>
									</Link>
                                : null
                            }
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default injectIntl(Insurance)

    // const [riskCheckList, setRiskCheckList ] = useState([
    //     { value: '航空器/飛彈/太空方面 Aircraft/Missile/Aerospace', isSelected: false },
    //     { value: '水上或海上交通工具 Watercraft or Offshore', isSelected: false },
    //     { value: '內陸交通運輸工具 Transportation/Transit', isSelected: false },
    //     { value: '醫療、維生、復健用品或設備 Medical, Life Support, Rehabilitation Service/Device/Equipment', isSelected: false },
    //     { value: '藥品、化妝保養品 Drug or Cusmetics', isSelected: false },
    //     { value: '嬰兒用品 Infant Products', isSelected: false }
    // ]);