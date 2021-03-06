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

<!-- Displays the files and folders in a table.
Displayed on the left hand side of the add files to item page -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<!-- Form to store - item the user is working on, current parent folder and the selected file ids -->
<form name="myPersonalCollections" method="post">
	<input type="hidden" id="myCollections_parentCollectionId" 
		name="parentCollectionId" value="${parentCollectionId}"/>
	<input type="hidden" id="myCollections_researcherId" 
	    name="researcherId" value="${researcherId}"/>
    <input type="hidden" id="myCollections_parentFolderId" 
           name="parentFolderId" value="${parentFolderId}"/>
    <input type="hidden" id="myCollections_versionedItemId" 
	                                   name="versionedItemId" 
	                                   value=""/>  
</form>

<!-- Displays the folder path -->
/<span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.researcher.publications.getPersonalCollectionById('0')">My Publications</ur:a>/
   <c:forEach var="collection" items="${collectionPath}">
       <span class="folderBtnImg">&nbsp;</span><ur:a href="javascript:YAHOO.ur.researcher.publications.getPersonalCollectionById('${collection.id}')">${collection.name}</ur:a>/
   </c:forEach>
	    
<br/>    
<br/>

<!-- Table for files and folders  -->            
<table class="itemFolderTable" width="100%">
	<thead>
		<tr>
			<th class="thItemFolder" width="20%">Add</th>
			<th class="thItemFolder">Personal Publication Information</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="fileSystemObject" items="${collectionFileSystem}">
			<tr >
				<td class="tdItemFolderLeftBorder">
				    <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                    <span class="addBtnImg">&nbsp;</span><a href="javascript:YAHOO.ur.researcher.publications.addPublication(${fileSystemObject.versionedItem.id});"> Add</a> 
	                 </c:if>
                   
				</td>
				
				<td class="tdItemFolderRightBorder">
					 <c:if test="${fileSystemObject.fileSystemType.type == 'personalCollection'}">
	                 	<span class="folderBtnImg">&nbsp;</span> <ur:a href="javascript:YAHOO.ur.researcher.publications.getPersonalCollectionById('${fileSystemObject.id}')"> <ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText> </ur:a>
	                 </c:if>
	                 
	                 <c:if test="${fileSystemObject.fileSystemType.type == 'personalItem'}">
	                    <span class="scriptImg">&nbsp;</span><ur:maxText numChars="50" text="${fileSystemObject.name}"></ur:maxText>
	                 </c:if>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
