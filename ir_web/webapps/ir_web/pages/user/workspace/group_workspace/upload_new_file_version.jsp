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
<%@ taglib prefix="ir" uri="ir-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="group_workspace_locked_by_user_error">
<span class="errorMessage" id="groupWorkspaceNewVersionError"><ir:printError errors="${fieldErrors}" 
		                       key="lockedByUser"/></span><br/>
		                       <span class="errorMessage"><ir:printError errors="${fieldErrors}" 
		                       key="cannotLock"/></span>
</div>
<input type="hidden" name="groupWorkspaceFileId" id="group_workspace_file_id" value="${groupWorkspaceFile.id}"/>
<input type="hidden" id="group_workspace_version_added" name="versionAdded" value="${versionAdded}"/>
<table class="formTable">
   
    <tr>
        <td colspan="2"><strong>Upload new Version for File: ${groupWorkspaceFile.name}</strong></td>
    </tr>
    <tr>
        <td class="label">File:</td>
        <td align="left" class="input"><input size="80" type="file" id="group_workspace_new_version_file" 
            name="file"/> </td>
    </tr>
    <tr>
         <td class="label">Description:</td>
         <td align="left" class="input"><textarea id="group_workspace_file_description" 
             name="groupFileDescription" cols="47" rows="2"></textarea></td>
    </tr>
        <tr>
         <td class="label">Keep Locked</td>
         <td align="left" class="input"><input type="checkbox" name="keepLocked" value="true"/></td>
    </tr>
</table>
