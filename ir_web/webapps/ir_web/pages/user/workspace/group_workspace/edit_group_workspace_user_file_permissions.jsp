<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />


<!--  
   Copyright 2008-2011 University of Rochester

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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Edit Permissions for Group Workspace File: ${groupWorkspaceFile.name}</title>
    
    <!-- Medatadata fragment for page cache -->
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
 	
 	
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
</head>

<body class="yui-skin-sam">

    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
        
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
  
        <c:url var="workspaceUrl" value="/user/workspace.action">
            <c:param name="tabName" value="GROUP_WORKSPACE"/>
        </c:url>
        <c:url var="groupWorkspaceUrl" value="/user/workspace.action">
            <c:param name="tabName" value="GROUP_WORKSPACE"/>
            <c:param name="groupWorkspaceId" value="${groupWorkspaceFile.groupWorkspace.id}"/>
        </c:url>
  
        <div id="bd">
            <strong>Path:&nbsp;/
	            <span class="groupImg">&nbsp;</span>
	            <a href="${workspaceUrl}">Group Workspaces</a> /
	           
	            <span class="groupImg">&nbsp;</span>
                <a href="${groupWorkspaceUrl}">${groupWorkspaceFile.groupWorkspace.name}</a>&nbsp;/
              
	    
                 <c:forEach var="currentFolder" items="${folderPath}">
                      <c:url var="folderUrl" value="/user/workspace.action">
                          <c:param name="tabName" value="GROUP_WORKSPACE"/>
                          <c:param name="groupWorkspaceId" value="${groupWorkspaceFolder.groupWorkspace.id}"/>
                          <c:param name="groupWorkspaceFolderId" value="${currentFolder.id}"/>
                      </c:url>
                      <span class="folderBtnImg">&nbsp;</span>
                           <a href="${folderUrl}">${currentFolder.name}</a>&nbsp;/
                 </c:forEach>
            </strong>
            <br/><br/>
           
            <h3> Permissions for File: ${groupWorkspaceFile.name} </h3>
            
           
            <h3>Permissions For User:${editUser.firstName}&nbsp;${editUser.lastName}</h3>
            <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
			key="parentFolderPermissionsError"/></p>
            
            <form action="<c:url value="/user/saveUserGroupFilePermissions"/>" >
            <input type="hidden" name="groupWorkspaceFileId" value="${groupWorkspaceFile.id}"/>
            <input type="hidden" name="editUserPermissionsId" value="${editUser.id}"/>
            <table>
            <tr>
                <td> File Edit:</td>
                <td>
                    <input type="checkbox" 
                        name="filePermissions"
                        value="EDIT"  
                        <c:if test='${ir:entryHasPermission(editUserAcl, "EDIT")}'>
                            checked="checked"
                        </c:if>
                    />    
                </td> 
            </tr>
            <tr>
                <td>Read:</td>
                <td> 
                    <input type="checkbox" 
                           name="filePermissions"
                           value="VIEW"
                         <c:if test='${ir:entryHasPermission(editUserAcl, "VIEW")}'>
                             checked="checked"
                         </c:if>
                     /> 
                 </td>
             </tr> 
             </table> 
             <input type="submit" value="save" />      
           </form>
        </div>
        <!--  end body div -->
      
        <!--  this is the footer of the page -->
        <c:import url="/inc/footer.jsp"/>
  
   </div>
   <!--  End doc div-->
 
</body>
</html>