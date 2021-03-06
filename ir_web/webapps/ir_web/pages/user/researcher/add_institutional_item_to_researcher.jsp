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

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>


<html>
    <head>
        <title>Welcome</title>
        <c:import url="/inc/meta-frag.jsp"/>
        
	    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
	    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
	    <ur:styleSheet href="page-resources/css/base-ur.css"/>
	    <ur:styleSheet href="page-resources/yui/tabview/assets/skins/sam/tabview.css"/>
	    <ur:styleSheet href="page-resources/yui/tabview/assets/border_tabs.css"/>
	    
	    <ur:styleSheet href="page-resources/css/main_menu.css"/>
	    <ur:styleSheet href="page-resources/css/global.css"/>
	    <ur:styleSheet href="page-resources/css/tables.css"/>
	    
	    <ur:js src="page-resources/yui/utilities/utilities.js"/>
	    <ur:js src="page-resources/yui/container/container-min.js"/>
	 	<ur:js src="page-resources/yui/menu/menu-min.js"/>
	 	<ur:js src="page-resources/yui/button/button-min.js"/>
	 	
	 	<ur:js src="page-resources/js/util/base_path.jsp"/>
	 	<ur:js src="page-resources/js/util/ur_util.js" />
	 	<ur:js src="page-resources/js/menu/main_menu.js"/>
	    <ur:js src="page-resources/js/user/add_institutional_item.js"/>
	 	
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

            <!--  this is the header of the page -->
            <c:import url="/inc/header.jsp"/>
            
            <h3> Add Institutional item to Researcher </h3>
            
            <!--  this is the body region of the page -->
            <div id="bd">
            	
       	        <div class="yui-g">
			        <div class="yui-u first">
						<br/>
						<br/>


						<!-- Table for files and folders  -->            
						<table class="itemFolderTable" width="100%">
							<thead>
								<tr>
									<th class="thItemFolder" width="20%">Add</th>
									<th class="thItemFolder">Institutional item</th>
								</tr>
							</thead>
							<tbody>
								<tr >
									<td class="tdItemFolderLeftBorder">
					                    <span class="addBtnImg">&nbsp;</span> <a href="javascript:YAHOO.ur.researcher.institutional.item.addInstitutionalItem('${institutionalItem.id}');"> Add</a> 
									</td>
									
									<td class="tdItemFolderRightBorder">
					                    <span class="packageBtnImg">&nbsp;</span><ur:maxText numChars="50" text="${institutionalItem.name}"></ur:maxText>
									</td>
								</tr>
							</tbody>
						</table>
			    								       
		             </div>
		             <!--  end the first column -->
            
        	        <div class="yui-u">
        	        	<!--  Table of selected files -->
                    	<div id="newResearcherFolders" >
	                          <form  id="files" name="myResearcherFolders"  method="POST" action="user/getResearcherFileSystemTable.action">
	                              <input type="hidden" id="myResearcherFolders_researcherId" 
	                                   name="researcherId" 
	                                   value="${researcherId}"/>
	                              <input type="hidden" id="myResearcherFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	                              <input type="hidden" id="myResearcherFolders_institutionalItemId" 
	                                   name="institutionalItemId" 
	                                   value="${institutionalItemId}"/>
	                          </form>
	                      </div>
	                      <!--  end table of selected files div -->
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

    
