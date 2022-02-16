import React, { Component } from 'react';

class FillData extends Component {
    constructor() {
        super()
        this.state={
            phase: 'amz-platform',
            tabs: [{name: 'Vital Info'}, {name: 'Variation'},{ name: 'Offer'}, 
                {name: 'Compliance'},{ name: 'Images'}, {name: 'Description'}, 
                {name: 'Keywords'}, {name: 'More details'}]
        }
    }
    render () {
        return (
            <div className="apply-section">
                <div className="apply-title-wrapper">
                    <p className="apply-title">申請單編號 例如 AP0001</p>
                </div>
                
                <div className="apply-fill-wrapper">
                    <div className="apply-info">
                        <div className="apply-info-column">
                            <div className="apply-info-item">
                                <span className="apply-info-item-title">欲銷售平台</span>
                                <span className="apply-info-item-context">{this.props.selectedPlatform}</span>
                            </div>
                            <div className="apply-info-item">
                                <span className="apply-info-item-title">欲銷售地區</span>
                                <span className="apply-info-item-context">{this.props.selectedMarketplace}</span>
                            </div>
                            <div className="apply-info-item">
                                <span className="apply-info-item-title">欲銷售產品</span>
                                <span className="apply-info-item-context">{this.props.selectedProduct}</span>
                            </div>
                        </div>
                        <div className="apply-info-column">
                            <div className="apply-info-item">
                                <span className="apply-info-item-title">Product ID</span>
                                <span className="apply-info-item-context">{this.props.selectedPlatform}</span>
                            </div>
                            <div className="apply-info-item">
                                <span className="apply-info-item-title">Product ID Type</span>
                                <span className="apply-info-item-context">{this.props.selectedMarketplace}</span>
                            </div>
                            <div className="apply-info-item">
                                <span className="apply-info-item-title">銷售通路是否已有相同產品？</span>
                                <span className="apply-info-item-context">否</span>
                            </div>
                        </div>
                        {/* <div className="apply-info-column">
                            <div className="apply-info-item">
                                <span className="apply-info-item-title">Product Category</span>
                                <span className="apply-info-item-context">{this.props.marketplaceCategory}</span>
                            </div>
                        </div> */}

                    </div>
                    { this.state.phase == 'amz-platform'
                        ?   <div className="apply-fill-main">
                                <div className="apply-fill-title-wrapper">
                                    <p className="apply-fill-title"> 填資料 > Amazon 銷售平台所需資料 </p>
                                </div>
                                <div className="apply-fill-part">
                                    <div className="apply-fill-tab-wrapper">
                                        <ul className="apply-fill-tab">
                                            { this.state.tabs.map((item,index) => (
                                                <li className="apply-fill-tab-item"> {item.name} </li>
                                            ))}
                                        </ul>
                                    </div>
                                    <div className="display-data-wrapper">
                                        <div className="display-data-item">
                                            <span className="data-title">Product Name</span>
                                            <span className="data-content">{this.props.productName}</span>
                                        </div>
                                        <div className="display-data-item">
                                            <span className="data-title">Manufacturer</span>
                                            <span className="data-content"></span>
                                        </div>
                                        <div className="display-data-item">
                                            <span className="data-title">Brand Name</span>
                                            <span className="data-content"></span>
                                        </div>
                                        <div className="display-data-item">
                                            <span className="data-title">Manufacturer Part Number</span>
                                            <span className="data-content"></span>
                                        </div>
                                        <div className="display-data-item">
                                            <span className="data-title">Ingredients</span>
                                            <span className="data-content"></span>
                                        </div>
                                        <div className="display-data-item">
                                            <span className="data-title">Unit Count</span>
                                            <span className="data-content"></span>
                                        </div>
                                        <div className="display-data-item">
                                            <span className="data-title">Unit Count Type</span>
                                            <span className="data-content"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        :   <div className="apply-fill-main">
                                <div className="apply-fill-title-wrapper">
                                    <p className="apply-fill-title"> 填資料 > DRS 通路所需資料 </p>
                                </div>
                                <div className="apply-fill-part">
                                    <div className="apply-fill-tab-wrapper">
                                        <ul className="apply-fill-tab">
                                            { this.state.tabs.map((item,index) => (
                                                <li className="apply-fill-tab-item"> {item.name} </li>
                                            ))}
                                        </ul>
                                    </div>
                                </div>
                            </div>
                    }
                    
                </div>

            </div>
        )
    }

}

export default FillData;
