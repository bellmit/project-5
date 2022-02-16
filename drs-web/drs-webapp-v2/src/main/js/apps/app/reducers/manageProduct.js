import {
  REQUEST_UPDATE_PRODUCT_PENDING,
  REQUEST_UPDATE_PRODUCT_SUCCESS,
  REQUEST_UPDATE_PRODUCT_FAILED,
  UPDATE_SHOW_PRODUCT,
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
  FETCH_SIMPLE_PRODUCT_LIST_SUCCESS,
  // UPDATE_PRODUCT_CATEGORY
} from '../constants/action-types'

const productState = {
  isPending: false,
  products: [],
  totalPages: 0 ,
  pageIndex : 1,
  error: '',
  // supplierId: '',
  showProducts: [],
  currentBP: 'All', 
  bpSelectOptions: [{ value: 'All', label: 'All'}],
  multiTheme: [],
  inputs: [],
  valid: false,
  brandNameCH: '',
  brandNameEN: '',
  productNameCH: '',
  productNameEN: '',
  manufacturerCH: '',
  manufacturerEN: '',
  checkValid: [
    // { key: 'brandNameCH', valid: false },
    // { key: 'brandNameEN', valid: false },
    // { key: 'productNameCH', valid: false },
    // { key: 'productNameEN', valid: false }
  ],
  productCategory: [],
  // productCategory: [{_id: 'Appliances'}, {_id: 'Arts, Crafts & Sewing'},{_id: 'Automotive'}, {_id: 'Baby Products'}, {_id: 'Beauty & Personal Care'},
  // {_id: 'Books'}, {_id: 'CDs & Vingl'}, {_id: 'Cell Phones & Accessories'}, {_id: 'Clothing, Shoes & Jewelry'}, {_id: 'Collectibles & Fine Art'},
  // {_id: 'Electronics'}, {_id: 'Grocery & Gourmet Food'}, {_id: 'Handmade Products'}, {_id: 'Health & Household'}, {_id: 'Home & Kitchen'}, 
  // {_id: 'Industrial & Scientific'}, {_id: 'Movies & TV'}, {_id: 'Musical Instruments'}, {_id: 'Office Products'}, {_id: 'Patio, Lawn & Garden'},
  // {_id: 'Pet Supplies'}, {_id: 'Software'}, {_id: 'Sports & Outdoors'}, {_id: 'Tools & Home Improvement'}, {_id: 'Toys & Games'}, {_id: 'Video Games'} ],
  currentCategory: '',
  loading: false,
  tpn:0,
  tsn:0,
  tasn:0,
  tossn:0
}

export const manageProductReducer = (state = productState, action) => {
  switch (action.type) {
    case FETCH_TOTAL_PRODUCT_NUMBER_PENDING:
      return Object.assign({}, state, {
        isPending: true,
      })
    case FETCH_TOTAL_PRODUCT_NUMBER_SUCCESS:
      return Object.assign({}, state, {
        tpn: action.payload.totalProducts,
        isPending: false
      })
    case FETCH_TOTAL_PRODUCT_NUMBER_FAILED:
      return Object.assign({}, state, {
        isPending: false,
        error: action.payload.error
      })

    case FETCH_TOTAL_SKU_NUMBER_PENDING:
      return Object.assign({}, state, {
        isPending: true,
      })
    case FETCH_TOTAL_SKU_NUMBER_SUCCESS:
      return Object.assign({}, state, {
        tsn: action.payload.totalSkus,
        isPending: false
      })
    case FETCH_TOTAL_SKU_NUMBER_FAILED:
      return Object.assign({}, state, {
        isPending: false,
        error: action.payload.error
      })    
    case FETCH_TOTAL_APPLIED_SKU_NUMBER_PENDING:
      return Object.assign({}, state, {
        isPending: true,
      })
    case FETCH_TOTAL_APPLIED_SKU_NUMBER_SUCCESS:
      return Object.assign({}, state, {
        tasn: action.payload.totalSkus,
        isPending: false
      })
    case FETCH_TOTAL_APPLIED_SKU_NUMBER_FAILED:
      return Object.assign({}, state, {
        isPending: false,
        error: action.payload.error
      })
    case FETCH_TOTAL_ONSALE_SKU_NUMBER_PENDING:
        return Object.assign({}, state, {
          isPending: true,
    })
    case FETCH_TOTAL_ONSALE_SKU_NUMBER_SUCCESS:
        return Object.assign({}, state, {
          tossn: action.payload.totalSkus,
          isPending: false
    })
    case FETCH_TOTAL_ONSALE_SKU_NUMBER_FAILED:
        return Object.assign({}, state, {
          isPending: false,
          error: action.payload.error
    })          
    case UPDATE_CURRENT_PRODUCT_CATEGORY:
      return Object.assign({}, state, {
        currentCategory: action.payload.currentCategory
      })
    case FETCH_PRODUCT_CATEGORY_SUCCESS:
      return Object.assign({}, state, {
        isPending: false,
        productCategory: action.payload.productCategory,
        loading: false
      })
    case FETCH_PRODUCT_CATEGORY_PENDING:
      return Object.assign({}, state, {
        isPending: true,
        loading: true
      })
    case FETCH_PRODUCT_CATEGORY_FAILED:
      return Object.assign({}, state, {
        isPending: false,
        error: action.payload.error,
        loading: false
      })
    case UPDATE_CHECK_VALID:
      return Object.assign({}, state, {
        checkValid: action.payload.checkValid
      })
    case ON_BRANDNAME_CH_CHANGE:
      return Object.assign({}, state, {
        brandNameCH: action.payload.brandNameCH
      })
    case ON_BRANDNAME_EN_CHANGE:
      return Object.assign({}, state, {
        brandNameEN: action.payload.brandNameEN
      })
    case ON_PRODUCTNAME_CH_CHANGE:
      return Object.assign({}, state, {
        productNameCH: action.payload.productNameCH
      })
    case ON_PRODUCTNAME_EN_CHANGE:
      return Object.assign({}, state, {
        productNameEN: action.payload.productNameEN
      })
    case ON_MANUFACTURER_CH_CHANGE:
      return Object.assign({}, state, {
        manufacturerCH: action.payload.manufacturerCH
      })
    case ON_MANUFACTURER_EN_CHANGE:
      return Object.assign({}, state, {
        manufacturerEN: action.payload.manufacturerEN
      })
    case CHECK_VALID:
      return Object.assign({}, state, {
        valid: action.payload.valid
      })
    case REQUEST_REMOVE_BP_PENDING:
      return Object.assign({}, state, { isPending: true })
    case REQUEST_REMOVE_BP_SUCCESS:
      return Object.assign({}, state, {
        isPending: false
        //products: action.payload.products,
        //showProducts: action.payload.showProducts,
        //currentBP: 'All',
        //bpOptions: action.payload.bpList
      })
    case REQUEST_REMOVE_BP_FAILED:
      return Object.assign({}, state, { error: action.payload, isPending: false })
    
    case REQUEST_NEXT_BPS_PENDING:
      return Object.assign({}, state, { isPending: true })
    case REQUEST_NEXT_BPS_SUCCESS:
      return Object.assign({}, state, {
        isPending: false,
        products: [...state.products].concat(action.payload.products),
        //todo arthur remove showProducts
        showProducts: [...state.showProducts].concat(action.payload.showProducts),
        pageIndex:action.payload.pageIndex,
        totalPages:action.payload.totalPages,
        currentBP: action.payload.currentBP
      })
    case REQUEST_NEXT_BPS_FAILED:
      return Object.assign({}, state, { error: action.payload, isPending: false })
    case REQUEST_EXACT_BP_PENDING:
      return Object.assign({}, state, { isPending: true })
    case REQUEST_EXACT_BP_SUCCESS:
      return Object.assign({}, state, {
        isPending: false,
        products: [action.payload.products],
        showProducts: [action.payload.showProducts],
        currentBP: action.payload.currentBP
      })
    case REQUEST_EXACT_BP_FAILED:
      return Object.assign({}, state, { error: action.payload, isPending: false })
    case REQUEST_UPDATE_PRODUCT_PENDING:
      return Object.assign({}, state, {isPending: true})
    case REQUEST_UPDATE_PRODUCT_SUCCESS:      
      return Object.assign({}, state, {
        isPending: false
        //products: [action.payload.updatedBP],
        //showProducts: [action.payload.showProducts],
        //currentBP: action.payload.currentBP,
        //bpOptions: action.payload.bpList
      })
    case REQUEST_UPDATE_PRODUCT_FAILED:
      return Object.assign({}, state, {
        isPending: false,
        error: action.payload
      })
    case UPDATE_SHOW_PRODUCT:
      return Object.assign({}, state, {
        currentBP: action.payload.currentBP,
        showProducts: action.payload.showProducts
      })
    case REQUEST_ADD_SKU_PENDING:
      return Object.assign({}, state, {
        isPending: true
      })
    case REQUEST_ADD_SKU_SUCCESS:
      return Object.assign({}, state, {
        isPending: false,
        products: [action.payload.updatedBP],
        showProducts: [action.payload.showProducts],
        currentBP: action.payload.currentBP,
        bpSelectOptions: action.payload.bpList
      })
    case REQUEST_ADD_SKU_FAILED:
      return Object.assign({}, state, {
        isPending: false,
        error: action.payload
      })
    case REQUEST_ADD_PRODUCT_PENDING:
      return Object.assign({}, state, { isPending: true })
    case REQUEST_ADD_PRODUCT_SUCCESS:
      return Object.assign({}, state, {
        isPending: false
       // products: [...state.products, action.payload.newBP],
       // showProducts: action.payload.showProducts,
       // currentBP: action.payload.currentBP,
       // bpOptions: action.payload.bpList
      })
    case REQUEST_ADD_PRODUCT_FAILED:
      return Object.assign({}, state, { error: action.payload, isPending: false })
    case REQUEST_PRODUCT_PENDING:
      return Object.assign({}, state, { isPending: true })
    case REQUEST_PRODUCT_SUCCESS:
      return Object.assign({}, state, {
        isPending: false,
        products: action.payload.products,
        totalPages: action.payload.totalPages,
        pageIndex: action.payload.pageIndex,
        showProducts: action.payload.products,
        currentBP: 'All'
      })
    case REQUEST_PRODUCT_FAILED:
      return Object.assign({}, state, { error: action.payload, isPending: false })
    case FETCH_SIMPLE_PRODUCT_LIST_SUCCESS:
        return Object.assign({}, state, {
          bpSelectOptions: action.payload.bpList
    })
    case UPDATE_MULTITHEME:
      return Object.assign({}, state, {
        multiTheme: action.payload.multiTheme
      })
    case UPDATE_INPUTS:
      return Object.assign({}, state, {
        inputs: action.payload.inputs
      })
    // case REQUEST_EXACT_BP_PENDING:

    //   return Object.assign({}, state, { isPending: true })
    // case REQUEST_EXACT_BP_SUCCESS:
    //   console.log('request exact success.....')
    //   return Object.assign({}, state, {
    //     isPending: false,
    //     products: action.payload.products,
    //     showProducts: action.payload.showProducts,
    //     currentBP: action.payload.currentBP,
    //   })
    // case REQUEST_EXACT_BP_FAILED:
    //   return Object.assign({}, state, { error: action.payload, isPending: false })
    // case UPDATE_PRODUCT_DATA:
    //   return [...state, Object.assign({}, action.payload.data)]
    default:
      return state;
  }
};

// export const manageShowProductReducer = (state = showProducts, action) => {
//   switch (action.type) {
//     case UPDATE_SHOW_PRODUCT:
//       return action.payload.data
//     default:
//       return state
//   }
// }

// const bpOptions = [ { value: 'All', label: 'All'} ]

// export const manageBPoptionsReducer = (state = bpOptions, action) => {
//   switch (action.type) {
//     case UPDATE_BP_OPTIONS:
//       return action.payload.data
//     default:
//       return state
//   }
// }

// const currentBP = 'All';
// export const manageCurrentBPReducer = (state = currentBP, action) => {
//   switch (action.type) {
//     case UPDATE_CURRENT_BP:
//       return action.payload.data
//     default:
//       return state
//   }
// }
