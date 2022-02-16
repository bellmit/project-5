import React, { useEffect, useMemo, useState, useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux'
// import { Redirect } from 'react-router-dom';
import { SelectColumnFilter } from '../common/Filters';
import ApplicationList from './ApplicationList'
import ApplicationFilter from './ApplicationFilter';
import { getFilterList, loadApplications,changeFilterTarget, getExactApplication, handleFilterP2MAp } from '../../../actions/index'
import { loadManageProduct, getExactBP, getNextBPs, removeExactBP, updateProduct ,
    loadProductStat ,fetchSimpleProductList, showLoading , hideLoading, loadProducts} from '../../../actions/index';
// import expandIcon from '../../../assets/images/chevron-left-icon@2x.png'
// import expandDownIcon from '../../../assets/images/chevron-down-icon-blue@2x.png'
import '../../../sass/p2m.scss';
import '../../../sass/button.scss';
import P2MModal from './P2MModal'
import {FormattedMessage} from 'react-intl';
import HelpIcon from '@material-ui/icons/Help';
import Select from 'react-select';

const ManageP2M = () => {
    
    const dispatch = useDispatch();
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state => state.d)
    const productState = useSelector(state => state.PD)

    const showCreateModal= useSelector(state => state.P2M.showCreateModal);
    const [show, showModal ] = useState(showCreateModal)
    const [modal, setModal] = useState('applyP2M')
    const [selectedSupplier, setSelectedSupplier] = useState('')
    const [selectedProduct, setSelectedProduct] = useState('')
    const handleShowModal = (boolean) => {
        showModal(boolean)
    }

    const onSelectBP = (e) => {
        setSelectedProduct(e.label)
        const filterTarget = p2m.filterTarget;
        filterTarget.product = e.label;
        const filterValue = e.label;
        filterTarget.name = ''
        filterTarget.country = ''
        filterTarget.status = ''

        const supplierId = d.u.cid;
        if (e.value !== 'All') {
          dispatch(loadApplications(supplierId, 0, filterTarget.country, filterTarget.status, selectedSupplier, filterTarget.product))
        } else {
          dispatch(loadApplications(supplierId, 0, filterTarget.country, filterTarget.status, selectedSupplier))
        }
        dispatch(changeFilterTarget(filterTarget))
        dispatch(getFilterList(filterTarget.kcode,filterValue))
    }

    const onSelectSupplier = (e) => {
        setSelectedSupplier(e.value)
        setSelectedProduct("")
        dispatch(fetchSimpleProductList(e.value))
        const filterTarget = p2m.filterTarget;
        filterTarget.kcode = e.value;
        const filterValue = e.value;
        filterTarget.name = ''
        filterTarget.product = ''
        filterTarget.country = ''
        filterTarget.status = ''
        const supplierId = d.u.cid;
        dispatch(loadApplications(supplierId, 0, filterTarget.country, filterTarget.status, filterTarget.kcode))
        dispatch(changeFilterTarget(filterTarget))
        dispatch(getFilterList(filterValue))
    }

    const handleSetModal = (target) => {
        setModal(target)
    }

    const fetchData = useCallback(({ pageIndex, pageSize }) => {
        const filterTarget = p2m.filterTarget
        const supplierId = d.u.cid;

        //todo arthur dispatch cause problem
        dispatch(loadApplications(supplierId , (pageIndex +1 ), filterTarget.country, filterTarget.status, filterTarget.kcode, filterTarget.product))

        console.log('fetch data')
    }, [])

    useEffect(() => {
        dispatch(getFilterList())
    },[])

    return (
        <div>
            {/* { p2m.isPending
                ?   <div className="spinner-wrapper">
                        <div className="spinner"></div>
                        <div className="spinner-text"><span>Loading...</span></div>
                    </div>
                :   null
            } */}
            <div className="pannel-header">
                <div style={{display:'flex'}}>
                <h1><FormattedMessage id="p2m.management"/></h1>
                <a href="../resources/files/DRS_Platform_Manual_v2.pdf" style={{color:'#737674',marginTop:'auto', marginBottom:'auto', marginLeft:'10px'}} target="_blank">
                    <HelpIcon/>
				</a>
                </div>
                <div className="btn-cta-group">
                    <button
                        className="drs-btn drs-btn-cta"
                        onClick={() => {
                            console.log('click 新建申請')
                            handleShowModal(true)
                            handleSetModal('applyP2M')
                        }}
                    >
                        <FormattedMessage id="p2m.create"/>
                    </button>
                </div>
            </div>
            <div className="pannel-header">
                <div style={{display:'flex', alignItems: 'left'}}>
                    { !d.u.isSp
                        ?   <h1 style={{marginRight: '12px'}}> <FormattedMessage id="product.supplier"/></h1>
                        :   null
                    }
                    { !d.u.isSp
                        ?   <Select
                                className='drs-selector'
                                placeholder={p2m.filterTarget.kcode == '' ? 'Select...': p2m.filterTarget.kcode}
                                options={d.supplierSelectOptions}
                                value={p2m.filterTarget.kcode}
                                onChange={(e) => onSelectSupplier(e)}
                            />
                        :   null
                    }
                    { !d.u.isSp
                        ?   <h1 style={{marginRight: '12px', marginTop:'auto', marginBottom:'auto'}}>產品</h1>
                        :   null
                    }
                    { !d.u.isSp
                        ?   <Select
                                className='drs-selector'
                                placeholder={p2m.filterTarget.product == '' ? 'All': p2m.filterTarget.product}
                                options={productState.bpSelectOptions}
                                value={productState.currentBP}
                                onChange={(e) => onSelectBP(e)}
                            />
                        :   null
                    }
                </div>
           </div>
            <div className="table-container">
                <P2MModal
                    onClose={handleShowModal}
                    show={show}
                    modal={modal}
                />
                
                <div className="table-wrapper">
                    <ApplicationFilter
                        fetchData = {fetchData}
                        data={p2m.filterList}
                        pageCount = {p2m.totalPages}
                    />
                    {/* <ApplicationList
                        columns={columns}
                        // handleUpdateAp={handleUpdateAp}
                        data={p2m.applications}
                        fetchData = {fetchData}
                        // handleUpdateCurrentAp={handleUpdateCurrentAp}
                        pageCount = {p2m.totalPages}
                    /> */}
                </div>
                
            </div>
        </div>
    )
}

export default ManageP2M
