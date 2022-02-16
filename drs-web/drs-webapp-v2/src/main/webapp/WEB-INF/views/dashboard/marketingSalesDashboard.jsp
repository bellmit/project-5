<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<head>

<title></title>

<script type='text/javascript' src="<c:url value="/resources/js/salesMarketingDashboard/main.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/salesMarketingDashboard/dropdown_functions.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/salesMarketingDashboard/graph_functions.js"/>"></script>
<script type='text/javascript' src="<c:url value="/resources/js/salesMarketingDashboard/variables.js"/>"></script>
<script type = "text/javascript" src = "https://d3js.org/d3.v4.min.js"></script>

</head>

<script>


$(function(){
	
	// get data and set sku_list
	set_sku_list(${listOfSkus});
	
	// initial dropdown load
	initial_dropdown_load();

	register_click_listeners();

	$('#draw_sku_btn').click(function(){

		set_all_toggles_true();

		//remove old graph
		//d3.select("svg").remove();
		$('#line_graph').html('');
		
		var marketplace = $("#marketplace_dropdown_menu_text").text();
		var kCode = $("#kcode_dropdown_menu_text").text();
		var skuCode = $("#sku_dropdown_menu_text").text();

		// console.log(marketplace);
		// console.log(kCode);
		// console.log(skuCode);
		
		$.ajax({
			
			type : 'get',
			url : '${pageContext.request.contextPath}/getMarketingSalesData',
			contentType : "application/json; charset=utf-8",
			data : {marketplace: marketplace, kCode: kCode, skuCode: skuCode},
			dataType : "json",
			success : function(data) {	
				
				// console.log(data);
				graph_data = JSON.parse(data);
				draw_graph();
				
			}
		});		
		
	})
	
	$('#draw_sku_btn').click();
	
	
})


</script>


<style type="text/css">

#main_container{
  margin-top: 40px;
  min-height: 700px;
}


#title{
  margin-top: 2rem;
  margin-bottom: 1.6rem;
  
}


#no_data_text{
	display: none;
	color: #e53935;

}

#line_graph{

  min-height: 400px;
}

.line {
  fill: none;
  stroke: #ff8f00;
  stroke-width: 1.5;
}

.overlay {
  fill: none;
  pointer-events: all;
}

.dot {
  fill: #ff8f00;
}

/*=========================================================*/

#graph_controls{
  min-height: 400px;
  padding-top: 15px;
}

#dropdown_group_container{
  background-color: #eeeeee;
  margin-bottom: 1rem;
  padding-top: 2.2rem;
  padding-bottom: 2.5rem;

}

.dropdown-toggle{
  margin: 0 auto;
  display: block !important;
  width: 80%;
  margin-bottom: 15px;
  box-shadow: 1px 1px 1px 1px #888888;
  color: white;
  font-size: 15px !important;
  font-weight: bold;
  border-radius: 0;
}

.dropdown-menu{
	margin-top: 0;
	margin-left: 2.4rem;
	width: 80%;
    padding: 0 !important;

}


.dropdown-item{
	border: 1px solid black !important;
	display: block;
	height: 30px;
	line-height: 27px;
	padding-left:2.5rem;
	font-size: 15px;
	
}


#toggle_group_container{
	margin-top: 4.2rem;
}


.category_btn{
  margin: 0 auto;
  width: 80%;
  border: .5px solid black;
  background-color: #616161;
  color: white;
  padding-top: 7px;
  padding-bottom: 7px;
  box-shadow: 1px 1px 1px 1px #888888;
  border-radius: 1px;

}

.category_btn p{
  margin: 0;
  text-align: left;
  margin-left: 2rem;
}

#ads_square{
	border-radius: 1px;
	background-color: #4caf50;
}

#review_square{
	border-radius: 1px;
	background-color: #2196f3;
}

#milestone_square{
	border-radius: 1px;
	background-color: #ff5252;
}



</style>


<div class="max-width">
	<div id="main_container" class="container-fluid">

  <h2 id="title">
    DRS Marketing Report &nbsp;
    
  </h2>

  <div class="row" id="graph_area">

    <div class="col-md-9">
    
		<h2 id="no_data_text">No Data to be Displayed.</h2>
		
		<div id="line_graph"></div>
		
    </div>

    <div id="graph_controls" class="col-md-3">


      <div id="dropdown_group_container">
        <div class="col-12">
          <div class="dropdown">
            <button class="btn dropdown-toggle" type="button" id="marketplace_dropdown_menu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="background-color: #2196f3;">
              <span id="marketplace_dropdown_menu_text"></span>  &#9662;
            </button>
            <div id="marketplace_dropdown_menu_list" class="dropdown-menu" aria-labelledby="dropdownMenuButton">

            </div>
          </div>
        </div>

        <div class="col-12">
          <div class="dropdown">
            <button class="btn dropdown-toggle" type="button" id="kcode_dropdown_menu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="background-color: #2196f3;">
              	<span id="kcode_dropdown_menu_text"></span>  &#9662;
              	
            </button>
            <div id="kcode_dropdown_menu_list" class="dropdown-menu" aria-labelledby="dropdownMenuButton">

            </div>
          </div>
        </div>

        <div class="col-12">
          <div class="dropdown">
            <button class="btn dropdown-toggle" type="button" id="sku_dropdown_menu" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="background-color: #2196f3;">
            	<span id="sku_dropdown_menu_text"></span>  &#9662;
            	
            </button>

            <div id="sku_dropdown_menu_list" class="dropdown-menu" aria-labelledby="dropdownMenuButton">

            </div>
          </div>
        </div>

        <div class="col-12 text-center">
          <button id="draw_sku_btn" type="button" class="btn btn-success">Draw Line Chart</button>
        </div>

          <div id="toggle_group_container">
	
	        <div class="col-12">
	          <div id="ads_toggle" class="category_btn">
	            <p>
	              Ads&nbsp;
	              <span id="ads_square" class="badge">&nbsp;&nbsp;</span>
	            </p>
	          </div>
	        </div>
	
	        <div class="col-12">
	          <div id="review_toggle" class="category_btn">
	            <p>Reviews&nbsp;
	              <span id="review_square" class="badge">&nbsp;&nbsp;</span>
	            </p>
	          </div>
	        </div>
	
	        <div class="col-12">
	          <div id="milestone_toggle" class="category_btn">
	            <p>Milestones&nbsp;
	              <span id="milestone_square" class="badge">&nbsp;&nbsp;</span>
	            </p>
	          </div>
	        </div>
	
	      </div>
      </div>

    </div>
  </div>
</div>
</div>

