import React, { Component } from 'react';
import ReactDOM from 'react-dom'
import axios from 'axios';
import Select from 'react-select'


class ProductDashboard extends Component {
	
	constructor() {
		super();
    
		this.state = {
      supplierOptions: [],
      selectedSupplier: "",
      mpOptions: [],
      selectedMp: "",
      bpOptions: [],
      selectedBp: "",
      skuOptions: [],
      selectedSku: "",
      salesQty: "",
      salesRev: "",
      amzReviews: [],
      returns: [],
      campaignSpend: "",
      acos: "",
      baseUrl : ""
    };
    

    this.updateSupplier = this.updateSupplier.bind(this);
    this.updateMp = this.updateMp.bind(this);
    this.updateBp = this.updateBp.bind(this);
    this.updateSku = this.updateSku.bind(this);
    this.submitGo = this.submitGo.bind(this);
    this.clean = this.clean.bind(this);

  }
  
   
	initLoad(){
	
      axios.post(this.state.baseUrl + '/c/sup/g')
	        .then(res => {
	            
	      let supplierList = []
              supplierList.push({value: "All", label: "All"})
				res.data.map(item => {
          let supplierOption = {value: item, label: item}
					supplierList.push(supplierOption)
				})
				
				this.setState({
					supplierOptions: supplierList
				});
        })

    
        axios.post(this.state.baseUrl + '/c/mkpl/g')
	        .then(res => {
	            
              let mpList = []
              mpList.push({value: "All", label: "All"})
				res.data.map(item => {
          let mpOption = {value: item.first, label: item.second}
					mpList.push(mpOption)
        })
        
        
				this.setState({
					mpOptions: mpList
        });
        
        
        })
	
    }


  
    updateSupplier(selectedSupplier){

      this.setState({ selectedSupplier });
       // console.log("Option selected:", selectedSupplier);

        this.updateBp(null)
        this.updateSku(null)
        
      
        axios.post(this.state.baseUrl + '/c/bp/g' , {
          kcode: selectedSupplier.value
        }).then(res => {
                
                let bpList = []
    
              res.data.map(item => {
                let bpOption = {value: item.productBaseCode, label: item.productBaseCode  + " " +item.name}
                bpList.push(bpOption)
              })
              
              this.setState({
                  bpOptions: bpList
              });
          })

    }

    updateMp(selectedMp){

      this.setState({ selectedMp });
  
    }

    updateBp(selectedBp){
      this.setState({ selectedBp });
      //  console.log("Option selected:", selectedBp);

        if(selectedBp != null){
          this.updateSku(null)
      
            axios.post(this.state.baseUrl + '/c/sk/g' ,{
              bpCode: selectedBp.value
            })
            .then(res => {
                
                let skuList = []
      
              res.data.map(item => {
                let skuOption = {value: item.skuCode, label: item.skuCode  + " " +item.name}
                skuList.push(skuOption)
              })
              
              this.setState({
                  skuOptions: skuList
              });
            })
        }
  

    }

    updateSku(selectedSku){
      this.setState({ selectedSku });
    }


    clean(){
      this.setState({
        selectedSupplier:"",
        selectedMp:"",
        selectedSku:"",
        selectedBp:"",
        salesQty: "",
        salesRev: "",
        campaignSpend: "",
        acos: "",
        amzReviews : [],
        returns : []
      });
    }

    submitGo(){
		 
			
      //get marketplace list

      if(this.state.selectedSupplier != null){
        var kcode , mp , bpcode , skucode ;

        kcode = this.state.selectedSupplier.value
        if(this.state.selectedMp != null)   mp =  this.state.selectedMp.value
        if(this.state.selectedBp != null)   bpcode =  this.state.selectedBp.value
        if(this.state.selectedSku != null)  skucode = this.state.selectedSku.value

  
        this.getSalesQtyRev(kcode,mp ,bpcode,skucode)

        this.getCampaignSpendAcos(kcode,mp,bpcode,skucode)
        this.getAmazonReviews(kcode,mp ,bpcode,skucode)
        this.getReturns(kcode,mp , bpcode,skucode)
       
      
      }
      
    }

    getSalesQtyRev( kcode , mp , bpcode , skucode){


      axios.post(this.state.baseUrl + "/pd/gdsqr", {
        kCode:kcode, mp:mp, bpCode:bpcode, skuCode:skucode
      })
          .then(res => {
            
            this.setState({
              salesQty: res.data.qty,
              salesRev: "$ " + res.data.rev
          });
      })
    }

    getCampaignSpendAcos( kcode , mp , bpcode , skucode){

      axios.post(this.state.baseUrl+"/pd/gca", {
        kCode:kcode, mp:mp, bpCode:bpcode, skuCode:skucode
      })
          .then(res => {
            this.setState({
              campaignSpend: "$ " + res.data.totalSpend,
              acos: res.data.acos+"%"
          });
      })
    }

    getAmazonReviews(kcode , mp , bpcode , skucode){
      axios.post(this.state.baseUrl + "/pd/gapr", {
        kCode:kcode, mp:mp , bpCode:bpcode, skuCode:skucode
      }).then(res => {
           
            this.setState({
              amzReviews: res.data
             });
        })
    }

    getReturns(kcode , mp , bpcode , skucode){
      axios.post(this.state.baseUrl + "/pd/gre", {
        kCode:kcode,  mp:mp , bpCode:bpcode, skuCode:skucode
      }).then(res => {
          
            this.setState({
              returns: res.data
             });

        })
    }
	
  
  componentDidMount() {
    this.initLoad();
	}
	
 
 
	render() {
	
		return (
		   
	    <div>

    <div class="row">
                    <div class="col-sm-12">
                      <div class="row">                        
                        <div class="col-md-2">
                        <br></br>
                        <div class="form-group">
                        <Select
                              options={this.state.supplierOptions}
                              onChange={this.updateSupplier}
                              value={this.state.selectedSupplier}
                              placeholder="Supplier"
                            />
                             </div>
                        </div>

                        <div class="col-md-2">
                        <br></br>
                        <div class="form-group">
                        <Select
                              options={this.state.mpOptions}
                              onChange={this.updateMp}
                              value={this.state.selectedMp}
                              placeholder="Marketplace" 
                            />
                             </div>
                        </div>
                        <div class="col-md-3">
                        <br></br>
                          <div class="form-group">
                          <Select
                                options={this.state.bpOptions}
                                onChange={this.updateBp}
                                value={this.state.selectedBp}
                                placeholder="Product Base Code" 
                              />
                          </div>
                        </div>
                        <div class="col-md-4">
                          <br></br>
                          <div class="form-group">
                          <Select
                                options={this.state.skuOptions}
                                onChange={this.updateSku}
                                value={this.state.selectedSku}
                                placeholder="Product SKU Code" 
                              />
                          </div>
                        
                        </div>

                        <button className="btn btn-primary btn-round btn-icon"
                        style={{margin: "20px 2px"}}  onClick={this.submitGo}>
                          <i class="nc-icon nc-button-play"></i>
                        </button>

                      <button className="btn btn-primary btn-round btn-icon"
                        style={{margin: "20px 2px"}}  onClick={this.clean}>
                        <i class="nc-icon nc-simple-remove"></i>
                        </button>
                      </div>
                    </div>
                 
                  </div>
      <p></p>
         <div class="row">
          <div class="col-lg-3 col-md-6 col-sm-6">
            <div class="card card-stats">
              <div class="card-body ">
                <div class="row">
                  <div class="col-5 col-md-4">
                    <div class="icon-big text-center icon-warning">
                      <i class="nc-icon nc-globe text-warning"></i>
                    </div>
                  </div>
                  <div class="col-7 col-md-8">
                    <div class="numbers">
                      <p class="card-category">Sales Qty</p>
                      <p class="card-title">{this.state.salesQty}</p>
                    </div>
                  </div>
                </div>
              </div>
              <div class="card-footer ">
                <hr/>
                <div class="stats">
                  <i class="fa fa-clock-o"></i> Daily
                </div>
              </div>
            </div>
          </div>
          <div class="col-lg-3 col-md-6 col-sm-6">
            <div class="card card-stats">
              <div class="card-body ">
                <div class="row">
                  <div class="col-5 col-md-4">
                    <div class="icon-big text-center icon-warning">
                      <i class="nc-icon nc-money-coins text-success"></i>
                    </div>
                  </div>
                  <div class="col-7 col-md-8">
                    <div class="numbers">
                      <p class="card-category">Revenue</p>
                      <p class="card-title">{this.state.salesRev}</p>
                    </div>
                  </div>
                </div>
              </div>
              <div class="card-footer ">
                <hr/>
                <div class="stats">
                  <i class="fa fa-clock-o"></i> Daily
                </div>
              </div>
            </div>
          </div>
          <div class="col-lg-3 col-md-8 col-sm-6">
            <div class="card card-stats">
              <div class="card-body ">
                <div class="row">
                  <div class="col-5 col-md-4">
                    <div class="icon-big text-center icon-warning">
                      <i class="nc-icon nc-vector text-danger"></i>
                    </div>
                  </div>
                  <div class="col-7 col-md-8">
                    <div class="numbers">
                      <p class="card-category">CP Spend</p>
                      <p class="card-title" style={{fontSize:"0.9em"}}>{this.state.campaignSpend}</p>
                    </div>
                  </div>
                </div>
              </div>
              <div class="card-footer ">
                <hr/>
                <div class="stats">
                  <i class="fa fa-clock-o"></i> MTD
                </div>
              </div>
            </div>
          </div>
          <div class="col-lg-3 col-md-6 col-sm-6">
            <div class="card card-stats">
              <div class="card-body ">
                <div class="row">
                  <div class="col-5 col-md-4">
                    <div class="icon-big text-center icon-warning">
                      <i class="nc-icon nc-favourite-28 text-primary"></i>
                    </div>
                  </div>
                  <div class="col-7 col-md-8">
                    <div class="numbers">
                      <p class="card-category">ACOS</p>
                      <p class="card-title">{this.state.acos}</p>
                    </div>
                  </div>
                </div>
              </div>
              <div class="card-footer ">
                <hr/>
                <div class="stats">
                  <i class="fa fa-clock-o"></i> MTD
                </div>
              </div>
            </div>
          </div>
        </div>

    <p></p>

    <div class="row">
    <div class="col-md-6">
    
            <div class="card">
              <div class="card-header">
                <div class="card-text">
                  <h3 class="card-title">Product Reviews</h3>
                </div>
              </div>
              <div class="card-body table-responsive">
                <table class="table table-hover">
                  <thead class="text-warning">
                    <tr>
                      <th></th>
                      <th>Star</th>
                      <th>SKU</th>
                    </tr>
                  </thead>
                  <tbody>
                  {
                      this.state.amzReviews.map((r , index) =>	      
                      <tr>
                        <td>{r.dateCreated}</td>
                        <td>{r.starRating}</td>
                        <td>{r.sku}</td>
                        </tr>
                      )
                    }
                   {(() => {
                      if(this.state.amzReviews.length == 0){
                       return (<tr><td></td><td>No Data</td><td></td></tr>)
                      }
                      
                      if(this.state.selectedSupplier.value == "All" || this.state.selectedSupplier ==""){
                       return (<tr><td></td><td colSpan="2">Only show 10 records</td></tr>)
                      }
                    }
                  )()}
                  </tbody>
                </table>
              </div>
              <div class="card-footer ">
                <hr/>
                <div class="stats">
                  <i class="fa fa-clock-o"></i> MTD
                </div>
              </div>
            </div>
    
    </div>

    <div class="col-md-6">
    
    <div class="card">
      <div class="card-header">
        <div class="card-text">
          <h3 class="card-title">Customer Returns</h3>
        </div>
      </div>
      <div class="card-body table-responsive">
        <table class="table table-hover">
          <thead class="text-warning">
            <tr>
              <th></th>
              <th>Order Id</th>
              <th>SKU</th>
            </tr>
          </thead>
          <tbody>
          {
              this.state.returns.map((r , index) =>	      
              <tr>
                <td>{r.returnDate}</td>
                <td>{r.orderId}</td>
                <td>{r.skuName}</td>
                </tr>
                )
            }
            {(() => {
                      if(this.state.returns.length == 0){
                       return (<tr><td></td><td>No Data</td><td></td></tr>)
                      }

                      if(this.state.selectedSupplier.value == "All" || this.state.selectedSupplier ==""){
                       return (<tr><td></td><td colSpan="2">Only show 10 records</td></tr>)
                      }
                    }
              )()}
          </tbody>
        </table>
      </div>
      <div class="card-footer ">
        <hr/>
        <div class="stats">
          <i class="fa fa-clock-o"></i> MTD
        </div>
      </div>
    </div>

</div>



          
          </div>


        
         
             
		</div>
      
      
		);
	}
	
}


  export default (ProductDashboard);


