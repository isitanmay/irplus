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

<!--  form fragment for dealing with editing departments
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

		<!--  represents a successful submission -->
		<input type="hidden" id="newFileServerFormSuccess"   value="${added}"/>
		       
		<!--  if editing an id must be passed -->     
	    <input type="hidden" id="newFileServerFormId" name="fileServerId" value="${fileServer.id}"/>
		               
	    <input type="hidden" id="newFileServerFormNew" name="newFileServerVal" value="true"/>
		        
		<div id="error_div">            
	        <!--  get the error messages from fieldErrors -->
		   <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		    key="fileServer.name"/></p>
		    <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
		    key="fileServerAlreadyExists"/></p>  
		</div>
		
	    <table class="formTable">    
		    <tr>       
	            <td align="left" class="label">Name:*</td>
	            <td align="left" class="input"><input type="text" 
			    id="newFileServerFormName" 
			    name="name" 
			    size="45"
			    value="<c:out value='${fileServer.name}'/>"/> </td>
			</tr>
			<tr>
			    <td align="left" class="label">Description:</td>
			    <td align="left" colspan="2" class="input"> <textarea name="description" 
	                id="newFileServerFormDescription" cols="42" rows="4"><c:out value='${fileServer.description}'/></textarea>
	            </td>
			</tr>
	    </table>