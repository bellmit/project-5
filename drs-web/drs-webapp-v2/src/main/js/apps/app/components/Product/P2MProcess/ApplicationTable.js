import React, { useState, Fragment, useEffect } from 'react'
import { useTable, usePagination, useSortBy, useFilters } from 'react-table';
import { Link} from 'react-router-dom';
import { useDispatch, useSelector} from 'react-redux';
import { getExactApplication, changeAp } from '../../../actions';
import { Table, Row, Button, Input } from 'reactstrap';
import { InlineFilter, DefaultColumnFilter, SelectColumnFilter } from '../common/Filters';
import { getFilterList, applyToRemoveP2MAp } from '../../../actions/index';
// import { useExportData } from 'react-table-plugins';
// import Cards from './Cards';
// import Papa from "papaparse";
// import XLSX from "xlsx";
// import JsPDF from "jspdf";
// import "jspdf-autotable";
import {FormattedMessage} from 'react-intl';

// const getExportFileBlob = ({ columns, data, fileType, fileName }) => {
//     if (fileType === "csv") {
//       // CSV example
//       const headerNames = columns.map((col) => col.exportValue);
//       const csvString = Papa.unparse({ fields: headerNames, data });
//       return new Blob([csvString], { type: "text/csv" });
//     } else if (fileType === "xlsx") {
//       // XLSX example
  
//       const header = columns.map((c) => c.exportValue);
//       const compatibleData = data.map((row) => {
//         const obj = {};
//         header.forEach((col, index) => {
//           obj[col] = row[index];
//         });
//         return obj;
//       });
  
//       let wb = XLSX.utils.book_new();
//       let ws1 = XLSX.utils.json_to_sheet(compatibleData, {
//         header,
//       });
//       XLSX.utils.book_append_sheet(wb, ws1, "React Table Data");
//       XLSX.writeFile(wb, `${fileName}.xlsx`);
  
//       // Returning false as downloading of file is already taken care of
//       return false;
//     }
//     //PDF example
//     if (fileType === "pdf") {
//       const headerNames = columns.map((column) => column.exportValue);
//       const doc = new JsPDF();
//       doc.autoTable({
//         head: [headerNames],
//         body: data,
//         margin: { top: 20 },
//         styles: {
//           minCellHeight: 9,
//           halign: "left",
//           valign: "center",
//           fontSize: 11,
//         },
//       });
//       doc.save(`${fileName}.pdf`);
  
//       return false;
//     }
  
//     // Other formats goes here
//     return false;
// }

// const IndeterminateCheckbox = React.forwardRef(
//     ({ indeterminate, ...rest }, ref) => {
//         const defaultRef = React.useRef()
//         const resolvedRef = ref || defaultRef

//         React.useEffect(() => {
//             resolvedRef.current.indeterminate = indeterminate
//         }, [resolvedRef, indeterminate])

//         return <input type="checkbox" ref={resolvedRef} {...rest} />
//     }
// )

// const handleUpdateAp= (subAp) => {
//     console.log('hiiii handle update current sub ap')
//     // let currentSubAp = {}
//     // if (subAp == 'marketplace') {
//     //     currentSubAp = applicationState.currentAp.subApplication.marketPlaceInfo
//     // } else if (subAp == 'insurance') {
//     //     currentSubAp = applicationState.currentAp.subApplication.insurance
//     // } else if (subAp == 'regional') {
//     //     currentSubAp = applicationState.currentAp.subApplication.regional
//     // } else if (subAp == 'shipping') {
//     //     currentSubAp = applicationState.currentAp.subApplication.shipping
//     // } else if (subAp == 'productinfo') {
//     //     currentSubAp = applicationState.currentAp.subApplication.productInfo
//     // }
//     // console.log(currentSubAp)
//     // dispatch(updateCurrentSubAp(currentSubAp))
// } 

const ApplicationTable = ({ columns , data , fetchData , pageCount: controlledPageCount, pageIndex, pageSize}) => {

    const dispatch = useDispatch();
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state => state.d)
    const user = useSelector(state => state.d.u)
    // const [showColumns, setShowColumns] = useState({show: false, style: 'hidden'});
   // const [totalPages, setTotalPages] = useState(0);

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        prepareRow,
        page,
        // exportData,
        // below props related to 'hide cells'
        // allColumns,
        // getToggleHideAllColumnsProps,
        // below props related to 'usePagination' hook
        // canPreviousPage,
        // canNextPage,
        // pageOptions,
        // pageCount,
        // gotoPage,
        // nextPage,
        // previousPage,
        // setPageSize,
        // state: { pageIndex, pageSize },
    } = useTable(
        { 
            columns,
            data,
            defaultColumn: { Filter: DefaultColumnFilter },
            // initialState: { pageIndex: 0 },
            // getExportFileBlob,
            manualPagination : true,
            pageCount : controlledPageCount,
            autoResetPage:false
        },
        useFilters,
        useSortBy,
        // useExpanded,
        usePagination,
        // useExportData
    );

    const generateSortingIndicator = column => {
        if (column.disableSortBy) {
            return <div></div>
        }
        return column.isSorted 
            ? (column.isSortedDesc
                ? <i class="fa fa-sort-desc" style={{marginLeft: '4px'}} aria-hidden="true"></i>
                : <i class="fa fa-sort-asc" style={{marginLeft: '4px'}} aria-hidden="true"></i>)
            : <i class="fa fa-sort" style={{marginLeft: '4px'}} aria-hidden="true"></i>
    }

    // const handleFilter = (column) => {
        
    //     // if (column.id == 'name') {
    //     //     console.log('column',column)
    //     //     console.log('handle filter', p2m.filterTarget)
    //     // }
   
    //     // setFilter(p2m.filterTarget || undefined)
    // }

    // useEffect(() => {
    //      fetchData({ pageIndex, pageSize })
    // //}, [onFetchData, pageIndex, pageSize, sortBy, filters])
    // }, [fetchData, pageIndex , pageSize])
    
    const [p2mLength, setP2MLength] = useState('Loading')

    useEffect(() => {
        console.log('data change')
        setP2MLength(data.length)
    },[data])

    const handleApplyToRemoveAp = (e,id) => {
        const message = "下架申請已送出"
        const supplierId = d.u.cid;
        if (confirm("請確認 " + e + " 是否提出申請下架。送出時，我們會一併審核。（如資料有缺漏我們將退回申請。）")) {
            dispatch(applyToRemoveP2MAp(supplierId,id,message))
        }
    }


//     useEffect(() => {
//         fetchData({ pageIndex, pageSize })
//    //}, [onFetchData, pageIndex, pageSize, sortBy, filters])
//    }, [fetchData, pageIndex , pageSize])
   
    
    return (
        <Table className="table-main" {...getTableProps()}>
            <thead className="table-head">
                
                {headerGroups.map(headerGroup => (
                    <tr className="table-head-tr" {...headerGroup.getHeaderGroupProps()}>

                        {headerGroup.headers.map(column => {
                            // handleFilter(column)
                            return (
                            <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                                <div {...column.getSortByToggleProps()}>
                                    {column.render("Header")}
                                    {generateSortingIndicator(column)}
                                </div>
                            </th>)}
                        )}
                    </tr>
                ))}
            </thead>

            <tbody className='table-tbody' {...getTableBodyProps()}>

            { p2mLength == 'Loading'
                ?    <tr><td>
                        <div className="spinner-wrapper">
                            <div className="spinner"></div>
                            <div className="spinner-text"><span>Loading...</span></div>
                        </div>
                    </td></tr>
                :   p2mLength == 0
                    ?   <tr><td colSpan='11'><div style={{ marginBottom: '2%',padding: '4%',textAlign: 'center',backgroundColor: 'rgba(90, 96, 127,0.1)',borderRadius: '8px'}}>
                                <p style={{fontSize: '15px', color: '#5a607f', display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                                    {/* <span style={{paddingTop: '2px', marginRight: '8px'}}><AnnouncementIcon/></span> */}
                                    <FormattedMessage id="p2m.noData"/>
                                </p>
                            </div>
                        </td> </tr>
                    :   null
                }

                {page.map(row => {
                     console.log(row, 'row')
                    prepareRow(row)
                    return (
                        
                        <Fragment key={row.getRowProps().key}>
                            <tr className="table-tbody-tr">
                                {row.cells.map((cell,index) => {
                                    // console.log(cell, 'cell')
                                    if (index == 0) {
                                        return ( 
                                            <td style={{padding: '12px'}}
                                                {...cell.getCellProps({style: {minWidth: cell.column.minWidth, width: cell.column.width }})}
                                                onClick={() => { 
                                                    // console.log('row.original._id', row.original._id.$oid)
                                                    // handleUpdateCurrentAp(cell.value);
                                                    // handleUpdateCurrentAp(row.original._id)
                                                    // dispatch(getExactApplication(row.original._id.$oid))
                                                    dispatch(changeAp({$oid: row.original._id.$oid}))
                                                }}
                                            > 
                                                    
                                            <a className="drs-link-normal">{cell.render('Cell')}</a>
                                                {/* <Link className='drs-link-normal' to='/product/application'>{cell.render('Cell')}</Link> */}
                                            </td>
                                        )
                                    } else if (index == 1) {
                                        return <td {...cell.getCellProps( {style: {marginRight: '2%',lineHeight: '1.5rem', fontSize: '15px', minWidth: cell.column.minWidth, width: cell.column.width }})}>{cell.render('Cell').props.value}</td>
                                    } else if (index == 3) {
                                        let ad = ""
                                        if(row.original.appliedDate != "") ad = new Intl.DateTimeFormat('en-US').format(row.original.appliedDate)
                                        return <td {...cell.getCellProps( {style: {minWidth: cell.column.minWidth, width: cell.column.width }})}> {ad}</td>
                                    } else if (index == 4) {
                                        let apd = ""
                                        if(row.original.approvedDate != "") apd = new Intl.DateTimeFormat('en-US').format(row.original.approvedDate)
                                        return <td {...cell.getCellProps( {style: {minWidth: cell.column.minWidth, width: cell.column.width }})}> {apd}</td>
                                    } else if (index == 5) {
                                        let rd = ""
                                        if(row.original.rejectedDate != "") rd = new Intl.DateTimeFormat('en-US').format(row.original.rejectedDate)
                                        return <td {...cell.getCellProps( {style: {minWidth: cell.column.minWidth, width: cell.column.width }})}> {rd}</td>
                                    } else if (index == 6) {
                                    //     let canceldt = ""
                                    //     if(row.original.cancelledDate != "") canceldt = new Intl.DateTimeFormat('en-US').format(row.original.cancelledDate)
                                    //     return <td {...cell.getCellProps( {style: {minWidth: cell.column.minWidth, width: cell.column.width }})}> {canceldt}</td>
                                    // } else if (index == 6) {
                                        return <td {...cell.getCellProps({style: { minWidth: cell.column.minWidth, width: cell.column.width }}
                                            )}>
                                                <div className="table-cell-col">
                                                    {cell.render('Cell').props.value.map((t,i) => {
                                                        return (<span style={{margin: '2px 6px 2px 2px', fontSize: '14px', lineHeight: '1rem'}}>
                                                            {t} 
                                                            {i == cell.render('Cell').props.value.length - 1 ? '' : ','}
                                                            </span>)
                                                    })}
                                                </div>
                                            </td>
                                    } else if (index == 10) {
                                        if (user.isSp == true){
                                            if(row.original.status == p2m.stMap.get(3)){
                                                return (
                                                    <td style={{padding: '12px',minWidth: cell.column.minWidth,width: cell.column.width, textAlign:'center'}}>
                                                        <div className='table-dropdown'>
                                                            <div className="table-dropdown-title-wrapper-2">
                                                                <i style={{color: '#7E84A3', fontSize: '18px'}} class="fa fa-bars" aria-hidden="true"></i>
                                                            </div>
                                                            <div className="table-dropdown-content" style={{left:'-15px',minWidth:'115px', textAlign:'initial'}}>
                                                                <button className="table-btn"
                                                                    onClick={(e) => { handleApplyToRemoveAp(row.original.name,row.original._id.$oid) }}
                                                                >
                                                                    申請下架
                                                                </button>
                                                                <button className="table-btn"
                                                                    onClick={()=> {dispatch(changeAp({$oid: row.original._id.$oid}))}}
                                                                >
                                                                    檢視
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </td>
                                                )
                                            } else{
                                                return (
                                                    <td style={{padding: '12px',minWidth: cell.column.minWidth,width: cell.column.width, textAlign:'center'}}>
                                                        <div className='table-dropdown'>
                                                            <div className="table-dropdown-title-wrapper-2">
                                                                <i style={{color: '#7E84A3', fontSize: '18px'}} class="fa fa-bars" aria-hidden="true"></i>
                                                            </div>
                                                            <div className="table-dropdown-content" style={{left:'-15px',minWidth:'88px', textAlign:'initial'}}>
                                                                <button className="table-btn"
                                                                    onClick={()=> {dispatch(changeAp({$oid: row.original._id.$oid}))}}
                                                                >
                                                                    檢視
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </td>
                                                )
                                            }
                                        } else{
                                            return (
                                                <td style={{padding: '12px',minWidth: cell.column.minWidth,width: cell.column.width, textAlign:'center'}}>
                                                    <div className='table-dropdown'>
                                                        <div className="table-dropdown-title-wrapper-2">
                                                            <i style={{color: '#7E84A3', fontSize: '18px'}} class="fa fa-bars" aria-hidden="true"></i>
                                                        </div>
                                                        <div className="table-dropdown-content" style={{left:'-15px',minWidth:'88px', textAlign:'initial'}}>
                                                            <button className="table-btn"
                                                                onClick={()=> {dispatch(changeAp({$oid: row.original._id.$oid}))}}
                                                            >
                                                                檢視
                                                            </button>
                                                        </div>
                                                    </div>
                                                </td>
                                            )
                                        }
                                    } else {
                                        return<td {...cell.getCellProps(
                                            {style: {minWidth: cell.column.minWidth, width: cell.column.width }}
                                        )}><span>{cell.render('Cell')}</span></td>
                                    }
                                })}
                            </tr>
                            {/* {row.isExpanded && (
                                <tr>
                                    <td colSpan="11">
                                        <div className="table-tbody-tr table-td-expander" onClick={() => dispatch(getExactSubAp(row.original._id.$oid, 'marketplace'))}>
                                            <Link className="application-sheet-list-link" to='/product/application/marketplace'>
                                                <span className="expander-title">{row.original.subApplication.marketPlaceInfo.name}</span>
                                            </Link>
                                        </div>
                                        <div className="table-tbody-tr table-td-expander" onClick={() => dispatch(getExactSubAp(row.original._id.$oid, 'insurance'))}>
                                            <Link className="application-sheet-list-link" to='/product/application/insurance'>
                                                <span className="expander-title">{row.original.subApplication.insurance.name}</span>
                                            </Link>
                                        </div>
                                        <div className="table-tbody-tr table-td-expander" onClick={() => dispatch(getExactSubAp(row.original._id.$oid, 'regional'))}>
                                            <Link className="application-sheet-list-link" to='/product/application/regional'>
                                                <span className="expander-title">{row.original.subApplication.regional.name}</span>
                                            </Link>
                                        </div>
                                        <div className="table-tbody-tr table-td-expander" onClick={() => dispatch(getExactSubAp(row.original._id.$oid, 'shipping'))}>
                                            <Link className="application-sheet-list-link" to='/product/application/shipping'>
                                                <span className="expander-title">{row.original.subApplication.shipping.name}</span>
                                            </Link>
                                        </div>
                                        <div className="table-tbody-tr table-td-expander" onClick={() => dispatch(getExactSubAp(row.original._id.$oid, 'productinfo'))}>
                                            <Link className="application-sheet-list-link" to='/product/application/productinfo'>
                                                <span className="expander-title">{row.original.subApplication.productInfo.name}</span>
                                            </Link>
                                        </div>
                                    </td>
                                </tr>
                            )} */}
                        </Fragment>
                    )
                })}
            </tbody>
        </Table>
            
    )
}

export default ApplicationTable
