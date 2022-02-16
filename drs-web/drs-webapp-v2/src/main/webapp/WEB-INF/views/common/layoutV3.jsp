<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page contentType="text/html; charset=utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />		
		<meta http-equiv="Cache-Control" content="no-cache, must-revalidate, no-store" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0">        
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
           <!-- <link href="/resources/bundle/css/navbar.css" rel="stylesheet" /> -->
        <link href="<c:url value="/resources/images/drs-favicon.ico"/>" rel="shortcut icon" type="image/vnd.microsoft.icon">

        <!-- design_control -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css" integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr" crossorigin="anonymous">
        <link href="<c:url value="https://fonts.googleapis.com/css?family=Montserrat"/>" rel="stylesheet">
   	 	<!-- Global site tag (gtag.js) - Google Analytics -->
		<script async src="https://www.googletagmanager.com/gtag/js?id=UA-71702187-2"></script>
		<script>
  			window.dataLayer = window.dataLayer || [];
  			function gtag(){dataLayer.push(arguments);}
  			gtag('js', new Date());
  			gtag('config', 'UA-71702187-2');
		</script>                
        <!-- Begin Inspectlet Embed Code -->
		<script type="text/javascript" id="inspectletjs">
			window.__insp = window.__insp || [];
			__insp.push(['wid', 1371674819]);
			(function() {
			function ldinsp(){if(typeof window.__inspld != "undefined") return; window.__inspld = 1; var insp = document.createElement('script'); insp.type = 'text/javascript'; insp.async = true; insp.id = "inspsync"; insp.src = ('https:' == document.location.protocol ? 'https' : 'http') + '://cdn.inspectlet.com/inspectlet.js'; var x = document.getElementsByTagName('script')[0]; x.parentNode.insertBefore(insp, x); };
			setTimeout(ldinsp, 500); document.readyState != "complete" ? (window.attachEvent ? window.attachEvent('onload', ldinsp) : window.addEventListener('load', ldinsp, false)) : ldinsp();
			})();
		</script>
		<!-- End Inspectlet Embed Code -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">        
        <link href="<c:url value="/resources/css/drsV2.css"/>" type="text/css" rel="stylesheet">         
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">

         <!-- <link href="/resources/bundle/css/app.css" rel="stylesheet" /> -->
         <!-- <link href="/resources/bundle/css/notification.css" rel="stylesheet" /> -->
         <link href="/resources/bundle/css/navbar.css" rel="stylesheet" />

		<!-- Start of drs Zendesk Widget script -->
		<script type="text/javascript">
			window.zESettings = {
				webWidget: {
					contactForm: {
						fields: [
					    	{ id: 360012745273, prefill: { '*': window.location.href } }
					    ],
					    tags: ['${user.companyKcode}']
					}
				}
			};
		</script>
		<script id="ze-snippet" src="https://static.zdassets.com/ekr/snippet.js?key=a555fe59-4104-4a83-af14-e4fa478f17db"> </script>
		<script>
	  		zE(function() {
	    		zE.setLocale('${userLocaleZendesk}');
	    		zE.identify({
	    			name: '${user.userDisplayName} - (${user.companyKcode})',
	    			email: '${userEmail}',
	    			organization: '${user.companyKcode}'
	    		});
	  		});
		</script>
		<sec:authorize access="hasAnyRole('${auth['DASH_BOARD_DRS_STAFF']}')">
		<style>
		#cs {
		   /* Hidden by default  display: none;*/
	 		position: fixed; /* Fixed/sticky position */
     		bottom: 10px; /* Place the button at the bottom of the page */
		  right: 20px; /* Place the button 30px from the right */
		  z-index: 99; /* Make sure it does not overlap */
		  border: none; /* Remove borders */
		  outline: none; /* Remove outline */
		  background-color: pink; /* Set a background color */
		  color: white; /* Text color */
		  cursor: pointer; /* Add a mouse pointer on hover */
		  border-radius: 10px; /* Rounded corners */
		  font-size: 12px; /* Increase font size */
		}
		
		#cs:hover {
			background-color: #555; /* Add a dark-grey background on hover */
		}
		</style>
		</sec:authorize>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js" integrity="sha256-VazP97ZCwtekAsvgPBSUwPFKdrwD3unUfSGVYrahUqU=" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
<style>
	img {
		max-width: 100%;
		max-height: 100%;
		height: auto;
		display: block;
	}
	.nav {
		position: relative !important;
	}
	.nav-link {
		display: inline !important;
	}
	#header {
		z-index: 900 !important;
	}
</style>
	</head>
 	<body>
		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="body" />
  		<tiles:insertAttribute name="footer" />
    </body>
</html>