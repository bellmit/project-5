import React, { Component } from 'react';
import '../../../sass/statusbar.scss';

class StatusBar extends Component {
    constructor(props) {
        super()
        this.state={ }
    }
    render () {
        return (
            <div className="statusbar-wrapper">
                {/*<span>{this.props.action}</span>*/}
                <div className="statusbar">
                    <ul className="progressbar">
                        {this.props.steps.map(step => (
                            <li className={step.state}>{step.name}</li>
                        ))}
                    </ul>
                </div>
            </div>
        )
    }
}

export default StatusBar
