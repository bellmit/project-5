import axios from 'axios';
import { toast } from 'react-toastify';

import {
    REQUEST_UPDATE_PRODUCT_PENDING,
    REQUEST_UPDATE_PRODUCT_SUCCESS,
    REQUEST_UPDATE_PRODUCT_FAILED,
    UPDATE_SHOW_PRODUCT,
    UPDATE_CONTROL,
    REQUEST_PRODUCT_PENDING,
    REQUEST_PRODUCT_SUCCESS,
    REQUEST_PRODUCT_FAILED,
    REQUEST_ADD_PRODUCT_PENDING,
    REQUEST_ADD_PRODUCT_SUCCESS,
    REQUEST_ADD_PRODUCT_FAILED,
    REQUEST_ADD_SKU_PENDING,
    REQUEST_ADD_SKU_SUCCESS,
    REQUEST_ADD_SKU_FAILED,
    REQUEST_EXACT_BP_PENDING,
    REQUEST_EXACT_BP_SUCCESS,
    REQUEST_EXACT_BP_FAILED,
    REQUEST_NEXT_BPS_PENDING,
    REQUEST_NEXT_BPS_SUCCESS,
    REQUEST_NEXT_BPS_FAILED,
    REQUEST_REMOVE_BP_PENDING,
    REQUEST_REMOVE_BP_SUCCESS,
    REQUEST_REMOVE_BP_FAILED,
    UPDATE_MULTITHEME,
    UPDATE_INPUTS,
    CHECK_VALID,
    ON_BRANDNAME_CH_CHANGE,
    ON_BRANDNAME_EN_CHANGE,
    ON_PRODUCTNAME_CH_CHANGE,
    ON_PRODUCTNAME_EN_CHANGE,
    ON_MANUFACTURER_CH_CHANGE,
    ON_MANUFACTURER_EN_CHANGE,
    UPDATE_CHECK_VALID,
    FETCH_PRODUCT_CATEGORY_PENDING,
    FETCH_PRODUCT_CATEGORY_SUCCESS,
    FETCH_PRODUCT_CATEGORY_FAILED,
    UPDATE_CURRENT_PRODUCT_CATEGORY,
    FETCH_TOTAL_PRODUCT_NUMBER_PENDING,
    FETCH_TOTAL_PRODUCT_NUMBER_SUCCESS,
    FETCH_TOTAL_PRODUCT_NUMBER_FAILED,

    FETCH_TOTAL_SKU_NUMBER_PENDING,
    FETCH_TOTAL_SKU_NUMBER_SUCCESS,
    FETCH_TOTAL_SKU_NUMBER_FAILED,

    FETCH_TOTAL_APPLIED_SKU_NUMBER_PENDING,
    FETCH_TOTAL_APPLIED_SKU_NUMBER_SUCCESS,
    FETCH_TOTAL_APPLIED_SKU_NUMBER_FAILED,

    FETCH_TOTAL_ONSALE_SKU_NUMBER_PENDING,
    FETCH_TOTAL_ONSALE_SKU_NUMBER_SUCCESS,
    FETCH_TOTAL_ONSALE_SKU_NUMBER_FAILED,

    DOMAIN_NAME,
    FETCH_SIMPLE_PRODUCT_LIST_PENDING,
    FETCH_SIMPLE_PRODUCT_LIST_FAILED,
    FETCH_SIMPLE_PRODUCT_LIST_SUCCESS,
    // CHANGE_CURRENT_BP 
    // UPDATE_PRODUCT_CATEGORY
  } from '../constants/action-types';

import {fetchSimpleSupplierList, hideLoading} from "../actions"

const Url = DOMAIN_NAME + '/product/mdbp';


export const loadProducts = (supplierId , pageIndex) => (dispatch ) => {

  dispatch({ type: REQUEST_PRODUCT_PENDING})

  axios.post(Url + '/bpl', {
      si: supplierId , pi:pageIndex
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log('request product success',res)
    
    if (res.data !== '') {
      //todo arthur have to handle bpList
      const list = [{value: "All", label: "All"}]
      res.data.products.map(item => {
        list.push({value: item._id.$oid, label: item.productNameEN})
      })
      
      dispatch({
        type: REQUEST_PRODUCT_SUCCESS,
        payload: {
          products: res.data.products,
          totalPages: res.data.totalPages,
          pageIndex: res.data.pageIndex,
          bpList: list
        }
      })

      dispatch(hideLoading())
    } else {
      alert('No product data')
    }
  })
  .catch(err => dispatch({type: REQUEST_PRODUCT_FAILED, payload: err }))

}

export const loadManageProduct = (supplierId) => (dispatch , getState) => {

  if(supplierId == undefined){
      setTimeout(() => {
        let cid = getState().d.u.cid
          if(cid != 'K2'){
            dispatch(loadProducts(cid , 1));
            dispatch(fetchSimpleProductList(cid))
          }else{
            dispatch(fetchSimpleSupplierList())
            dispatch(hideLoading())
          }
         
      }, 1000);
    
  }else{
      if(supplierId != 'K2'){
        dispatch(loadProducts(supplierId,1));
        dispatch(fetchSimpleProductList(supplierId))
      }else{
        dispatch(fetchSimpleSupplierList())
        dispatch(hideLoading())
      }
  }
}

export const fetchSimpleProductList = (supplierId) => (dispatch ) => {

  dispatch({ type: FETCH_SIMPLE_PRODUCT_LIST_PENDING})

  console.log(supplierId)
  axios.post(Url + '/sbpl', {
      si: supplierId
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {

    if (res.data !== '') {
      res.data.bpList.unshift({value: "All", label: "All"})
      console.log(res.data.bpList)
      dispatch({
        type: FETCH_SIMPLE_PRODUCT_LIST_SUCCESS,
        payload: {
          bpList: res.data.bpList
        }
      })
    } 
  })
  .catch(err => dispatch({type: FETCH_SIMPLE_PRODUCT_LIST_FAILED, payload: err }))
}

export const updateCurrentProductCategory = (value) => ({
  type: UPDATE_CURRENT_PRODUCT_CATEGORY,
  payload: {
    currentCategory: value
  }
})

// export const updateProductCategory = (productCategory) => ({
//   type: UPDATE_PRODUCT_CATEGORY,
//   payload: {
//     productCategory: productCategory
//   }
// })

export const getProductCategory = (value) => (dispatch) => {
  dispatch({ type: FETCH_PRODUCT_CATEGORY_PENDING})

  axios.post(DOMAIN_NAME + '/pc/gl', {
    parent: value
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log('request product category success',res)
    
    const productCategory = res.data;
    if (res) {
      dispatch({
        type: FETCH_PRODUCT_CATEGORY_SUCCESS,
        payload: {
          productCategory: productCategory
        }
      })
      // request for check if is root

      // const requestArr = res.data.map(item => {
      //   return axios.post('http://localhost:8080/pc/gl', {parent: item._id})
      // })
      
      // axios.all(requestArr).then(res => {
      //   // console.log(res, 'response of requestarr')
      //   const result = [...productCategory];

      //   res.map((item,index) => {
      //     // console.log(item.data.length)
      //     if (item.data.length < 1) { 
      //       // console.log('< 1')
      //       // console.log(productCategory[index])
      //       result[index].isLast = true;
      //     }
      //   })

      //   dispatch({
      //     type: FETCH_PRODUCT_CATEGORY_SUCCESS,
      //     payload: {
      //       productCategory: result
      //     }
      //   })

      // }).catch(errors => { console.log(errors)})

    } else {
      alert('unable to get product category data')
    }
  })
  .catch(err => dispatch({type: FETCH_PRODUCT_CATEGORY_FAILED, payload: err }))
}

export const getTotalProductNumber = (supplierId) => (dispatch) => {
  dispatch({ type: FETCH_TOTAL_PRODUCT_NUMBER_PENDING})
  
  axios.post(DOMAIN_NAME + '/p2m/gtpn', {
    si: supplierId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    if (res) {
      dispatch({
        type: FETCH_TOTAL_PRODUCT_NUMBER_SUCCESS,
        payload: {
          totalProducts: res.data
        }
      })
    } 
  })
  .catch(err => dispatch({type: FETCH_TOTAL_PRODUCT_NUMBER_FAILED, payload: err }))
}

export const getTotalSkuNumber = (supplierId) => (dispatch) => {
  dispatch({ type: FETCH_TOTAL_SKU_NUMBER_PENDING})
  
  axios.post(DOMAIN_NAME + '/p2m/gtsn', {
    si:supplierId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    if (res) {
      dispatch({
        type: FETCH_TOTAL_SKU_NUMBER_SUCCESS,
        payload: {
          totalSkus: res.data
        }
      })
    } 
  })
  .catch(err => dispatch({type: FETCH_TOTAL_SKU_NUMBER_FAILED, payload: err }))
}

export const getTotalAppliedSkuNumber = (supplierId) => (dispatch) => {
  dispatch({ type: FETCH_TOTAL_APPLIED_SKU_NUMBER_PENDING})
  
  axios.post(DOMAIN_NAME + '/p2m/gtasn', {
    si: supplierId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    
    if (res) {
      dispatch({
        type: FETCH_TOTAL_APPLIED_SKU_NUMBER_SUCCESS,
        payload: {
          totalSkus: res.data
        }
      })
    } 
  })
  .catch(err => dispatch({type: FETCH_TOTAL_APPLIED_SKU_NUMBER_FAILED, payload: err }))
}

export const getTotalOnsaleSkuNumber = (supplierId) => (dispatch) => {
  
  dispatch({ type: FETCH_TOTAL_ONSALE_SKU_NUMBER_PENDING})
  
  axios.post(DOMAIN_NAME + '/p2m/gtossn', {
    si: supplierId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    
    if (res) {
      dispatch({
        type: FETCH_TOTAL_ONSALE_SKU_NUMBER_SUCCESS,
        payload: {
          totalSkus: res.data
        }
      })
    } 
  })
  .catch(err => dispatch({type: FETCH_TOTAL_ONSALE_SKU_NUMBER_FAILED, payload: err }))
}

export const loadProductStat = (supplierId) => (dispatch , getState) => {
  if(supplierId == undefined){
      setTimeout(() => {
        let cid = getState().d.u.cid
        if(cid != "K2"){
          dispatch(getTotalAppliedSkuNumber(cid))
          dispatch(getTotalOnsaleSkuNumber(cid))
          dispatch(getTotalProductNumber(cid))
          dispatch(getTotalSkuNumber(cid))
        }
   
      }, 1000);
  } else {
    if(supplierId != "K2"){
      dispatch(getTotalAppliedSkuNumber(supplierId))
      dispatch(getTotalOnsaleSkuNumber(supplierId))
      dispatch(getTotalProductNumber(supplierId))
      dispatch(getTotalSkuNumber(supplierId))
    }
  }
}

export const updateCheckValid = (checkValid) => ({
  type: UPDATE_CHECK_VALID,
  payload: {
    checkValid: checkValid 
  }
})

export const onBrandNameChChange = (value) => ({
  type: ON_BRANDNAME_CH_CHANGE,
  payload: {
    brandNameCH: value
  }
})

export const onBrandNameEnChange = (value) => ({
  type: ON_BRANDNAME_EN_CHANGE,
  payload: {
    brandNameEN: value
  }
})

export const onProductNameChChange = (value) => ({
  type: ON_PRODUCTNAME_CH_CHANGE,
  payload: {
    productNameCH: value
  }
})

export const onProductNameEnChange = (value) => ({
  type: ON_PRODUCTNAME_EN_CHANGE,
  payload: {
    productNameEN: value
  }
})

export const onManufacturerChChange = (value) => ({
  type: ON_MANUFACTURER_CH_CHANGE,
  payload: {
    manufacturerCH: value
  }
})

export const onManufacturerEnChange = (value) => ({
  type: ON_MANUFACTURER_EN_CHANGE,
  payload: {
    manufacturerEN: value
  }
})

export const checkValid = (valid) => ({
  type: CHECK_VALID,
  payload: {
    valid: valid
  }
})

// export const changeCurrentBp = (bpId) => ({
//   type: CHANGE_CURRENT_BP,
//   payload: {
//     currentBP: bpId
//   }
// })

export const updateProduct = (supplierId, bpId, bpObj, message) => (dispatch) => {
  dispatch({type: REQUEST_UPDATE_PRODUCT_PENDING })
  // console.log('request for updating products', bpObj)
  const data = JSON.stringify(bpObj)

  axios.post(Url + '/u', {
    si: supplierId,
    bi: bpId,
    bp: data
  },{
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
      console.log(res, 'updateProduct')
      if (res) {
        dispatch({
          type: REQUEST_UPDATE_PRODUCT_SUCCESS
         // payload: {
           // updatedBP: res.data.products,
           // showProducts: res.data.products,
           // bpList: res.data.bpList,
           // currentBP: res.data.products.productNameEN
         // }
        })

        dispatch(loadManageProduct(supplierId))
        dispatch(loadProductStat(supplierId))
      } else {
        alert('unable to update product')
      }
  })
  .catch(err => dispatch({ type: REQUEST_UPDATE_PRODUCT_FAILED, payload: err }))   
}

export const addNewSku = (supplierId, bpId, bpObj, message) => (dispatch) => {
  dispatch({ type: REQUEST_ADD_SKU_PENDING })
  console.log('request for addNewSku',bpObj)
  const data = JSON.stringify(bpObj)
  
  axios.post(Url + '/u', {
    si: supplierId,
    bi: bpId,
    bp: data 
  })
  .then(res => {
      console.log('add skus response',res);

      if (res) {
        dispatch({
          type: REQUEST_ADD_SKU_SUCCESS,
          payload: {
            updatedBP: res.data.product,
            showProducts: res.data.product,
            bpList: res.data.bpList,
            currentBP: res.data.productNameEN,
          }
        })
        dispatch(loadManageProduct(supplierId))
        dispatch(loadProductStat(supplierId))
      } else {
        alert('unable to add SKU')
      }
  })
  .catch(err => dispatch({ type: REQUEST_ADD_SKU_FAILED, payload: err }))
  
}

export const addNewProduct = (supplierId, bpObj,message) => (dispatch) => {
  console.log('add new product bpobj', bpObj)
  const data = JSON.stringify(bpObj)
  
  dispatch({ type: REQUEST_ADD_PRODUCT_PENDING })

  axios.post(Url + '/c', {
    si: supplierId,
    bp: data
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    //todo arhtur change this
    if (res) {
      console.log(res, 'addNewproduct')
     
      dispatch({
        type: REQUEST_ADD_PRODUCT_SUCCESS
         // payload: {
         // bpList: res.data.bpList,
         // currentBP: res.data.products.productNameEN,
         // newBP: res.data.products,
         // showProducts: [res.data.products]
        //}
      })
      dispatch(loadManageProduct(supplierId))
      dispatch(loadProductStat(supplierId))
      toast(message)
    } else {
      alert('unable to add products')
    }
  })
  .catch(err => dispatch({ type: REQUEST_ADD_PRODUCT_FAILED, payload: err }))
}

export const removeExactBP = (supplierId, bpId) => (dispatch) => {
  console.log(supplierId, bpId)
  console.log('request remove bp');
  dispatch({ type: REQUEST_REMOVE_BP_PENDING })
  axios.post(Url + '/d', {
    si: supplierId,
    bi: bpId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    console.log('remove bps response', res)
    if (res) {
      dispatch({
        type: REQUEST_REMOVE_BP_SUCCESS
       // payload: {
        //  products: res.data.products,
        //  showProducts: res.data.products,
        //  bpList: res.data.bpList
        //}
      })

      dispatch(loadManageProduct(supplierId))
      dispatch(loadProductStat(supplierId))
    }
  })
  .catch(err => dispatch({ type: REQUEST_REMOVE_BP_FAILED , payload: err }))
}

export const getNextBPs = (supplierId, pageIndex) => (dispatch) => {

  dispatch({ type: REQUEST_NEXT_BPS_PENDING })
  axios.post(Url + '/np', {
    si: supplierId,
    ci: pageIndex
  },{
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
      
      if (res) {
        dispatch({
          type: REQUEST_NEXT_BPS_SUCCESS,
          payload: {
            products: res.data.products,
            showProducts: res.data.products,
            totalPages: res.data.totalPages,
            pageIndex: res.data.pageIndex
          }
        })
      }
  })
  .catch(err => dispatch({ type: REQUEST_NEXT_BPS_FAILED , payload: err }))
} 

export const getExactBP = (supplierId, bpId) => (dispatch) => {

  dispatch({ type: REQUEST_EXACT_BP_PENDING })

  axios.post(Url + '/gbp', {
    si: supplierId,
    bi: bpId
  }, {
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  })
  .then(res => {
    
    if (res) {
      dispatch({
        type: REQUEST_EXACT_BP_SUCCESS,
        payload: {
          currentBP: res.data.productNameEN,
          products: res.data,
          showProducts: res.data
        }
      })
    } else {
      alert('unable to add products')
    }
  })
  .catch(err => dispatch({ type: REQUEST_EXACT_BP_FAILED, payload: err }))
}

export const updateShowProduct = (target,result) => ({
  type: UPDATE_SHOW_PRODUCT,
  payload: {
    currentBP: target,
    showProducts: result
  }
})

export const updateControl = (obj) => ({
  type: UPDATE_CONTROL,
  payload: {
    data: obj
  }
})

export const updateMultiTheme = (multiTheme) =>({
  type: UPDATE_MULTITHEME,
  payload: {
    multiTheme: multiTheme
  }
})

export const updateInputs = (inputs) => ({
  type: UPDATE_INPUTS,
  payload: {
    inputs: inputs
  }
})