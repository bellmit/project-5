import React, { useEffect, useMemo, useState, useRef } from 'react';
import CreateP2MApplication from './CreateP2MApplication';

const P2MModal = ({show,modal,onClose}) => {
    if (!show) {
        return null;
    }
    return (
        <div id="modal" className="drs-modal">
            <div className="drs-modal-content">
                { modal == 'applyP2M'
                    ?   <CreateP2MApplication
                            onClose={onClose}
                        />
                    :   <div></div>
                }
                
            </div>
        </div>
    )
}

export default P2MModal