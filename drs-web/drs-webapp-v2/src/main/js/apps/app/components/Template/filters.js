import React from 'react'
import { Input, CustomInput } from 'reactstrap'
import chevronDown from '../../assets/images/chevron-down@2x.png';

export const Filter = ({ column }) => {
    return (
        <div className="selector-wrapper" style={{marginTop: 5 }}>
           {column.canFilter && 
            <div className="selector-head">
              <span className="selector-title">{column.render("Header")}</span>
            </div>
            }
            {column.canFilter && column.render('Filter')}
            {column.canFilter && <div className="selector-icon-wrapper">
              <img className="icon" src={chevronDown} alt="chevronDown"/>
            </div>}
        </div>
    )
}

export const DefaultColumnFilter = ({
    column: {
        filterValue,
        setFilter,
        preFilteredRows: { length },
    },
}) => {
    return (
        <Input
            value={filterValue || ""}
            onChange={e => { 
                setFilter(e.target.value || undefined)
            }}
            placeholder = {`search ($(length))...`}
        />
    )
}

export const SelectColumnFilter = ({
    column: { filterValue, setFilter, preFilteredRows, id },
  }) => {
    const options = React.useMemo(() => {
      const options = new Set()
      preFilteredRows.forEach(row => {
        options.add(row.values[id])
      })
      return [...options.values()]
    }, [id, preFilteredRows])
  
    return (
      <CustomInput
        id="custom-select"
        type="select"
        value={filterValue}
        onChange={e => {
          setFilter(e.target.value || undefined)
        }}
      >
        <option value="">All</option>
        {options.map(option => (
          <option key={option} value={option}>
            {option}
          </option>
        ))}
      </CustomInput>
    )
  }