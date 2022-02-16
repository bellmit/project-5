import React from 'react';
import ReactDOM from 'react-dom';
import ErrorBoundary from "./ErrorBoundary";
import Wrapper from "./Wrapper";
import { BrowserRouter as Router, Route , IndexRoute ,Switch} from 'react-router-dom';

//import '../../node_modules/bootstrap/dist/css/bootstrap.min.css';
//import 'bootstrap/dist/css/bootstrap.min.css';

//import './scss/app.scss';

import './App.css'
import App from './App'

//import msgi18n from "./msgi18n";

import { Provider ,connect} from "react-redux"
import {store,history }from "./store/index";

ReactDOM.render((
	<Provider store={store}>
		<ErrorBoundary>
			<Wrapper> 
				<App history={history} />
			</Wrapper> 
		</ErrorBoundary>
	</Provider>
		  
), document.getElementById('root'));
