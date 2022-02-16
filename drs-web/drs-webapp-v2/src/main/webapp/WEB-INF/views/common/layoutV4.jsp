<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
  <meta charset="utf-8" />
  <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <title></title>

  <link rel="apple-touch-icon" sizes="76x76" href="<c:url value='/resources/assets/img/apple-icon.png'/>">
  <link href="<c:url value='/resources/images/drs-favicon.ico'/>" rel="shortcut icon" type="image/vnd.microsoft.icon">

  <!-- Fonts and icons -->
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700,200" rel="stylesheet" />
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" rel="stylesheet">
  <!-- CSS Files -->
  <link href="<c:url value='/resources/assets/css/bootstrap.min.css'/>" rel="stylesheet" />
  <link href="<c:url value='/resources/assets/css/paper-dashboard.css?v=2.0.1'/>" rel="stylesheet" />
  <!-- CSS Just for demo purpose, don't include it in your project <link href="<c:url value='/resources/assets/demo/demo.css'/>" rel="stylesheet" />-->

  <script defer src="https://use.fontawesome.com/releases/v5.6.3/js/all.js"
    integrity="sha384-EIHISlAOj4zgYieurP0SdoiBYfGJKkgWedPHH4jCzpCXLmzVsw1ouK59MuUtP4a1" crossorigin="anonymous">
    </script>
  <script
  src="https://code.jquery.com/jquery-3.4.1.min.js"
  integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
  crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/floatthead/2.1.4/jquery.floatThead.js"></script>
  <style>
    body, button {
      /* font-family: "Open Sans", Helvetica, Arial, sans-serif, 微軟正黑體, Lato; */
      font-family: "Open Sans", "apple system", Helvetica, Arial, sans-serif, 微軟正黑體, Lato;
    }
    body {
      font-size: 15px;
    }
    .btn {
      font-size: 13px;
      margin: 6px 1px;
    }
    .card {
      padding: 3rem;
    }
    .card label {
      font-size: 15px;
      /* color: #66615B; */
      color: #535353;
      font-weight: 500;
      margin-bottom: 0;
    }
    .card .card-header {
      padding: 0 12px;
    }
    .card .btn {
      border-radius: 4px;
    }
    .card-title {
      /* font-weight: 700; */
      color: #2c2c2c;
    }
    .title-space {
      margin: 20px 0;
    }
    #marketplaceSelector {
      height: 34px !important;
    }
    .form-inline-wrapper {
      margin: 5px 0 5px 0 !important;
      display: flex;
    }
    .searchBtn {
      height: 34px;
      padding: 6px 12px;
      margin: 0;
      color: #fff;
      line-height: 1.2em;
    }
    .enhancement a {
			margin: 0 3px;
    }

    /* 2020.02.06 */
    h6 {
      color: #535353;
      font-size: 16px;
    }
    /* .mb-1 {
      margin-top: 0.8rem !important;
    } */
    /* .card-first-area {
      padding-top: 1px;
      margin-left: 24px;
      margin-bottom: 24px;
    } */
    .control-label {
      padding: 5px 0;
      line-height: 1.6em;
    }
    .form-inline {
      padding: 5px 0;
    }
    .btn-align {
      margin: 0;
    }
    #startDateInput, #endDateInput {
      height: 34px;
      width: 108px;
    }

    /* about selector */
   
    /* about table */
    table tbody+tbody {
      border: none !important;
    }
    table td {
      border: none !important;
    }
    table.dataTable tbody tr.child span.dtr-title {
      min-width: 200px;
    }
    table.dtatTable tbody tr.child span.dtr-data {
      padding-right: 15px;
    }
    table.dataTable tbody tr.child ul li {
      display: flex;
      justify-content: space-between;
    }
    table tbody tr td {
      color: #141414;
    }
    thead .border-top {
      border-top: 1px solid #dee2e6;
    }
    thead .border-bottom {
      border-bottom: 1px solid #dee2e6;
    }
    thead tr.bg {
      background-color: #f9f9f9;
    }

    tbody .border-bottom {
      border-bottom: 1px solid #dee2e6;
    }
    #tableHeader {
      background-color: #fff;
      color: #535353;
    }
    table tbody {
      color: #141414;
      font-size: 14px;
    }
    table.dataTable {
      border-collapse: collapse !important; 
    }
    table.dataTable {    
      width: 100%;
      margin-top: 0px !important;
      margin-bottom: 0px !important;
    }
    /* table.dataTable tbody,tfoot {
      border-color: transparent; !important
    } */
    table.dataTable thead th {
      position: relative;
      background-image: none !important;
    }
    table.dataTable thead th, table.dataTable thead td {
      border-bottom: 0px !important;
    }
    table.dataTable tfoot th, table.dataTable tfoot td {
      border-top: 1px solid #dee2e6 !important;
    }
    table.dataTable thead th.sorting:after {
      content: "\f0dc" !important;
    }
    table.dataTable thead th.sorting_asc:after {
      content: "\f0de" !important;
    }
    table.dataTable thead th.sorting_desc:after {
      content: "\f0dd" !important;
    }
    table.dataTable.no-footer {
      border-bottom: 0px solid #111;
    }
    table.dataTable thead > tr > th.sorting_asc, table.dataTable thead > tr > th.sorting_desc, table.dataTable thead > tr > th.sorting, table.dataTable thead > tr > td.sorting_asc, table.dataTable thead > tr > td.sorting_desc, table.dataTable thead > tr > td.sorting {
      padding-right: 20px;
    }
    table.dataTable tbody tr {
      background-color:#ffffff
    }
    table.dataTable tbody tr.selected {
      background-color:#B0BED9
    }
    table.dataTable tfoot th, table.dataTable tfoot td {
      padding: 8px 10px !important;
      border-top: 2px solid #ccc;
    }
    table.fixedHeader-floating {
      top:48px !important;
    }
    table.dataTable thead th {
      border-bottom: 1px solid #eeeded;
      padding: 0;
    }
    .table > tbody > tr > td.dataTables_empty {
      border-top:0px;
    }
    .table-width {
      width: 100% !important;
    }
    h4 {
      margin-top:10px !important;
    }
    /* dataTable page info css */
    div.dataTables_wrapper div.dataTables_paginate ul.pagination li.active a {
      color: #fff;
      background-color: #51cbce;
      box-shadow: 0 2px 2px rgba(204, 197, 185, 0.5);
    }

    div.dataTables_wrapper div.dataTables_paginate ul.pagination li.active a:hover {
      background-color: #51cbce !important;
      box-shadow: 0 2px 2px rgba(204, 197, 185, 0.5) !important;
    }

    div.dataTables_wrapper div.dataTables_paginate ul.pagination li.paginate_button a {
      border-radius: 15px;
      border: 0px;
      display: flex;
      justify-content: center;
      align-items: center;
      min-width: 25px !important;
      min-height: 26px !important;
    }

    div.dataTables_wrapper div.dataTables_paginate ul.pagination li.paginate_button a:hover {
      background-color: #dee2e6 ;
      border-radius: 15px;
      border: 0;
      box-shadow: none;
    }

    div.dataTables_wrapper div.dataTables_paginate ul.pagination li.paginate_button {
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 15px;
      padding: 0 !important;
      margin-left: 11px !important;
      min-width: 0px !important;
      border: none !important;
      background-color: transparent;
    }

    .dataTables_wrapper .dataTables_paginate ul.pagination li.paginate_button:hover {
      background: none;
    }

    .dataTables_wrapper .dataTables_paginate ul.pagination li.current:hover {
      border: none;
      background: none;
    }
    
    #searchTermReport_previous a,
    #searchTermReport_next a {
      padding: 2px 8px;
    }
    
    #searchTermReport_previous a:hover,
    #searchTermReport_next a:hover {
      border-radius: 12px;
    }
    .border-b tr {
      border-bottom: 1px solid rgba(128,128,128,.1);
    }
    .padding-reset td {
      padding-top: 8px !important;
      padding-bottom: 8px !important;
    }
    .pl-td {
      padding-left: 50px !important;
    }
    .mr-td {
      padding-right: 20px !important;
    }
    .clickableRow {
      cursor: pointer;
    }
    .clickableRow:hover {
      /* background-color: #f1f1f1; */
      background-color: #dddfde;
    }
    .btn-color {
      color: #66615B !important;
    }
    .btn-inside-table {
      padding: 6px 11px !important;
      border: 1px solid #66615B !important;
    }
    .btn-inside-table:active,
    .btn-inside-table:focus {
      background-color: transparent !important;
      color: #66615B !important;
    }
    .tb-with-space {
      padding: 11px !important;
    }
    .tb-with-bg {
      /* background-color: rgba(204, 197, 185, 0.5); */
      background-color: rgba(237,237,237, .36);
    }
    .pr-reset-td {
      padding-right: 25px !important;
    }
    .tb-pb-reset {
      padding-bottom: 0px !important;
    }
    .tb-border-around {
      border-right: 1.3px solid rgba(128,128,128,.1);
      border-left: 1.3px solid rgba(128,128,128,.1);
    }
    .tb-radius {
      border-radius: 12px;
    }
    /* table column width reset */
    table.dataTable tbody td.pd-reset {
      padding: 8px 6px !important;
      min-width: 55px;
      max-width: 75px;
      max-height: 108px;
    }
    table.dataTable thead th.pd-reset {
      padding: 10px 6px !important;
      min-width: 55px;
      max-width: 75px;
      max-height: 108px;
    }
    table.dataTable thead th.pd-wider {
      padding: 10px 6px !important;
      min-width: 75px;
      max-width: 90px;
      max-height: 108px;
    }
    .ts-s {
      font-size: 5px !important;
    }
    .pr-2 {
      padding-right: 65px !important;
    }
    .dataTables_wrapper.no-footer .dataTables_scrollBody {
      border-bottom: 1px solid #dee2e6 !important;
    }
    table.dataTable.no-footer {
      border-bottom: 1px solid #dee2e6 !important;
    }
    thead.pd-reset th {
      padding: 10px 20px 10px 10px !important;
    }
    tbody.bb-reset {
      border-bottom: 1px solid #dee2e6;
    }
    .breadcrumb {
      background-color: transparent !important;
      padding-left: 33px !important;
    }
    .breadcrumb span {
      color: #535353;
    }
    .pl-20 {
      padding-left: 20px !important;
    }
    .pl-18 {
      padding-left: 18px !important;
    }
    .pr-18 {
      padding-right: 18px !important;
    }
    .pt-15 {
      padding-top: 15px !important;
    }
    .bnext {
      width: 10px;
      height: 10px;
      margin: 0px 10px;
      display: inline;
    }
    .statement-info {
      line-height: 25px;
      font-size: 14px;
      /* color: #535353; */
      letter-spacing: .25px;
      padding-bottom: 25px;
      padding-right: 23px;
    }
    table tr.padding-reset td {
      padding-top: 8px !important;
      padding-bottom: 8px !important;
    }
    table tbody.padding-reset td {
      padding-top: 8px !important;
      padding-bottom: 8px !important;
    }
    select,
    select option {
      cursor: pointer;
    }
  </style>
</head>

<body>
  <div class="wrapper ">
    <tiles:insertAttribute name="sidebar" />
    <div class="main-panel">
      <tiles:insertAttribute name="navbar" />
        <div class="content">
          <!-- body -->
          <tiles:insertAttribute name="body" />
          <!-- end body -->
        </div>
      <tiles:insertAttribute name="footer" />
    </div>
  </div>

  <!-- Core JS Files -->
  <!-- <script src="<c:url value="/resources/assets/js/core/jquery.min.js"/>"></script> -->
  <script src="<c:url value='/resources/assets/js/core/popper.min.js'/>"></script>
  <script src="<c:url value='/resources/assets/js/core/bootstrap.min.js'/>"></script>
  <script src="<c:url value='/resources/assets/js/plugins/perfect-scrollbar.jquery.min.js'/>"></script>
  <script src="<c:url value='/resources/assets/js/plugins/moment.min.js'/>"></script>
  <script src="<c:url value='/resources/assets/js/plugins/moment-timezone-with-data.js'/>"></script>
  <!-- Plugin for Switches, full documentation here: http://www.jque.re/plugins/version3/bootstrap.switch/ -->
  <script src="<c:url value='/resources/assets/js/plugins/bootstrap-switch.js'/>"></script>
  <!-- Plugin for Sweet Alert -->
  <script src="<c:url value='/resources/assets/js/plugins/sweetalert2.min.js'/>"></script>
  <!-- Forms Validations Plugin -->
  <script src="<c:url value='/resources/assets/js/plugins/jquery.validate.min.js'/>"></script>
  <!-- Plugin for the Wizard, full documentation here: https://github.com/VinceG/twitter-bootstrap-wizard -->
  <script src="<c:url value='/resources/assets/js/plugins/jquery.bootstrap-wizard.js'/>"></script>
  <!-- Plugin for Select, full documentation here: http://silviomoreto.github.io/bootstrap-select -->
  <script src="<c:url value='/resources/assets/js/plugins/bootstrap-selectpicker.js'/>"></script>
  <!-- Plugin for the DateTimePicker, full documentation here: https://eonasdan.github.io/bootstrap-datetimepicker/ -->
  <script src="<c:url value='/resources/assets/js/plugins/bootstrap-datetimepicker.js'/>"></script>
  <!-- DataTables.net Plugin, full documentation here: https://datatables.net/ -->
  <script src="<c:url value='/resources/assets/js/plugins/jquery.dataTables.min.js'/>"></script>
  <!-- Plugin for Tags, full documentation here: https://github.com/bootstrap-tagsinput/bootstrap-tagsinputs -->
  <script src="<c:url value='/resources/assets/js/plugins/bootstrap-tagsinput.js'/>"></script>
  <!-- Plugin for Fileupload, full documentation here: http://www.jasny.net/bootstrap/javascript/#fileinput -->
  <script src="<c:url value='/resources/assets/js/plugins/jasny-bootstrap.min.js'/>"></script>
  <!-- Full Calendar Plugin, full documentation here: https://github.com/fullcalendar/fullcalendar -->
  <script src="<c:url value='/resources/assets/js/plugins/fullcalendar.min.js'/>"></script>
  <!-- Vector Map plugin, full documentation here: http://jvectormap.com/documentation/ -->
  <script src="<c:url value='/resources/assets/js/plugins/jquery-jvectormap.js'/>"></script>
  <!-- Plugin for the Bootstrap Table -->
  <script src="<c:url value='/resources/assets/js/plugins/nouislider.min.js'/>"></script>
  <!-- Google Maps Plugin -->
  <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
  <!-- Chart JS -->
  <script src="<c:url value='/resources/assets/js/plugins/chartjs.min.js'/>"></script>
  <!-- Notifications Plugin -->
  <script src="<c:url value='/resources/assets/js/plugins/bootstrap-notify.js'/>"></script>

  <!-- Control Center for Now Ui Dashboard: parallax effects, scripts for the example pages etc -->
  <script src="<c:url value='/resources/assets/js/paper-dashboard.js?v=2.0.1'/>" type="text/javascript"></script>

  <!-- <script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
  <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
  <link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.bootstrap.min.css"> -->
  <script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
  <script src="https://cdn.datatables.net/fixedcolumns/3.2.2/js/dataTables.fixedColumns.min.js"></script>	
  <script src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>
  <link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
  <script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
  <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
  <link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.2.2/css/buttons.bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.2/css/fixedHeader.dataTables.min.css">
  <script src="https://cdn.datatables.net/fixedheader/3.1.2/js/dataTables.fixedHeader.min.js"></script>
  <script src="https://cdn.datatables.net/plug-ins/1.10.16/sorting/absolute.js"></script>

  <!-- Paper Dashboard DEMO methods, don't include it in your project! -->
  <script src="<c:url value='/resources/assets/demo/demo.js'/>"></script>
  <script>
    $(document).ready(function() {
      // Javascript method's body can be found in assets/js/demos.js
      // demo.initDashboardPageCharts();
      // demo.initVectorMap();
    });
  </script>
</body>

</html>
