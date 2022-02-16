import { combineReducers } from 'redux'
import { connectRouter } from 'connected-react-router'
import {manageProductReducer,} from "./manageProduct";
import manageControlReducer from './manageControl';
import { manageP2MReducer } from './manageP2M';
import { manageDefaultReducer } from './index';

const rootReducer = (history) => combineReducers({
   PD: manageProductReducer,
   P2M: manageP2MReducer,
   d:manageDefaultReducer,
   manageControl: manageControlReducer,
   router: connectRouter(history)
})

export default rootReducer
