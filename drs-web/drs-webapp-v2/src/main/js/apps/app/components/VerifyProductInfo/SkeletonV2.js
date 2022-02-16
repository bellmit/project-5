import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class Skeleton extends Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        console.log(this.props)
    }

    render() {
        return (
            <div className='info-wrapper'>
                <div className='info-row-wrapper'>
                    <span className='info-title-skeleton-wrapper'>
                        <div className="skeleton-info-title" style={{ width: this.props.tw1 }}><p></p></div>
                    </span>
                    <div className="skeleton result" style={{ width: this.props.dw1 }}><p></p></div>                                        
                </div>
                <div className='info-row-wrapper'>
                    <span className='info-title-skeleton-wrapper'>
                        <div className="skeleton-info-title" style={{ width: this.props.tw2 }}><p></p></div>
                    </span>
                    <div className="skeleton result" style={{ width: this.props.dw2 }}><p></p></div>                                        
                </div>
                <div className='info-row-wrapper'>
                    <span className='info-title-skeleton-wrapper'>
                        <div className="skeleton-info-title" style={{ width: this.props.tw2 }}><p></p></div>
                    </span>
                    <div className="skeleton result" style={{ width: this.props.dw3 }}><p></p></div>                                        
                </div>
            </div>            
            // <div className={this.props.class} style={{ width: this.props.width }}>
            //     <p></p>
            // </div>
        )
    }
}

export default Skeleton;