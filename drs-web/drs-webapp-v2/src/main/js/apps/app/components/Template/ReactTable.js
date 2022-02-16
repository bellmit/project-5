import React, { useEffect, useMemo, useState } from 'react'
import TableContainer from './TableContainer'

import { SelectColumnFilter } from './filters';
import { Container, Card, CardImg, CardText, CardBody, CardTitle, Table } from 'reactstrap'
import expandIcon from '../../assets/images/chevron-left-icon@2x.png'
import expandDownIcon from '../../assets/images/chevron-down-icon-blue@2x.png'


const renderRowSubComponent = row => {
    const {
        name: { first, last },
        location: { city, street, postcode },
        picture,
        cell,
    } = row.original
    return (
        <div className="table-expand-section">
            <div className="table-expand-column-wrapper">
                <div className="table-expand-column">
                    <div className="table-expand-info">
                        <p className="table-expand-title">Title A</p>
                        <p className="table-expand-description">some descripiton</p>
                    </div>
                    <div className="table-expand-info">
                        <p className="table-expand-title">Title B</p>
                        <p className="table-expand-description">a lot of descripiton</p>
                    </div>
                </div>
                <div className="table-expand-column">
                    <div className="table-expand-info">
                        <p className="table-expand-title">Title C</p>
                        <p className="table-expand-description">short descripiton</p>
                    </div>
                    <div className="table-expand-info">
                        <p className="table-expand-title">Title D</p>
                        <p className="table-expand-description">long long long descripiton</p>
                    </div>
                </div>
                <div className="table-expand-column">
                    <div className="table-expand-info">
                        <p className="table-expand-title">Title E</p>
                        <p className="table-expand-description">some descripiton</p>
                    </div>
                    <div className="table-expand-info">
                        <p className="table-expand-title">Title F</p>
                        <p className="table-expand-description">long long long descripiton</p>
                    </div>
                </div>
                <div className="table-expand-column">
                    <div className="table-expand-info">
                        <p className="table-expand-title">Title G</p>
                        <p className="table-expand-description">some descripiton</p>
                    </div>
                    <div className="table-expand-info">
                        <p className="table-expand-title">Title L</p>
                        <p className="table-expand-description">long long long descripiton</p>
                    </div>
                </div>
                
            </div>
            <div className="sub-table-title-wrapper">
                <p className="sub-table-title">Title Of Table</p>
            </div>
            <Table className="sub-table-main">
                
                <thead className="sub-table-head">
                    <tr>
                        <th>column head</th>
                        <th>column head</th>
                        <th>column head</th>
                        <th>column head</th>
                    </tr>
                </thead>
                <tbody className="table-tbody">
                    <tr>
                        <td>body contents</td>
                        <td>body contents</td>
                        <td>body contents</td>
                        <td>body contents</td>
                    </tr>
                    <tr>
                        <td>body contents</td>
                        <td>body contents</td>
                        <td>body contents</td>
                        <td>body contents</td>
                    </tr>
                </tbody>
            </Table>
            
        </div>
        // <Card style={{ width: "18rem", margin: "0 auto" }}>
        //     {/*<CardImg top src={} alt="Card image cap" />*/}
        //     <CardBody>
        //         <CardTitle>
        //             <strong>{`${first} ${last}`} </strong>
        //         </CardTitle>
        //         <CardText>
        //             <strong>Phone</strong>: {cell} <br />
        //             <strong>Address:</strong> {`${street.name} ${street.number} - ${postcode} - ${city}`}
        //         </CardText>
        //     </CardBody>
        // </Card>
    )
}

const ReactTable = () => {
    const [data, setData] = useState([])
    const columns = useMemo(() => [
        {
            Header: () => null,
            id: 'expander',
            Cell: ({ row }) => (
                <span {...row.getToggleRowExpandedProps()}>
                    {/* {row.isExpanded ? '👇' : '👉'} */}
                    {row.isExpanded ? <img src={expandDownIcon} alt="expandIcon"/> : <img src={expandIcon} alt="expandIcon"/>}
                </span>
            )
        },
        {
            Header: 'Title',
            accessor: 'name.title',
            Filter: SelectColumnFilter,
            filter: 'equals'// by default, filter: 'text', but in our case we don't want to filter options like text, we want to find exact match of selected option.
        },
        {
            Header: 'First Name',
            accessor: 'name.first',
            Filter: SelectColumnFilter,
            filter: 'equals'
        },
        {
            Header: 'Last Name',
            accessor: 'name.last',
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,
        },
        {
            Header: 'Email',
            accessor: 'email',
            Filter: SelectColumnFilter,
            filter: 'equals',
            disableFilters: true,

        },
        {
            Header: 'City',
            accessor: 'location.city',
            Filter: SelectColumnFilter,
            filter: 'equals'
        },
    ],
    []
    )
    useEffect(() => {
        const dofetch = async () => {
            const res = await fetch("https://randomuser.me/api/?results=100")
            const body = await res.json()
            const contacts = body.results
            console.log(contacts)
            setData(contacts)
        }
        dofetch()
    }, [])

    return (
        <div className="table-container">   
            <TableContainer
                title='Title of table'
                columns={columns}
                data={data}
                renderRowSubComponent={renderRowSubComponent}
            />
        </div>
    )
}

export default ReactTable
