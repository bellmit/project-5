import {
    UPDATE_CONTROL
} from '../constants/action-types'

const initialState = {
    title: '產品管理',
    btngroups: [
        { name: '新建產品', path: '/product/add', action: 'addNewProduct'}
        // { name: '我要申請出貨', path: '/', action: 'cancel' },
        // { name: '我要申請上架', path: '/product', action: 'applyforNPO' },
        // { name: '新建產品', path: '/product', action: 'addNewProduct' }
    ],
   
}

const manageControlReducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_CONTROL:
            return Object.assign({}, state, action.payload.data)
        default:
            return state
    }
}

export default manageControlReducer;
