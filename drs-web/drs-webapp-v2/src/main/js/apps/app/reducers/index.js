import { FETCH_USER_SUCCESS, FETCH_USER_FAILED, FETCH_USER_PENDING ,
  FETCH_SUPPLIERS_FAILED,FETCH_SUPPLIERS_SUCCESS,FETCH_SUPPLIERS_PENDING, SHOW_LOADING } from "../constants/action-types";


const defaultState = {
  u : {},
  supplierSelectOptions: [{ value: 'All', label: 'All'}],
  isPending: false,
  error: '',
  showLoading:false
}

export const manageDefaultReducer = (state = defaultState, action) => {
  switch (action.type) {
    
    case FETCH_USER_PENDING:
      return Object.assign({}, state, {
        isPending: true,
        // u: action.payload.user
      })
    case FETCH_USER_SUCCESS:
      return Object.assign({}, state, {
        u: action.payload.user,
        isPending: false
      })
    case FETCH_USER_FAILED:
      return Object.assign({}, state, {
        u: action.payload.user,
        isPending: false,
        error: action.payload.error
      })
    case FETCH_SUPPLIERS_PENDING:
        return Object.assign({}, state, {
          isPending: true
        })
    case FETCH_SUPPLIERS_SUCCESS:
        return Object.assign({}, state, {
          supplierSelectOptions: action.payload.suppliers,
          isPending: false
        })
    case FETCH_SUPPLIERS_FAILED:
        return Object.assign({}, state, {
          isPending: false,
          error: action.payload.error
        })
    case SHOW_LOADING:
          return Object.assign({}, state, {
            showLoading: action.payload.showLoading
    })
  
    default:
      return state;
  }
};
  
export default manageDefaultReducer;  