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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Edit Contributor Types</title>
    
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
 	
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
 	<ur:js src="page-resources/js/util/wait_dialog.js" />
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/js/util/ur_table.js"/>
    <ur:js src="page-resources/js/admin/contributor_type.js"/>
     
</head>

<body class=" yui-skin-sam">
    
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
        <h3>Edit Contributor Types</h3>
  
        <div id="bd">
		<button id="showContributorType" class="ur_button" 
 		    onmouseover="this.className='ur_buttonover';"
 		    onmouseout="this.className='ur_button';">New Contributor type</button> 
	    
	    <button id="showDeleteContributorType" class="ur_button" 
 		    onmouseover="this.className='ur_buttonover';"
 		    onmouseout="this.className='ur_button';">Delete</button>
	   
	    <br/>
	    <br/> 
	    <div id="newContributorTypes"> </div>
      </div>
      <!--  end body div -->
  
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
  </div>
  <!--  End  doc div-->
  
    <div id="newContributorTypeDialog" class="hidden">
        <div class="hd">Contributor Type Information</div>
	    <div class="bd">
		    <form id="addContributorType" name="newContributorType" method="post" 
		              action="<c:url value="/user/addContributorType.action"/>">
		              
                <div id="contributorTypeDialogFields">
                    <c:import url="contributor_type_form.jsp"/>
                </div>
	        </form>
        </div>
    </div>
	      
    <div id="deleteContributorTypeDialog" class="hidden">
	    <div class="hd">Delete Contributor Types</div>
		<div class="bd">
		    <form id="deleteContributorType" name="deleteContributorType" method="POST" 
		              action="<c:url value="/admin/deleteContributorType.action"/>">
		        <div id="deleteContributorTypeError" class="errorMessage"></div>
			          <p>Are you sure you wish to delete the selected contributor types?</p>
		    </form>
		</div>
     </div>
     
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