import React, {useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Switch, Route , Link, useRouteMatch, Redirect} from 'react-router-dom';
import P2MApplication from './P2MProcess/P2MApplication';
import ManageP2M from './P2MProcess/Index'
import ManageProduct from './ManageProduct/Index'
import MarketplaceInfo from './P2MProcess/MarketplaceInfo';
import Insurance from './P2MProcess/Insurance';
import Regional from './P2MProcess/Regional';
import Shipping from './P2MProcess/Shipping';
import ProductInfo from './P2MProcess/ProductInfo';
import ProductBreadcrumbs from './P2MProcess/ProductBreadcrumbs';
import {FormattedMessage} from 'react-intl';
import {updateRedirectAp} from '../../actions/index';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '../../sass/product.scss'
import '../../sass/loading.scss';
import '../../sass/sidebar.scss';
import '../../sass/toaster.scss';

const Product = () => {
        let match = useRouteMatch();
        const p2m = useSelector(state => state.P2M)
        const dispatch = useDispatch();
        const [showCollapseBtn, setShowCollapseBtn] = useState(true)
        const [showSideBar, setShowSideBar] = useState(false)
        // const [state={notLoaded:true}, setState] = useState(null);

        const handleSideBarCollapse = () => {
            showSideBar ? setShowSideBar(false) : setShowSideBar(true)   
        }
        // const handleShowCollapseBtn = () => {
        //     showCollapseBtn ? setShowCollapseBtn(false) : setShowCollapseBtn(true)
        // }
        // const context = useContext(Context);
	    // useEffect(() => {
        //     console.log('init Product')
	    // }, [])
	
        return (
            <div>
                <ToastContainer
                    position="top-right"
                    autoClose={3000}
                    hideProgressBar={false}
                    newestOnTop={false}
                    closeOnClick
                    rtl={false}
                    pauseOnFocusLoss
                    draggable
                    pauseOnHover
                />
                <div className={`sidebar-wrapper ${showSideBar ? '' : 'transform'}`}>
                    <button className="sidebar-collapse-btn" onClick={handleSideBarCollapse} style={showCollapseBtn ? null : {display: 'none'}}>
                        {  showSideBar
                            ?   <i class="fa fa-angle-left" aria-hidden="true"></i>
                            :   <i class="fa fa-angle-right" aria-hidden="true"></i>
                        }
                    </button>
                    <ul className='sidebar-nav'>
                        <li className="sidebar-item sidebar-item-title">
                            <Link to={`${match.url}`}>
                                <span><FormattedMessage id="product.management"/></span>
                            </Link>
                        </li>
                        <li className="sidebar-item sidebar-item-title" onClick={() => dispatch(updateRedirectAp(''))}>
                            <Link to={`${match.url}/apply`}>
                                <span><FormattedMessage id="p2m.management"/></span>
                            </Link>      
                        </li>
                        {/* <select value = {context.locale} onChange={context.selectLanguage}>
                            <option value= 'en'>English</option>
                            <option value= 'zh-TW'>Zh-TW</option>
                        </select> */}
                    </ul>
                </div>
                <div className={`app-main-pannel ${showSideBar ? '': 'sidebar-close-width'}`}>
                    <Switch>
                        {/* <Route path='p2m/p/a/:topicId'><P2MApplication/></Route> */}
                        <Route exact path={match.path}>
                            { p2m.redirectTo == 'P2MApplication'
                                ? <Redirect to="/product/application"/>
                                : <ManageProduct/>
                            }
                        </Route>
                        <Route exact path={`${match.path}/apply`}>
                            { p2m.redirectTo == 'P2MApplication'
                                ? <Redirect to="/product/application"/>
                                : <ManageP2M/>
                            }
                        </Route>
                        <Route exact path={`${match.path}/application`}>
                            <ProductBreadcrumbs/>
                            { p2m.redirectTo == 'manageP2M'
                                ? <Redirect to="/product/apply"/>
                                : <P2MApplication/>
                            }
                        </Route>
                        <Route exact path={`${match.path}/application/marketplace`}>
                            <ProductBreadcrumbs/>
                            <MarketplaceInfo/>
                        </Route>
                        <Route exact path={`${match.path}/application/insurance`}>
                            <ProductBreadcrumbs/>
                            <Insurance/>
                        </Route>
                        <Route exact path={`${match.path}/application/regional`}>
                            <ProductBreadcrumbs/>
                            <Regional/>
                        </Route>
                        <Route exact path={`${match.path}/application/shipping`}>
                            <ProductBreadcrumbs/>
                            <Shipping/>
                        </Route>
                        <Route exact path={`${match.path}/application/productinfo`}>
                            <ProductBreadcrumbs/>
                            <ProductInfo/>
                        </Route>
                    </Switch>
                </div>
            </div>
        )
}

export default Product;
