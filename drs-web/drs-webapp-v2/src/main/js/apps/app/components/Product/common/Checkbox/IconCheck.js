import React from 'react'

export default class IconCheck extends React.Component {

  render() {
    return (
      // <svg style={Styles.svg} viewBox="0 0 22 16">
      //   <g stroke="none" strokeWidth="1" fill="none" fillRule="evenodd">
      //     <g transform="translate(-1.000000, -4.000000)" fill="#06C595">
      //       <polygon points="20.6465116 4 8.7255814 15.5061728 3.35348837 10.3209877 1 12.5925926 7.54883721 18.9135802 8.7255814 20 9.90232558 18.9135802 23 6.27160494" />
      //     </g>
      //   </g>
      // </svg>
      <svg style={Styles.svg} viewBox="0 0 24 24">
        <g stroke="none" strokeWidth="1" fill="none" fillRule="evenodd">
          <g stroke="#D8D8D8" strokeWidth="1">
            <rect id="Rectangle" x="1.5" y="1.5" width="21" height="21" rx="3" />
            <svg style={Style.svg} viewBox="0 0 16 21">
              <g transform="translate(-1.000000, -4.000000)" fill="#06C595">
                <polygon points="20.6465116 4 8.7255814 15.5061728 3.35348837 10.3209877 1 12.5925926 7.54883721 18.9135802 8.7255814 20 9.90232558 18.9135802 23 6.27160494" />
              </g>
            </svg>
          </g>
        </g>
      </svg>

    )
  }
}

const Style = {
  svg: {
    width: '18px',
    height: '18px',
    padding: '4px'
  }
}

const Styles = {
  svg: {
    width: '24px',
    height: '24px'
  }
}

// const Styles = {
//   svg: {
//     width: '18px',
//     height: '18px',
//     padding: '4px'
//   }
// }