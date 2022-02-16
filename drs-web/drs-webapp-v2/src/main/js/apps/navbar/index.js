import React from 'react';
import ReactDOM from 'react-dom';
import NavBar from './App';
import Wrapper from "./Wrapper";

ReactDOM.render(
    <Wrapper>
        <NavBar/>
    </Wrapper>,
    document.getElementById('react')
);