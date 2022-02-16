import React,{ useEffect, useState } from 'react';
import '../../../sass/category.scss';
import { useSelector, useDispatch } from 'react-redux'
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import { getProductCategory, updateCurrentProductCategory } from '../../../actions/index';
import ClearRoundedIcon from '@material-ui/icons/ClearRounded';
import CircularProgress from '@material-ui/core/CircularProgress';
import '../../../sass/scroll.scss'
import categoryDemo from '../../../assets/images/category-demo.png'
import {FormattedMessage} from 'react-intl';

// const pC = [{name: 'Appliances'}, {name: 'Arts, Crafts & Sewing'},{name: 'Automotive'}, {name: 'Baby Products'}, {name: 'Beauty & Personal Care'},
// {name: 'Books'}, {name: 'CDs & Vingl'}, {name: 'Cell Phones & Accessories'}, {name: 'Clothing, Shoes & Jewelry'}, {name: 'Collectibles & Fine Art'},
// {name: 'Electronics'}, {name: 'Grocery & Gourmet Food'}, {name: 'Handmade Products'}, {name: 'Health & Household'}, {name: 'Home & Kitchen'}, 
// {name: 'Industrial & Scientific'}, {name: 'Movies & TV'}, {name: 'Musical Instruments'}, {name: 'Office Products'}, {name: 'Patio, Lawn & Garden'},
// {name: 'Pet Supplies'}, {name: 'Software'}, {name: 'Sports & Outdoors'}, {name: 'Tools & Home Improvement'}, {name: 'Toys & Games'}, {name: 'Video Games'} ]

const Category = (props) => {
    const pd = useSelector(state => state.PD)
    const dispatch = useDispatch()

    // const [selected, setSelected] = useState(pd.currentCategory)
    const [selected, setSelected] = useState('')
    const [path, setPath] =  useState([''])
    const [showList, setShowList] = useState(true)
       
    const onSelectCategory = (item) => {
        // props.selectCategory(value);
        // console.log(item)
        
        if(item.isLast == '1') {
            props.updateShow('next')
            setShowList(false)
        } else {
            dispatch(getProductCategory(item.name))
            props.updateShow('')
            setShowList(true)
        }

        if (item.path) {
            dispatch(updateCurrentProductCategory(item.path))
            setSelected(item.path)
            let arr = [...path];
            // arr.push(item.name)
            arr.push(item)
            // console.log(arr)
            setPath(arr)
        } else {
            setSelected(item.name)
            dispatch(updateCurrentProductCategory(item.name))
            let arr = [];
            // arr.push(item.name)
            arr.push(item)
            // console.log(arr)
            setPath(arr)
        }
    }

    const onBreadcrumbsChange = (item) => {
        if(item == '') {
            dispatch(getProductCategory(''))
            dispatch(updateCurrentProductCategory(''))
            setSelected('')
        } else {
            dispatch(getProductCategory(item.name))
            if (item.path) {
                dispatch(updateCurrentProductCategory(item.path))
                setSelected(item.path)
                let arr = [...path];
                let i = arr.indexOf(item)
                console.log(i)
                let result = arr.slice(0,i+1);
                console.log(result);
                setPath(result)
            } else {
                setSelected(item.name)
                dispatch(updateCurrentProductCategory(item.name))
                let arr = [];
                arr.push(item)
                setPath(arr)
            }
        }
        setShowList(true)
        // console.log(item)
        
    }

    const handleBreadcrumbs = () => {
        // const arr = selected.split(',');
        // console.log(arr)
        return path.map((item, index) => {
            return (
                <li className="breadcrumbs-btn-li">
                    <button className="breadcrumbs-btn" onClick={() => { console.log('onclick');  onBreadcrumbsChange(item) }}>
                        <span className="breadcrumbs-btn-text">{item.name}</span>
                    </button>
                    {index == path.length - 1 ? null : <ChevronRightIcon/>  }
                </li>)
            
        })
    }

    useEffect(() => {
        dispatch(getProductCategory(''))
    },[])

    return(
        <div style={{width: '70%'}}>
            <div className="base-reminder-wrapper">
                <p className="base-reminder" style={{width: '100%'}}>
                <i class="fa fa-exclamation-circle" style={{marginRight: '3px'}} aria-hidden="true"></i>
                <FormattedMessage id ="category.reminder"></FormattedMessage>
                </p>
            </div>
            <div className="application-img-wrapper">
            <img src={categoryDemo} alt='categoryDemo' />
            </div>
            { selected == '' 
                ? <div className="breadcrumbs-container">
                    <span className="breadcrumbs-text-placeholder" style={{marginLeft: '12px'}}>
                        <FormattedMessage                                
                            id="category.select"
                        /> ...
                    </span>
                </div>
                : <div className="breadcrumbs-container" style={{border: '1px solid #053763'}}>
                    <ul style={{display: 'flex', padding: '4px'}}>
                        { handleBreadcrumbs() }
                    </ul>
                    <button style={{paddingRight: '2%'}} onClick={() => { onBreadcrumbsChange('') }}>
                        <ClearRoundedIcon/>
                    </button>
                    {/* <p className="product-category-info">你選擇的是 { selected }</p> */}
                  </div>
            }
            { pd.loading
                ? <div className="page-loading-wrapper"><CircularProgress /></div>
                : <div className="scroll" style={showList ? {boxShadow: '0 6px 10px -4px rgba(0, 0, 0, 0.15)', maxHeight: '600px'}: {boxShadow: 'none'}}>
                    
                    { showList
                        ?   <ul className="product-category-list">
                                {/* { props.productCategory.map(list => (
                                    <li className="product-category-li">
                                        <button onClick={()=> onSelectCategory(list.category)}>
                                            {list.category}
                                        </button>
                                    </li>
                                ))} */}
                                { pd.productCategory.map(item => {
                                    return item.isLast == '1'
                                        ? <li className="product-category-li">
                                            
                                            <div className="product-category-li-last" style={{padding: '2%'}}>
                                                {item.name}
                                            </div>
                                            
                                            <div style={{marginRight: '2%'}}>
                                                <button
                                                    className="category-btn"
                                                    onClick={()=> onSelectCategory(item, true)}> Select </button>
                                            </div>
                                            
                                        </li>
                                        : <li className="product-category-li">
                                            
                                            <button
                                                style={{width: '100%', textAlign: 'left', padding: '2%'}}
                                                onClick={()=> onSelectCategory(item, false)}
                                            >
                                                {item.name}
                                            </button>
                                            
                                            <div style={{marginRight: '2%'}}><ChevronRightIcon/></div>
                                        </li>
                                })}
                            </ul>
                        :   null
                    }
                </div>
            }
        </div>
    )
}

export default Category;