import {
    REQUEST_P2M_APPLICATIONS_SUCCESS,
    REQUEST_P2M_APPLICATIONS_FAILED,
    REQUEST_P2M_APPLICATIONS_PENDING,
    UPDATE_CURRENT_APPLICATION,
    UPDATE_SHOW_BREADCRUMBS,
    UPDATE_CURRENT_SUB_APPLICATION,
    CREATE_P2M_APPLICATION_PENDING,
    CREATE_P2M_APPLICATION_SUCCESS,
    CREATE_P2M_APPLICATION_FAILED,
    CHANGE_P2M_APPLICATION_FAILED,
    CHANGE_P2M_APPLICATION_PENDING,
    CHANGE_P2M_APPLICATION_SUCCESS,
    UPDATE_P2M_APPLICATION_FAILED,
    UPDATE_P2M_APPLICATION_PENDING,
    UPDATE_P2M_APPLICATION_SUCCESS,
    INIT_CREATE_P2M_APPLICATION,

    LOAD_P2M_MARKETPLACE_INFO_PENDING,
    LOAD_P2M_MARKETPLACE_INFO_SUCCESS,
    LOAD_P2M_MARKETPLACE_INFO_FAILED,
    
    LOAD_P2M_PRODUCT_INFO_PENDING,
    LOAD_P2M_PRODUCT_INFO_SUCCESS,
    LOAD_P2M_PRODUCT_INFO_FAILED,

    LOAD_P2M_INSURANCE_PENDING,
    LOAD_P2M_INSURANCE_SUCCESS,
    LOAD_P2M_INSURANCE_FAILED,

    LOAD_P2M_REGIONAL_PENDING,
    LOAD_P2M_REGIONAL_SUCCESS,
    LOAD_P2M_REGIONAL_FAILED,

    LOAD_P2M_SHIPPING_PENDING,
    LOAD_P2M_SHIPPING_SUCCESS,
    LOAD_P2M_SHIPPING_FAILED,

    SELECT_P2M_COUNTRY,

    SAVE_P2M_MARKETPLACE_INFO_PENDING,
    SAVE_P2M_MARKETPLACE_INFO_SUCCESS,
    SAVE_P2M_MARKETPLACE_INFO_FAILED,

    SAVE_P2M_REGIONAL_PENDING,
    SAVE_P2M_REGIONAL_SUCCESS,
    SAVE_P2M_REGIONAL_FAILED,

    SAVE_P2M_INSURANCE_PENDING,
    SAVE_P2M_INSURANCE_SUCCESS,
    SAVE_P2M_INSURANCE_FAILED,

    SAVE_P2M_PRODUCT_INFO_PENDING,
    SAVE_P2M_PRODUCT_INFO_SUCCESS,
    SAVE_P2M_PRODUCT_INFO_FAILED,

    SAVE_P2M_SHIPPING_PENDING,
    SAVE_P2M_SHIPPING_SUCCESS,
    SAVE_P2M_SHIPPING_FAILED,

    SELECT_P2M_APPLICATION_TYPE,
    SELECT_P2M_APPLICATION_PRODUCT,
    SELECT_P2M_APPLICATION_SKU,
    SELECT_P2M_APPLICATION_PLATFORM,

    SELECT_P2M_APPLICATION_NAME,
    SELECT_REMOVAL_SKU,
    CHANGE_P2M_APPLICATION_PLATFORM_OPTIONS,
    GET_EXACT_PRODUCT_FOR_AP_PENDING,
    GET_EXACT_PRODUCT_FOR_AP_SUCCESS,
    GET_EXACT_PRODUCT_FOR_AP_FAILED,
    UPDATE_REDIRECT_APPLICATION,
    UPDATE_APPLICATION,

    SUBMIT_APPLICATION_PENDING,
    SUBMIT_APPLICATION_SUCCESS,
    SUBMIT_APPLICATION_FAILED,

    APPLY_TO_REMOVE_PENDING,
    APPLY_TO_REMOVE_SUCCESS,
    APPLY_TO_REMOVE_FAILED,
    REJECT_TO_REMOVE_PENDING,
    REJECT_TO_REMOVE_SUCCESS,
    REJECT_TO_REMOVE_FAILED,
    APPROVE_TO_REMOVE_PENDING,
    APPROVE_TO_REMOVE_SUCCESS,
    APPROVE_TO_REMOVE_FAILED,

    REFILL_APPLICATION_PENDING,
    REFILL_APPLICATION_SUCCESS,
    REFILL_APPLICATION_FAILED,

    GET_EXACT_APPLICATION_PENDING,
    GET_EXACT_APPLICATION_SUCCESS,
    GET_EXACT_APPLICATION_FAILED,

    GET_EXACT_SUBAP_PENDING,
    GET_EXACT_SUBAP_SUCCESS,
    GET_EXACT_SUBAP_FAILED,

    REJECT_APPLICATION_PENDING,
    REJECT_APPLICATION_SUCCESS,
    REJECT_APPLICATION_FAILED,

    APPROVE_APPLICATION_PENDING,
    APPROVE_APPLICATION_SUCCESS,
    APPROVE_APPLICATION_FAILED,

    UPDATE_MODAL_STATE,
    HANDLE_SMOOTH_SCROLL,

    FETCH_P2MAP_COMMENT_PENDING,
    FETCH_P2MAP_COMMENT_SUCCESS,
    FETCH_P2MAP_COMMENT_FAILED,
    SHOW_CREATE_P2M_MODAL,
    HIDE_CREATE_P2M_MODAL,

    DELETE_AP_PENDING,
    DELETE_AP_SUCCESS,
    DELETE_AP_FAILED,

    CHANGE_SKUITEM_SHOW,
    CHANGE_MP_SKU_VARIATIONNAME_SHOW,
    CHANGE_MP_SKU_IMG_SHOW,
    CHANGE_MP_SKU_MAINTITLE_SHOW,
    CHANGE_MP_SKU_DES_SHOW,
    CHANGE_MP_SKU_FEATURE_SHOW,
    CHANGE_MP_SKU_KEYWORD_SHOW,
    UPDATE_MP,
    UPDATE_PAI,
    UPDATE_SH,
    UPDATE_INS,
    UPDATE_RE,
    UPDATE_SKU_OPTIONS,
    CHANGE_AP,
    GET_FILTERLIST_PENDING,
    GET_FILTERLIST_SUCCESS,
    GET_FILTERLIST_FAILED,
    CHANGE_FILTER_TARGET,
    CHANGE_FILTERED_APS
} from '../constants/action-types'

const applicationTypeOptions = [
    { value: 'new', 
      label: "managep2m.new"
    },
    { value: 'update',
      label: "managep2m.update"
    }
//     ,
//     { value: 'removal', 
//       label: "managep2m.removal"
//   },
]

const countryOptions = [
    { value: 'com',
      label: "US"
    },
    { value: 'co.uk',
      label: "UK"
    },
    { value: 'ca',
      label: "CA"
    },
    { value: 'de',
      label: "DE"
    },
    { value: 'fr',
      label: "FR"
    },
    { value: 'it',
      label: "IT"
    },
    { value: 'es',
      label: "ES"
    },
];

const subApTypeOptions = [
    {value: 'mr', label: 'subApplication.marketplaceinfo'},
    {value: 'in', label: 'subApplication.insurance'},
    {value: 're', label: 'subApplication.regional'},
    {value: 'sh', label: 'subApplication.shipping'},
    {value: 'pi', label: 'subApplication.productinfo'}
]

let p2mStatusMap = new Map()
p2mStatusMap.set(0, 'Pending')
    .set(1, 'In Review')
    .set(2, 'Rejected')
    .set(3, 'Approved')
    .set(4, 'Removal in Review')
    .set(5, 'Removed')

const p2m = {
    stMap: p2mStatusMap,
    isPending: false,
    redirectTo: '',
    redirect: '',
    result: '',
    error: '',
    filterList: [],
    filterTarget: {name:'', country: '', status: '', kcode: '', product: ''},
    applications: [],
    pageIndex: 0,
    totalPages:0,
    skuOptions: [],
    skuItemShow: [],
    showSkuVariationName: [],
    showSkuImg: [],
    showSkuMainTitle: [],
    showSkuDescription: [],
    showSkuFeature: [],
    showSkuKeyword: [],
    currentApType: {label: "createp2mapplication.select"},
    applicationTypeOptions: applicationTypeOptions,
    countryOptions: countryOptions,
    productOptions: [],
    platformOptions: [],
    skuOptions: [],
    subApTypeOptions: subApTypeOptions,
    currentAp: {
        name:'',
        type: '',
        status: '',
        version: 0,
        _id: {$oid: ''},
        serial_num: '',
        supplierId: '',
        p2mApplicationId: {},
        createdBy: '',
        createdDateTime: '',
        updateBy: '',
        updateDateTime: '',
        appliedDate: '',
        approvedDate: '',
        rejectedDate: '',
        // cancelledDate: '',
        returnTimes: 0,
        selectedCountry: 'createp2mapplication.select',        
        selectedPlatform: "createp2mapplication.select",
        selectedSubApType: 'createp2mapplication.select',
        selectedProductId: '',
        selectedProduct: '',
        brandNameEN: '',
        productNameEN: '',
        manufacturerEN: '',
        bpId: '',
        appliedSkuCode: [],
        removalSku: [],
        // to do jinny need to refactor
        // country: '',
        // platform: '',
        blacklist: [],
        subApplication: {
            marketPlaceInfo: {
                type: 'MarketPlace Information',
                name: '',
                main: {
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
                },
                appliedSku: [{
                    sellerSku: '',
                    productId: {name: 'Product ID', value: ''},
                    productIdType: {name: 'Product ID Type', value: ''},
                    variable: {name: 'Variable', value: ''},
                    variationTheme: {name: 'Variation Theme', value: ''},
                    skuCode: '',
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
                }],
                advanced: {
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
                },
                competitorInfo: '',
                comment: {
                    competitorInfo: ''
                },
                status: 'Draft'
            },
            insurance: {
                type: 'Insurance',
                name: '',
                appliedSku: [{
                    skuCode: '',
                    supplierInsuranceFile: [{
                        name: '',
                        insuredProductName: '',
                        insuredRegions: [],
                        insuredValidDate: new Date(),
                        agreement: ''
                    }]
                }],
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
                returnStat :{
                    returnStatus : false,
                    returnMsg : ''
                },
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
                name: '',
                appliedSku: [{
                    skuCode: '',
                    productImg: [],
                    certificateFile: [],
                    patentFile: [],
                    otherFile: [],
                }],
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
                name: '',
                appliedSku: [{
                    skuCode: '',
                    netWidth: '',
                    netHeight: '',
                    netLength: '',
                    netWeight: '',
                    includePackageWidth: '',
                    includePackageHeight: '',
                    includePackageLength: '',
                    includePackageWeight: '',
                    fcaPrice: ''
                }],
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
                name: '',
                appliedSku: [{
                    sellerSku: '',
                    productId: {name: 'Product ID', value: ''},
                    productIdType: {name: 'Product ID Type', value: ''},
                    productIdByDrs: {name: 'Product ID By Drs', value: ''},
                    variable: {name: 'Variable', value: ''},
                    variationTheme: {name: 'Variation Theme', value: ''},
                    skuCode: '',
                    url: '',
                    startDate: new Date(),
                    manufactureDays: '',
                    manufacturePlace: '',
                    suggestedPricingNoTax: '',
                    suggestedPricingTax: '',
                    suggestedRetailPriceNoTax: '',
                    suggestedRetailPriceTax: '',
                    ddpUnitPrice: '',
                    modelNumber : '',
                    packageFile: [],
                    packageImg: [],
                    manualFile: [],
                    manualImg: [],
                    exportSideHsCode: '',
                    salesSideHsCode: '',
                    ingredient:'',
                    ingredientFile: [],
                    woodenFile: [],
                    wirelessFile: [],
                    batteryFile: [],
                }],
                urlAllSkuApplied: 'yes',
                startDateAllSkuApplied: 'yes',
                manufactureDaysAllSkuApplied: 'yes',
                manufacturePlaceAllSkuApplied: 'yes',
                modelNumberAllSkuApplied : 'yes',
                url: '',
                startDate: new Date(),
                manufactureDays: '',
                manufacturePlace: '',
                modelNumber : '',
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
                    modelNumber : '',
                    productId: '',
                    packageImg: '',
                    packageFile: '',
                    manualImg: '',
                    manualFile: '',
                    ingredientFile: '',
                    woodenFile: '',
                    wirelessFile: '',
                    batteryFile: '',
                    hsCode: '',
                    ingredient: ''
                },
                status: 'Draft'
            }
        },
    },
    currentSubAp: {},
    mp: {
        type: 'MarketPlace Information',
        name: '',
        main: {
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
        },
        appliedSku: [{
            sellerSku: '',
            productId: {name: 'Product ID', value: ''},
            productIdType: {name: 'Product ID Type', value: ''},
            variable: {name: 'Variable', value: ''},
            variationTheme: {name: 'Variation Theme', value: ''},
            skuCode: '',
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
        }],
        advanced: {
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
        },
        competitorInfo: '',
        comment: {
            competitorInfo: ''
        },
        status: 'Draft'
    },
    ins: {
        type: 'Insurance',
        name: '',
        appliedSku: [{
            skuCode: '',
            supplierInsuranceFile: [{
                name: '',
                insuredProductName: '',
                insuredRegions: [],
                insuredValidDate: new Date(),
                agreement: ''
            }]
        }],
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
        returnStat :{
            returnStatus : false,
            returnMsg : ''
        },
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
    re: {
        type: 'Regional',
        name: '',
        appliedSku: [{
            sellerSku: '',
            productId: {name: 'Product ID', value: ''},
            productIdType: {name: 'Product ID Type', value: ''},
            variable: {name: 'Variable', value: ''},
            variationTheme: {name: 'Variation Theme', value: ''},
            skuCode: '',
            productImg: [],
            certificateFile: [],
            patentFile: [],
            otherFile: [],
        }],
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
    sh: {
        type: 'Shipping',
        name: '',
        appliedSku: [{
            skuCode: '',
            netWidth: '',
            netHeight: '',
            netLength: '',
            netWeight: '',
            includePackageWidth: '',
            includePackageHeight: '',
            includePackageLength: '',
            includePackageWeight: '',
            fcaPrice: ''
        }],
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
    pai: {
        type: 'Product Advanced Information',
        name: '',
        appliedSku: [{
            sellerSku: '',
            productId: {name: 'Product ID', value: ''},
            productIdType: {name: 'Product ID Type', value: ''},
            productIdByDrs: {name: 'Product ID By Drs', value: ''},
            variable: {name: 'Variable', value: ''},
            variationTheme: {name: 'Variation Theme', value: ''},
            skuCode: '',
            url: '',
            startDate: undefined,
            manufactureDays: '',
            manufacturePlace: '',
            suggestedPricingNoTax: '',
            suggestedPricingTax: '',
            suggestedRetailPriceNoTax: '',
            suggestedRetailPriceTax: '',
            ddpUnitPrice: '',
            modelNumber : '',
            packageFile: [],
            packageImg: [],
            manualFile: [],
            manualImg: [],
            exportSideHsCode: '',
            salesSideHsCode: '',
            ingredient:'',
            ingredientFile: [],
            woodenFile: [],
            wirelessFile: [],
            batteryFile: [],
        }],
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
        modelNumber : '',
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
            modelNumber : '',
            productId: '',
            packageImg: '',
            packageFile: '',
            manualImg: '',
            manualFile: '',
            ingredientFile: '',
            woodenFile: '',
            wirelessFile: '',
            batteryFile: '',
            hsCode: '',
            ingredient: ''
        },
        status: 'Draft'
    },
    smoothScroll: [false, ''],
    allComments: {},
    showCreateModal: false
}

export const manageP2MReducer = (state = p2m, action) => {
    switch (action.type) {
        case CHANGE_FILTERED_APS:
            return Object.assign({}, state, {
                applications: action.payload.filteredAps
            })
        case CHANGE_FILTER_TARGET:
            return Object.assign({}, state, {
                filterTarget: action.payload.filterTarget
            })
        case CHANGE_AP:
            return Object.assign({}, state, {
                currentAp: Object.assign({}, state.currentAp, {
                    _id: action.payload.p2mId,
                }),
                redirectTo: action.payload.redirectTo
            })
        case UPDATE_SKU_OPTIONS:
            return Object.assign({}, state, {
                skuOptions: action.payload.skuOptions
            })
        case CHANGE_SKUITEM_SHOW:
            return Object.assign({}, state, {
                skuItemShow: action.payload.skuItemShow
            })
        case CHANGE_MP_SKU_VARIATIONNAME_SHOW:
            return Object.assign({}, state, {
                showSkuVariationName: action.payload.showSkuVariationName,
            })
        case CHANGE_MP_SKU_IMG_SHOW:
            return Object.assign({}, state, {
                showSkuImg: action.payload.showSkuImg,
            })
        case CHANGE_MP_SKU_MAINTITLE_SHOW:
            return Object.assign({}, state, {
                showSkuMainTitle: action.payload.showSkuMainTitle,
            })
        case CHANGE_MP_SKU_DES_SHOW:
            return Object.assign({}, state, {
                showSkuDescription: action.payload.showSkuDescription,
            })
        case CHANGE_MP_SKU_FEATURE_SHOW:
            return Object.assign({}, state, {
                showSkuFeature: action.payload.showSkuFeature,
            })
        case CHANGE_MP_SKU_KEYWORD_SHOW:
            return Object.assign({}, state, {
                showSkuKeyword: action.payload.showSkuKeyword,
            })
        case SELECT_REMOVAL_SKU:
            return Object.assign({}, state, {
                currentAp: Object.assign({}, state.currentAp, {
                    removalSku: action.payload.removalSku
                }),
            })
        case SELECT_P2M_APPLICATION_NAME:
            return Object.assign({}, state, {
                currentAp: Object.assign({}, state.currentAp, {
                    name: action.payload.name
                }),
            })
        case SHOW_CREATE_P2M_MODAL:
            return Object.assign({}, state, {
                showCreateModal: true,
                currentApType: action.payload.currentApType,
                currentAp: Object.assign({}, state.currentAp, {
                    selectedProduct: action.payload.productName,
                    selectedProductId : action.payload.productId
                }),
                skuOptions: action.payload.skuOptions
        })
        case HIDE_CREATE_P2M_MODAL:
            return Object.assign({}, state, {
                showCreateModal: false      
        })
        case FETCH_P2MAP_COMMENT_PENDING:
            return Object.assign({}, state, {
                isPending: true
            })
        case FETCH_P2MAP_COMMENT_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                allComments: action.payload.allComments
            })
        case FETCH_P2MAP_COMMENT_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case HANDLE_SMOOTH_SCROLL:
            return Object.assign({}, state, {
                smoothScroll: action.payload.smoothScroll
            })
        case UPDATE_MODAL_STATE:
            return Object.assign({}, state, {
                currentApType: action.payload.currentApType,
                currentAp: action.payload.currentAp,
                productOptions: action.payload.productOptions,
                platformOptions: action.payload.platformOptions,
                skuOptions: action.payload.skuOptions
            })
        case GET_FILTERLIST_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case GET_FILTERLIST_SUCCESS:
            return Object.assign({}, state, {
                filterList: action.payload.filterList,
                isPending: false
            })
        case GET_FILTERLIST_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case REJECT_APPLICATION_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case REJECT_APPLICATION_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                currentAp: action.payload.currentAp,
                redirectTo: action.payload.redirectTo
            })
        case REJECT_APPLICATION_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case APPROVE_APPLICATION_PENDING:
            return Object.assign({}, state, {
                isPending: true
            })
        case APPROVE_APPLICATION_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                currentAp: action.payload.currentAp,
                redirectTo: action.payload.redirectTo
            })
        case APPROVE_APPLICATION_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case GET_EXACT_SUBAP_PENDING:
            return Object.assign({}, state, {
                isPending: true
            })
        case GET_EXACT_SUBAP_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                currentAp: action.payload.currentAp,
                currentSubAp: action.payload.currentSubAp
            })
        case GET_EXACT_SUBAP_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case GET_EXACT_APPLICATION_PENDING: 
            return Object.assign({}, state, {
                isPending: true
            })
        case GET_EXACT_APPLICATION_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                currentAp: action.payload.currentAp,
                redirectTo: action.payload.redirectTo
            })
        case GET_EXACT_APPLICATION_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case SUBMIT_APPLICATION_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case SUBMIT_APPLICATION_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                result: action.payload.result,
                redirectTo: action.payload.redirectTo
            })
        case SUBMIT_APPLICATION_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case APPLY_TO_REMOVE_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case APPLY_TO_REMOVE_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                result: action.payload.result,
                redirectTo: action.payload.redirectTo
            })
        case APPLY_TO_REMOVE_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case REJECT_TO_REMOVE_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case REJECT_TO_REMOVE_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                result: action.payload.result,
                redirectTo: action.payload.redirectTo
            })
        case REJECT_TO_REMOVE_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case APPROVE_TO_REMOVE_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case APPROVE_TO_REMOVE_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                result: action.payload.result,
                redirectTo: action.payload.redirectTo
            })
        case APPROVE_TO_REMOVE_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case REFILL_APPLICATION_PENDING:
                return Object.assign({}, state, {
                    isPending: true,
                })
        case REFILL_APPLICATION_SUCCESS:
                return Object.assign({}, state, {
                    isPending: false,
                    result: action.payload.result,
                    redirectTo: action.payload.redirectTo
                })
        case REFILL_APPLICATION_FAILED:
                return Object.assign({}, state, {
                    isPending: false,
                    error: action.payload.error
            })
        case UPDATE_CURRENT_SUB_APPLICATION:
            return Object.assign({}, state, {
                currentSubAp: action.payload.currentSubAp
            })
        case UPDATE_MP:
            return Object.assign({}, state, {
                mp: action.payload.mp,
                currentSubAp: action.payload.currentSubAp
            })
        case UPDATE_PAI:
            return Object.assign({}, state, {
                pai: action.payload.pai,
                currentSubAp: action.payload.currentSubAp
            })
        case UPDATE_SH:
            return Object.assign({}, state, {
                sh: action.payload.sh,
                currentSubAp: action.payload.currentSubAp
            })
        case UPDATE_RE:
            return Object.assign({}, state, {
                re: action.payload.re,
                currentSubAp: action.payload.currentSubAp
            })
        case UPDATE_INS:
            return Object.assign({}, state, {
                re: action.payload.ins,
                currentSubAp: action.payload.currentSubAp
            })
        case UPDATE_SHOW_BREADCRUMBS:
            return Object.assign({}, state, {
                showBreadcrumbs: action.payload.showBreadcrumbs
            })
        case UPDATE_CURRENT_APPLICATION:
            return Object.assign({}, state, {
                currentAp: action.payload.currentAp
            })
        case UPDATE_APPLICATION:
            return Object.assign({}, state, {
                currentAp: action.payload.currentAp,
                currentSubAp: action.payload.currentSubAp
            })
        case REQUEST_P2M_APPLICATIONS_PENDING:
            return Object.assign({}, state, {
                isPending: true
            })
        case REQUEST_P2M_APPLICATIONS_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                applications: action.payload.applications,
                totalPages:action.payload.totalPages
            })
        case REQUEST_P2M_APPLICATIONS_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case INIT_CREATE_P2M_APPLICATION:
            return Object.assign({}, state, {
                productOptions: action.payload.productOptions,
                currentAp: Object.assign({}, state.currentAp, {
                    selectedProduct: action.payload.selectedProduct
                })  
                // error: action.payload.error
            })
        case SELECT_P2M_APPLICATION_PRODUCT:
            return Object.assign({}, state, {
                // currentApType: action.payload.currentApType,
                currentAp: Object.assign({}, state.currentAp, {
                    selectedProduct: action.payload.selectedProduct
                }),
                skuOptions: action.payload.skuOptions
            })      
        case GET_EXACT_PRODUCT_FOR_AP_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case GET_EXACT_PRODUCT_FOR_AP_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                currentAp: Object.assign({}, state.currentAp, {
                    selectedProduct: action.payload.selectedProduct,
                }),
                skuOptions: action.payload.skuOptions
            })
        case GET_EXACT_PRODUCT_FOR_AP_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        // case SELECT_P2M_SUBAP_TYPE:
        //     return Object.assign({}, state, {
        //         currentAp: Object.assign({}, state.currentAp, {
        //             selectedSubApType: action.payload.selectedSubApType
        //         })
        //     })
        case SELECT_P2M_APPLICATION_TYPE:
            return Object.assign({}, state, {
                currentApType: action.payload.currentApType,
            })
        case SELECT_P2M_APPLICATION_SKU:
            return Object.assign({}, state, {
                currentAp: Object.assign({}, state.currentAp, {
                    appliedSkuCode: action.payload.appliedSkuCode
                })
            })
        case SELECT_P2M_APPLICATION_PLATFORM:
            return Object.assign({}, state, {
                currentAp: Object.assign({}, state.currentAp, {
                    selectedPlatform: action.payload.selectedPlatform
                })
            })
        case CHANGE_P2M_APPLICATION_PLATFORM_OPTIONS:
            return Object.assign({}, state, {
                platformOptions: action.payload.platformOptions
            })
        case SELECT_P2M_COUNTRY:
            return Object.assign({}, state, {
                currentAp: Object.assign({}, state.currentAp, {
                    selectedCountry: action.payload.selectedCountry,
                    // selectedPlatform: action.payload.selectedPlatform
                }),
            })
        case CREATE_P2M_APPLICATION_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case CREATE_P2M_APPLICATION_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                result: action.payload.result,
            })
        case CREATE_P2M_APPLICATION_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case CHANGE_P2M_APPLICATION_PENDING:
                return Object.assign({}, state, {
                    isPending: true,
            })
        case CHANGE_P2M_APPLICATION_SUCCESS:
                return Object.assign({}, state, {
                    isPending: false,
                    result: action.payload.result,
            })
        case CHANGE_P2M_APPLICATION_FAILED:
                return Object.assign({}, state, {
                    isPending: false,
                    error: action.payload.error
            })
        case UPDATE_P2M_APPLICATION_PENDING:
                return Object.assign({}, state, {
                    isPending: true,
            })
        case UPDATE_P2M_APPLICATION_SUCCESS:
                return Object.assign({}, state, {
                    isPending: false,
                    result: action.payload.result,
                    redirectTo: action.payload.redirectTo
            })
        case UPDATE_P2M_APPLICATION_FAILED:
                return Object.assign({}, state, {
                    isPending: false,
                    error: action.payload.error
            })
        case SAVE_P2M_MARKETPLACE_INFO_PENDING:
            return Object.assign({}, state, {
                isPending: true,
                result: action.payload.result,
            })
        case SAVE_P2M_MARKETPLACE_INFO_SUCCESS:
            return Object.assign({}, state, {
                result: action.payload.result,
                currentSubAp: action.payload.marketplaceInfo,
                isPending: false,
                redirect: action.payload.redirect
            })
        case SAVE_P2M_MARKETPLACE_INFO_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case SAVE_P2M_INSURANCE_PENDING:
            return Object.assign({}, state, {
                isPending: true,
            })
        case SAVE_P2M_INSURANCE_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                result: action.payload.result,
                // redirect: action.payload.redirect
            })
        case SAVE_P2M_INSURANCE_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case SAVE_P2M_PRODUCT_INFO_PENDING:
            return Object.assign({}, state, {
                isPending: true
            })
        case SAVE_P2M_PRODUCT_INFO_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                result: action.payload.result,
                redirect: action.payload.redirect
            })
        case SAVE_P2M_PRODUCT_INFO_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case SAVE_P2M_REGIONAL_PENDING:
            return Object.assign({}, state, {
                isPending: true
            })
        case SAVE_P2M_REGIONAL_SUCCESS:
            return Object.assign({}, state, {
                result: action.payload.result,
                isPending: false,
                redirect: action.payload.redirect
            })
        case SAVE_P2M_REGIONAL_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case SAVE_P2M_SHIPPING_PENDING:
            return Object.assign({}, state, {
                isPending: true
            })
        case SAVE_P2M_SHIPPING_SUCCESS:
            return Object.assign({}, state, {
                isPending: false,
                result: action.payload.result,
                redirect: action.payload.redirect
            })
        case SAVE_P2M_SHIPPING_FAILED:
            return Object.assign({}, state, {
                error: action.payload.error,
                isPending: false
            })
        case LOAD_P2M_MARKETPLACE_INFO_PENDING:
            return Object.assign({}, state, {
                isPending: true,
                redirect: ''
            })
        case LOAD_P2M_MARKETPLACE_INFO_SUCCESS:
            return Object.assign({}, state, {
                currentSubAp: action.payload.marketplaceInfo,
                mp: Object.assign({}, state.mp, action.payload.marketplaceInfo),
                isPending: false
            })
        case LOAD_P2M_MARKETPLACE_INFO_FAILED:
            return Object.assign({}, state, {
                error: action.payload.error,
                isPending: false,
            })
        case LOAD_P2M_PRODUCT_INFO_PENDING:
            return Object.assign({}, state, {
                isPending: true,
                redirect: ''
            })
        case LOAD_P2M_PRODUCT_INFO_SUCCESS:
            return Object.assign({}, state, {
                currentSubAp: action.payload.productInfo,
                pai: Object.assign({}, state.pai, action.payload.productInfo),
                isPending: false
            })
        case LOAD_P2M_PRODUCT_INFO_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case LOAD_P2M_SHIPPING_PENDING:
            return Object.assign({}, state, {
                isPending: true,
                redirect: ''
            })
        case LOAD_P2M_SHIPPING_SUCCESS:
            return Object.assign({}, state, {
                currentSubAp: action.payload.shipping,
                sh: Object.assign({}, state.sh, action.payload.shipping),
                isPending: false
            })
        case LOAD_P2M_SHIPPING_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case LOAD_P2M_REGIONAL_PENDING:
            return Object.assign({}, state, {
                isPending: true,
                redirect: ''
            })
        case LOAD_P2M_REGIONAL_SUCCESS:
            return Object.assign({}, state, {
                currentSubAp: action.payload.regional,
                re: Object.assign({}, state.re, action.payload.regional),
                isPending: false
            })
        case LOAD_P2M_REGIONAL_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case LOAD_P2M_INSURANCE_PENDING:
            return Object.assign({}, state, {
                isPending: true,
                redirect: ''
            })
        case LOAD_P2M_INSURANCE_SUCCESS:
            return Object.assign({}, state, {
                currentSubAp: action.payload.insurance,
                ins: Object.assign({}, state.ins, action.payload.insurance),
                isPending: false
            })
        case LOAD_P2M_INSURANCE_FAILED:
            return Object.assign({}, state, {
                isPending: false,
                error: action.payload.error
            })
        case UPDATE_REDIRECT_APPLICATION:
            return Object.assign({}, state, {
                redirectTo: action.payload.redirectTo
            })
        case DELETE_AP_PENDING:
                return Object.assign({}, state, {
                    isPending: true,
                })
        case DELETE_AP_SUCCESS:
                return Object.assign({}, state, {
                    isPending: false,
                    result: action.payload.result,
                    redirectTo: action.payload.redirectTo
                })
        case DELETE_AP_FAILED:
                return Object.assign({}, state, {
                    isPending: false,
                    error: action.payload.error
            })
        default:
            return state
    }
}