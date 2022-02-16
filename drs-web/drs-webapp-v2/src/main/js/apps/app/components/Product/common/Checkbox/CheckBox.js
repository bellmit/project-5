import React,{useEffect, useState} from 'react';
import IconCheck from './IconCheck'
import IconUnchecked from './IconUnchecked'

const CheckBox = ({isSelected,onSelect,index, disabled}) => {

  useEffect(() => {
      setChecked(isSelected)
    }, [isSelected])

  const [checked, setChecked] = useState(isSelected)

  // const icon = () => {
  //   return checked ? <IconCheck /> : <IconUnchecked />
  // }

  const onToggleChange = () => {
    // console.log('toggle')
    if (checked) {
        // this.setState({ checked: false })
        setChecked(false)
    } else {
        // this.setState({ checked: true })
        setChecked(true)
    }
    onSelect(index)
  }

  return (
    <button
      style={ disabled ? Styles.disabledButton: Styles.button }
      onClick={() => onToggleChange()}
      disabled={disabled}
    >
      <div style={ Styles.check }>
              {/* {icon()} */}
              {checked ?<IconCheck /> : <IconUnchecked /> }
       </div>

      <div style={ Styles.content }>
        {/* { this.props.children } */}
      </div>
    </button>
  )

}

const Styles = {
  disabledButton: {
    background: 'transparent',
    border: '0',
    // marginBottom: '0.5rem',
    fontSize: '1rem',
    display: 'flex',
    outline: '0',
    color: '#9B9B9B',
    // marginRight: '0.5rem',
    cursor: 'not-allowed',
    textAlign: 'left'
  },
  button: {
    background: 'transparent',
    border: '0',
    // marginBottom: '0.5rem',
    fontSize: '1rem',
    display: 'flex',
    outline: '0',
    color: '#9B9B9B',
    // marginRight: '0.5rem',
    cursor: 'pointer',
    textAlign: 'left'
  },

  check: {
    // marginRight: '1rem'
    width: '21px',
    height: '21px',
    textAlign: 'center'
  },

  content: {
    paddingTop: '0.2rem',
    fontSize: '0.9rem',
    fontWeight: '100',
    lineHeight: '1.25rem'
  }
}

export default CheckBox
//import React from 'react';
//import IconCheck from './IconCheck'
//import IconUnchecked from './IconUnchecked'
//
//export default class Button extends React.Component {
//  constructor(props) {
//    super(props)
//    this.state = {
//      checked: this.props.isSelected
//    }
//  }
//
//  icon = () => {
//    return  this.state.checked ? <IconCheck /> : <IconUnchecked />
//  }
//
//  onToggleChange = () => {
//    // console.log('toggle')
//    if (this.state.checked) {
//        this.setState({ checked: false })
//    } else {
//        this.setState({ checked: true })
//    }
//    this.props.onSelect(this.props.index)
//  }
//
//  render() {
//    return (
//      <button
//        style={ this.props.disabled ? Styles.disabledButton: Styles.button }
//        onClick={() => this.onToggleChange()}
//        disabled={this.props.disabled}
//      >
//        <div style={ Styles.check }>
//          {this.icon()}
//        </div>
//
//        <div style={ Styles.content }>
//          {/* { this.props.children } */}
//        </div>
//      </button>
//    )
//  }
//}
//
//const Styles = {
//  disabledButton: {
//    background: 'transparent',
//    border: '0',
//    // marginBottom: '0.5rem',
//    fontSize: '1rem',
//    display: 'flex',
//    outline: '0',
//    color: '#9B9B9B',
//    // marginRight: '0.5rem',
//    cursor: 'not-allowed',
//    textAlign: 'left'
//  },
//  button: {
//    background: 'transparent',
//    border: '0',
//    // marginBottom: '0.5rem',
//    fontSize: '1rem',
//    display: 'flex',
//    outline: '0',
//    color: '#9B9B9B',
//    // marginRight: '0.5rem',
//    cursor: 'pointer',
//    textAlign: 'left'
//  },
//
//  check: {
//    // marginRight: '1rem'
//    width: '21px',
//    height: '21px',
//    textAlign: 'center'
//  },
//
//  content: {
//    paddingTop: '0.2rem',
//    fontSize: '0.9rem',
//    fontWeight: '100',
//    lineHeight: '1.25rem'
//  }
//}


// import '../sass/checkbox.scss';

// const Checkbox = ({ label, isSelected, onCheckboxChange }) => (
//     <div className="drs-form-check">
//         {/* <label class="form-check-label" for={this.props.id}>
//             <input
//                 class="form-check-input2"
//                 type="checkbox"
//                 name="optionCheckboxes"
//                 value={this.props.value}
//                 checked = {this.props.docChecked}
//                 id={this.props.id}
//                 onClick={()=>this.checkCheckboxStatus()}
//                 ref={this.inputRef}
//                 disabled
//             />
//             <span class="form-check-sign"></span>
//         </label> */}
//         <label className="drs-form-check-label">
//             <input
//                 type="checkbox"
//                 name={label}
//                 checked={isSelected}
//                 onChange={onCheckboxChange}
//                 className="drs-form-check-input"
//             />
//             <span className="drs-form-check-sign"></span>
//             {label}
//         </label>
//     </div>
// )

// export default Checkbox;