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

<%@ taglib prefix="ur" uri="ur-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<div class="pager">

	<ur:pager currentPageNumber="${currentPageNumber}" 
	     startPageNumber="${startPageNumber}"  
		 totalHits="${totalHits}"
		 numberOfPagesToShow="${numberOfPagesToShow}"  
		 numberOfResultsToShow="${numberOfResultsToShow}" >	

		<ur:firstPage>
		      <a href="javascript:YAHOO.ur.usergroup.userGroupSearch(0, 1, 1);">First</a>
		      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</ur:firstPage>
				
		<ur:previousPage>
			<c:if test="${totalPageNumber > 1}">												
				<&nbsp;<a href="javascript:YAHOO.ur.usergroup.userGroupSearch(${rowStart}, ${prevousPageStartPageNumber}, ${currentPageNumber - 1});">Previous</a> &nbsp;&nbsp;
			</c:if>
		</ur:previousPage>
								
		<ur:forEachPage var="pageNumber">
		    <c:if test="${pageNumber != currentPageNumber}">
				 <a href="javascript:YAHOO.ur.usergroup.userGroupSearch(${rowStart}, ${startPageNumber}, ${pageNumber});">${pageNumber}</a>&nbsp;&nbsp;
		     </c:if>
										
			 <c:if test="${pageNumber == currentPageNumber}">
			     ${pageNumber}&nbsp;&nbsp;
			 </c:if>
		</ur:forEachPage>								
															
		<ur:nextPage>
			<c:if test="${totalPageNumber > 1}">									
			     <a href="javascript:YAHOO.ur.usergroup.userGroupSearch(${rowStart}, ${nextPageStartPageNumber}, ${currentPageNumber + 1});">Next</a>&nbsp;> &nbsp;&nbsp;
			</c:if>
	    </ur:nextPage>

	    <ur:lastPage>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:YAHOO.ur.usergroup.userGroupSearch(${rowstartForLastPage}, ${startPageNumber}, ${currentPageNumber});">Last</a> 
		</ur:lastPage>	
				    
	</ur:pager>	


</div>   

				         
				         