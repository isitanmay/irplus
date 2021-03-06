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
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
<fmt:setBundle basename="messages"/>
<html>
  <head>
    <title> Edit Project Page:&nbsp;${groupWorkspaceProjectPage.groupWorkspace.name}</title>
    <c:import url="/inc/meta-frag.jsp"/>
        
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>
    
    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    <ur:styleSheet href="page-resources/css/tree.css"/>
    
	<ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
 	<ur:js src="page-resources/yui/button/button-min.js"/>
    
        
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/util/wait_dialog.js" />
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
 	<ur:js src="page-resources/js/user/edit_project_page_images.js"/>
 	
  </head>
    
   <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  
           

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body of the page -->
            <div id="bd">
                      <c:url value="/user/editGroupWorkspaceProjectPage.action" var="editProjectPageUrl">
				          <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
				      </c:url>
                      <h3> <a href="${editProjectPageUrl}">${groupWorkspaceProjectPage.groupWorkspace.name} </a> &gt; Edit Images </h3>
            		  <input type="hidden" id="group_workspace_project_page_id" name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
            
                      <button class="ur_button" 
 		                        onmouseover="this.className='ur_buttonover';"
 		                        onmouseout="this.className='ur_button';"
 		                        id="showUploadImage">Add Image</button>        
                      <div id="group_workspace_project_page_images">
                          <c:import url="group_workspace_project_page_image_table_frag.jsp"/>
                      </div>
            
            </div>
            <!--  end body -->
   
            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
        
        <!-- Dialog box for uploading pictures -->
        <div id="uploadProjectPageImageDialog" class="hidden">
	        <div class="hd">Image Upload</div>
		    <div class="bd">
		        <form id="addProjectPageImage" name="imageUploadForm" 
		            method="post" enctype="multipart/form-data"
		            action="user/uploadGroupWorkspaceProjectPageImage.action">
		              
		            <input type="hidden" id="group_workspace_id" name="groupWorkspaceId" value="${groupWorkspaceProjectPage.groupWorkspace.id}"/>
		            <div id="upload_form_fields">
		                <c:import url="upload_group_workspace_project_page_image_frag.jsp"/>
		             </div>
		        </form>
		    </div>
	     </div>
	     <!--  end upload picture dialog -->
        
    </body>
</html>