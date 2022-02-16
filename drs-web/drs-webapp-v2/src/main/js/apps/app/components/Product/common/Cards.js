import React, { useEffect } from 'react';
import '../../../sass/cards.scss';
// import { useSelector, useDispatch } from 'react-redux';
import {FormattedMessage} from 'react-intl';

const Cards = ({data}) => {
    
    useEffect(()=> {
        console.log('init card')
        // const fakeJson = {
        //     totalBp: 5,
        //     totalSku: 12,
        //     appliedSku: 12,
        //     salingSku: 12,
        // }
        // setData(fakeJson)
        // console.log(props.columns);
        // console.log(props.data);
        // console.log(props.rows)
    },[])
        
    return(
        <div className="data-card-wrapper">
            {data.map(item => {
                return (
                    <div className="data-card-s" style={{width: '24%'}}>
                        <p className="data-card-title">
                            <FormattedMessage id={item.title}/>
                        </p>
                        <p className="data-card-content">{item.num}</p>

                        <div className="data-card-info">
                            {/* <span className="data-card-static static-grow-up">2.3%</span>
                            <span className="data-card-description">than last month</span> */}
                        </div>
                    </div>
                )
            })}
            
            
        </div>
    )
}

export default Cards;