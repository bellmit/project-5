import React, { useState, Fragment, useEffect, useMemo } from 'react'
import { useTable, usePagination, useSortBy, useFilters } from 'react-table';
import { Link} from 'react-router-dom';
import { useDispatch, useSelector} from 'react-redux';
import { changeAp } from '../../../actions';
import { Table, Row, Button, Input } from 'reactstrap';
import { InlineFilter,SelectColumnFilter, DefaultColumnFilter } from '../common/Filters';
import ApplicationTable from './ApplicationTable';
import {FormattedMessage} from 'react-intl';

const ApplicationFilter = ({  data , fetchData , pageCount: controlledPageCount}) => {

    const dispatch = useDispatch();
    const p2m = useSelector(state => state.P2M)

    const columns = useMemo(() => [
        // {
        //     Header: () => null,
        //     id: 'expander',
        //     Cell: ({ row }) => (
        //         <span {...row.getToggleRowExpandedProps()}>
        //             {row.isExpanded ? <img src={`/${expandDownIcon}`} alt="expandIcon"/> : <img src={`/${expandIcon}`} alt="expandIcon"/>}
        //         </span>
        //     )
        // },
        {
            Header:
            <FormattedMessage id="p2m.name"/>,
            accessor: 'name',
            maxWidth: 150,
            minWidth: 100,
            width: 100,
            Filter: SelectColumnFilter,
            filter: 'text'// by default, filter: 'text', but in our case we don't want to filter options like text, we want to find exact match of selected option.
        },
        {
            Header:
            <FormattedMessage id="p2m.selectedProduct"/>,
            accessor: 'selectedProduct',
            maxWidth: 150,
            minWidth: 120,
            width: 120,
            Filter: SelectColumnFilter,
            filter: 'equals',// by default, filter: 'text', but in our case we don't want to filter options like text, we want to find exact match of selected option.
            disableFilters: true,
        },
        {
            Header:
            <FormattedMessage id="p2m.type"/>,
            accessor: 'type',
            maxWidth: 60,
            minWidth: 60,
            width: 60,
            Filter: SelectColumnFilter,
            filter: 'equals',// by default, filter: 'text', but in our case we don't want to filter options like text, we want to find exact match of selected option.
            disableSortBy: true,
            disableFilters: true,
        },
        {
            Header:
            <FormattedMessage id="p2m.appliedDate"/>,
            accessor: 'appliedDate',
            maxWidth: 100,
            minWidth: 90,
            width: 90,
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
        },
        {
            Header:
            <FormattedMessage id="p2m.approvedDate"/>,
            accessor: 'approvedDate',
            maxWidth: 100,
            minWidth: 90,
            width: 90,
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
        },
        {
            Header:
            <FormattedMessage id="p2m.rejectedDate"/>,
            accessor: 'rejectedDate',
            maxWidth: 100,
            minWidth: 90,
            width: 90,
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
        },
        // {
        //     Header:
        //     <FormattedMessage id="p2m.cancelledDate"/>,
        //     accessor: 'cancelledDate',
        //     maxWidth: 100,
        //     minWidth: 90,
        //     width: 90,
        //     Filter: SelectColumnFilter,
        //     filter: 'equals',
        //     disableFilters: true,
        // },
        {
            Header:
            <FormattedMessage id="p2m.appliedSkuCode"/>,
            accessor: 'appliedSkuCode',
            maxWidth: 150,
            minWidth: 120,
            width: 120,
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
            disableSortBy: true
        },
        {
            Header:
            <FormattedMessage id="p2m.country"/>,
            accessor: 'selectedCountry',
            maxWidth: 70,
            minWidth: 70,
            width: 70,
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableSortBy: true
            // disableFilters: true,
        },
        {
            Header:
            <FormattedMessage id="p2m.platform"/>,
            accessor: 'selectedPlatform',
            maxWidth: 80,
            minWidth: 80,
            width: 80,
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
            disableSortBy: true
        },
        {
            Header:
            <FormattedMessage id="p2m.status"/>,
            accessor: 'status',
            maxWidth: 80,
            minWidth: 80,
            width: 80,
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableSortBy: true
        },
        {
            Header:
            <FormattedMessage id="p2m.action"/>,
            accessor: 'action',
            maxWidth: 60,
            minWidth: 60,
            width: 60,
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
            disableSortBy: true
        },
    ], [] )

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        prepareRow,
        page,
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
            initialState: { pageIndex: p2m.pageIndex },
            manualPagination : true,
            pageCount : controlledPageCount,
            autoResetPage:false,
        },
        useFilters,
        useSortBy,
        usePagination,
    );

    const onChangeInInput = event => {
        const page = event.target.value ? Number(event.target.value) - 1 : 0
        gotoPage(page)
    }
    // const generateSortingIndicator = column => {
    //     if (column.disableSortBy) {
    //         return <div></div>
    //     }
    //     return column.isSorted 
    //         ? (column.isSortedDesc
    //             ? <i class="fa fa-sort-desc" style={{marginLeft: '4px'}} aria-hidden="true"></i>
    //             : <i class="fa fa-sort-asc" style={{marginLeft: '4px'}} aria-hidden="true"></i>)
    //         : <i class="fa fa-sort" style={{marginLeft: '4px'}} aria-hidden="true"></i>
    // }

    // useEffect(() => {
    //      fetchData({ pageIndex, pageSize })
    // //}, [onFetchData, pageIndex, pageSize, sortBy, filters])
    // }, [fetchData, pageIndex , pageSize])
    
    // const [p2mLength, setP2MLength] = useState('Loading')

    // useEffect(() => {
    //     setP2MLength(data.length)
    // },[data])

    useEffect(() => {
        fetchData({ pageIndex, pageSize })
   //}, [onFetchData, pageIndex, pageSize, sortBy, filters])
   }, [fetchData, pageIndex , pageSize])

    return (

        <Fragment>
            <div className="table-title-section">
                <span className="table-title" style={{minWidth: '12%'}}><FormattedMessage id="p2m.application"/></span>
                <div className='table-action-container'>
                    <Table className="table-filter-section table-inline-filter" {...getTableProps()}>
                        {headerGroups.map(headerGroup => (
                            <tr className="table-filter-row" {...headerGroup.getHeaderGroupProps()}>
                                
                                {headerGroup.headers.map(column => {
                                    // console.log(column, 'column')
                                    return (
                                        <th id="table-filter-th" colspan="3" {...column.getHeaderProps()}>
                                            <InlineFilter column={column}/>
                                        </th>
                                    )
                                })}
                                
                            </tr>
                        ))}
                    </Table>
                </div>
            </div>
            <ApplicationTable
                columns = {columns}
                data = {p2m.applications}
                fetchData = {fetchData}
                pageCount = {pageCount}
                pageIndex={p2m.pageIndex}
                pageSize={pageSize}
            />
            
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
                    <Button onClick={() => nextPage()} disabled={!canNextPage}>
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

export default ApplicationFilter
