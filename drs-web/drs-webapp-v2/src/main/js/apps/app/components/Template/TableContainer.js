import React, { useState, Fragment } from 'react'
import { useTable, usePagination, useSortBy, useFilters, useExpanded } from 'react-table'
import { Table, Row, Col, Button, Input, CustomInput } from 'reactstrap';
import { Filter, DefaultColumnFilter } from './filters';
import { useExportData } from 'react-table-plugins';
import Cards from './Cards';
import Papa from "papaparse";
import XLSX from "xlsx";
import JsPDF from "jspdf";
import "jspdf-autotable";

function getExportFileBlob({ columns, data, fileType, fileName }) {
    if (fileType === "csv") {
      // CSV example
      const headerNames = columns.map((col) => col.exportValue);
      const csvString = Papa.unparse({ fields: headerNames, data });
      return new Blob([csvString], { type: "text/csv" });
    } else if (fileType === "xlsx") {
      // XLSX example
  
      const header = columns.map((c) => c.exportValue);
      const compatibleData = data.map((row) => {
        const obj = {};
        header.forEach((col, index) => {
          obj[col] = row[index];
        });
        return obj;
      });
  
      let wb = XLSX.utils.book_new();
      let ws1 = XLSX.utils.json_to_sheet(compatibleData, {
        header,
      });
      XLSX.utils.book_append_sheet(wb, ws1, "React Table Data");
      XLSX.writeFile(wb, `${fileName}.xlsx`);
  
      // Returning false as downloading of file is already taken care of
      return false;
    }
    //PDF example
    if (fileType === "pdf") {
      const headerNames = columns.map((column) => column.exportValue);
      const doc = new JsPDF();
      doc.autoTable({
        head: [headerNames],
        body: data,
        margin: { top: 20 },
        styles: {
          minCellHeight: 9,
          halign: "left",
          valign: "center",
          fontSize: 11,
        },
      });
      doc.save(`${fileName}.pdf`);
  
      return false;
    }
  
    // Other formats goes here
    return false;
}

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

const TableContainer = ({ title, columns, data, renderRowSubComponent}) => {
    const [showColumns, setShowColumns] = useState({show: false, style: 'hidden'});
    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        page,
        prepareRow,
        exportData,
        // below props related to 'hide cells'
        allColumns,
        getToggleHideAllColumnsProps,
        // below props related to 'usePagination' hook
        canPreviousPage,
        canNextPage,
        pageOptions,
        pageCount,
        gotoPage,
        nextPage,
        previousPage,
        setPageSize,
        state: { pageIndex, pageSize },
    } = useTable(
        { 
            columns,
            data,
            defaultColumn: { Filter: DefaultColumnFilter },
            initialState: { pageIndex: 0, pageSize: 10 },
            getExportFileBlob,
        },
        useFilters,
        useSortBy,
        useExpanded,
        usePagination,
        useExportData
    );
    
    const onChangeInSelect = event => {
        setPageSize(Number(event.target.value))
    }

    const onChangeInInput = event => {
        const page = event.target.value ? Number(event.target.value) - 1 : 0
        gotoPage(page)
    }
    const generateSortingIndicator = column => {
        return column.isSorted ? (column.isSortedDesc ? " 🔽" : " 🔼") : ""
    }
    
    return (
        <Fragment>
            <Table className="table-filter-section" {...getTableProps()}>
                {headerGroups.map(headerGroup => (
                    <tr className="table-filter-row" {...headerGroup.getHeaderGroupProps()}>
                        
                        {headerGroup.headers.map(column => (
                            <th id="table-filter-th" colspan="3" {...column.getHeaderProps()}>
                                <Filter column={column}/>
                            </th>
                        ))}
                        
                    </tr>
                ))}
            </Table>
            <Cards
                columns={columns}
                data={data}
                rows={rows}
            />
            <div className="table-title-section">
                <span className="table-title">{title}</span>
                <div className="export-btn-group">            
                    <button
                        className="table-btn"
                        onClick={() => {
                            exportData("csv", true);
                        }}
                    >
                        Export All as CSV
                    </button>
                    <button
                        className="table-btn"
                        onClick={() => {
                            exportData("csv", false);
                        }}
                    >
                        Export Current View as CSV
                    </button>
                    <button
                        className="table-btn"
                        onClick={() => {
                            showColumns.show == false 
                            ? setShowColumns({show: true, style: 'show'})
                            : setShowColumns({show: false, style: 'hidden'})
                        }}
                    >
                        Show Columns
                    </button>
                    {/*<button
                        className="table-btn"
                        onClick={() => {
                        exportData("xlsx", true);
                        }}
                    >
                        Export All as xlsx
                    </button>
                    <button
                        className="table-btn"
                        onClick={() => {
                        exportData("xlsx", false);
                        }}
                    >
                        Export Current View as xlsx
                    </button>
                    <button
                        className="table-btn"
                        onClick={() => {
                        exportData("pdf", true);
                        }}
                    >
                        Export All as PDF
                    </button>{" "}
                    <button
                        className="table-btn"
                        onClick={() => {
                        exportData("pdf", false);
                        }}
                    >
                        Export Current View as PDF
                    </button>*/}
                </div>
            </div>            
            <div className={`table-checkbox-section ${showColumns.style}`}>
                <div className="table-checkbox-wrapper" >
                    <label className="table-checkbox-label">
                        <IndeterminateCheckbox {...getToggleHideAllColumnsProps()} /> Toggle All
                        <span className="checkmark"></span>
                    </label>
                </div>
                {allColumns.map(column => (
                    <div className="table-checkbox-wrapper" key={column.id}>
                        <label className="table-checkbox-label">
                            <input type="checkbox" {...column.getToggleHiddenProps()} />{' '}
                            {column.id}
                            <span className="checkmark"></span>
                        </label>
                    </div>
                ))}
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
                        <tr {...headerGroup.getHeaderGroupProps()}>

                            {headerGroup.headers.map(column => (

                                <th {...column.getHeaderProps(column.getSortByToggleProps())}>
                                    
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
                        return (
                           
                            <Fragment key={row.getRowProps().key}>
                                <tr className="table-tbody-tr">
                                    {row.cells.map((cell,index) => {
                                        return <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                                    })}
                                </tr>
                                {row.isExpanded && (
                                    <tr>
                                        <td colSpan="8">{renderRowSubComponent(row)}</td>
                                    </tr>
                                )}
                            </Fragment>
                        )
                    })}

                </tbody>
            </Table>
            
            <Row className="table-pagination-row">
                <Col md={3}>
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
                </Col>
                <Col md={2} style={{ marginTop: 7 }}>
                    Page{" "}
                    <strong>
                        {pageIndex + 1} of {pageOptions.length}
                    </strong>
                </Col>
                <Col md={2}>
                    <Input
                        type="number"
                        min={1}
                        style={{ width: 70 }}
                        max={pageOptions.length}
                        defaultValue={pageIndex + 1}
                        onChange={onChangeInInput}
                    />
                </Col>
                <Col md={2}>
                    <CustomInput type="select" value={pageSize} onChange={onChangeInSelect}>
                        >
                        {[10, 20, 30, 40, 50].map(pageSize => (
                        <option key={pageSize} value={pageSize}>
                            Show {pageSize}
                        </option>
                        ))}
                    </CustomInput>
                </Col>
                <Col md={3}>
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
                </Col>
            </Row>
        </Fragment>
        
    )
}

export default TableContainer
