<%--
 *
 *  Copyright 2011 - Sardegna Ricerche, Distretto ICT, Pula, Italy
 *
 * Licensed under the EUPL, Version 1.1.
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *  http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in  writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 * In case of controversy the competent court is the Court of Cagliari (Italy).
--%>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<link rel='stylesheet' type='text/css' href='css/reset.css' />
	<link rel="stylesheet" type="text/css" href="css/colorbox.css" />
	<link rel='stylesheet' type='text/css' href='css/south-street/jquery-ui-1.8.9.custom.css' />
	<link rel='stylesheet' type='text/css' href='css/jquery.jgrowl.css' />
	<link rel="stylesheet" type='text/css' href="css/jquery.fileupload-ui.css" />
	<link rel="stylesheet" type="text/css" href="css/layout_sliding_door.css" />
  	<link rel="shortcut icon" type="image/x-icon" href="images/favicon.png">
	<title><s:text name="titleExtended"/></title>

	<!-- (de) Fuegen Sie hier ihre Meta-Daten ein -->
  	<!--[if lte IE 7]>
		<link href="css/patches/patch_sliding_door.css" rel="stylesheet" type="text/css" />
  	<![endif]-->
	<!--<script src="js/lib/jquery.min.js"></script>-->
	<script>
		$(function(){
			var url = window.location.href;
			// $("#icons li a").removeClass("active");	
			// var page = "home1";
			// if(url.indexOf('=') != -1){		
				// page = url.substring(url.indexOf('=') + 1);				
			// }
			// $("#" + page).addClass("active");
		});
	</script>
</head>
<body>
	<!-- skip link navigation -->
	<ul id="skiplinks">
		<li><a class="skip" href="#nav">Skip to navigation (Press Enter).</a></li>
		<li><a class="skip" href="#col3">Skip to main content (Press Enter).</a></li>
	</ul>
	
	<s:url action="logout" var="url_logout"></s:url> 
	<s:url action="home" var="url_home"></s:url>
	<s:url action="findAllRooms" var="url_findallroom"></s:url>
	<s:url action="findAllRoomTypes" var="url_findallroomtypes"></s:url>
	<s:url action="findAllGuests" var="url_findallguest"></s:url>
	<s:url action="findAllExtras" var="url_findallextra"></s:url>
	<s:url action="findAllSeasons" var="url_findallseasons"></s:url>
	<s:url action="findAllConventions" var="url_findallconventions"></s:url>
	<s:url action="findAllFacilities" var="url_findallfacilities"></s:url>
	<s:url action="findAllImages" var="url_findallimages"></s:url>
	<s:url action="goUpdateDetails" var="url_details"></s:url>
	<s:url action="findAllPropertyTypes" var="url_findallpropertytypes"></s:url>
	<s:url action="goFindAllRoomPriceLists" var="url_findallroompricelists"></s:url>
	<s:url action="goFindAllExtraPriceLists" var="url_findallextrapricelists"></s:url>
	<s:url action="goOnlineBookings" var="url_onlinebookings"></s:url>
	<s:url action="goExport" var="url_export"></s:url>
	<s:url action="goAboutInfo" var="url_about"></s:url>

	<div class="page_margins">
		<div class="page">
			<!-- begin: main content area #main -->
			
	<header class="header-main">
		<div class="row-fluid">
			<div class="logo">
				<img class="logo-big" src="images/ritmlogo.png" alt=""/>
				<img class="sml-icon" src="images/ritmlogosmall.png" alt=""/>
				<div class="plus-sign"> <input type="button"/> </div>
				<a href="#" id="pull"></a>
			</div>
		<div class="main-nav pull-right">			    	
			<nav class="clearfix" style="display:inline">
				<ul id="icons" class="clearfix">
					<li><a id="home1" href="#">HOME</a></li>
					<li><a id="planner" href="<s:property value="url_home"/>?sect=planner"><s:text name="PLANNER" /></a></li>
					<li><a id="accomodation" href="#"><s:text name="ACCOMMODATION" /></a>
						<ul class="submenu">
							<li><a href="<s:property value="url_findallroom"/>?sect=accomodation"><s:text name="" />Rooms</a></li>
							<li><a href="<s:property value="url_findallroomtypes"/>?sect=accomodation"><s:text name="Room Types" /></a></li>
							<li><a href="<s:property value="url_findallextra"/>?sect=accomodation"><s:text name="Extras" /></a></li>
						</ul>
					</li>
					<li><a id="guests" href="<s:property value="url_findallguest"/>?sect=guests"><s:text name="GUESTS" /></a></li>
					<li><a id="settings" href="#"><s:text name="SETTINGS" /></a>
						<ul class="submenu">
							<li><a href="<s:property value="url_onlinebookings"/>?sect=settings"><s:text name="Online Booking" /></a></li>
							<li><a href="<s:property value="url_findallseasons"/>?sect=settings"><s:text name="Seasons" /></a></li>
							<li><a href="<s:property value="url_findallroompricelists"/>?sect=settings"><s:text name="Room Price Lists" /></a></li>
							<li><a href="<s:property value="url_findallextrapricelists"/>?sect=settings"><s:text name="Extra Price List" /></a></li>
							<li><a href="<s:property value="url_findallconventions"/>?sect=settings"><s:text name="Conventions" /></a></li>
							<li><a href="<s:property value="url_findallfacilities"/>?sect=settings"><s:text name="" />Facilities</a></li>
							<li><a href="<s:property value="url_findallimages"/>?sect=settings"><s:text name="Images" /></a></li>
                            <li><a href="<s:property value="url_findallpropertytypes"/>?sect=settings"><s:text name="propertyTypes" /></a></li>
							<li><a href="<s:property value="url_details"/>?sect=settings"><s:text name="Structure Settings" /></a></li>
							<li><a href="<s:property value="url_export"/>?sect=settings"><s:text name="Data Export" /></a></li>
						</ul>
					</li>
					<li><span><a title="logout" class="logout" href="<s:property value="url_logout"/>">SIGN OUT</a></span> </li>					
				</ul>
			</nav>
		</div>
	</header> 
			

			