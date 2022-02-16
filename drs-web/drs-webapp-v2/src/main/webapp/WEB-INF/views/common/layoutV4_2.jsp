<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page contentType="text/html; charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>

 <meta charset="utf-8" />
  <link rel="apple-touch-icon" sizes="76x76" href="<c:url value="/resources/assets/img/apple-icon.png"/>">
  <link rel="icon" type="image/png" href="<c:url value="/resources/assets/img/favicon.png"/>">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <title>
  </title>
  <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no' name='viewport' />
  <!--     Fonts and icons     -->
  <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700,200" rel="stylesheet" />
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" rel="stylesheet">
  <!-- CSS Files -->
  <link href="<c:url value="/resources/assets/css/bootstrap.min.css"/>" rel="stylesheet" />
  <link href="<c:url value="/resources/assets/css/paper-dashboard.css?v=2.0.0"/>" rel="stylesheet" />
  <!-- CSS Just for demo purpose, don't include it in your project -->

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








  <!--   Core JS Files   -->
  <script src="<c:url value="/resources/assets/js/core/jquery.min.js"/>"></script>
  <script src="<c:url value="/resources/assets/js/core/popper.min.js"/>"></script>
  <script src="<c:url value="/resources/assets/js/core/bootstrap.min.js"/>"></script>
  <script src="<c:url value="/resources/assets/js/plugins/perfect-scrollbar.jquery.min.js"/>"></script>
  <!--  Google Maps Plugin    -->
  <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
  <!-- Chart JS -->
  <script src="<c:url value="/resources/assets/js/plugins/chartjs.min.js"/>"></script>
  <!--  Notifications Plugin    -->
  <script src="<c:url value="/resources/assets/js/plugins/bootstrap-notify.js"/>"></script>
  <!-- Control Center for Now Ui Dashboard: parallax effects, scripts for the example pages etc -->
  <script src="<c:url value="/resources/assets/js/paper-dashboard.min.js?v=2.0.0"/>" type="text/javascript"></script>

  
<script src="<c:url value="/resources/assets/js/plugins/jquery.dataTables.min.js"/>"></script>




  <!-- Paper Dashboard DEMO methods, don't include it in your project! -->
  <script src="<c:url value="/resources/assets/demo/demo.js"/>"></script>
  <script>
    $(document).ready(function() {
      // Javascript method's body can be found in assets/assets-for-demo/js/demo.js
      demo.initChartsPages();
    });
  </script>


    </body>



</html>