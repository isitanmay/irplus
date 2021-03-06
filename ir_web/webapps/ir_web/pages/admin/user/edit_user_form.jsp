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

<!--  form fragment for dealing with editing user
      this form will return with error messages in it if there
      is an issue.
 -->
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ur" uri="ur-tags"%>

<table class="formTable">

    <tr>
        <td colspan="2">
		<!--  represents a successful submission -->
		 <input type="hidden" id="editUserForm_success" value="${added}"/>
		
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="userAlreadyExists"/></p>
                    
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="emailExistError"/></p>
           
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="rolesNotSelectedError"/></p>
          
          <p class="errorMessage"><ir:printError errors="${fieldErrors}" 
                   key="externalAccountError"/></p>
         </td>
        </tr> 
	<tr>
		 <td align="left" class="label"> First Name:</td>
         <td align="left" class="input"><input type="text" 
              id="editUserForm_first_name" name="irUser.firstName" value="${irUser.firstName}" size="45"/> </td> 
	</tr>              

	<tr>
		 <td align="left" class="label"> Last Name:</td>
         <td align="left" class="input"> <input type="text"  
   	              id="editUserForm_last_name" name="irUser.lastName" value="${irUser.lastName}" size="45"/> </td> 
	</tr>              

	<tr>
		 <td align="left" class="label"> User Name:*</td>
         <td align="left" class="input"><input type="text"  
              id="editUserForm_name" name="irUser.username" value="${irUser.username}" size="45"/> </td> 
	</tr>  
	
	<c:if test="${repositoryService.externalAuthenticationEnabled }">
	    <tr>
              <td align="left" class="label">External User Account Name:</td>
              <td align="left" class="input"><input type="text" 
              id="editUserForm_password_check" name="externalAccountUserName" value="${irUser.externalAccount.externalUserAccountName}" size="45"/></td>
        </tr>  
    
   	    <tr>
		     <td align="left" class="label">External Account Type:</td>
 		     <td align="left" class="input"> 
      	         <select id="editUserForm_external_account_type" name="externalAccountTypeId" />
      	             <option value = "0"> N/A</option>
	      		     <c:forEach items="${externalAccountTypes}" var="externalAccountType">
	      		   
	      			      <option value = "${externalAccountType.id}"
	      			      <c:if test="${externalAccountType.id == irUser.externalAccount.externalAccountType.id}">
	      				   selected
	      			      </c:if>
	      			      > ${externalAccountType.name}</option>
	      		     </c:forEach>
      	         </select>
		    </td> 
	    </tr>   
	</c:if>               

	<tr>
		 <td align="left" class="label">Phone Number:</td>
         <td align="left" class="input"> <input type="text"
              id="editUserForm_phone_number" name="phoneNumber" value="${irUser.phoneNumber}" size="45"/> </td> 
	</tr>              

	<tr>
		 <td align="left" class="label">Affiliation: 
           <input type="hidden" id="editUserForm_affiliation_name" value="${irUser.affiliation.name}"/>  </td>
		<td align="left" class="input"> 
      	   <select id="editUserForm_affiliation" name="affiliationId" />
	      		<c:forEach items="${affiliations}" var="affiliation">
	      			<option value = "${affiliation.id}"
	      			<c:if test="${affiliation.id == irUser.affiliation.id}">
	      				selected
	      			</c:if>
	      			> ${affiliation.name}</option>
	      		</c:forEach>
      	   </select>
		</td> 
	</tr>              

	<tr>
		 <td align="left" class="label">Department(s):</td>
         <td align="left" class="input"> 
      	   <select multiple="multiple" id="editUserForm_department" name="departmentIds" />
      	   		<option value = "0"> N/A</option>
	      		<c:forEach items="${departments}" var="department">
	      			<option value = "${department.id}"
	      			<c:forEach items="${irUser.departments}" var="userDepartment">
	      			    <c:if test="${department.id == userDepartment.id}">
	      				    selected
	      			    </c:if>
	      			</c:forEach>
	      			> ${department.name}</option>
	      		</c:forEach>
      	   </select>
      	   </td> 
	</tr>              

	<tr>
		 <td align="left" class="label"> Account Locked: </td>
         <td align="left" class="input"> <input type="checkbox" 
              id="editUserForm_account_locked" name="accountLocked" value="true" 
	              <c:if test="${irUser.accountLocked == true}"> 
	              	checked
	              </c:if>
              
          /> </td> 
	</tr>              

	<tr>
		 <td class="label"> Account Expired: </td>
         <td align="left" class="input"> <input type="checkbox" 
              id="editUserForm_account_expired" name="accountExpired" value="true" 
	               <c:if test="${irUser.accountExpired == true}"> 
	              	checked
	              </c:if>
         /> </td> 
	</tr>              

	<tr>
		 <td align="left" class="label"> Credentials Expired:</td>
         <td align="left" class="input"> <input type="checkbox" 
              id="editUserForm_credentials_expired" name="credentialsExpired" value="true" 
	              <c:if test="${irUser.credentialsExpired == true}"> 
	              	checked
	              </c:if>
              
          /></td> 
	</tr>              

	<tr>
		 <td align="left" class="label" colspan="2"> <strong> Roles: </strong> </td>
	</tr>
	
	<tr>
		 <td class="label" colspan="2"> 		  
 		     <input type="checkbox"
 		            onclick="YAHOO.ur.email.autoCheckRoles(this);"
 		     <c:if test='${ir:checkUserHasRole(irUser, "ROLE_ADMIN", "")}'> 
                 checked="true" 
             </c:if>
             id="editUserForm_isAdmin" name="adminRole" value="true"/>  Administrator
		</td>
	</tr>
	 <tr>
		<td class="label" colspan="2"> 
		 <input type="checkbox"	
		     onclick="YAHOO.ur.email.autoCheckRoles(this);"
		     <c:if test='${ir:checkUserHasRole(irUser, "ROLE_AFFLIATION_APPROVER", "")}'> 
                checked="true" 
             </c:if>
              name="affiliationApproverRole" value="true" id="editUserForm_isAffilationApprover"/> User Affiliation Approver
 		</td>
	</tr> 
   <tr>
		<td class="label" colspan="2"> 
		 <input type="checkbox"	
		     onclick="YAHOO.ur.email.autoCheckRoles(this);"
		     <c:if test='${ir:checkUserHasRole(irUser, "ROLE_COLLECTION_ADMIN", "")}'> 
                checked="true" 
             </c:if>
              name="collectionAdminRole" value="true" id="editUserForm_isCollectionAdmin"/> Collection Administrator
 		</td>
	</tr> 
	
    <tr>
		<td class="label" colspan="2"> 	
            <input type="checkbox"
                onclick="YAHOO.ur.email.autoCheckRoles(this);"
            <c:if test='${ir:checkUserHasRole(irUser, "ROLE_RESEARCHER", "")}'> 
                 checked="true"   
            </c:if>
            name="researcherRole" value="true" id="editUserForm_isResearcher"/> Researcher
 		</td>
	</tr>
    <tr>
		<td class="label" colspan="2"> 
		 <input type="checkbox"	
		     onclick="YAHOO.ur.email.autoCheckRoles(this);"
		     <c:if test='${ir:checkUserHasRole(irUser, "ROLE_IMPORTER", "")}'> 
                checked="true" 
             </c:if>
              name="importerRole" value="true" id="editUserForm_isImporter"/> Importer
 		</td>
	</tr> 
	<tr>
		<td class="label" colspan="2"> 	
            <input type="checkbox"
                onclick="YAHOO.ur.email.autoCheckRoles(this);"
            <c:if test='${ir:checkUserHasRole(irUser, "ROLE_GROUP_WORKSPACE_CREATOR", "")}'> 
                checked="true" 
            </c:if>  
            name="groupWorkspaceCreatorRole" value="true" id="editUserForm_isGroupWorkspaceCreator"/> Group Workspace Creator
		</td>
	</tr>     
	<tr>
		<td class="label" colspan="2"> 	
            <input type="checkbox"
                onclick="YAHOO.ur.email.autoCheckRoles(this);"
            <c:if test='${ir:checkUserHasRole(irUser, "ROLE_AUTHOR", "")}'> 
                checked="true" 
            </c:if>  
            name="authorRole" value="true" id="editUserForm_isAuthor"/> Author
		</td>
	</tr>        
    
	<tr>
		 <td class="label" colspan="2"> 	
             <input type="checkbox"
                 onclick="YAHOO.ur.email.autoCheckRoles(this);"
             <c:if test='${ir:checkUserHasRole(irUser, "ROLE_USER", "")}'> 
              checked="true" 
             </c:if>
               name="userRole" value="true" id="editUserForm_isUser"/>  User
		</td>
	</tr>
	
	
	
	<tr>
		<td class="label" colspan="2"> 
		 <input type="checkbox"	
		     onclick="YAHOO.ur.email.autoCheckRoles(this);"
		     <c:if test='${ir:checkUserHasRole(irUser, "ROLE_COLLABORATOR", "")}'> 
                checked="true" 
             </c:if>
              name="collaboratorRole" value="true" id="editUserForm_isCollaborator"/> Collaborator Only
 		</td>
	</tr>
	
	
        
</table>	