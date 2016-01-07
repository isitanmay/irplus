<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<!--  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
<fmt:setBundle basename="messages"/>
<html>
  <head>
    <title>Playing file ${itemFile.irFile.name}</title>
    <c:import url="/inc/meta-frag.jsp"/>
        
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
 
    <!--  Style for dialog boxes -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
    <ur:js src="page-resources/yui/tabview/tabview-min.js"/>
    
 	<ur:js src="page-resources/js/admin/manage_institutional_item_permissions.js"/>
 	<ur:js src="page-resources/js/admin/add_group_to_item.js"/>
 	 	
 	<ur:js src="pages/js/base_path.js"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	
 	<script type="text/javascript" src="page-resources/jw_player/jwplayer.js"></script>
    <script type="text/javascript">jwplayer.key="${mediaPlayerKey}";</script>
    
    <ur:js src="page-resources/js/google_analytics.js"/>
  </head>
    
  <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  
            
            <!--  this is the body of the page -->
            <div id="bd">
 
                <!--  this is the header of the page -->
                <c:import url="/inc/header.jsp"/>
                <c:url var="publicationVersionDownloadUrl" value="institutionalPublicationPublicView.action">
	              <c:param name="institutionalItemId" value="${institutionalItemId}"/>
	              <c:param name="versionNumber" value="${versionNumber}"/>
	            </c:url>
	            
      		    <h3><a href="${publicationVersionDownloadUrl}">${item.fullName}</a> - Playing file: ${itemFile.irFile.name}</h3>
      		    
      		    <div id="myElement">Loading the player...</div>

                <c:url var="itemFileDownload" value="/fileDownloadForInstitutionalItem.action">
				        <c:param value="${item.id}" name="itemId"/>
					    <c:param value="${itemFile.id}" name="itemFileId"/>
				</c:url>
                <script type="text/javascript">
                        jwplayer("myElement").setup({
                        file: "${itemFileDownload}",
                        autostart: true,
                        primary: "flash",
                        type:<c:if test="${itemFile.irFile.fileInfo.extension != 'mov'}">"${itemFile.irFile.fileInfo.extension}"</c:if><c:if test="${itemFile.irFile.fileInfo.extension == 'mov'}">"mp4"</c:if>
                });
                </script>
 	            
	        </div>
            <!--  end the tabs div -->
             
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        </div>
        <!-- end doc -->

    </body>
</html>