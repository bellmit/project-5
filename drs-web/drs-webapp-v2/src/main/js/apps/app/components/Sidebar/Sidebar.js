import React, { Component } from 'react';
import '../../sass/sidebar.scss';
import home from '../../assets/images/home-icon@2x.png';
import product from '../../assets/images/product-icon@2x.png';
import orders from '../../assets/images/ic_inbox@2x.png';
import logistics from '../../assets/images/ic_invoice@2x.png';
import customerService from '../../assets/images/ic_users@2x.png';
import accounting from '../../assets/images/ic_message@2x.png';
import reports from '../../assets/images/ic_calendar@2x.png';
import marketing from '../../assets/images/ic_help@2x.png';

class Sidebar extends Component {
    constructor() {
        super();
        this.state = {}
    }

    render() {
        return (
            <div className="sidebar-wrapper">
                <div className="sidebar-btn-wrapper">
                    <button className=""></button>
                </div>
                <ul className="sidebar-nav">
                    <li className="sidebar-item">
                        <div className="sidebar-item-icon-wrapper">
                            <img className="sidebar-item-icon" src={home} alt="homepage"/>
                        </div>
                        <span>首頁</span>
                    </li>
                    
                    <li className="sidebar-item">
                        <div className="sidebar-item-icon-wrapper">
                            <img className="sidebar-item-icon" src={product} alt="product"/>
                        </div>
                        <span>產品</span>
                    </li>
                    <li className="sidebar-item">
                        <div className="sidebar-item-icon-wrapper">
                            <img className="sidebar-item-icon" src={orders} alt="orders"/>
                        </div>
                        <span>訂單</span>
                    </li>
                    <li className="sidebar-item">
                        <div className="sidebar-item-icon-wrapper">
                            <img className="sidebar-item-icon" src={logistics} alt="orders"/>
                        </div>
                        <span>物流</span>
                    </li>
                    <li className="sidebar-item">
                        <div className="sidebar-item-icon-wrapper">
                            <img className="sidebar-item-icon" src={customerService} alt="orders"/>
                        </div>
                        <span>客戶服務</span>
                    </li>
                    <li className="sidebar-item">
                        <div className="sidebar-item-icon-wrapper">
                            <img className="sidebar-item-icon" src={accounting} alt="orders"/>
                        </div>
                        <span>帳務</span>
                    </li>
                    <li className="sidebar-item">
                        <div className="sidebar-item-icon-wrapper">
                            <img className="sidebar-item-icon" src={reports} alt="orders"/>
                        </div>
                        <span>報告</span>
                    </li>
                    <li className="sidebar-item">
                        <div className="sidebar-item-icon-wrapper">
                            <img className="sidebar-item-icon" src={marketing} alt="orders"/>
                        </div>
                        <span>行銷</span>
                    </li>
                </ul>
            </div>
        )
    }
}

export default (Sidebar);
