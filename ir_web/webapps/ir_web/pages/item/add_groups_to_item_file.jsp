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
    <title>Admin - Add Groups to Institutional Item: ${item.name}</title>
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

 	<ur:js src="page-resources/js/admin/add_group_to_item.js"/>
 	
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	
    
    <!--  Style for dialog boxes -->
    <style>

        /* this is a simple fix for geco based browsers
         * this does have side affects if scroll bars are used.
         * in geco based browsers see cursor fix on yahoo
         * http://developer.yahoo.com/yui/container/
         */
        .mask 
        {
            overflow:visible; /* or overflow:hidden */
        }
    </style>
    
  </head>
    
  <body class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">  
            
        <!--  this is the body of the page -->
        <div id="bd">   
             <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <h3>Add read permission groups for file: ${itemFile.irFile.name}</h3>
           
            <c:url var="viewItem" value="/admin/viewInstitutionalItemPermissions.action">
                <c:param name="itemId" value="${item.id}"/>
                <c:param name="institutionalItemId" value="${institutionalItemId}"/>
                <c:param name="showItemFileTab" value="true"/> 
            </c:url>
            <input type="button" 
                class="ur_button" 
 		        onmouseover="this.className='ur_buttonover';"
 		        onmouseout="this.className='ur_button';"
                onclick="javascript:window.location='${viewItem}'" value="Done">
           
            <br/>
            <br/>
            <div id="all_user_groups">
                <c:import url="all_item_file_user_groups_frag.jsp"></c:import>
            </div>
            <input type="button" 
                class="ur_button" 
 		        onmouseover="this.className='ur_buttonover';"
 		        onmouseout="this.className='ur_button';"
                onclick="javascript:window.location='${viewItem}'" value="Done">
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
     </div>  
     
     
  </body>
</html>