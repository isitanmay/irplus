<jsp:directive.page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8" />


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

<%@ taglib prefix="ir" uri="ir-tags"%>



<!--  get the error messages from fieldErrors -->
<div id="researcher_upload_error">
   <p class="errorMessage" id="upload_error"><ir:printError errors="${fieldErrors}" 
	key="researcherPictureUploadError"/></p>
</div>
<input type="hidden" id="picture_added" value="${added}"/>
<table class="formTable">
    <tr>       
        <td align="left" class="label">File:*</td>
        <td align="left" class="input"><input type="file" name="file"
			id="picture_file_name" value="" size="45"/> </td>
	</tr>
	<tr>
	    <td align="left" class="label">Primary Picture:</td>
	    <td align="left" class="input"> <input type="checkbox"
			name="primaryResearcherPicture" id="primary_picture" value="true" />
        </td>
	</tr>
</table>
