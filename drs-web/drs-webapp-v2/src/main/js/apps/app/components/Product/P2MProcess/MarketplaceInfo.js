import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Link, Redirect, useParams } from 'react-router-dom';
import expandIcon from '../../../assets/images/chevron-left-icon@2x.png'
import expandDownIcon from '../../../assets/images/chevron-down-icon-blue@2x.png'
// import smapleImg from '../../../assets/images/marketplace-sample-export@2x.png'
import InsertPhotoIcon from '@material-ui/icons/InsertPhoto';
import { MultiImgUploader } from '../common/MultiImgUploader';
import axios from 'axios';
import {DOMAIN_NAME} from '../../../constants/action-types';
import { Dropzone } from '../common/Dropzone';
import Comment from './common/Comment';
import { saveP2MMarketplaceInfo, initP2MMarketplaceInfo, updateCurrentSubAp,updateMp, changeMpSkuDesShow, 
    changeMpSkuMainTitleShow,changeMpSkuVariationNameShow, changeMpSkuKeywordShow, changeMpSkuFeatureShow,
    changeMpSkuImgShow, getExactApplication
} from '../../../actions';
import RateReviewIcon from '@material-ui/icons/RateReview';
import HelpIcon from '@material-ui/icons/Help';
import InfoIcon from '@material-ui/icons/Info';
import {FormattedMessage} from 'react-intl';
import { toast } from 'react-toastify';
import { Tooltip } from '@material-ui/core';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import image from '../../../assets/images/image.png';
import image1 from '../../../assets/images/image1.png';
import image4 from '../../../assets/images/image4.png';
import image12 from '../../../assets/images/image12.png';
import image23 from '../../../assets/images/image23.png';
import image24 from '../../../assets/images/image24.png';
import image6 from '../../../assets/images/image6.png';
import image18 from '../../../assets/images/image18.png';
import image8 from '../../../assets/images/image8.png';
import Modal from '@material-ui/core/Modal';
import '../../../sass/table.scss';
import P2MApplicationInfo from './P2MApplicationInfo';
import OpenInNewIcon from '@material-ui/icons/OpenInNew';

const MarketPlaceInfoApplication = () =>  {
    // let { id } = useParams();
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state => state.d )
    const dispatch = useDispatch()

    const toaster = <FormattedMessage id = "managep2maction.savedsuccessfully"/>;
    const savingMsg = <FormattedMessage id = "managep2m.savingMessage"/>
    const [show, setShow] = useState(p2m.smoothScroll[2] ? 'skus':'main')
    const [baseInfoExpanded, isBaseInfoExpanded] = useState(true);
    const [advancedExpanded, isAdvancedExpanded] = useState(true);
    const [reminder, setReminder] = useState(false);
    const [mppreminder, setMPPReminder] = useState(false);
    const [mainpicturereminder, setMainPictureReminder] = useState(false);
    const [secondarypicturereminder, setSecondarypicturereminder] = useState(false);
    const [descriptionreminder, setDescriptionreminder] = useState(false);
    const [featurereminder, setFeaturereminder] = useState(false);
    const [keywordreminder, setKeywordreminder] = useState(false);

    const [ showMainTitle, setShowMainTitleEditor ] = useState(false)
    const [ showMainImg, setShowMainImgEditor ] = useState(false)
    const [ showMainDescription, setShowMainDescription] = useState(false)
    const [ showMainFeature, setShowMainFeature] = useState(false)
    const [ showMainKeyword, setShowMainKeyword] = useState(false)

    const [ showAdvancedForbiddenWords, setShowAdvancedForbiddenWords ] = useState(false)
    const [ showAdvancedExpectedWeeklySales, setshowAdvancedExpectedWeeklySales ] = useState(false)
    const [ showAdvancedConsumerWarranty, setshowAdvancedConsumerWarranty] = useState(false)
    const [ showAdvancedUseSoftware, setshowAdvancedUseSoftware] = useState(false)
    const [ showAdvancedTradeMarkFile, setshowAdvancedTradeMarkFile] = useState(false)

    const [showCompetitor, setShowCompetitorEditor] = useState(false)
    const [saveTimeout, setSaveTimeout] = useState(false)

    const [showUploader, setShowUploader] = useState(false);
    const [showSkuImgUploader, setShowSkuImgUploader] = useState(false);
    const [showSkuImgIndex, setShowSkuImgIndex] = useState(0)

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
            })
        }
    const onUpload = () => {
        console.log('onUpload')
        if (showUploader){
            return setShowUploader(false)
        }
        setShowUploader(true)
        setShowValidWarning(false)
    }

    const onSkuImgUpload = (index) => {
        console.log('onSkuImgUpload')
        setShowSkuImgIndex(index)
        if (showSkuImgUploader) {
            return setShowSkuImgUploader(false)
        }
        setShowSkuImgUploader(true)
    }

    const handleSubmitImg = (filename) => {
        const targetSubAp = p2m.mp;
        if (filename.length > 0) {
            targetSubAp.main.secondaryImg = filename.map(item => {
                return { name: item.name, preview: item.preview }
            })
        }
        filename.forEach((file) => {
            uploadImg(file)
        })
        dispatch(updateMp(targetSubAp))
        setShowUploader(false)
    }

    const handleSkuImgSubmit = (filename,index) => {
        const targetSubAp = p2m.mp;
        if (filename.length > 0) {
            targetSubAp.appliedSku[index].img = filename.map(item => {
                return { name: item.name, preview: item.preview }
            })
        }
        filename.forEach((file) => {
            uploadImg(file)
        })
        dispatch(updateMp(targetSubAp))
        setShowSkuImgUploader(false)
    }

    const handleUploadAgain = (target) => {

        if(target == 'secondaryImg') {
            setShowUploader(true);
        }
    }

    const handleRemoveAll = (target) => {
        const targetSubAp = p2m.mp;

        if (target == 'secondaryImg') {
            targetSubAp.main.secondaryImg = []
        dispatch(updateMp(targetSubAp))
        }
    }

    const handleSkuImgUploadAgain = (target , index) => {
        if (target == 'skuImg') {
            setShowSkuImgIndex(index);
            setShowSkuImgUploader(true);
        }
    }

    const handleSkuImgRemoveAll = ( target,index) => {
        const targetSubAp = p2m.mp;

        if (target == 'skuImg') {
            targetSubAp.appliedSku[index].img = []
            dispatch(updateMp(targetSubAp))
        }
    }

    const note =<FormattedMessage id="marketplaceinfo.note"/>;

    useEffect(() => {
        const p2mId = p2m.currentAp._id.$oid;

        if (!p2mId) {
            console.log('refresh get')
            // dispatch(getExactApplication(id))
            // dispatch(initP2MMarketplaceInfo(id));
        } else {
            dispatch(initP2MMarketplaceInfo(p2mId));
        }
        if (p2m.smoothScroll[0]) {
            smoothScroll(document.getElementById(p2m.smoothScroll[1]))
        }
    }, []);

   const handleExpand = (param) => {
        switch(param) {
            case 'base':
                return isBaseInfoExpanded(baseInfoExpanded ? false : true)
            case 'advance':
                return isAdvancedExpanded(advancedExpanded ? false : true)
            default:
                return ''
        }
    }

    const handleReminderOpen = () => {
            setReminder(true);
        }

    const handleReminderClose = () => {
            setReminder(false);
        }

    const handleMPPOpen = () => {
            setMPPReminder(true);
        }

    const handleMPPClose = () => {
            setMPPReminder(false);
        }

    const handleMainPictureOpen = () => {
            setMainPictureReminder(true);
        }

    const handleMainPictureClose = () => {
            setMainPictureReminder(false);
        }

    const handleSecondaryPictureOpen = () => {
            setSecondarypicturereminder(true);
        }

    const handleSecondaryPictureClose = () => {
            setSecondarypicturereminder(false);
        }

    const handleDescriptionOpen = () => {
            setDescriptionreminder(true);
        }

    const handleDescriptionClose = () => {
            setDescriptionreminder(false);
        }

    const handleFeatureOpen = () => {
            setFeaturereminder(true);
        }

    const handleFeatureClose = () => {
            setFeaturereminder(false);
        }

    const handleKeywordOpen = () => {
            setKeywordreminder(true);
        }

    const handleKeywordClose = () => {
            setKeywordreminder(false);
        }



    const learnMoreModal = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-10%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleReminderClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>

            <p style={{lineHeight : '1.5rem'}}>
                <p><FormattedMessage id="marketplaceinfo.remindertheme1" /></p>
                <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700', marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.remindertheme2" /></p>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li><FormattedMessage id="marketplaceinfo.remindertheme2-1" /></li>
                    <li><FormattedMessage id="marketplaceinfo.remindertheme2-2" /></li>
                    <li><FormattedMessage id="marketplaceinfo.remindertheme2-3" /></li>
                    <li><FormattedMessage id="marketplaceinfo.remindertheme2-4" /></li>
                </ul>
                <p style={{marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.remindertheme3" /></p>
                <p><FormattedMessage id="marketplaceinfo.remindertheme3-1" /></p>
                <p><FormattedMessage id="marketplaceinfo.remindertheme3-2" /></p>
            </p>
        </div>

        );

    const mpplearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -30%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-4%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleMPPClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
                <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700'}}>產品圖片要求與建議</p>
                <div className="section-line" style={{marginTop: '2%'}}></div>
            <p style={{lineHeight : '1.5rem'}}>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li>須清晰、美觀且傳達足夠資訊。</li>
                    <li>圖片檔案須為 JPEG 格式（.jpg 或 .jpeg）。</li>
                    <li>圖片檔案須小於 100 MB。</li>
                    <li>圖片解析度（長與寬）須至少 500 像素（500 pixel）。</li>
                    <li>若欲使用亞馬遜平台上的放大產品圖片之功能，圖片的解析度（長與寬）至少要有 1000 像素（1000 pixel），已有數據顯示產品具有放大功能可以促進銷售量。</li>
                    <li>圖片須呈現完整的產品，非屬產品的物件不可出現在圖片中。請勿使用任何道具，若道具是必要的（如表達產品的用途），請盡量縮減道具的版面。</li>
                    <li>請盡量減少圖片與實際產品的色差，因為色差可能導致退貨。由於顯示器的不同，每個人看到的圖片顏色多少有些差異。若您的產品在顏色因素上較敏感，建議您在產品文案中加入備註「Due to computer screen variations, the actual colors of products may differ slightly from what you see on your computer.」。</li>
                    <li>產品應占圖片比例 85% 以上，產品於主圖片的比例呈現越大越好，在搜尋頁面較能吸引消費者目光。</li>
                </ul>
                <img src={`/${image4}`} alt="image4" style={{width: '65%', margin: 'auto'}}/>
            </p>
        </div>

    );

    const mainPicturelearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -20%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-3%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleMainPictureClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
                <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700', marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.mainpicturereminder" /></p>
            <p style={{lineHeight : '1.5rem'}}>
                <div className="section-line" style={{marginTop: '2%'}}></div>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li><FormattedMessage id="marketplaceinfo.mainpicturereminder1-1" /></li>
                    <li><FormattedMessage id="marketplaceinfo.mainpicturereminder1-2" /></li>
                    <li><FormattedMessage id="marketplaceinfo.mainpicturereminder1-3" /></li>
                    <li><FormattedMessage id="marketplaceinfo.mainpicturereminder1-4" /></li>
                    <li><FormattedMessage id="marketplaceinfo.mainpicturereminder1-5" /></li>
                </ul>

                <img src={`/${image23}`} alt="image23" style={{width: '80%', margin: 'auto'}}/>
                <img src={`/${image24}`} alt="image24" style={{width: '80%', marginTop:'2%', marginLeft: 'auto', marginRight: 'auto'}}/>
            </p>
        </div>

        );

    const secondaryPicturelearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -20%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-2%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleSecondaryPictureClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
                <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700', marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.secondarypicturereminder1" /></p>
                <p style={{lineHeight : '1.5rem'}}>
                    <div className="section-line" style={{marginTop: '2%'}}></div>
                    <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder1-1" /></li>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder1-2" /></li>
                    </ul>
                </p>
                <p style={{marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.secondarypicturereminder2" /></p>
                <img src={`/${image6}`} alt="image6" style={{width: '80%', marginTop:'2%', marginLeft: 'auto', marginRight: 'auto'}}/>
                <p style={{lineHeight : '1.5rem'}}>
                    <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder2-1" /></li>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder2-2" /></li>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder2-3" /></li>
                    </ul>
                </p>
                <img src={`/${image18}`} alt="image18" style={{width: '80%', marginTop:'2%', marginLeft: 'auto', marginRight: 'auto'}}/>
                <p style={{marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.secondarypicturereminder3" /></p>
                <p style={{lineHeight : '1.5rem'}}>
                    <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder3-1" /></li>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder3-2" /></li>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder3-3" /></li>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder3-4" /></li>
                    </ul>
                </p>
                <p style={{marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.secondarypicturereminder4" /></p>
                <div className="section-line" style={{marginTop: '2%'}}></div>
                <p style={{marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.secondarypicturereminder5" /></p>
                <p style={{marginTop:'3%'}}><FormattedMessage id="marketplaceinfo.secondarypicturereminder5" /></p>
                <p style={{lineHeight : '1.5rem'}}>
                    <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder6-1" /></li>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder6-2" /></li>
                        <li><FormattedMessage id="marketplaceinfo.secondarypicturereminder6-3" /></li>
                    </ul>
                </p>
                <img src={`/${image8}`} alt="image8" style={{width: '80%', marginTop:'2%', marginLeft: 'auto', marginRight: 'auto'}}/>
        </div>

        );

    const descriptionlearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -45%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-4%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleDescriptionClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>

            <p style={{lineHeight : '1.5rem'}}>
                <p>產品敘述（Product Description）延伸敘述產品主要特點內容，提供消費者更深入的細節，幫助他們決定是否購買這個產品。消費者從產品敘述獲得的資訊應該比產品名稱及產品特點更多，且彼此對應、不可衝突，例如：產品特點提及「dual voltage」，產品敘述的電壓即不應只提及「110V」或「240V」，避免混淆。產品敘述可說明產品的使用目的及場合、使用方法及限制，要有吸引力但也要寫實、不誇大。</p>
                <p style={{marginTop:'2%'}}>產品敘述可以使用一些基本的 HTML 語法，最多可寫到兩千個英文字元（含 HTML 語法空格及符號）。若您打算使用 HTML 語法完成產品文稿，請記得您提供給 DRS 的最終版產品敘述應該包含 HTML 語法。</p>
                <div className="section-line" style={{marginTop: '2%'}}></div>
                <p style={{fontSize:'18px', fontWeight : '700', marginTop:'2%'}}>產品敘述的必要內容:</p>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li>描述產品用途及如何運作，及使用產品可帶來的好處、產品如何解決使用者遇到的問題。</li>
                    <li>產品規格，例如：尺寸、重量、材質、電壓等。請注意各個國家習慣使用的單位不同，請依照目標市場的需求撰寫，避免產生距離感或混淆。</li>
                    <li>產品包含之配件及數量，請清楚告知消費者購買品項包含之內容。經常有誤解認為圖片上呈現的內容即是所有購買內容。</li>
                </ul>
                <p style={{fontSize:'18px', fontWeight : '700', marginTop:'2%'}}>產品敘述的禁止內容:</p>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li>請勿加入人名及公司名稱、電子信箱、網站或任何公司相關資訊。</li>
                    <li>請勿描述與產品無關的內容。</li>
                    <li>請勿加入價格、促銷、優惠的訊息，例如「優惠價」或「免運費」。</li>
                </ul>
                <p style={{fontSize:'18px', fontWeight : '700', marginTop:'2%'}}>加分提示:</p>
                <p style={{marginTop:'2%'}}>提供以下內容可幫助您管理消費者對產品的期望，增加信賴感，並可避免消費者買到不如預期的產品，降低退貨率及負面評論。</p>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li>產品安裝及維護資訊。</li>
                    <li>產品保固資訊。（如果產品有提供保固承諾，當產品特色沒有透露保固資訊時，請在產品描述裡提及）</li>
                    <li>加入備註或警告來提醒消費者此產品的使用限制。</li>
                </ul>
                {/* <p style={{marginTop:'2%'}}>加入備註及警告來提醒消費者此產品的使用限制。這幫助您管理消費者對產品的期望，讓他們了解這個產品是否為他們需要的。</p> */}
            </p>
        </div>

        );

    const featurelearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-8%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleFeatureClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>

            <p style={{lineHeight : '2rem'}}>
                <p style={{fontSize:'18px', fontWeight : '700', marginTop:'2%'}}>產品特色的必要內容:</p>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li>請列出五個消費者最需要了解的產品關鍵特色，例如：產品特色、重要規格（例如：通用電壓）及主要用途。</li>
                    <li>產品特色可重申產品標題或產品描述中提及的重點，但請盡量不使用相同句子。</li>
                    <li>每個產品特點以一百個英文字元為限，含空格及符號。</li>
                    <li>要點符號（Bullet Point）後第一個字母要大寫。</li>
                    <li>為了節省字數及直接點出重點，不需使用完整英文語句結構，例如：「This hand warmer can be charged via either USB or AC adapter.」，可以直接寫作「Charges via either USB or AC adapter」。結尾勿加上句末符號，例如：驚嘆號、句號。</li>
                </ul>
                <p style={{fontSize:'18px', fontWeight : '700', marginTop:'2%'}}>產品特色的禁止內容:</p>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li>請勿加入人名及公司名稱、電子信箱、網站或任何公司相關資訊。</li>
                    <li>請勿描述與產品無關的內容。</li>
                    <li>請勿加入價格、促銷、優惠的訊息，例如「優惠價」或「免運費」。</li>
                </ul>
            </p>
        </div>

        );

    const keywordlearnMore = (

        <div style={{top: '50%' , left : '50%' , position : 'absolute' ,
            transform: 'translate(-50%, -50%)', backgroundColor:'#f0f2f5' ,
                borderRadius: '4px', padding : '2%'}}>
                <div className="modal-close-wrapper" style={{position: 'absolute',top: '-8%', left: '0%'}}>
                    <button className="modal-close-btn" onClick={() => {handleKeywordClose()}}>
                    <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                    <span>
                        <FormattedMessage id="addnewproduct.close"/>
                    </span>
                    </button>
                </div>
                <p style={{fontSize:'18px', marginBottom : '12px' , fontWeight : '700', marginTop:'2%'}}>搜索字詞</p>
                <div className="section-line" style={{marginTop: '2%'}}></div>
            <p style={{lineHeight : '1.5rem'}}>
                <p style={{marginTop:'2%'}}>搜尋字詞（Search Terms）不會顯示在銷售頁面，其作用是當消費者在搜尋方塊輸入關鍵字搜尋時，您的產品若有事先設定相同的字詞，將有機會出現於該搜尋結果頁面，精準的搜尋字詞可增加曝光率。</p>
                {/* <p style={{marginTop:'2%'}}>即使您設定了完全相同的字詞，您的產品也有可能不會出現在搜尋頁面上，舉例來說， Amazon 執行一次搜尋只能顯示二十頁的產品。</p>
                <p style={{marginTop:'2%'}}>** 各種銷售平台計算排行的方式不盡相同，但主要有這幾個考量因素：產品評價、點擊率、訂單轉換率（是否導致購買）等。新品上架時，通常這幾個數字都比不過現有的競爭者。因此我們十分推薦新品使用關鍵字贊助廣告（Amazon Sponsored Ads）增加產品出現在第一搜尋頁面的機會。</p> */}
                <p style={{fontWeight : '700', marginTop:'2%'}}>必要內容:</p>
                <ul style={{listStyleType: 'disc' , padding : '30px' , fontWeight : '600'}}>
                    <li>請提供五組英文詞彙或短語來形容產品，每組關鍵字不可超過五十個英文字元，含空格。</li>
                    <li>每個字詞可以使用空格區隔，不需加入逗號。</li>
                    <li>請不要使用非屬於您產品的字詞，例如：競爭者的品牌名稱。</li>
                    <li>當字詞已包含在產品標題、特點與產品敘述中，就不需重複使用同樣的字詞。</li>
                </ul>
            </p>
        </div>

        );

    const handleSave = (status) => {
        setSaveTimeout(true)
        toast(savingMsg)
        const marketplaceInfo = p2m.mp
        marketplaceInfo.p2mApplicationId = p2m.currentAp._id;
        marketplaceInfo.status = status;
        marketplaceInfo.main.secondaryImg.map( img => {img.preview = ''})

        marketplaceInfo.appliedSku.map((item,index) => {
            item.img.map( img => {img.preview = ''})
        })

        const isComment = false;
        // const redirect = isComment ? '': `/product/application/${id}`;
        const redirect = isComment ? "": '/product/application'

        setTimeout(() => {
            setSaveTimeout(false)
            dispatch(saveP2MMarketplaceInfo(redirect,marketplaceInfo,toaster))
        }, 3000)
    }
    const onMainTitleChange = (e) => {
        const targetSubAp = p2m.mp;
        targetSubAp.main.title = e.target.value;
        setShowValidWarning(false)
        dispatch(updateMp(targetSubAp))
    }
    const onMainImgChange = (filename) => {
        const targetSubAp = p2m.mp;
        targetSubAp.main.imgUrl = filename;
        if (filename !== '') {
            setShowValidWarning(false)
        }
        dispatch(updateMp(targetSubAp))
    }
    const onMainDescriptionChange = (e) => {
        const targetSubAp = p2m.mp;
        targetSubAp.main.description = e.target.value
        setShowValidWarning(false)
        dispatch(updateMp(targetSubAp))
    }

    const onMainFeatureChange = (e,index) => {
        const targetSubAp = p2m.mp;
        targetSubAp.main.feature[index] = e.target.value;
        setShowValidWarning(false)
        dispatch(updateMp(targetSubAp))
    }
    const onMainKeywordChange = (e, index) => {
        const targetSubAp = p2m.mp;
        targetSubAp.main.keyword[index] = e.target.value;
        setShowValidWarning(false)
        dispatch(updateMp(targetSubAp))
    }
    const onCompetitorInfoChange = (e) => {
        const targetSubAp = p2m.mp;
        targetSubAp.competitorInfo = e.target.value;
        dispatch(updateMp(targetSubAp))
    }
    const onSkuTitleChange = (e,index) => {
        const targetSubAp = p2m.mp;
        targetSubAp.appliedSku[index].title = e.target.value
        dispatch(updateMp(targetSubAp))
    }
    const onSkuImgChange = (filename,index) => {
        const targetSubAp = p2m.mp;
        targetSubAp.appliedSku[index].mainImgUrl = filename
        dispatch(updateMp(targetSubAp))
    }
    const onSkuVariationNameChange = (e, index) => {
        const targetSubAp = p2m.mp;
        targetSubAp.appliedSku[index].variationNameForMarketplace = e.target.value
        setShowSkuValidWarning(false)
        dispatch(updateMp(targetSubAp))
    }
    const onSkuDescriptionChange = (e,index) => {
        const targetSubAp = p2m.mp;
        targetSubAp.appliedSku[index].description = e.target.value
        dispatch(updateMp(targetSubAp))
    }
    const onSkuFeatureChange = (e, i, index) => {
        const targetSubAp = p2m.mp;
        targetSubAp.appliedSku[index].feature[i] = e.target.value
        dispatch(updateMp(targetSubAp))
    }
    const onSkuKeywordChange = (e, i, index) => {
        const targetSubAp = p2m.mp;
        targetSubAp.appliedSku[index].keyword[i] = e.target.value
        dispatch(updateMp(targetSubAp))
    }
    const onExpandForbiddenWordsChange = (e) => {
        const targetSubAp = p2m.mp;
        targetSubAp.advanced.forbiddenWords = e.target.value;
        dispatch(updateMp(targetSubAp))
    }
    const onExpandExpectedWeeklySalesChange = (e) => {
        const targetSubAp = p2m.mp;
        targetSubAp.advanced.expectedWeeklySales = e.target.value;
        setShowExpandValidWarning(false)
        dispatch(updateMp(targetSubAp))
    }
    const onExpandConsumerWarrantyChange = (e) => {
        const targetSubAp = p2m.mp;
        targetSubAp.advanced.consumerWarranty = e.target.value;
        dispatch(updateMp(targetSubAp))
    }
    const onExpandUseSoftwareChange = (e) => {
        const targetSubAp = p2m.mp;
        targetSubAp.advanced.useSoftware = e.target.value;
        dispatch(updateMp(targetSubAp))
    }
    const onExpandTradeMarkFileChange = (e) => {
        const targetSubAp = p2m.mp;
        targetSubAp.advanced.tradeMarkFile = e.target.value;
        dispatch(updateMp(targetSubAp))
    }

    const handleCommentChange = (content, index, target) => {
        const targetSubAp = p2m.mp;
        if (target == 'competitorInfo') {
            targetSubAp.comment.competitorInfo = content
        } else if (target == 'mainTitle') {
            targetSubAp.main.comment.title = content
        } else if (target == 'skuMainTitle') {
            targetSubAp.appliedSku[index].comment.title = content
        } else if (target == 'mainDescription') {
            targetSubAp.main.comment.description = content
        } else if (target == 'skuDescription') {
            targetSubAp.appliedSku[index].comment.description = content
        } else if (target == 'mainImg') {
            targetSubAp.main.comment.img = content
        } else if (target == 'skuVariationName') {
            marketplaceInfo.appliedSku[index].comment.variationNameForMarketplace = content
        } else if (target == 'skuImg') {
            targetSubAp.appliedSku[index].comment.img = content
        } else if (target == 'mainFeature') {
            targetSubAp.main.comment.feature = content
        } else if (target == 'skuFeature') {
            targetSubAp.appliedSku[index].comment.feature = content
        } else if (target == 'mainKeyword') {
            targetSubAp.main.comment.keyword = content
        } else if (target == 'skuKeyword') {
            targetSubAp.appliedSku[index].comment.keyword = content
        } else if (target == 'advancedForbiddenWords') {
            targetSubAp.advanced.comment.forbiddenWords = content
        } else if (target == 'advancedExpectedWeeklySales') {
            targetSubAp.advanced.comment.expectedWeeklySales = content
        } else if (target == 'advancedConsumerWarranty') {
            targetSubAp.advanced.comment.consumerWarranty = content
        } else if (target == 'advancedUseSoftware') {
            targetSubAp.advanced.comment.useSoftware = content
        } else if (target == 'advancedTradeMarkFile') {
            targetSubAp.advanced.comment.tradeMarkFile = content
        }
        dispatch(updateMp(targetSubAp));
    }

    const handleSaveComment = (content, index, target) => {
        const marketplaceInfo = p2m.mp
        marketplaceInfo.p2mApplicationId = p2m.currentAp._id;
        const isComment = true;
        if (target == 'competitorInfo') {
            marketplaceInfo.comment.competitorInfo = content
        } else if (target == 'mainTitle') {
            marketplaceInfo.main.comment.title = content
        } else if (target == 'skuMainTitle') {
            marketplaceInfo.appliedSku[index].comment.title = content
        } else if (target == 'mainDescription') {
            marketplaceInfo.main.comment.description = content
        } else if (target == 'skuDescription') {
            marketplaceInfo.appliedSku[index].comment.description = content
        } else if (target == 'mainImg') {
            marketplaceInfo.main.comment.img = content
        } else if (target == 'skuVariationName') {
            marketplaceInfo.appliedSku[index].comment.variationNameForMarketplace = content
        } else if (target == 'skuImg') {
            marketplaceInfo.appliedSku[index].comment.img = content
        } else if (target == 'mainFeature') {
            marketplaceInfo.main.comment.feature = content
        } else if (target == 'skuFeature') {
            marketplaceInfo.appliedSku[index].comment.feature = content
        } else if (target == 'mainKeyword') {
            marketplaceInfo.main.comment.keyword = content
        } else if (target == 'skuKeyword') {
            marketplaceInfo.appliedSku[index].comment.keyword = content
        } else if (target == 'advancedForbiddenWords') {
            marketplaceInfo.advanced.comment.forbiddenWords = content
        } else if (target == 'advancedExpectedWeeklySales') {
            marketplaceInfo.advanced.comment.expectedWeeklySales = content
        } else if (target == 'advancedConsumerWarranty') {
            marketplaceInfo.advanced.comment.consumerWarranty = content
        } else if (target == 'advancedUseSoftware') {
            marketplaceInfo.advanced.comment.useSoftware = content
        } else if (target == 'advancedTradeMarkFile') {
            marketplaceInfo.advanced.comment.tradeMarkFile = content
        }
        // const redirect = isComment ? '': `/product/application/${id}`;
        const redirect = isComment ? '': '/product/application'

        dispatch(saveP2MMarketplaceInfo(redirect,marketplaceInfo,toaster));
    }

    const handleShowEditor = (target,index) => {
        // console.log('handle show editor', target, index);
        let arr = []
        switch(target) {
            case 'competitorInfo':
                return showCompetitor ? setShowCompetitorEditor(false) : setShowCompetitorEditor(true)
            case 'mainTitle':
                return showMainTitle ? setShowMainTitleEditor(false) : setShowMainTitleEditor(true)
            case 'mainImg':
                return showMainImg ? setShowMainImgEditor(false): setShowMainImgEditor(true)
            case 'mainDescription':
                return showMainDescription ? setShowMainDescription(false): setShowMainDescription(true)
            case 'mainFeature':
                return showMainFeature ? setShowMainFeature(false): setShowMainFeature(true)
            case 'mainKeyword':
                return showMainKeyword ? setShowMainKeyword(false): setShowMainKeyword(true)
            case 'advancedForbiddenWords':
                return showAdvancedForbiddenWords ? setShowAdvancedForbiddenWords(false) : setShowAdvancedForbiddenWords(true)
            case 'advancedExpectedWeeklySales':
                return showAdvancedExpectedWeeklySales ? setshowAdvancedExpectedWeeklySales(false): setshowAdvancedExpectedWeeklySales(true)
            case 'advancedConsumerWarranty':
                return showAdvancedConsumerWarranty ? setshowAdvancedConsumerWarranty(false): setshowAdvancedConsumerWarranty(true)
            case 'advancedUseSoftware':
                return showAdvancedUseSoftware ? setshowAdvancedUseSoftware(false): setshowAdvancedUseSoftware(true)
            case 'advancedTradeMarkFile':
                return showAdvancedTradeMarkFile ? setshowAdvancedTradeMarkFile(false): setshowAdvancedTradeMarkFile(true)
            case 'skuVariationName':
                arr = [...p2m.showSkuVariationName];
                arr[index] ? arr[index] = false : arr[index] = true
                return dispatch(changeMpSkuVariationNameShow(arr))
            case 'skuImg':
                arr = [...p2m.showSkuImg];
                arr[index] ? arr[index] = false : arr[index] = true
                return dispatch(changeMpSkuImgShow(arr))
            case 'skuMainTitle':
                arr = [...p2m.showSkuMainTitle];
                arr[index] ? arr[index] = false : arr[index] = true
                return dispatch(changeMpSkuMainTitleShow(arr))
            case 'skuDescription':
                arr = [...p2m.showSkuDescription];
                arr[index] ? arr[index] = false: arr[index] = true
                return dispatch(changeMpSkuDesShow(arr))
            case 'skuFeature':
                arr = [...p2m.showSkuFeature];
                arr[index] ? arr[index] = false : arr[index] = true
                return dispatch(changeMpSkuFeatureShow(arr))
            case 'skuKeyword':
                arr = [...p2m.showSkuKeyword];
                arr[index] ? arr[index] = false : arr[index] = true
                return dispatch(changeMpSkuKeywordShow(arr))
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
    const [showSkuValidWarning, setShowSkuValidWarning] = useState(false)
    const [showExpandValidWarning, setShowExpandValidWarning] = useState(false)

    const handleValid = () =>　{

        let checkSkuValid = []
        let checkValid = []
        let checkExpandValid = []
        p2m.mp.appliedSku.map(item => {
            item.variationNameForMarketplace == '' ? checkSkuValid.push(false): null
        })
//        for (let index = 0; index < p2m.mp.appliedSku.length; index++) {
//            if(p2m.mp.appliedSku[index].variationNameForMarketplace == ''){
//                checkSkuValid.push(false)
//            }
//        }
        if ( p2m.mp.competitorInfo == '' || p2m.mp.main.imgUrl == '' || p2m.mp.main.secondaryImg.length == 0 || p2m.mp.main.title == '' || p2m.mp.main.description == '' || p2m.mp.main.feature[0] == '' || p2m.mp.main.feature[1] == '' || p2m.mp.main.feature[2] == '' || p2m.mp.main.feature[3] == '' || p2m.mp.main.feature[4] == '' || p2m.mp.main.keyword[0] == '' || p2m.mp.main.keyword[1] == '' || p2m.mp.main.keyword[2] == '' || p2m.mp.main.keyword[3] == '' || p2m.mp.main.keyword[4] == ''){
            checkValid.push(false)
        }
        if ( p2m.mp.advanced.expectedWeeklySales == ''){
            checkExpandValid.push(false)
        }


        if ( checkValid.length !== 0){
            setShowValidWarning(true)
        }
        if ( checkSkuValid.length !== 0){
            setShowSkuValidWarning(true)
        }
        if ( checkExpandValid.length !== 0){
            setShowExpandValidWarning(true)
        }
        if ((checkValid.length == 0)&&(checkSkuValid.length == 0)&&(checkExpandValid.length == 0)) {
            setShowValidWarning(false)
            setShowSkuValidWarning(false)
            setShowExpandValidWarning(false)
            handleSave("Pending")
        }
    }

    if (p2m.redirect !== '') {
        return <Redirect to={p2m.redirect}/>
    }

    return(
        <div>
            { p2m.isPending || saveTimeout
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
                            {p2m.currentAp.name}-<FormattedMessage id="subApplication.marketplaceinfoTitle"/>
                        </p>
                        <div className="application-subtitle-info-wrapper">
                            <div className="application-info-column" style={{width: '69%'}}>
                                <div className="application-info">
                                    <p className="application-info-title" style={{width: '30%'}}>
                                        <FormattedMessage id="p2mapplication.formtype"/>
                                    </p>
                                    <p className="application-info-content">
                                        <FormattedMessage id="subApplication.marketplaceinfo"/>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="section-line"></div>
                    <P2MApplicationInfo/>
                    <div className="section-line"></div>
                    <div className="base-reminder-wrapper" style={{marginTop:'2%'}}>
                        <p className="base-reminder" style={{width: '100%'}}>
                        <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                            <FormattedMessage id ="marketplaceinfo.reminder"></FormattedMessage>
                            <p style={{marginTop:'2%'}}><FormattedMessage id ="marketplaceinfo.reminder2"></FormattedMessage></p>
                        <span style={{color: '#51cbce' , cursor : 'pointer'  ,marginTop: '1%'}} onClick={()=>{handleReminderOpen()}}>
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


                    <div className="application-info-section-wrapper">
                        <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                            <div className="application-info-section-expand-title">
                                <FormattedMessage id="marketplaceinfo.competingproduct"/>
                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                    *
                                </span>
                                { p2m.mp.competitorInfo == ''
                                    ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                            <FormattedMessage id="inputField.required"/>
                                        </p>
                                    :   null
                                }
                            </div>
                            { d.u.isSp
                                ? null
                                : p2m.currentAp.status == p2m.stMap.get(1)
                                    ? <button onClick={() =>{
                                        handleShowEditor('competitorInfo');
                                        showCompetitor ? null : smoothScroll(document.getElementById('competitor'))
                                    }}><RateReviewIcon/></button>
                                    : null
                            }
                        </div>
                        <div className="application-info-row" style={{marginBottom: '0px'}}>
                            <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                <FormattedMessage id="marketplaceinfo.paragraph1"/>
                            </div>

                            <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                <FormattedMessage  id="marketplaceinfo.paragraph1">
                                    { placeholder =>
                                        <textarea
                                            id="competitor"
                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                            name="competitor"
                                            rows="4"
                                            onChange={(e)=> onCompetitorInfoChange(e)}
                                            value={p2m.mp.competitorInfo}
                                            placeHolder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder}
                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                        >
                                        </textarea>
                                    }
                                    </FormattedMessage>
                                </div>
                                <div id="competitor">
                                    <Comment
                                        comment={p2m.mp.comment.competitorInfo}
                                        handleSaveComment={handleSaveComment}
                                        handleCommentChange={handleCommentChange}
                                        edit={showCompetitor}
                                        handleShow={handleShowEditor}
                                        target='competitorInfo'
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="section-line"></div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-expand-title" onClick={()=> { handleExpand('base') }}>
                            <FormattedMessage id="marketplaceinfo.basicitems"/>
                            <span style={{marginLeft: '6px'}}> {baseInfoExpanded ? <img src={`/${expandDownIcon}`} alt="expandIcon"/> : <img src={`/${expandIcon}`} alt="expandIcon"/>}</span>
                        </p>
                        { baseInfoExpanded
                            ?   <div>
                                    <p className="application-intro-paragraph">
                                        <FormattedMessage id="marketplaceinfo.paragraph2-1"/>
                                    </p>
                                    <div className="application-img-wrapper" style={{width: '75%',margin:'2% auto'}}>
                                        <img src={`/${image}`} alt='image'/>
                                    </div>
                                    <div style={{marginLeft:'25%'}}>
                                        <p style={{marginTop:'2%'}}><FormattedMessage id="marketplaceinfo.subtitle1" /></p>
                                        <p style={{marginTop:'1%'}}><FormattedMessage id="marketplaceinfo.subtitle2" /></p>
                                        <p style={{marginTop:'1%'}}><FormattedMessage id="marketplaceinfo.subtitle3" /></p>
                                        <p style={{marginTop:'1%'}}><FormattedMessage id="marketplaceinfo.subtitle4" /></p>
                                        <p style={{marginTop:'1%'}}><FormattedMessage id="marketplaceinfo.subtitle5" /></p>
                                        <p style={{marginTop:'1%'}}><FormattedMessage id="marketplaceinfo.subtitle6" /></p>
                                        <p style={{marginTop:'1%'}}><FormattedMessage id="marketplaceinfo.subtitle7" /></p>
                                        <p style={{marginTop:'1%'}}><FormattedMessage id="marketplaceinfo.subtitle8" /></p>
                                        <p style={{marginTop:'1%'}}><FormattedMessage id="marketplaceinfo.subtitle9" /></p>
                                    </div>
                                    <p className="application-intro-paragraph" style={{marginTop:'2%'}}>
                                        <FormattedMessage id="marketplaceinfo.paragraph2-2"/>
                                    </p>

                                </div>
                            :   null
                        }
                        { baseInfoExpanded
                            ?   <div className="section-line" style={{margin: '2% 0'}}></div>
                            :   null
                        }
                        { baseInfoExpanded
                            ?   <div className="application-tab-wrapper">
                                    <div className={`application-tab ${show == 'main' ? 'application-tab-active':''}`}>
                                        <span onClick={() => {setShow('main')}}>
                                            <FormattedMessage id="marketplaceinfo.sharedata"/>
                                        </span>
                                    </div>
                                    <div className={`application-tab ${show == 'skus' ? 'application-tab-active':''}`}>
                                        <span onClick={() => { setShow('skus')}}>
                                            <FormattedMessage id="marketplaceinfo.skurespectiveinformation"/>
                                        </span>
                                    </div>
                                </div>
                            :   null
                        }
                        { baseInfoExpanded
                            ?   <div className="application-tab-content-wrapper">
                                { show == 'main'
                                    ?  <div className="application-info-section column" style={{margin: '0px', borderRadius: '16px'}}>
                                            <div style={{marginTop: '2%'}}></div>
                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                <div className="application-intro-title" style={{display: 'flex',fontWeight:'600',fontSize:'19px'}}>
                                                    <span><FormattedMessage id="marketplaceinfo.mainproductpicture"/></span>
                                                    <span style={{color: '#0B3B3D', cursor : 'pointer', marginLeft: '1%', fontSize: '15px'}} onClick={()=>{handleMPPOpen()}}>
                                                        <HelpIcon/>
                                                        <span>如何填寫</span>
                                                    </span>
                                                    {/* <span style={{color: '#51cbce' , cursor : 'pointer' ,marginLeft: '1%'}} onClick={()=>{handleMPPOpen()}}>
                                                        <InfoIcon/>
                                                        <FormattedMessage id="basedata.instruction"></FormattedMessage>
                                                    </span> */}
                                                </div>
                                                <Modal
                                                    open={mppreminder}
                                                    onClose={handleMainPictureClose}
                                                    aria-labelledby="simple-modal-title"
                                                    aria-describedby="simple-modal-description"
                                                    style={{overflow: 'scroll'}}
                                                >
                                                  {mpplearnMore}
                                                </Modal>
                                                { d.u.isSp
                                                    ? null
                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                        ? <button onClick={() => {
                                                            handleShowEditor('mainImg');
                                                            showMainImg ? null : smoothScroll(document.getElementById('mainImg'))
                                                        }}><RateReviewIcon/></button>
                                                        : null
                                                }
                                            </div>
                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                <div style={{marginTop: '2%'}}>
                                                    <FormattedMessage id="marketplaceinfo.mainpicture"/>
                                                    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                        *
                                                    </span>
                                                    { p2m.mp.main.imgUrl == ''
                                                        ?   <span className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="picture.required"/>
                                                            </span>
                                                        :   null
                                                    }
                                                </div>
                                                <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                    <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                                        <span><FormattedMessage id="marketplaceinfo.paragraph4"/></span>
                                                        <span style={{color: '#51cbce' , cursor : 'pointer' ,marginLeft: '1%'}}
                                                            onClick={()=>{handleMainPictureOpen()}}>
                                                            {/* <FormattedMessage id="basedata.learnmore"></FormattedMessage> */}
                                                            {/* <i class="fa fa-question-circle"></i> */}
                                                            <InfoIcon/>
                                                            <FormattedMessage id="basedata.instruction"></FormattedMessage>
                                                        </span>
                                                    </div>
                                                </div>

                                                <Modal
                                                    open={mainpicturereminder}
                                                    onClose={handleMainPictureClose}
                                                    aria-labelledby="simple-modal-title"
                                                    aria-describedby="simple-modal-description"
                                                    style={{overflow: 'scroll'}}
                                                >
                                                  {mainPicturelearnMore}
                                                </Modal>
                                                <div style={{marginTop: '1%'}}>
                                                    <Dropzone
                                                        id='main'
                                                        imgUrl={p2m.mp.main.imgUrl == undefined ? '': p2m.mp.main.imgUrl}
                                                        p2mName={p2m.currentAp.name}
                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                        onChange={onMainImgChange}
                                                    />
                                                </div>
                                            </div>
                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                <div style={{marginTop: '2%'}}>
                                                    <div style={{width: '100%', display: 'flex', alignItems: 'center', justifyContent: 'space-between'}}>
                                                        <p style={{width: '8%'}}><FormattedMessage id="marketplaceinfo.secondarypicture"/>
                                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                *
                                                            </span>
                                                        </p>
                                                        { p2m.mp.main.secondaryImg.length == 0 || p2m.currentAp.status !== p2m.stMap.get(0)
                                                            ?   <p className="drs-blue" style={{width: '130%', fontSize: '13px'}}>
                                                                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                    <FormattedMessage id="picture.required"/>
                                                                </p>
                                                            :   <div style={{display: 'flex', alignItems: 'center'}}>
                                                                    <button
                                                                        className="drs-btn drs-btn-normal"
                                                                        onClick={() => handleUploadAgain('secondaryImg') }>
                                                                        <span style={{marginLeft:'6px'}}>重新上傳</span>
                                                                    </button>
                                                                    <button
                                                                        className="drs-btn drs-btn-normal"
                                                                        onClick={() => handleRemoveAll( 'secondaryImg') }>
                                                                        <span style={{marginLeft:'6px'}}>清空全部</span>
                                                                    </button>
                                                                </div>
                                                        }
                                                    </div>
                                                </div>
                                                <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                    <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                                        <span><FormattedMessage id="marketplaceinfo.paragraph5"/></span>

                                                        <span style={{color: '#51cbce' , cursor : 'pointer' ,marginLeft: '1%'}}
                                                            onClick={()=>{handleSecondaryPictureOpen()}}>
                                                            {/* <FormattedMessage id ="basedata.learnmore"></FormattedMessage> */}
                                                            {/* <i class="fa fa-question-circle"></i> */}
                                                            <InfoIcon/>
                                                            <FormattedMessage id="basedata.instruction"></FormattedMessage>
                                                        </span>
                                                    </div>
                                                </div>
                                                <Modal
                                                    open={secondarypicturereminder}
                                                    onClose={handleSecondaryPictureClose}
                                                    aria-labelledby="simple-modal-title"
                                                    aria-describedby="simple-modal-description"
                                                    style={{overflow: 'scroll'}}
                                                >
                                                  {secondaryPicturelearnMore}
                                                </Modal>
                                                { showUploader
                                                    ? <MultiImgUploader
                                                            onClose={setShowUploader}
                                                            onSubmit={handleSubmitImg}
                                                        />
                                                    : null
                                                }
                                                <div>
                                                    { p2m.mp.main.secondaryImg.length == 0
                                                        ?   <div className="upload-img-area">
                                                                <button
                                                                    className={`upload-img-btn ${!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0) ? 'disabled': ''}`}
                                                                    onClick={() => { onUpload()}}
                                                                    disabled={!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                >
                                                                    <InsertPhotoIcon fontSize="medium"/>
                                                                    <span style={{marginLeft:'6px'}}>
                                                                        <FormattedMessage id="productinfo.uploadimage"/>
                                                                    </span>
                                                                </button>
                                                            </div>
                                                        :   <div className="upload-img-preview-wrapper">
                                                                { p2m.mp.main.secondaryImg.map((file) => (
                                                                    <div className="upload-img-preview-column">
                                                                        {file.preview == '' ?
                                                                         <div className="upload-img-preview"><img src={`${DOMAIN_NAME}/p2m/mp/i/${p2m.currentAp.name}/${file.name}`}></img></div>
                                                                            : <div className="upload-img-preview"><img src={file.preview}></img></div>
                                                                        }
                                                                        <a href={`${DOMAIN_NAME}/p2m/mp/i/${p2m.currentAp.name}/${file.name}`} download={file.name}>
                                                                            <p style={{fontSize: '14px', margin: '8px 0'}}>{file.name}</p>
                                                                        </a>
                                                                    </div>
                                                                ))}
                                                            </div>
                                                    }
                                                </div>
                                            </div>
                                            <div id="mainImg">
                                                <Comment
                                                    comment={p2m.mp.main.comment.img}
                                                    handleSaveComment={handleSaveComment}
                                                    handleCommentChange={handleCommentChange}
                                                    edit={showMainImg}
                                                    handleShow={handleShowEditor}
                                                    target='mainImg'
                                                />
                                            </div>

                                            <div className="section-line" style={{margin: '2% 0'}}></div>

                                            <div style={{marginTop:'2%',width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                <div className="application-intro-title" style={{display: 'flex',fontWeight:'600',fontSize:'19px'}}>
                                                    <FormattedMessage id="marketplaceinfo.pagescript"/>
                                                </div>
                                            </div>
                                            <p className="application-intro-paragraph" style={{marginTop:'2%',lineHeight : '1.5rem'}}>
                                                <FormattedMessage id="marketplaceinfo.paragraph3"/>
                                            </p>

                                            <div className="section-line" style={{margin: '2% 0'}}></div>

                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                <div className="application-intro-title" style={{display: 'flex'}}>
                                                    <FormattedMessage id="marketplaceinfo.maintitle"/>
                                                    <span className="drs-notice-red" style={{marginLeft: '5px' ,marginRight: '1%'}}>
                                                        *
                                                    </span>
                                                    { p2m.mp.main.title == ''
                                                        ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="inputField.required"/>
                                                            </p>
                                                        :   null
                                                    }
                                                </div>
                                                { d.u.isSp
                                                    ? null
                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                        ?   <button onClick={() => {
                                                                handleShowEditor('mainTitle');
                                                                showMainTitle ? null : smoothScroll(document.getElementById('mainTitle'))
                                                            }}>
                                                                <RateReviewIcon/>
                                                            </button>
                                                        : null
                                                }
                                            </div>
                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                                    <FormattedMessage id="marketplaceinfo.paragraph7"/>
                                                    <Link to="/p2mExplanation/miExplanation" target="_blank">
                                                        <FormattedMessage id="basedata.learnmore"/><OpenInNewIcon/>
                                                    </Link>
                                                </div>
                                                <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                                    <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                        <FormattedMessage id="marketplaceinfo.paragraph13">
                                                            { placeholder=>
                                                                <textarea
                                                                    id="main-title"
                                                                    className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                    name="main-title"
                                                                    rows="8"
                                                                    onChange={(e)=> onMainTitleChange(e)}
                                                                    // onBlur={(e) => onMainTitleBlur(e)}
                                                                    placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                                    value= {p2m.mp.main.title}
                                                                    readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                >
                                                                </textarea>
                                                            }
                                                        </FormattedMessage>
                                                    </div>
                                                    <div id="mainTitle">
                                                        <Comment
                                                            comment={p2m.mp.main.comment.title}
                                                            handleSaveComment={handleSaveComment}
                                                            handleCommentChange={handleCommentChange}
                                                            edit={showMainTitle}
                                                            handleShow={handleShowEditor}
                                                            target='mainTitle'
                                                        />
                                                    </div>

                                                </div>
                                            </div>
                                            <div className="section-line" style={{margin: '2% 0'}}></div>
                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                <div className="application-intro-title" style={{display: 'flex'}}>
                                                    <FormattedMessage id="marketplaceinfo.mainproductdescription"/>
                                                    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                        *
                                                    </span>
                                                    { p2m.mp.main.description == ''
                                                        ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="inputField.required"/>
                                                            </p>
                                                        :   null
                                                    }
                                                </div>
                                                { d.u.isSp
                                                    ? null
                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                        ? <button onClick={() =>{
                                                                handleShowEditor('mainDescription');
                                                                showMainDescription ? null : smoothScroll(document.getElementById('mainDescription'))
                                                             }}>
                                                            <RateReviewIcon/></button>
                                                        : null
                                                }
                                            </div>
                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                                    <span><FormattedMessage id="marketplaceinfo.paragraph8"/></span>
                                                    <span style={{color: '#51cbce' , cursor : 'pointer' ,marginLeft: '1%',display: 'flex', alignItems: 'center'}}
                                                        onClick={()=>{handleDescriptionOpen()}}>
                                                        {/* <FormattedMessage id ="basedata.learnmore"></FormattedMessage> */}
                                                        {/* <i class="fa fa-question-circle"></i> */}
                                                        <InfoIcon/>
                                                        <FormattedMessage id="basedata.instruction"></FormattedMessage>
                                                    </span>
                                                </div>

                                                <Modal
                                                    open={descriptionreminder}
                                                    onClose={handleDescriptionClose}
                                                    aria-labelledby="simple-modal-title"
                                                    aria-describedby="simple-modal-description"
                                                    style={{overflow: 'scroll'}}
                                                >
                                                  {descriptionlearnMore}
                                                </Modal>
                                                <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                                    <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                        <FormattedMessage id="marketplaceinfo.paragraph16">
                                                            { placeholder=>
                                                                <textarea
                                                                    id="main-product-description"
                                                                    className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                    name="main-product-description"
                                                                    rows="8"
                                                                    onChange={(e)=> onMainDescriptionChange(e)}
                                                                    // onBlur={(e) => onMainDescriptionBlur(e)}
                                                                    placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                                    value= {p2m.mp.main.description}
                                                                    readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                >
                                                                </textarea>
                                                            }
                                                        </FormattedMessage>
                                                    </div>
                                                    <div id="mainDescription">
                                                        <Comment
                                                            comment={p2m.mp.main.comment.description}
                                                            handleSaveComment={handleSaveComment}
                                                            handleCommentChange={handleCommentChange}
                                                            edit={showMainDescription}
                                                            handleShow={handleShowEditor}
                                                            target='mainDescription'
                                                        />
                                                    </div>
                                                </div>
                                            </div>
                                            <div className="section-line" style={{margin: '2% 0'}}></div>
                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                <div className="application-intro-title" style={{display: 'flex'}}>
                                                    <FormattedMessage id="marketplaceinfo.mainproductfeatures"/>
                                                    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                        *
                                                    </span>
                                                    { p2m.mp.main.feature[0] == '' || p2m.mp.main.feature[1] == '' || p2m.mp.main.feature[2] == '' || p2m.mp.main.feature[3] == '' || p2m.mp.main.feature[4] == ''
                                                        ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="inputField.required"/>
                                                            </p>
                                                        :   null
                                                    }
                                                </div>
                                                { d.u.isSp
                                                    ? null
                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                        ?   <button onClick={() => {
                                                                handleShowEditor('mainFeature');
                                                                showMainFeature ? null : smoothScroll(document.getElementById('mainFeature'))
                                                            }}>
                                                            <RateReviewIcon/>
                                                            </button>
                                                        : null
                                                }
                                            </div>

                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                                    <span><FormattedMessage id="marketplaceinfo.paragraph9"/></span>
                                                    <span style={{color: '#51cbce' , cursor : 'pointer' ,marginLeft: '1%',display: 'flex', alignItems: 'center'}}
                                                        onClick={()=>{handleFeatureOpen()}}>
                                                        {/* <FormattedMessage id ="basedata.learnmore"></FormattedMessage> */}
                                                        {/* <i class="fa fa-question-circle"></i> */}
                                                        <InfoIcon/>
                                                        <FormattedMessage id="basedata.instruction"></FormattedMessage>
                                                    </span>
                                                </div>

                                                <Modal
                                                    open={featurereminder}
                                                    onClose={handleFeatureClose}
                                                    aria-labelledby="simple-modal-title"
                                                    aria-describedby="simple-modal-description"
                                                    style={{overflow: 'scroll'}}
                                                >
                                                  {featurelearnMore}
                                                </Modal>

                                                <ul className="input-list">
                                                    { p2m.mp.main.feature.map((item,index) => {
                                                        return (
                                                            <li className="input-list-li" style={{alignItems: 'flex-start'}}>
                                                                <p style={{margin: '11px 8px 0 0'}}>{`${index + 1}.`} </p>
                                                                <FormattedMessage id="marketplaceinfo.paragraph14">
                                                                    { placeholder=>
                                                                        <textarea
                                                                            id={`main-product-feature-0${index + 1}`}
                                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                            name="main-product-feature"
                                                                            rows="3"
                                                                            onChange={(e)=> onMainFeatureChange(e,index)}
                                                                            // onBlur={(e) => onMainFeatureBlur(e,index)}
                                                                            placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                                            defaultValue={item}
                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0) ? true : false}
                                                                        >
                                                                        </textarea>
                                                                    }
                                                                </FormattedMessage>
                                                            </li>
                                                        )
                                                    })}
                                                </ul>
                                                <div id="mainFeature">
                                                    <Comment
                                                        comment={p2m.mp.main.comment.feature}
                                                        handleSaveComment={handleSaveComment}
                                                        handleCommentChange={handleCommentChange}
                                                        edit={showMainFeature}
                                                        handleShow={handleShowEditor}
                                                        target='mainFeature'
                                                    />
                                                </div>
                                            </div>
                                            <div className="section-line" style={{margin: '2% 0'}}></div>
                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                <div className="application-intro-title" style={{display: 'flex'}}>
                                                    <FormattedMessage id="marketplaceinfo.mainkeywords"/>
                                                    <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                        *
                                                    </span>
                                                    { p2m.mp.main.keyword[0] == '' || p2m.mp.main.keyword[1] == '' || p2m.mp.main.keyword[2] == '' || p2m.mp.main.keyword[3] == '' || p2m.mp.main.keyword[4] == ''
                                                        ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                <FormattedMessage id="inputField.required"/>
                                                            </p>
                                                        :   null
                                                    }
                                                </div>
                                                { d.u.isSp
                                                    ? null
                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                        ? <button onClick={() => {
                                                            handleShowEditor('mainKeyword');
                                                            showMainKeyword ? null : smoothScroll(document.getElementById('mainKeyword'))
                                                        }}><RateReviewIcon/></button>: null
                                                }
                                            </div>
                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                    <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                                        <span><FormattedMessage id="marketplaceinfo.paragraph10"/></span>

                                                        <span style={{color: '#51cbce' , cursor : 'pointer' ,marginLeft: '1%',display: 'flex', alignItems: 'center'}}
                                                            onClick={()=>{handleKeywordOpen()}}>
                                                            {/* <FormattedMessage id ="basedata.learnmore"></FormattedMessage> */}
                                                            {/* <i class="fa fa-question-circle"></i> */}
                                                            <InfoIcon/>
                                                            <FormattedMessage id="basedata.instruction"></FormattedMessage>
                                                        </span>
                                                    </div>

                                                    <Modal
                                                        open={keywordreminder}
                                                        onClose={handleKeywordClose}
                                                        aria-labelledby="simple-modal-title"
                                                        aria-describedby="simple-modal-description"
                                                        style={{overflow: 'scroll'}}
                                                    >
                                                      {keywordlearnMore}
                                                    </Modal>
                                                    <ul className="input-list">
                                                        { p2m.mp.main.keyword.map((item,index) => {
                                                            return (
                                                                <li className="input-list-li">
                                                                    <p style={{marginRight: '10px'}}>{`${index + 1}.`} </p>
                                                                    {/* <input
                                                                        className="base-input"
                                                                        placeholder="keywords"
                                                                        value={item}
                                                                        onBlur={(e) => onMainKeywordBlur(e,index)}
                                                                        onChange={(e) => onMainKeywordChange(e,index)}
                                                                    /> */}
                                                                    <FormattedMessage id="marketplaceinfo.paragraph15">
                                                                        { placeholder=>
                                                                            <textarea
                                                                                id={`main-product-keyword-0${index + 1}`}
                                                                                className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                                name="main-product-keyword"
                                                                                rows="1"
                                                                                onChange={(e) => onMainKeywordChange(e,index)}
                                                                                // onBlur={(e) => onMainKeywordBlur(e,index)}
                                                                                placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                                                defaultValue={item}
                                                                                readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true : false}
                                                                            >
                                                                            </textarea>
                                                                        }
                                                                    </FormattedMessage>
                                                                </li>
                                                            )
                                                        })}
                                                    </ul>
                                                    <div id="mainKeyword">
                                                        <Comment
                                                            comment={p2m.mp.main.comment.keyword}
                                                            handleSaveComment={handleSaveComment}
                                                            handleCommentChange={handleCommentChange}
                                                            edit={showMainKeyword}
                                                            handleShow={handleShowEditor}
                                                            target='mainKeyword'
                                                        />
                                                    </div>
                                                </div>
                                        </div>
                                    :
                                        <div className="application-info-section column" style={{margin: '0px', borderRadius: '16px'}}>
                                            <div style={{marginTop: '2%'}}></div>
                                            <p className="application-intro-title" style={{width: '100%', padding: '0 1%'}}>
                                                <FormattedMessage id="marketplaceinfo.productskulist"/>
                                            </p>
                                            <p className="application-intro-paragraph" style={{width: '100%', padding: '0 1%'}}>
                                                <p><FormattedMessage id="marketplaceinfo.paragraph11"/></p>
                                                {/* <p style={{marginTop:'2%'}}>變體名稱（Variation Name）是顯示在縮圖之上、針對 SKU 產品的名稱。消費者把產品放入購物車後，變體名稱也會顯示在該 SKU 產品標題後面。如您使用亞馬遜的條碼標籤，變體名稱也會顯示在標籤上。請填寫市場語言的變體名稱。</p> */}
                                            </p>
                                            <div className="application-img-wrapper" style={{marginTop:'2%', width:'80%', marginRight:'auto', marginLeft:'auto'}}>
                                                <img src={`/${image1}`} alt='image1'/>
                                            </div>
                                            <div className="application-form-table-wrapper">
                                                { p2m.mp.appliedSku.map((item,index) => {
                                                    return (
                                                        <div className="application-form-table-container">
                                                            <p className='application-form-table-title'>{index + 1}. {item.sellerSku}</p>
                                                            <table role='table' className="table-main table">
                                                                <thead>
                                                                    <tr className="application-table-thead-tr">
                                                                        <th>SKU</th>
                                                                        <th><FormattedMessage id="marketplaceinfo.productid"/></th>
                                                                        <th><FormattedMessage id="marketplaceinfo.productidtype"/></th>
                                                                        <th><FormattedMessage id="marketplaceinfo.variationtheme"/></th>
                                                                        <th><FormattedMessage id="marketplaceinfo.variationname"/></th>
                                                                        <th style={{display: 'flex', justifyContent: 'space-between'}}>
                                                                            <span>
                                                                                <FormattedMessage id="marketplaceinfo.variationnameformarketplace"/>
                                                                                <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                                                    *
                                                                                </span>
                                                                            </span>
                                                                            { d.u.isSp
                                                                                ? null
                                                                                : p2m.currentAp.status == p2m.stMap.get(1)
                                                                                    ? <button onClick={() => {
                                                                                            handleShowEditor('skuVariationName', index);
                                                                                            p2m.showSkuVariationName ? null : smoothScroll(document.getElementById('skuVariationName'))
                                                                                        }}><RateReviewIcon/></button> : null
                                                                            }
                                                                        </th>
                                                                        {/* <th>Color_map</th> */}
                                                                        {/* <th>Condition</th> */}
                                                                        {/* <th>Product Photo</th> */}
                                                                    </tr>
                                                                </thead>
                                                                <tbody className="table-tbody" role="rowgroup">
                                                                    <tr className="table-tbody-tr" rowspan="2">
                                                                        <th>{item.sellerSku}</th>
                                                                        {/* <td>{item.sellerSku}</td> */}
                                                                        <td rowspan="1">{item.noIdProvide ? "由 DRS 提供": item.productId.value}</td>
                                                                        <td rowspan="1">{item.noIdProvide ? "由 DRS 提供": item.productIdType.value}</td>
                                                                        <td rowspan="1">{item.variationTheme.value}</td>
                                                                        <td rowspan="1">{item.variable.value}</td>
                                                                        <td rowspan="1">
                                                                        <Tooltip title={
                                                                            <div style={{fontSize: '15px', lineHeight : '1.5rem'}}>
                                                                                <FormattedMessage id="marketplaceinfo.note"/>
                                                                            </div>

                                                                        }>
                                                                            <span style={{fontSize: '13px', color: '#2d243f', lineHeight:'1.5rem', display: 'flex'}}>
                                                                                <p className="base-input-notice" style={{marginRight: '4px'}}>
                                                                                    <i class="fa fa-question-circle" aria-hidden="true" ></i>
                                                                                    <FormattedMessage id="marketplaceinfo.question"/>
                                                                                </p>
                                                                                { item.variationNameForMarketplace == ''
                                                                                    ?   <p className="base-input-notice drs-blue" style={{marginLeft: '10px'}}>
                                                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                                                            <FormattedMessage id="inputField.required"/>
                                                                                        </p>
                                                                                    :   null
                                                                                }
                                                                            </span>
                                                                            </Tooltip>
                                                                            <FormattedMessage id="marketplaceinfo.variationNameForMarketplacePlaceHolder">
                                                                                { placeholder=>
                                                                                <textarea
                                                                                    id={`${item.sellerSku}-variation-name-for-marketplace`}
                                                                                    className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0) ? 'base-field-readonly':''}`}
                                                                                    name={`${item.sellerSku}-variation-name-for-marketplace`}
                                                                                    rows="1"
                                                                                    // onBlur={(e) => onSkuVariationNameBlur(e)}
                                                                                    onChange={(e)=> onSkuVariationNameChange(e,index)}
                                                                                    placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '': placeholder }
                                                                                    readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true : false}
                                                                                >
                                                                                    {item.variationNameForMarketplace}
                                                                                </textarea>
                                                                                }
                                                                            </FormattedMessage>

                                                                        </td>

                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="6">
                                                                            <div id={`skuVariationName-${index}`}>
                                                                                <Comment
                                                                                    comment={item.comment.variationNameForMarketplace}
                                                                                    handleSaveComment={handleSaveComment}
                                                                                    handleCommentChange={handleCommentChange}
                                                                                    edit={p2m.showSkuVariationName[index]}
                                                                                    handleShow={handleShowEditor}
                                                                                    target='skuVariationName'
                                                                                    index={index}
                                                                                />
                                                                            </div>
                                                                        </td>
                                                                    </tr>

                                                                    <tr className="table-tbody-tr">
                                                                        <td colspan="7">
                                                                            <div style={{marginTop: '2%'}}></div>
                                                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                                                <div className="application-intro-title" style={{display: 'flex',fontSize:'17px'}}>
                                                                                    {item.sellerSku} 圖片
                                                                                </div>
                                                                                { d.u.isSp
                                                                                    ? null
                                                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                                                        ? <button onClick={() => {
                                                                                                handleShowEditor('skuImg', index);
                                                                                                p2m.showSkuImg ? null : smoothScroll(document.getElementById('skuImg'))
                                                                                            }}><RateReviewIcon/></button> : null
                                                                                }
                                                                            </div>
                                                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                                                <div style={{marginTop: '2%',fontSize:'15px'}}>
                                                                                    <FormattedMessage id="marketplaceinfo.mainpicture"/>
                                                                                </div>
                                                                                <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                                                    <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                                                                        <span>請上傳一張圖片作為您 SKU 的主要圖片。</span>
                                                                                    </div>
                                                                                </div>
                                                                                <div style={{marginTop: '1%'}}>
                                                                                    <Dropzone
                                                                                        id={`${item.sellerSku}-picture`}
                                                                                        imgUrl={item.mainImgUrl == undefined ? '': item.mainImgUrl}
                                                                                        p2mName={p2m.currentAp.name}
                                                                                        disabled={p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                                        onChange={onSkuImgChange}
                                                                                        index={index}
                                                                                    />
                                                                                </div>
                                                                            </div>
                                                                            <div style={{marginTop: '2%'}}></div>
                                                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                                                <p className="application-intro-title" style={{fontSize:'15px'}}>
                                                                                    <FormattedMessage id="marketplaceinfo.secondarypicture"/>
                                                                                </p>
                                                                                { item.img.length == 0 || p2m.currentAp.status !== p2m.stMap.get(0)
                                                                                    ?   null
                                                                                    :   <div style={{display: 'flex', alignItems: 'center'}}>
                                                                                            <button
                                                                                                className="drs-btn drs-btn-normal"
                                                                                                onClick={() => handleSkuImgUploadAgain('skuImg', index) }>
                                                                                                <span style={{marginLeft:'6px'}}>重新上傳</span>
                                                                                            </button>
                                                                                            <button
                                                                                                className="drs-btn drs-btn-normal"
                                                                                                onClick={() => handleSkuImgRemoveAll( 'skuImg',index) }>
                                                                                                <span style={{marginLeft:'6px'}}>清空全部</span>
                                                                                            </button>
                                                                                        </div>
                                                                                }
                                                                            </div>
                                                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                                                <p className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                                                                    增加 SKU 副圖片（Additional Images）以呈現產品的不同角度、產品使用情境或產品細節。
                                                                                </p>
                                                                                { showSkuImgUploader
                                                                                    ? <MultiImgUploader
                                                                                            onClose={setShowSkuImgUploader}
                                                                                            onSubmit={handleSkuImgSubmit}
                                                                                            index={showSkuImgIndex}
                                                                                        />
                                                                                    : null
                                                                                }
                                                                                <div>
                                                                                    { item.img.length == 0
                                                                                        ?   <div className="upload-img-area">
                                                                                                <button
                                                                                                    className={`upload-img-btn ${!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0) ? 'disabled': ''}`}
                                                                                                    disabled={!d.u.isSp || p2m.currentAp.status !== p2m.stMap.get(0)}
                                                                                                    onClick={() => {onSkuImgUpload(index)}}
                                                                                                >
                                                                                                    <InsertPhotoIcon fontSize="medium"/>
                                                                                                    <span style={{marginLeft:'6px'}}>
                                                                                                        <FormattedMessage id="dropzone.description"/>
                                                                                                    </span>
                                                                                                </button>
                                                                                            </div>
                                                                                        :   <div className="upload-img-preview-wrapper" style={{width: '1120px'}}>
                                                                                            {item.img.map((file) => (
                                                                                                <div className="upload-img-preview-column">
                                                                                                    {file.preview == '' ?
                                                                                                     <div className="upload-img-preview"><img src={`${DOMAIN_NAME}/p2m/mp/i/${p2m.currentAp.name}/${file.name}`}></img></div>
                                                                                                        : <div className="upload-img-preview"><img src={file.preview}></img></div>
                                                                                                    }
                                                                                                    <a href={`${DOMAIN_NAME}/p2m/mp/i/${p2m.currentAp.name}/${file.name}`} download={file.name}>
                                                                                                        <p style={{fontSize: '14px', margin: '8px 0'}}>{file.name}</p>
                                                                                                    </a>
                                                                                                </div>
                                                                                            ))}
                                                                                            </div>
                                                                                    }
                                                                                </div>
                                                                                <div id={`skuImg-${index}`}>
                                                                                    <Comment
                                                                                        comment={item.comment.img}
                                                                                        handleSaveComment={handleSaveComment}
                                                                                        handleCommentChange={handleCommentChange}
                                                                                        edit={p2m.showSkuImg[index]}
                                                                                        handleShow={handleShowEditor}
                                                                                        target='skuImg'
                                                                                        index={index}
                                                                                    />
                                                                                </div>
                                                                                <div style={{marginBottom: '2%'}}></div>
                                                                            </div>
                                                                        </td>
                                                                    </tr>

                                                                    <tr className="table-tbody-tr">
                                                                        <td colspan='7'>
                                                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                                                <p className="application-intro-title">{item.sellerSku}
                                                                                    <FormattedMessage id="marketplaceinfo.producttitle"/>
                                                                                </p>
                                                                                { d.u.isSp
                                                                                    ? null
                                                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                                                        ? <button onClick={() => {
                                                                                            handleShowEditor('skuMainTitle', index);
                                                                                            p2m.showSkuMainTitle[index] ? null : smoothScroll(document.getElementById(`skuMainTitle-${index}`))
                                                                                        }}><RateReviewIcon/></button> : null
                                                                                }
                                                                            </div>

                                                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                                                <p className="application-intro-paragraph enhance space-between" style={{margin: '0px'}}>
                                                                                    <FormattedMessage id="marketplaceinfo.paragraph7"/>
                                                                                </p>
                                                                                <div className="application-input-wrapper column" style={{margin: '1% 0'}}>

                                                                                    <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                                                        <FormattedMessage id="marketplaceinfo.paragraph13">
                                                                                            { placeholder=>
                                                                                                <textarea
                                                                                                    id={`sku-title-${index}`}
                                                                                                    className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                                                    name={`sku-title-${index}`}
                                                                                                    rows="4"
                                                                                                    onChange={(e) => onSkuTitleChange(e,index)}
                                                                                                    // onBlur={(e) => onSkuTitleBlur(e,index)}
                                                                                                    placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                                                                    readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true : false}
                                                                                                >
                                                                                                    {item.title}
                                                                                                </textarea>
                                                                                            }
                                                                                        </FormattedMessage>
                                                                                    </div>
                                                                                    <div id={`skuTitle-${index}`}>
                                                                                        <Comment
                                                                                            comment={item.comment.title}
                                                                                            handleSaveComment={handleSaveComment}
                                                                                            handleCommentChange={handleCommentChange}
                                                                                            edit={p2m.showSkuMainTitle[index]}
                                                                                            handleShow={handleShowEditor}
                                                                                            target='skuMainTitle'
                                                                                            index={index}
                                                                                        />
                                                                                    </div>
                                                                                </div>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr className="table-tbody-tr">
                                                                        <td colspan="7">
                                                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                                                <p className="application-intro-title">{item.sellerSku}
                                                                                    <FormattedMessage id="marketplaceinfo.productdescription"/>
                                                                                </p>
                                                                                { d.u.isSp
                                                                                    ? null
                                                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                                                        ? <button onClick={() => {
                                                                                            handleShowEditor('skuDescription', index);
                                                                                            p2m.showSkuDescription[index] ? null : smoothScroll(document.getElementById(`skuMainDes-${index}`))
                                                                                        }}><RateReviewIcon/></button>: null
                                                                                }
                                                                            </div>
                                                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                                                <p className="application-intro-paragraph enhance space-between" style={{margin: '0px'}}>
                                                                                    <FormattedMessage id="marketplaceinfo.paragraph8"/>
                                                                                </p>
                                                                                <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                                                                    <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                                                        <FormattedMessage id="marketplaceinfo.paragraph16">
                                                                                            { placeholder=>
                                                                                                <textarea
                                                                                                    id={`sku-product-description-${index}`}
                                                                                                    className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                                                    name={`sku-product-description-${index}`}
                                                                                                    rows="8"
                                                                                                    onChange={(e)=> onSkuDescriptionChange(e,index)}
                                                                                                    // onBlur={(e) => onSkuDescriptionBlur(e,index)}
                                                                                                    placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                                                                    readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                                                >
                                                                                                    {item.description}
                                                                                                </textarea>
                                                                                            }
                                                                                        </FormattedMessage>
                                                                                    </div>
                                                                                </div>
                                                                                <div id={`skuMainDes-${index}`}>
                                                                                    <Comment
                                                                                        comment={item.comment.description}
                                                                                        handleSaveComment={handleSaveComment}
                                                                                        handleCommentChange={handleCommentChange}
                                                                                        edit={p2m.showSkuDescription[index]}
                                                                                        handleShow={handleShowEditor}
                                                                                        target='skuDescription'
                                                                                        index={index}
                                                                                    />
                                                                                </div>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr className="table-tbody-tr">
                                                                        <td colspan="7">
                                                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                                                <p className="application-intro-title">{item.sellerSku}
                                                                                    <FormattedMessage id="marketplaceinfo.productfeatures"/>
                                                                                </p>
                                                                                { d.u.isSp
                                                                                    ? null
                                                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                                                        ?   <button onClick={() => {
                                                                                                handleShowEditor('skuFeature', index);
                                                                                                p2m.showSkuFeature[index] ? null : smoothScroll(document.getElementById(`skuFeature-${index}`))
                                                                                            }}><RateReviewIcon/></button> : null
                                                                                }
                                                                            </div>

                                                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                                                <p className="application-intro-paragraph enhance space-between" style={{margin: '0px'}}>
                                                                                    <FormattedMessage id="marketplaceinfo.paragraph9"/>
                                                                                </p>
                                                                                <ul className="input-list">
                                                                                    { item.feature.map((li,i) => {
                                                                                        return (
                                                                                            <li className="input-list-li" style={{alignItems: 'flex-start'}}>
                                                                                                <p style={{margin: '11px 8px 0 0'}}>{`${i+ 1}.`} </p>
                                                                                                <FormattedMessage id="marketplaceinfo.paragraph14">
                                                                                                    { placeholder=>
                                                                                                        <textarea
                                                                                                            id={`sku-product-feature-0${i + 1}`}
                                                                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                                                            name={`sku-product-feature-0${i + 1}`}
                                                                                                            rows="3"
                                                                                                            onChange={(e)=> onSkuFeatureChange(e,i,index)}
                                                                                                            // onBlur={(e) => onSkuFeatureBlur(e,i,index)}
                                                                                                            placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                                                        >
                                                                                                            {li}
                                                                                                        </textarea>
                                                                                                    }
                                                                                                </FormattedMessage>
                                                                                                </li>
                                                                                        )
                                                                                    })}
                                                                                </ul>
                                                                                <div id={`skuFeature-${index}`}>
                                                                                    <Comment
                                                                                        comment={item.comment.feature}
                                                                                        handleSaveComment={handleSaveComment}
                                                                                        handleCommentChange={handleCommentChange}
                                                                                        edit={p2m.showSkuFeature[index]}
                                                                                        handleShow={handleShowEditor}
                                                                                        target='skuFeature'
                                                                                        index={index}
                                                                                    />
                                                                                </div>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr className="table-tbody-tr">
                                                                        <td colspan="7">
                                                                            <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                                                                <p className="application-intro-title">{item.sellerSku}
                                                                                    <FormattedMessage id="marketplaceinfo.productkeywords"/>
                                                                                </p>
                                                                                { d.u.isSp
                                                                                    ? null
                                                                                    : p2m.currentAp.status == p2m.stMap.get(1)
                                                                                        ? <button onClick={() => {
                                                                                            handleShowEditor('skuKeyword', index);
                                                                                            p2m.showSkuKeyword[index] ? null : smoothScroll(document.getElementById(`skuKeyword-${index}`))
                                                                                        }}><RateReviewIcon/></button> : null
                                                                                }
                                                                            </div>
                                                                            <div className="application-info-row" style={{marginBottom: '0px'}}>
                                                                                <p className="application-intro-paragraph enhance space-between" style={{margin: '0px'}}>
                                                                                    <FormattedMessage id="marketplaceinfo.paragraph10"/>
                                                                                </p>
                                                                                <ul className="input-list">
                                                                                    { item.keyword.map((li, i) => {
                                                                                        return (
                                                                                            <li className="input-list-li">
                                                                                                <p style={{marginRight: '10px'}}>{`${i+ 1}.`} </p>
                                                                                                <FormattedMessage id="marketplaceinfo.paragraph15">
                                                                                                    { placeholder=>
                                                                                                        <textarea
                                                                                                            id={`sku-product-keyword-0${i + 1}`}
                                                                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                                                                            name={`sku-product-keyword-0${i + 1}`}
                                                                                                            rows="1"
                                                                                                            onChange={(e)=> onSkuKeywordChange(e,i,index)}
                                                                                                            // onBlur={(e) => onSkuKeywordBlur(e,i,index)}
                                                                                                            placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                                                                        >
                                                                                                            {li}
                                                                                                        </textarea>
                                                                                                    }
                                                                                                </FormattedMessage>
                                                                                            </li>
                                                                                        )
                                                                                    })}
                                                                                </ul>
                                                                                <div id={`skuKeyword-${index}`}>
                                                                                    <Comment
                                                                                        comment={item.comment.keyword}
                                                                                        handleSaveComment={handleSaveComment}
                                                                                        handleCommentChange={handleCommentChange}
                                                                                        edit={p2m.showSkuKeyword[index]}
                                                                                        handleShow={handleShowEditor}
                                                                                        target='skuKeyword'
                                                                                        index={index}
                                                                                    />
                                                                                </div>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    ) })
                                                }
                                            </div>
                                        </div>
                                }
                                </div>
                            :   null

                        }
                    </div>
                    <div className="section-line"></div>
                    <div className="application-info-section-wrapper">
                        <p className="application-info-section-expand-title" onClick={() => { handleExpand('advance')}}>
                            <FormattedMessage id="marketplaceinfo.expandtitle"/>
                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                            *
                            </span>
                            <span style={{marginLeft: '6px'}}>{advancedExpanded ? <img src={`/${expandDownIcon}`} alt="expandIcon"/> : <img src={`/${expandIcon}`} alt="expandIcon"/>}</span>
                        </p>
                        { advancedExpanded
                            ?   <div>
                                    <div style={{width: '100%', display: 'flex', justifyContent: 'space-between', marginTop: '2%'}}>
                                        <div className="application-intro-title" style={{display: 'flex'}}>
                                            <FormattedMessage id="marketplaceinfo.forbiddenwords"/>
                                        </div>
                                        { d.u.isSp
                                            ? null
                                            : p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('advancedForbiddenWords');
                                                    showAdvancedForbiddenWords ? null : smoothScroll(document.getElementById('advancedForbiddenWords'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </div>
                                    <div className="application-info-row" style={{marginBottom: '0px'}}>
                                        <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                            <FormattedMessage id="marketplaceinfo.paragraph17"/>
                                        </div>
                                        <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                            <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                <FormattedMessage id="marketplaceinfo.forbiddenwords">
                                                    { placeholder=>
                                                        <textarea
                                                            id="advanced-forbiddenwords"
                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            name="advanced-forbiddenwords"
                                                            rows="4"
                                                            onChange={(e)=> onExpandForbiddenWordsChange(e)}
                                                            placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                            value= {p2m.mp.advanced.forbiddenWords}
                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                        >
                                                        </textarea>
                                                    }
                                                </FormattedMessage>
                                            </div>
                                        </div>
                                        <div id="advancedForbiddenWords">
                                            <Comment
                                                comment={p2m.mp.advanced.comment.forbiddenWords}
                                                handleSaveComment={handleSaveComment}
                                                handleCommentChange={handleCommentChange}
                                                edit={showAdvancedForbiddenWords}
                                                handleShow={handleShowEditor}
                                                target='advancedForbiddenWords'
                                            />
                                        </div>
                                    </div>

                                    <div className="section-line" style={{margin: '2% 0'}}></div>

                                    <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                        <div className="application-intro-title" style={{display: 'flex'}}>
                                            <FormattedMessage id="marketplaceinfo.expectedweeklysales"/>
                                            <span className="drs-notice-red" style={{marginLeft: '5px'}}>
                                                *
                                            </span>
                                            { p2m.mp.advanced.expectedWeeklySales == ''
                                                ?   <p className="drs-blue" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                        <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                        <FormattedMessage id="inputField.required"/>
                                                    </p>
                                                :   null
                                            }
                                        </div>
                                        { d.u.isSp
                                            ? null
                                            : p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('advancedExpectedWeeklySales');
                                                    showAdvancedExpectedWeeklySales ? null : smoothScroll(document.getElementById('advancedExpectedWeeklySales'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </div>
                                    <div className="application-info-row" style={{marginBottom: '0px'}}>
                                        <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                            <FormattedMessage id="marketplaceinfo.paragraph18"/>

                                        </div>
                                        <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                            <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                <FormattedMessage id="marketplaceinfo.expectedweeklysales">
                                                    { placeholder=>
                                                        <textarea
                                                            id="advanced-expectedweeklysales"
                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            name="advanced-expectedweeklysales"
                                                            rows="4"
                                                            onChange={(e)=> onExpandExpectedWeeklySalesChange(e)}
                                                            placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                            value= {p2m.mp.advanced.expectedWeeklySales}
                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                        >
                                                        </textarea>
                                                    }
                                                </FormattedMessage>
                                            </div>
                                        </div>
                                        <div id="advancedExpectedWeeklySales">
                                            <Comment
                                                comment={p2m.mp.advanced.comment.expectedWeeklySales}
                                                handleSaveComment={handleSaveComment}
                                                handleCommentChange={handleCommentChange}
                                                edit={showAdvancedExpectedWeeklySales}
                                                handleShow={handleShowEditor}
                                                target='advancedExpectedWeeklySales'
                                            />
                                        </div>
                                    </div>

                                    <div className="section-line" style={{margin: '2% 0'}}></div>

                                    <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                        <div className="application-intro-title" style={{display: 'flex'}}>
                                            <FormattedMessage id="marketplaceinfo.consumerwarranty"/>
                                        </div>
                                        { d.u.isSp
                                            ? null
                                            : p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('advancedConsumerWarranty');
                                                    showAdvancedConsumerWarranty ? null : smoothScroll(document.getElementById('advancedConsumerWarranty'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </div>
                                    <div className="application-info-row" style={{marginBottom: '0px'}}>
                                        <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                            <FormattedMessage id="marketplaceinfo.paragraph19"/>
                                        </div>
                                        <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                            <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                <FormattedMessage id="marketplaceinfo.consumerwarranty">
                                                    { placeholder=>
                                                        <textarea
                                                            id="advanced-consumerwarranty"
                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            name="advanced-consumerwarranty"
                                                            rows="4"
                                                            onChange={(e)=> onExpandConsumerWarrantyChange(e)}
                                                            placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                            value= {p2m.mp.advanced.consumerWarranty}
                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                        >
                                                        </textarea>
                                                    }
                                                </FormattedMessage>
                                            </div>
                                        </div>
                                        <div id="advancedConsumerWarranty">
                                            <Comment
                                                comment={p2m.mp.advanced.comment.consumerWarranty}
                                                handleSaveComment={handleSaveComment}
                                                handleCommentChange={handleCommentChange}
                                                edit={showAdvancedConsumerWarranty}
                                                handleShow={handleShowEditor}
                                                target='advancedConsumerWarranty'
                                            />
                                        </div>
                                    </div>

                                    <div className="section-line" style={{margin: '2% 0'}}></div>

                                    <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                        <div className="application-intro-title" style={{display: 'flex'}}>
                                            <FormattedMessage id="marketplaceinfo.usesoftware"/>
                                        </div>
                                        { d.u.isSp
                                            ? null
                                            : p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('advancedUseSoftware');
                                                    showAdvancedUseSoftware ? null : smoothScroll(document.getElementById('advancedUseSoftware'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </div>
                                    <div className="application-info-row" style={{marginBottom: '0px'}}>
                                        <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                            <FormattedMessage id="marketplaceinfo.paragraph20"/>
                                        </div>
                                        <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                            <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                <FormattedMessage id="marketplaceinfo.usesoftware">
                                                    { placeholder=>
                                                        <textarea
                                                            id="advanced-usesoftware"
                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            name="advanced-usesoftware"
                                                            rows="4"
                                                            onChange={(e)=> onExpandUseSoftwareChange(e)}
                                                            placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                            value= {p2m.mp.advanced.useSoftware}
                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                        >
                                                        </textarea>
                                                    }
                                                </FormattedMessage>
                                            </div>
                                        </div>
                                        <div id="advancedUseSoftware">
                                            <Comment
                                                comment={p2m.mp.advanced.comment.useSoftware}
                                                handleSaveComment={handleSaveComment}
                                                handleCommentChange={handleCommentChange}
                                                edit={showAdvancedUseSoftware}
                                                handleShow={handleShowEditor}
                                                target='advancedUseSoftware'
                                            />
                                        </div>
                                    </div>

                                    {/* <div className="section-line" style={{margin: '2% 0'}}></div>

                                    <div style={{width: '100%', display: 'flex', justifyContent: 'space-between'}}>
                                        <div className="application-intro-title" style={{display: 'flex'}}>
                                            <FormattedMessage id="marketplaceinfo.trademarkfile"/>
                                        </div>
                                        { d.u.isSp
                                            ? null
                                            : p2m.currentAp.status == p2m.stMap.get(1)
                                                ? <button onClick={() => {
                                                    handleShowEditor('advancedTradeMarkFile');
                                                    showAdvancedTradeMarkFile ? null : smoothScroll(document.getElementById('advancedTradeMarkFile'))
                                                }}><RateReviewIcon/></button>
                                                : null
                                        }
                                    </div>
                                    <div className="application-info-row" style={{marginBottom: '0px'}}>
                                        <div className="application-intro-paragraph enhance" style={{margin: '0px'}}>
                                            <FormattedMessage id="marketplaceinfo.paragraph21"/>
                                        </div>
                                        <div className="application-input-wrapper column" style={{margin: '1% 0'}}>
                                            <div className="base-input-with-description-wrapper" style={{width: '100%'}}>
                                                <FormattedMessage id="marketplaceinfo.trademarkfile">
                                                    { placeholder=>
                                                        <textarea
                                                            id="advanced-trademarkfile"
                                                            className={`base-textarea ${p2m.currentAp.status !== p2m.stMap.get(0)? 'base-field-readonly':''}`}
                                                            name="advanced-trademarkfile"
                                                            rows="4"
                                                            onChange={(e)=> onExpandTradeMarkFileChange(e)}
                                                            placeholder={p2m.currentAp.status !== p2m.stMap.get(0)? '':placeholder }
                                                            value= {p2m.mp.advanced.tradeMarkFile}
                                                            readOnly={p2m.currentAp.status !== p2m.stMap.get(0)? true: false}
                                                        >
                                                        </textarea>
                                                    }
                                                </FormattedMessage>
                                            </div>
                                        </div>
                                        <div id="advancedTradeMarkFile">
                                            <Comment
                                                comment={p2m.mp.advanced.comment.tradeMarkFile}
                                                handleSaveComment={handleSaveComment}
                                                handleCommentChange={handleCommentChange}
                                                edit={showAdvancedTradeMarkFile}
                                                handleShow={handleShowEditor}
                                                target='advancedTradeMarkFile'
                                            />
                                        </div>
                                    </div> */}
                                </div>
                            :   null
                        }
                    </div>
                    <div className="section-line"></div>
                    <div>
                        {showValidWarning
                            ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                    <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                        <FormattedMessage id="marketplaceinfo.warning"/>
                                    </p>
                                </div>
                            :   null
                        }
                        {showSkuValidWarning
                            ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                    <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                        <FormattedMessage id="marketplaceinfo.secondwarning"/>
                                    </p>
                                </div>
                            :   null
                        }
                        {showExpandValidWarning
                            ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                    <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                        進階項目預期周銷量未填寫
                                    </p>
                                </div>
                            :   null
                        }
                    </div>
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
                                // :  <Link to={`/product/application/${id}`}>
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
    //} end render
}

export default MarketPlaceInfoApplication