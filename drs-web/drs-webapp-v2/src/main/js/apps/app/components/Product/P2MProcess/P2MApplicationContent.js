import React, {useState} from "react";
import { useSelector, useDispatch } from "react-redux";
import { Link, useParams,useRouteMatch } from "react-router-dom";
import { updateCurrentSubAp, handleSmoothScroll, changeSkuItemShow,updateRedirectAp } from "../../../actions";
import { FormattedMessage } from 'react-intl';
import CheckIcon from '@material-ui/icons/Check';
import ErrorIcon from '@material-ui/icons/Error';
import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
import KeyboardArrowRightIcon from '@material-ui/icons/KeyboardArrowRight';
import DescriptionIcon from '@material-ui/icons/Description';

const P2MApplicationList = () => {
    const dispatch = useDispatch()
    const p2m = useSelector(state => state.P2M)
    //let { id } = useParams();

    const marketplacePath = `/product/application/marketplace`
    const insurancePath = `/product/application/insurance`
    const productinfoPath = `/product/application/productinfo`
    const shippingPath = `/product/application/shipping`
    const regionalPath = `/product/application/regional`

    // const breadcrumbsHandler = (subAp) => {
    //     let currentSubAp = {}
    //     if (subAp == 'marketplace') {
    //         currentSubAp = p2m.currentAp.subApplication.marketPlaceInfo
    //     } else if (subAp == 'insurance') {
    //         currentSubAp = p2m.currentAp.subApplication.insurance
    //     } else if (subAp == 'regional') {
    //         currentSubAp = p2m.currentAp.subApplication.regional
    //     } else if (subAp == 'shipping') {
    //         currentSubAp = p2m.currentAp.subApplication.shipping
    //     } else if (subAp == 'productinfo') {
    //         currentSubAp = p2m.currentAp.subApplication.productInfo
    //     }
    //     dispatch(updateCurrentSubAp(currentSubAp))
    // }

    const [marShow, setMarShow] = useState(false)
    const [advShow, setAdvShow] = useState(true)
    const [insShow, setInsShow] = useState(false)
    const [shiShow, setShiShow] = useState(false)
    const [proShow, setProShow] = useState(false)
    const [regShow, setRegShow] = useState(false)

    console.log(p2m)
    console.log(p2m.mp.status)
    console.log(p2m.ins.process)
    console.log(p2m.re.status)
    console.log(p2m.sh.status)
    console.log(p2m.pai.status)

    const handleShowReview = (target, index) => {
        switch(target){
            case 'marketplaceinfo':
                return marShow ? setMarShow(false): setMarShow(true)
            case 'advanced':
                return advShow ? setAdvShow(false): setAdvShow(true)
            case 'insurance':
                return insShow ? setInsShow(false): setInsShow(true)
            case 'shipping':
                return shiShow ? setShiShow(false): setShiShow(true)
            case 'productinfo':
                return proShow ? setProShow(false): setProShow(true)
            case 'regional':
                return regShow ? setRegShow(false): setRegShow(true)
            case 'marSkuItem':
                let arr = [...p2m.skuItemShow];
                p2m.skuItemShow[index] ? arr[index] = false : arr[index] = true
                return dispatch(changeSkuItemShow(arr))
        }
    }

    return (
        <div className="application-sheet-list-section-wrapper">
            <p className="application-sheet-list-title">
                <FormattedMessage id="p2mapplication.applicationforms"/>
            </p>
            <div style={{padding: '1%'}}>
                <div className="application-sheet-list-wrapper">
                    <p className="application-sheet-list-item-type">
                        <FormattedMessage id="subApplication.marketplaceinfoSubtitle"/>
                    </p>
                    <div className="application-sheet-list" >
                        <Link className="application-sheet-list-link" to={marketplacePath}
                            onClick={()=> {
                                // breadcrumbsHandler('marketplace');
                                dispatch(updateRedirectAp('p2mSubAp'));
                                dispatch(handleSmoothScroll([false, '']));
                            }}>
                            <div className="application-sheet-list-column" >
                                <DescriptionIcon/>
                                {/* <span className="application-sheet-list-item-title">{p2m.currentAp.subApplication.marketPlaceInfo.name} </span> */}
                                <span className="application-sheet-list-item-title"> {p2m.currentAp.name}-<FormattedMessage id="subApplication.marketplaceinfoTitle"/></span>
                            </div>
                            <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}> {p2m.mp.status}</span>
                        </Link>
                        {p2m.currentAp.status == p2m.stMap.get(0)
                            ?   null
                            :   <div>
                                <div className="application-sheet-list-lineitem">
                                    <p className="application-sheet-list-item-content" style={{display: 'flex', alignItems: 'center'}}>
                                        <span style={marShow ? {fontWeight: '500'}: {fontWeight: '400'}}>檢視詳細審核結果</span>
                                        <span style={{ margin: '0 0 2.1px 10px',cursor: 'pointer'}} onClick={() => {handleShowReview('marketplaceinfo')}}>
                                            {  marShow
                                                ? <KeyboardArrowDownIcon fontSize="small"/>
                                                : <KeyboardArrowRightIcon fontSize="small"/>
                                            }
                                        </span>
                                    </p>
                                </div>
                                {   marShow
                                    ?   <div style={{padding: '1%', display: 'flex'}}>
                                            <div className="application-sheet-list-column-section">
                                                <ul className={`application-info-list-wrapper ${marShow ? 'list-show' : 'list-hidden'}`}>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            breadcrumbsHandler('marketplace');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true,'competitor']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={marketplacePath}>
                                                            <span>Competitor Info</span>
                                                            <span className="application-info-list-icon">
                                                                { p2m.allComments.marketPlaceInfo.competitorInfo == ''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                </ul>
                                                <ul className={`application-info-list-wrapper ${marShow ? 'list-show' : 'list-hidden'}`}>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('marketplace');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'mainImg']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={marketplacePath}>
                                                            <span>Main Image</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.marketPlaceInfo.main.img == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('marketplace');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true,'mainTitle']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={marketplacePath}>
                                                            <span>Main Title</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.marketPlaceInfo.main.title == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('marketplace');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true,'mainDescription']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={marketplacePath}>
                                                            <span>Main Description</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.marketPlaceInfo.main.description == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/> }
                                                            </span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('marketplace');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'mainFeature']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={marketplacePath}>
                                                            <span>Main Feature</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.marketPlaceInfo.main.feature == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('marketplace');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'mainKeyword']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={marketplacePath}>
                                                            <span>Main Keyword</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.marketPlaceInfo.main.keyword == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                </ul>
                                            </div>
                                            <div className="application-sheet-list-column-section">
                                                <div className={`application-info-list-wrapper ${marShow ? 'list-show' : 'list-hidden'}`}>
                                                    <p style={{display: 'flex', alignItems: 'center'}}>
                                                        <span style={advShow ? {fontWeight: '600'}: {fontWeight: '500'}}>Advanced</span>
                                                        <span style={{ margin: '0 0 2.1px 10px',cursor: 'pointer'}} onClick={() => {handleShowReview('advanced')}}>
                                                            {  advShow
                                                                ? <KeyboardArrowDownIcon fontSize="small"/>
                                                                : <KeyboardArrowRightIcon fontSize="small"/>
                                                            }
                                                        </span>
                                                    </p>
                                                    <ul className={`application-info-list-wrapper ${advShow ? 'list-show' : 'list-hidden'}`}>
                                                        <li className="application-info-list"
                                                            onClick={()=> {
                                                                // breadcrumbsHandler('marketplace');
                                                                dispatch(updateRedirectAp('p2mSubAp'));
                                                                dispatch(handleSmoothScroll([true, 'advancedForbiddenWords']))
                                                            }}>
                                                            <Link className="application-info-list-link" to={marketplacePath}>
                                                                <span>Forbidden Words</span>
                                                                <span className="application-info-list-icon">
                                                                    {p2m.allComments.marketPlaceInfo.advanced.forbiddenWords == ''
                                                                        ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                                </span>
                                                            </Link>
                                                        </li>
                                                        <li className="application-info-list"
                                                            onClick={()=> {
                                                                // breadcrumbsHandler('marketplace');
                                                                dispatch(updateRedirectAp('p2mSubAp'));
                                                                dispatch(handleSmoothScroll([true,'advancedExpectedWeeklySales']))
                                                            }}>
                                                            <Link className="application-info-list-link" to={marketplacePath}>
                                                                <span>Expected Weekly Sales</span>
                                                                <span className="application-info-list-icon">
                                                                    {p2m.allComments.marketPlaceInfo.advanced.expectedWeeklySales == ''
                                                                        ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                                </span>
                                                            </Link>
                                                        </li>
                                                        <li className="application-info-list"
                                                            onClick={()=> {
                                                                // breadcrumbsHandler('marketplace');
                                                                dispatch(updateRedirectAp('p2mSubAp'));
                                                                dispatch(handleSmoothScroll([true,'advancedConsumerWarranty']))
                                                            }}>
                                                            <Link className="application-info-list-link" to={marketplacePath}>
                                                                <span>Consumer Warranty</span>
                                                                <span className="application-info-list-icon">
                                                                    {p2m.allComments.marketPlaceInfo.advanced.consumerWarranty == ''
                                                                        ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/> }
                                                                </span>
                                                            </Link>
                                                        </li>
                                                        <li className="application-info-list"
                                                            onClick={()=> {
                                                                // breadcrumbsHandler('marketplace');
                                                                dispatch(updateRedirectAp('p2mSubAp'));
                                                                dispatch(handleSmoothScroll([true, 'advancedUseSoftware']))
                                                            }}>
                                                            <Link className="application-info-list-link" to={marketplacePath}>
                                                                <span>Software Used</span>
                                                                <span className="application-info-list-icon">
                                                                    {p2m.allComments.marketPlaceInfo.advanced.useSoftware == ''
                                                                        ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                                </span>
                                                            </Link>
                                                        </li>
                                                        {/*<li className="application-info-list"
                                                            onClick={()=> {
                                                                // breadcrumbsHandler('marketplace');
                                                                dispatch(updateRedirectAp('p2mSubAp'));
                                                                dispatch(handleSmoothScroll([true, 'advancedTradeMarkFile']))
                                                            }}>
                                                            <Link className="application-info-list-link" to={marketplacePath}>
                                                                <span>Trademark File</span>
                                                                <span className="application-info-list-icon">
                                                                    {p2m.allComments.marketPlaceInfo.advanced.tradeMarkFile == ''
                                                                        ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                                </span>
                                                            </Link>
                                                        </li>*/}
                                                    </ul>
                                                </div>
                                            </div>
                                            <div className="application-sheet-list-column-section">

                                            { p2m.allComments.marketPlaceInfo.appliedSku.map((item, index) => {
                                                return (
                                                    <div className={`application-info-list-wrapper ${marShow ? 'list-show' : 'list-hidden'}`}>
                                                        <p style={{display: 'flex', alignItems: 'center'}}>
                                                            <span style={p2m.skuItemShow[index] ? {fontWeight: '600'}: {fontWeight: '500'}}>{item.sellerSku}</span>
                                                            <span style={{ margin: '0 0 2.1px 10px',cursor: 'pointer'}} onClick={() => {handleShowReview('marSkuItem',index)}}>
                                                                {  p2m.skuItemShow[index]
                                                                    ? <KeyboardArrowDownIcon fontSize="small"/>
                                                                    : <KeyboardArrowRightIcon fontSize="small"/>
                                                                }
                                                            </span>
                                                        </p>
                                                        <ul className={`application-info-list-wrapper ${p2m.skuItemShow[index] ? 'list-show' : 'list-hidden'}`}>
                                                            <li className="application-info-list"
                                                                onClick={() => {
                                                                    // breadcrumbsHandler('marketPlace');
                                                                    dispatch(updateRedirectAp('p2mSubAp'));
                                                                    dispatch(handleSmoothScroll([true,`skuVariationName-${index}`,'skus'] ))
                                                                }}
                                                            >
                                                                <Link className="application-info-list-link" to={marketplacePath}>
                                                                    <span> SKU { item.sellerSku} Variation Name For MarketPlace</span>
                                                                    <span className="application-info-list-icon">
                                                                        { item.variationNameForMarketplace == ''
                                                                            ? <CheckIcon fontSize="small"  />
                                                                            : <ErrorIcon style={{color: '#f0142f'}}/>
                                                                        }
                                                                    </span>
                                                                </Link>
                                                            </li>
                                                            <li className="application-info-list"
                                                                onClick={()=> {
                                                                    // breadcrumbsHandler('marketplace');
                                                                    dispatch(updateRedirectAp('p2mSubAp'));
                                                                    dispatch(handleSmoothScroll([true,`skuImg-${index}`,'skus']))
                                                                }}>
                                                                <Link className="application-info-list-link" to={marketplacePath}>
                                                                    <span> SKU {item.sellerSku} Image</span>
                                                                    <span className="application-info-list-icon">
                                                                        {item.img == ''
                                                                            ? <CheckIcon fontSize="small"  />
                                                                            : <ErrorIcon style={{color: '#f0142f'}}/> }
                                                                    </span>
                                                                </Link>
                                                            </li>
                                                            <li className="application-info-list"
                                                                onClick={()=> {
                                                                    // breadcrumbsHandler('marketplace');
                                                                    dispatch(updateRedirectAp('p2mSubAp'));
                                                                    dispatch(handleSmoothScroll([true,`skuTitle-${index}`,'skus']))
                                                                }}>
                                                                <Link className="application-info-list-link" to={marketplacePath}>
                                                                    <span> SKU {item.sellerSku} Title</span>
                                                                    <span className="application-info-list-icon">
                                                                        {item.title == ''
                                                                            ? <CheckIcon fontSize="small"  />
                                                                            : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                                    </span>
                                                                </Link>
                                                            </li>
                                                            <li className="application-info-list"
                                                                onClick={()=> {
                                                                    // breadcrumbsHandler('marketplace');
                                                                    dispatch(updateRedirectAp('p2mSubAp'));
                                                                    dispatch(handleSmoothScroll([true,`skuMainDes-${index}`, 'skus']))
                                                                }}>
                                                                <Link className="application-info-list-link"to={marketplacePath}>
                                                                    <span> SKU {item.sellerSku} Description</span>
                                                                    <span className="application-info-list-icon">
                                                                        { item.description == ''
                                                                            ? <CheckIcon fontSize="small"  />
                                                                            : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                                    </span>
                                                                </Link>
                                                            </li>
                                                            <li className="application-info-list"
                                                                onClick={()=> {
                                                                    // breadcrumbsHandler('marketplace');
                                                                    dispatch(updateRedirectAp('p2mSubAp'));
                                                                    dispatch(handleSmoothScroll([true,`skuFeature-${index}`,'skus']))
                                                                }}>
                                                                <Link className="application-info-list-link" to={marketplacePath}>
                                                                    <span> SKU {item.sellerSku} Feature</span>
                                                                    <span className="application-info-list-icon">
                                                                        { item.feature == ''
                                                                            ? <CheckIcon fontSize="small"  />
                                                                            : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                                    </span>
                                                                </Link>
                                                            </li>
                                                            <li className="application-info-list"
                                                                onClick={()=> {
                                                                    // breadcrumbsHandler('marketplace');
                                                                    dispatch(updateRedirectAp('p2mSubAp'));
                                                                    dispatch(handleSmoothScroll([true,`skuKeyword-${index}`,'skus']))
                                                                }}>
                                                                <Link className="application-info-list-link" to={marketplacePath}>
                                                                    <span> SKU {item.sellerSku} Keyword</span>
                                                                    <span className="application-info-list-icon">
                                                                        { item.keyword == ''
                                                                            ? <CheckIcon fontSize="small"  />
                                                                            : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                                    </span>
                                                                </Link>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                )
                                            })}
                                            </div>
                                        </div>
                                    :   null
                                }
                                </div>
                        }
                    </div>
                </div>
                <div className="application-sheet-list-wrapper">
                    <p className="application-sheet-list-item-type">
                        <FormattedMessage id="subApplication.insuranceSubtitle"/>
                    </p>
                    <div className="application-sheet-list">
                    <Link className="application-sheet-list-link" to={insurancePath}
                        onClick={()=> {
                            // breadcrumbsHandler('insurance');
                        dispatch(updateRedirectAp('p2mSubAp')); dispatch(handleSmoothScroll([false, ''])) }}>
                        <div className="application-sheet-list-column">
                            <DescriptionIcon/>
                            {/* <span className="application-sheet-list-item-title">{p2m.currentAp.subApplication.insurance.name}</span> */}
                            <span className="application-sheet-list-item-title"> {p2m.currentAp.name}-<FormattedMessage id="subApplication.insuranceTitle"/></span>
                        </div>

                        {p2m.ins.process == "ph-1" || p2m.ins.process == "ph-2"
                            ?   <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}>Draft</span>
                            :   <span>
                                    {p2m.ins.process == "ph-3"
                                        ?   <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}>Evaluating</span>
                                        :   null
                                    }
                                    {p2m.ins.process == "ph-4"
                                        ?   <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}>Suggestion replied</span>
                                        :   null
                                    }
                                    {p2m.ins.process == "ph-6"
                                        ?   <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}>In Review</span>
                                        :   null
                                    }
                                    {p2m.ins.process == "ph-7"
                                        ?   <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}>Approved</span>
                                        :   null
                                    }
                                </span>
                        }


                    </Link>
                    {p2m.currentAp.status == p2m.stMap.get(0)
                        ?   null
                        :   <div>
                                {/* <div className="application-sheet-list-lineitem">
                                    <p className="application-sheet-list-item-content" style={{display: 'flex', alignItems: 'center'}}>
                                        <span style={insShow ? {fontWeight: '500'}: {fontWeight: '400'}}>檢視項目審查結果</span>
                                        <span style={{ margin: '0 0 2.1px 10px',cursor: 'pointer'}} onClick={() => {handleShowReview('insurance')}}>
                                            {  insShow
                                                ? <KeyboardArrowDownIcon fontSize="small"/>
                                                : <KeyboardArrowRightIcon fontSize="small"/>
                                            }
                                        </span>
                                    </p>
                                </div>
                                {   insShow
                                    ?   <div style={{padding: '1%'}}>
                                            <p style={{fontWeight: '500', lineHeight: '1.5rem'}}>{p2m.currentAp.subApplication.insurance.name}</p>
                                            <ul className="application-info-list-wrapper">
                                                <li className="application-info-list"
                                                    onClick={()=> {
                                                        breadcrumbsHandler('insurance');
                                                        dispatch(updateRedirectAp('p2mSubAp'));
                                                        // dispatch(handleSmoothScroll([true,]))
                                                    }}>
                                                    <Link to={insurancePath}>
                                                        <span>Process</span>
                                                        <span>{p2m.currentAp.subApplication.insurance.process == ''? <CheckIcon /> : <ErrorIcon/>}</span>
                                                    </Link>
                                                </li>
                                                <li className="application-info-list" onClick={()=> {breadcrumbsHandler('insurance'); dispatch(updateRedirectAp('p2mSubAp'))}}>
                                                    <Link to={insurancePath}>
                                                        <span>hasInsured</span>
                                                        <span>{p2m.currentAp.subApplication.insurance.hasInsured == 'yes'? <CheckIcon /> : <ErrorIcon/>}</span>
                                                    </Link>
                                                </li>
                                                <li className="application-info-list" onClick={()=> {breadcrumbsHandler('insurance'); dispatch(updateRedirectAp('p2mSubAp'))}}>
                                                    <Link to={insurancePath}>
                                                        <span>reviewOfInsurance</span>
                                                        <span>{p2m.currentAp.subApplication.insurance.reviewOfInsurance == '' ? <CheckIcon /> : <ErrorIcon/>}</span>
                                                    </Link>
                                                </li>
                                            </ul>
                                        </div>
                                    :   null
                                } */}
                            </div>
                    }

                </div>
                </div>
                <div className="application-sheet-list-wrapper">
                    <p className="application-sheet-list-item-type">
                        <FormattedMessage id="subApplication.regionalSubtitle"/>
                    </p>
                    <div className="application-sheet-list">
                        <Link className="application-sheet-list-link" to={regionalPath}
                            onClick={()=> {
                                // breadcrumbsHandler('regional');
                                dispatch(updateRedirectAp('p2mSubAp'));
                                dispatch(handleSmoothScroll([false, '']))}}>
                            <div className="application-sheet-list-column">
                                <DescriptionIcon/>
                                {/* <span className="application-sheet-list-item-title">{p2m.currentAp.subApplication.regional.name}</span> */}
                                <span className="application-sheet-list-item-title"> {p2m.currentAp.name}-<FormattedMessage id="subApplication.regionalTitle"/></span>
                            </div>
                            <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}> {p2m.re.status}</span>
                        </Link>

                        {p2m.currentAp.status == p2m.stMap.get(0)
                            ?   null
                            :   <div>
                                    <div className="application-sheet-list-lineitem">
                                        <p className="application-sheet-list-item-content" style={{display: 'flex', alignItems: 'center'}}>
                                            <span style={regShow ? {fontWeight: '500'}: {fontWeight: '400'}}>檢視項目審查結果</span>
                                            <span style={{ margin: '0 0 2.1px 10px',cursor: 'pointer'}} onClick={() => {handleShowReview('regional')}}>
                                                {  regShow
                                                    ? <KeyboardArrowDownIcon fontSize="small"/>
                                                    : <KeyboardArrowRightIcon fontSize="small"/>
                                                }
                                            </span>
                                        </p>
                                    </div>

                                    { regShow
                                        ?   <div style={{padding: '1%'}}>
                                                <ul className="application-info-list-wrapper">
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('regional');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'productImg']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={regionalPath}>
                                                            <span>Product Image</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.regional.productImg == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('regional');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'certificateFile']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={regionalPath}>
                                                            <span>Certificate File</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.regional.certificateFile==''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('regional');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'patentFile']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={regionalPath}>
                                                            <span>Patent File</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.regional.patentFile == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('regional');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'otherFile']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={regionalPath}>
                                                            <span>Other File</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.regional.otherFile == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                </ul>
                                            </div>
                                        :   null
                                    }
                                </div>
                        }

                    </div>
                </div>
                <div className="application-sheet-list-wrapper">
                    <p className="application-sheet-list-item-type">
                        <FormattedMessage id="subApplication.shippingSubtitle"/>
                    </p>
                    <div className="application-sheet-list">
                        <Link className="application-sheet-list-link" to={shippingPath}
                            onClick={()=> {
                                // breadcrumbsHandler('shipping');
                                dispatch(updateRedirectAp('p2mSubAp')); dispatch(handleSmoothScroll([false, '']))}}>
                            <div className="application-sheet-list-column">
                                <DescriptionIcon/>
                                {/* <span className="application-sheet-list-item-title">{p2m.currentAp.subApplication.shipping.name}</span> */}
                                <span className="application-sheet-list-item-title"> {p2m.currentAp.name}-<FormattedMessage id="subApplication.shippingTitle"/></span>
                            </div>
                            <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}> {p2m.sh.status}</span>
                        </Link>
                        {p2m.currentAp.status == p2m.stMap.get(0)
                            ?   null
                            :   <div>
                                    <div className="application-sheet-list-lineitem">
                                        <p className="application-sheet-list-item-content" style={{display: 'flex', alignItems: 'center'}}>
                                            <span style={shiShow ? {fontWeight: '500'}: {fontWeight: '400'}}>檢視項目審查結果</span>
                                            <span style={{ margin: '0 0 2.1px 10px',cursor: 'pointer'}} onClick={() => {handleShowReview('shipping')}}>
                                                {  shiShow
                                                    ? <KeyboardArrowDownIcon fontSize="small"/>
                                                    : <KeyboardArrowRightIcon fontSize="small"/>
                                                }
                                            </span>
                                        </p>
                                    </div>
                                    {   shiShow
                                        ?   <div style={{padding: '1%'}}>
                                                <ul className="application-info-list-wrapper">
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('shipping');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'shippingMethod']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={shippingPath}>
                                                            <span>Shipping Method</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.shipping.shippingMethod == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('shipping');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'shippingInfo']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={shippingPath}>
                                                            <span>Shipping Info</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.shipping.shippingInfo == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}
                                                            </span>
                                                        </Link>
                                                    </li>
                                                </ul>
                                            </div>
                                        :   null
                                    }
                                </div>
                        }
                    </div>
                </div>
                <div className="application-sheet-list-wrapper">
                    <p className="application-sheet-list-item-type">
                        <FormattedMessage id="subApplication.productinfoSubtitle"/>
                    </p>
                    <div className="application-sheet-list">
                        <Link className="application-sheet-list-link" to={productinfoPath}
                            onClick={()=> {
                                // breadcrumbsHandler('productinfo');
                                dispatch(updateRedirectAp('p2mSubAp'));dispatch(handleSmoothScroll([false, '']))}}>
                            <div className="application-sheet-list-column">
                                <DescriptionIcon/>
                                {/* <span className="application-sheet-list-item-title">{p2m.currentAp.subApplication.productInfo.name}</span> */}
                                <span className="application-sheet-list-item-title"> {p2m.currentAp.name}-<FormattedMessage id="subApplication.productinfoTitle"/></span>
                            </div>
                            <span className="application-sheet-list-item-title" style={{fontSize:'14px',fontWeight:'600'}}> {p2m.pai.status}</span>
                        </Link>
                        {p2m.currentAp.status == p2m.stMap.get(0)
                            ?   null
                            :   <div>
                                    <div className="application-sheet-list-lineitem">
                                        <p className="application-sheet-list-item-content" style={{display: 'flex', alignItems: 'center'}}>
                                            <span style={proShow ? {fontWeight: '500'}: {fontWeight: '400'}}>檢視項目審查結果</span>
                                            <span style={{ margin: '0 0 2.1px 10px',cursor: 'pointer'}} onClick={() => {handleShowReview('productinfo')}}>
                                                {  proShow
                                                    ? <KeyboardArrowDownIcon fontSize="small"/>
                                                    : <KeyboardArrowRightIcon fontSize="small"/>
                                                }
                                            </span>
                                        </p>
                                    </div> 
                                
                                    { proShow
                                        ?   <div style={{padding: '1%'}}>
                                                <ul className="application-info-list-wrapper">
                                                    <li className="application-info-list" 
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'))
                                                            dispatch(handleSmoothScroll([true, 'url']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>URL</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.url== ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'startdate']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Start Date</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.startDate == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'manufactureDays']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Manufacture Days</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.manufactureDays == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'manufacturePlace']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Manufacture Place</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.manufacturePlace == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'modelNumber']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Model Number</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.modelNumber == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'productId']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Product Id</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.productId == ''
                                                                    ? <CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'packageImg']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Package Image</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.packageImg == ''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'packageFile']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Package File</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.packageFile == ''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'manualImg']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Manual Image</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.manualImg == ''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'manualFile']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Manual File</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.manualFile == ''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'woodenFile']))
                                                        }}>
                                                        <Link className="application-info-list-link"to={productinfoPath}>
                                                            <span>Wooden File</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.woodenFile == ''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'wirelessFile']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Wireless File</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.wirelessFile == ''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'batteryFile']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Battery File</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.batteryFile == ''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'hsCode']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>HsCode</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.hsCode ==''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                    <li className="application-info-list"
                                                        onClick={()=> {
                                                            // breadcrumbsHandler('productInfo');
                                                            dispatch(updateRedirectAp('p2mSubAp'));
                                                            dispatch(handleSmoothScroll([true, 'ingredient']))
                                                        }}>
                                                        <Link className="application-info-list-link" to={productinfoPath}>
                                                            <span>Ingredient</span>
                                                            <span className="application-info-list-icon">
                                                                {p2m.allComments.productInfo.ingredient ==''
                                                                    ?<CheckIcon fontSize="small"  /> : <ErrorIcon style={{color: '#f0142f'}}/>}</span>
                                                        </Link>
                                                    </li>
                                                </ul>
                                            </div>
                                        :   null
                                    }
                                </div>
                        }
                    
                    </div>
                </div>

            </div>
        </div>
    )
}

export default P2MApplicationList