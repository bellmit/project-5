import axios from 'axios';
import { toast } from 'react-toastify';

import {
    REQUEST_P2M_APPLICATIONS_PENDING,
    REQUEST_P2M_APPLICATIONS_SUCCESS,
    REQUEST_P2M_APPLICATIONS_FAILED,

    UPDATE_CURRENT_APPLICATION,
    UPDATE_SHOW_BREADCRUMBS,
    UPDATE_CURRENT_SUB_APPLICATION,
    
    CREATE_P2M_APPLICATION_PENDING,
    CREATE_P2M_APPLICATION_SUCCESS,
    CREATE_P2M_APPLICATION_FAILED,
    
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

    SAVE_P2M_SHIPPING_PENDING,
    SAVE_P2M_SHIPPING_SUCCESS,
    SAVE_P2M_SHIPPING_FAILED,

    SAVE_P2M_PRODUCT_INFO_PENDING,
    SAVE_P2M_PRODUCT_INFO_SUCCESS,
    SAVE_P2M_PRODUCT_INFO_FAILED,

    SELECT_P2M_APPLICATION_TYPE,
    SELECT_P2M_APPLICATION_PRODUCT,
    SELECT_P2M_APPLICATION_SKU,
    SELECT_P2M_APPLICATION_PLATFORM,

    SELECT_P2M_APPLICATION_NAME,
    SELECT_REMOVAL_SKU,
    // SELECT_P2M_SUBAP_TYPE,

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
    DOMAIN_NAME,
    CHANGE_P2M_APPLICATION_FAILED,
    CHANGE_P2M_APPLICATION_PENDING,
    CHANGE_P2M_APPLICATION_SUCCESS,
    UPDATE_P2M_APPLICATION_FAILED,
    UPDATE_P2M_APPLICATION_PENDING,
    UPDATE_P2M_APPLICATION_SUCCESS,
    REFILL_APPLICATION_PENDING,
    REFILL_APPLICATION_SUCCESS,
    REFILL_APPLICATION_FAILED,
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
    UPDATE_RE,
    UPDATE_INS,
    UPDATE_SKU_OPTIONS,
    CHANGE_AP,
    GET_FILTERLIST_PENDING,
    GET_FILTERLIST_SUCCESS,
    GET_FILTERLIST_FAILED,
    CHANGE_FILTER_TARGET,
    CHANGE_FILTERED_APS,
} from '../constants/action-types'

const Url = DOMAIN_NAME;

const res = [];

const fakeJson = {
  marketPlaceInfo: {
    competitorInfo:'',
    main: {
      img: '',
      title: '',
      description: '',
      feature: '',
      keyword: ''
    },
    appliedSku: [
      {
        sellerSku: '',
        variationNameForMarketPlace:'',
        img: '',
        title: '',
        description: '',
        feature: '',
        keyword: ''
      }
    ],
  },
  insurance: {
  },
  regional: {
    otherFile: '',
    patentFile: '',
    certificateFile: '',
    productImg: ''
  },
  shipping: {
    shippingMethod: '',
    shippingInfo: ''
  },
  productInfo: {
    url: '',
    startDate: '',
    manufactureDays:'',
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
  }
}
export const handleFilterP2MAp = (result) => ({
  type: CHANGE_FILTERED_APS,
  payload: {
    filteredAps: result
  }
}) 

export const getFilterList = (kcode , product) => (dispatch, getState) => {
  dispatch({ type: GET_FILTERLIST_PENDING})
  
  let supplierId = getState().d.u.cid;
  const k = kcode == undefined ? '': kcode
  const p = product == undefined ? '': product
  
  axios.post(Url + '/p2m/aapl', {
    si: supplierId,
    k: k,
    p: p
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log(res, 'request for /p2m/aapl')

    if (res != null) {
      dispatch({
        type: GET_FILTERLIST_SUCCESS,
        payload: {
          filterList: res.data.p2mApplications,
        }
      })
    } else {
      alert('cant get ap list')
    }
  })
  .catch(err => {dispatch({ type: GET_FILTERLIST_FAILED, payload: { error: err } })})
}

export const selectAp = (target) => ({
  type: SELECT_P2M_APPLICATION_NAME,
  payload: {
    name: target.value.name
  }
})

export const selectRemovalSku = (target) => ({
  type: SELECT_REMOVAL_SKU,
  payload: {
    removalSku: target
  }
})

export const showCreateP2MModal = (currentApType , proudctName , productId, skuOptions) => ({
  type: SHOW_CREATE_P2M_MODAL,
  payload: {
    show: true,
    productName: proudctName,
    productId:productId,
    //currentApType: { value:currentApType , label: "createp2mapplication.select"},
    currentApType: currentApType ,
    skuOptions: skuOptions
  }
})

export const hideCreateP2MModal = () => ({
  type: HIDE_CREATE_P2M_MODAL,
  payload: {
    show: false
  }
})
export const updateSkuOp = (arr) => ({
  type: UPDATE_SKU_OPTIONS,
  payload: {
    skuOptions: arr
  }
})

export const changeSkuItemShow = (arr) => ({
  type: CHANGE_SKUITEM_SHOW,
  payload: {
    skuItemShow: arr
  }
})
export const changeMpSkuVariationNameShow = (arr) => ({
    type: CHANGE_MP_SKU_VARIATIONNAME_SHOW,
    payload: {
      showSkuVariationName: arr,
    }
})
export const changeMpSkuImgShow = (arr) => ({
  type: CHANGE_MP_SKU_IMG_SHOW,
  payload: {
    showSkuImg: arr,
  }
})
export const changeMpSkuMainTitleShow = (arr) => ({
  type: CHANGE_MP_SKU_MAINTITLE_SHOW,
  payload : {
    showSkuMainTitle: arr,
  }
})
export const changeMpSkuDesShow = (arr) => ({
  type: CHANGE_MP_SKU_DES_SHOW,
  payload : {
    showSkuDescription: arr,
  }
})
export const changeMpSkuFeatureShow = (arr) => ({
  type: CHANGE_MP_SKU_FEATURE_SHOW,
  payload : {
      showSkuFeature: arr,
  }
})
export const changeMpSkuKeywordShow = (arr) => ({
  type: CHANGE_MP_SKU_KEYWORD_SHOW,
  payload : {
      showSkuKeyword: arr,
  }
})

export const fetchP2MApComment = (p2mId) => (dispatch)  => {
  dispatch({ type: FETCH_P2MAP_COMMENT_PENDING })
  // dispatch({ 
  //   type: FETCH_P2MAP_COMMENT_SUCCESS,
  //   payload: {
  //     // allComment: res.data
  //     allComments: fakeJson
  //   }
  // })
  axios.post(Url + '/p2m/gac', {
    p2mId: p2mId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then( res => {
    console.log(res.data, 'fetch p2m comments');

    if(res != null) {
      dispatch({ 
        type: FETCH_P2MAP_COMMENT_SUCCESS,
        payload: {
          allComments: res.data
          // allComments: fakeJson
        }
      })
    }
  }).catch( err => dispatch({ type: FETCH_P2MAP_COMMENT_FAILED, payload: {error: err} }))
}

export const handleSmoothScroll = (arr) => (dispatch) => {
  dispatch({
      type: HANDLE_SMOOTH_SCROLL,
      payload: {
        smoothScroll: arr
      }
    })
}

export const updateModalState = () => ({
  type: UPDATE_MODAL_STATE,
  payload: {
    currentApType: {label: "createp2mapplication.select"},
    currentAp: {
      selectedProduct: '',
      selectedCountry: 'createp2mapplication.select',
      selectedPlatform: 'createp2mapplication.select',
      selectedSubApType: 'createp2mapplication.select',
      appliedSkuCode: [],
    },
    productOptions: [],
    platformOptions: [],
    skuOptions: [],
  }
})

export const refillP2MAp = (p2mId,message) => (dispatch) => {
  //console.log('request refill', p2mAp);
  dispatch({ type: REFILL_APPLICATION_PENDING})
  
  axios.post(Url + '/p2m/re', {
    pid:p2mId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    if (res != null) {
      dispatch({
        type: REFILL_APPLICATION_SUCCESS,
        payload: {
          result: res.data, 
          redirectTo: 'manageP2M',
        }
      })
      toast(message)
    } else {
      alert('unable to requeset to refill')
    }
  })
  .catch(err => {dispatch({ type: REFILL_APPLICATION_FAILED, payload: { error: err } })})
}

export const deleteP2MAp = (p2mId, message) => (dispatch) => {
  dispatch({ type: DELETE_AP_PENDING })
  axios.post(Url + '/p2m/d', {
    pid: p2mId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    }
  }).then(res => {
    if (res != null) {
      dispatch({
        type: DELETE_AP_SUCCESS,
        payload: {
          result: res.data,
          redirectTo: 'manageP2M'
        }
      })
      toast(message)
    } else {
      alert('unable to request to remove')
    }
  }).catch(err => {
     dispatch({ type: DELETE_AP_FAILED, payload: {error: err }})
  })
}

export const applyToRemoveP2MAp = (supplierId,p2mId,message) => (dispatch) => {
  //console.log('request submit', ap)
  // const p2mId = ap._id;

  dispatch({type: APPLY_TO_REMOVE_PENDING})

  axios.post(Url + '/p2m/atr',{
    p2mId: p2mId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log(res, 'apply to remove p2m application success');

    if (res != null) {
      dispatch({
        type: APPLY_TO_REMOVE_SUCCESS,
        payload: {
          result: res.data,
          redirectTo: 'manageP2M',
        }
      })

      dispatch(loadApplications(supplierId , 1))

      toast(message)
    } else {
      alert('unable to submit')
    }
  })
  .catch(err => dispatch({ type: APPLY_TO_REMOVE_FAILED, payload: { error: err } }))

}

export const rejectToRemoveP2MAp = (p2mAp, message) => (dispatch) => {
  //p2mAp.status = 'Rejected'
  const jsonStr = JSON.stringify(p2mAp);
  dispatch({ type: REJECT_TO_REMOVE_PENDING})

  axios.post(Url + '/p2m/rtr', {
    p2mAp: jsonStr
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {

    if(res != null) {
      dispatch({
        type: REJECT_TO_REMOVE_SUCCESS,
        payload: {
          currentAp: p2mAp,
          redirectTo: 'manageP2M'
        }
      })
    } else {
      alert('unable to reject')
    }
    toast(message)
  })
  .catch( err => dispatch({ type: REJECT_TO_REMOVE_FAILED, payload: { error: err }}))
}

export const approveToRemoveP2MAp = (p2mAp, message) => (dispatch) => {

  const jsonStr = JSON.stringify(p2mAp);
  dispatch({ type: APPROVE_TO_REMOVE_PENDING})

  axios.post(Url + '/p2m/aprtr', {
    p2mAp: jsonStr
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {

    if (res != null) {
      dispatch({
        type: APPROVE_TO_REMOVE_SUCCESS,
        payload: {
          currentAp: p2mAp,
          redirectTo: "manageP2M"
        }
      })
    } else {
      alert('unable to approve')
    }
    toast(message)
  })
  .catch( err => dispatch({ type: APPROVE_TO_REMOVE_FAILED, payload: { error: err}}))
}
 
export const submitApplication = (ap,message) => (dispatch) => {
  //console.log('request submit', ap)
  // const p2mId = ap._id;

  dispatch({type: SUBMIT_APPLICATION_PENDING})

  const jsonStr = JSON.stringify(ap)

  axios.post(Url + '/p2m/s',{
    p2mAp: jsonStr
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log(res, 'submit p2m application success');

    if (res != null) {
      dispatch({
        type: SUBMIT_APPLICATION_SUCCESS,
        payload: {
          result: res.data,
          redirectTo: 'manageP2M',
        }
      })
      
      toast(message)
    } else {
      alert('unable to submit')
    }
  })
  .catch(err => dispatch({ type: SUBMIT_APPLICATION_FAILED, payload: { error: err } }))
  
}

export const rejectP2MAp = (p2mAp, message) => (dispatch) => {
  //p2mAp.status = 'Rejected'
  const jsonStr = JSON.stringify(p2mAp);
  dispatch({ type: REJECT_APPLICATION_PENDING})

  axios.post(Url + '/p2m/r', {
    p2mAp: jsonStr
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    
    if(res != null) {
      dispatch({
        type: REJECT_APPLICATION_SUCCESS,
        payload: {
          currentAp: p2mAp,
          redirectTo: 'manageP2M'
        }
      })
    } else {
      alert('unable to reject')
    }
    toast(message)
  })
  .catch( err => dispatch({ type: REJECT_APPLICATION_FAILED, payload: { error: err }}))
}

export const approveP2MAp = (p2mAp, message) => (dispatch) => {
  
  const jsonStr = JSON.stringify(p2mAp);
  dispatch({ type: APPROVE_APPLICATION_PENDING})

  axios.post(Url + '/p2m/a', {
    p2mAp: jsonStr
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    
    if (res != null) {
      dispatch({
        type: APPROVE_APPLICATION_SUCCESS,
        payload: {
          currentAp: p2mAp,
          redirectTo: "manageP2M"
        }
      })
    } else {
      alert('unable to approve')
    }
    toast(message)
  })
  .catch( err => dispatch({ type: APPROVE_APPLICATION_FAILED, payload: { error: err}}))
}

export const changeAp = (p2mId) => ({
  type: CHANGE_AP,
  payload : {
      p2mId: p2mId,
      redirectTo: "P2MApplication"
  }
})

export const getExactApplication = (p2mId, isFilter) => (dispatch) => {
  console.log('get exact application', p2mId, isFilter)
  dispatch({ type: GET_EXACT_APPLICATION_PENDING})

  axios.post(Url + '/p2m/g', {
    id: p2mId
  },{
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log('get exact ap response',res);

    const filteredAp = res.data
    let filteredApList = [filteredAp]
    console.log(filteredAp, filteredApList)

    if (res != null) {
      dispatch({
        type: GET_EXACT_APPLICATION_SUCCESS,
        payload: {
          currentAp: res.data,
          // redirectTo: "P2MApplication"
        }
      })
      const arr = res.data.appliedSkuCode
      const result = arr.map(item => {return false})
      dispatch(changeSkuItemShow(result))
      dispatch(handleFilterP2MAp(filteredApList))
      
    } else {
      alert('unable to get')
    }
  })
  .catch(err => dispatch({ type: GET_EXACT_APPLICATION_FAILED, payload: {error: err} })) 
}

export const changeFilterTarget = (target) => ({
  type: CHANGE_FILTER_TARGET,
  payload: {
    filterTarget: target
  }
})

export const loadApplications = (supplierId , pageIndex, country, status, kcode, product) => (dispatch , getState) => {

  if (supplierId == undefined) {
      setTimeout(() => {
        let cid = getState().d.u.cid
          dispatch(load(cid , pageIndex, country, status, kcode, product));
      }, 1000);
    
  } else {
      dispatch(load(supplierId , pageIndex, country, status, kcode, product));
  }
}


const load = (supplierId , pageIndex, country, status, kcode, product) => (dispatch) => {
    dispatch({ type: REQUEST_P2M_APPLICATIONS_PENDING })

    const c = country == undefined ? '': country
    const s = status == undefined ? '': status
    const k = kcode == undefined ? '': kcode
    const p = product == undefined ? '': product

    console.log(c, 'country', s, 'status', k, 'kcode', p, 'product')

    axios.post(Url + '/p2m/apl',{
        si: supplierId,
        pi: pageIndex,
        c: c,
        s: s,
        k: k,
        p: p
    }, {
      headers: { 
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      }
    })
    .then(res => {
      console.log(res, 'load res')
      // test 
      /*
      const aps = res.data;
      const testId = "6093b20452cdde3e172d5794"
      const result = aps.map(item => {
        // console.log(item._id.$oid)
        if (item._id.$oid == testId) {
          // console.log('hhhhhhhh')
          item.status = 'Rejected'
          return item
        }
        return item
      })
      console.log(result);
      */

      if (res != null) {
        dispatch({
          type: REQUEST_P2M_APPLICATIONS_SUCCESS,
          payload: { 
            applications: res.data.p2mApplications,
            totalPages: res.data.totalPages
          }
          //payload: {applications: result}
        })
        
      } else {
        alert('unable to get applications')
      }
    })
    .catch(err => dispatch({
      type: REQUEST_P2M_APPLICATIONS_FAILED,
      payload: { error: err }
    }))
    
}

export const initP2MApplication = (param) => ({
    type: INIT_CREATE_P2M_APPLICATION,
    payload: {
        productOptions: param.productOptions,
        selectedProduct: param.selectedProduct
        // p2mApplication: {}
    }
})

export const initP2MMarketplaceInfo = (p2mId) => (dispatch) => {
  // console.log('dispatch init marketplaceinfo', p2mId)
  dispatch({ type: LOAD_P2M_MARKETPLACE_INFO_PENDING })

  axios.post(Url + '/p2m/gm', {
    p2mId: p2mId,
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    // console.log(res, 'initP2MMarketplaceInfo')

    if (res.data !== null) {
      dispatch({
        type: LOAD_P2M_MARKETPLACE_INFO_SUCCESS,
        payload: { marketplaceInfo: res.data }
      })
      
      const appliedSku = res.data.appliedSku

      const skuOp = appliedSku.map(item => {
        return { value: item.skuCode, label: item.skuCode }
      })
      dispatch(updateSkuOp(skuOp))

      const result = appliedSku.map(item => {return false})

      dispatch(changeMpSkuVariationNameShow(result))
      dispatch(changeMpSkuMainTitleShow(result))
      dispatch(changeMpSkuImgShow(result))
      dispatch(changeMpSkuDesShow(result))
      dispatch(changeMpSkuFeatureShow(result))
      dispatch(changeMpSkuKeywordShow(result))
    }
    // let mp = null;
    // if (res.data != null) {
    //     mp = res.data;
    // }
    // if (!mp.hasOwnProperty('p2mApplicationId')) {
    //   dispatch({ type: INIT_P2M_MARKETPLACE_INFO_SUCCESS })
    // } else {
    //   dispatch({
    //     type: LOAD_P2M_MARKETPLACE_INFO_SUCCESS,
    //     payload: { marketplaceInfo: mp }
    //   })
      
    //   const arr = mp.appliedSku

    //   const skuOp = arr.map(item => {
    //   	return { value: item.skuCode, label: item.skuCode }
    //   })
    //   dispatch(updateSkuOp(skuOp))

    //   const result = arr.map(item => {return false})
    //   dispatch(changeMpSkuVariationNameShow(result))
    //   dispatch(changeMpSkuMainTitleShow(result))
    //   dispatch(changeMpSkuImgShow(result))
    //   dispatch(changeMpSkuDesShow(result))
    //   dispatch(changeMpSkuFeatureShow(result))
    //   dispatch(changeMpSkuKeywordShow(result))
    // }
  })
  .catch(err => dispatch({ type: LOAD_P2M_MARKETPLACE_INFO_FAILED, payload:{ error: err } }))
}



export const initP2MRegional = (p2mId) => (dispatch) => {
  dispatch({ type: LOAD_P2M_REGIONAL_PENDING })
  axios.post(Url + '/p2m/gr', {
    p2mId: p2mId,
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    // console.log('request for p2m regional', res)
    if (res.data != null) {
      dispatch({
        type: LOAD_P2M_REGIONAL_SUCCESS,
        payload: { regional: res.data }
      })
      const skuOp = res.data.appliedSku.map(item => {
      	return { value: item.skuCode, label: item.skuCode }
      })
      dispatch(updateSkuOp(skuOp))
    }

    // let re = null;
    // if (res.data != null) {
    //     re = res.data;
    // }
    // if (!re.hasOwnProperty('p2mApplicationId')) {
    //   dispatch({ type: INIT_P2M_REGIONAL_SUCCESS })
    // } else {
    //   dispatch({
    //     type: LOAD_P2M_REGIONAL_SUCCESS,
    //     payload: { regional: re }
    //   })
    //   const skuOp = re.appliedSku.map(item => {
    //   	return { value: item.skuCode, label: item.skuCode }
    //   })
    //   dispatch(updateSkuOp(skuOp))
    // }
  })
  .catch(err => dispatch({ type: LOAD_P2M_REGIONAL_FAILED, payload:{ error: err } }))
}


export const initP2MShipping = (p2mId) => (dispatch) => {

  dispatch({ type: LOAD_P2M_SHIPPING_PENDING })
  axios.post(Url + '/p2m/gs', {
    p2mId: p2mId,
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    if (res.data !== null) {
      dispatch({
        type: LOAD_P2M_SHIPPING_SUCCESS,
        payload: { shipping: res.data }
      })
      const skuOp = res.data.appliedSku.map(item => {
      	return { value: item.skuCode, label: item.skuCode }
      })
      dispatch(updateSkuOp(skuOp))
    }
  })
  .catch(err => dispatch({ type: LOAD_P2M_SHIPPING_FAILED, payload:{error: err} }))
}

export const initP2MInsurance = (p2mId) => (dispatch) => {
  dispatch({ type: LOAD_P2M_INSURANCE_PENDING })
  axios.post(Url + '/p2m/gi', {
    p2mId: p2mId,
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    // console.log('request for p2m insurance', res)
    if (res.data !== null) {
      dispatch({
        type: LOAD_P2M_INSURANCE_SUCCESS,
        payload: { insurance: res.data }
      })
      const skuOp = res.data.appliedSku.map(item => {
      	return { value: item.skuCode, label: item.skuCode }
      })
      dispatch(updateSkuOp(skuOp))
    }
  })
  .catch(err => dispatch({ type: LOAD_P2M_INSURANCE_FAILED, payload:{ error: err } }))
}

export const initP2MProductInfo = (p2mId) => (dispatch) => {
  dispatch({ type: LOAD_P2M_PRODUCT_INFO_PENDING })

  axios.post(Url + '/p2m/gp', {
    p2mId: p2mId,
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    // console.log('request for p2m product info', res)
    if (res.data != null) {
      // const appliedSku = res.data.appliesSku
      dispatch({
        type: LOAD_P2M_PRODUCT_INFO_SUCCESS,
        payload: { 
          productInfo: res.data,
          // appliedSku: appliedSku
        }
      })

      const skuOp = res.data.appliedSku.map(item => {
      	return { value: item.skuCode, label: item.skuCode }
      })
      dispatch(updateSkuOp(skuOp))
    }
  })
  .catch(err => dispatch({ type: LOAD_P2M_PRODUCT_INFO_FAILED, payload:{ error: err } }))
}


// todo jinny
export const getExactProductForAp = (supplierId, bpId) => (dispatch) => {
    console.log('request to get exact bp', bpId );
    dispatch({ type: GET_EXACT_PRODUCT_FOR_AP_PENDING })
  
    axios.post(Url + '/product/mdbp/gbp', {
      si: supplierId,
      bi: bpId
    }, {
      headers: { 
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      }
    })
    .then(res => {
      // console.log('get exact bp', res.data);
      if (res) {
        const skuOptions = res.data.skus.map(item => {
            return { value: item.sellerSku, label: item.sellerSku }
        })
        dispatch({
          type: GET_EXACT_PRODUCT_FOR_AP_SUCCESS,
          payload: {
            // bpList: res.data.bpList,
            selectedProduct: res.data.productNameEN,
            skuOptions: skuOptions
          }
        })
      } else {
        alert('unable to get product')
      }
    })
    .catch(err => dispatch({ type: GET_EXACT_PRODUCT_FOR_AP_FAILED, payload:{error: err} })) 
  }

export const changeP2MApplication = (supplierId,p2mId) => (dispatch) => {
  dispatch({ type: CHANGE_P2M_APPLICATION_PENDING })

  axios.post(Url + '/p2m/ch',{
    si: supplierId,
    pid: p2mId
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    if (res != null) {
      dispatch({
          type: CHANGE_P2M_APPLICATION_SUCCESS,
          payload: {
            result: res.data,
          }
      })

      dispatch(loadApplications(supplierId , 1))
      //toast(message)

    } else {
      alert('unable to change P2M application')
    }
  })
  .catch(err => dispatch({type: CHANGE_P2M_APPLICATION_FAILED, payload: {error: err} }))
}

export const updateP2MApplication = (supplierId,p2mId,currentAp,skuInfo,message) => (dispatch) => {
  const jsonStr = JSON.stringify(currentAp)
  const skuInfoJsonStr = JSON.stringify(skuInfo)
  dispatch({ type: UPDATE_P2M_APPLICATION_PENDING })

  axios.post(Url + '/p2m/up',{
    si: supplierId,
    pid: p2mId,
    skui: skuInfoJsonStr,
    currentAp: jsonStr
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    if (res != null) {
      dispatch({
          type: UPDATE_P2M_APPLICATION_SUCCESS,
          payload: {
            result: res.data,
            redirectTo: 'manageP2M',
          }
      })

      dispatch(loadApplications(supplierId , 1))
      toast(message)

    } else {
      alert('unable to update P2M application')
    }
  })
  .catch(err => dispatch({type: UPDATE_P2M_APPLICATION_FAILED, payload: {error: err} }))
}
  

export const createP2MApplication = (supplierId,currentAp,message) => (dispatch) => {
  // console.log(currentAp, 'currentAp') 
  const jsonStr = JSON.stringify(currentAp)
  dispatch({ type: CREATE_P2M_APPLICATION_PENDING })
  axios.post(Url + '/p2m/c',{
    si: supplierId,
    currentAp: jsonStr
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    if (res != null) {
      dispatch({
          type: CREATE_P2M_APPLICATION_SUCCESS,
          payload: {
            result: res.data,
          }
      })

      dispatch(loadApplications(supplierId , 1))
      toast(message)
    
    } else {
      alert('unable to create P2M application')
    }
  })
  .catch(err => dispatch({type: CREATE_P2M_APPLICATION_FAILED, payload: {error: err} }))
}

export const saveP2MMarketplaceInfo = (redirect,marketplaceInfo,toaster) => (dispatch) => {

  dispatch({ 
    type: SAVE_P2M_MARKETPLACE_INFO_PENDING,
    payload: {result: 'loading'}
  })
  const jsonStr = JSON.stringify(marketplaceInfo)

  axios.post(Url + '/p2m/smi',{
      marketplaceInfo:jsonStr
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log(res, 'request save p2m marketplace info')
    //todo arthur we don't have pass the supplierid
    //dispatch(loadApplications(11))
    if (res != null) {
      dispatch({
        type: SAVE_P2M_MARKETPLACE_INFO_SUCCESS,
        payload: {
          marketplaceInfo: marketplaceInfo,
          result: res.data,
          redirect: redirect
        }
      })
      
      toast(toaster)
    }
  })
  .catch(err => dispatch({type: SAVE_P2M_MARKETPLACE_INFO_FAILED, payload:{ error: err } }))
}


export const saveP2MRegional = (regional,toaster,redirect) => (dispatch) => {

  dispatch({ type: SAVE_P2M_REGIONAL_PENDING })
    const jsonStr = JSON.stringify(regional);
  
    axios.post(Url + '/p2m/sr',{
        regional:jsonStr
    }, {
      headers: { 
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      }
    })
    .then(res => {
      console.log(res, 'save p2m regional')
      //todo arthur we don't have pass the supplierid
      //dispatch(loadApplications(11))

      if (res != null) {
        dispatch({
            type: SAVE_P2M_REGIONAL_SUCCESS,
            payload: {
              result: res.data,
              redirect: redirect
            },
          })

        toast(toaster)
      } else {
        alert('unable to get saved data')
      }
    })
    .catch(err => dispatch({type: SAVE_P2M_REGIONAL_FAILED, payload: {error: err} }))
}


export const saveP2MInsurance = (insurance,toaster) => (dispatch) => {
    dispatch({ type: SAVE_P2M_INSURANCE_PENDING })

    const jsonStr = JSON.stringify(insurance)
    
    axios.post(Url + '/p2m/si',{
        insurance:jsonStr
    }, {
      headers: { 
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      }
    })
    .then(res => {
      console.log(res, 'save p2m insurance')
      //todo arthur we don't have pass the supplierid
      if (res != null) {
        dispatch({
          type: SAVE_P2M_INSURANCE_SUCCESS,
          payload: {
            result: res.data,
            // redirect: 'P2MApplication'
          },
        })

        toast(toaster)
      } else {
        alert('unable to get saved data')
      }
    })
    .catch(err => dispatch({type: SAVE_P2M_INSURANCE_FAILED, payload: {error:err} }))
}

export const saveP2MProductInfo = (productInfo,toaster,redirect) => (dispatch) => {

    dispatch({ type: SAVE_P2M_PRODUCT_INFO_PENDING })

    const jsonStr = JSON.stringify(productInfo)
 
    axios.post(Url + '/p2m/spi',{
      productInfo: jsonStr
    }, {
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
      }
    })
    .then(res => {

      //todo arthur we don't have to pass the supplierid
      //dispatch(loadApplications(11))

      if (res != null) {
        dispatch({
          type: SAVE_P2M_PRODUCT_INFO_SUCCESS,
          payload: { 
            result: res.data,
            redirect: redirect
          },
        })

        toast(toaster)

      } else {
        alert('unable to get products data')
      }
    })
    .catch(err => dispatch({
      type: SAVE_P2M_PRODUCT_INFO_FAILED,
      payload: { error: err } 
    }))
}


export const saveP2MShipping = (shipping,toaster,redirect) => (dispatch) => {
  
  dispatch({ type: SAVE_P2M_SHIPPING_PENDING })
  
  const jsonStr = JSON.stringify(shipping);
  axios.post(Url + '/p2m/ssh',{
    shipping:jsonStr
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log(res, 'save p2m shipping success')
    //todo arthur we don't have pass the supplierid
    //dispatch(loadApplications(11))

    if (res != null) {
      dispatch({
          type: SAVE_P2M_SHIPPING_SUCCESS,
          payload: {
            result: res.data,
            redirect: redirect
          }
      })
      toast(toaster)
    } else {
      alert('unable to get saved data')
    }
  })
  .catch(err => dispatch({ type: SAVE_P2M_SHIPPING_FAILED, payload: { error: err } }))
}

// export const selectSubApType = (selectedSubApType) => ({
//   type: SELECT_P2M_SUBAP_TYPE,
//   payload: {
//     selectedSubApType: selectedSubApType
//   }
// })

export const selectApplicationType = (selectedApplicationType) => ({
    type: SELECT_P2M_APPLICATION_TYPE,
    payload: {
        currentApType: selectedApplicationType
    }
})


export const selectApplicationProduct = (selectedProduct, skuOptions) => ({
    type: SELECT_P2M_APPLICATION_PRODUCT,
    payload: {
        selectedProduct: selectedProduct,
        // currentApType: ,
        skuOptions: skuOptions,
    }
})
export const selectApplicationAppliedSku = (appliedSkuCode) => ({
    type: SELECT_P2M_APPLICATION_SKU,
    payload: {
        appliedSkuCode: appliedSkuCode
    }
})
export const selectCountry = (selectedCountry) => ({
    type: SELECT_P2M_COUNTRY,
    payload: {
        selectedCountry: selectedCountry,
        // selectedPlatform: 'Sel123...'
    }
})
export const selectPlatform = (selectedPlatform) => ({
    type: SELECT_P2M_APPLICATION_PLATFORM,
    payload: {
        selectedPlatform: selectedPlatform
    }
})
export const onPlatformOptionsChange = (platformOptions) => ({
    type: CHANGE_P2M_APPLICATION_PLATFORM_OPTIONS,
    payload: {
        platformOptions: platformOptions
    }
})

export const updateCurrentAp = (currentAp, p2mApId) => (dispatch) => {
  console.log('updateCurrentAp dispatch');
  if (currentAp) {
    dispatch({
      type: UPDATE_CURRENT_APPLICATION,
      payload: {
        currentAp: currentAp
      }
    })
  } else {
    // console.log('get exact application', p2mApId)
    // dispatch({ type: GET_EXACT_APPLICATION_PENDING})

    // axios.post(Url + '/g', {
    //   id: p2mApId
    // },{
    //   headers: { 
    //     'Content-Type': 'application/json',
    //     'Access-Control-Allow-Origin': '*',
    //   }
    // })
    // .then(res => {
    //     console.log('get exact ap response',res);

    //     if (res != null) {
    //       dispatch({
    //         type: GET_EXACT_APPLICATION_SUCCESS,
    //         payload: {
    //           currentAp: res.data
    //         }
    //       })
    //     } else {
    //       alert('unable to get')
    //     }
    // })
    // .catch(err => dispatch({ type: GET_EXACT_APPLICATION_FAILED, payload: {error: err} })) 
  } 
}

export const getExactSubAp = (p2mApId, sub) => (dispatch) => {
  dispatch({type: GET_EXACT_SUBAP_PENDING})

  axios.post(Url + '/p2m/g', {
    id: p2mApId
  },{
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    // console.log('getExactSubAp',res);
    let subAp = {}

    if (res != null) {
      if (sub == 'marketplace') {
        subAp = res.data.subApplication.marketPlaceInfo
      } else if (sub == 'regional') {
        subAp = res.data.subApplication.regional
      } else if (sub == 'insurance') {
        subAp = res.data.subApplication.insurance
      } else if (sub == 'productinfo') {
        subAp = res.data.subApplication.productInfo
      } else if (sub == 'shipping') {
        subAp = res.data.subApplication.shipping
      }
      
      dispatch({
        type: GET_EXACT_SUBAP_SUCCESS,
        payload: {
          currentAp: res.data,
          currentSubAp: subAp
        }
      })
    } else {
      alert('unable to get')
    }
  })
  .catch(err => dispatch({ type: GET_EXACT_SUBAP_FAILED, payload: {error: err} })) 

}

export const updateAp = (currentAp, currentSubAp) => (dispatch) => {
  dispatch({
    type: UPDATE_APPLICATION,
    payload: {
      currentAp: currentAp,
      currentSubAp: currentSubAp
    }
  })
}

export const updateShowBreadcrumbs = (boolean) => ({
    type: UPDATE_SHOW_BREADCRUMBS,
    payload: {
        showBreadcrumbs: boolean
    }
})

export const updateCurrentSubAp = (currentSubAp) => (dispatch) => {
  console.log('updateCurrentSubAp dispatch', currentSubAp)
  dispatch({
    type: UPDATE_CURRENT_SUB_APPLICATION,
    payload: {
        currentSubAp: currentSubAp,
    }
  })
}

export const updateMp = (mp) => (dispatch) => {
  console.log('updatemp', mp)
  dispatch({
    type: UPDATE_MP,
    payload: {
      mp: mp,
      currentSubAp: mp,
    }
  })
}

export const updatePai = (pai) => (dispatch) => {
  console.log('updatePai', pai)
  dispatch({
    type: UPDATE_PAI,
    payload: {
      pai: pai,
      currentSubAp: pai,
    }
  })
}
export const updateSh = (sh) => (dispatch) => {
  console.log('updateSh', sh)
  dispatch({
    type: UPDATE_SH,
    payload: {
      sh: sh,
      currentSubAp: sh,
    }
  })
}
export const updateRe = (re) => (dispatch) => {
  console.log('updateRe', re)
  dispatch({
    type: UPDATE_RE,
    payload: {
      re: re,
      currentSubAp: re,
    }
  })
}
export const updateIns = (ins) => (dispatch) => {
  console.log('updateIns', ins)
  dispatch({
    type: UPDATE_INS,
    payload: {
      ins: ins,
      currentSubAp: ins,
    }
  })
}

export const updateRedirectAp = (target) => ({
  type: UPDATE_REDIRECT_APPLICATION,
  payload: {
    redirectTo: target
  }
})
