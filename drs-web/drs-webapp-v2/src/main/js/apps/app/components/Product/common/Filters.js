import React from 'react';
import {useDispatch, useSelector} from 'react-redux';
import { Input, CustomInput } from 'reactstrap'
import { loadApplications,changeFilterTarget, getExactApplication, handleFilterP2MAp } from '../../../actions'
import chevronDown from '../../../assets/images/chevron-down@2x.png'

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
              <img className="icon" src={`/${chevronDown}`} alt="chevronDown"/>
            </div>}
        </div>
    )
}

export const InlineFilter = ({ column }) => {
  return (
    <div className="selector-inline-wrapper" style={{marginTop: 0 }}>
      { column.canFilter && 
        <div className="selector-inline-head">
          <span className="selector-inline-title">{column.render("Header")}</span>
        </div>
      }
      { column.canFilter && column.render('Filter')}
      { column.canFilter &&
        <div className="selector-icon-wrapper">
          <img className="icon" src={`/${chevronDown}`} alt="chevronDown"/>
        </div>
      }
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
    column: { filterValue, setFilter, preFilteredRows, id},
  }) => {

    const dispatch = useDispatch()
    const p2m = useSelector(state => state.P2M)
    const d = useSelector(state => state.d)

    const options = React.useMemo(() => {
      const options = new Set()
      preFilteredRows.forEach(row => {
        options.add(row.values[id])
      })
      return [...options.values()]
    }, [id, preFilteredRows])
    // console.log(options)
  
    return (
      <CustomInput
        id="custom-select"
        type="select"
        value={id == 'name' ? p2m.filterTarget.name : id == 'selectedCountry' ? p2m.filterTarget.country : id == 'status' ? p2m.filterTarget.status : filterValue}
        onChange={e => {
          if (id == 'name') {
            const filterTarget = p2m.filterTarget;
            filterTarget.name = e.target.value;
            dispatch(changeFilterTarget(filterTarget))

            const filterValue = e.target.value;
            const isFilter = true
            const target = p2m.filterList.filter(item => item.name == filterValue)
            const result = p2m.applications.filter(item => item.name == filterValue)

            if (filterValue == '') {
              const supplierId = d.u.cid;
              dispatch(loadApplications(supplierId, 0, filterTarget.country, filterTarget.status, filterTarget.kcode, filterTarget.product))
            } else if (result.length == 0 && target.length !== 0) {
              dispatch(getExactApplication(target[0]._id, isFilter))
            } else {
              dispatch(handleFilterP2MAp(result))
            }
            setFilter(e.target.value || undefined)

          } else if (id == 'sellerSku'){
            setFilter(e.target.value || undefined)
          } else if (id == 'status') {
            const filterTarget = p2m.filterTarget;
            filterTarget.status = e.target.value;
            const isFilter = true
            const target = p2m.filterList.filter(item => item.name == filterTarget.name)

            if (filterTarget.name == '') {
              const supplierId = d.u.cid;
              dispatch(loadApplications(supplierId, 0, filterTarget.country, filterTarget.status, filterTarget.kcode, filterTarget.product))
            } else {
              const isFilter = true;
              dispatch(getExactApplication(target[0]._id, isFilter))
            }
            dispatch(changeFilterTarget(filterTarget))
            
          } else if (id == 'selectedCountry') {
            const filterTarget = p2m.filterTarget;
            filterTarget.country = e.target.value;
            const isFilter = true
            const target = p2m.filterList.filter(item => item.name == filterTarget.name)

            if (filterTarget.name == '') {
              const supplierId = d.u.cid;
              dispatch(loadApplications(supplierId, 0, filterTarget.country, filterTarget.status, filterTarget.kcode, filterTarget.product))
            } else {
              const isFilter = true;
              dispatch(getExactApplication(target[0]._id, isFilter))
            }
            dispatch(changeFilterTarget(filterTarget))
          }
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

  export const SelectFilter = ({
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
          <option value="Amazon.com">Amazon.com</option>
          <option value="Amazon.co.uk">Amazon.co.uk</option>
          <option value="Amazon.ca">Amazon.ca</option>
          <option value="Amazon.de">Amazon.de</option>
          <option value="Amazon.fr">Amazon.fr</option>
          <option value="Amazon.it">Amazon.it</option>
          <option value="Amazon.es">Amazon.es</option>
          <option value="TrueToSource">TrueToSource</option>
        </CustomInput>
      )
    }