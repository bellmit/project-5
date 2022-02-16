import React, { useEffect, useState} from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { FormattedMessage} from 'react-intl';
import Select from 'react-select';
import { initP2MApplication, selectApplicationType, selectApplicationProduct,
    selectPlatform, selectApplicationAppliedSku, selectCountry, selectAp,selectRemovalSku,
    createP2MApplication, onPlatformOptionsChange, getExactProductForAp, updateModalState,
    changeP2MApplication, updateP2MApplication, updateCurrentAp, hideCreateP2MModal
} from '../../../actions/index';

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

const P2MApplicationInformation = () => {
    const dispatch = useDispatch()
    const pd = useSelector(state => state.PD)
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state=> state.d)
    const user = useSelector(state => state.d.u)
    const appliedDate = p2m.currentAp.appliedDate != "" ?
        new Intl.DateTimeFormat('en-US').format(p2m.currentAp.appliedDate):p2m.currentAp.appliedDate
             
    const approvedDate = p2m.currentAp.approvedDate != "" ?
        new Intl.DateTimeFormat('en-US').format(p2m.currentAp.approvedDate):p2m.currentAp.approvedDate

    // const cancelledDate = p2m.currentAp.cancelledDate != "" ?
    //     new Intl.DateTimeFormat('en-US').format(p2m.currentAp.cancelledDate):p2m.currentAp.cancelledDate
        
    const rejectedDate = p2m.currentAp.rejectedDate != "" ?
        new Intl.DateTimeFormat('en-US').format(p2m.currentAp.rejectedDate):p2m.currentAp.rejectedDate

    const [displayedSkuCode, setDisplayedSkuCode] = useState([])
    const [displayedP2mSkuCode, setDisplayedP2mSkuCode] = useState([])
    const [currentBp, setCurrentBp] = useState({})
    const [showEdit, setShowEdit] = useState(false)

    const handleEditSku = () => {
        setShowEdit(true)
        const product = pd.products.filter(item => item.productNameEN == p2m.currentAp.selectedProduct)
        const skuOptions = product[0].skus.map(item => {
            return { value: item.sellerSku, label: item.sellerSku }
        })
        const appliedSkuCode = p2m.currentAp.appliedSkuCode.map(item => {
            return { value: item, label: item }
        })
        setDisplayedP2mSkuCode(appliedSkuCode)
        dispatch(selectApplicationProduct(p2m.currentAp.selectedProduct, skuOptions))
    }

    const handleSkuChange = (arr) => {
        const p2mId = p2m.currentAp._id.$oid
        const currentAp = p2m.currentAp
        console.log(p2m.currentAp.appliedSkuCode)
        p2m.currentAp.appliedSkuCode = arr
        let skuCode = []
        if (arr) {
            skuCode = arr.map(item => {return item.value})
            setDisplayedSkuCode(arr)
            const appliedSkuCode = p2m.currentAp.appliedSkuCode.map(item => {
                return { value: item.value, label: item.value }
            })
            setDisplayedP2mSkuCode(appliedSkuCode)
        } else {
            setDisplayedSkuCode([])
            setDisplayedP2mSkuCode([])
        }
        dispatch(selectApplicationAppliedSku(skuCode))
    }

    const handleSaveEditSku = () => {
        const p2mId = p2m.currentAp._id.$oid
        const currentAp = p2m.currentAp
        const supplierId = d.u.cid;
        const message = p2m.currentAp.name + "---SKU 編輯完成"
        setShowEdit(false)
        const filterProducts = pd.products.filter(item => item.productNameEN == p2m.currentAp.selectedProduct)
        const skuInfo = filterProducts[0].skus
        dispatch(updateP2MApplication(supplierId, p2mId, currentAp, skuInfo, message))
    }

    return (
        <div className="application-info-section-wrapper">
            <p className="application-info-section-title">
                <FormattedMessage id="p2mapplication.applicationinfo"/>
            </p>
            <div className="application-info-section">
                <div className="application-info-column" style={{width: '25%'}}>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.supplier"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.supplierId}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.status"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.status}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.applicationtype"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.type}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.country"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.selectedCountry}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.platform"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.selectedPlatform}</p>
                    </div>
                    
                </div>
                <div className="application-info-column" style={{width: '25%'}}>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.applieddate"/>
                        </p>
                        <p className="application-info-content">{appliedDate}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.approveddate"/>
                        </p>
                        <p className="application-info-content">{approvedDate}</p>
                    </div>
                    {/* <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.cancelledDate"/>
                        </p>
                        <p className="application-info-content">{cancelledDate}</p>
                    </div> */}
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.rejectedDate"/>
                        </p>
                        <p className="application-info-content">{rejectedDate}</p>
                    </div>
                </div>
                
                <div className="application-info-column" style={{width: '50%'}}>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '25%'}}>
                            <FormattedMessage id="p2mapplication.brandname"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.brandNameEN}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '25%'}}>
                            <FormattedMessage id="p2mapplication.productname"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.productNameEN}</p>
                    </div>
                    <div className="application-info" style={{alignItems: 'flex-start'}}>
                        <p className="application-info-title" style={{width: '25%'}}>SKU</p>
                        <p className="application-info-content" style={{width: '50%', flexWrap: 'wrap', display:'flex'}}>
                            { p2m.currentAp.appliedSkuCode.length == null
                                ? ''
                                : p2m.currentAp.appliedSkuCode.map((sku,i) => {
                                    return (<span style={{marginRight: '6px'}}>{sku}
                                        {i == p2m.currentAp.appliedSkuCode.length - 1 ? '': ','}
                                    </span>)
                                })
                            }
                        </p>
                        { user.isSp == true && p2m.currentAp.status == p2m.stMap.get(0) && showEdit == false
                            ?   <span className="base-input-with-description-wrapper" style={{display: 'contents', margin: '8px 0',width: '50%'}}>
                                    <button className="drs-btn drs-btn-cta" style={{height:'30px'}}
                                        onClick={()=> {handleEditSku()}}
                                    >
                                        編輯
                                    </button>
                                </span>
                            :   null
                        }
                    </div>
                    <div className="application-info">
                        {showEdit
                            ?   <p className="base-input-with-description-wrapper" style={{display: 'contents', margin: '8px 0',width: '50%'}}>
                                    <FormattedMessage id="productinfo.select">
                                        { placeholder =>
                                            <Select
                                                className="base-selector"
                                                styles={customStyles}
                                                value={displayedP2mSkuCode}
                                                placeholder={displayedP2mSkuCode.length == 0 ? placeholder: displayedP2mSkuCode}
                                                onChange={handleSkuChange}
                                                options={p2m.skuOptions}
                                                classNamePrefix='mySelector'
                                                isMulti
                                            />
                                        }
                                    </FormattedMessage>
                                    <button className="drs-btn drs-btn-cta" style={{height:'30px'}}
                                        onClick={()=> {handleSaveEditSku()}}
                                    >
                                        Save
                                    </button>
                                </p>
                            :   null
                        }
                    </div>
                </div>                            
            </div>
        </div>
    )
}

export default P2MApplicationInformation