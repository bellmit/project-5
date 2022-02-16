import axios from 'axios';
import { resetWarningCache } from 'prop-types';
import { FETCH_USER_PENDING, FETCH_USER_FAILED,FETCH_USER_SUCCESS ,
  FETCH_SUPPLIERS_PENDING,FETCH_SUPPLIERS_SUCCESS,FETCH_SUPPLIERS_FAILED, DOMAIN_NAME, SHOW_LOADING} from '../constants/action-types'

export * from './manageProductAction'
export * from './manageP2MAction'

const Url = DOMAIN_NAME + '/c';

export const fetchUser = () => (dispatch) => {

  dispatch({ type: FETCH_USER_PENDING})

  axios.post(Url + '/u',{
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  }).then(res => {
    console.log(res.data);
    
    if (res != null) {
      dispatch({
        type: FETCH_USER_SUCCESS,
        payload: {
          user: res.data
        }
      })
    }
  }).catch(err => 
    dispatch({ 
      type: FETCH_USER_FAILED,
      payload: {
        error: err,
        // todo jinny for test
        // user: user
      }
  }))
}


export const fetchSimpleSupplierList = () => (dispatch) => {

  dispatch({ type: FETCH_SUPPLIERS_PENDING})
   
  axios.post(Url + '/sup/g',{
    headers: { 
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
    }
  }).then(res => {
    
    if (res != null) {

      const list = []
      res.data.map(item => {
        list.push({value: item, label: item})
      })
  
      dispatch({
        type: FETCH_SUPPLIERS_SUCCESS,
        payload: {
           suppliers: list
        }
      })
    }
  }).catch(err => 
    dispatch({ 
      type: FETCH_SUPPLIERS_FAILED,
      payload: {
        error: err
      }
  }))
}



export const showLoading = () => (dispatch) => {

  dispatch({
    type: SHOW_LOADING,
    payload: {
       showLoading: true
    }
  })
   

}

export const hideLoading = () => (dispatch) => {
  
  dispatch({
    type: SHOW_LOADING,
    payload: {
       showLoading: false
    }
  })
   

}