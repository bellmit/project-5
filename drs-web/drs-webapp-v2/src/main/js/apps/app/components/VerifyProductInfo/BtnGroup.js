import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class BtnGroup extends Component {
    constructor(props) {
        super(props);
    }
    render(){
        return (
            <div>
        
                    <button
                    className='btn btn-success'
                    onClick={() => this.props.btnLeftClick()}
                        >
                            { this.props.isLoading === true
                                ?   <div className="btn-loading-wrapper">
                                        <div className="btn-spinner"></div>
                                        <span className="btn-spinner-text">Loading ...</span>
                                    </div>
                                :  this.props.btnTextLeft
                            }
                        </button>
       
                <button 
                    className='btn btn-default'
                    onClick={() => this.props.btnRightClick(this.props.baseProductCode)}
                >
                    { this.props.isLoading === true
                        ?   <div className="btn-loading-wrapper">
                                <div className="btn-spinner"></div>
                                <span className="btn-spinner-text">Loading ...</span>
                            </div>
                        :  this.props.btnTextRight
                    }
                </button>


            </div>
        )
    }
}

export default BtnGroup;