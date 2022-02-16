import React, { useContext,useEffect, } from 'react';
import '../../sass/navbar.scss';
import logo from '../../assets/images/DRS-color-logo-01.png';
import search from '../../assets/images/search-icon@2x.png';
import message from '../../assets/images/ic_message@2x.png';
import bell from '../../assets/images/ic-bell@2x.png';
// import help from '../../assets/images/ic-help@2x.png';
import chevronDown from '../../assets/images/chevron-down@2x.png';
import avatar from '../../assets/images/avatar@2x.png';
import { Link } from 'react-router-dom';
import { Context } from "../../Wrapper";

const Navbar = (props) => {
    const context = useContext(Context);
    useEffect(() => {
        console.log(props)
    })

    
        return (
            <header className="nav">
                <nav class="navbar navbar-expand-lg navbar-light">
                <div className="nav-left">
                    <div className="nav-logo-wrapper">
                        <img className="nav-logo" src={logo} alt="DRS-logo"/>
                    </div>
                    <ul className="nav-menu">
                        <li className="nav-menu-item"><span>首頁</span></li>
                        <li className="nav-menu-item">
                            <Link to='/product'><span>產品</span></Link>
                        </li>
                        <li className="nav-menu-item"><span>訂單</span></li>
                        <li className="nav-menu-item"><span>物流</span></li>
                        <li className="nav-menu-item"><span>客戶服務</span></li>
                        <li className="nav-menu-item"><span>帳務</span></li>
                        <li className="nav-menu-item"><span>報告</span></li>
                        <li className="nav-menu-item"><span>行銷</span></li>
                    </ul>
                    <select value = {context.locale} onChange={context.selectLanguage}>
                        <option value= 'en'>English</option>
                        <option value= 'zh-TW'>Zh-TW</option>
                    </select>
                    
                </div>
                
                <div className="actions-wrapper">
                    {/* <div className="search-wrapper">
                        <div className="icon-wrapper">
                            <img className="icon" src={search} alt="search-icon"/>
                        </div>
                        <input type="text" className="search-input" placeholder="Search.."/>
                    </div>
                    <div className="icon-wrapper">
                        <img className="icon" src={message} alt="message"/>
                    </div> */}
                    <div className="icon-wrapper">
                        <img className="icon" src={bell} alt="bell"/>
                    </div>
                    {/* <div className="icon-wrapper">
                        <img className="icon" src={help} alt='help'/>
                    </div> */}
                    <div className="user-wrapper">
                        <img className="avatar" src={avatar} alt="avatar"/>
                        <span className="username">user name</span>
                        <img className="icon" src={chevronDown} alt="chevron-down"/>
                    </div>

                </div>
                </nav>
            </header>
        )
    }


export default (Navbar);

