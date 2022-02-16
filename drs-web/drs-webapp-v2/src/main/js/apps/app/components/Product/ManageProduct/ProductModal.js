import React, { Component } from 'react';
import AddNewProduct from './AddNewProduct';
import AddNewSku from './AddNewSku';
import UpdateProduct from './UpdateProduct';

class ProductModal extends Component {
    constructor(props) {
        super();
        this.state={}
    }
    // componentDidMount() {
    //     console.log('SHOW MODAL', this.props.show)
    // }
    render () {
        if ( !this.props.show) {
            return null;
        }
        return (
            <div id="modal" className="drs-modal">
                <div className="drs-modal-content">
                    {this.props.modal == 'addNewProduct'
                        ?   <AddNewProduct
                                onClose={this.props.onClose}
                                bpId={this.props.bpId}
                            />
                        :   <div></div>
                    }
                    {this.props.modal == 'addNewSku'
                        ?   <AddNewSku
                                onClose={this.props.onClose}
                                bpId={this.props.bpId}
                            />
                        :   <div></div>
                    }
                    {this.props.modal == 'updateProduct'
                        ?   <UpdateProduct
                                onClose={this.props.onClose}
                                bpId={this.props.bpId}
                            />
                        :   <div></div>
                    }
                </div>
            </div>
        )
    }
}

export default ProductModal
