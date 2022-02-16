import React, { useState, Fragment } from 'react'
import { useDispatch } from 'react-redux'
import { useTable, usePagination, useSortBy, useFilters, useExpanded } from 'react-table'
import { Table, Row, Col, Button, Input, CustomInput } from 'reactstrap';
import { Filter, InlineFilter, DefaultColumnFilter } from '../common/Filters';
// import { useExportData } from "react-table-plugins";
 import {useHistory} from "react-router-dom";
// import Cards from './Cards';
// import Papa from "papaparse";
// import XLSX from "xlsx";
// import JsPDF from "jspdf";
// import "jspdf-autotable";
// import chevronDown from '../../../assets/images/chevron-down@2x.png';
import {FormattedMessage} from 'react-intl';
import { showCreateP2MModal} from '../../../actions/manageP2MAction';
import {DOMAIN_NAME} from '../../../constants/action-types';
import '../../../sass/table.scss';
 
// function getExportFileBlob({ columns, data, fileType, fileName }) {
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

const Url = DOMAIN_NAME;

const IndeterminateCheckbox = React.forwardRef(
    ({ indeterminate, ...rest }, ref) => {
        const defaultRef = React.useRef()
        const resolvedRef = ref || defaultRef

        React.useEffect(() => {
            resolvedRef.current.indeterminate = indeterminate
        }, [resolvedRef, indeterminate])

        return <input type="checkbox" ref={resolvedRef} {...rest} />
    }
)

const ProductTable = ({ id, bpStatus, title, columns, data, renderRowSubComponent,
     handleShowModal,handleSetModal, handleSetBPId, onRemoveBP }) => {
    const dispatch = useDispatch()
    const [showColumns, setShowColumns] = useState({show: false, style: 'hidden'});
    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        // rows,
        page,
        prepareRow,
        // exportData,
        // below props related to 'hide cells'
        allColumns,
        getToggleHideAllColumnsProps,
        // below props related to 'usePagination' hook
        canPreviousPage,
        canNextPage,
        pageOptions,
        // pageCount,
        gotoPage,
        nextPage,
        previousPage,
        // setPageSize,
        state: { pageIndex, pageSize },
    } = useTable(
        { 
            columns,
            data,
            defaultColumn: { Filter: DefaultColumnFilter },
            initialState: { pageIndex: 0, pageSize: 5 },
            // getExportFileBlob,
        },
        useFilters,
        useSortBy,
        useExpanded,
        usePagination,
        // useExportData
    );
    
    // const onChangeInSelect = event => {
    //     setPageSize(Number(event.target.value))
    // }

    const onChangeInInput = event => {
        const page = event.target.value ? Number(event.target.value) - 1 : 0
        gotoPage(page)
    }
    const generateSortingIndicator = column => {
        // return column.isSorted ? (column.isSortedDesc ? " 🔽" : " 🔼") : ""
        if (column.id == 'expander') {
            return <div></div>
        }
        return column.isSorted 
            ? (column.isSortedDesc 
                ? <i class="fa fa-sort-desc" style={{marginLeft: '4px'}} aria-hidden="true"></i>
                : <i class="fa fa-sort-asc" style={{marginLeft: '4px'}} aria-hidden="true"></i>)
            : <i class="fa fa-sort" style={{marginLeft: '4px'}} aria-hidden="true"></i>
    }
    
    let history = useHistory();

    const handleApplySales = () =>{
        const skuOptions = data.map(item => {
            return {value: item.sellerSku, label: item.sellerSku}
        })
        const apTypeNew = [{ value: 'new', label: "managep2m.new"}]
        dispatch(showCreateP2MModal(apTypeNew[0],title , id , skuOptions))
    
        history.push("/product/apply");
    }

    const handleApplyShipment = () =>{
        window.location.assign(DOMAIN_NAME + '/InventoryShipments');
    }

    const handleChangeP2M = () => {
        const skuOptions = data.map(item => {
            return {value: item.sellerSku, label: item.sellerSku}
        })
        const apTypeUpdate =  [{ value: 'update', label: "managep2m.update"}]
        dispatch(showCreateP2MModal(apTypeUpdate[0],title , id , skuOptions))
        history.push("/product/apply");
    }

    return (
        <Fragment>
            {/* <Table className="table-filter-section" {...getTableProps()}>
                {headerGroups.map(headerGroup => (
                    <tr className="table-filter-row" {...headerGroup.getHeaderGroupProps()}>
                        
                        {headerGroup.headers.map(column => (
                            <th id="table-filter-th" colspan="3" {...column.getHeaderProps()}>
                                <Filter column={column}/>
                            </th>
                        ))}
                        
                    </tr>
                ))}
            </Table> */}
            {/* <Cards
                columns={columns}
                data={data}
                rows={rows}
            /> */}
            <div className="table-title-section">
                <span className="table-title">{title}</span>
                <div className="table-action-container">
                    <Table className="table-filter-section table-inline-filter" {...getTableProps()}>
                        {headerGroups.map(headerGroup => (
                            <tr className="table-filter-row" {...headerGroup.getHeaderGroupProps()}>
                                {headerGroup.headers.map(column => (
                                    <th id="table-filter-th" colspan="3" {...column.getHeaderProps()}>
                                        <InlineFilter column={column}/>
                                    </th>
                                ))}
                            </tr>
                        ))}
                    </Table>
                    <div className="export-btn-group">            
                        {/* <button
                            className="table-btn"
                            onClick={() => {
                                exportData("csv", true);
                            }}
                        >
                            {/* Export All as CSV */}
                            {/* Export as CSV
                        </button> */} 
                        {/* <button
                            className="table-btn"
                            onClick={() => {
                                exportData("csv", false);
                            }}
                        >
                            Export Current View as CSV
                        </button> */}
                        {/* <button
                            className="table-btn"
                            onClick={() => {
                                showColumns.show == false 
                                ? setShowColumns({show: true, style: 'show'})
                                : setShowColumns({show: false, style: 'hidden'})
                            }}
                        >
                            Show Columns
                        </button> */}
                        <div className='table-dropdown'>
                            <div className="selector-inline-wrapper" style={{marginTop: 0}}>
                                <div className="table-dropdown-title-wrapper">
                                    <span className="table-dropdown-title">
                                        <FormattedMessage id="product.action"/>
                                    </span>
                                </div>
                                <div className="dropdown-icon-wrapper">
                                    <i style={{color: '#7E84A3', fontSize: '8px', marginBottom: '2px'}} class="fa fa-bars" aria-hidden="true"></i>
                                </div>
                            </div>

                            { bpStatus == 'unapplied'
                                ?   <div className="table-dropdown-content">  
                                        <button className="table-btn"
                                            onClick={() => {
                                               handleApplySales()
                                            }}
                                        >
                                            <FormattedMessage id="product.applyforsale"/>
                                        </button>
                                        <button className='table-btn'
                                            onClick={() => {
                                                handleShowModal(true);
                                                handleSetModal('updateProduct');
                                                handleSetBPId(id)
                                            }}
                                        >
                                            <FormattedMessage id="product.edit"/>
                                        </button>
                                        <button className="table-btn" onClick={() => {onRemoveBP(id)}}>
                                            <FormattedMessage id="product.delete"/>
                                        </button>
                                        <button
                                            className="table-btn"
                                            onClick={() => {
                                                showColumns.show == false 
                                                ? setShowColumns({show: true, style: 'table-show'})
                                                : setShowColumns({show: false, style: 'hidden'})
                                            }}
                                        >
                                            <FormattedMessage id="product.showColumns"/>
                                        </button>
                                        {/* <button className="table-btn" onClick={() => {exportData("csv", true)}}>
                                            <FormattedMessage id="product.export"/>
                                        </button> */}
                                    </div>
                                :   <div></div>
                            }
                            { bpStatus == 'applied'
                                ?   <div className="table-dropdown-content">
                                        <button className="table-btn"
                                            onClick={() => {
                                               handleApplySales()
                                            }}
                                        >
                                            <FormattedMessage id="product.applyforsale"/>
                                        </button>
                                        <button className='table-btn' onClick={
                                            () => handleApplyShipment()
                                        }>
                                            <FormattedMessage id="product.applyforshipment"/>
                                        </button>
                                        <button className='table-btn' onClick={
                                            ()=>{handleChangeP2M()}
                                        }>
                                            <FormattedMessage id="product.applyforchange"/>
                                        </button>
                                        {/* <button className="table-btn"
                                            onClick={() => handleStatusChange(id,'unapplied')}
                                        >
                                            <FormattedMessage id="product.applyforremoval"/>
                                        </button> */}
                                        <button className="table-btn"
                                            onClick={() => {
                                                handleShowModal(true);
                                                handleSetModal('addNewSku');
                                                handleSetBPId(id)
                                        }}>
                                            {/* <FormattedMessage id="product.addSKU"/> */}
                                            <FormattedMessage id="product.edit"/>
                                        </button>
                                        <button className="table-btn"
                                            onClick={() => {
                                                showColumns.show == false 
                                                ? setShowColumns({show: true, style: 'table-show'})
                                                : setShowColumns({show: false, style: 'hidden'})
                                            }}
                                        >
                                            <FormattedMessage id="product.showColumns"/>
                                        </button>
                                        {/* <button
                                            className="table-btn"
                                            onClick={() => { exportData("csv", true) }}
                                        >
                                            <FormattedMessage id="product.export"/>
                                        </button> */}
                                    </div>
                                :   <div></div>
                            }

                        </div>
                    </div>
                    
                </div>
            </div>            
            <div className={`table-checkbox-section ${showColumns.style}`}>
                <div className="table-checkbox-wrapper" >
                    <label className="table-checkbox-label">
                        <IndeterminateCheckbox {...getToggleHideAllColumnsProps()} /> Toggle All
                        <span className="checkmark"></span>
                    </label>
                </div>
                {allColumns.map(column => {
                    return (
                    <div className="table-checkbox-wrapper" key={column.id}>
                        <label className="table-checkbox-label">
                            <input type="checkbox" {...column.getToggleHiddenProps()} />{' '}
                            {column.id}
                            <span className="checkmark"></span>
                        </label>
                    </div>
                    )
                })}
                <br />
            </div>
            
            <Table className="table-main" {...getTableProps()}>
                <thead className="table-head">
                    {/* {headerGroups.map(headerGroup => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                            
                            {headerGroup.headers.map(column => (
                                <th id="filter" colspan="6" {...column.getHeaderProps()}>
                                    <Filter column={column}/>
                                </th>
                            ))}
                            
                        </tr>
                    ))} */}
                    {headerGroups.map(headerGroup => (
                        <tr className="table-head-tr" {...headerGroup.getHeaderGroupProps()}>
                            {headerGroup.headers.map(column => (
                                <th {...column.getHeaderProps(
                                        column.getSortByToggleProps(),
                                        {style: {minWidth: column.minWidth, width: column.width }}
                                )}>
                                    <div {...column.getSortByToggleProps()}>
                                        {column.render("Header")}
                                        {generateSortingIndicator(column)}
                                    </div>
                                    {/* <Filter column={column}/> */}
                                </th>
                            ))}
                        </tr>
                    ))}
                </thead>

                <tbody className='table-tbody' {...getTableBodyProps()}>
                    {page.map(row => {
                        prepareRow(row)
                        if (row.original.isSelected == true || row.original.isSelected == null) {
                            return (
                                <Fragment key={row.getRowProps().key}>
                                    <tr className="table-tbody-tr">
                                        {row.cells.map((cell,index) => {
                                            if (cell.column.id == 'selling' || cell.column.id== 'applying') {
                                                // console.log(cell)
                                                // console.log(cell.render('Cell'))
                                                // console.log(cell.render('Cell').props.value)
                                                // console.log(cell.render('Cell').props.value.toString())
                                                return <td {...cell.getCellProps({style: { minWidth: cell.column.minWidth, width: cell.column.width }}
                                                    )}>
                                                        <div className="table-cell-col">
                                                            {cell.render('Cell').props.value.map(t => {
                                                               return <span className="table-cell-col-t">{t}</span>
                                                            })}
                                                        </div>
                                                    </td>
                                            } 
                                            // console.log(cell, 'cell');
                                            // if (index == 6 && type == 'product') {
                                            //     const cellValue = cell.render('Cell').props.cell.value
                                            //     // console.log('cell value',cell.render('Cell').props.cell.value)
                                            //     let showValue = ''; 
                                            //     switch (cellValue) {
                                            //         case 'applied':
                                            //             showValue = 
                                            //             <FormattedMessage id="productdata.applied"/>
                                            //             return <td {...cell.getCellProps()}>{showValue}</td>
                                            //         case 'on selling':
                                            //             showValue = 
                                            //             <FormattedMessage id="productdata.selling"/>
                                            //             return <td {...cell.getCellProps()}>{showValue}</td>
                                            //         case 'unapplied':
                                            //             showValue =
                                            //             <FormattedMessage id="productdata.unapplied"/>
                                            //             return <td {...cell.getCellProps()}>{showValue}</td>
                                            //         case 'in review':
                                            //             showValue = 
                                            //             <FormattedMessage id="productdata.review"/>
                                            //             return <td {...cell.getCellProps()}>{showValue}</td>
                                            //         default:
                                            //             showValue = 
                                            //             <FormattedMessage id="productdata.unknown"/>
                                            //             return <td {...cell.getCellProps()}>{showValue}</td>
                                            //     }
                                                
                                            // } else if (index == 7 && type == 'product') {
                                            //     // console.log(cell.render('Cell').props.cell.value)
                                            //     const arr = cell.render('Cell').props.cell.value;
                                            //     return <td {...cell.getCellProps()}>{
                                            //         arr.map(item => {
                                            //             switch (item) {
                                            //                 case '申請出貨':
                                            //                     return <button className="table-btn" onClick={()=> {console.log('申請出貨')}}>{item}</button>
                                            //                 case '申請變更':
                                            //                     return <button className="table-btn" onClick={()=> {console.log('申請變更')}}>{item}</button>
                                            //                 case '查看進度':
                                            //                     return <button className="table-btn" onClick={()=> {console.log('查看進度')}}>{item}</button>
                                            //                 case '修改':
                                            //                     return <button className="table-btn" onClick={()=> { 
                                            //                         handleSetModal('updateProduct');
                                            //                         handleShowModal(true);
                                            //                         handleSetBPId(id)
                                            //                         console.log('修改')
                                            //                     }}>{item}</button>
                                            //                 case '申請銷售':
                                            //                     return <button className="table-btn"
                                            //                      onClick={()=> {console.log('申請銷售')}}>{item}</button>
                                            //                 default:
                                            //                     return <div></div>
                                            //             }
                                            //         })
                                            //     }</td>                  
                                                
                                            // }
                                            return <td {...cell.getCellProps(
                                                {style: {minWidth: cell.column.minWidth, width: cell.column.width }}
                                            )}><span>{cell.render('Cell')}</span></td>
                                        })}
                                    </tr>
                                    {row.isExpanded && (
                                        <tr>
                                            <td colSpan="8">{renderRowSubComponent(row)}</td>
                                        </tr>
                                    )}
                                </Fragment>
                            )
                        }
                        
                    })}

                </tbody>
            </Table>
            
            <Row className="table-pagination-row">
                <div className="table-pagination-col">
                    {/* <Button
                        onClick={() => gotoPage(0)}
                        disabled={!canPreviousPage}
                    >
                        {"<<"}
                    </Button> */}
                    <Button
                        onClick={previousPage}
                        disabled={!canPreviousPage}
                    >
                        {"<"}
                    </Button>
                </div>
                {/* <Col md={3}>
                    <Button
                        color="primary"
                        onClick={() => gotoPage(0)}
                        disabled={!canPreviousPage}
                    >
                        {"<<"}
                    </Button>
                    <Button
                        color="primary"
                        onClick={previousPage}
                        disabled={!canPreviousPage}
                    >
                        {"<"}
                    </Button>
                </Col> */}
                {/* <Col md={1} style={{ marginTop: 2 }}>
                    Page{" "}
                    <strong>
                        {pageIndex + 1} of {pageOptions.length}
                    </strong>
                </Col> */}
                <div className="table-pagination-col">
                    Page{" "}
                    <strong>
                        {pageIndex + 1} of {pageOptions.length}
                    </strong>
                </div>
                <div className="table-pagination-col">
                    <Input
                        type="number"
                        min={1}
                        style={{ width: 70 }}
                        max={pageOptions.length}
                        defaultValue={pageIndex + 1}
                        onChange={onChangeInInput}
                    />
                </div>
                {/* <Col md={2}>
                    <CustomInput type="select" value={pageSize} onChange={onChangeInSelect}>
                        >
                        {[10, 20, 30, 40, 50].map(pageSize => (
                        <option key={pageSize} value={pageSize}>
                            Show {pageSize}
                        </option>
                        ))}
                    </CustomInput>
                </Col> */}
                <div className="table-pagination-col">
                    <Button onClick={nextPage} disabled={!canNextPage}>
                        {">"}
                    </Button>
                    {/* <Button
                        onClick={() => gotoPage(pageCount - 1)}
                        disabled={!canNextPage}
                    >
                        {">>"}
                    </Button> */}
                </div>
                {/* <Col md={3}>
                    <Button color="primary" onClick={nextPage} disabled={!canNextPage}>
                        {">"}
                    </Button>
                    <Button
                        color="primary"
                        onClick={() => gotoPage(pageCount - 1)}
                        disabled={!canNextPage}
                    >
                        {">>"}
                    </Button>
                </Col> */}
            </Row>
        </Fragment>
        
    )
}

export default ProductTable
