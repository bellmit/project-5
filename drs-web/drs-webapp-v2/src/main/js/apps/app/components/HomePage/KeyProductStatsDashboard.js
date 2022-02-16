import React, { useEffect, useState } from 'react';
// import {useSelector} from 'react-redux'
import { descending, ascending} from 'd3';
import '../../sass/cards.scss'
import axios from 'axios';
import {DOMAIN_NAME} from '../../constants/action-types'
import SkuOrdersBarChart from './SkuOrdersBarChart';
import MarketplaceBarChart from './MarketplaceBarChart';
import InventoryStackedBarChart from './InventoryStackedBarChart';

const Url = DOMAIN_NAME + '/pd';

const KeyProductStatsDashboard = () => {
    //testing, kcode should be using d.u.cid directly
    const [kcode, setKcode] = useState('K612')

    const [invData, setInvData] = useState([])
    const [soData, setSoData] = useState([])
    const [mpData, setMpData] = useState([])
    
    // need to adjust because the mp options would vary from different kcode
    const [soMpOp, setSoMpOp] = useState([{value: 'Amazon.com', label: 'Amazon.com'}])
    const [invMpOp, setInvMpOp] = useState([{value: 'Amazon.com', label: 'Amazon.com'}])

    const [invMp, setInvMp] = useState('Amazon.com')
    const [soMp, setSoMp] = useState('Amazon.com')

    const [dailyOrder, setDailyOrder] = useState(0)
    const [dailySales, setDailySales] = useState(0)
    const [dailySalesQty, setDailySalesQty] = useState(0)
    
    // const getKeyProductBaseRetailPrice = () => {
    //     axios.post(Url + '/gkprp', {
    //         // kCode: d.u.cid
    //         kCode: kcode
    //       },{
    //         headers: { 
    //           'Content-Type': 'application/json',
    //           'Access-Control-Allow-Origin': '*',
    //         }
    //     }).then(res => {
    //         console.log(res.data, 'response of getKeyProductBaseRetailPrice')
    //     })
    // }

    // const getDetailPageSalesTraffic = () => {
    //     axios.post(Url + '/gpst', {
    //         // kCode: d.u.cid
    //         kCode: kcode,
    //         mp: mp,
    //       },{
    //         headers: { 
    //           'Content-Type': 'application/json',
    //           'Access-Control-Allow-Origin': '*',
    //         }
    //     }).then(res => {
    //         console.log(res.data, 'response of getDetailPagesSalesTraffic')
            
    //     })
    // }

    // const getKeyProductSinceLastSettlementOrder = () => {
    //     axios.post(Url + '/gkplseor', {kCode: kcode, mp: mp},{
    //         headers: {
    //             'Content-Type': 'application/json',
    //             'Access-Control-Allow-Origin': '*'
    //         }
    //     }).then(res => {
    //         console.log(res.data, 'response of getKeyProductSinceLastSettlementOrder')
    //     })
    // }

    // const getKeyProductLastSevenDaysOrder = () => {
    //     axios.post(Url + '/gkplsor', {kCode: kcode, mp: mp},{
    //         headers: {
    //             'Content-Type': 'application/json',
    //             'Access-Control-Allow-Origin': '*'
    //         }
    //     }).then(res => {
    //         console.log(res.data, 'response of getKeyProductLastSevenDaysOrder')
    //     })
    // }

    const getDailySalesQtyAndRev = () => {
        axios.post(Url + '/gdsqr',{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        }).then(res => {
            // console.log(res.data, 'response of getDailySalesQtyAndRev')
            setDailySalesQty(res.data.qty)
            setDailySales(res.data.rev)
        })
    }

    const getDailyOrder = () => {
        axios.post(Url + '/gdo',{
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        }).then(res => {
            // console.log(res.data, 'response of getDailyOrder')
            setDailyOrder(res.data.Orders)
        })
    }

    const getMarketplaceOrders = () => {
        // request should be wrote here
        // testing data
        const mpOrders = [
            {marketplace: 'Amazon.com', orders: 20},
            {marketplace: 'Amazon.fr', orders: 12},
            {marketplace: 'Amazon.de', orders: 20},
            {marketplace: 'Amazon.it', orders: 18},
            // {marketplace: 'Amazon.uk', orders: 12},
            // {marketplace: 'Amazon.la', orders: 14},
            // {marketplace: 'Amazon.ca', orders: 2},
            // {marketplace: 'Amazon.me', orders: 2},
            // {marketplace: 'Amazon.au', orders: 18}
        ]
        setMpData(mpOrders)
    }

    const getSkuOrdersData = (selectMp) => {
        axios.post(Url + '/gkplseor', {kCode: kcode, mp: selectMp},{
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            }
        }).then(res => {
            // console.log(res, 'response of getKeyProductSinceLastSettlementOrder')
            if(res !== null) {
                // need to adjust, data may should be controlled with react-redux
                setSoData(res.data)
            }
        })
    }

    const handleMpOptions = () => {
        axios.post(Url + '/gkplseor', {kCode: kcode},{
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*'
            }
        }).then(res => {
            // console.log(res, 'response of getKeyProductSinceLastSettlementOrder')

            if(res !== null) { 
                const marketplace = res.data.map(item => item.marketplace)
                const distinctMarketplace = [...new Set(marketplace)]
                const options = distinctMarketplace.map(item => {
                    return {value: item, label: item}
                })
                setSoMpOp(options)
                // console.log(options)
            }
        }).catch(err => {
            console.log(err)
        })

        axios.post(Url + '/ginvst', {kCode: kcode}, {
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        }).then(res => {
            // console.log(res, 'response of getKeyProductInventoryStats')

            if(res !== null) { 
                const marketplace = res.data.map(item => item.marketplace)
                const distinctMarketplace = [...new Set(marketplace)]
                const options = distinctMarketplace.map(item => {
                    return {value: item, label: item}
                })
                setInvMpOp(options)
                // console.log(options)
            }
        })
        .catch(err => {
            console.log(err);
        })
    }

    const getKeyProductInventoryStats = (mp) => {
        axios.post(Url + '/ginvst', {kCode: kcode, mp: mp}, {
            headers: { 
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
            }
        }).then(res => {
            // console.log(res.data, 'res.data response of get inventory stats')
            // need to adjust, data may should be controlled with react-redux
            setInvData(res.data)
        })
        .catch(err => {
            console.log(err);
        })
    }

    const selectInvMp = (value) => {
        setInvMp(value)
        getKeyProductInventoryStats(value)
    }

    const selectSoMp = (value) => {
        setSoMp(value)
        getSkuOrdersData(value)
    }
    
    const onSortData = (isSorted) => {
        const changedData = soData;
        if (isSorted) {
            changedData.sort((a, b) => {
                return descending(a.totalOrder, b.totalOrder)
            })
            // need to adjust, data may should be controlled with react-redux
            setSoData(changedData)
        } else {
            changedData.sort((a, b) => {
                return ascending(a.sku, b.sku)
            })
            // need to adjust, data may should be controlled with react-redux
            setSoData(changedData)
        }
    }

    useEffect(() => {
        getDailySalesQtyAndRev()
        getDailyOrder()
        getKeyProductInventoryStats(invMp)
        getSkuOrdersData(soMp)
        getMarketplaceOrders()
        handleMpOptions()
    }, [])
    
    // const onSelectSupplier = () => {
    //     console.log('onselect supplier')
    // }

    return (
        <div className="home-container">
            <div className="pannel-header">
                <div style={{width: '100%',display: 'flex', flexDirection: 'column', alignItems: 'left'}}>
                    {/* <h1 style={{marginRight: '12px'}}>Key Product Stats</h1> */}
                    <div className="card-content-info">
                        <span>Time Period : Since Last Settlement</span>
                        <span>Last Updated : 07/13/2021 00:00</span>
                    </div>
                </div>
                {/* {!d.u.isSp
                    ?   <div style={{display:'flex', alignItems: 'left'}}>
                            <h1 style={{marginRight: '12px'}}>
                                <FormattedMessage id="product.supplier"/>
                            </h1>
                            <Select
                                className='drs-selector'
                                //placeholder={productState.currentBP}
                                // options={d.supplierSelectOptions}
                                //value={productState.currentBP}
                                onChange={(e) => onSelectSupplier(e)}
                            />
                        </div>
                    : null
                } */}
            </div>
            <div className="widget-wrapper">
                <div className="data-card-wrapper">
                    <div className="data-card-s">
                        <p className="data-card-title">訂單</p>
                        <p className="data-card-content">{dailyOrder}</p>
                        {/* <div className="data-card-info">
                            <span className="data-card-static static-grow-up">2.3%</span>
                            <span className="data-card-description">than last month</span>
                        </div> */}
                    </div>
                    <div className="data-card-s">
                        <p className="data-card-title">銷售額</p>
                        <p className="data-card-content">{dailySales}</p>
                        {/* <div className="data-card-info">
                            <span className="data-card-static static-grow-up">2021.07.08</span>
                            <span className="data-card-description">最新更新時間 07/08/2021 00:00:00</span>
                        </div> */}
                    </div>
                    <div className="data-card-s">
                        <p className="data-card-title">銷售量</p>
                        <p className="data-card-content">{dailySalesQty}</p>
                        {/* <div className="data-card-info">
                            <span className="data-card-static static-drop-down">1.3%</span>
                            <span className="data-card-description">than last month</span>
                        </div> */}
                    </div>
                    <div className="data-card-s">
                        <p className="data-card-title">Welcome! This is data info dashboard....</p>
                        <p >Please take a look at the notifications. It seems you have 5 notifications.</p>
                    </div>
                </div>
            </div>
            <div className="widget-wrapper">
                <div className="data-card-column-wrapper">
                    <div id="mpbar" className="data-card data-card-column" style={{width: '40%', height: '450px'}}>
                        <MarketplaceBarChart
                            data={mpData}
                        />
                    </div>
                    <div id="skuorderbar" className="data-card data-card-column" style={{width: '56%', height: '450px'}}>
                        <SkuOrdersBarChart
                            data={soData}
                            mp={soMp}
                            mpOp={soMpOp}
                            onSelect={selectSoMp}
                            onSortData={onSortData}
                        />
                    </div>
                </div>
            </div>
            <div className="widget-wrapper">
                <div className="data-card-wrapper">
                    <div className="data-card" style={{width: '100%'}}>
                        <InventoryStackedBarChart
                            data={invData}
                            mp={invMp}
                            mpOp={invMpOp}
                            onSelect={selectInvMp}
                        />
                    </div>
                </div>
            </div>

        </div>
    )
}

export default KeyProductStatsDashboard;
