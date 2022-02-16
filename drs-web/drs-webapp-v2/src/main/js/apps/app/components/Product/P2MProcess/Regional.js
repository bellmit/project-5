import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux'
import Select from 'react-select';
import DatePicker from 'react-datepicker'
import "react-datepicker/dist/react-datepicker.css";
import { initP2MRegional, saveP2MRegional, updateRe, getExactApplication } from '../../../actions';
import { MultiImgUploader } from '../common/MultiImgUploader';
import Comment from './common/Comment';
import RateReviewIcon from '@material-ui/icons/RateReview';
import {FormattedMessage} from 'react-intl';
import InsertPhotoIcon from '@material-ui/icons/InsertPhoto';
import { Link, Redirect, useParams } from 'react-router-dom';
import axios from 'axios';
import {DOMAIN_NAME} from '../../../constants/action-types';
import { toast } from 'react-toastify';
import FileUploader from './common/FileUploader';
import P2MApplicationInfo from './P2MApplicationInfo';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import Modal from '@material-ui/core/Modal';


// const batteryCategoryOptions =  [
//     { value: '無電池(No battery)', label: '無電池(No battery)' },
//     { value: '鹹性電池(Alkali-manganese batteries)、碳鋅電池(Zinc-Carbon batteries)、鎳鎘電池(Nickel-cadmium battries)',
//       label: '鹹性電池(Alkali-manganese batteries)、碳鋅電池(Zinc-Carbon batteries)、鎳鎘電池(Nickel-cadmium battries)'
//     },
//     { value: '鋰電池(Lithium batteries)', label: '鋰電池(Lithium batteries)' },
//     { value: '濕式電池', label: '濕式電池' },
//     // { value: 'AAA', label: 'AAA' },
//     // { value: 'AAAA', label: 'AAAA' },
//     // { value: 'C', label: 'C' },
//     // { value: 'CR123A', label: 'CR123A' },
//     // { value: 'CR2', label: 'CR2' },
//     // { value: 'CR5', label: 'CR5' },
//     // { value: 'D', label: 'D' },
//     // { value: '鋰離子', label: '鋰離子' },
//     // { value: '鋰金屬', label: '鋰金屬' },
//     // { value: '鎳氫', label: '鎳氫' },
//     // { value: 'P76', label: 'P76' },
//     // { value: '產品專屬電池', label: '產品專屬電池' },
// ];

const customStyles = {
    control: (provided, state) => ({
        ...provided,
        background: '#fff',
        borderColor: '#d7dbec',
        minHeight: '37px',
        boxShadow: state.isFocused ? null : null,
    }),

    valueContainer: (provided, state) => ({
        ...provided,
        position:'static',
        display: 'grid',
        padding: '0 6px'
    }),
    multiValue: (provided, state) => ({
        ...provided,
        width: 'fit-content',
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

const Regional = () => {
    const dispatch = useDispatch()
    // const { id } = useParams()
    const d = useSelector(state => state.d)
    const p2m = useSelector(state => state.P2M)
    const toaster = <FormattedMessage id = "managep2maction.savedsuccessfully"/>;
    // const [skuOptions, setSkuOptions] = useState([]);
    const fileTypeOptions = [
        {value : 'testingReport' , label : '測試報告'},
        {value : 'certification' , label : '證書'},
        {value : 'selfDeclaration' , label : '自我宣告'},
        {value : 'registerDoc' , label : '註冊文件'},
        {value : 'other' , label : '其他'}];

    const complianceTypeOptions = [
        {value : 'ce' , label : 'CE'},
        {value : 'reach' , label : 'REACH'},
        {value : 'rohs' , label : 'RoHS'},
        {value : 'weee' , label : 'WEEE'},
        {value : 'fcc' , label : 'FCC'},
        {value : 'fda' , label : 'FDA'},
        {value : 'msds' , label : 'MSDS'},
        {value : 'ncc' , label : 'NCC'},
        {value : 'bluetooth' , label : 'Bluetooth 藍芽授權'},
        {value : 'ca65' , label : '加州 65 法案'},
        {value : 'otherCertificationOrDoc' , label : '其他認證或註冊文件'}
    ]

    useEffect(() => {
        const p2mId = p2m.currentAp._id.$oid;

        if (!p2mId) {
            console.log('refresh get')
            // dispatch(getExactApplication(id))
            // dispatch(initP2MRegional(id));
        } else {
            dispatch(initP2MRegional(p2mId))
        }

        if (p2m.smoothScroll[0]) {
            smoothScroll(document.getElementById(p2m.smoothScroll[1]))
        }
    }, []);

    const option = [
      {value: 'Select All', label: 'Select All'}
    ]
    const options = option.concat(p2m.skuOptions).concat([{value: 'Clear All', label: 'Clear All'}])


    // handle selected sku change
    const handleSelectSku = (e, index, target) => {
        if(!e) { e = [] }
        const selectLabel = e.map(item => {return item.label})
        const targetSubAp = p2m.re
        switch(target) {
            case 'certification':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.certificateFile[index].appliedSku = p2m.skuOptions;
                    return dispatch(updateRe(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true){
                    targetSubAp.certificateFile[index].appliedSku = [];
                    return dispatch(updateRe(targetSubAp))
                } else {
                    targetSubAp.certificateFile[index].appliedSku = e;
                    return dispatch(updateRe(targetSubAp))
                }
            case 'patent':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.patentFile[index].appliedSku = p2m.skuOptions;
                    return dispatch(updateRe(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true){
                    targetSubAp.patentFile[index].appliedSku = [];
                    return dispatch(updateRe(targetSubAp))
                } else {
                    targetSubAp.patentFile[index].appliedSku = e;
                    return dispatch(updateRe(targetSubAp))
                }
            case 'other':
                if (selectLabel.includes('Select All') == true) {
                    targetSubAp.otherFile[index].appliedSku = e;
                    return dispatch(updateRe(targetSubAp))
                } else if (selectLabel.includes('Clear All') == true){
                    targetSubAp.otherFile[index].appliedSku = e;
                    return dispatch(updateRe(targetSubAp))
                } else {
                    targetSubAp.otherFile[index].appliedSku = e;
                    return dispatch(updateRe(targetSubAp))
                }
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

    const uploadImg = (input) => {

        //todo arthur not finish , have to refactor
        var formData = new FormData();
        formData.append('p2mName' , p2m.currentAp.name )
        formData.append('file', input);

        axios({
            method: "POST",
            url: DOMAIN_NAME + '/p2m/upi',
            data: formData,
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }).then(result => {
            console.log(result, 'upload result')
        })
    }

    // certificationFiles
    const handleCertificationFile = (action, index, event) => {
        const targetSubAp = p2m.re

        if (action == 'upload') {

            targetSubAp.certificateFile[index].name = event.target.files[0].name;
            upload(event.target.files[0])
        } else if (action == 'minus') {
            targetSubAp.certificateFile.length > 1 ? null : alert('必須上傳一份文件')
            targetSubAp.certificateFile.splice(index, 1)
        } else if (action == 'drop') {
            targetSubAp.certificateFile[index].name = 'Choose a file'
        } else if (action == 'add'){
            let newUploadInput = {
                name: 'Choose a file',
                appliedSku: [],
                modelNumber: '',
                documentType: '',
                documentValidDate: new Date(),
                certificationBody: '',
                complianceType: ''
            };
            targetSubAp.certificateFile.push(newUploadInput)
        }

        dispatch(updateRe(targetSubAp))
    }

    const onCertificationFileInfoChange = (e, index, type) => {
        if(!e) { e = [] }
        console.log(e, index, type, 'oncertificatefileinfochange')
        const targetSubAp = p2m.re
        switch(type) {
            case 'modelNumber':
                targetSubAp.certificateFile[index].modelNumber = e.target.value;
                return dispatch(updateRe(targetSubAp))
            case 'documentType':
                targetSubAp.certificateFile[index].documentType = e;
                return dispatch(updateRe(targetSubAp))
            case 'certificationBody':
                targetSubAp.certificateFile[index].certificationBody = e.target.value;
                return dispatch(updateRe(targetSubAp))
            case 'complianceType':
                targetSubAp.certificateFile[index].complianceType = e;
                return dispatch(updateRe(targetSubAp))
        }
    }

    const onCertificateDocumentValidDateChange = (date, index) => {
        const targetSubAp = p2m.re
        targetSubAp.certificateFile[index].documentValidDate = date;
        dispatch(updateRe(targetSubAp))
    }

    // patent files
    const handlePatentFile = (action, index, event) => {

        const targetSubAp = p2m.re;
        if (action == 'upload') {

            targetSubAp.patentFile[index].name = event.target.files[0].name;
            upload(event.target.files[0])
        } else if (action == 'minus') {
            // targetSubAp.patentFile.length > 1 ? targetSubAp.patentFile.splice(index, 1): targetSubAp.patentFile[0].name = 'Choose a file'
            targetSubAp.patentFile.length > 1 ? null : alert('必須上傳一份文件')
            targetSubAp.patentFile.splice(index, 1)
        } else if (action == 'drop') {
            targetSubAp.patentFile[index].name = 'Choose a file'
        } else if (action == 'add'){
            let newUploadInput = {
                name: 'Choose a file',
                appliedSku: [],
                modelNumber: '',
                documentType: '',
                documentValidDate: new Date(),
                certificationBody: '',
                complianceType: ''
            };
            targetSubAp.patentFile.push(newUploadInput)
        }

        dispatch(updateRe(targetSubAp))
    }
    const onPatentInfoChange = (e, index, type) => {
        if(!e) { e = [] }
        const targetSubAp = p2m.re
        switch(type) {
            case 'modelNumber':
                targetSubAp.patentFile[index].modelNumber = e.target.value;
                return dispatch(updateRe(targetSubAp))
            case 'documentType':
                targetSubAp.patentFile[index].documentType = e;
                return dispatch(updateRe(targetSubAp))
            case 'certificationBody':
                targetSubAp.patentFile[index].certificationBody = e.target.value;
                return dispatch(updateRe(targetSubAp))
            case 'complianceType':
                targetSubAp.patentFile[index].complianceType = e;
                return dispatch(updateRe(targetSubAp))
        }
    }
    const onPatentDocumentValidDateChange = (date, index) => {
        const targetSubAp = p2m.re;
        targetSubAp.patentFile[index].documentValidDate = date;
        dispatch(updateRe(targetSubAp))
    }

    // other files
    const handleOtherFile = (action, index, event) => {
        const targetSubAp = p2m.re
        if (action == 'upload') {
            targetSubAp.otherFile[index].name = event.target.files[0].name;
            upload(event.target.files[0])
        } else if (action == 'drop') {
            // targetSubAp.otherFile.length > 1 ? targetSubAp.otherFile.splice(index, 1): targetSubAp.otherFile[0].name = 'Choose a file'
            targetSubAp.otherFile[index].name = 'Choose a file'
        } else if (action == 'minus') {
            targetSubAp.otherFile.length > 1 ? null : alert('必須上傳一份文件')
            targetSubAp.otherFile.splice(index, 1)
        } else if (action == 'add') {
            let newUploadInput = {
                name: 'Choose a file',
                appliedSku: [],
                description: ''
            };
            targetSubAp.otherFile.push(newUploadInput)
        }
        dispatch(updateRe(targetSubAp))
    }
    const onOtherFileInfoChange = (e, index) => {
        const targetSubAp = p2m.re
        targetSubAp.otherFile[index].description = e.target.value;
        dispatch(updateRe(targetSubAp))
    }

    const savingMsg = <FormattedMessage id = "managep2m.savingMessage"/>
    const [saveTimeout, setSaveTimeout] = useState(false)
    const [certificateFileReminder, setCertificateFile] = useState(false)
    const [otherFileReminder, setOtherFile] = useState(false)
    const [patentFileReminder, setPatentFile] = useState(false)

    const handleCertificateFileOpen = () => {
        setCertificateFile(true);
    }

    const handleCertificateFileClose = () => {
        setCertificateFile(false);
    }

    const handlePatentFileOpen = () => {
        setPatentFile(true);
    }

    const handlePatentFileClose = () => {
        setPatentFile(false);
    }

    const handleOtherFileOpen = () => {
        setOtherFile(true);
    }

    const handleOtherFileClose = () => {
        setOtherFile(false);
    }

    const certificateFilelearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-15%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleCertificateFileClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
                <p style={{lineHeight : '1.5rem'}}>
                    <p>全球常見之認證文件包含 FCC、FDA、CE、UKCA、RoHS等。台灣則有BSMI、NCC所規範之安全性報告。您需要提供目標市場端與供應端地區的認證文件，例如：台灣生產的電子產品出貨到美國亞馬遜銷售，請提供台灣 NCC 證書與美國的 FCC 證書。證書上必須顯示產品型號及品名，如在運送清關或銷售過程中遇到主管機關審查，DRS 才可在第一時間交出文件，產品認證資料越完整，越有利於順利通過審查。</p>
                    <p style={{marginTop:'2%'}}>產品認證不僅是為了讓產品在當地市場合法銷售，也是品牌與產品取得消費者信任的關鍵，同時保障消費者在銷售地區的權益。若您不確定產品是否需要哪些合規性文件，建議您可向 SGS、TUV 或 UL 認證單位尋求諮詢。</p>
                    <p style={{marginTop:'2%'}}>CE 及 UKCA 所需要的代理人（authorised representative or responsible person）的資料以及授權書也請在此欄位上傳。</p>
                </p>
        </div>

        );

    const patentFilelearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-25%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handlePatentFileClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                         <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
                <p style={{lineHeight : '1.5rem'}}>
                    <p>若您的產品使用的是自己的專利技術，我們很歡迎您提供專利文件，若您的產品使用的是受智慧財產權保護的專利技術，我們需要您提供受到授權的證書文件。若在銷售平台發生專利糾紛，或於銷售通路被提出質疑時，準備充份的認證文件能夠第一時間證明您是專利持有人。</p>
                </p>
        </div>

        );

    const otherFilelearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
            borderRadius: '4px', padding : '2%'}}>
            <div className="modal-close-wrapper" style={{position: 'absolute',top: '-30%', left: '0%'}}>
                <button className="modal-close-btn" onClick={() => {handleOtherFileClose()}}>
                <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                <span>
                        <FormattedMessage id="addnewproduct.close"/>
                </span>
                </button>
            </div>
            <p style={{lineHeight : '1.5rem'}}>
                <p>若市場地區要求產品加入警語，例如：塑膠袋警告標示，請上傳產品包裝所附塑膠袋上標示警語的照片。例如：如果產品包含插頭，請附上符合該市場使用的插頭照片，且須能夠清楚辨別插頭的詳細規格。</p>
            </p>
        </div>

        );

    // save
    const handleSave = (status) => {
        setSaveTimeout(true)
        toast(savingMsg)

        const regional = p2m.re
        regional.p2mApplicationId = p2m.currentAp._id;
        regional.status = status;
        const isComment = false;

        regional.certificateFile.map((item,index) => {
            item.appliedSku.map(sku => {
                regional.appliedSku.map(obj => {
                    obj.skuCode == sku.value
                    ? obj.certificateFile[index] = {
                        name: item.name,
                        modelNumber: item.modelNumber,
                        documentType: item.documentType,
                        documentValidDate: item.documentValidDate,
                        certificationBody: item.certificationBody,
                        complianceType: item.complianceType
                      }
                    : null
                })
            })
        })
        regional.patentFile.map((item,index) => {
            item.appliedSku.map(sku => {
                regional.appliedSku.map(obj => {
                    obj.skuCode == sku.value
                    ? obj.patentFile[index] = {
                        name: item.name,
                        modelNumber: item.modelNumber,
                        documentType: item.documentType,
                        documentValidDate: item.documentValidDate,
                        certificationBody: item.certificationBody,
                        complianceType: item.complianceType
                      }
                      : null
                })
            })
        })
        regional.otherFile.map((item,index) => {
            item.appliedSku.map(sku => {
                regional.appliedSku.map(obj => {
                    obj.skuCode == sku.value ? obj.otherFile[index] = {
                        name: item.name,
                        description: item.description,
                    } : null
                })
            })
        })

        regional.appliedSku.map((item,index) => {
            item.productImg.map(img => {
                img.preview=''
            })
        })

        // const redirect = isComment ? '': `/product/application/${id}`;
        const redirect = isComment ? '' :  `/product/application`

        setTimeout(() => {
            setSaveTimeout(false)
            dispatch(saveP2MRegional(regional,toaster,redirect))
        }, 3000)

    }

    const handleSaveComment = (content,index, target) => {
        const regional = p2m.re
        regional.p2mApplicationId = p2m.currentAp._id;
        const isComment = true;
        // const redirect = isComment ? '': `/product/application/${id}`;
        const redirect = isComment ? '' :  `/product/application`

        if (target == 'productImg') {
            regional.comment.productImg = content
        } else if (target == 'otherFile') {
            regional.comment.otherFile = content
        } else if (target == 'patentFile') {
            regional.comment.patentFile = content
        } else if (target == 'certificateFile') {
            regional.comment.certificateFile = content
        }

        dispatch(saveP2MRegional(regional,toaster,redirect))
    }
    const handleCommentChange = (content, index, target) => {
        const targetSubAp = p2m.re;

        if (target == 'productImg') {
            targetSubAp.comment.productImg = content
        } else if (target == 'otherFile') {
            targetSubAp.comment.otherFile = content
        } else if (target == 'patentFile') {
            targetSubAp.comment.patentFile = content
        } else if (target == 'certificateFile') {
            targetSubAp.comment.certificateFile = content
        }

        dispatch(updateRe(targetSubAp))
    }

    const [showProductImg, setShowProductImgEditor] = useState(false)
    const [showOtherFile, setShowOtherFileEditor] = useState(false)
    const [showPatentFile, setShowPatentFileEditor] = useState(false)
    const [showCertificateFile, setShowCertificateFileEditor] = useState(false)

    const handleShowEditor = (target) => {
        switch(target) {
            case 'productImg':
                return showProductImg ? setShowProductImgEditor(false): setShowProductImgEditor(true)
            case 'otherFile':
                return showOtherFile ? setShowOtherFileEditor(false) : setShowOtherFileEditor(true)
            case 'patentFile':
                return showPatentFile ? setShowPatentFileEditor(false) : setShowPatentFileEditor(true)
            case 'certificateFile':
                return showCertificateFile ? setShowCertificateFileEditor(false): setShowCertificateFileEditor(true)
            default:
                return null
        }
    }

    const [showUploader, setShowUploader] = useState(false);
    const [showIndex, setShowIndex] = useState(0)
    const onUpload = (index) => {
        // console.log(index);
        setShowIndex(index)
        if (showUploader){
            return setShowUploader(false)
        }
        setShowUploader(true)
        setShowValidWarning(false)
    }
    const handleSubmitImg = (accFiles,index) => {
        accFiles.forEach((file) => {
            const reader = new FileReader()
            reader.onabort = () => console.log('file reading was aborted')
            reader.onerror = () => console.log('file reading has failed')
            reader.onload = () => {
            // Do whatever you want with the file contents
            }
           // reader.readAsArrayBuffer(file)
            uploadImg(file)
        })
        const arr = p2m.re.appliedSku;
        if (accFiles.length > 0) {
            arr[index].productImg = accFiles.map(item => {
                return { name: item.name, preview: item.preview }
            })
        }

        const targetSubAp = p2m.re;
        targetSubAp.appliedSku = arr;
        dispatch(updateRe(targetSubAp))
        setShowUploader(false)
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


    const handleUploadAgain = (index) => {
        setShowIndex(index)
        setShowUploader(true);
    }
    const handleRemoveAll = (index) => {
        const targetSubAp = p2m.re
        targetSubAp.appliedSku[index].productImg = []
        dispatch(updateRe(targetSubAp))
    }

    const [showValidWarning, setShowValidWarning] = useState(false)

    const handleValid = () => {
        let checkValid = []
         p2m.re.appliedSku.map(item => {
            item.productImg.length == 0 ? checkValid.push(false): null
        })
//        for (let index = 0; index < p2m.re.appliedSku.length; index++) {
//            if(p2m.re.appliedSku[index].productImg.length == 0){
//                checkValid.push(false)
//            }
//        }
        if (checkValid.length !== 0) {
            setShowValidWarning(true)
        } else {
            handleSave("Pending")
        }

    }

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
                            {p2m.currentAp.name}-<FormattedMessage id="subApplication.regionalTitle"/>
                        </p>
                        <div className="application-subtitle-info-wrapper">
                            <div className="application-info-column" style={{width: '48%'}}>
                                <div className="application-info">
                                    <p className="application-info-title" style={{width: '50%'}}>
                                        <FormattedMessage id="p2mapplication.formtype"/>
                                    </p>
                                    <p className="application-info-content">
                                        <FormattedMessage id="subApplication.regional"/>
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
                            <FormattedMessage id="regional.title1"/>
                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                *
                            </span>
                        </p>
                        <div className="application-info-section column align-center mt-1">
                            <p className="application-info-section-subtitle" style={{display: 'flex', justifyContent: 'space-between'}}>
                                { d.u.isSp
                                    ? null
                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                        ?   <button onClick={() => {
                                                handleShowEditor('productImg');
                                                showProductImg ? null : smoothScroll(document.getElementById('productImg'))
                                            }}><RateReviewIcon/></button>
                                        :   null
                                }
                            </p>
                            <div className="application-info-sub-section">
                                <p className="application-intro-paragraph enhance">
                                    <FormattedMessage id="regional.paragraph1"/>
                                </p>
                                <div className="application-info-row">

                                    { showUploader
                                        ? <MultiImgUploader
                                                onClose={setShowUploader}
                                                onSubmit={handleSubmitImg}
                                                index={showIndex}
                                                // accFiles={existFiles.length == 0 ? null : existFiles}
                                            />
                                        : null
                                    }
                                    <div className="yscroll-section scrollbox" style={{maxHeight: '700px'}}>
                                    { p2m.re.appliedSku.map((item, index) => {
                                        return (
                                            <div>
                                                <div style={{width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                                                    <p style={{width: '9%', fontSize: '1rem', fontWeight: '600'}}>{item.skuCode}</p>
                                                    { item.productImg.length == 0 || p2m.currentAp.status !== p2m.stMap.get(0)
                                                        ?   <p className="drs-blue" style={{width: '130%', fontSize: '13px'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="picture.required"/>
                                                            </p>
                                                        :   <div style={{display: 'flex', alignItems: 'center'}}>
                                                                <button
                                                                    className="drs-btn drs-btn-normal"
                                                                    onClick={() => handleUploadAgain(index) }>
                                                                    <span style={{marginLeft:'6px'}}>重新上傳</span>
                                                                </button>
                                                                <button
                                                                    className="drs-btn drs-btn-normal"
                                                                    onClick={() => handleRemoveAll(index) }>
                                                                    <span style={{marginLeft:'6px'}}>清空全部</span>
                                                                </button>
                                                            </div>
                                                    }
                                                </div>
                                                <div>
                                                    { item.productImg.length == 0
                                                        ?   <div className="upload-img-area">
                                                                <button
                                                                    className={`upload-img-btn ${!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0) ? 'disabled': ''}`}
                                                                    onClick={() => { onUpload(index)}}
                                                                    disabled={!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                >
                                                                    <InsertPhotoIcon fontSize="medium"/>
                                                                    <span style={{marginLeft:'6px'}}>
                                                                        <FormattedMessage id="productinfo.uploadimage"/>
                                                                    </span>
                                                                </button>
                                                            </div>
                                                        :   <div className="upload-img-preview-wrapper">
                                                                {item.productImg.map((file) => (
                                                                    <div className="upload-img-preview-column">
                                                                        {file.preview == ''
                                                                         ? <div className="upload-img-preview"><img src={`${DOMAIN_NAME}/p2m/r/i/${p2m.currentAp.name}/${file.name}`}></img></div>
                                                                         : <div className="upload-img-preview"><img src={file.preview}></img></div>
                                                                        }
                                                                        <a href={`${DOMAIN_NAME}/p2m/r/i/${p2m.currentAp.name}/${file.name}`} download={file.name}>
                                                                            <p style={{fontSize: '14px', margin: '8px 0'}}>{file.name}</p>
                                                                        </a>
                                                                    </div>
                                                                ))}
                                                            </div>
                                                    }
                                                </div>
                                            </div>
                                        )
                                    })}
                                    </div>
                                    <div id="productImg">
                                        <Comment
                                            comment={p2m.re.comment.productImg}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showProductImg}
                                            handleShow={handleShowEditor}
                                            target='productImg'
                                            readonly={d.u.isSp}
                                        />
                                    </div>
                                </div>
                            </div>
                            <div className="section-line"></div>
                        </div>
                    </div>
                    <div className="section-line"></div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-title">
                            <FormattedMessage id="regional.title2"/>
                        </p>
                        <div className="application-intro-wrapper">
                            <p className="application-intro-paragraph">
                                <FormattedMessage id="regional.paragraph2"/>
                            </p>
                        </div>
                        <div className="application-info-section column align-center mt-1">
                            <p className="application-info-section-subtitle" style={{display: 'flex', justifyContent: 'space-between'}}>
                                <FormattedMessage id="regional.subtitle1"/>
                                { d.u.isSp
                                    ? null
                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                        ? <button onClick={() => {
                                            handleShowEditor('certificateFile');
                                            showCertificateFile ? null : smoothScroll(document.getElementById('certificateFile'))
                                        }}><RateReviewIcon/></button>
                                        : null
                                }
                            </p>

                            <div className="application-info-sub-section">
                                <div className="application-info-row">
                                    <p className="application-intro-paragraph enhance space-between">
                                        <div style={{width:'100%'}}>
                                            <FormattedMessage id="regional.paragraph4"/>
                                            <span style={{marginLeft: '1%', color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{handleCertificateFileOpen()}}>
                                                <i class="fa fa-question-circle"></i>
                                                <FormattedMessage id ="basedata.learnmore"></FormattedMessage>
                                            </span>
                                        </div>

                                        <Modal
                                            open={certificateFileReminder}
                                            onClose={handleCertificateFileClose}
                                            aria-labelledby="simple-modal-title"
                                            aria-describedby="simple-modal-description"
                                        >
                                          {certificateFilelearnMore}
                                        </Modal>
                                        { d.u.isSp
                                            ?   p2m.currentAp.status == p2m.stMap.get(0)
                                                ?   <div className="paragraph-inline-btn-wrapper" style={{width:'15%'}}>
                                                        <span style={{marginRight: '5px'}}>
                                                            <FormattedMessage id="regional.adduploadspace"/>
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handleCertificationFile('add')}
                                                        ><i class="fa fa-plus" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                :   null
                                            :   null
                                        }
                                    </p>
                                    { p2m.re.certificateFile.map((item, index) => (
                                    <div className="application-upload-section-wrapper">
                                        { item.appliedSku !== '' && item.name !== 'Choose a file' && item.modelNumber !== '' && item.documentType !== '' && item.certificationBody !== '' && item.complianceType !== ''
                                            ?   null
                                            :   item.appliedSku == '' && item.name == 'Choose a file' && item.modelNumber == '' && item.documentType == '' && item.certificationBody == '' && item.complianceType == ''
                                                ?   null
                                                :   <p style={{margin: '12px 0'}}>
                                                        <span className="drs-notice-red" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                            <FormattedMessage id="regional.title3"/>
                                                        </span>
                                                    </p>
                                        }
                                        {p2m.re.certificateFile.length >1
                                            ? <div className="paragraph-inline-btn-wrapper" >
                                                <span style={{marginRight: '5px'}}>
                                                    <FormattedMessage id="regional.remove"/>
                                                </span>
                                                <button
                                                    className="paragraph-inline-btn"
                                                    onClick={() => handleCertificationFile('minus',index)}
                                                    disabled={p2m.re.certificateFile.length > 1 ? false : true}>
                                                    <i class="fa fa-minus-square" aria-hidden="true"></i>
                                                </button>
                                            </div>
                                            : <div></div>
                                        }
                                        <div className="base-input-column-wrapper">
                                            <div className="base-input-row-wrapper" style={{alignItems:'normal'}}>
                                                <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%', alignItems:'normal'}}>
                                                    <div className="selector-inline-head" style={{minHeight: '37px',width: '27%',height:'fit-content'}}>
                                                        <span style={{fontSize: '14px'}}>
                                                            <FormattedMessage id="regional.applytosku"/>
                                                        </span>
                                                    </div>
                                                    <FormattedMessage id="regional.select">
                                                        { placeholder =>
                                                            <Select
                                                                className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                                                styles={customStyles}
                                                                closeMenuOnSelect={false}
                                                                value={item.appliedSku}
                                                                placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                onChange={(e) => handleSelectSku(e, index, 'certification')}
                                                                options={options}
                                                                classNamePrefix='drsSelector'
                                                                isMulti
                                                                isDisabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                </div>
                                                <div className="base-input-file-wrapper">
                                                    <div className="base-input-file-column">
                                                        <FileUploader
                                                            fileName={item.name}
                                                            index={index}
                                                            href={`${DOMAIN_NAME}/p2m/r/f/${p2m.currentAp.name}/${item.name}`}
                                                            onChange={handleCertificationFile}
                                                            disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                        />
                                                    </div>
                                                </div>
                                            </div>

                                            <p className="application-intro-sub-paragraph">
                                                <FormattedMessage id="regional.paragraph5"/>
                                            </p>
                                            <div className="base-input-row-wrapper" style={{marginTop: '1%'}}>
                                                <div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="regional.modelnumber"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper">
                                                            <FormattedMessage  id="regional.modelnumber">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        placeholder={placeholder}
                                                                        value={item.modelNumber}
                                                                        onChange={(e)=>onCertificationFileInfoChange(e, index, 'modelNumber')}
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
                                                                <FormattedMessage id="regional.documenttype"/>
                                                            </p>
                                                        </div>
                                                        <div style={{width : '50%'}}>
                                                            <FormattedMessage  id="regional.documenttype">
                                                                { placeholder =>
                                                                    <Select
                                                                        className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        value={item.documentType}
                                                                        placeholder={item.documentType.length == 0 ? placeholder: item.documentType}
                                                                        onChange={(e)=>onCertificationFileInfoChange(e, index, 'documentType')}
                                                                        options={fileTypeOptions}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        isDisabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                    >
                                                                    </Select>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>

                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="regional.documentvaliddate"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper">
                                                            <DatePicker
                                                                selected={new Date(item.documentValidDate)}
                                                                onChange={date => onCertificateDocumentValidDateChange(date,index)}
                                                                dateFormat="yyyy/MM/dd"
                                                                className="base-input date-picker"
                                                                disabled={p2m.currentAp.status !== p2m.stMap.get(0) ? true: false}
                                                            />
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="regional.certificationbody"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper">
                                                            <FormattedMessage  id="regional.certificationbody">
                                                                { placeholder =>
                                                                    <input
                                                                        className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        placeholder={placeholder}
                                                                        value={item.certificationBody}
                                                                        onChange={(e)=>onCertificationFileInfoChange(e, index, 'certificationBody')}
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
                                                                <FormattedMessage id="regional.complianttype"/>
                                                            </p>
                                                        </div>
                                                        <div style={{width: '50%'}}>
                                                            <FormattedMessage id="regional.complianttype">
                                                                { placeholder =>
                                                                    <Select
                                                                        className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        isMulti
                                                                        value={item.complianceType}
                                                                        placeholder={item.complianceType.length == 0? placeholder : item.complianceType}
                                                                        onChange={(e)=>onCertificationFileInfoChange(e, index, 'complianceType')}
                                                                        options={complianceTypeOptions}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        isDisabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                    >
                                                                    </Select>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    ))}
                                    <div id="certificateFile">
                                        <Comment
                                            comment={p2m.re.comment.certificateFile}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showCertificateFile}
                                            handleShow={handleShowEditor}
                                            target='certificateFile'
                                        />
                                    </div>
                                </div>
                            </div>

                            <p className="application-info-section-subtitle" style={{display: 'flex', justifyContent: 'space-between'}}>
                                <FormattedMessage id="regional.subtitle2"/>
                                { d.u.isSp
                                    ? null
                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                        ? <button onClick={() => {
                                            handleShowEditor('patentFile');
                                            showPatentFile ? null : smoothScroll(document.getElementById("patentFile"))
                                        }}><RateReviewIcon/></button>
                                        : null
                                }
                            </p>
                            <div className="application-info-sub-section">
                                <div className="application-info-row">
                                    <p className="application-intro-paragraph enhance space-between">
                                        <div style={{width:'100%'}}>
                                            <FormattedMessage id="regional.paragraph6"/>
                                            <span style={{marginLeft: '1%', color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{handlePatentFileOpen()}}>
                                                <i class="fa fa-question-circle"></i>
                                                <FormattedMessage id ="basedata.learnmore"></FormattedMessage>
                                            </span>
                                            {/* <span className="drs-notice-red" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                <FormattedMessage id="regional.title3"/>
                                            </span> */}
                                        </div>

                                        <Modal
                                            open={patentFileReminder}
                                            onClose={handlePatentFileClose}
                                            aria-labelledby="simple-modal-title"
                                            aria-describedby="simple-modal-description"
                                        >
                                          {patentFilelearnMore}
                                        </Modal>
                                        { d.u.isSp
                                            ?   p2m.currentAp.status == p2m.stMap.get(0)
                                                    ?   <div className="paragraph-inline-btn-wrapper" style={{width:'15%'}}>
                                                            <span style={{marginRight: '5px'}}>
                                                                <FormattedMessage id="regional.adduploadspace"/>
                                                            </span>
                                                            <button
                                                                className="paragraph-inline-btn"
                                                                onClick={() => handlePatentFile('add')}
                                                            ><i class="fa fa-plus" aria-hidden="true"></i>
                                                            </button>
                                                        </div>
                                                    :   null
                                            :   null
                                        }
                                    </p>
                                    { p2m.re.patentFile.map((item, index) => (
                                    <div className="application-upload-section-wrapper">
                                        { item.appliedSku !== '' && item.name !== 'Choose a file' && item.modelNumber !== '' && item.documentType !== '' && item.certificationBody !== '' && item.complianceType !== ''
                                            ?   null
                                            :   item.appliedSku == '' && item.name == 'Choose a file' && item.modelNumber == '' && item.documentType == '' && item.certificationBody == '' && item.complianceType == ''
                                                ?   null
                                                :   <p style={{margin: '12px 0'}}>
                                                        <span className="drs-notice-red" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                            <FormattedMessage id="regional.title3"/>
                                                        </span>
                                                    </p>
                                        }
                                        { p2m.re.patentFile.length > 1
                                          ? <div className="paragraph-inline-btn-wrapper" >
                                            <span style={{marginRight: '5px'}}>
                                                <FormattedMessage id="regional.remove"/>
                                            </span>
                                            <button className="paragraph-inline-btn"
                                                onClick={() => handlePatentFile('minus', index)}
                                                disabled={p2m.re.patentFile.length >1 ? false: true}
                                            >
                                                <i class="fa fa-minus-square" aria-hidden="true"></i>
                                            </button>
                                        </div>
                                         :<div></div>
                                        }
                                        <div className="base-input-column-wrapper">
                                            <div className="base-input-row-wrapper" style={{alignItems:'normal'}}>
                                                <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%', alignItems:'normal'}}>
                                                    <div className="selector-inline-head" style={{minHeight: '37px',width: '27%',height:'fit-content'}}>
                                                        <span style={{fontSize: '14px'}}>
                                                            <FormattedMessage id="regional.applytosku"/>
                                                        </span>
                                                    </div>
                                                    <FormattedMessage id="regional.select">
                                                        { placeholder =>
                                                            <Select
                                                                className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                                                styles={customStyles}
                                                                closeMenuOnSelect={false}
                                                                value={item.appliedSku}
                                                                placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                onChange={(e) => handleSelectSku(e, index, 'patent')}
                                                                options={options}
                                                                classNamePrefix='drsSelector'
                                                                isMulti
                                                                isDisabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                            />
                                                        }
                                                    </FormattedMessage>
                                                </div>
                                                <div className="base-input-file-wrapper">
                                                    <div className="base-input-file-column">
                                                        <FileUploader
                                                            fileName={item.name}
                                                            index={index}
                                                            href={`${DOMAIN_NAME}/p2m/r/f/${p2m.currentAp.name}/${item.name}`}
                                                            onChange={handlePatentFile}
                                                            disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                        />
                                                    </div>
                                                </div>
                                            </div>

                                            <p className="application-intro-sub-paragraph">
                                                <FormattedMessage id="regional.paragraph7"/>
                                            </p>
                                            <div className="base-input-row-wrapper" style={{marginTop: '1%'}}>
                                                <div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="regional.modelnumber"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper">
                                                            <FormattedMessage id="regional.modelnumber">
                                                                { placeholder =>
                                                                <input
                                                                    className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                    placeholder={placeholder}
                                                                    value={item.modelNumber}
                                                                    onChange={(e)=>onPatentInfoChange(e, index, 'modelNumber')}
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
                                                                <FormattedMessage id="regional.documenttype"/>
                                                            </p>
                                                        </div>
                                                        <div style={{width: '50%'}}>
                                                        <FormattedMessage  id="regional.documenttype">
                                                            { placeholder =>
                                                                <Select
                                                                    className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                    value={item.documentType}
                                                                    placeholder={item.documentType.length == 0 ? placeholder: item.documentType}
                                                                    onChange={(e)=>onPatentInfoChange(e, index, 'documentType')}
                                                                    options={fileTypeOptions}
                                                                    readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                    isDisabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                >
                                                                </Select>
                                                            }
                                                        </FormattedMessage>
                                                        </div>
                                                    </div>

                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="regional.documentvaliddate"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper">
                                                            <DatePicker
                                                                selected={new Date(item.documentValidDate)}
                                                                onChange={date => onPatentDocumentValidDateChange(date,index)}
                                                                dateFormat="yyyy/MM/dd"
                                                                className="base-input date-picker"
                                                                disabled={p2m.currentAp.status !== p2m.stMap.get(0) ? true: false}
                                                            />
                                                        </div>
                                                    </div>
                                                    <div className="application-input-wrapper align-center">
                                                        <div className="base-input-label-wrapper">
                                                            <p className="application-input-label">
                                                                <FormattedMessage id="regional.certificationbody"/>
                                                            </p>
                                                        </div>
                                                        <div className="base-input-inline-wrapper">
                                                        <FormattedMessage  id="regional.certificationbody">
                                                            { placeholder =>
                                                                <input
                                                                    className={`base-input ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                    placeholder={placeholder}
                                                                    value={item.certificationBody}
                                                                    onChange={(e)=>onPatentInfoChange(e, index, 'certificationBody')}
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
                                                                <FormattedMessage id="regional.complianttype"/>
                                                            </p>
                                                        </div>
                                                        <div style={{width : '50%'}}>
                                                            <FormattedMessage  id="regional.complianttype">
                                                                { placeholder =>
                                                                    <Select
                                                                        className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                        isMulti
                                                                        value={item.complianceType}
                                                                        placeholder={item.complianceType.length == 0? placeholder : item.complianceType}
                                                                        onChange={(e)=>onPatentInfoChange(e, index, 'complianceType')}
                                                                        options={complianceTypeOptions}
                                                                        readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        isDisabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                    >
                                                                    </Select>
                                                                }
                                                            </FormattedMessage>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    ))}
                                    <div id="patentFile">
                                        <Comment
                                            comment={p2m.re.comment.patentFile}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showPatentFile}
                                            handleShow={handleShowEditor}
                                            target='patentFile'
                                        />
                                    </div>
                                </div>
                            </div>

                            <p className="application-info-section-subtitle" style={{display: 'flex', justifyContent: 'space-between'}}>
                                <FormattedMessage id="regional.subtitle3"/>
                                { d.u.isSp
                                    ? null
                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                        ? <button onClick={() => {
                                            handleShowEditor('otherFile');
                                            showOtherFile ? null : smoothScroll(document.getElementById("otherFile"))
                                        }}><RateReviewIcon/></button>
                                        : null
                                }
                            </p>
                            <div className="application-info-sub-section">
                                <div className="application-info-row">
                                    <p className="application-intro-paragraph enhance space-between">
                                        <div style={{width:'100%'}}>
                                            <FormattedMessage id="regional.paragraph8"/>
                                            <span style={{marginLeft: '1%', color: '#51cbce' , cursor : 'pointer'}} onClick={()=>{handleOtherFileOpen()}}>
                                                <i class="fa fa-question-circle"></i>
                                                <FormattedMessage id ="basedata.learnmore"></FormattedMessage>
                                            </span>
                                            {/* <span className="drs-notice-red" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                <FormattedMessage id="regional.title3"/>
                                            </span> */}
                                        </div>

                                        <Modal
                                            open={otherFileReminder}
                                            onClose={handleOtherFileClose}
                                            aria-labelledby="simple-modal-title"
                                            aria-describedby="simple-modal-description"
                                        >
                                          {otherFilelearnMore}
                                        </Modal>
                                        { d.u.isSp
                                            ?   p2m.currentAp.status == p2m.stMap.get(0)
                                                ?   <div className="paragraph-inline-btn-wrapper" style={{width:'15%'}}>
                                                        <span style={{marginRight: '5px'}}>
                                                            <FormattedMessage id="regional.adduploadspace"/>
                                                        </span>
                                                        <button
                                                            className="paragraph-inline-btn"
                                                            onClick={() => handleOtherFile('add')}
                                                        ><i class="fa fa-plus" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                :   null
                                            :   null
                                        }
                                    </p>
                                    { p2m.re.otherFile.map((item, index) => (
                                        <div className="application-upload-section-wrapper">
                                            { item.appliedSku !== '' && item.name !== 'Choose a file' && item.description !== ''
                                                ?   null
                                                :   item.appliedSku == '' && item.name == 'Choose a file' && item.description == ''
                                                    ?   null
                                                    :   <p style={{margin: '12px 0'}}>
                                                            <span className="drs-notice-red" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="regional.title3"/>
                                                            </span>
                                                        </p>
                                            }
                                            { p2m.re.otherFile.length > 1
                                                ?   <div className="paragraph-inline-btn-wrapper" >
                                                        <span style={{marginRight: '5px'}}>
                                                            <FormattedMessage id="regional.remove"/>
                                                        </span>
                                                        <button className="paragraph-inline-btn" onClick={() => handleOtherFile('minus',index)} disabled={p2m.re.otherFile.length >1 ? false: true}>
                                                            <i class="fa fa-minus-square" aria-hidden="true"></i>
                                                        </button>
                                                    </div>
                                                : <div></div>
                                            }
                                            <div className="base-input-column-wrapper">
                                                <div className="base-input-row-wrapper" style={{alignItems:'normal'}}>
                                                    <div className="selector-inline-wrapper" style={{width: '60%', marginRight: '1%', alignItems:'normal'}}>
                                                        <div className="selector-inline-head" style={{minHeight: '37px',width: '27%',height:'fit-content'}}>
                                                            <span style={{fontSize: '14px'}}>
                                                                <FormattedMessage id="regional.applytosku"/>
                                                            </span>
                                                        </div>
                                                        <FormattedMessage id="regional.select">
                                                            { placeholder =>
                                                                <Select
                                                                    className={`base-selector ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                                                    styles={customStyles}
                                                                    closeMenuOnSelect={false}
                                                                    value={item.appliedSku}
                                                                    placeholder={item.appliedSku.length == 0 ? placeholder: item.appliedSku}
                                                                    onChange={(e) => handleSelectSku(e, index, 'other')}
                                                                    options={options}
                                                                    classNamePrefix='drsSelector'
                                                                    isMulti
                                                                    isDisabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                />
                                                            }
                                                        </FormattedMessage>
                                                    </div>
                                                    <div className="base-input-file-wrapper">
                                                        <div className="base-input-file-column">
                                                            <FileUploader
                                                                fileName={item.name}
                                                                index={index}
                                                                href={`${DOMAIN_NAME}/p2m/r/f/${p2m.currentAp.name}/${item.name}`}
                                                                onChange={handleOtherFile}
                                                                disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                            />
                                                        </div>
                                                    </div>
                                                </div>

                                                <p className="application-intro-sub-paragraph">
                                                    <FormattedMessage id="regional.paragraph9"/>
                                                </p>
                                                <div className="base-input-row-wrapper" style={{marginTop: '1%'}}>
                                                    <div className="base-input-column-wrapper" style={{width: '100%', alignItems: 'center'}}>
                                                        <div className="application-input-wrapper">
                                                            <div className="base-input-label-wrapper" style={{marginLeft: '4px', width: '10%'}}>
                                                                <p className="application-input-label" style={{marginTop: '2%'}}>
                                                                    <FormattedMessage id="regional.description"/>
                                                                </p>
                                                            </div>
                                                            <div className="base-input-with-description-wrapper" style={{width: '84%'}}>
                                                                <FormattedMessage id="regional.paragraph10">
                                                                    { placeholder =>
                                                                        <textarea
                                                                            id={`${item.skuCode}-other-file-description`}
                                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            name={`${item.skuCode}-other-file-description`}
                                                                            rows="4"
                                                                            value={item.description}
                                                                            placeholder={placeholder}
                                                                            onChange={(e)=> onOtherFileInfoChange(e, index)}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                        >
                                                                        </textarea>
                                                                    }
                                                                </FormattedMessage>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                    <div id="otherFile">
                                        <Comment
                                            comment={p2m.re.comment.otherFile}
                                            handleSaveComment={handleSaveComment}
                                            handleCommentChange={handleCommentChange}
                                            edit={showOtherFile}
                                            handleShow={handleShowEditor}
                                            target='otherFile'
                                        />
                                    </div>
                                </div>
                            </div>
                            <div>
                                {showValidWarning
                                    ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                            <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                                <FormattedMessage id="regional.warning"/>
                                            </p>
                                        </div>
                                    :   null
                                }
                            </div>
                        </div>
                    </div>
                    <div className="section-line"></div>
                    <div className="operation-pannel-footer" style={{display:'flex',justifyContent:'end'}}>
                        <div className="operation-btn-group">
                            { d.u.isSp && p2m.currentAp.status == p2m.stMap.get(0)
                                ?   <button className="drs-btn" style={{backgroundColor:'#0085CC'}} onClick={() => {handleSave("Draft")}}>
                                        儲存草稿
                                    </button>
                                :   null
                            }
                        </div>
                        <div className="operation-btn-group">
                            { d.u.isSp && p2m.currentAp.status == p2m.stMap.get(0)
                                ?   <button className="drs-btn drs-btn-cta" onClick={() => {handleValid()}}>
                                        <FormattedMessage id="save"/>
                                    </button>
                                // :   <Link to={`/product/application/${id}`}>
                                :   <Link to="/product/application">
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

export default Regional