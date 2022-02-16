import React from 'react';
import { Switch, Route , Link } from 'react-router-dom';
import '../App.css';

// import ManageProduct from "./ManageProduct";
// import ServiceFee from "./ServiceFee";

// import Sample from "./OnboardingNewProduct/Sample";

// import TrailListProduct from "./OnboardingNewProduct/TrailListProduct";
// import CheckCompliance from "./OnboardingNewProduct/CheckCompliance";
// import CheckInsurance from "./OnboardingNewProduct/CheckInsurance";
// import CheckProfitability from "./OnboardingNewProduct/CheckProfitability";
// import ApproveSample from "./OnboardingNewProduct/ApproveSample";
// import EvaluateSample from "./OnboardingNewProduct/EvaluateSample";
// import GiveFeedbackByMS from "./OnboardingNewProduct/GiveFeedbackByMS";
// import GiveFeedbackBySC from "./OnboardingNewProduct/GiveFeedbackBySC";
// import PresentToMSAndSC from "./OnboardingNewProduct/PresentToMSAndSC";

// import ConfirmQuotation from "../components/Quotation/ConfirmQuotation";

// import MarketingActivityTable from "./MarketingActivity/MarketingActivityTable";
// import CreateMarketingActivity from "./MarketingActivity/CreateMarketingActivity";
// import EditMarketingActivity from "./MarketingActivity/EditMarketingActivity";

// import FulfillmentList from "./Shipment/FulfillmentList"
// import ExportShipmentList from "./Shipment/ExportShipmentList"
// import UnifiedShipmentList from "./Shipment/UnifiedShipmentList"
// import ExportBasicInfo from "./Shipment/ExportBasicInfo"
// import TransportationInformation from "./Shipment/TransportationInformation"
// import ItemDetails from "./Shipment/ItemDetails"
// import CheckProducts from "./Shipment/CheckProducts"
// import CheckPackages from "./Shipment/CheckPackages"
// import FinalizeRemarkedItems from "./Shipment/FinalizeRemarkedItems"
import ProductDashboard from './ProductDashboard/index';
// skeleton:
// import VerifyProductInfo from './VerifyProductInfo/indexV3';
// spinner:
import VerifyProductInfo from './VerifyProductInfo/indexV2';
// original:
// import VerifyProductInfo from './VerifyProductInfo/index';
import D3MapTest from './D3MapTest';
import Template from './Template/Template';
import HomePage from './HomePage/HomePage';

const Main = ({}) => (
	<div className="app">
	{/* <div id="bodyContentContainer" className="container">*/}
		{/* 
			<Link to={"/ri2"}>test link</Link>
			<Link to={"/drs-sys-web/sample"}>sample</Link>
		*/}
		<main>
		
			<Switch>
			<Route exact path="/" component={Template}/>
			<Route exact path="/home" component={HomePage}/>
			{/* { <Route exact path="/" component={D3MapTest} /> } */}		
			<Route exact path="/shipment/ivp" component={VerifyProductInfo} /> 
		    {/* <Route exact path="/" component={VerifyProductInfo}/> */}
			{/* <Route exact path='/' component={ProductDashboard}/> */}
			<Route path="/pd" component={ProductDashboard} /> 

				{/*<Route exact path="/drs-sys-web/ri" component={ManageProduct} />*/}
				
				{/* onboarding new product */}
				{/* <Route exact path="/drs-sys-web/oai/TrailListProduct" component={TrailListProduct} />
				<Route path={`match.path/tlp`} component={TrailListProduct} />
				<Route path={`match.path/es`} component={EvaluateSample} />
				
				<Route path="/oai/tlp" component={TrailListProduct} />
				<Route path="/oai/es" component={EvaluateSample} />
				
				<Route path="/oai/aps" component={ApproveSample} />
				
				<Route  path="/oai/pc" component={GiveFeedbackByMS} />
				<Route  path="/oai/gf" component={GiveFeedbackBySC} />
				<Route  path="/oai/p" component={PresentToMSAndSC} />
				
				<Route  path="/oai/ckc" component={CheckCompliance} />
				<Route  path="/oai/cki" component={CheckInsurance} />
				<Route  path="/oai/ckp" component={CheckProfitability} />*/}
				
				{/* quotation */}
				{/* <Route exact path="/drs-sys-web/q/cq" component={ConfirmQuotation} />
				<Route path="/ri2" component={TrailListProduct} />
				<Route path="/se" component={ServiceFee} />*/}

				{/* marketing activity */}
				{/* <Route exact path="/ms/create" component={CreateMarketingActivity} />
				<Route exact path="/ms/edit/:id" component={EditMarketingActivity} />
				<Route exact path="/ms" component={MarketingActivityTable} />*/}
				
				{/* shipment */}
				{/* <Route exact path="/shipment/fulfillmentList" component={FulfillmentList} />
				<Route exact path="/shipment/exportShipmentList" component={ExportShipmentList} />
				<Route exact path="/shipment/unifiedShipmentList" component={UnifiedShipmentList} />
				<Route exact path="/shipment/exportBasicInfo" component={ExportBasicInfo} />
				<Route exact path="/shipment/transportationInformation" component={TransportationInformation} />
				<Route exact path="/shipment/itemDetails" component={ItemDetails} />
				<Route exact path="/shipment/checkProducts" component={CheckProducts} />
				<Route exact path="/shipment/checkPackages" component={CheckPackages} />
				<Route exact path="/shipment/finalizeRemarkedItems" component={FinalizeRemarkedItems} />*/}
				
				
				
				
				{/* 
					<Route path="/ri2" component={ManageProduct} />
					<Route path="/drs-sys-web/sample" component={Sample} />
					<Route render={() => (<div>Miss</div>)} />		
	 			*/}	
	  
				
			</Switch>
	   
		</main>

	</div>
	
)


export default Main;
