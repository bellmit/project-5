import React, { Component } from 'react';
import '../../sass/app.scss';
import Navbar from '../Navbar/Navbar';
import Sidebar from '../Sidebar/Sidebar';
import MainPannel from './MainPannel';

class Template extends Component {
    constructor() {
        super();
        this.state = {}
    }

    render() {
        return (
            <div className="template">
                <Navbar/>
                {/* <Sidebar/> */}
                <MainPannel/>
            </div>
        )
    }
}

export default (Template);
