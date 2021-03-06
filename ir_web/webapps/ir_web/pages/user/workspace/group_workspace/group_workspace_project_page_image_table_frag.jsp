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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<h3>Images</h3>
<c:if test="${!empty(groupWorkspaceProjectPage.images)}">

    <div class="dataTable">
              <urstb:table>
                  <urstb:thead>
                      <urstb:tr>
                          <urstb:td>Thumbnail</urstb:td>
                          <urstb:td>Move</urstb:td>
                          <urstb:td>Delete</urstb:td>
                      </urstb:tr>
                  </urstb:thead>
                  <urstb:tbody
                      var="image" 
                      oddRowClass="odd"
                      evenRowClass="even"
                      currentRowClassVar="rowClass"
                      collection="${groupWorkspaceProjectPage.imagesByOrder}">
                      <urstb:tr 
                          cssClass="${rowClass}"
                          onMouseOver="this.className='highlight'"
                          onMouseOut="this.className='${rowClass}'">
                        
                          <urstb:td>
                               <c:if test="${ir:hasThumbnail(image.imageFile)}">
				                   <c:url var="url" value="/user/groupWorkspaceProjectPageThumbnailDownloader.action">
                                       <c:param name="groupWorkspaceId" value="${groupWorkspaceProjectPage.groupWorkspace.id}"/>
                                       <c:param name="irFileId" value="${image.imageFile.id}"/>
                                   </c:url>
                                   <img class="basic_thumbnail" src="${url}"/>
                                   <br/>
                               </c:if>
                               Order: ${image.order} File Location: ${image.imageFile.fileInfo.fullPath}
                          </urstb:td>

                          <urstb:td>
                             <c:url value="/user/moveGroupWorkspaceProjectPageImageUp.action" var="moveImageUpUrl">
						                <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
						                <c:param name="imageId" value="${image.id}"/>
						              </c:url>
						              <a href="${moveImageUpUrl}"><img class="tableImg" alt="up" 
									          src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_up.gif" /></a>  
									  <c:url value="/user/moveGroupWorkspaceProjectPageImageDown.action" var="moveImageDownUrl">
						                <c:param name="groupWorkspaceProjectPageId" value="${groupWorkspaceProjectPage.id}"/>
						                <c:param name="imageId" value="${image.id}"/>
						              </c:url>
						              <a href="${moveImageDownUrl}"><img class="tableImg" alt="down" 
									          src="${pageContext.request.contextPath}/page-resources/images/all-images/move_arrow_down.gif" /></a>
	                 	            
                          </urstb:td>
                          
                          <urstb:td >
                             <button class="ur_button"
					              onmouseover="this.className='ur_buttonover';"
					              onmouseout="this.className='ur_button';"
					              onclick="javascript:YAHOO.ur.edit.project_page_image.deleteImage(${groupWorkspaceProjectPage.id}, ${image.id});"
					              id="deleteCollectionPictureConfirm">Delete</button>
                          </urstb:td>                        

                      </urstb:tr>
                  </urstb:tbody>
              </urstb:table>
          </div>


</c:if>
<c:if test="${empty(groupWorkspaceProjectPage.images)}">
    There are currently no group workspace project page images.
</c:if>