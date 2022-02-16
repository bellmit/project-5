import React, { Component } from 'react';
import ReactDOM from 'react-dom';

class DocStatus extends Component {
    constructor(props) {
        super(props);

        this.checkCheckboxStatus = this.checkCheckboxStatus.bind(this);
        this.inputRef = React.createRef();
    }

    checkCheckboxStatus () {
        // console.log(this.inputRef.current.checked);
        const obj = {
            id: this.props.id,
            isChecked: this.inputRef.current.checked
        };
        const index = this.props.value - 1;
        this.props.changeCheckboxState(obj, index);
    }

    componentDidMount() {
        // console.log(this.props.isLogistics);
    }

    render() {
        return (
            <div className="doc-status-wrapper">
                { 
                <span className="doc-status-withoutlink"></span>
                // this.props.hasDoc === false
                //     ?  <span className="doc-status-withoutlink"> Not Required</span>
                //     :  <span className="doc-status-withlink"><a href='' target='_blank'>Document Link </a></span>
                }
                { this.props.isLogistics === false
                    ?   <div></div>
                    :   <div class="form-check form-check-inline">
                            { this.props.vpStatus === 'Confirmed'
                                ? <label class="form-check-label" for={this.props.id}>
                                    <input
                                        class="form-check-input2"
                                        type="checkbox" 
                                        name="optionCheckboxes"
                                        value={this.props.value}
                                        checked = {this.props.docChecked}
                                        id={this.props.id}
                                        onClick={()=>this.checkCheckboxStatus()}
                                        ref={this.inputRef}
                                        disabled
                                    />
                                    <span class="form-check-sign"></span>
                                </label>
                                : <label class="form-check-label" for={this.props.id}>
                                    <input
                                        class="form-check-input2"
                                        type="checkbox"
                                        name="optionCheckboxes"
                                        value={this.props.value}
                                        checked = {this.props.docChecked}
                                        id={this.props.id}
                                        onClick={()=>this.checkCheckboxStatus()}
                                        ref={this.inputRef}
                                    />
                                    <span class="form-check-sign"></span>
                                </label>
                            }
                        </div>
                }
            </div>
        )
    }
}

export default DocStatus;