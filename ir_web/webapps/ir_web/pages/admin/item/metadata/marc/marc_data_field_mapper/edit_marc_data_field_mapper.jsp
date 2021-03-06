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
<%@ taglib prefix="urstb" uri="simple-ur-table-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>
<html>

<head>
    <title>Create MARC Mapping</title>
    
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
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/js/util/ur_table.js"/>
    <ur:js src="page-resources/js/admin/marc_identifier_type_mapping.js"/>
    <ur:js src="page-resources/js/admin/marc_extent_type_mapping.js"/>
    
     
</head>

<body class=" yui-skin-sam">
    
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">
    
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
      
       
        <h3><a href="<c:url value="/admin/viewMarcDataFieldMappers.action"/>">Data Field Mappings</a> &gt; Edit Data Field Mapping</h3>
  
        <div id="bd">
		
	  <form action="<c:url value="/admin/saveMarcDataFieldMapper.action"/>"  method="post">
	      <input type="hidden" name="id" value="${ marcDataFieldMapper.id}"/>
	      <div id="error" class="errorMessage">${message}</div>
	      <table class="formTable"> 
	      
           <tr>       
	           <td align="left" class="label">Data Field:*</td>
		       <td align="left" class="input">
		          <select name="dataFieldId">
		          <c:forEach items="${marcDataFields}" var="dataField">
		               <option  
		                <c:if test="${dataField.id == marcDataFieldMapper.marcDataField.id}">
		                  selected="true"
		                </c:if>
		               
		               value="${dataField.id}">${dataField.code} - ${dataField.name}</option>
		         </c:forEach>
		         </select> 
		      </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Indicator 1</td>
		       <td align="left" class="input">
		          <input type="text" size="1" maxlength="1" name="indicator1" value="${marcDataFieldMapper.indicator1}"/>
		      </td>
	       </tr>
	       <tr>       
	           <td align="left" class="label">Indicator 2</td>
		       <td align="left" class="input">
		          <input type="text" size="1" maxlength="1" name="indicator2" value="${marcDataFieldMapper.indicator2}"/>
		      </td>
	       </tr>
	       
	    </table>
	    <input type="submit" value="Save"/>
	    <input type="button" value="Cancel" onclick='javascript: window.location =  "<c:url value="/admin/viewMarcDataFieldMappers.action"/>"'/>
	    </form>
	    
	    
	    <c:if test="${marcDataFieldMapper.id != null}">
	        <h3>Identifier Type Mappings</h3>
	        <c:url value="/admin/editMarcIdentifierTypeSubFieldMapper.action" var="newIdentifierTypeMapperUrl">
	            <c:param name="marcDataFieldMapperId" value="${marcDataFieldMapper.id}"/>
	        </c:url>
	        <a href="${newIdentifierTypeMapperUrl}">New Identifier Type Mapping</a>
	        <br/>
	        <br/>
	        <div class="dataTable">

	        <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
	                <urstb:td>Id</urstb:td>
					<urstb:td>Identifier Type</urstb:td>
					<urstb:td>Sub Field</urstb:td>
					<urstb:td>Action</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="ident" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${marcDataFieldMapper.identifierTypeSubFieldMappings}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                       ${ident.id}
	                        </urstb:td>
	                        <urstb:td>
			                   ${ident.identifierType.name}
	                        </urstb:td>
	                        <urstb:td>
			                   ${ident.marcSubField.name} 
	                        </urstb:td>
	                       
	                        <urstb:td>
	                           <c:url value="/admin/editMarcIdentifierTypeSubFieldMapper.action" var="editUrl">
	                               <c:param name="id" value="${ident.id}"/>
	                           </c:url>
	                           <a href="${editUrl}">Edit</a> /<a href="javascript:YAHOO.ur.marc.marcIdentifierTypeMapping.deleteMapping(${ident.id});">Delete</a>
	                        </urstb:td>
	                       
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
  
            </div>	

	        <h3>Extent Type Mappings</h3>
	        <c:url value="/admin/editMarcExtentTypeSubFieldMapper.action" var="newExtentTypeMapperUrl">
	            <c:param name="marcDataFieldMapperId" value="${marcDataFieldMapper.id}"/>
	        </c:url>
	        <a href="${newExtentTypeMapperUrl}">New Extent Type Mapping</a>
	        <br/>
	        <br/>
	        <div class="dataTable">

	        <urstb:table width="100%">
	        <urstb:thead>
	            <urstb:tr>
	                <urstb:td>Id</urstb:td>
					<urstb:td>Extent Type</urstb:td>
					<urstb:td>Sub Field</urstb:td>
					<urstb:td>Action</urstb:td>
	            </urstb:tr>
	            </urstb:thead>
	            <urstb:tbody
	                var="extent" 
	                oddRowClass="odd"
	                evenRowClass="even"
	                currentRowClassVar="rowClass"
	                collection="${marcDataFieldMapper.extentTypeSubFieldMappings}">
	                    <urstb:tr 
	                        cssClass="${rowClass}"
	                        onMouseOver="this.className='highlight'"
	                        onMouseOut="this.className='${rowClass}'">
	                        <urstb:td>
		                       ${extent.id}
	                        </urstb:td>
	                        <urstb:td>
			                   ${extent.extentType.name}
	                        </urstb:td>
	                        <urstb:td>
			                   ${extent.marcSubField.name} 
	                        </urstb:td>
	                       
	                        <urstb:td>
	                           <c:url value="/admin/editMarcExtentTypeSubFieldMapper.action" var="editUrl">
	                               <c:param name="id" value="${extent.id}"/>
	                           </c:url>
	                           <a href="${editUrl}">Edit</a> /  <a href="javascript:YAHOO.ur.marc.marcExtentTypeMapping.deleteMapping(${extent.id});">Delete</a>
	                        </urstb:td>
	                       
	                    </urstb:tr>
	            </urstb:tbody>
	        </urstb:table>
  
            </div>	
	    </c:if>
	    
	    
	    
        </div>
        <!--  end body div -->
  
      <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
  
    <div id="deleteExtentDialog" class="hidden">
	    <div class="hd">Delete MARC Extent Type Field Mapping</div>
	    <div class="bd">
		    <p>Are you sure you wish to delete the selected Extent Type Mapping?</p>
	    </div>
    </div>
    <!--  end dialog -->
    
    <div id="deleteIdentDialog" class="hidden">
	    <div class="hd">Delete MARC Identifier Type Field Mapping</div>
	    <div class="bd">
		    <p>Are you sure you wish to delete the selected Identifier Type Mapping?</p>
	    </div>
    </div>
    <!--  end dialog -->
    
  </div>
  <!--  End  doc div-->
  


</body>
</html>