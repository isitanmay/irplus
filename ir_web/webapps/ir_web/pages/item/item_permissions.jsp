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
    <title>Admin - Edit Institutional Item: ${item.name}</title>
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
    
    <script>
    	var myTabs = new YAHOO.widget.TabView("item-permission-tabs");
    </script>
  </head>
    
  <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  
            
            <!--  this is the body of the page -->
            <div id="bd">
            
            <form id="hidden_collection_form" name="hiddenCollectionId">
                <input type="hidden" id="hidden_collection_id" value="${item.id}"/>
            	<input type="hidden" id="show_item_file_tab" name="showItemFileTab" value="${showItemFileTab}">
            </form>

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
      		<h3>Managing permissions for publication "${item.name}"</h3>
      		
      		<c:if test="${item.embargoed}">
      		<div class="errorMessage"> <h3>This publication and its files are under embargo until ${item.releaseDate}</h3> </div>
            </c:if>
           <form name="viewPublication" 
              method="get" action="<c:url value="/user/institutionalPublicationPublicView.action"/>">
				
				<input type="hidden" id="view_item_id" name="institutionalItemId" value="${institutionalItemId}"/>
	 				
				<button class="ur_button" type="submit" 
                       onmouseover="this.className='ur_buttonover';"
	                   onmouseout="this.className='ur_button';"
                       id="view_publication">Back to Publication</button>
            </form> 

			<br/>
            
            <!--  set up tabs for the workspace -->
	        <div id="item-permission-tabs" class="yui-navset">
	             <ul class="yui-nav">
                     <li class="selected"><a href="#tab1"><em>Publication Permission</em></a></li>
                     <li><a href="#tab2"><em>Publication File Permission</em></a></li>
                 </ul>
      
	             <div class="yui-content">
	                     <!--  first tab -->
                     	 <div id="tab1">
		                     <c:url var="addGroupsUrl" value="/admin/addGroupsToItem.action">
		                         <c:param name="itemId" value="${item.id}"/>
		                         <c:param name="institutionalItemId" value="${institutionalItemId}"/>
		                     </c:url>
		                     <br/>
		                     <br/>		                     

								Is the publication viewable by all users:
								<input type="radio" name="isItemPublic_${item.id}" value="true" onClick="javascript:YAHOO.ur.institution.item.permission.updateItemPublicView(${item.id}, 'true')"
								<c:if test="${item.publiclyViewable}">
									checked
								</c:if>
								> Yes  <c:if test="${item.embargoed}"><span class="errorMessage">(After Embargo)&nbsp;</span></c:if>
								<input type="radio" name="isItemPublic_${item.id}"  value="false" onClick="javascript:YAHOO.ur.institution.item.permission.updateItemPublicView(${item.id}, 'false')"
								<c:if test="${!item.publiclyViewable}">
									checked
								</c:if>
								> No 
							 <br/>
		                     <br/>
		                      
		                     <a href="${addGroupsUrl}">Add Groups To Publication</a>
		                     <br/>
		                     <div id="all_user_groups">
		                         <c:import url="item_group_permissions_frag.jsp"></c:import>
		                     </div>
		                  </div>
		                  
	                     <!--  Second tab -->
                     	 <div id="tab2">
 							 <h3>File Read permission groups</h3>
		                     
		                     <div id="file_user_groups">
		                         <c:import url="item_file_group_permissions_frag.jsp"></c:import>
		                     </div> 
		                  </div>		                  
	             </div>
	             <!--  end content div -->
	            
	        </div>
            <!--  end the tabs div -->
             
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        </div>
        
      </div>
      <!-- end doc -->
        
	     <!--  generic error dialog -->   	     
	     <div id="error_dialog_box" class="hidden">
	         <div class="hd">Error</div>
		     <div class="bd">
		          <div id="default_error_dialog_content">
		          </div>
		     </div>
	     </div>
	     <!-- End generic error dialog -->
	     
	     <!--  permissions for a group on a Item -->   	     
	     <div id="edit_group_permissions" class="hidden">
	         <div class="hd">Edit Permissions For Group</div>
		     <div class="bd">
		          <div id="group_permissions">
		              <c:url var="submitAction" 
		                  value="/admin/updateItemEntryPermissionsToGroup.action"/>
		              <form method="POST" name="permissionsItemForm" id="permissions_for_item_form" action="${submitAction}">
		                  <input type="hidden" id="group_permissions_group_id" name="groupId" value=""/>
		                  <input type="hidden" name="itemId" value="${item.id}"/>
		                  <div id="permissions_for_group">
		                      <c:import url="item_permissions_form_frag.jsp"/>
		                  </div>
		              </form>
		          </div>
             </div>
	     </div>
	     <!-- End permissions for a group on a Item -->   	     
   	     
	   
	   	<!--  permissions for a Item -->   	     
	     <div id="remove_group_confirm" class="hidden">
	         <div class="hd">Remove Group Permissions for Item</div>
		     <div class="bd">
		          <div id="group_permissions">
		              <form method="POST" id="remove_group_from_item_form" action="">
		                  <input type="hidden" id="remove_group_id" name="groupId" value=""/>
		                  <input type="hidden" name="itemId" value="${item.id}"/>
		              </form>
		              <p>Are you sure you wish to remove the selected group?</p>
		          </div>
		     </div>
	     </div>
	     <!-- End permissions for a Item -->   	     
	   
	   	<!--  permissions for a Item -->   	     
	     <div id="remove_file_group_confirm" class="hidden">
	         <div class="hd">Remove Group Permissions for File</div>
		     <div class="bd">
		          <div id="file_group_permissions">
		              <form method="POST" id="remove_group_from_item_file_form" action="">
		                  <input type="hidden" id="remove_file_group_id" name="groupId" value=""/>
		                  <input type="hidden" name="itemId" value="${item.id}"/>
		                  <input type="hidden" name="itemFileId" id="remove_item_file_id"/>
		                  <input type="hidden" name="institutionalItemId" id="remove_institutional_item_id"/>
		              </form>
		              <p>Are you sure you wish to remove the selected group?</p>
		          </div>
		     </div>
	     </div>
	     <!-- End permissions for a Item -->   		     
    </body>
</html>