 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@ page contentType="text/html; charset=utf-8"%> 
 <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
 <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

   <div class="sidebar" data-color="white" data-active-color="orange">
      <!--
        Tip 1: You can change the color of the sidebar using: data-color="blue | green | orange | red | yellow"
    -->
      <div class="logo">

        <a href="/" class="simple-text logo-mini">
          <div class="logo-image-small">
            <img src="../resources/images/drs-favicon.ico">
          </div>
        </a>


        <a href="/" class="simple-text logo-small">
         <div class="logo-image-small">
            <img id="drsLogo" style="width:88px" src="../resources/images/drs_logo2-removebg.png">
          </div>
        </a>
      </div>
      <div class="sidebar-wrapper">

        <ul class="nav">

             <li class="active">
                      <a data-toggle="collapse" href="#pagesExamples">
                        <i class="nc-icon nc-book-bookmark"></i>
                        <p>
                          <spring:message code='header.products' />
                          <b class="caret"></b>
                        </p>
                      </a>
                      <div class="collapse " id="pagesExamples">
                        <ul class="nav">

                          <li>
                            <a href="${pageContext.request.contextPath}/Products">
                              <span class="sidebar-mini-icon">T</span>
                              <span class="sidebar-normal">
                                  <spring:message code='header.ProductsInformationMaintenance' />
                               </span>
                            </a>
                          </li>

                          	<sec:authorize access="hasAnyRole('${auth['PRODUCTS_NAVIGATION']}')">
                           <li>
                                <a href=" ${pageContext.request.contextPath}/NewDevelopingProductList">
                                   <span class="sidebar-mini-icon">L</span>
                                   <span class="sidebar-normal">
                                   <spring:message code='header.manageProducts' /> </span>
                                 </a>
                            </li>

                          </sec:authorize>
                             <sec:authorize access="hasAnyRole('${auth['ASIN_MAPPING']}')">
		                       <li>
                                 <a href="${pageContext.request.contextPath}/asin">
                                   <span class="sidebar-mini-icon">R</span>
                                   <span class="sidebar-normal"> <spring:message code='header.asinMapping' /> </span>
                                  </a>
                               </li>
		                </sec:authorize>


                      <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
		                       <li>
                                 <a href="${pageContext.request.contextPath}/pd">
                                   <span class="sidebar-mini-icon">D</span>
                                   <span class="sidebar-normal">Product Dashboard (Internal)</span>
                                  </a>
                               </li>
		                </sec:authorize>


                        </ul>
                      </div>
                    </li>

         <sec:authorize access="hasAnyRole('${auth['CUSTOMER_ORDER_NAVIGATION']}')">
            <li >
            <a href="${pageContext.request.contextPath}/CustomerOrderList">
              <i class="nc-icon nc-bank"></i>
              <p><spring:message code='header.orders' /></p>
            </a>
          </li>

          </sec:authorize>

          <li>
            <a data-toggle="collapse" href="#componentsExamples">
              <i class="nc-icon nc-layout-11"></i>
              <p>
             <spring:message code='header.logistics' />
                <b class="caret"></b>
              </p>
            </a>
            <div class="collapse " id="componentsExamples">
              <ul class="nav">

             <sec:authorize access="hasAnyRole('${auth['REPLENISHMENT_PREDICTION']}')">
               <li>
                              <a href="${pageContext.request.contextPath}/ReplenishmentPlanning">
                                <span class="sidebar-mini-icon">B</span>
                                <span class="sidebar-normal"> <spring:message code='header.replenishmentPrediction' /> </span>
                              </a>
                            </li>

                </sec:authorize>

            <sec:authorize access="hasAnyRole('${auth['INVENTORY_SHIPMENTS_NAVIGATION']}')">
                 <li>
                            <a href=" ${pageContext.request.contextPath}/InventoryShipments">
                              <span class="sidebar-mini-icon">G</span>
                              <span class="sidebar-normal"><spring:message code='header.inventoryShipments' /></span>
                            </a>
               </li>
            </sec:authorize>

            <sec:authorize access="hasAnyRole('${auth['UNIFIED_SHIPMENTS_NAVIGATION']}')">
          	  <li>
                 <a href="${pageContext.request.contextPath}/UnifiedShipments">
                   <span class="sidebar-mini-icon">P</span>
                   <span class="sidebar-normal"><spring:message code='header.unifiedShipments' /></span>
                 </a>
              </li>
          	</sec:authorize>
	        <sec:authorize access="hasAnyRole('${auth['REPLENISHMENT_INFORMATION']}')">
          	  <li>
                 <a href="${pageContext.request.contextPath}/replenishment-information">
                                <span class="sidebar-mini-icon">SA</span>
                                <span class="sidebar-normal"><spring:message code='header.replenishmentInformation' /></span>
                 </a>
              </li>
          	</sec:authorize>
            <sec:authorize access="hasAnyRole('${auth['AMAZON_INVENTORY_AVAILABILITY']}')">
             <li>
                             <a href="${pageContext.request.contextPath}/updateProductSkuFBAInventoryAvailability">
                               <span class="sidebar-mini-icon">N</span>
                               <span class="sidebar-normal"><spring:message code='header.inventoryAvailability' /></span>
                             </a>
             </li>

            </sec:authorize>

            <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

               <li>
                              <a href=" ${pageContext.request.contextPath}/afi">
                                <span class="sidebar-mini-icon">I</span>
                                <span class="sidebar-normal">FBA Inventory (beta - internal)</span>
                              </a>
                            </li>


            </sec:authorize>



              <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                  <li>
                   <a class="dropdown-item" href="${pageContext.request.contextPath}/bwinv">
                    <span class="sidebar-mini-icon">I</span>
                      <span class="sidebar-normal">   Biweekly Inventory (beta - internal)</span>
                     </a>
                   </li>

               </sec:authorize>

              </ul>
            </div>
          </li>


          <li>
            <a data-toggle="collapse" href="#formsExamples">
              <i class="nc-icon nc-ruler-pencil"></i>
              <p>
                <spring:message code='header.customerCare'/>
                <b class="caret"></b>
              </p>
            </a>
            <div class="collapse " id="formsExamples">
              <ul class="nav">
              	<sec:authorize access="hasAnyRole('${auth['CUSTOMER_CARE_CASE_NAVIGATION']}')">
               <li>
                                 <a href="${pageContext.request.contextPath}/CustomerCareCases">
                                   <span class="sidebar-mini-icon">RF</span>
                                   <span class="sidebar-normal"><spring:message code='header.customerCareCase' />
                                   </span>
                                 </a>
                               </li>
               </sec:authorize>

	        <sec:authorize access="hasAnyRole('${auth['ISSUES_NAVIGATION']}')">
          							<a class="dropdown-item" href=></a>
          	 <li>
                              <a href="${pageContext.request.contextPath}/Issues">
                                <span class="sidebar-mini-icon">EF</span>
                                <span class="sidebar-normal"><spring:message code='header.issues' /></span>
                              </a>
                            </li>

          	</sec:authorize>
               <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                  <li>
                       <a href="${pageContext.request.contextPath}/cci">
                         <span class="sidebar-mini-icon">V</span>
                         <span class="sidebar-normal">Customer Care Cases Issues (beta - internal)</span>
                        </a>
                  </li>

                </sec:authorize>
              </ul>
            </div>
          </li>


        	  <li>
          	   <a data-toggle="collapse" href="#tablesExamples">
                            <i class="nc-icon nc-single-copy-04"></i>
                            <p>
                              <spring:message code='header.accounting'/>
                              <b class="caret"></b>
                            </p>
                          </a>
                          <div class="collapse " id="tablesExamples">
                        <ul class="nav">
          							<sec:authorize access="hasAnyRole('${auth['SS2SP_STATEMENTS_NAVIGATION']}')">
          							   <li>
                                                      <a href="${pageContext.request.contextPath}/statements">
                                                        <span class="sidebar-mini-icon">RT</span>
                                                        <span class="sidebar-normal"><spring:message code='header.ss2spStatements' /></span>
                                                      </a>
                                                    </li>
          							</sec:authorize>
          							<sec:authorize access="hasAnyRole('${auth['MS2SS_STATEMENTS_NAVIGATION']}')">

          						   <li>
                                                  <a href="${pageContext.request.contextPath}/MS2SS-Statements/received">
                                                    <span class="sidebar-mini-icon">RT</span>
                                                    <span class="sidebar-normal"><spring:message code='header.intercompanyStatementsReceived' /></span>
                                                  </a>
                                                </li>
          							</sec:authorize>
          							<sec:authorize access="hasAnyRole('${auth['MS2SS_STATEMENTS_NAVIGATION']}')">
          				   <li>
                                          <a href="${pageContext.request.contextPath}/MS2SS-Statements/issued">
                                            <span class="sidebar-mini-icon">RT</span>
                                            <span class="sidebar-normal"><spring:message code='header.intercompanyStatementsIssued' /></span>
                                          </a>
                                        </li>
          					</sec:authorize>
          					<sec:authorize access="hasAnyRole('${auth['REMITTANCE_LIST']}')">

          					   <li>
                                              <a href="${pageContext.request.contextPath}/Remittance">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal"><spring:message code='header.remittance' /></span>
                                              </a>
                                            </li>
          					</sec:authorize>
          					<sec:authorize access="hasAnyRole('${auth['DOMESTIC_TRANSACTION']}')">

          					   <li>
                                              <a href="${pageContext.request.contextPath}/DomesticTransactions">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal"><spring:message code='header.domesticTransaction' /></span>
                                              </a>
                                            </li>
          					</sec:authorize>
          					<sec:authorize access="hasAnyRole('${auth['INTERNATIONAL_TRANSACTION']}')">

          					   <li>
                                              <a href="${pageContext.request.contextPath}/InternationalTransactions">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal"><spring:message code='header.internationalTransaction' /></span>
                                              </a>
                                            </li>
          					</sec:authorize>
          					<sec:authorize access="hasAnyRole('${auth['IMPORT_DUTY_TRANSACTION']}')">

          					   <li>
                                              <a  href="${pageContext.request.contextPath}/ImportDutyTransaction">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal">	 <spring:message code='header.importDutyTransaction' /></span>
                                              </a>
                                            </li>
          					</sec:authorize>
          					<sec:authorize access="hasAnyRole('${auth['SS2SP_DRAFT_STATEMENTS']}')">

          					   <li>
                                              <a href="${pageContext.request.contextPath}/draft-statements">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal"><spring:message code='header.draftStatements' /></span>
                                              </a>
                                            </li>
          					</sec:authorize>
          					<sec:authorize access="hasAnyRole('${auth['PROFIT_COST_SHARE_STATEMENTS']}')">

          					   <li>
                                              <a href="${pageContext.request.contextPath}/ProfitCostShareStatements">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal"><spring:message code='header.profitCostShareStatements' /></span>
                                              </a>
                                            </li>
          					</sec:authorize>
          					<sec:authorize access="hasAnyRole('${auth['PROCESS_COUPON_REDEMPTION']}')">
          					   <li>
                                              <a href="${pageContext.request.contextPath}/ProcessCouponRedemptionFee">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal">	<spring:message code='header.processCouponRedemptionFee' /></span>
                                              </a>
                                            </li>
          					</sec:authorize>
          					<sec:authorize access="hasAnyRole('${auth['PROCESS_AD_SPEND']}')">
          					   <li>
                                              <a href="${pageContext.request.contextPath}/ProcessAdSpend">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal">Process Ad Spend (Internal)</span>
                                              </a>
                                            </li>
          					</sec:authorize>

          					<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                 <li>
                                                <a href="${pageContext.request.contextPath}/accinv">
                                                  <span class="sidebar-mini-icon">RT</span>
                                                  <span class="sidebar-normal"> Invoice (beta - internal)</span>
                                                </a>
                                              </li>
                                </sec:authorize>

                                <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                 <li>
                                                <a href="${pageContext.request.contextPath}/niinv">
                                                  <span class="sidebar-mini-icon">RT</span>
                                                  <span class="sidebar-normal"> Non-issued Invoice (beta - internal)</span>
                                                </a>
                                              </li>
                                </sec:authorize>

                              <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                               <li>
                                              <a  href="${pageContext.request.contextPath}/amzse">
                                                <span class="sidebar-mini-icon">RT</span>
                                                <span class="sidebar-normal">Amazon Settlement Report (beta - internal)</span>
                                              </a>
                                            </li>
                              </sec:authorize>

                               <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
                                 <li>
                                                <a  href="${pageContext.request.contextPath}/araging">
                                                  <span class="sidebar-mini-icon">RT</span>
                                                  <span class="sidebar-normal">AR Aging (internal)</span>
                                                </a>
                                              </li>
                               </sec:authorize>


                        </ul>
                        </div>
          		 </li>



            <li>
                    	   <a data-toggle="collapse" href="#rpts">
                                      <i class="nc-icon nc-chart-bar-32"></i>
                                      <p>
                                        <spring:message code='header.report'/>
                                        <b class="caret"></b>
                                      </p>
                                    </a>
                                    <div class="collapse " id="rpts">
                                  <ul class="nav">


	                                    <sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT']}')">
                                               <li>
                                                <a  href="${pageContext.request.contextPath}/customer-satisfaction">
                                                  <span class="sidebar-mini-icon">RT</span>
                                                  <span class="sidebar-normal"> <spring:message code='customerSatisfaction.title' /></span>
                                                </a>
                                              </li>

          							</sec:authorize>
          									<sec:authorize access="hasAnyRole('${auth['SALES_AND_PAGE_TRAFFIC_REPORT']}')">

                                                  <li>
                                                    <a href="${pageContext.request.contextPath}/SalesAndPageTrafficReport">
                                                      <span class="sidebar-mini-icon">RT</span>
                                                      <span class="sidebar-normal">	<spring:message code='header.salesAndPageTrafficReport' /></span>
                                                    </a>
                                                  </li>
          									</sec:authorize>
          									<sec:authorize access="hasAnyRole('${auth['VIEW_MONTHLY_STOREAGE_FEE_REPORT']}')">

                                                 <li>
                                                   <a href="${pageContext.request.contextPath}/storage-fee">
                                                     <span class="sidebar-mini-icon">RT</span>
                                                     <span class="sidebar-normal"><spring:message code='header.storageFee' /></span>
                                                   </a>
                                                 </li>

          									</sec:authorize>
          									<sec:authorize access="hasAnyRole('${auth['AMAZON_INVENTORY_HEALTH_REPROT_VIEW']}')">

                                                <li>
                                                 <a href="${pageContext.request.contextPath}/inventory-health-report">
                                                   <span class="sidebar-mini-icon">RT</span>
                                                   <span class="sidebar-normal"><spring:message code='inventoryHealthReport.title' /></span>
                                                 </a>
                                               </li>

          									</sec:authorize>
          									<sec:authorize access="hasAnyRole('${auth['IMPORT_AMAZON_REPORT']}')">

                                                <li>
                                                 <a href="${pageContext.request.contextPath}/ImportAmazonReport">
                                                   <span class="sidebar-mini-icon">RT</span>
                                                   <span class="sidebar-normal"><spring:message code='header.importInventoryReport' /></span>
                                                 </a>
                                               </li>

          									</sec:authorize>
          									<sec:authorize access="hasAnyRole('${auth['EMAIL_REMINDER']}')">

                                                <li>
                                                  <a href="${pageContext.request.contextPath}/emailReminder">
                                                    <span class="sidebar-mini-icon">RT</span>
                                                    <span class="sidebar-normal"><spring:message code='header.emailReminder' /></span>
                                                  </a>
                                                </li>
          									</sec:authorize>

          									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                                <li>
                                                  <a href="${pageContext.request.contextPath}/sdb">
                                                    <span class="sidebar-mini-icon">RT</span>
                                                    <span class="sidebar-normal">Sales Dashboard (beta - internal)</span>
                                                  </a>
                                                </li>

          									</sec:authorize>

          									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                                <li>
                                                 <a href="${pageContext.request.contextPath}/cpdb">
                                                   <span class="sidebar-mini-icon">RT</span>
                                                   <span class="sidebar-normal">Campaign Performance Dashboard (beta - internal)</span>
                                                 </a>
                                               </li>

          									</sec:authorize>

          									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                                <li>
                                                  <a href="${pageContext.request.contextPath}/rv">
                                                    <span class="sidebar-mini-icon">RT</span>
                                                    <span class="sidebar-normal">Amazon Review (beta - internal)</span>
                                                  </a>
                                                </li>

                                             </sec:authorize>

                                              <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                                <li>
                                                  <a href="${pageContext.request.contextPath}/re">
                                                    <span class="sidebar-mini-icon">RT</span>
                                                    <span class="sidebar-normal">Amazon Return (beta - internal)</span>
                                                  </a>
                                                </li>

                                              </sec:authorize>

          									<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                                <li>
                                                  <a href="${pageContext.request.contextPath}/marketingSalesDashboard">
                                                    <span class="sidebar-mini-icon">RT</span>
                                                    <span class="sidebar-normal"><spring:message code='header.marketingSalesDashboard' /></span>
                                                  </a>
                                                </li>

          									</sec:authorize>

          									 <sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                                <li>
                                                 <a href="${pageContext.request.contextPath}/sab">
                                                   <span class="sidebar-mini-icon">RT</span>
                                                   <span class="sidebar-normal"> Sales Board (alpha - internal)</span>
                                                 </a>
                                               </li>


                                               </sec:authorize>

                                               	<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                                     <li>
                                                         <a href="${pageContext.request.contextPath}/sks">
                                                              <span class="sidebar-mini-icon">RT</span>
                                                             <span class="sidebar-normal"> Supplier Key Stats (alpha - internal)</span>
                                                         </a>
                                                      </li>


                                                 </sec:authorize>

                                   </ul>
                                   </div>

               </li>




          <li>
             <a data-toggle="collapse" href="#marketingActivities">
                                          <i class="nc-icon nc-box"></i>
                                                 <p>
                                                 	<spring:message code='header.marketingActivities' />
                                                   <b class="caret"></b>
                                                 </p>
                                               </a>
                     <div class="collapse " id="marketingActivities">
                   <ul class="nav">
	                    <sec:authorize access="hasAnyRole('${auth['SKU_ADVERTISING_PERFORMANCE_REPORT']}')">
                                 <li>
                                                 <a href="${pageContext.request.contextPath}/sku-adv-performance-report">
                                                   <span class="sidebar-mini-icon">RT</span>
                                                   <span class="sidebar-normal"><spring:message code='marketingReport.skuAdvertisingPerformanceReport' /></span>
                                                 </a>
                                               </li>
          				</sec:authorize>
          						<sec:authorize access="hasAnyRole('${auth['SEARCH_TERM_REPORT']}')">

                                            <li>
                                                 <a  href="${pageContext.request.contextPath}/SearchTermReport">
                                                   <span class="sidebar-mini-icon">RT</span>
                                                   <span class="sidebar-normal"> <spring:message code='marketingReport.searchTermReport' /></span>
                                                 </a>
                                               </li>
          										</sec:authorize>
          										<sec:authorize access="hasAnyRole('${auth['SKU_ADV_EVAL_REPORT']}')">

                                                 <li>
                                                 <a href="${pageContext.request.contextPath}/sku-adv-eval-report">
                                                   <span class="sidebar-mini-icon">RT</span>
                                                   <span class="sidebar-normal"><spring:message code='marketingReport.skuAdvertisingEvaluationReport' /></span>
                                                 </a>
                                               </li>
          										</sec:authorize>
          										<sec:authorize access="hasAnyRole('${auth['SPONSORED_BRANDS_CAMPAIGN_REPORT']}')">
                                                <li>
                                                 <a  href="${pageContext.request.contextPath}/AmazonSponsoredBrandsCampaignReport">
                                                   <span class="sidebar-mini-icon">RT</span>
                                                   <span class="sidebar-normal"> <spring:message code='header.sponsoredBrandsCampaign' /></span>
                                                 </a>
                                               </li>
                                                </sec:authorize>
          										<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">

                                                  <li>
                                                  <a   href="${pageContext.request.contextPath}/ms">
                                                    <span class="sidebar-mini-icon">RT</span>
                                                    <span class="sidebar-normal"> Marketing Activity (internal)</span>
                                                  </a>
                                                </li>
                                                  </sec:authorize>

          										<sec:authorize access="hasAnyRole('${auth['CAMPAIGN_PERFORMANCE_REPORT_NAVIGATION']}')">

                                                  <li>
                                                  <a href="${pageContext.request.contextPath}/MarketingReports">
                                                    <span class="sidebar-mini-icon">RT</span>
                                                    <span class="sidebar-normal"><spring:message code='header.importMarketingReport' /></span>
                                                  </a>
                                                </li>
          										</sec:authorize>
                    </ul>

                    </div>
          </li>




        </ul>
      </div>
    </div>