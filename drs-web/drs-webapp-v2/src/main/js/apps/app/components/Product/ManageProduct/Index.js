import React, { useEffect, useMemo, useState} from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { loadManageProduct, getExactBP, getNextBPs, removeExactBP, updateProduct ,
    loadProductStat ,fetchSimpleProductList, showLoading , hideLoading, loadProducts} from '../../../actions/index';
import ProductTable from './ProductTable';
import ProductModal from './ProductModal';
import { SelectColumnFilter,SelectFilter } from '../common/Filters';
import expandIcon from '../../../assets/images/chevron-left-icon@2x.png'
import expandDownIcon from '../../../assets/images/chevron-down-icon-blue@2x.png'
import Select from 'react-select';
import InfiniteScroll from 'react-infinite-scroll-component';
import Swal from 'sweetalert2';
import Cards from '../common/Cards';
import {FormattedMessage} from 'react-intl';
import '../../../sass/button.scss';
import HelpIcon from '@material-ui/icons/Help';

const renderRowSubComponent = row => {
    const {
        // name: { first, last },
        // location: { city, street, postcode },
        // picture,
        productId,
        productIdType,
        hscode,
        variationTheme,
        variable,
        salespage,
        applications,
        platform,
        marketplace,
    } = row.original

    var {
        retailPriceInUs,
        retailPriceInTts,
        retailPriceInUk,
        retailPriceInCa,
        retailPriceInDe,
        retailPriceInFr,
        retailPriceInIt,
        retailPriceInEs,
    } = row.original
    if(productId.value == ""){
        productId.value = "由 DRS 提供"
    }
    if(retailPriceInUs == undefined){
        retailPriceInUs = "未定價"
    }
    if(retailPriceInTts == undefined){
        retailPriceInTts = "未定價"
    }
    if(retailPriceInUk == undefined){
        retailPriceInUk = "未定價"
    }
    if(retailPriceInCa == undefined){
        retailPriceInCa = "未定價"
    }
    if(retailPriceInDe == undefined){
        retailPriceInDe = "未定價"
    }
    if(retailPriceInFr == undefined){
        retailPriceInFr = "未定價"
    }
    if(retailPriceInIt == undefined){
        retailPriceInIt = "未定價"
    }
    if(retailPriceInEs == undefined){
        retailPriceInEs = "未定價"
    }

    return (
         <div style={{display: 'flex',flexDirection:'column'}}>
            <div className="table-expand-section">
                <div className="table-expand-column-container">
                    <div className="table-expand-column-wrapper">
                        <div className="table-expand-column-2">
                            <p className="table-expand-title">{productId.name}</p>
                            <p className="table-expand-description">{productId.value}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title">{productIdType.name}</p>
                            <p className="table-expand-description">{productIdType.value}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title">{variationTheme.name}</p>
                            <p className="table-expand-description">{variationTheme.value}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title">{variable.name}</p>
                            <p className="table-expand-description">{variable.value}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div className="table-expand-section">
                <p className="section-line" style={{width: '70%',margin:'auto'}}></p>
            </div>
            <div className="table-expand-section">
                <div className="table-expand-column-container" style={{display: 'flex'}}>
                    <div style={{padding: '2%',width: '250px'}}>
                        <div style={{width: '245px',marginTop: '-2%'}}>
                            <p className="table-expand-title-style-2" style={{color:'#505468'}}>平台</p>
                            <p className="table-expand-description" style={{fontWeight: '700', color: '#505468'}}>零售價</p>
                        </div>
                    </div>
                    <div className="table-expand-column-wrapper">
                        <div className="table-expand-column-2">
                            <p className="table-expand-title-style">Amazon.com</p>
                            <p className="table-expand-description">{retailPriceInUs}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title-style">TureToSource</p>
                            <p className="table-expand-description">{retailPriceInTts}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title-style">Amazon.co.uk</p>
                            <p className="table-expand-description">{retailPriceInUk}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title-style">Amazon.ca</p>
                            <p className="table-expand-description">{retailPriceInCa}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title-style">Amazon.de</p>
                            <p className="table-expand-description">{retailPriceInDe}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title-style">Amazon.fr</p>
                            <p className="table-expand-description">{retailPriceInFr}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title-style">Amazon.it</p>
                            <p className="table-expand-description">{retailPriceInIt}</p>
                        </div>
                        <div className="table-expand-column-2">
                            <p className="table-expand-title-style">Amazon.es</p>
                            <p className="table-expand-description">{retailPriceInEs}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

const ManageProduct = () => {
    const productState = useSelector(state => state.PD)
    const d = useSelector(state => state.d)
    const dispatch = useDispatch();
   
    const [show, showModal ] = useState(false);
    const [modal, setModal] = useState('addNewProduct')
    const [bpId, setBPId] = useState('')
    const [selectedSupplier, setSelectedSupplier] = useState('')

    const onRemoveBP = (pid) => {
        const supplierId = d.u.cid;
        const bp = productState.showProducts.filter(item => item._id.$oid == pid)
        const skuList = bp[0].skus.map(item => {
            return item.sellerSku
        });
        const skuStr = skuList.toString();
        Swal.fire({
            title: '確定要刪除這個產品?',
            text: `注意： ${skuStr} 將會被刪除`,
            icon: '',
            showCancelButton: true,
            // confirmButtonColor: '#2194ED',
            confirmButtonColor: '#7ed6a5',
            cancelButtonColor: '#A5B4BF',
            confirmButtonText: '確定刪除',
            cancelButtonText: '取消'
          }).then((result) => {
              if (result.value) {
                  dispatch(removeExactBP(supplierId, pid))
                  //setHasMore(true)
              }            
          })
        
    }
    
    const onSelectBP = (e) => { 
        const supplierId = d.u.cid;
        if (e.value !== 'All') {
            dispatch(getExactBP(supplierId,e.value))
        } else {
            //setHasMore(true)
            if(d.u.isSp){
                dispatch(loadManageProduct(supplierId))
            }else{
                dispatch(loadProducts(selectedSupplier,1))
            }
        }
    }
    const onSelectSupplier = (e) => { 
        setSelectedSupplier(e.value)
        dispatch(fetchSimpleProductList(e.value))
        dispatch(loadProductStat(e.value))
        dispatch(loadProducts(e.value,1))
    }
    const handleSetBPId = (id) => {
        console.log(id, 'handle set bp')
        setBPId(id)
    }
    const handleShowModal = (boolean) => {
        showModal(boolean)
    }
    const handleSetModal = (target) => {
        //setHasMore(true)
        setModal(target)
    }

    // const handleStatusChange = (id, status) => {
    //     //todo arthur maybe need refactor
    //     const result = productState.products.filter(item => item._id.$oid == id);
    //     result[0].bpStatus = status;
    //     const changedSkus = result[0].skus.map(item => {
    //         item.status = status
    //         item.editable = false
    //         item.retailPrice = 89
    //         // item.salesVolume = 7
    //         item.settlementsPeriodOrder = 7,
    //         item.openPosition = 15
    //         item.fbaQuantity = 28
    //         return item
    //     })
    //     result[0].skus = changedSkus;
    //     const supplierId = d.u.cid
    //     dispatch(updateProduct(supplierId, id, result[0]))
    // }

    const columns = useMemo(() => [
        {
            Header: () => null,
            id: 'expander',
            Cell: ({ row }) => (
                <span {...row.getToggleRowExpandedProps()}>
                    {/* {row.isExpanded ? '👇' : '👉'} */}
                    {row.isExpanded ? <img src={expandDownIcon} alt="expandIcon"/> : <img src={expandIcon} alt="expandIcon"/>}
                </span>
            ),
            width: 60,
            maxWidth: 60,
            minWidth: 15,
        },
        {
            Header: 'SKU',
            accessor: 'sellerSku',
            Filter: SelectColumnFilter,
            filter: 'equals'// by default, filter: 'text', but in our case we don't want to filter options like text, we want to find exact match of selected option.
        },
//        {
//            Header: <FormattedMessage id="product.retailPrice"/>,
//            accessor: 'retailPrice',
//            Filter: SelectColumnFilter,
//            filter: 'equals',
//            disableFilters: true,
//        },
        // {
        //     Header: <FormattedMessage id="product.salesVolume"/>,
        //     accessor: 'salesVolume',
        //     Filter: SelectColumnFilter,
        //     filter: 'equals',
        //     disableFilters: true,
        // },
        // {
        //     Header: <FormattedMessage id="product.fbaQuantity"/>,
        //     accessor: 'fbaQuantity',
        //     Filter: SelectColumnFilter,
        //     filter: 'equals',
        //     disableFilters: true,
        // },
        // {
        //     Header: <FormattedMessage id="product.openPosition"/>,
        //     accessor: 'openPosition',
        //     Filter: SelectColumnFilter,
        //     filter: 'equals',
        //     disableFilters: true,
        // },
        {
            Header: <FormattedMessage id="product.settlementsPeriodOrder"/>,
            accessor: 'settlementsPeriodOrder',
            // accessor: 'salesVolume',
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
        },
        {
            Header: <FormattedMessage id="product.lastSevenDaysOrder"/>,
            accessor: 'lastSevenDaysOrder',
            // accessor: 'openPosition',
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
        },
        {
            Header: <FormattedMessage id="product.applying"/>,
            accessor: 'applying',
            Filter: SelectFilter,
            maxWidth: 300,
            minWidth: 120,
            width: 200,
            filter: 'fuzzyText'// Use `fuzzyText` filter on this column
        },
        //todo arhtur have modify rwd css
        {
            Header: <FormattedMessage id="product.selling"/>,
            accessor: 'selling',
            Filter: SelectFilter,
            filter: 'fuzzyText',
            maxWidth: 300,
            minWidth: 120,
            width: 200,
        },
        // {
        //     Header: 'SKU可採取動作',
        //     accessor: 'actions',
        //     Filter: SelectColumnFilter,
        //     filter: 'equals',
        //     disableFilters: true,
        // },
    ],[])

   // const [hasMore, setHasMore] = useState(true)
    const fetchMoreBPs = () => {
        const supplierId = d.u.isSp ? d.u.cid : selectedSupplier;

        if (productState.totalPages > productState.pageIndex) {
            //setHasMore(false)
        //}else{
            dispatch(getNextBPs(supplierId,(productState.pageIndex + 1)))
        }
    }
    useEffect(() => {
        dispatch(showLoading())
        const supplierId = d.u.cid;
        dispatch(loadManageProduct(supplierId))
        dispatch(loadProductStat(supplierId))
    }, [])


    return (
        <div>
            <div className="pannel-header">
                <div style={{display:'flex', alignItems: 'left'}}>
                    { !d.u.isSp
                        ?   <h1 style={{marginRight: '12px'}}> <FormattedMessage id="product.supplier"/></h1>
                        :   null
                    }
                    { !d.u.isSp
                        ?   <Select
                                className='drs-selector'
                                options={d.supplierSelectOptions}
                                onChange={(e) => onSelectSupplier(e)}
                            />
                        :   null
                    }
                    <h1 style={{marginRight: '12px', marginTop:'auto', marginBottom:'auto'}}><FormattedMessage id="product.management"/></h1>
                    <Select
                        className='drs-selector'
                        placeholder={productState.currentBP}
                        options={productState.bpSelectOptions}
                        value={productState.currentBP}
                        onChange={(e) => onSelectBP(e)}
                    />
                    <a href="resources/files/DRS_Platform_Manual_v2.pdf" style={{color:'#737674',marginTop:'auto', marginBottom:'auto'}} target="_blank">
                    <HelpIcon/>
					</a>
                </div>
                <div className="btn-cta-group">
                    <button
                        className="drs-btn drs-btn-cta"
                        onClick={() => {
                            handleShowModal(true)
                            handleSetModal('addNewProduct')
                        }}
                    >
                        <FormattedMessage id="product.create"/>
                    </button>
                </div>
           </div>
            <Cards
                columns={columns}
                data={[
                    {title: 'product.totalBP', num: productState.tpn},
                    {title: 'product.totalSKU', num: productState.tsn},
                    {title: 'product.appliedSKU', num: productState.tasn},
                    {title: 'product.salingSKU', num: productState.tossn} 
                ]}
            />
            <div className="table-container">
                <ProductModal
                    onClose={handleShowModal}
                    show={show}
                    modal={modal}
                    bpId={bpId}
                />
                { d.showLoading
                    ?   <div className="black-spinner-wrapper">
                            <div className="spinner"></div>
                            <div className="spinner-text"><span>Loading...</span></div>
                        </div>
                        
                    :    productState.showProducts == 0
                        ?   <div style={{ padding: '4%',textAlign: 'center',backgroundColor: 'rgba(90, 96, 127,0.1)',borderRadius: '8px'}}>
                                <p style={{fontSize: '15px', color: '#5a607f'}}> <FormattedMessage id="product.noData"/></p>
                            </div>
                        :   <InfiniteScroll
                                dataLength={productState.showProducts.length}
                                next={fetchMoreBPs}
                                hasMore={productState.totalPages == productState.pageIndex ?  false :  true }
                                loader={<h5>Loading...</h5>}
                            >
                                {productState.showProducts.map((item,index) => {
                                    return (
                                    <div>
                                        <div className="table-wrapper">
                                            <ProductTable
                                                key={index}
                                                id={item._id.$oid}
                                                // type='product'
                                                bpStatus={item.bpStatus}
                                                title={item.productNameEN}
                                                columns={columns}
                                                data={item.skus}
                                                renderRowSubComponent={renderRowSubComponent}
                                                handleShowModal={handleShowModal}
                                                handleSetModal={handleSetModal}
                                                handleSetBPId={handleSetBPId}
                                                onRemoveBP={onRemoveBP}
                                                // handleStatusChange={handleStatusChange}
                                            />
                                        </div>
                                    </div>
                                )})}
                            </InfiniteScroll>
                }
            </div>
        </div> 
    )
}

export default ManageProduct
