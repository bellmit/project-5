import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
// import { updateControl } from '../../../actions/actions';

// const mapStateToProps = (state) => {
//     return {
//         initialState: state.manageControl
//     }
// }

// const mapDispatchToProps = (dispatch) => {
//     return {
//         onUpdateControl: (obj) => dispatch(updateControl(obj))
//     }
// }

const ControlPannel = () => {
    const dispatch = useDispatch()
    const initialState = useSelector(state => state.manageControl)
    const handleUpdateControl = (param) => {
        let update = {};
        if (param == 'addNewProduct') {
            update = {
                title: '新建產品',
                btngroups: [
                    { name: '取消新建產品', path: '/product', action: 'cancel' },
                ],
                
            }
        } else if (param == 'applyforNPO') {
            update = {
                title: '申請上架',
                btngroups: [
                    { name: '取消申請上架', path: '/', action: 'cancel' },
                ],
                
            }
        } else if (param == 'cancel'|| param == 'product') {
            update = {
                title: '產品管理',
                btngroups: [
                    { name: '新建產品', path: '/product/add', action: 'addNewProduct'}
                ],
                
            }
        }
    //    dispatch(updateControl(update));
    }
    
    return (
        <div className="pannel-header">
            <h1>{initialState.title}</h1>
            <div className="btn-cta-group">
                {initialState.btngroups.map(btn => (
                    <Link to={btn.path}>
                        <button
                            className="btn btn-cta"
                            onClick={() => handleUpdateControl(btn.action)}
                        >
                            {btn.name}
                        </button>
                    </Link>
                ))}
            </div>
        </div>
    )
}

export default ControlPannel
