<%@ page session="false"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<head>
  <title>
    <spring:message code="salesAndPageTrafficReport.title" /> - DRS
  </title>
  <link href="<c:url value='/resources/css/bootstrap-multiselect.css'/>" type="text/css" rel="stylesheet">
  <script type="text/javascript" src="<c:url value='/resources/js/jquery.floatThead.js'/>"></script>
  <script type="text/javascript" src="<c:url value='/resources/js/bootstrap-multiselect.js'/>"></script>
  <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/css/select2.min.css" rel="stylesheet" />
  <link href="https://cdnjs.cloudflare.com/ajax/libs/select2-bootstrap-theme/0.1.0-beta.10/select2-bootstrap.min.css"
    rel="stylesheet" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.3/js/select2.min.js"></script>

  <script>
    $(document).ready(function () {
      // when customized dateRange selected show input date boxes with datepicker plugin
      if ($('.selectDateRange').val() == 6) {
        $('.showOnCustomize').fadeTo(200, 1);
        $('#startDateInput,#endDateInput').prop('disabled', false);
      } else {
        $('.showOnCustomize').fadeTo(0, 0);
        $('#startDateInput,#endDateInput').prop('disabled', true);
      }
      $('.selectDateRange').on('change', function () {
        if ($(this).val() == 6) {
          $('.showOnCustomize').fadeTo(200, 1);
          $('#startDateInput,#endDateInput').prop('disabled', false);
        } else {
          $('.showOnCustomize').fadeTo(200, 0);
          $('#startDateInput,#endDateInput').prop('disabled', true);
        }
      });

      const maxDate = moment().subtract(2, "days").format("l");
      /* New datepicker */
      $("#startDateInput, #endDateInput").datetimepicker({
        format: "YYYY-MM-DD", // Originally'MM/DD/YYYY', changed to comply with DRS' date format
        icons: {
          time: "far fa-clock",
          date: "fas fa-calendar-alt",
          up: "fas fa-chevron-up",
          down: "fas fa-chevron-down",
          previous: "fas fa-chevron-left",
          next: "fas fa-chevron-right",
          today: "fa fa-screenshot",
          clear: "fa fa-trash",
          close: "fa fa-remove"
        },
        minDate: "2015-01-01",
        maxDate: maxDate/* ,
        debug: true */
      });

      /* $('#startDateInput,#endDateInput').datepicker({
        beforeShow: function () {
          setTimeout(function () {
            $('.ui-datepicker').css('z-index', 200);
          }, 0);
        },
        dateFormat: 'yy-mm-dd',
        changeYear: true,
        minDate: new Date(2015, 1 - 1, 1),
        maxDate: '-2'
      }).keyup(function (e) {
        if (e.keyCode == 8 || e.keyCode == 32 || e.keyCode == 46) {
          $(this).datepicker('setDate', null);
        }
      });
      $('#startDateInput,#endDateInput').attr('readonly', true); */

      /* New floatThead event
      Windows' y value is inverted to Unix like OSes */
      document.addEventListener("scroll", (e) => {
        const $table = $("#salesAndPageTrafficReport");
        const os = window.navigator.userAgent;
        if (os.indexOf("Linux") != -1 || os.indexOf("Mac") != -1) {
          if ($table.offset().top >= 0) {
            $table.floatThead();
          } else {
            $table.floatThead("destroy");
          }
        } else {
          if ($table.offset().top <= 0) {
            $table.floatThead();
          } else {
            $table.floatThead("destroy");
          }
        }
      }, true);

      /* if ($(window).width() >= 1024) {
        $('#salesAndPageTrafficReport').floatThead({
          scrollingTop: $("#s5_menu_wrap").height(),
          zIndex: 100
        });
        $('#salesAndPageTrafficReport').on("floatThead", function (e, isFloated, $floatContainer) {
          if (isFloated) {
            $floatContainer.addClass("table-floated");
          } else {
            $floatContainer.removeClass("table-floated");
          }
        });
      } */

      //	build and initialize baseproduct and sku dropdowns with multiselect
      $('#baseProduct').multiselect({
        disableIfEmpty: true,
        includeSelectAllOption: true,
        maxHeight: 200,
        buttonWidth: '100%',
        numberDisplayed: 1
      });
      $('#sku').multiselect({
        disableIfEmpty: true,
        includeSelectAllOption: true,
        nonSelectedText: 'None specified',
        maxHeight: 200,
        buttonWidth: '100%',
        numberDisplayed: 1
      });
      var conditionsJson = ${ conditionsJson };
      if (conditionsJson.supplierKcode == null) {
        $.ajax({
          type: 'get',
          url: '${pageContext.request.contextPath}/SalesAndPageTrafficReport/getProductSkus',
          contentType: "application/json; charset=utf-8",
          data: { productBase: conditionsJson.baseProduct },
          dataType: "json",
          success: function (SKUs) {
            buildSkuSelection(SKUs);
          }
        });
      } else {
        $.ajax({
          type: 'get',
          url: '${pageContext.request.contextPath}/SalesAndPageTrafficReport/getProductBases',
          contentType: "application/json; charset=utf-8",
          data: { supplierKcode: conditionsJson.supplierKcode },
          dataType: "json",
          success: function (productBases) {
            $('#baseProduct').empty();
            $.each(productBases, function (baseCode, productName) {
              $('<option>').val(baseCode).text(productName).appendTo('#baseProduct');
            });
            $('#baseProduct').multiselect('rebuild');
            var selectedProducts = ${ selectedProducts };
            $('#baseProduct').multiselect('select', selectedProducts);
            $('#baseProduct').multiselect('updateButtonText');

            $.ajax({
              type: 'get',
              url: '${pageContext.request.contextPath}/SalesAndPageTrafficReport/getProductSkus',
              contentType: "application/json; charset=utf-8",
              data: {
                productBase: conditionsJson.baseProduct,
              },
              dataType: "json",
              success: function (SKUs) {
                buildSkuSelection(SKUs);
              }
            });
          }
        });
      }
      $("#supplierKcode").change(function () {
        $.ajax({
          type: 'get',
          url: '${pageContext.request.contextPath}/SalesAndPageTrafficReport/getProductBases',
          contentType: "application/json; charset=utf-8",
          data: { supplierKcode: this.value },
          dataType: "json",
          success: function (productBases) {
            $('#baseProduct').empty();
            $('#sku').empty();
            $.each(productBases, function (baseCode, productName) {
              $('<option>').val(baseCode).text(productName).appendTo('#baseProduct');
            });
            $('#baseProduct').multiselect('rebuild');
            $('#sku').multiselect('rebuild');
          }
        });
      });
      $("#supplierKcode").select2({
        theme: "bootstrap"
      });

      $("#baseProduct").change(function () {
        if ($("#baseProduct").val() == null || $("#baseProduct").val().length != 1) {
          $('#sku').empty();
          $('#sku').multiselect('rebuild');
        } else {
          $.ajax({
            type: 'get',
            url: '${pageContext.request.contextPath}/SalesAndPageTrafficReport/getProductSkus',
            contentType: "application/json; charset=utf-8",
            data: { productBase: this.value },
            dataType: "json",
            success: function (SKUs) {
              buildSkuSelection(SKUs);
            }
          });
        }
      });
      function buildSkuSelection(SKUs) {
        $('#sku').empty();
        $.each(SKUs, function (baseCode, SKUName) {
          $('<option>').val(baseCode).text(SKUName).appendTo('#sku');
        });
        $('#sku').multiselect('rebuild');
        var selectedSkus = ${ selectedSkus };
        $('#sku').multiselect('select', selectedSkus);
        $('#sku').multiselect('updateButtonText');
      }

      //	build initialize dateRange dropdown according to timescale selected
      var dateRangeList = $('.dateRangeList');
      $('#timeScale').on('change', function () {
        displayTimeRanges($(this).val());
      });
      displayTimeRanges($('#timeScale').val());
      function displayTimeRanges(timescale) {
        var selected = $('.selectDateRange').val();
        $('.selectDateRange').empty();
        if (timescale == 1) {
          buildDateRangeFromIndex(0);
        } else if (timescale == 2) {
          buildDateRangeFromIndex(1);
        } else if (timescale == 3) {
          buildDateRangeFromIndex(3);
        }
        $('.selectDateRange').val(selected);
      }
      function buildDateRangeFromIndex(index) {
        for (var i = index; i < 6; i++) {
          $('<option>').val(dateRangeList.eq(i).val()).text(dateRangeList.eq(i).text()).appendTo('.selectDateRange');
        }
      }

      if ($("#conditionURL").val() == null) {
        $("#orderExport").hide();
      } else {
        $("#orderExport").show();
      }
      $("#orderExport").on('click', function (e) {
        e.preventDefault();
        if ($("#conditionURL").val() != null) {
          var conditionURL = $("#conditionURL").val().substring(25);
          $('#pageSalesIframe').attr('src',
            "${pageContext.request.contextPath}/SalesAndPageTrafficReport/export" + conditionURL);
        }
      });

      google.charts.load('current', { 'packages': ['corechart'] });
      google.charts.setOnLoadCallback(function () {
        $(function () {
          var trafficType = [], scale = [], title = [];
          trafficType[1] = '<spring:message code="salesAndPageTrafficReport.SESSIONS" />';
          trafficType[2] = '<spring:message code="salesAndPageTrafficReport.ORDERED_PRODUCT_SALES" />';
          trafficType[3] = '<spring:message code="salesAndPageTrafficReport.TOTAL_UNITS_ORDERED" />';
          scale[1] = '<spring:message code="salesAndPageTrafficReport.date" />'
            + ' (<spring:message code="salesAndPageTrafficReport.DAY" />)';
          scale[2] = '<spring:message code="salesAndPageTrafficReport.date" />'
            + ' (<spring:message code="salesAndPageTrafficReport.WEEK" />)';
          scale[3] = '<spring:message code="salesAndPageTrafficReport.date" />'
            + ' (<spring:message code="salesAndPageTrafficReport.MONTH" />)';
          title[1] = ' (<spring:message code="salesAndPageTrafficReport.TOTAL" />)';
          title[2] = ' (<spring:message code="salesAndPageTrafficReport.AVERAGE" />)';
          var conditions = ${ conditionsJson };
          var chartDataString = $('#chartDataString').val();
          if (chartDataString == "") {
            return;
          }
          var chartData = JSON.parse(chartDataString);
          var rowsData = [];
          for (i = 0; i < chartData.length; i++) {
            var rowData = [new Date(chartData[i]['date']), Number(chartData[i]['total'])];
            rowsData.push(rowData);
          }
          var data = new google.visualization.DataTable();
          data.addColumn('date', 'Day');
          data.addColumn('number', trafficType[conditions.trafficType]);
          data.addRows(rowsData);
          var options = {
            title: trafficType[conditions.trafficType] + title[conditions.totalOrAverage],
            legend: { position: 'right' },
            series: { 0: { color: '#3d85c6' } },
            hAxis: {
              title: scale[conditions.timeScale],
              format: 'yyyy/MM/dd',
              gridlines: { count: 10 }
            },
            vAxis: {
              title: trafficType[conditions.trafficType],
              viewWindow: { min: 0 },
              gridlines: { count: 5 }
            },
            chartArea: { left: 80, top: 50, width: '75%', height: '75%' },
            height: 400,
            trendlines: {
              0: {
                labelInLegend: '<spring:message code="salesAndPageTrafficReport.Trendline" />',
                type: 'linear',
                color: '#e69138',
                lineWidth: 3,
                opacity: 0.6,
                visibleInLegend: true
              }
            }
          };

          chart = new google.visualization.LineChart(document.getElementById('lineChart'));
          chart.draw(data, options);

          //toggle trendline visibility
          google.visualization.events.addListener(chart, 'click', function (target) {
            if (target.targetID == 'legendentry#1') {
              if (options.trendlines[0].lineWidth == 3) {
                options.trendlines[0].lineWidth = 0;
              } else {
                options.trendlines[0].lineWidth = 3;
              }
            }
            chart.draw(data, options);
          });
        });
      });
    });
  </script>
  <style>
    select {
      padding: 0.5rem !important;
    }

    button {
      margin: 0 !important;
      color: #fff !important;
    }

    #salesAndPageTrafficReportForm > div > div:nth-child(1) > div:nth-child(2) > span > div > button > b,
    #salesAndPageTrafficReportForm > div > div:nth-child(1) > div:nth-child(3) > span > div > button > b,
    div.bootstrap-datetimepicker-widget.dropdown-menu::before,
    div.bootstrap-datetimepicker-widget.dropdown-menu::after {
      display: none !important;
    }

    .select2-container--default .select2-selection--single {
      border: 1px solid #ccc;
      color: #555;
    }

    .multiselect {
      color: #555;
    }

    .card {
      padding: 2rem;
    }

    .btn-drs {
      padding: 0.6rem 1rem;
    }

    .text-heading {
      font-size: 1.25rem;
      font-weight: bold;
    }

    .text-heading-sm {
      font-size: 1rem;
      font-weight: bold;
    }

    .mt-45 {
      margin-top: 2.5rem;
    }

    .mt-6 {
      margin-top: 3.5rem;
    }
  </style>
</head>

<div class="max-width">
  <div class="container-fluid">
    <div class="card">
      <div class="card-body">
        <!-- <div class="row">
          <div class="col-md-12">
            <div class="page-heading">
              <spring:message code="salesAndPageTrafficReport.title" />
            </div>
          </div>
        </div> -->
        <div class="row">
          <div class="col-md-12">
            <form:form action="${pageContext.request.contextPath}/SalesAndPageTrafficReport" method="GET"
              name="SalesAndPageTrafficReport" id="salesAndPageTrafficReportForm"
              modelAttribute="SalesAndPageTrafficReport">
              <div class="row">
                <div class="col-12 col-md-6">
                  <sec:authorize access="hasAnyRole('${auth['SALES_AND_PAGE_TRAFFIC_SUPPLIER_SELECTOR']}')">
                    <div class="form-group">
                      <label for="supplierKcode">
                        <spring:message code="salesAndPageTrafficReport.supplier" /></label>
                      <form:select id="supplierKcode" class="form-control" path="supplierKcode">
                        <form:option value="">--- Select ---</form:option>
                        <c:forEach var="supplierKcodeName" items="${supplierKcodeNames}">
                          <form:option value="${supplierKcodeName.key}">${supplierKcodeName.key} ${supplierKcodeName.value}
                          </form:option>
                        </c:forEach>
                      </form:select>
                    </div>
                  </sec:authorize>

                  <div class="form-group">
                    <label for="baseProduct">
                      <spring:message code="salesAndPageTrafficReport.BPandSKU" /></label>
                    <form:select id="baseProduct" multiple="true" class="form-control" path="baseProduct">
                      <c:forEach var="productBase" items="${productBases}">
                        <form:option value="${productBase.key}">${productBase.value}</form:option>
                      </c:forEach>
                    </form:select>
                  </div>

                  <div class="form-group">
                    <label for="sku">
                      <spring:message code="salesAndPageTrafficReport.SKU" /></label>
                    <form:select multiple="true" id="sku" class="form-control" path="sku">
                    </form:select>
                  </div>

                  <div class="form-group">
                    <label for="marketplaceId">
                      <spring:message code="salesAndPageTrafficReport.marketplace" /></label>
                    <form:select class="form-control" path="marketplaceId">
                      <form:option value="">--- Select ---</form:option>
                      <c:forEach var="marketplace" items="${marketplaces}">
                        <form:option value="${marketplace.key}">${marketplace.name}</form:option>
                      </c:forEach>
                    </form:select>
                  </div>
                </div>
                <div class="col-12 col-md-6">

                  <div class="form-group">
                    <label for="timeScale">
                      <spring:message code="salesAndPageTrafficReport.timeScale" /></label>
                    <form:select class="form-control" path="timeScale">
                      <c:forEach var="timeScale" items="${timeScales}">
                        <form:option value="${timeScale.key}">
                          <spring:message code="salesAndPageTrafficReport.${timeScale}" />
                        </form:option>
                      </c:forEach>
                    </form:select>
                  </div>

                  <div class="form-group">
                    <label for="trafficType">
                      <spring:message code="salesAndPageTrafficReport.trafficType" /></label>
                    <form:select id="trafficType" class="form-control" path="trafficType">
                      <c:forEach var="trafficType" items="${trafficTypes}">
                        <form:option value="${trafficType.key}">
                          <spring:message code="salesAndPageTrafficReport.${trafficType}" />
                        </form:option>
                      </c:forEach>
                    </form:select>
                  </div>

                  <div class="form-group">
                    <label for="dateRange">
                      <spring:message code="salesAndPageTrafficReport.timeRange" /></label>
                    <form:select id="dateRange" class="form-control selectDateRange" path="dateRange">
                      <c:forEach var="dateRange" items="${dateRanges}">
                        <form:option class="dateRangeList" value="${dateRange.key}">
                          <spring:message code="salesAndPageTrafficReport.${dateRange}" />
                        </form:option>
                      </c:forEach>
                    </form:select>
                  </div>

                  <div class="row">
                    <!-- Start, end date -->
                    <div class="col-6">
                      <div class="showOnCustomize form-group">
                        <label>
                          <spring:message code="campaignPerformance.startDateInput" /></label>
                        <form:input class="form-control" id="startDateInput" path="startDate"
                          style="width:100px;cursor:default;background-color:white;"></form:input>
                      </div>
                      <div class="showOnCustomize form-group">
                        <label>
                          <spring:message code="campaignPerformance.endDateInput" /></label>
                        <form:input class="form-control" id="endDateInput" path="endDate"
                          style="width:100px;cursor:default;background-color:white;"></form:input>
                      </div>
                    </div>

                    <!-- Total or average radio groups -->
                    <div class="col-6">
                      <div class="form-check">
                        <span class="d-flex align-items-center mt-45 mb-5">
                          <form:radiobutton class="totalOrAverage" path="totalOrAverage" value="1" />
                          <label class="mb-0 ml-1">
                            <spring:message code="salesAndPageTrafficReport.TOTAL" /></label>
                        </span>
                        <span id="radioTotalAvg" class="d-flex align-items-center mt-6">
                          <form:radiobutton class="totalOrAverage" path="totalOrAverage" value="2" />
                          <label class="mb-0 ml-1">
                            <spring:message code="salesAndPageTrafficReport.AVERAGE" /></label>
                        </span>
                      </div>
                    </div>
                  </div>

                  <!-- Warning message -->
                  <div class="my-3">
                    <c:if test="${empty report}">
                      <small class="form-text">
                        <spring:message code="salesAndPageTrafficReport.noInfoFound" />
                        <spring:message code="salesAndPageTrafficReport.searchAgain" />
                      </small>
                    </c:if>
                  </div>

                  <!-- Search and export buttons -->
                  <div class="d-flex flex-row justify-content-end align-items-center">
                    <input type="hidden" id="selectedSkus" class="ml-2" name="selectedSkus" value="${selectedSkus}" />
                    <input type="hidden" id="selectedProducts" class="ml-2" name="selectedProducts" value="${selectedProducts}" />
                    <button class="btn btn-primary btn-drs ml-2" type="submit"><i class="fas fa-search"></i>
                      <spring:message code="salesAndPageTrafficReport.search" /></button>
                    <input id="orderExport" class="btn btn-default btn-drs ml-2" type="submit"
                      value="<spring:message code='customerOrder.export'/>" />
                  </div>
                  <iframe id="pageSalesIframe" style="border:none;height:0;width:0;"></iframe>
                </div>
              </div>
            </form:form>
          </div>
        </div>

        <hr class="mt-0">

        <!-- Total, average -->
        <div class="row d-flex justify-content-between align-items-center my-4">
          <div class="col-md-6">
            <c:choose>
              <c:when test="${conditions.totalOrAverage eq 1}">
                <h5 class="text-heading m-0">
                  <spring:message code="salesAndPageTrafficReport.TOTAL" /></h5>
              </c:when>
              <c:when test="${conditions.totalOrAverage eq 2}">
                <h5 class="text-heading m-0">
                  <spring:message code="salesAndPageTrafficReport.AVERAGE" /></h5>
              </c:when>
            </c:choose>
          </div>
          <div class="col-md-6 text-right">
            <spring:message code="salesAndPageTrafficReport.dateRange" /> ${report.startDate} to ${report.endDate}
          </div>
        </div>

        <!--  -->
        <div class="row">
          <div class="col-md-12">
            <table class="table table-withoutBorder">
              <tr class="text-heading-sm bg-light">
                <td class="border-top-0">
                  <spring:message code="salesAndPageTrafficReport.totalUnitsOrdered" />
                  ${report.totalUnitsOrdered}
                </td>
                <td class="text-center border-top-0">
                  <spring:message code="salesAndPageTrafficReport.orderProductSales" />
                  ${report.currency} ${report.totalOrderedProductSales}
                </td>
                <td class="text-right border-top-0">
                  <spring:message code="salesAndPageTrafficReport.totalSessions" />
                  ${report.totalSessions}
                </td>
              </tr>
            </table>
          </div>
        </div>

        <!-- Charts -->
        <div class="row">
          <div class="col-md-12">
            <input type="hidden" id="chartDataString" name="chartDataString" value='${chartData}' />
            <div id="lineChart"></div>
          </div>
        </div>

        <hr class="mt-5 mb-4">

        <!-- Table and pagination -->
        <div class="row overflow-hidden">
          <div class="col-md-12 overflow-hidden">
            <c:choose>
              <c:when test="${totalPages > 0}">
                <c:url value="SalesAndPageTrafficReport" var="URL">
                  <c:forEach var="condition" items="${conditions}">
                    <c:param name="${condition.key}" value="${condition.value}" />
                  </c:forEach>
                </c:url>
                <input type="hidden" id="conditionURL" name="conditionURL" value="${URL}" />
              </c:when>
            </c:choose>
            <c:choose>
              <c:when test="${totalPages > 1}">
                <c:url var="firstUrl" value="${URL}&page=1" />
                <c:url var="lastUrl" value="${URL}&page=${totalPages}" />
                <c:url var="prevUrl" value="${URL}&page=${currentPageIndex - 1}" />
                <c:url var="nextUrl" value="${URL}&page=${currentPageIndex + 1}" />
                <center>
                  <nav>
                    <ul class="pagination pagination-sm justify-content-center">
                      <c:choose>
                        <c:when test="${currentPageIndex != 1}">
                          <li class="page-item"><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
                          <li class="page-item"><a class="page-link" href="${prevUrl}">&lt;</a></li>
                        </c:when>
                      </c:choose>
                      <c:forEach var="i" begin="${startPage}" end="${endPage}">
                        <c:url var="pageUrl" value="${URL}&page=${i}" />
                        <c:choose>
                          <c:when test="${i == currentPageIndex}">
                            <li class="active page-item"><a class="page-link" href="${pageUrl}">
                                <c:out value="${i}" /></a></li>
                          </c:when>
                          <c:otherwise>
                            <li class="page-item"><a class="page-link" href="${pageUrl}">
                                <c:out value="${i}" /></a></li>
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>
                      <c:choose>
                        <c:when test="${currentPageIndex != totalPages}">
                          <li class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
                          <li class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
                        </c:when>
                      </c:choose>
                    </ul>
                  </nav>
                </center>
              </c:when>
            </c:choose>

            <!-- Table top -->
            <div class="overflow-hidden mt-3">
              <table id="salesAndPageTrafficReport" class="table table-withoutBorder table-striped overflow-hidden">
                <thead class="bg-white">
                  <tr>
                    <th>
                      <spring:message code="salesAndPageTrafficReport.date" />
                    </th>
                    <th class="text-right">
                      <spring:message code="salesAndPageTrafficReport.Sessions" />
                    </th>
                    <th class="text-right">
                      <spring:message code="salesAndPageTrafficReport.pageView" />
                    </th>
                    <th class="text-right">
                      <spring:message code="salesAndPageTrafficReport.buybox" />
                    </th>
                    <th class="text-right">
                      <spring:message code="salesAndPageTrafficReport.unitSession" />
                    </th>
                    <th class="text-right">
                      <spring:message code="salesAndPageTrafficReport.orderedProductSales" /> (${report.currency})</th>
                    <th class="text-right">
                      <spring:message code="salesAndPageTrafficReport.unitsOrdered" />
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${historyLines}" var="historyLine">
                    <tr>
                      <td>${historyLine.date}</td>
                      <td class="text-right">${historyLine.sessions}</td>
                      <td class="text-right">${historyLine.pageViews}</td>
                      <td class="text-right">${historyLine.buyBoxPercentage}</td>
                      <td class="text-right">${historyLine.unitSessionPercentage}</td>
                      <td class="text-right">${historyLine.orderedProductSales}</td>
                      <td class="text-right">${historyLine.unitsOrdered}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
            <c:choose>
              <c:when test="${totalPages > 1}">
                <c:url value="SalesAndPageTrafficReport" var="URL">
                  <c:forEach var="condition" items="${conditions}">
                    <c:param name="${condition.key}" value="${condition.value}" />
                  </c:forEach>
                </c:url>
                <c:url var="firstUrl" value="${URL}&page=1" />
                <c:url var="lastUrl" value="${URL}&page=${totalPages}" />
                <c:url var="prevUrl" value="${URL}&page=${currentPageIndex - 1}" />
                <c:url var="nextUrl" value="${URL}&page=${currentPageIndex + 1}" />
                <center>
                  <nav>
                    <ul class="pagination pagination-sm justify-content-center">
                      <c:choose>
                        <c:when test="${currentPageIndex != 1}">
                          <li class="page-item"><a class="page-link" href="${firstUrl}">&lt;&lt;</a></li>
                          <li class="page-item"><a class="page-link" href="${prevUrl}">&lt;</a></li>
                        </c:when>
                      </c:choose>
                      <c:forEach var="i" begin="${startPage}" end="${endPage}">
                        <c:url var="pageUrl" value="${URL}&page=${i}" />
                        <c:choose>
                          <c:when test="${i == currentPageIndex}">
                            <li class="active page-item"><a class="page-link" href="${pageUrl}">
                                <c:out value="${i}" /></a></li>
                          </c:when>
                          <c:otherwise>
                            <li class="page-item"><a class="page-link" href="${pageUrl}">
                                <c:out value="${i}" /></a></li>
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>
                      <c:choose>
                        <c:when test="${currentPageIndex != totalPages}">
                          <li class="page-item"><a class="page-link" href="${nextUrl}">&gt;</a></li>
                          <li class="page-item"><a class="page-link" href="${lastUrl}">&gt;&gt;</a></li>
                        </c:when>
                      </c:choose>
                    </ul>
                  </nav>
                </center>
              </c:when>
            </c:choose>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
