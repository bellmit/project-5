import React, { Component } from 'react';
import Select from 'react-select';

const customStyles = {
    control: (provided, state) => ({
        ...provided,
        background: '#fff',
        borderColor: '#d7dbec',
        minHeight: '37px',
        height: '37px',
        boxShadow: state.isFocused ? null : null,
    }),

    valueContainer: (provided, state) => ({
        ...provided,
        height: '37px',
        padding: '0 6px'
    }),

    input: (provided, state) => ({
        ...provided,
        margin: '0px',
        padding: '0px',
    }),
    indicatorSeparator: state => ({
        display: 'none',
    }),
    indicatorsContainer: (provided, state) => ({
        ...provided,
        height: '100%',
    }),
};

class MarketPlace extends Component {
    constructor() {
        super()
        this.state={}
    }

    render() {
        return (
            
            <div className="action-board">
                <div className="action-board-main">
                    <p className="action-board-title">請選擇銷售平台和市場地區？</p>
                    <div className="base-input-wrapper">
                        <div className="base-input-label-wrapper">
                            <p className="base-input-label">欲銷售平台</p>
                        </div>
                        <div className="base-input-select-wrapper">                    
                            <Select
                                className="base-selector"
                                styles={customStyles}
                                value={this.props.selectedPlatform}
                                placeholder={this.props.selectedPlatform}
                                onChange={this.props.handlePlatformChange}
                                options={this.props.platformOptions}
                                classNamePrefix='mySelector'
                            />                                
                        </div>
                    </div>
                    <div className="base-input-wrapper">
                        <div className="base-input-label-wrapper">
                            <p className="base-input-label">欲銷售地區</p>
                        </div>
                        <div className="base-input-select-wrapper">                    
                            <Select
                                className="base-selector"
                                styles={customStyles}
                                value={this.props.selectedMarketplace}
                                placeholder={this.props.selectedMarketplace}
                                onChange={this.props.handleMarketplaceChange}
                                options={this.props.marketplaceOptions}
                                classNamePrefix='mySelector'
                            />                                
                        </div>
                    </div>
                    <div className="base-input-wrapper">
                        <div className="base-input-label-wrapper">
                            <p className="base-input-label">欲銷售產品</p>
                        </div>
                        <div className="base-input-select-wrapper">                    
                            <Select
                                className="base-selector"
                                styles={customStyles}
                                value={this.props.selectedProduct}
                                placeholder={this.props.selectedProduct}
                                onChange={this.props.handleProductChange}
                                options={this.props.productOptions}
                                classNamePrefix='mySelector'
                            />                                
                        </div>
                    </div>
                </div>
            </div>
            
        )
    }
}

export default MarketPlace;
