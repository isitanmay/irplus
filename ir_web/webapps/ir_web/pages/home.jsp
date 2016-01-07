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


<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>

<html>
    <head>
        <title>${repository.name} Institutional Repository</title>
        
        <c:import url="/inc/meta-frag.jsp"/>
        
        <!-- Core + Skin CSS -->
        <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
        <ur:styleSheet href="page-resources/css/base-ur.css"/>
        <ur:styleSheet href="page-resources/yui/menu/assets/skins/sam/menu.css"/>

        <ur:styleSheet href="page-resources/css/global.css"/>
        <ur:styleSheet href="page-resources/css/home_page_content_area.css"/>
        <ur:styleSheet href="page-resources/css/main_menu.css"/>
        <ur:styleSheet href="page-resources/css/tables.css"/>

        <!-- Dependencies --> 
        <ur:js src="page-resources/yui/yahoo-dom-event/yahoo-dom-event.js"/>
        <ur:js src="page-resources/yui/connection/connection-min.js"/>
        <ur:js src="page-resources/yui/container/container_core-min.js"/>
        <ur:js src="page-resources/yui/menu/menu-min.js"/>
        
        <!-- Source File -->
        <ur:js src="page-resources/js/menu/main_menu.js"/>
        
        <!--  base path information -->
 	    <ur:js src="pages/js/base_path.js"/>
 	    <ur:js src="page-resources/js/util/ur_util.js"/>
        <ur:js src="page-resources/js/public/home.js"/>
        
        <!--  styling for page specific elements -->
        <style type="text/css">
            .ur_button
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
            
            .ur_buttonover
            {
                width: 100px;
                margin: 0px;
                padding: 0px;
            }
        </style>
        <ur:js src="page-resources/js/google_analytics.js"/>
    </head>
    
    <body class="yui-skin-sam">
        <!--  yahoo doc 2 template creates a page 950 pixles wide -->
        <div id="doc2">  

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <!--  this is the body regin of the page -->
            <div id="bd">
                
                <!--  create the first column -->
                <div class="yui-g">
                <div class="yui-u first">
                   
                   <div class="contentContainer">
                   
                       <div class="contentBoxTitle">
                           <p>Browse/Search</p>
                       </div>
                   
                       <div class="contentBoxContent">
                           <c:url var="searchRepositoryItems" value="/searchRepositoryItems.action"/>
                           <form method="get" action="${searchRepositoryItems}">
                                <p>Search: <input type="text" name="query" size="50"/>
                                 <br/>
                                 <br/>
                                    <button type="submit" class="ur_button" 
		                               onmouseover="this.className='ur_buttonover';"
	 		                           onmouseout="this.className='ur_button';">Search</button>
	 		                           
	 		                     <br/>
	 		                     <br/>
	 		                      <c:url var="browseRepositoryItems" value="/browseRepositoryItems.action"/>
	 		                     <a href="${browseRepositoryItems}"><strong>Browse All/Search</strong></a>
                                </p>
                               
                           </form>
                           
                       </div>
                   </div>
                   
                   <!--  container to view all of the institutional collections -->
                   <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>Institutional Collections</p>
                        </div>
                        <div class="contentBoxContent">
                            <c:if test="${!ur:isEmpty(institutionalCollections)}">
                                <table class="baseTable">
                                <c:forEach items="${institutionalCollections}" var="institutionalCollection" >
       
                                    <c:url var="institutionalCollectionUrl" value="/viewInstitutionalCollection.action">
                                        <c:param name="collectionId" value="${institutionalCollection.id}"/>
                                    </c:url>
       
                                    <tr>
                                        <td class="baseTableImage">
                                            <c:if test="${ir:hasThumbnail(institutionalCollection.primaryPicture)}">
                                                <c:url var="url" value="/institutionalCollectionThumbnailDownloader.action">
                                                    <c:param name="collectionId" value="${institutionalCollection.id}"/>
                                                    <c:param name="irFileId" value="${institutionalCollection.primaryPicture.id}"/>
                                                </c:url>
                                                <img class="basic_thumbnail" src="${url}"/>
                                            </c:if>
                                        </td>
                                        <td>
                                            <p><strong><a href="${institutionalCollectionUrl}">${institutionalCollection.name}</a></strong> <ur:maxText numChars="100" text="${institutionalCollection.description}"></ur:maxText></p>
                                        </td>
                                    </tr>
       
                                </c:forEach>
                               </table>
                           </c:if>
                           <c:if test="${ur:isEmpty(institutionalCollections)}">
                               <p>There are no institutional collections</p>
                           </c:if>
                        </div>
                    </div>
                </div>
                <!--  end the first column -->
            
                <!--  Start the second column -->
                <div class="yui-u">
                    <!--  only show news items if they exist -->
                    <c:if test="${!ur:isEmpty(newsItems)}">
                        <div class="contentContainer">
                            <div class="contentBoxTitle">
                                <p>News</p>
                            </div>
                            <div id="news_items" class="contentBoxContent">
                                <c:import url="news_items_frag.jsp"/>
                            </div>
                        </div>
                     </c:if>
                    
                     <c:if test="${numRepositoryPictures >= 1}">
                     <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>Images</p>
                        </div>
                   
                        <div class="contentBoxContentPicture">
                            <div id="repository_picture">
                                <c:import url="next_repository_picture_frag.jsp"/>
                            </div>
                        </div>
                    </div>
                    </c:if>
                    
                   <c:if test="${!ur:isEmpty(researchers)}">
                   <div class="contentContainer">
                        <div class="contentBoxTitle">
                            <p>Researchers</p>
                        </div>
                        <div class="contentBoxContent">
                            <div id="researcher_picture" >
                                <c:import url="next_researcher_picture_frag.jsp"/>
                            </div>
                        </div>
                    </div>
                    </c:if>
                    
                    <div class="contentContainer">
                       <div class="contentBoxTitle">
                           <p>Statistics </p>
                       </div>
                   
                       <div class="contentBoxContent">
                       		<p>
                                <c:if test="${numberOfCollections > 0}">
                                    Number of collections  : ${numberOfCollections}<br/><br/>
                                </c:if>
                                <c:if test="${numberOfPublications > 0}">
                                    Number of publications : ${numberOfPublications}<br/><br/>
                                </c:if>
                                <c:if test="${numberOfFileDownloads > 0}">
                                    Number of file downloads : ${numberOfFileDownloads}<br/><br/>
                                </c:if>
                                <c:if test="${numberOfUsers > 0}">
                                    Number of members : ${numberOfUsers}<br/><br/>
                                </c:if>
                                <a href="<c:url value="/viewRepositoryStatistics.action"/>">View All Repository Statistics</a>  
                          </p>
                      </div>
                   </div>
                </div>
                <!--  end the second column -->
                
                </div>
                <!--  end the grid -->
                
            </div>
            <!--  end the body tag --> 

            <!--  this is the footer of the page -->
            <c:import url="/inc/footer.jsp"/>
        
        </div>
        <!-- end doc -->
    </body>
</html>

    
