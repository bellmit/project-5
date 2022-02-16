<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<head>

<title><spring:message code='processAdSpend.viewHSAReport' /></title>
<script src="<c:url value="/resources/js/pagination.js"/>"></script>

<style type="text/css">
#settlement-period-row {
	background-color: #eeeeee;
	border-radius: 5px;
	min-height: 56px;
}

#table-pagination {
	margin-bottom: 17px;
}

.paginationjs-pages ul li a {
	width: 40px;
}

#page-set {
	font-size: 18px;
	margin-top: 3px;
	width: 105%;
}

#table-section {
	min-height: 300px;
	padding: 0px;
}

thead tr {
	font-size: 14px;
}
</style>

<script>

$(function(){
	
	//parse json from controller
	var transactions = JSON.parse(${transactionsJSON});
	//console.log(transactions);
	
	//if there is report
	if(transactions.length > 0){
	    function createDemo(name){
	        var container = $('#pagination-' + name);
	
	        var options = {
	            	
	        	dataSource: transactions,            
	        	pageSize: 100,        	
	        	pageRange: 3,
	        	
	            callback: function(response, pagination){
	                // window.console && console.log(response, pagination);
	                
	                //logs the page number user is currently on 
	                console.log(pagination.pageNumber);
	                
	                var transactionSet = pagination.pageNumber * 100;
	                var totalTransactions = transactions.length;
	                
	                //show page index
	                $("#page-set").html(transactionSet + "/" + totalTransactions);
	
	                var dataHtml = '';
	
	                $.each(response, function(index, transaction){
	                    dataHtml += '<tr>\
	                    	<td>'+ transaction.id +'</td>\
	                     <td>'+ transaction.supplierKcode +'</td>\
	                     <td>'+ transaction.marketplaceName +'</td>\
	                     <td>'+ transaction.campaignName +'</td>\
	                     <td>'+ transaction.reportDate +'</td>\
	                     <td>'+ transaction.currencyName +'</td>\
	                     <td>'+ transaction.totalSpend +'</td>\
	                     </tr>';
	                });
	
	                $("#transactionTable").html(dataHtml);
	                
	            }
	        };
	
	        $.pagination(container, options);
	
	        container.addHook('beforeInit', function(){
	            window.console && console.log('beforeInit...');
	        });
	        container.pagination(options);
	
	        container.addHook('beforePageOnClick', function(){
	            window.console && console.log('beforePageOnClick...');
	            //return false
	        });
	        return container;
	    }
	    createDemo('bar');
	
	//if no report 
	}else{
		$("#no-report-available-text").html("<h4>No Campaign Report Available.</h4>");
	};
    
	
    //export json as csv file
    $("#export-csv").click(function(){
        
        //convert json to csv
        const items = transactions
        const replacer = (key, value) => value === null ? '' : value // specify how you want to handle null values here
        const header = Object.keys(items[0])
        let csv = items.map(row => header.map(fieldName => JSON.stringify(row[fieldName], replacer)).join(','))
        csv.unshift(header.join(','))
        csv = csv.join('\r\n')
        
        var csvData = new Blob([csv], { type: 'text/csv' }); //new way
        var csvUrl = URL.createObjectURL(csvData);

        $(this)
        .attr({
            'download': 'campaign_detail.csv',
            'href': csvUrl
        });
        
    });
    
});

</script>

</head>

<div class="max-width">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-12" style="padding-left: 0px;">
				<div class="page-heading" style="font-weight: bold;"><spring:message code='processAdSpend.viewHSAReport' /></div>
			</div>
		</div>

		<div class="row" id="settlement-period-row">
			<div class="col-md-8">
				<h3 style="margin-top: 16px;">
					<spring:message code='processAdSpend.settlementPeriod' />
					: ${latestSettlementPeriod.startDate} ~
					${latestSettlementPeriod.endDate}
				</h3>
			</div>

			<div class="col-md-4" style="padding-top: 13px;">
				<a href="${pageContext.request.contextPath}/ProcessAdSpend"
					class="btn btn-primary" style="float: right;"> <spring:message
						code='processAdSpend.backToProcessPage' />
				</a> <a id="export-csv" class="btn btn-warning" style="float: right;">
					<spring:message code='processAdSpend.exportFile' />
				</a>
			</div>
		</div>
		<br />
		<div class="row" id="table-pagination">
			<div class="col-md-6 col-md-offset-3">
				<div id="pagination-bar"></div>
			</div>
			<div class="col-md-2">
				<div id="page-set" class="badge badge-warning"></div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12" id="table-section">
				<table class="table table-bordered">
					<thead>
						<tr class="success">
							<th><spring:message code='processAdSpend.id' /></th>
							<th><spring:message code='processAdSpend.supplier' /></th>
							<th><spring:message code='processAdSpend.marketplaceName' /></th>
							<th><spring:message code='processAdSpend.campaignName' /></th>
							<th><spring:message code='processAdSpend.utcDate' /></th>
							<th><spring:message code='processAdSpend.currency' /></th>
							<th><spring:message code='processAdSpend.totalSpend' /></th>
						</tr>
					</thead>
					<tbody id="transactionTable"></tbody>
				</table>
				<div id="no-report-available-text"></div>
			</div>
		</div>
	</div>
</div>
