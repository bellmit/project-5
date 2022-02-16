// this component is not using

// import React, { useEffect , useState } from 'react';
// import {useDispatch ,useSelector } from 'react-redux';
// import {
// 	BrowserRouter as Router,
// 	Switch,
// 	Route,
// 	Link,
// 	useParams,
// 	useRouteMatch ,useHistory
//   } from "react-router-dom";
// import '../App.css';
// import '../sass/app.scss';
// import Navbar from './Navbar/Navbar'
// import Sidebar from './Sidebar/Sidebar'

// import ProductDashboard from '../containers/ProductDashboard/index';
// import VerifyProductInfo from './VerifyProductInfo/indexV2';

// import Template from './Template/Template';
// import HomePage from './HomePage/HomePage';
// import Product from './Product/Index'
// import { fetchUser, getExactApplication } from '../actions';
// import axios from 'axios';

// const Main = () => {

// 	useEffect(() => {
// 		console.log('Main init')
// 	}, [])

// 	//const [state={notLoaded:true}, setState] = useState(null);

// 	const dispatch = useDispatch()   
// 	const p2m = useSelector(state => state.P2M)

// 	useEffect(() => {
// 		dispatch(fetchUser())

// 		const asyncCallback = async () =>{
// 			const data = await axios.post('http://localhost:8080/c/ctx')
// 			// console.log(data.data.p2mAppId)
// 			dispatch(getExactApplication(data.data.p2mAppId))
// 		//   console.log(p2m.currentAp.subApplication, 'p2m currentAp subApplication')
// 		}
		
// 		asyncCallback()		
// 	}, [])

// 	// let history = useHistory();
// 	// if(p2m.currentAp.subApplication != null) {
// 	// 	console.log('history push', history)
// 	//  	history.push("/product");
// 	// }
	
// 	return (
	
// 		<div className="app">
// 			<main>
// 				<div className="template">
// 					<Navbar/>
// 					{/* <Sidebar/> */}
				
// 				</div>
// 			</main>
// 		</div>

// )}

// export default Main;
