import React, { Component } from 'react';
import '../../sass/main.scss';
import ReactTable from './ReactTable';
import ControlPannel from '../Product/common/ControlPannel';

class MainPannel extends Component {
    constructor() {
        super();
        this.state={ }
    }

    render() {
        return (
            <div className="app-main-pannel">
                <ControlPannel/>
                <ReactTable/>
            </div>
        )
    }
}

export default (MainPannel);