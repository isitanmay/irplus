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

<table class="formTable">
<c:forEach items="${permissions}" var="permission">
    <tr>
        <td align="left">
            <input type="checkbox" 
                name="permissionIds" 
                id="${permission.name}"
                value="${permission.id}"
                onclick="YAHOO.ur.group.collection.autoCheckPermission(this, permissionIds);"
                <c:if test="${ir:groupHasPermissionForCollection(acl, collection, userGroup, permission)}">
                checked="true"
                </c:if>
             />  
        </td>
        <td align="left">
            <p>${permission.name} - ${permission.description}</p>
        </td>
    </tr>
</c:forEach>
</table>