import React from 'react';
import { useSelector} from 'react-redux';
import { FormattedMessage} from 'react-intl';

const P2MApplicationInfo = () => {
    const p2m = useSelector(state => state.P2M)
    const appliedDate = p2m.currentAp.appliedDate != "" ?
        new Intl.DateTimeFormat('en-US').format(p2m.currentAp.appliedDate):p2m.currentAp.appliedDate
             
    const approvedDate = p2m.currentAp.approvedDate != "" ?
        new Intl.DateTimeFormat('en-US').format(p2m.currentAp.approvedDate):p2m.currentAp.approvedDate

    // const cancelledDate = p2m.currentAp.cancelledDate != "" ?
    //     new Intl.DateTimeFormat('en-US').format(p2m.currentAp.cancelledDate):p2m.currentAp.cancelledDate
        
    const rejectedDate = p2m.currentAp.rejectedDate != "" ?
        new Intl.DateTimeFormat('en-US').format(p2m.currentAp.rejectedDate):p2m.currentAp.rejectedDate

    return (
        <div className="application-info-section-wrapper">
            <p className="application-info-section-title">
                <FormattedMessage id="p2mapplication.applicationinfo"/>
            </p>
            <div className="application-info-section">
                <div className="application-info-column" style={{width: '25%'}}>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.supplier"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.supplierId}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.status"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.status}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.applicationtype"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.type}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.country"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.selectedCountry}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.platform"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.selectedPlatform}</p>
                    </div>
                    
                </div>
                <div className="application-info-column" style={{width: '25%'}}>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.applieddate"/>
                        </p>
                        <p className="application-info-content">{appliedDate}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.approveddate"/>
                        </p>
                        <p className="application-info-content">{approvedDate}</p>
                    </div>
                    {/* <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.cancelledDate"/>
                        </p>
                        <p className="application-info-content">{cancelledDate}</p>
                    </div> */}
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '50%'}}>
                            <FormattedMessage id="p2mapplication.rejectedDate"/>
                        </p>
                        <p className="application-info-content">{rejectedDate}</p>
                    </div>
                </div>
                
                <div className="application-info-column" style={{width: '50%'}}>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '25%'}}>
                            <FormattedMessage id="p2mapplication.brandname"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.brandNameEN}</p>
                    </div>
                    <div className="application-info">
                        <p className="application-info-title" style={{width: '25%'}}>
                            <FormattedMessage id="p2mapplication.productname"/>
                        </p>
                        <p className="application-info-content">{p2m.currentAp.productNameEN}</p>
                    </div>
                    <div className="application-info" style={{alignItems: 'flex-start'}}>
                        <p className="application-info-title" style={{width: '25%'}}>SKU</p>
                        <p className="application-info-content" style={{width: '50%', flexWrap: 'wrap', display:'flex'}}>
                            { p2m.currentAp.appliedSkuCode.map((sku,i) => {
                                return (<span style={{marginRight: '6px'}}>{sku}
                                    {i == p2m.currentAp.appliedSkuCode.length - 1 ? '': ','}
                                </span>)
                            })}
                        </p>
                    </div>
                </div>                            
            </div>
        </div>
    )
}

export default P2MApplicationInfo