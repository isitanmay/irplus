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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp" />

<fmt:setBundle basename="messages" />

<html>
<head>

    <title>Move Project Page Information</title>
    <c:import url="/inc/meta-frag.jsp" />

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

    <!-- Source File -->
    <ur:js src="page-resources/js/menu/main_menu.js"/>

    <!--  base path information -->
    <ur:js src="page-resources/js/util/base_path.jsp" />
    <ur:js src="page-resources/js/util/ur_util.js" />
    <ur:js src="page-resources/js/util/wait_dialog.js" />
    <ur:js src="page-resources/js/user/move_project_page_info.js" />
</head>

<body class="yui-skin-sam">
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2"><!--  this is the header of the page --> 
        <c:import url="/inc/header.jsp" /> 

        <h3>Move Project Page Information</h3>

        <!--  this is the body regin of the page -->
        <div id="bd">
            <c:url value="/user/editGroupWorkspaceProjectPageFileSystem.action" var="editProjectPageUrl">
			    <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
			</c:url>
    
            <button class="ur_button"
			    onmouseover="this.className='ur_buttonover';"
			    onmouseout="this.className='ur_button';"
			    onclick="location.href='${editProjectPageUrl}';">Cancel</button>
            
            <h3 class="errorMessage"><ir:printError key="moveError" errors="${fieldErrors}"></ir:printError></h3>
    <table>
        <tr>
		    <td width="400px" align="left" valign="top"></td>
		    <td width="100px"></td>
		    <td width="400px" align="left" valign="top">
		
		
		        <div id="destination_path">
		            Move To Location: <a href="javascript:YAHOO.ur.project_page.filesystem.move.getMoveFolder('0');">/${groupWorkspaceProjectPage.groupWorkspace.name}</a>/
		            <c:forEach var="folder" items="${destinationPath}">
			            <a href="javascript:YAHOO.ur.project_page.filesystem.move.getMoveFolder('${folder.id}')">${folder.name}</a>/
                    </c:forEach>
                </div>
            
                <br/>
                <br/>                                 
            
            </td>
	    </tr>
	    <tr>
		    <td width="400px" align="left" valign="top">
		        <div class="dataTable">
		        <table width="400px">
			        <thead>
				        <tr>
					        <td>Project Page Objects To Move</td>
				        </tr>
			        </thead>
			        <tbody>
			            <c:set var="even" value="true"/>
			            
				        <c:forEach items="${foldersToMove}" var="folder">
				            <c:if test="${even}">
                                <tr onmouseout="this.className='even'" onmouseover="this.className='highlight'" class="even">
                            </c:if>
                            <c:if test="${!even}">
                                 <tr onmouseout="this.className='odd'" onmouseover="this.className='highlight'" class="odd">
                            </c:if>
                              <td><span class="folderBtnImg">&nbsp;</span>${folder.name}</td>
					            
					        </tr>
                            <c:set var="even" value ="${!even}"/>
				        </c:forEach>

				        <c:forEach items="${filesToMove}" var="file">
				            <c:if test="${even}">
                                <tr onmouseout="this.className='even'" onmouseover="this.className='highlight'" class="even">
                            </c:if>
                            <c:if test="${!even}">
                                 <tr onmouseout="this.className='odd'" onmouseover="this.className='highlight'" class="odd">
                            </c:if>
						        <td><ir:fileTypeImg cssClass="tableImg" irFile="${file.irFile}"/>${file.name}</td>
					        </tr>
					         <c:set var="even" value ="${!even}"/>
				        </c:forEach>
				        <c:forEach items="${itemsToMove}" var="item">
				            <c:if test="${even}">
                                <tr onmouseout="this.className='even'" onmouseover="this.className='highlight'" class="even">
                            </c:if>
                            <c:if test="${!even}">
                                 <tr onmouseout="this.className='odd'" onmouseover="this.className='highlight'" class="odd">
                            </c:if>
						        <td><span class="packageBtnImg">&nbsp;</span>${item.name}</td>
					        </tr>
					         <c:set var="even" value ="${!even}"/>
 				        </c:forEach>
				        <c:forEach items="${publicationsToMove}" var="publication">
				            <c:if test="${even}">
                                <tr onmouseout="this.className='even'" onmouseover="this.className='highlight'" class="even">
                            </c:if>
                            <c:if test="${!even}">
                                 <tr onmouseout="this.className='odd'" onmouseover="this.className='highlight'" class="odd">
                            </c:if>
						        <td><span class="scriptImg">&nbsp;</span>${publication.name}</td>
					        </tr>
					        <c:set var="even" value ="${!even}"/>
                            
				        </c:forEach>
				        <c:forEach items="${linksToMove}" var="link">
				        
				            <c:if test="${even}">
                                <tr onmouseout="this.className='even'" onmouseover="this.className='highlight'" class="even">
                            </c:if>
                            <c:if test="${!even}">
                                 <tr onmouseout="this.className='odd'" onmouseover="this.className='highlight'" class="odd">
                            </c:if>
						        <td><img  alt="" 
			                       src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>${link.name}</td>
					        </tr>
					        
				        </c:forEach>
			        </tbody>
		        </table>
		        </div>
		    </td>
		    <td width="100px" valign="top" align="center">
		
		        <button class="ur_button" id="move_button"
			            onclick="javascript:YAHOO.ur.project_page.filesystem.move.moveFolder();"
			            onmouseover="this.className='ur_buttonover';"
			            onmouseout="this.className='ur_button';"
			            >Move<span class="pageWhiteGoBtnImg">&nbsp;</span>
			    </button>
		   </td>
		   <td width="400px" align="left" valign="top">
		       <form name="viewChildContentsForMove"
	              id="move_folder_form">
	              <input type="hidden" id="destination_id" name="destinationId" value="${destinationId}" />
	              <input type="hidden" name="parentFolderId" value="${parentFolderId}" /> 
	              <input type="hidden" id="groupWorkspaceProjectPageId"  name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPageId}" /> 
	
	               <c:forEach items="${foldersToMove}" var="folder">
	                   <input type="hidden" value="${folder.id}" name="folderIds" />
                   </c:forEach> 
 
                   <c:forEach items="${filesToMove}" var="file">
	                   <input type="hidden" value="${file.id}" name="fileIds" />
                   </c:forEach>
                   
                   <c:forEach items="${itemsToMove}" var="item">
	                   <input type="hidden" value="${item.id}" name="itemIds" />
                   </c:forEach>
                   
                   <c:forEach items="${linksToMove}" var="link">
	                   <input type="hidden" value="${link.id}" name="linkIds" />
                   </c:forEach>
                   
                   <c:forEach items="${publicationsToMove}" var="publication">
	                   <input type="hidden" value="${publication.id}" name="publicationIds" />
                   </c:forEach>
    
	               <!-- set to indicate a success full move -->
	               <input type="hidden" id="action_success" value="${actionSuccess}" name="actionSuccess"/>
               </form>
                <div class="dataTable">
              <urstb:table width="100%">
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td>Destination</urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="fileSystemObject" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${currentDestinationContents}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                          <urstb:td>
                            
                            <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFolder'}">
                               <c:set var="canMove" value="${ir:canMoveToFolder(foldersToMove, fileSystemObject)}"/>
			           	       <c:if test="${canMove}">
						            <span class="folderBtnImg"></span><a
							            href="javascript:YAHOO.ur.project_page.filesystem.move.getMoveFolder(${fileSystemObject.id});">${fileSystemObject.name}</a>
					           </c:if>
					           <c:if test="${!canMove}">
					               <span class="folderBtnImg"></span><span class="errorMessage">${fileSystemObject.name} [Moving]</span>
					           </c:if>
					       </c:if>
			           	   <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFile'}">
			           	       <c:set var="beingMoved" value="${ir:isFileToBeMoved(filesToMove, fileSystemObject)}"/>
			           	       <c:if test="${!beingMoved}">
						           <ir:fileTypeImg cssClass="tableImg" irFile="${fileSystemObject.irFile}"/>${fileSystemObject.name}
					           </c:if>
					           <c:if test="${beingMoved}">
						           <ir:fileTypeImg cssClass="tableImg" irFile="${fileSystemObject.irFile}"/><span class="errorMessage">${fileSystemObject.name}[Moving]</span>
					           </c:if>
					       </c:if>
					      
					       <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageFileSystemLink'}">
					           <c:set var="beingMoved" value="${ir:isFileToBeMoved(linksToMove, fileSystemObject)}"/>
			           	       <c:if test="${!beingMoved}">
						            <img alt="" 
			                           src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/>${fileSystemObject.name}
					           </c:if>
			           	       <c:if test="${beingMoved}">
						            <img  alt="" 
			                           src="${pageContext.request.contextPath}/page-resources/images/all-images/link.gif"/><span class="errorMessage">${fileSystemObject.name}[Moving]</span>
					           </c:if>

					       </c:if>
					       <c:if test="${fileSystemObject.fileSystemType.type == 'groupWorkspaceProjectPageInstitutionalItem'}">
					           <c:set var="beingMoved" value="${ir:isFileToBeMoved(itemsToMove, fileSystemObject)}"/>
			           	       <c:if test="${!beingMoved}">
						            <span class="packageBtnImg">&nbsp;</span> ${fileSystemObject.name}
						       </c:if>
			           	       <c:if test="${beingMoved}">
						            <span class="packageBtnImg">&nbsp;</span><span class="errorMessage"> ${fileSystemObject.name} [Moving]</span>
						       </c:if>
					       </c:if>						
                          </urstb:td>
                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
              </div>
               
		   </td>
	   </tr>
   </table>

            <!--  end the body tag --> <!--  this is the footer of the page --> 
            <c:import url="/inc/footer.jsp" />
        </div>
    </div>
    <!-- end doc -->
    
 
    
    <!--  wait div -->
	<div id="wait_dialog_box" class="hidden">
	    <div class="hd">Processing...</div>
		<div class="bd">
		    <c:url var="wait" value="/page-resources/images/all-images/ajax-loader.gif"/>
		    <p><img src="${wait}"></img></p>
		</div>
	</div>   
</body>
</html>