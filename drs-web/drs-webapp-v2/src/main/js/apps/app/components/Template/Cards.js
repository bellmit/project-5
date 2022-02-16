import React, { Component } from 'react';
import '../../sass/cards.scss'

class Cards extends Component {
    constructor(props) {
        super();
        this.state = {}
    }
    componentDidMount() {
        console.log(this.props.columns);
        console.log(this.props.data);
        console.log(this.props.rows)
    }
    render() {
        return(
            <div className="data-card-wrapper">
                <div className="data-card-s">
                    <p className="data-card-title">Total Data Number</p>
                    <p className="data-card-content">{this.props.data.length}</p>
                    <div className="data-card-info">
                        <span className="data-card-static static-grow-up">2.3%</span>
                        <span className="data-card-description">than last month</span>
                    </div>
                </div>
                <div className="data-card-s">
                    <p className="data-card-title">Filtered Data Number</p>
                    <p className="data-card-content">{this.props.rows.length}</p>
                    <div className="data-card-info">
                        <span className="data-card-static static-grow-up">2.3%</span>
                        <span className="data-card-description">than last month</span>
                    </div>
                </div>
                <div className="data-card-s">
                    <p className="data-card-title">Total Data Number</p>
                    <p className="data-card-content">{this.props.data.length}</p>
                    <div className="data-card-info">
                        <span className="data-card-static static-drop-down">1.3%</span>
                        <span className="data-card-description">than last month</span>
                    </div>
                </div>
                <div className="data-card-s">
                    <p className="data-card-title">Total Data Number</p>
                    <p className="data-card-content">{this.props.data.length}</p>
                    <div className="data-card-info">
                        <span className="data-card-static static-grow-up">2.3%</span>
                        <span className="data-card-description">than last month</span>
                    </div>
                </div>
            </div>
        )
    }
}

export default Cards;