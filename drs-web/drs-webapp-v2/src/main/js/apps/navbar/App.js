import React, {useState , useEffect , useContext } from 'react';
import axios from 'axios';
import {DOMAIN_NAME} from './constants/index'
import './navbar.scss';
import {FormattedMessage} from 'react-intl';
// import {Context} from "./Wrapper";
// import DrsIcon from "../../../webapp/resources/bundle/images/2785fed332f78cb45f9f70703d9fc021.png"
// import DrsIcon from '../../../webapp/resources/assets/img/drs_logo2-removebg.png';
//import { FaBell } from "react-icons/fa";
// import '../app/sass/notification.scss'
import NotifyMe from './NotifyMe'
// const local = navigator.language;

// let lang;
// if (local === 'en') {
//     console.log(local)
//     lang = EN;
// }else {
//     console.log(local)
//     if (local === 'zh-TW') {
//         console.log('check')
//         lang = TW;
//         console.log(lang)
//     }
// }

const NavBar = (props) => {
    // const {user} = props
    //const context = useContext(Context);   
    //const [data, setData] = useState([]);
    //const yourOwnFunctionHandler = () =>{
      //  console.log("read")
    //}

    const [user, setUser] = useState({});

    useEffect(() => {

        axios.post(DOMAIN_NAME + '/c/u',{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
          }).then(res => {
            if (res != null) {
                setUser(res.data)
                console.log(res.data)
            }
          })

    }, [])

    useEffect(() => {
        console.log('props',props)
    },[])
  
  return (
    // <IntlProvider messages={messages} locale={locale}>
    <div>
      <header id="header" class="nav">
        <nav class="navbar navbar-expand-lg navbar-light">
            <div class="nav-logo-wrapper">
                {/* <a href="/"><img class="nav-logo" id="logo" src={DrsIcon} alt="drs-logo"/></a> */}
                <a href="/"><img class="navbar-brand" id="logo" src="/resources/images/drs_logo2.png"/></a>
            </div>

            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarSupportedContent" style={{marginLeft: '2%'}}>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                            <FormattedMessage id="header.products"/>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            {user.enableSS ?
                                    <a class="dropdown-item" href="/product">
                                        <FormattedMessage id="header.manageProducts"/>
                                    </a>

                            :
                            <a class="dropdown-item" href="/Products">
                            <FormattedMessage id="header.ProductsInformationMaintenance"/>
                            </a>
                            } 
                            {!user.enableSS ?
                                <a class="dropdown-item" href="/NewDevelopingProductList">
                                    <FormattedMessage id="header.manageProducts"/>
                                    </a>
                                :null
                            }

                            { !user.isSp ? 
                                <a class="dropdown-item" href="/asin">
                                      <FormattedMessage id="header.asinMapping"/>
                                </a>
                            : null}
                            { !user.isSp ? 
                            <a class="dropdown-item" href="/pd"> Product Dashboard (Internal)</a>
                            : null}

                            { !user.isSp ? 
                            <a class="dropdown-item" href="/product">
                                <FormattedMessage id="header.manageProducts"/>
                             </a>
                            : null}
                            
                        </div>
                    </li>

                    <li class="nav-item">
                        <a class="nav-link" href="/CustomerOrderList">
                            <FormattedMessage id="header.orders"/>
                        </a>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <FormattedMessage id="header.logistics"/>
                        </a>

                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a  class="dropdown-item" href="/ReplenishmentPlanning">
                                <FormattedMessage id="header.replenishmentPrediction"/>
                            </a>
                            <a class="dropdown-item" href="/InventoryShipments">
                                <FormattedMessage id="header.inventoryShipments"/>
                            </a>
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/UnifiedShipments">
                                <FormattedMessage id="header.unifiedShipments"/>
                                </a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/replenishment-information">
                                <FormattedMessage id="header.replenishmentInformation"/>
                                </a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/updateProductSkuFBAInventoryAvailability">
                                <FormattedMessage id="header.inventoryAvailability"/>
                                </a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/afi">FBA Inventory (beta - internal)</a>
                            : null}
                            { !user.isSp ? 
                                <a  class="dropdown-item" href="/bwinv">Biweekly Inventory (beta - internal)</a>
                            : null}
                        </div>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <FormattedMessage id="header.customerCare"/>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a  class="dropdown-item" href="/CustomerCareCases">
                                <FormattedMessage id="header.customerCareCase"/>
                            </a>
                            <a class="dropdown-item" href="/Issues">
                                <FormattedMessage id="header.issues"/>
                            </a>
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/cci">Customer Care Cases Issues (beta - internal)</a>
                            : null}
                        </div>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <FormattedMessage id="header.accounting"/>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="/statements">
                                <FormattedMessage id="header.ss2spStatements"/>
                            </a>
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/MS2SS-Statements/received">
                                <FormattedMessage id="header.intercompanyStatementsReceived"/>
                                </a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/MS2SS-Statements/issued" >
                                <FormattedMessage id="header.intercompanyStatementsIssued"/>
                                </a>                                
                            : null}

                            { !user.isSp ? 
                                <a class="dropdown-item" href="/Remittance">
                                <FormattedMessage id="header.remittance"/>
                                </a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/DomesticTransactions">
                                <FormattedMessage id="header.domesticTransaction"/>
                                </a>                                                            
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/InternationalTransactions">
                                <FormattedMessage id="header.internationalTransaction"/>
                                </a>                                                            
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/ImportDutyTransaction">
                                <FormattedMessage id="header.importDutyTransaction"/>
                                </a>                                
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/draft-statements">
                                <FormattedMessage id="header.draftStatements"/>
                                </a>                                
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/ProfitCostShareStatements">
                                <FormattedMessage id="header.profitCostShareStatements"/>
                                </a>                                
                            : null}

                            { !user.isSp ? 
                                <a class="dropdown-item" href="/ProcessCouponRedemptionFee">
                                <FormattedMessage id="header.processCouponRedemptionFee"/>
                                </a>                                
                            : null}

                            { !user.isSp ? 
                                <a class="dropdown-item" href="/ProcessAdSpend">Process Ad Spend (Internal)</a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/accinv">Invoice (beta - internal)</a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/niinv">Non-issued Invoice (beta - internal)</a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/amzse">Amazon Settlement Report (beta - internal)</a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/araging">AR Aging (internal)</a>
                            : null}
                            
                        </div>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
                            <FormattedMessage id="header.report"/>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a  class="dropdown-item" href="/customer-satisfaction">
                            <FormattedMessage id="customerSatisfaction.title"/>
                                </a>
                            <a class="dropdown-item" href="/SalesAndPageTrafficReport">
                                <FormattedMessage id="header.salesAndPageTrafficReport"/>
                            </a>
                            <a class="dropdown-item" href="/storage-fee">
                                <FormattedMessage id="header.storageFee"/>
                            </a>
                            <a class="dropdown-item" href="/inventory-health-report">
                            <FormattedMessage id="inventoryHealthReport.title"/>
                            </a>
                            { !user.isSp ? 
                            <a class="dropdown-item" href="/ImportAmazonReport">
                            <FormattedMessage id="header.importInventoryReport"/>
                            </a>                                
                            : null}
                            { !user.isSp ? 
                            <a class="dropdown-item" href="/emailReminder">
                            <FormattedMessage id="header.emailReminder"/>
                            </a>                            
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/sdb">Sales Dashboard (beta - internal)</a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/cpdb">Campaign Performance Dashboard (beta - internal)</a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/rv">Amazon Review (beta - internal)</a>
                            : null}                            
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/re">Amazon Return (beta - internal)</a>
                            : null}                            
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/sab">Sales Board (alpha - internal)</a>
                            : null}
                            { !user.isSp ? 
                                <a class="dropdown-item" href="/sks">Supplier key Stats (internal)</a>
                            : null}
                            
                        </div>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                            aria-haspopup="true" aria-expanded="false">
                            <FormattedMessage id="header.marketingActivities"/>
                        </a>
                        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="/sku-adv-performance-report">
                                <FormattedMessage id="header.skuAdvertisingPerformanceReport"/>
                            </a>
                            <a class="dropdown-item" href="/SearchTermReport">
                                <FormattedMessage id="header.searchTermReport"/>
                            </a>
                            <a class="dropdown-item" href="/sku-adv-eval-report">
                                <FormattedMessage  id="header.skuAdvertisingEvaluationReport"/>
                            </a>
                            <a  class="dropdown-item" href="/AmazonSponsoredBrandsCampaignReport">
                                <FormattedMessage id="header.sponsoredBrandsCampaign"/>
                            </a>
                            <a  class="dropdown-item" href="/AmazonSponsoredBrandsCampaign">
                                <FormattedMessage id="header.sponsoredBrandsCampaignCP"/>
                            </a>
                            { !user.isSp ? 
                                <a  class="dropdown-item" href="/ms"> Marketing Activity (internal)</a>
                            : null}                            
                            { !user.isSp ? 
                                <a  class="dropdown-item" href="/MarketingReports">
                                <FormattedMessage id="header.importMarketingReport"/>
                                </a>                                
                            : null}

                        </div>
                    </li>
                </ul>

             {/*todo arthur */}
            {/* <ul class="navbar-nav">
                 

             
                </ul> */}
                
            </div>
            <div style={{display: 'flex', alignItems: 'center'}}>
                {/* <select value = {context.locale} onChange={context.selectLanguage}>
                    <option value= 'en'>English</option>
                    <option value= 'zh-TW'>Zh-TW</option>
                </select> */}

                <ul class="navbar-nav" style={{marginLeft: '12px', alignItems: 'center'}}>
                    
                    <li class="nav-item dropdown">
                        <a class="nav-link" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i id="user_icon" class="fas fa-user-circle fa-lg"></i>
                        </a>
                        <div class="dropdown-menu dropdown-menu-left" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" href="#" >{user.name}</a>
                            <div class="dropdown-divider"></div>
                            { !user.isSp ? 
                            <a class="dropdown-item" href="th:text='Joomla_root_link' />/account/article-management">
                            <FormattedMessage id="header.companyInfoMaintenance"/>
                            </a>                          
                            : null}


                            <a class="dropdown-item" href="esources/files/DRSChannelParticipationAgreement-zh_TW.pdf">
                                <FormattedMessage id="header.agreement"/>
                                </a>
                            { !user.isSp ? 
                            <a class="dropdown-item" href="Joomla_root_link/account/drs_typography">
                            <FormattedMessage id="header.drs-typography"/>
                            </a>                      
                            : null}    

                            <a class="dropdown-item"href="/logout">
                                <FormattedMessage id="header.logout"/>
                                </a>
                        </div>
                    </li>          

                    <li class="nav-item dropdown">

                         <NotifyMe
                            // data={data}
                            storageKey='notific_key'
                            notific_key='timestamp'
                            notific_value='content'
                            notific_link='refUrl'
                            //notific_link='content'
                            //notific_index="currentIndex"
                            notific_read="read"
                            notific_clear="clear"
                            heading='Notifications'
                            sortedByKey={false}
                            showDate={true}
                            size={20}
                            topic='topic'
                            uid = {user.uid}
                            // color="white"
                            //markAsReadFn={() => yourOwnFunctionHandler()}
                            // getNext={getNext}
                        /> 
                        
                        {/* <NotifyMe
                        data={data}
                        storageKey='notific_key'
                        notific_key='timestamp'
                        notific_value='update'
                        heading='Notification Alerts'
                        sortedByKey={false}
                        showDate={true}
                        size={64}
                        color="yellow"
                        markAsReadFn={() => yourOwnFunctionHandler()}
                        /> */}
                    </li>
                </ul>
            </div>
        </nav>
      </header>
    </div>
    // </IntlProvider>
  );
}

export default NavBar