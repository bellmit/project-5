
import React, { useEffect, useState} from 'react';
import { useSelector, useDispatch } from 'react-redux'
import { Switch, Route, useRouteMatch, Link, useParams} from 'react-router-dom';
import { rejectToRemoveP2MAp, approveToRemoveP2MAp, submitApplication, updateRedirectAp, getExactApplication,
    rejectP2MAp, approveP2MAp,fetchP2MApComment ,refillP2MAp,deleteP2MAp,initP2MShipping,initP2MProductInfo,
    initP2MRegional,initP2MMarketplaceInfo,initP2MInsurance} from '../../../actions/index';
import {FormattedMessage} from 'react-intl';

// import MarketplaceInfo from './MarketplaceInfo';
// import Insurance from './Insurance';
// import Regional from './Regional';
// import Shipping from './Shipping';
// import ProductInfo from './ProductInfo';
import P2MApplicationContent from './P2MApplicationContent';
import P2MApplicationInfo from './P2MApplicationInfo';
import P2MApplicationSummary from './P2MApplicationSummary';
// import ProductBreadcrumbs from './P2MProcess/ProductBreadcrumbs';
// import CheckIcon from '@material-ui/icons/Check';
// import ErrorIcon from '@material-ui/icons/Error';
// import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
// import KeyboardArrowRightIcon from '@material-ui/icons/KeyboardArrowRight';
// import DescriptionIcon from '@material-ui/icons/Description';

const P2MApplication = ({props}) => {
    useEffect(() => {
        // console.log('init P2MApplication', props)
    },[])
    // let { id } = useParams();
    // let match = useRouteMatch();

    const dispatch = useDispatch()
    const p2m = useSelector(state => state.P2M)
    const user = useSelector(state => state.d.u)

    useEffect(() => {
        // const p2mId = p2m.currentAp.p2mApplicationId;
        const p2mId = p2m.currentAp._id.$oid
        // console.log(p2mId, 'p2mId')
        dispatch(fetchP2MApComment(p2mId));
        dispatch(updateRedirectAp(''))

        dispatch(getExactApplication(p2mId))
        dispatch(initP2MShipping(p2mId))
        dispatch(initP2MProductInfo(p2mId))
        dispatch(initP2MRegional(p2mId))
        dispatch(initP2MMarketplaceInfo(p2mId))
        dispatch(initP2MInsurance(p2mId))
    }, [])

    // useEffect(() => {
    //     console.log('geExactApplication', id)
    //     // dispatch(getExactApplication(id))
    // },[p2m.currentAp._id])
    // useEffect(() => {
    // console.log("VVVVVVV")
    // console.log('component mount P2mApplication')

    //todo arthur
    //dispatch(getExactApplication('6087a7140ba6fa6606c65049'))

     //todo arthur  para
         //dispatch(updateRedirectAp(''))
        // dispatch(getExactApplication('608f87fed935b73c9d835d9a'))
         // const p2mId = p2m.currentAp.p2mApplicationId;
         // dispatch(getExactApplication(p2mId))

    //dispatch(updateCurrentSubAp({}))
    //dispatch(getExactApplication('608f87fed935b73c9d835d9a'))
    // }, [])
    console.log(p2m)
    console.log(p2m.mp.status)
    console.log(p2m.ins.process)
    console.log(p2m.re.status)
    console.log(p2m.sh.status)
    console.log(p2m.pai.status)

    const [disabledSubmit, setDisabledSubmit] = useState(false)

    const checkSupApplication = () => {
        if (p2m.mp.status == 'Pending' && p2m.re.status == 'Pending' && p2m.sh.status == 'Pending' && p2m.pai.status == 'Pending' && p2m.ins.process == "ph-7") {
            return false
        } else {
            return true
        }
    }

    const handleSubmitAp = () => {
        const message = <FormattedMessage id = "managep2m.submitted"/>;
        if (confirm("請確認您已經將五份表單填寫完畢。送出時，我們會一併審核。（如資料有缺漏我們將退回申請單。）")) {
            const p2mAp = p2m.currentAp;
            p2mAp.p2mApplicationId = p2m.currentAp._id;
            dispatch(submitApplication(p2mAp,message))
        }
    }
    const handleRejectAp = () => {
        const message = <FormattedMessage id = "managep2m.rejected"/>;
        if (confirm("確認要退回此申請案嗎？")) {
            dispatch(rejectP2MAp(p2m.currentAp, message))
        }
    }
    const handleApproveAp = () => {
        const message = <FormattedMessage id = "managep2m.approved"/>;
        if (confirm("確認要讓此申請案通過嗎？")) {
            dispatch(approveP2MAp(p2m.currentAp, message))
        }
    }
    const handleRejectToRemoveAp = () => {
        const message = "下架申請已被退回";
        if (confirm("確認要退回此下架申請嗎？")) {
            dispatch(rejectToRemoveP2MAp(p2m.currentAp, message))
        }
    }
    const handleApproveToRemoveAp = () => {
        const message = "下架申請已通過核准";
        if (confirm("確認要讓此下架申請通過嗎？")) {
            dispatch(approveToRemoveP2MAp(p2m.currentAp, message))
        }
    }
    const handleRefillAp = () => {
        const message = <FormattedMessage id = "managep2m.refilled"/>;
        if (confirm("確認要重啟此申請案以再度編輯申請內容嗎？")) {
            dispatch(refillP2MAp(p2m.currentAp._id.$oid , message))
        }
    }
    const handleRemoveAp = () => {
        const message = <FormattedMessage id="managep2m.removed"/>
        if (confirm("按下確認將會刪除此申請案，確認刪除嗎？")) {
            dispatch(deleteP2MAp(p2m.currentAp._id.$oid, message))
        }
    }

    return (
        <div>
            <div className="application-wrapper">
                <div className="application">
                    <div className="application-title-wrapper">
                        <p className="application-title" style={{display:'flex',width:'50%'}}>
                            <FormattedMessage id="p2mapplication.application"/>
                            {p2m.currentAp.name}
                            {p2m.mp.status == 'Draft' || p2m.re.status == 'Draft' || p2m.sh.status == 'Draft' || p2m.pai.status == 'Draft'
                                ?   <span>
                                        {p2m.ins.process == "ph-7"
                                            ?   <span className="drs-notice-red" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                    <i class="fa fa-exclamation-circle" aria-hidden="true" style={{marginRight: '2px'}}></i>
                                                    仍有表單處於草稿狀態
                                                </span>
                                            :   <span className="drs-notice-red" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                    <i class="fa fa-exclamation-circle" aria-hidden="true" style={{marginRight: '2px'}}></i>
                                                    仍有表單處於草稿狀態，產品保險尚未通過審核
                                                </span>
                                        }
                                    </span>
                                :   <span>
                                        {p2m.ins.process == "ph-7"
                                            ?   null
                                            :   <span className="drs-notice-red" style={{marginLeft: '10px', fontSize: '13px'}}>
                                                    <i class="fa fa-exclamation-circle" aria-hidden="true" style={{marginRight: '2px'}}></i>
                                                    產品保險尚未通過審核
                                                </span>
                                        }
                                    </span>
                            }
                        </p>

                        { user.isSp
                        ?   <div className="application-action-btn-wrapper">
                                { p2m.currentAp.status == p2m.stMap.get(0)
                                    ?   <button
                                            className={`drs-btn drs-btn-cta ${checkSupApplication() ? 'disabled':''}`}
                                            onClick={() => { handleSubmitAp() }}
                                            disabled={checkSupApplication()}
                                        >
                                            <FormattedMessage id="p2mapplication.submit"/>
                                        </button>
                                    :   null
                                }
                                { p2m.currentAp.status == p2m.stMap.get(0)
                                    ?   <button
                                            className="drs-btn drs-btn-warning"
                                            onClick={() => { handleRemoveAp() }}
                                        >
                                            <FormattedMessage id="p2map.remove"/>
                                        </button>
                                    :   null
                                }
                                { p2m.currentAp.status == p2m.stMap.get(2)
                                    ?   <button
                                            className="drs-btn drs-btn-cta"
                                            onClick={() => {handleRefillAp() }}
                                        >
                                            <FormattedMessage id="p2map.refill"/>
                                        </button>
                                    :   null
                                }
                                
                            </div>
                        :   <div className="application-action-btn-wrapper">
                                { p2m.currentAp.status == p2m.stMap.get(1)
                                    ?   <div>
                                            {/* <button className="drs-btn drs-btn-warning" >
                                                <FormattedMessage id="p2mapplication.warning"/>
                                            </button> */}
                                            <button
                                                className="drs-btn drs-btn-warning"
                                                onClick={() => handleRejectAp()}
                                            >
                                                <FormattedMessage id="p2mapplication.reject"/>
                                            </button>
                                            <button
                                                className="drs-btn drs-btn-cta"
                                                onClick={() => handleApproveAp()}
                                            >
                                                <FormattedMessage id="p2mapplication.approve"/>
                                            </button>
                                        </div>
                                    :   null
                                }
                                { p2m.currentAp.status == p2m.stMap.get(4)
                                    ?   <div>
                                            {/* <button className="drs-btn drs-btn-warning" >
                                                <FormattedMessage id="p2mapplication.warning"/>
                                            </button> */}
                                            <button
                                                className="drs-btn drs-btn-warning"
                                                onClick={() => handleRejectToRemoveAp()}
                                            >
                                                退回下架
                                            </button>
                                            <button
                                                className="drs-btn drs-btn-cta"
                                                onClick={() => handleApproveToRemoveAp()}
                                            >
                                                核准下架
                                            </button>
                                        </div>
                                    :   null
                                }
                            </div>
                    }
                    </div>
                    <div className="section-line"></div>
                    <P2MApplicationSummary/>
                    
                    <P2MApplicationContent/>
                    
                </div>
            </div>
        </div>
    )
}

export default P2MApplication
