// import { ConnectedRouter } from 'connected-react-router'
import React, {useEffect} from 'react';
import {useDispatch ,useSelector } from 'react-redux';
import { fetchUser, getExactApplication } from './actions';
import axios from 'axios';
import {
	BrowserRouter as Router,
	Switch,
	Route,
} from "react-router-dom";
import ProductDashboard from './containers/ProductDashboard/index';
import VerifyProductInfo from './components/VerifyProductInfo/indexV2';
// import Template from './components/Template/Template';
import KeyProductStatsDashboard from './components/HomePage/KeyProductStatsDashboard';
import Product from './components/Product/Index';
import './App.css';
import './sass/app.scss';
// import './sass/main.scss';
import {DOMAIN_NAME} from '../app/constants/action-types'
import ScrollToTop from './ScrollToTop';

const App = () => {
  const dispatch = useDispatch()
  

	useEffect(() => {
    dispatch(fetchUser())
		const asyncCallback = async () => {
			const data = await axios.post( DOMAIN_NAME + '/c/ctx')
      if (data.data.p2mAppId.length > 0) dispatch(getExactApplication(data.data.p2mAppId))
		}
		asyncCallback()
	}, [])

  return (
    <Router>
       <ScrollToTop/> 
        <Switch>
          <Route exact path="/h" component={KeyProductStatsDashboard}/>
          <Route path="/product"><Product/></Route>
          {/* <Route path="/p2m/p/a"><Product/></Route> */}
          <Route exact path="/shipment/ivp" component={VerifyProductInfo}/>
          <Route path="/pd" component={ProductDashboard} /> 
        </Switch>
    </Router>
  );
}

export default App
