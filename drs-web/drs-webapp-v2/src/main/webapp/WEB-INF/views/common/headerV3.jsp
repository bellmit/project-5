<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<script>
	$(document).ready(function() {
		var  mn = $(".main-nav");
    	mns = "main-nav-scrolled";
    	hdr = $('header').height();
		$(window).scroll(function() {
  			if( $(this).scrollTop() > hdr ) {
    			mn.addClass(mns);
  			} else {
    			mn.removeClass(mns);
  			}
		});			
	});
</script>
<script>

function toggleDropdown (e) {
	  let _d = $(e.target).closest('.dropdown'),
	      _m = $('.dropdown-menu', _d);
	  setTimeout(function(){
	    let shouldOpen = e.type !== 'click' && _d.is(':hover');
	    _m.toggleClass('show', shouldOpen);
	    _d.toggleClass('show', shouldOpen);
	    $('[data-toggle="dropdown"]', _d).attr('aria-expanded', shouldOpen);
	  }, e.type === 'mouseleave' ? 300 : 0);
	}

	$('body')
	  .on('mouseenter mouseleave','.dropdown',toggleDropdown)
	  .on('click', '.dropdown-menu a', toggleDropdown);

</script>
<style>

.main-nav {
 	position: relative;
  	z-index: 150;

}
.main-nav-scrolled {
	position: fixed;
  	width: 100%;
  	top: 0;

}

#navbar {

    width: 1200px;
    margin: auto;
}

#logo {
    height:45px;
    margin: 5px 30px 5px 0;
}

.nav-link, .dropdown-item {
    font-family: Montserrat, Arial, Helvetica, sans-serif;
}

.nav-item {

	padding-left: 5px;
	padding-right: 5px;
	
}

#header {
    box-shadow: 0px 1px 15px rgb(182, 179, 179);
  	width: 100%;
  	margin: auto;
  	background-color: #fff;
}


#s5_menu_wrap {
padding:0;

}

.dropdown-menu {
box-shadow: 0px 15px 20px rgb(182, 179, 179);
-moz-box-shadow: 0px 15px 20px rgb(182, 179, 179);
-webkit-box-shadow: 0px 15px 20px rgb(182, 179, 179);
border-radius: 0px;
border: 0px;
clear: both;
font-size: 1rem;
margin-top: 0px;
}

.show > .dropdown-menu {
  max-height: 900px;
  visibility: visible;

}

</style>
<header id="header" class="main-nav">
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-light" id="s5_menu_wrap">
    <a href="<c:url value="/"/>"><img class="navbar-brand" id="logo" src="<c:url value="/resources/images/drs_logo2.png"/>"></a>
     <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav ml-auto">

       <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			<spring:message code='header.products' />
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">

		<sec:authorize access="hasAnyRole('${auth['PRODUCTS_NAVIGATION']}')">
		  <a class="dropdown-item" href="${pageContext.request.contextPath}/Products"><spring:message code='header.ProductsInformationMaintenance' /></a>
		</sec:authorize>
		 
		<sec:authorize access="hasAnyRole('${auth['PRODUCTS_NAVIGATION']}')">
		  <a class="dropdown-item" href="${pageContext.request.contextPath}/NewDevelopingProductList"><spring:message code='header.manageProducts' /></a>
		  </sec:authorize>
		  <sec:authorize access="hasAnyRole('${auth['ASIN_MAPPING']}')">
		  <a class="dropdown-item" href="${pageContext.request.contextPath}/asin"><spring:message code='header.asinMapping' /></a>
		  </sec:authorize>

		  	  <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
          		  <a class="dropdown-item" href="${pageContext.request.contextPath}/pd">Product Dashboard (Internal)</a>
             </sec:authorize>

		  	  <sec:authorize access="hasAnyRole('${auth['DRS_1_2_X']}')">
          		  <a class="dropdown-item" href="${pageContext.request.contextPath}/product">Manage Product (RC 1.2.x)</a>
             </sec:authorize>
        </div>
	  </li>

	  <li class="nav-item">
			<sec:authorize access="hasAnyRole('${auth['CUSTOMER_ORDER_NAVIGATION']}')">
			<a class="nav-link" href="${pageContext.request.contextPath}/CustomerOrderList"><spring:message code='header.orders' /></a>
			</sec:authorize>
		  </li>

		  <li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<spring:message code='header.logistics' />
				</a>
				<div class="dropdown-menu" aria-labelledby="navbarDropdown">
				<sec:authorize access="hasAnyRole('${auth['REPLENISHMENT_PREDICTION']}')">
				  <a class="dropdown-item" href="${pageContext.request.contextPath}/ReplenishmentPlanning"><spring:message code='header.replenishmentPrediction' /></a>
				  </sec:authorize>
				  <sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_NAVIGATION']}')">
				  <a class="dropdown-item" href="${pageContext.request.contextPath}/InventoryShipments"><spring:message code='header.inventoryShipments' /></a>
				  </sec:authorize>
				  <sec:authorize access="hasAnyRole('${auth['UNIFIED_SHIPMENTS_NAVIGATION']}')">
					<a class="dropdown-item" href="${pageContext.request.contextPath}/UnifiedShipments"><spring:message code='header.unifiedShipments' /></a>
				  </sec:authorize>
				<sec:authorize access="hasAnyRole('${auth['REPLENISHMENT_INFORMATION']}')">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/replenishment-information"><spring:message code='header.replenishmentInformation' /></a>
				</sec:authorize>
				<sec:authorize access="hasAnyRole('${auth['AMAZON_INVENTORY_AVAILABILITY']}')">
					<a class="dropdown-item" href="${pageContext.request.contextPath}/updateProductSkuFBAInventoryAvailability"><spring:message code='header.inventoryAvailability' /></a>
				</sec:authorize>

			    <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                      <a class="dropdown-item" href="${pageContext.request.contextPath}/afi">FBA Inventory (beta - internal)</a>
                   </sec:authorize>


                 <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                     <a class="dropdown-item" href="${pageContext.request.contextPath}/bwinv">Biweekly Inventory (beta - internal)</a>
                  </sec:authorize>

				</div>
			  </li>

			  <li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<spring:message code='header.customerCare'/>
					</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
					<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_NAVIGATION']}')">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/CustomerCareCases"><spring:message code='header.customerCareCase' /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['ISSUES_NAVIGATION']}')">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/Issues"><spring:message code='header.issues' /></a>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                    		<a class="dropdown-item" href="${pageContext.request.contextPath}/cci">Customer Care Cases Issues (beta - internal)</a>
                     </sec:authorize>
					</div>
				  </li>

				  <li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<spring:message code='header.accounting'/>
						</a>
						<div class="dropdown-menu" aria-labelledby="navbarDropdown">
							<sec:authorize access="hasAnyRole('${auth['SS2SP_STATEMENTS_NAVIGATION']}')">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/statements"><spring:message code='header.ss2spStatements' /></a>
							</sec:authorize>
							<sec:authorize access="hasAnyRole('${auth['MS2SS_STATEMENTS_NAVIGATION']}')">
								<a class="dropdown-item" href="${pageContext.request.contextPath}/MS2SS-Statements/received"><spring:message code='header.intercompanyStatementsReceived' /></a>
							</sec:authorize>
							<sec:authorize access="hasAnyRole('${auth['MS2SS_STATEMENTS_NAVIGATION']}')">
								<a class="dropdown-item" href="${pageContext.request.contextPath}/MS2SS-Statements/issued"><spring:message code='header.intercompanyStatementsIssued' /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['REMITTANCE_LIST']}')">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/Remittance"><spring:message code='header.remittance' /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['DOMESTIC_TRANSACTION']}')">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/DomesticTransactions"><spring:message code='header.domesticTransaction' /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['INTERNATIONAL_TRANSACTION']}')">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/InternationalTransactions"><spring:message code='header.internationalTransaction' /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['IMPORT_DUTY_TRANSACTION']}')">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/ImportDutyTransaction"><spring:message code='header.importDutyTransaction' /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['SS2SP_DRAFT_STATEMENTS']}')">				
							<a class="dropdown-item" href="${pageContext.request.contextPath}/draft-statements"><spring:message code='header.draftStatements' /></a>
					</sec:authorize>			
					<sec:authorize access="hasAnyRole('${auth['PROFIT_COST_SHARE_STATEMENTS']}')">				
							<a class="dropdown-item" href="${pageContext.request.contextPath}/ProfitCostShareStatements"><spring:message code='header.profitCostShareStatements' /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PROCESS_COUPON_REDEMPTION']}')">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/ProcessCouponRedemptionFee"><spring:message code='header.processCouponRedemptionFee' /></a>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('${auth['PROCESS_AD_SPEND']}')">				
							<a class="dropdown-item" href="${pageContext.request.contextPath}/ProcessAdSpend">Process Ad Spend (Internal)</a>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                         <a class="dropdown-item" href="${pageContext.request.contextPath}/accinv">Invoice (beta - internal)</a>
                      </sec:authorize>

					<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/niinv">Non-issued Invoice (beta - internal)</a>
					  </sec:authorize>  

                    <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                       <a class="dropdown-item" href="${pageContext.request.contextPath}/amzse">Amazon Settlement Report (beta - internal)</a>
                    </sec:authorize>

                     <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                          <a class="dropdown-item" href="${pageContext.request.contextPath}/araging">AR Aging (internal)</a>
                     </sec:authorize>


					</div>
					  </li>
					  <li class="nav-item dropdown">
							<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
								<spring:message code='header.report' />
							</a>
							<div class="dropdown-menu" aria-labelledby="navbarDropdown">
									<sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT']}')">
											<a  class="dropdown-item" href="${pageContext.request.contextPath}/customer-satisfaction"><spring:message code='customerSatisfaction.title' /></a>
									</sec:authorize>
									<sec:authorize access="hasAnyRole('${auth['SALES_AND_PAGE_TRAFFIC_REPORT']}')">
											<a class="dropdown-item" href="${pageContext.request.contextPath}/SalesAndPageTrafficReport"><spring:message code='header.salesAndPageTrafficReport' /></a>
									</sec:authorize>
									<sec:authorize access="hasAnyRole('${auth['VIEW_MONTHLY_STOREAGE_FEE_REPORT']}')">
											<a class="dropdown-item" href="${pageContext.request.contextPath}/storage-fee"><spring:message code='header.storageFee' /></a>
									</sec:authorize>
									<sec:authorize access="hasAnyRole('${auth['AMAZON_INVENTORY_HEALTH_REPROT_VIEW']}')">
											<a class="dropdown-item" href="${pageContext.request.contextPath}/inventory-health-report"><spring:message code='inventoryHealthReport.title' /></a>
									</sec:authorize>
									<sec:authorize access="hasAnyRole('${auth['IMPORT_AMAZON_REPORT']}')">
											<a class="dropdown-item" href="${pageContext.request.contextPath}/ImportAmazonReport"><spring:message code='header.importInventoryReport' /></a>
									</sec:authorize>
									<sec:authorize access="hasAnyRole('${auth['EMAIL_REMINDER']}')">
											<a class="dropdown-item" href="${pageContext.request.contextPath}/emailReminder"><spring:message code='header.emailReminder' /></a>
									</sec:authorize>

									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
											<a class="dropdown-item" href="${pageContext.request.contextPath}/sdb">Sales Dashboard (beta - internal)</a>
									</sec:authorize>

									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
											<a class="dropdown-item" href="${pageContext.request.contextPath}/cpdb">Campaign Performance Dashboard (beta - internal)</a>
									</sec:authorize>

									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                                    		<a class="dropdown-item" href="${pageContext.request.contextPath}/rv">Amazon Review (beta - internal)</a>
                                    </sec:authorize>

                                    <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                                    				<a class="dropdown-item" href="${pageContext.request.contextPath}/re">Amazon Return (beta - internal)</a>
                                    </sec:authorize>

									 <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                                           <a class="dropdown-item" href="${pageContext.request.contextPath}/sab">Sales Board (alpha - internal)</a>
                                     </sec:authorize>

                                      <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                                          <a class="dropdown-item" href="${pageContext.request.contextPath}/sks">Supplier key Stats (internal)</a>
                                      </sec:authorize>


							</div>
						  </li>

						  <li class="nav-item dropdown">
								<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
									<spring:message code='header.marketingActivities' />
								</a>
								<div class="dropdown-menu" aria-labelledby="navbarDropdown">
										<sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT']}')">
												<a class="dropdown-item" href="${pageContext.request.contextPath}/sku-adv-performance-report"><spring:message code='marketingReport.skuAdvertisingPerformanceReport' /></a>
										</sec:authorize>
										<sec:authorize access="hasAnyRole('${auth['SEARCH_TERM_REPORT']}')">
												<a class="dropdown-item" href="${pageContext.request.contextPath}/SearchTermReport"><spring:message code='marketingReport.searchTermReport' /></a>
										</sec:authorize>
										<sec:authorize access="hasAnyRole('${auth['SKU_ADV_EVAL_REPORT']}')">
												<a class="dropdown-item" href="${pageContext.request.contextPath}/sku-adv-eval-report"><spring:message code='marketingReport.skuAdvertisingEvaluationReport' /></a>
										</sec:authorize>
										<sec:authorize access="hasAnyRole('${auth['SPONSORED_BRANDS_CAMPAIGN_REPORT']}')">
												<a class="dropdown-item" href="${pageContext.request.contextPath}/AmazonSponsoredBrandsCampaignReport"><spring:message code='header.sponsoredBrandsCampaign' /></a>
										</sec:authorize>
										<sec:authorize access="hasAnyRole('${auth['SPONSORED_BRANDS_CAMPAIGN']}')">
                                        	    <a class="dropdown-item" href="${pageContext.request.contextPath}/AmazonSponsoredBrandsCampaign">
                                        			<spring:message code='header.sponsoredBrandsCampaignCP' /></a>
                                        </sec:authorize>

										<sec:authorize access="hasAnyRole('${auth['CAMPAIGN_PERFORMANCE_REPORT_NAVIGATION']}')">
												<a class="dropdown-item" href="${pageContext.request.contextPath}/MarketingReports">
												<spring:message code='header.importMarketingReport' /></a>
										</sec:authorize>
								</div>
							  </li>

							  <li class="nav-item dropdown">
									<a class="nav-link" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
											<i id="user_icon" class="fas fa-user-circle fa-lg"></i>
									</a>
									<div class="dropdown-menu" aria-labelledby="navbarDropdown">
									<a class="dropdown-item" href="#">${user.userDisplayName}</a>
									<div class="dropdown-divider"></div>
											<sec:authorize access="hasAnyRole('${auth['COMPANIES_NAVIGATION']}')">
													<sec:authorize access="hasAnyRole('${auth['COMPANIES_VIEW_BY_ONE']}')">
															<a class="dropdown-item" href="${pageContext.request.contextPath}/Company/view"><spring:message code='header.companyInfoMaintenance' /> </a>
													</sec:authorize>
													<sec:authorize access="hasAnyRole('${auth['COMPANIES_EDIT_BY_ONE']}')">
															<a class="dropdown-item" href="${pageContext.request.contextPath}/Company/edit"><spring:message code='header.companyInfoMaintenance' /></a>
													</sec:authorize>
													</sec:authorize>
													<sec:authorize access="hasAnyRole('${auth['ARTICLE_MANAGEMENT']}')">
															<a class="dropdown-item" href="<spring:message code='Joomla_root_link' />/account/article-management"><spring:message code='header.article-management' /></a>
													</sec:authorize>
															<a class="dropdown-item" href="<c:url value="/resources/files/DRSChannelParticipationAgreement-zh_TW.pdf"/>"><spring:message code='header.agreement' /></a>
													<sec:authorize access="hasAnyRole('${auth['DRS_TYPOGRAPHY']}')">
															<a class="dropdown-item" href="<spring:message code='Joomla_root_link' />/account/drs_typography"><spring:message code='header.drs-typography' /></a>
													</sec:authorize>
															<a class="dropdown-item" 
															href="<c:url value="/logout" />"><spring:message code='header.logout' /></a>
									</div>
								  </li>
					</ul>
			</div>

    </nav>
    </div>
</header>