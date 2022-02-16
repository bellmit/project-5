import React, { useState, useEffect } from 'react';

const CheckBoxValidationWords = (props) => {
    return (
        <div style={{paddingLeft: '2%'}}>
            { props.show
                ?   <div className="base-input-notice-wrapper">
                        <p className="base-input-notice drs-notice-red">
                            <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                            請打勾以確認同意此項目
                        </p>
                    </div>
                : null
            }
        </div>
    )
}

export default CheckBoxValidationWords