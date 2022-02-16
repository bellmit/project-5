import React from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { Link, useParams } from 'react-router-dom';
import { updateRedirectAp } from '../../../actions/index';

const ProductBreadcrumbs = () => {
    const p2m = useSelector(state => state.P2M)
    const dispatch = useDispatch()
    // let {id} = useParams()
    const changeRoute = (param) => {
        switch(param) {
            case 'MarketPlace Information':
                return 'marketplace'
            case 'Insurance':
                return 'insurance'
            case 'Regional':
                return 'regional'
            case 'Shipping':
                return 'shipping'
            case 'Product Information':
                return 'productinfo'
            default:
                return ''
        }
    }

    return (
        <div className="breadcrumbs">
            {/* <div className="breadcrumbs-element" onClick={() => dispatch(updateRedirectAp('manageP2M'))}> */}
            <div className="breadcrumbs-element">
                <Link className='drs-link-blue' to='/product/apply'>
                    <span>申請管理</span>
                </Link>
            </div>
            <div className="breadcrumbs-element"><span>/</span></div>
            {/* <div className="breadcrumbs-element" onClick={() => dispatch(updateRedirectAp('P2MApplication'))}> */}
            <div className="breadcrumbs-element">
                {/* <Link className='drs-link-blue' to={`/product/application/${id}`}> */}
                <Link className="drs-link-blue" to='/product/application'>
                    <span>申請案 {p2m.currentAp.name} </span>
                </Link>
            </div>
            {/* { Object.keys(p2m.currentSubAp).length == 0
                ?   null
                :   <div className="breadcrumbs-element"><span>/</span></div>
            } */}
            
            { p2m.redirectTo == 'p2mSubAp'
                ?   <div className="breadcrumbs-element"><span>/</span></div>
                :   null
            }

            { p2m.redirectTo == 'p2mSubAp'
                ?   <div className="breadcrumbs-element" onClick={() => dispatch(updateRedirectAp('p2mSubAp'))}>
                        {/* <Link className='drs-link-blue' to={`/product/application/${changeRoute(p2m.currentSubAp.type)}/${id}`}> */}
                        <Link className='drs-link-blue' to={`/product/application/${changeRoute(p2m.currentSubAp.type)}`}>
                            <span>表單 {p2m.currentAp.name}-{p2m.currentSubAp.type}</span>
                        </Link>
                    </div>
                :   null
            }
        </div>
    )
}

export default ProductBreadcrumbs;