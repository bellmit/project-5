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
            <div className='info-row-wrapper'>
                { this.props.isLoading === true
                    ? <span className='info-title-skeleton-wrapper'>
                        {/* <Skeleton class={"skeleton-info-title"} width={"30%"}/> */}
                        <div className={this.props.tclass} style={{ width: this.props.twidth }}><p></p></div>
                      </span>
                    : <span className='info-title'>{this.props.infoTitle}</span>
                }
                { this.props.isLoading === true 
                    // ? <Skeleton class={"skeleton result"} width={"20%"}/>
                    ? <div className={this.props.dclass} style={{ width: this.props.dwidth }}><p></p></div>
                    : <span className='result'>{this.props.targetData}</span>
                }                                            
            </div>
            // <div className={this.props.class} style={{ width: this.props.width }}>
            //     <p></p>
            // </div>
        )
    }
}

export default Skeleton;