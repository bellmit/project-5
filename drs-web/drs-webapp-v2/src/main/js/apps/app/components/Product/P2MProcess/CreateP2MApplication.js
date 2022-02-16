import React, { useEffect, useState} from 'react';
import { useSelector, useDispatch } from 'react-redux';
import closeIcon from '../../../assets/images/close-icon-thin-white@2x.png';
import Select from 'react-select';
import CheckBox from '../common/Checkbox/CheckBox';
import { initP2MApplication, selectApplicationType, selectApplicationProduct,
    selectPlatform, selectApplicationAppliedSku, selectCountry, selectAp,selectRemovalSku,
    createP2MApplication, onPlatformOptionsChange, getExactProductForAp, updateModalState,
    changeP2MApplication, hideCreateP2MModal
} from '../../../actions/index';
import {injectIntl,FormattedMessage} from 'react-intl';
import PublicIcon from '@material-ui/icons/Public';
import AssignmentTurnedInIcon from '@material-ui/icons/AssignmentTurnedIn';
import ListAltIcon from '@material-ui/icons/ListAlt';
import SelectAllIcon from '@material-ui/icons/SelectAll';

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


const CreateP2MApplication = (props) => {
   //  let history = useHistory();
    const dispatch = useDispatch()
    const pd = useSelector(state => state.PD)
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state=> state.d)
    const [checkAllBlacklist, setCheckAllBlacklist] = useState(false)
    const [ blacklist, setBlacklist ] = useState([
        {
            value: "createp2map.blacklist1",
            isChecked: false,
        },{
            value: "createp2map.blacklist2",
            isChecked: false,
        },{
            value: "createp2map.blacklist3",
            isChecked: false,
        },{
            value: "createp2map.blacklist4",
            isChecked: false,
        },{
            value: "createp2map.blacklist5",
            isChecked: false,
        },{
            value: "createp2map.blacklist6",
            isChecked: false,
        },{
            value: "createp2map.blacklist7",
            isChecked: false,
        },{
            value: "createp2map.blacklist8",
            isChecked: false,
        },{
            value: "createp2map.blacklist9",
            isChecked: false,
        },{
            value: "createp2map.blacklist10",
            subItem: [
                {value: "createp2map.blacklist10-1", link: "https://sellercentral.amazon.com/gp/help/external/G201730840"},
                {value: "createp2map.blacklist10-2", link: "https://sellercentral.amazon.com/gp/help/external/G200140860?language=en_US&ref=efph_G200140860_cont_200832300"}
            ],
            isChecked: false,
        }]
    );
    // const [blacklistDisclaimerAgreement, setBlacklistDisclaimerAgreement] = useState(false)
    const [disabledCreate, setDisabledCreate] = useState(false)

    const [currentBp, setCurrentBp] = useState({})
    const [displayedSkuCode, setDisplayedSkuCode] = useState([])

    const message = <FormattedMessage id = "managep2maction.createdsuccessfully"/>;

    const currentApType = props.intl.formatMessage({id: p2m.currentApType.label});
    const currentPlatform = props.intl.formatMessage({id: p2m.currentAp.selectedPlatform});
    const currentCountry = props.intl.formatMessage({ id: p2m.currentAp.selectedCountry});
    const applicationTypeOptions = p2m.applicationTypeOptions.map(item => {
        return { value: item.value, label: props.intl.formatMessage({id: item.label})}
    })

    const countryOptions = p2m.countryOptions.map(item => {
        return { value: item.value, label: props.intl.formatMessage({id:item.label })}
    })

    const apOptions = p2m.applications.map(item => {
        return {value: item._id, label: item.name }
    })

    useEffect(() => {
        //todo arthur
       // const productOptions = pd.bpOptions.slice(1);
       //const product = pd.products.filter(item => item._id.$oid == value.value);
       //setCurrentBp(product[0])
       const param = {
           productOptions: pd.bpSelectOptions,
           selectedProduct: ''
       }
        dispatch(initP2MApplication(param))
        // console.log(disabledCreate, 'disabledCreate')
    },[]);

    const option = [
      {value: 'Select All', label: 'Select All'}
    ]
    const options = option.concat(p2m.skuOptions).concat([{value: 'Clear All', label: 'Clear All'}])

    // if need to select subAp
    // const currentSubApType = props.intl.formatMessage({ id: p2m.currentAp.selectedSubApType})
    // const subApTypeOptions = p2m.subApTypeOptions.map(item => {
    //     return { value: item.value, label: props.intl.formatMessage({id: item.label})}
    // })
    // const handleSubApTypeChange = (value) => {
    //     console.log(value)
    //     dispatch(selectSubApType(value.label))
    // }


    const handleProductChange = (value) => {
        // console.log(value, 'handleProductChange : value')
        const supplierId = d.u.cid;
        const product = pd.products.filter(item => item._id.$oid == value.value);
        setShowValidWarning(false)
        setShowSkuWarning(false)

        if (product.length > 0) {

            setCurrentBp(product[0])
            const skuOptions = product[0].skus.map(item => {
                return { value: item.sellerSku, label: item.sellerSku }
            })
            dispatch(selectApplicationProduct(value.label, skuOptions))

        } else {
            // test
            dispatch(getExactProductForAp(supplierId, value.value))
        }
        // init Sku filter
        setDisplayedSkuCode([])
        dispatch(selectApplicationAppliedSku([]))
    }
    const analyzeCountry = (target) => {
//                let countryValue = '';
                if (target.value == 'com') {
    //                countryValue = 'US'
                        return 'US'
                }else if (target.value == 'co.uk'){
    //                countryValue = 'Uk'
                        return 'UK'
                }else if (target.value == 'ca'){
                        return 'CA'
                }else if (target.value == 'de'){
                          return 'DE'
                }else if (target.value == 'fr'){
                             return 'FR'
                }else if (target.value == 'it'){
                         return 'IT'
                }else if (target.value == 'es'){
                          return 'ES'
                }

            }

    const handleCountryChange = (target) => {
        const result = analyzeCountry(target)
        dispatch(selectCountry(result))
        const platform =  `Amazon.${target.value}`;
        setDisabledCreate(true)
        setShowValidWarning(false)
        setShowPlatformWarning(false)
        let platformOptions = []

        if(target.value == 'com'){
            platformOptions = [
                {label: platform, value: platform },
                {label: 'TrueToSource', value: 'TrueToSource'}
            ];
        }else{
            platformOptions = [
                {label: platform, value: platform }
            ];
        }

        dispatch(onPlatformOptionsChange(platformOptions))
        dispatch(selectPlatform("createp2mapplication.select"))
    }

    const handlePlatformChange = (value) => {
        setShowValidWarning(false)
        dispatch(selectPlatform(value.value))
    }

    const handleSkuChange = (arr) => {
        setShowValidWarning(false)
        let skuCode = []
        if (arr) {
            skuCode = arr.map(item => {return item.value})
            if (skuCode.includes('Select All') == true) {
                skuCode = p2m.skuOptions.map(item => {return item.value})
                setDisplayedSkuCode(p2m.skuOptions)
            } else if (skuCode.includes('Clear All') == true) {
                skuCode = []
                setDisplayedSkuCode([])
            } else {
                skuCode = arr.map(item => {return item.value})
                setDisplayedSkuCode(arr)
            }
        } else {
            setDisplayedSkuCode([])
        }
        dispatch(selectApplicationAppliedSku(skuCode))
    }

    const handleApplicationTypeChange = (value) => {
        setShowValidWarning(false)
        dispatch(selectApplicationType(value))
    }

    const handleChangeApplication = () => {
        const supplierId = d.u.cid;
        //todo arthur message
        dispatch(changeP2MApplication(supplierId, changeApplicationId))
        dispatch(hideCreateP2MModal())
        props.onClose(false)
        handleClear()
    }

    const [showSkuWarning, setShowSkuWarning] = useState(false)
    const [showPlatformWarning, setShowPlatformWarning] = useState(false)
    const [showValidWarning, setShowValidWarning] = useState(false)
    const [showFirstWarning, setShowFirstWarning] = useState(false)
    const [showSecondWarning, setShowSecondWarning] = useState(false)

    const onCheckBlacklist = (index) => {
        const arr = blacklist
        if (blacklist[index].isChecked) {
            arr[index].isChecked = false;
        } else {
            arr[index].isChecked = true;
        }
        setBlacklist(arr)

        handleShowBlacklistWarning(arr)
        checkBlacklist() ? setDisabledCreate(false): setDisabledCreate(true)
    }

    const onCheckAllBlacklist = (index) => {
        const arr = blacklist
        if (checkAllBlacklist == false){
            for(let i =0; i<arr.length ;i++){
                arr[i].isChecked = true;
            }
        } else {
            for(let i =0; i<arr.length ;i++){
                arr[i].isChecked = false;
            }
        }
        setBlacklist(arr)
        handleShowBlacklistWarning(arr)
        checkAllBlacklist ? setDisabledCreate(true) : setDisabledCreate(false)
        checkAllBlacklist ? setCheckAllBlacklist(false) : setCheckAllBlacklist(true)
    }

    const handleShowBlacklistWarning = (arr) => {
        let firstCheckArr = []
        let secondCheckArr = []

        arr.map((item, index) => {
            if (index < 2 && item.isChecked == false) {
                firstCheckArr.push(item.isChecked)
            } else if (index > 1 && item.isChecked == false) {
                secondCheckArr.push(item.isChecked)
            }
        })
        firstCheckArr.length !== 0 ? setShowFirstWarning(true) : setShowFirstWarning(false)
        secondCheckArr.length !== 0 ? setShowSecondWarning(true) : setShowSecondWarning(false)
    }

    const checkBlacklist = () => {
        const checkResultArr = blacklist.filter(item => item.isChecked == false)
        const checkResult = checkResultArr.length == 0 ? true: false
        return checkResult
    }

    // const onAgreeBlacklistDisclaimer = () => {
    //     let boolean = true
    //     if (blacklistDisclaimerAgreement) {
    //         boolean = false
    //     }
    //     setBlacklistDisclaimerAgreement(boolean)
    // }

    const handleCreateApplication = () => {
        const supplierId = d.u.cid;
        let bp = (currentBp.skus == undefined) ?
            pd.products.filter(item => item._id.$oid == p2m.currentAp.selectedProductId)[0]:currentBp

        const filteredSku = p2m.currentAp.appliedSkuCode.map(code => {
            let target = bp.skus.filter(item => item.sellerSku == code)
            return target[0]
        })

        const subApMarketPlaceInfo = filteredSku.map(item => {
            return Object.assign({}, item, {
                skuCode: item.sellerSku,
                variationNameForMarketplace: '',
                mainImgUrl: '',
                img: [],
                title: '',
                description: '',
                feature: ['','','','',''],
                keyword: ['','','','',''],
                comment: {
                    variationNameForMarketplace: '',
                    img:'',
                    title: '',
                    description: '',
                    feature: '',
                    keyword: '',
                }
            })
        })

        const mainMarketPlaceInfo = {
            imgUrl: '',
            secondaryImg: [],
            title: '',
            description: '',
            feature: ['','','','',''],
            keyword: ['','','','',''],
            comment: {
                img:'',
                title: '',
                description: '',
                feature: '',
                keyword: '',
            }
        }
        const advancedMarketPlaceInfo = {
            forbiddenWords: '',
            expectedWeeklySales: '',
            consumerWarranty: '',
            useSoftware: '',
            tradeMarkFile: '',
            comment: {
                forbiddenWords: '',
                expectedWeeklySales: '',
                consumerWarranty: '',
                useSoftware: '',
                tradeMarkFile: ''
            }
        }
        const subApProductInfo = filteredSku.map(item => {
            return {
                skuCode: item.sellerSku,
                productId: item.productId,
                noIdProvide: item.noIdProvide,
                productIdByDrs: '',
                url: '',
                startDate: undefined,
                manufactureDays: '',
                manufacturePlace: '',
                suggestedPricingNoTax: '',
                suggestedPricingTax: '',
                suggestedRetailPriceNoTax: '',
                suggestedRetailPriceTax: '',
                ddpUnitPrice: '',
                modelNumber: '',
                packageFile: [],
                packageImg: [],
                manualFile: [],
                manualImg: [],
                exportSideHsCode: '',
                salesSideHsCode: '',
                ingredientFile: [],
                woodenFile: [],
                wirelessFile: [],
                batteryFile: [],
            }

        })
        const subApRegional = p2m.currentAp.appliedSkuCode.map(item => {
            return {
                skuCode: item,
                productImg: [],
                certificateFile: [],
                patentFile: [],
                otherFile: [],
            }
        })
        const subApInsurance = p2m.currentAp.appliedSkuCode.map(item => {
            return {
                skuCode: item,
                supplierInsuranceFile: [{
                    name: '',
                    insuredProductName: '',
                    insuredRegions: [],
                    insuredValidDate: new Date(),
                    agreement: ''
                }]
            }
        })
        const subApShipping = p2m.currentAp.appliedSkuCode.map(item => {
            return {
                skuCode: item,
                netWidth: '',
                netHeight: '',
                netLength: '',
                netWeight: '',
                includePackageWidth: '',
                includePackageHeight: '',
                includePackageLength: '',
                includePackageWeight: '',
                fcaPrice: ''
            }
        })

        const currentAp = {
            name: p2m.currentAp.selectedProduct,
            type: p2m.currentApType.value,
            // status: p2m.p2mSt.get(0),  move to backend
            supplierId: supplierId,
            appliedDate: '',
            approvedDate: '',
            rejectedDate: '',
            // cancelledDate: '',
            returnTimes: 0,
            selectedProduct: p2m.currentAp.selectedProduct,
            selectedCountry: p2m.currentAp.selectedCountry,
            selectedPlatform: p2m.currentAp.selectedPlatform,
            brandNameEN: bp.brandNameEN,
            productNameEN: p2m.currentAp.selectedProduct,
            bpId: bp._id.$oid,
            appliedSkuCode: p2m.currentAp.appliedSkuCode,
            // appliedSku: bp.skus, //this name may lead to misunderstanding
            // skus: bp.skus, //no need to store this
            // to do jinny need to refactor
            // country: p2m.currentAp.selectedCountry,
            // platform: p2m.currentAp.selectedPlatform,
            blacklist: blacklist,
            subApplication: {
                marketPlaceInfo: {
                    type: 'MarketPlace Information',
                    name: `${p2m.currentAp.selectedProduct}-MarketPlace-Information`,
                    main: mainMarketPlaceInfo,
                    appliedSku: subApMarketPlaceInfo,
                    advanced: advancedMarketPlaceInfo,
                    competitorInfo: '',
                    comment: {
                        competitorInfo: ''
                    },
                    status: 'Draft'
                },
                insurance: {
                    type: 'Insurance',
                    name: `${p2m.currentAp.selectedProduct}-Insurance`,
                    appliedSku: subApInsurance,
                    hasInsured: 'no',
                    inquiryForm: [{name: 'Choose a file'}],
                    quotationFromDrs: [{name: 'Choose a file'}],
                    signedQuotation: [{name: 'Choose a file'}],
                    insuranceFile: [{
                        name: 'Choose a file',
                        appliedSku: [],
                        insuredProductName: '',
                        insuredRegions: [],
                        insuredValidDate: new Date(),
                        agreeOfAdditionalInsured: false,
                        agreeOfAdditionalFee: false
                    }],
                    updatedInsuranceFile: [{
                        name: 'Choose a file',
                        appliedSku: [],
                        insuredProductName: '',
                        insuredRegions: [],
                        insuredValidDate: new Date(),
                        agreeOfAdditionalInsured: false,
                        agreeOfAdditionalFee: false
                    }],
                    reviewOfInsurance: "",
                    caseTypeOfInsure: "",
                    process: 'ph-1',
                    steps: [
                        {
                            name: "insurance.step1",
                            state: 'active'
                        },{
                            name: "insurance.step2",
                            state: ''
                        },{
                            name: "insurance.step3",
                            state: ''
                        },{
                            name: "insurance.step4",
                            state: ''
                        },{
                            name: "insurance.step5",
                            state: ''
                        },{
                            name: "insurance.step6",
                            state: ''
                        },{
                            name: "insurance.step7",
                            state: ''
                        }
                    ]
                },
                regional: {
                    type: 'Regional',
                    name: `${p2m.currentAp.selectedProduct}-Regional`,
                    appliedSku: subApRegional,
                    otherFile: [{
                        name: 'Choose a file',
                        appliedSku: [],
                        description: ''
                    }],
                    patentFile: [{
                        name: 'Choose a file',
                        appliedSku: [],
                        modelNumber: '',
                        documentType: '',
                        documentValidDate: new Date(),
                        certificationBody: '',
                        complianceType: ''
                    }],
                    certificateFile: [{
                        name: 'Choose a file',
                        appliedSku: [],
                        modelNumber: '',
                        documentType: '',
                        documentValidDate: new Date(),
                        certificationBody: '',
                        complianceType: ''
                    }],
                    comment: {
                        otherFile: '',
                        patentFile: '',
                        certificateFile: '',
                        productImg: ''
                    },
                    status: 'Draft'
                },
                shipping: {
                    type: 'Shipping',
                    name: `${p2m.currentAp.selectedProduct}-Shipping`,
                    appliedSku: subApShipping,
                    checkReturn: false,
                    haslocalWarehouse: 'yes',
                    availableHaslocalWarehouse: 'yes',
                    psAllSkuApplied: 'yes',
                    pnwAllSkuApplied: 'yes',
                    ppsAllSkuApplied: 'yes',
                    ppwAllSkuApplied: 'yes',
                    allIncludePackageWeight: '',
                    allIncludePackageHeight: '',
                    allIncludePackageLength: '',
                    allIncludePackageWidth: '',
                    allNetWeight: '',
                    allNetLength: '',
                    allNetWidth: '',
                    allNetHeight: '',
                    returnAddr: {
                        country: '',
                        city: '',
                        stateorprovince: '',
                        ziporpostalcode: '',
                        address1: '',
                        address2: '',
                        fullname: '',
                        phonenumber: '',
                        emailaddress: ''
                    },
                    availableReturnAddr: {
                        availableCountry: '',
                        availableCity: '',
                        availableStateorprovince: '',
                        availableZiporpostalcode: '',
                        availableAddress1: '',
                        availableAddress2: '',
                        availableFullname: '',
                        availablePhonenumber: '',
                        availableEmailaddress: ''
                    },
                    comment: {
                        shippingMethod: '',
                        shippingInfo: ''
                    },
                    status: 'Draft'
                },
                productInfo: {
                    type: 'Product Advanced Information',
                    name: `${p2m.currentAp.selectedProduct}-Product-Advanced-Information`,
                    appliedSku: subApProductInfo,
                    ingredient: '',
                    urlAllSkuApplied: 'yes',
                    startDateAllSkuApplied: 'yes',
                    manufactureDaysAllSkuApplied: 'yes',
                    manufacturePlaceAllSkuApplied: 'yes',
                    modelNumberAllSkuApplied: 'yes',
                    url: '',
                    startDate: undefined,
                    manufactureDays: '',
                    manufacturePlace: '',
                    modelNumber: '',
                    packageFile: [{ name: 'Choose a file', appliedSku: [] }],
                    manualFile: [{ name: 'Choose a file', appliedSku: [] }],
                    ingredientFile: [{ name: 'Choose a file', appliedSku: [] }],
                    woodenFile: [{ name: 'Choose a file', appliedSku: [] }],
                    wirelessFile: [{ name: 'Choose a file', appliedSku: [] }],
                    batteryFile: [{ name: 'Choose a file', appliedSku: [],batteryCategory: ''}],
                    isWooden: 'no',
                    isBattery: 'no',
                    isWireless: 'no',
                    comment: {
                        url:'',
                        startDate: '',
                        manufactureDays: '',
                        manufacturePlace: '',
                        modelNumber: '',
                        productId: '',
                        packageImg: '',
                        packageFile: '',
                        manualImg: '',
                        manualFile: '',
                        woodenFile: '',
                        wirelessFile: '',
                        batteryFile: '',
                        hsCode: '',
                        ingredient: ''
                    },
                    status: 'Draft'
                }
            }
        }

        dispatch(createP2MApplication(supplierId, currentAp, message))
        dispatch(hideCreateP2MModal())
        props.onClose(false)
    }

    const handleClear = () => {
        dispatch(updateModalState())
    }

    const [p2mSkuOptions, setP2MSkuOptions] = useState([])
    const [changeApplicationId, setChangeApplicationId] = useState('')

    const handleApChange = (target) => {
        const id = target.value.$oid
        setChangeApplicationId(id)
        const arr = p2m.applications.filter(item => item._id.$oid == id)
        const options = arr[0].appliedSkuCode.map(item => {
            return { value: item , label: item }
        })
        setP2MSkuOptions(options)
        // pass id
        dispatch(selectAp(target))


    }

    const handleRemovalSkuChange = (target) => {
        console.log(target , 'handleRemovalSku')
        if (target) {
            return dispatch(selectRemovalSku(target))
        }
        dispatch(selectRemovalSku([]))
    }
    const handleValid = () =>　{
        if ( checkBlacklist() == false || p2m.currentAp.selectedProduct == '' || p2m.currentAp.selectedCountry == 'createp2mapplication.select' || p2m.currentAp.selectedPlatform == 'createp2mapplication.select' || displayedSkuCode.length == 0) {
            setShowValidWarning(true)
        } else {
            setShowValidWarning(false)
            p2m.currentApType.value == 'new' ? checkBlacklist() ? handleCreateApplication(): null  : null
            handleClear()
        }
    }

    const handleFocusCountry = () => {
        if (p2m.currentAp.selectedCountry == 'createp2mapplication.select'){
            setShowPlatformWarning(true)
        } else {
            setShowPlatformWarning(false)
        }
    }

    const handleFocusProduct = () => {
        if (p2m.currentAp.selectedProduct == ''){
            setShowSkuWarning(true)
        } else {
            setShowSkuWarning(false)
        }
    }


    return (

        <div id="createP2MApplication" className="drs-modal-container">
            <div className="drs-modal" style={{display: 'flex', justifyContent: 'center'}}>
                <div className="app-sub-pannel" style={{margin: '0', width: '50%'}}>
                    <div className="modal-close-wrapper">
                        <button className="modal-close-btn" onClick={() => {props.onClose(false); handleClear()}}>
                            <img src={`/${closeIcon}`} alt="closeIcon" style={{width: '14px'}}/>
                            <span>
                                <FormattedMessage id="createp2mapplication.close"/>
                            </span>
                        </button>
                    </div>
                    <div className="drs-modal-action-board-wrapper">
                        <div className="drs-modal-action-board">

                            <div style={{marginBottom: '1%'}}>
                                <p className="action-board-title">
                                    <FormattedMessage id="createp2mapplication.create"/>
                                </p>
                            </div>

                            <div className="action-board-input-wrapper">
                                <div className="action-board-input-label-wrapper" style={{display: 'flex'}}>
                                    <p className="action-board-input-label" style={{marginTop: '8px'}}>
                                        <FormattedMessage id="createp2mapplication.product"/>
                                    </p>
                                    { p2m.currentAp.selectedProduct == ''
                                        ?   <p className="base-input-notice drs-blue" style={{marginLeft: '10px'}}>
                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                <FormattedMessage id="selectField.required"/>
                                            </p>
                                        :   null
                                    }
                                </div>

                                <div className="base-input-select-wrapper">
                                    <FormattedMessage id="createp2mapplication.select">
                                        { placeholder =>
                                            <Select
                                                className="base-selector"
                                                styles={customStyles}
                                                value={p2m.currentAp.selectedProduct}
                                                placeholder={p2m.currentAp.selectedProduct == '' ? placeholder: p2m.currentAp.selectedProduct}
                                                onChange={handleProductChange}
                                                options={p2m.productOptions}
                                                classNamePrefix='mySelector'
                                            />
                                        }
                                    </FormattedMessage>
                                </div>
                            </div>

                            <div className="action-board-input-wrapper">
                                <div className="action-board-input-label-wrapper" style={{display: 'flex'}}>
                                    <p className="action-board-input-label" style={{marginTop: '8px'}}>
                                        <FormattedMessage id="createp2mapplication.applicationtype"/>
                                    </p>
                                    { p2m.currentApType.label == 'createp2mapplication.select'
                                        ?   <p className="base-input-notice drs-blue" style={{marginLeft: '10px'}}>
                                                <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                <FormattedMessage id="selectField.required"/>
                                            </p>
                                        :   null
                                    }
                                </div>
                                <div className="base-input-select-wrapper">
                                    <Select
                                        className="base-selector"
                                        styles={customStyles}
                                        value={currentApType}
                                        placeholder={currentApType}
                                        onChange={handleApplicationTypeChange}
                                        options={applicationTypeOptions}
                                        classNamePrefix='mySelector'
                                    />
                                </div>
                            </div>
                            <div className="section-line" style={{margin: '12px 0'}}></div>

                            { p2m.currentApType.value == 'removal'
                                ?   <div className="action-board-section-wrapper">
                                        <p className="action-board-section-title">
                                            <FormattedMessage id="createp2mapplication.process"/>
                                        </p>
                                        <div className="process-description-wrapper">
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <SelectAllIcon style={{color: '#053763',fontSize: '28px'}}/>
                                                </div>
                                                <p className="process-description-text">
                                                    <strong>
                                                        <FormattedMessage id="createp2mapplication.Step1"/>
                                                    </strong>
                                                    {/* <FormattedMessage id="createp2mapplication.paragraph1"/> */}
                                                    請先前往申請管理頁面，找到您欲申請下架的產品與市場地區先前申請通過時的申請案編號
                                                </p>
                                            </div>
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <ListAltIcon style={{fontSize: '28px',color: '#053763' }}/>
                                                </div>
                                                <p className="process-description-text">
                                                    <strong>
                                                        <FormattedMessage id="createp2mapplication.Step2"/>
                                                    </strong>
                                                    {/* <FormattedMessage id="createp2mapplication.paragraph2"/> */}
                                                    選擇欲下架的產品，請詳閱產品下架貨物處理說明，勾選已詳閱並同意後，再遞交申請
                                                </p>
                                            </div>
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <AssignmentTurnedInIcon style={{fontSize: '28px',color: '#053763' }}/>
                                                </div>
                                                <p className="process-description-text">
                                                    <strong>
                                                        <FormattedMessage id="createp2mapplication.Step3"/>
                                                    </strong>
                                                    {/* <FormattedMessage id="createp2mapplication.paragraph3"/> */}
                                                    DRS 將檢視您的申請並為您處理產品下架
                                                </p>
                                            </div>

                                        </div>
                                    </div>
                                :   null
                            }
                            { p2m.currentApType.value == 'removal'
                                ?   <div className="action-board-section-wrapper">
                                        <p className="action-board-section-title">
                                            請選擇欲申請下架產品所屬之申請案編號後，選擇欲下架之產品？
                                        </p>

                                        <div className="action-board-input-wrapper">
                                            <div className="action-board-input-label-wrapper">
                                                <p className="action-board-input-label" style={{marginTop: '8px'}}>
                                                    申請案編號
                                                </p>
                                            </div>
                                            <FormattedMessage id="createp2mapplication.select">
                                                { placeholder =>
                                                    <Select
                                                        className="base-selector"
                                                        styles={customStyles}
                                                        value={p2m.currentAp.name}
                                                        placeholder={p2m.currentAp.name == '' ? placeholder: p2m.currentAp.name}
                                                        onChange={handleApChange}
                                                        options={apOptions}
                                                        classNamePrefix='mySelector'
                                                    />
                                                }
                                            </FormattedMessage>

                                        </div>
                                        <div className="action-board-input-wrapper">
                                            <div className="action-board-input-label-wrapper">
                                                <p className="action-board-input-label" style={{marginTop: '8px'}}>
                                                    欲下架產品
                                                </p>
                                            </div>
                                            <FormattedMessage id="createp2mapplication.select">
                                                { placeholder =>
                                                    <Select
                                                        className="base-selector"
                                                        styles={customStyles}
                                                        value={p2m.currentAp.removalSku}
                                                        placeholder={p2m.currentAp.removalSku.length == 0 ? placeholder: p2m.currentAp.removalSku}
                                                        onChange={handleRemovalSkuChange}
                                                        options={p2mSkuOptions}
                                                        classNamePrefix='mySelector'
                                                        isMulti
                                                    />
                                                }
                                            </FormattedMessage>

                                        </div>
                                    </div>
                                :   null
                            }

                            { p2m.currentApType.value == 'update'
                                ?   <div className="action-board-section-wrapper">
                                        <p className="action-board-section-title">
                                            <FormattedMessage id="createp2mapplication.process"/>
                                        </p>
                                        <div className="process-description-wrapper">
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <SelectAllIcon style={{color: '#053763',fontSize: '28px'}}/>
                                                </div>
                                                <p className="process-description-text">
                                                    <strong>
                                                        <FormattedMessage id="createp2mapplication.Step1"/>
                                                    </strong>
                                                    {/* <FormattedMessage id="createp2mapplication.paragraph1"/> */}
                                                    請先前往申請管理頁面，找到您欲申請變更的產品與市場地區先前申請通過時的申請案編號
                                                </p>
                                            </div>
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <ListAltIcon style={{fontSize: '28px',color: '#053763' }}/>
                                                </div>
                                                <p className="process-description-text">
                                                    <strong>
                                                        <FormattedMessage id="createp2mapplication.Step2"/>
                                                    </strong>
                                                    {/* <FormattedMessage id="createp2mapplication.paragraph2"/> */}
                                                    建立申請更新案後，於各表單編輯更新需變更處，再遞交申請
                                                </p>
                                            </div>
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <AssignmentTurnedInIcon style={{fontSize: '28px',color: '#053763' }}/>
                                                </div>
                                                <p className="process-description-text">
                                                    <strong>
                                                        <FormattedMessage id="createp2mapplication.Step3"/>
                                                    </strong>
                                                    {/* <FormattedMessage id="createp2mapplication.paragraph3"/> */}
                                                    DRS 將審查更新內容並回覆結果
                                                </p>
                                            </div>

                                        </div>
                                    </div>
                                :   null
                            }
                            {p2m.currentApType.value == 'update'
                                ?   <div className="action-board-section-wrapper">
                                        <p className="action-board-section-title">
                                            請選擇欲申請更新之申請案編號？
                                        </p>
                                        {/* <div className="action-board-input-wrapper">
                                            <div className="action-board-input-label-wrapper">
                                                <p className="action-board-input-label" style={{marginTop: '8px'}}>
                                                    表單類型
                                                </p>
                                            </div>
                                            <div className="base-input-select-wrapper">
                                                <Select
                                                    className="base-selector"
                                                    styles={customStyles}
                                                    value={currentSubApType}
                                                    placeholder={currentSubApType}
                                                    onChange={handleSubApTypeChange}
                                                    options={subApTypeOptions}
                                                    classNamePrefix='mySelector'
                                                />
                                            </div>
                                        </div>
                                        //todo athur update applicatoin
                                        */}
                                        <div className="action-board-input-wrapper">
                                            <div className="action-board-input-label-wrapper">
                                                <p className="action-board-input-label" style={{marginTop: '8px'}}>
                                                    申請案編號
                                                </p>
                                            </div>
                                            <FormattedMessage id="createp2mapplication.select">
                                                { placeholder =>
                                                    <Select
                                                        className="base-selector"
                                                        styles={customStyles}
                                                        value={p2m.currentAp.name}
                                                        placeholder={p2m.currentAp.name == '' ? placeholder: p2m.currentAp.name}
                                                        onChange={handleApChange}
                                                        options={apOptions}
                                                        classNamePrefix='mySelector'
                                                    />
                                                }
                                            </FormattedMessage>
                                        </div>

                                    </div>
                                :   null
                            }

                            { p2m.currentApType.value == 'new'
                                ?   <div className="action-board-section-wrapper">
                                        <p className="action-board-section-title">
                                            <FormattedMessage id="createp2mapplication.process"/>
                                        </p>
                                        <div className="process-description-wrapper">
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <PublicIcon style={{fontSize: '28px',color: '#053763' }} />
                                                </div>
                                                <p className="process-description-text">
                                                    <strong><FormattedMessage id="createp2mapplication.Step1"/></strong>
                                                    <FormattedMessage id="createp2mapplication.paragraph1"/>
                                                </p>
                                            </div>
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <ListAltIcon style={{fontSize: '28px',color: '#053763' }}/>
                                                </div>
                                                <p className="process-description-text">
                                                    <strong><FormattedMessage id="createp2mapplication.Step2"/></strong>
                                                    <FormattedMessage id="createp2mapplication.paragraph2"/>
                                                </p>
                                            </div>
                                            <div className="process-description-circle-section">
                                                <div className="process-description-circle">
                                                    <AssignmentTurnedInIcon style={{fontSize: '28px',color: '#053763' }}/>
                                                </div>
                                                <p className="process-description-text">
                                                    <strong><FormattedMessage id="createp2mapplication.Step3"/></strong>
                                                    <FormattedMessage id="createp2mapplication.paragraph3"/>
                                                </p>
                                            </div>

                                        </div>
                                    </div>
                                :   null
                            }

                            { p2m.currentApType.value == 'new'
                                ?   <div className="action-board-section-wrapper">
                                        <p className="action-board-section-title">
                                            <FormattedMessage id="createp2mapplication.paragraph4"/>
                                        </p>
                                        <div className="action-board-input-wrapper">
                                            <div className="action-board-input-label-wrapper" style={{display: 'flex'}}>
                                                <p className="action-board-input-label" style={{marginTop: '8px'}}>
                                                    <FormattedMessage id="createp2mapplication.country"/>
                                                </p>
                                                { p2m.currentAp.selectedCountry == 'createp2mapplication.select'
                                                    ?   <p className="base-input-notice drs-blue" style={{marginLeft: '10px'}}>
                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                            <FormattedMessage id="selectField.required"/>
                                                        </p>
                                                    :   null
                                                }
                                            </div>
                                            <div className="base-input-select-wrapper">
                                                <Select
                                                    className="base-selector"
                                                    styles={customStyles}
                                                    value={currentCountry}
                                                    placeholder={currentCountry}
                                                    onChange={handleCountryChange}
                                                    options={countryOptions}
                                                    classNamePrefix='mySelector'
                                                />
                                            </div>
                                        </div>
                                        <div className="action-board-input-wrapper">
                                            <div className="action-board-input-label-wrapper" style={{display: 'flex'}}>
                                                <p className="action-board-input-label" style={{marginTop: '8px'}}>
                                                    <FormattedMessage id="createp2mapplication.platform"/>
                                                </p>
                                                { p2m.currentAp.selectedPlatform == 'createp2mapplication.select'
                                                    ?   <p className="base-input-notice drs-blue" style={{marginLeft: '10px'}}>
                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                            <FormattedMessage id="selectField.required"/>
                                                        </p>
                                                    :   null
                                                }
                                                {showPlatformWarning
                                                    ?   <p className="base-input-notice drs-notice-red">
                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                                                            <FormattedMessage id="createp2mapplication.platformwarning"/>
                                                        </p>
                                                    :   null
                                                }
                                            </div>
                                            <div className="base-input-select-wrapper">
                                                <Select
                                                    className="base-selector"
                                                    styles={customStyles}
                                                    value={currentPlatform}
                                                    placeholder={currentPlatform}
                                                    onFocus={handleFocusCountry}
                                                    onChange={handlePlatformChange}
                                                    options={p2m.platformOptions}
                                                    classNamePrefix='mySelector'
                                                />
                                            </div>
                                        </div>

                                        <div className="action-board-input-wrapper" style={{height: 'fit-content'}}>
                                            <div className="action-board-input-label-wrapper" style={{display: 'flex'}}>
                                                <p className="action-board-input-label" style={{marginTop: '8px'}}>SKUs
                                                    <FormattedMessage id="createp2mapplication.multiplechoice"/>
                                                </p>
                                                { displayedSkuCode.length == 0
                                                    ?   <p className="base-input-notice drs-blue" style={{marginLeft: '10px'}}>
                                                            <i class="fa fa-exclamation-circle" aria-hidden="true"></i>
                                                            <FormattedMessage id="selectField.required"/>
                                                        </p>
                                                    :   null
                                                }
                                                {showSkuWarning
                                                    ?   <p className="base-input-notice drs-notice-red">
                                                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                                                            <FormattedMessage id="createp2mapplication.skuwarning"/>
                                                        </p>
                                                    :   null
                                                }
                                            </div>
                                            <div className="base-input-select-wrapper">
                                                <FormattedMessage id="productinfo.select">
                                                    { placeholder =>
                                                        <Select
                                                            className="base-selector"
                                                            styles={customStyles}
                                                            value={displayedSkuCode}
                                                            placeholder={displayedSkuCode.length == 0 ? placeholder: displayedSkuCode}
                                                            onFocus={handleFocusProduct}
                                                            onChange={handleSkuChange}
                                                            options={options}
                                                            classNamePrefix='mySelector'
                                                            isMulti
                                                        />
                                                    }
                                                </FormattedMessage>
                                            </div>
                                            <div className="action-board-section-wrapper" style={{width: '123%'}}>
                                                <p className="base-input-notice drs-notice-red">
                                                    <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                                                    <FormattedMessage id="createp2mapplication.paragraph5"/>
                                                </p>
                                                <div className="check-list-wrapper">
                                                    {blacklist.map((item, index) => {
                                                        if (index == 8 || index == 9) {
                                                            return (
                                                                <div>
                                                                    <div className="check-list-li">
                                                                        <CheckBox
                                                                            isSelected={item.isChecked}
                                                                            onSelect={onCheckBlacklist}
                                                                            index={index}
                                                                        />
                                                                        <p className="check-list-lable" style={{maxWidth: '90%'}}>
                                                                            <FormattedMessage id={item.value}/>
                                                                        </p>
                                                                    </div>
                                                                    <ul style={{padding:'0 42px',margin:'9px 0'}}>
                                                                        {index == 8
                                                                            ?   <li style={{fontSize: '14px',lineHeight: '1.2rem',fontWeight: '500'}}>
                                                                                    {p2m.currentAp.selectedCountry == 'createp2mapplication.select'
                                                                                        ?   <a href="https://sellercentral.amazon.com/gp/help/external/200164330" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist9-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }
                                                                                    {p2m.currentAp.selectedCountry == 'US'
                                                                                        ?   <a href="https://sellercentral.amazon.com/gp/help/external/200164330" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist9-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'UK'
                                                                                        ?   <a href="https://sellercentral.amazon.co.uk/gp/help/external/201743940?language=en_GB" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist9-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'CA'
                                                                                        ?   <a href="https://sellercentral.amazon.ca/gp/help/external/G200301050" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist9-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'DE'
                                                                                        ?   <a href="https://sellercentral.amazon.de/gp/help/external/201743940?language=en_DE&ref=efph_201743940_cont_201190440" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist9-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'FR'
                                                                                        ?   <a href="https://sellercentral.amazon.fr/gp/help/external/201743940?language=en_FR&ref=efph_201743940_cont_201190440" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist9-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'IT'
                                                                                        ?   <a href="https://sellercentral.amazon.it/gp/help/external/201743940?language=en_IT&ref=efph_201743940_cont_201190440" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist9-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'ES'
                                                                                        ?   <a href="https://sellercentral.amazon.es/gp/help/external/201743940?language=en_ES&ref=efph_201743940_cont_201190440" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist9-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }
                                                                                </li>

                                                                            :   <li style={{fontSize: '14px',lineHeight: '1.2rem',fontWeight: '500'}}>
                                                                                    {p2m.currentAp.selectedCountry == 'createp2mapplication.select'
                                                                                        ?   <a href="https://sellercentral.amazon.com/gp/help/external/G201730840?language=en_US&ref=efph_G201730840_cont_200140860" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist10-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }
                                                                                    {p2m.currentAp.selectedCountry == 'US'
                                                                                        ?   <a href="https://sellercentral.amazon.com/gp/help/external/G201730840?language=en_US&ref=efph_G201730840_cont_200140860" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist10-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'UK'
                                                                                        ?   <a href="https://sellercentral.amazon.co.uk/gp/help/external/201730840?language=en_GB&ref=efph_201730840_cont_200140860" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist10-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'CA'
                                                                                        ?   <a href="https://sellercentral.amazon.ca/gp/help/external/201730840?language=en_CA&ref=efph_201730840_cont_G200140860" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist10-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'DE'
                                                                                        ?   <a href="https://sellercentral.amazon.de/gp/help/external/G201730840?language=en_DE&ref=efph_G201730840_cont_G200140860" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist10-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'FR'
                                                                                        ?   <a href="https://sellercentral.amazon.fr/gp/help/external/201730840?language=en_FR&ref=efph_201730840_cont_200140860" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist10-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'IT'
                                                                                        ?   <a href="https://sellercentral.amazon.it/gp/help/external/201730840?language=en_IT&ref=efph_201730840_cont_202121570" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist10-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }{p2m.currentAp.selectedCountry == 'ES'
                                                                                        ?   <a href="https://sellercentral.amazon.es/gp/help/external/201730840?language=en_ES&ref=efph_201730840_cont_200140860" target='_blank'>
                                                                                                <FormattedMessage id="createp2map.blacklist10-1"/>
                                                                                            </a>
                                                                                        :   null
                                                                                    }
                                                                                </li>
                                                                        }
                                                                        {index == 9
                                                                            ?    <li style={{fontSize: '14px',lineHeight: '1.2rem',fontWeight: '500'}}>
                                                                                     {p2m.currentAp.selectedCountry == 'createp2mapplication.select'
                                                                                         ?   <a href="https://sellercentral.amazon.com/gp/help/external/200140860?language=en_US&ref=efph_200140860_cont_200164330" target='_blank'>
                                                                                                 <FormattedMessage id="createp2map.blacklist10-2"/>
                                                                                             </a>
                                                                                         :   null
                                                                                     }
                                                                                     {p2m.currentAp.selectedCountry == 'US'
                                                                                         ?   <a href="https://sellercentral.amazon.com/gp/help/external/200140860?language=en_US&ref=efph_200140860_cont_200164330" target='_blank'>
                                                                                                 <FormattedMessage id="createp2map.blacklist10-2"/>
                                                                                             </a>

                                                                                         :   null
                                                                                     }{p2m.currentAp.selectedCountry == 'UK'
                                                                                         ?   <a href="https://sellercentral.amazon.co.uk/gp/help/external/200140860" target='_blank'>
                                                                                                 <FormattedMessage id="createp2map.blacklist10-2"/>
                                                                                             </a>
                                                                                         :   null
                                                                                     }{p2m.currentAp.selectedCountry == 'CA'
                                                                                         ?   <a href="https://sellercentral.amazon.ca/gp/help/external/G200140860" target='_blank'>
                                                                                                 <FormattedMessage id="createp2map.blacklist10-2"/>
                                                                                             </a>
                                                                                         :   null
                                                                                     }{p2m.currentAp.selectedCountry == 'DE'
                                                                                         ?   <a href="https://sellercentral.amazon.de/gp/help/external/G200140860?language=en_DE&ref=efph_G200140860_cont_201743940" target='_blank'>
                                                                                                 <FormattedMessage id="createp2map.blacklist10-2"/>
                                                                                             </a>
                                                                                         :   null
                                                                                     }{p2m.currentAp.selectedCountry == 'FR'
                                                                                         ?   <a href="https://sellercentral.amazon.fr/gp/help/external/help-page.html?itemID=200140860&language=en_FR&ref=efph_200140860_bred_201730840" target='_blank'>
                                                                                                 <FormattedMessage id="createp2map.blacklist10-2"/>
                                                                                             </a>
                                                                                         :   null
                                                                                     }{p2m.currentAp.selectedCountry == 'IT'
                                                                                         ?   <a href="https://sellercentral.amazon.it/gp/help/external/help-page.html?itemID=200140860&language=en_IT&ref=efph_200140860_bred_201730840" target='_blank'>
                                                                                                 <FormattedMessage id="createp2map.blacklist10-2"/>
                                                                                             </a>
                                                                                         :   null
                                                                                     }{p2m.currentAp.selectedCountry == 'ES'
                                                                                         ?   <a href="https://sellercentral.amazon.es/gp/help/external/help-page.html?itemID=200140860&language=en_ES&ref=efph_200140860_bred_201730840" target='_blank'>
                                                                                                 <FormattedMessage id="createp2map.blacklist10-2"/>
                                                                                             </a>
                                                                                         :   null
                                                                                     }
                                                                                 </li>
                                                                            :    null
                                                                        }
                                                                    </ul>
                                                                </div>
                                                            )
                                                        } else {
                                                            return (
                                                                <div className="check-list-li">
                                                                    <CheckBox
                                                                        isSelected={item.isChecked}
                                                                        onSelect={onCheckBlacklist}
                                                                        index={index}
                                                                    />
                                                                    <p className="check-list-lable">
                                                                        <FormattedMessage id={item.value}/>
                                                                    </p>
                                                                </div>
                                                            )
                                                        }

                                                    })}
                                                    <div className="check-list-li">
                                                        <CheckBox
                                                            isSelected={false}
                                                            onSelect={onCheckAllBlacklist}
                                                        />
                                                        <p className="check-list-lable" style={{fontSize: '15px',fontWeight: '600'}}>
                                                            全選
                                                        </p>
                                                    </div>
                                                </div>
                                                <div>
                                                    {showFirstWarning
                                                        ?   <div className="disclaimer-section">
                                                                <p className="disclaimer-text">
                                                                    <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                                                                    DRS 連結生產者與消費者，讓產品設計者直接獲取終端市場回饋，獲取比代工更高的長期利潤。您需要是產品的設計者或是品牌擁有者，
                                                                    才能透過 DRS 銷售產品。建議您重新挑選適合的商品，並建立於 DRS 平台後，再次遞交申請銷售。
                                                                </p>
                                                            </div>
                                                        :   null
                                                    }
                                                    {showSecondWarning
                                                        ?   <div className="disclaimer-section">
                                                                <p className="disclaimer-text">
                                                                    <i class="fa fa-exclamation-circle" style={{marginRight: '5px'}} aria-hidden="true"></i>
                                                                    DRS 致力打造對環境友善且能永續經營的供應鏈， 若為營利而間接傷害環境生態、失去企業社會責任，是我們不樂見的，因此我們希望商品必須符合以下條件：
                                                                </p>
                                                                <ul className="disclaimer-list">
                                                                    <li className="disclaimer-list-item">不含動物成份、並非經動物測試</li>
                                                                    <li className="disclaimer-list-item">非漁具、獵具、暴力傾向產品</li>
                                                                    <li className="disclaimer-list-item">非用於攝取菸、酒、禁藥、動物性食品</li>
                                                                    <li className="disclaimer-list-item">符合相關國家（包含生產地區與銷售地區）所有適用之法規及智慧財產法規</li>
                                                                    <li className="disclaimer-list-item">投保產品責任險</li>
                                                                </ul>
                                                                <p className="disclaimer-text"> 建議您重新挑選適合的商品，並建立於 DRS 平台後，再次遞交申請銷售。</p>
                                                            </div>
                                                        :   null
                                                    }
                                                    {showFirstWarning || showSecondWarning
                                                        ?   <div className="disclaimer-text-wrapper">
                                                                <p className="disclaimer-text">若上述聲明不符合您的產品描述，表示您的產品不屬於 DRS 服務範圍，但不表示產品不能在亞馬遜平台銷售，DRS 另有提供帳號代營運服務，詳見官方網站資訊、歡迎聯繫 DRS。</p>
                                                                {/* <div className="checkbox-inline-wrapper" style={{width: '100%', margin: '12px 0',paddingLeft: '2%'}}>
                                                                    <CheckBox
                                                                        isSelected={blacklistDisclaimerAgreement}
                                                                        onSelect={()=> onAgreeBlacklistDisclaimer()}
                                                                        disabled={!d.u.isSp}
                                                                    />
                                                                    <span className="checkbox-inline-text">
                                                                        我理解並接受以上說明
                                                                    </span>
                                                                </div> */}
                                                            </div>
                                                        :   null
                                                    }
                                                    {showValidWarning
                                                        ?   <div style={{display: 'flex', alignItems: 'center', flexDirection: 'column'}}>
                                                                <p style={{lineHeight: '1.5rem', margin: '6px 0', fontSize: '15px'}} className="drs-notice-red">
                                                                    <FormattedMessage id="createp2mapplication.warning"/>
                                                                </p>
                                                            </div>
                                                        :   null
                                                    }
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                :   null
                            }
                            
                        </div>
                        { p2m.currentApType.label == 'createp2mapplication.select'
                            ?   null
                            :   <div style={{margin: '20px 0'}}>
                                   <button
                                       className={`drs-btn drs-btn-normal ${disabledCreate ? 'disabled':''}`}
                                       onClick={() => {
                                           handleValid()
                                           p2m.currentApType.value == 'update' ? handleChangeApplication() : null
                                           p2m.currentApType.value == 'removal' ? alert('還在開發中') : null
                                            }}
                                       disabled={disabledCreate}
                                   >
                                       <FormattedMessage id="createp2mapplication.confirmestablishment"/>
                                   </button>
                               </div>
                        }

                    </div>
                    
                </div>
            </div>
        </div>
    )
}

export default injectIntl(CreateP2MApplication)