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
<%@ taglib prefix="ir" uri="ir-tags"%>

<!--  document type -->
<c:import url="/inc/doctype-frag.jsp"/>
          
<fmt:setBundle basename="messages"/>


<html>

<head>
    <title>My Workspace</title>
    <!-- Medatadata fragment for page cache -->
    <c:import url="/inc/meta-frag.jsp"/>
    
    <!-- Core + Skin CSS -->
    <ur:styleSheet href="page-resources/yui/assets/skins/sam/skin.css"/>
    <ur:styleSheet href="page-resources/yui/reset-fonts-grids/reset-fonts-grids.css"/>
    <ur:styleSheet href="page-resources/css/base-ur.css"/>

    <ur:styleSheet href="page-resources/css/main_menu.css"/>
    <ur:styleSheet href="page-resources/css/global.css"/>
    <ur:styleSheet href="page-resources/css/tables.css"/>
    
    <!--  Style for dialog boxes -->
    <style>
         #yui-history-iframe {
         position:absolute;
            top:0; left:0;
           width:1px; height:1px;
           visibility:hidden;
         }
    </style>
    
    <!--  required imports -->
    <ur:js src="page-resources/yui/utilities/utilities.js"/>
    <ur:js src="page-resources/yui/button/button-min.js"/>
    <ur:js src="page-resources/yui/container/container-min.js"/>
    <ur:js src="page-resources/yui/history/history-min.js"/>
    <ur:js src="page-resources/yui/menu/menu-min.js"/>
    <ur:js src="page-resources/yui/tabview/tabview-min.js"/>
    
 	<!--  base path information -->
 	<ur:js src="page-resources/js/util/base_path.jsp"/>
 	<ur:js src="page-resources/js/util/ur_util.js"/>
    <ur:js src="page-resources/js/util/wait_dialog.js" />
 	<ur:js src="page-resources/js/menu/main_menu.js"/>
    <ur:js src="page-resources/js/util/ur_table.js"/>
    <ur:js src="page-resources/js/user/workspace.js"/>
    <ur:js src="page-resources/js/user/workspace_shared_file_inbox.js"/>
    <ur:js src="page-resources/js/user/workspace_collection.js"/>
    <ur:js src="page-resources/js/user/workspace_folder.js"/>
    <ur:js src="page-resources/js/user/workspace_search.js"/>
    <ur:js src="page-resources/js/user/group_workspace.js"/>
 
</head>


<body id="body" class="yui-skin-sam">

    
    <iframe id="yui-history-iframe" src="page-resources/yui/assets/blank.html"></iframe>
    <input id="yui-history-field" type="hidden">
    
    <script type="text/javascript">
        try
        {
            YAHOO.util.History.initialize("yui-history-field", "yui-history-iframe"); 
        }
        catch(e)
        {
        	 // The only exception that gets thrown here is when the browser is
            // not supported (Opera, or not A-grade) Degrade gracefully.
            // Note that we have two options here to degrade gracefully:
            //   1) Call initializeNavigationBar. The page will use Ajax/DHTML,
            //      but the back/forward buttons will not work.
            //   2) Initialize our module. The page will not use Ajax/DHTML,
            //      but the back/forward buttons will work. This is what we
            //      chose to do here:


            // do nothing
        }
    </script>

    
    <input type="hidden" id="set_tab_name" name="set_tab_name"  value="${tabName}"/> 
    
    <!--  yahoo doc 2 template creates a page 950 pixles wide -->
    <div id="doc2">  
 
        <!--  this is the header of the page -->
        <c:import url="/inc/header.jsp"/>
          
  
        <!--  this is the body regin of the page -->
        <div id="bd">
            <div class="workspace_head">
                <div class="workspace_head_float_left"><h3>Workspace: ${user.firstName}&nbsp;${user.lastName}&nbsp;(<a href="<c:url value="/user/viewResearcher.action"/>">Edit Researcher Page</a>) </h3></div>
                <div class="workspace_head_float_right">Last Login:&nbsp;<fmt:formatDate type="both" dateStyle="full" timeStyle="full" value="${user.lastLoginDate}" /> </div>
            </div>
	        <!--  set up tabs for the workspace -->
	        <div id="workspace-tabs" class="yui-navset">
	                 <ul class="yui-nav">
                         <li class="selected"><a href="#tab1"><em><u>My Files</u></em></a></li>
                         <li><a href="#tab2"><em><u>Group Workspaces</u></em></a></li>
                         <li><a href="#tab3"><em><u>My Publications</u></em></a></li>
                         <li><a href="#tab4"><em><u>Search My Workspace</u></em></a></li>
                         <li><a href="#tab5"><em><u>Shared File Inbox <strong>(<span id="inbox_files_count">&nbsp;&nbsp;&nbsp;</span>)</strong></u></em></a></li>
                     </ul>

                    <!--  first tab -->
                    <div class="yui-content">
                        <div id="tab1">
	                    <!--  table of files and folders -->
	                    <div id="newPersonalFolders" >
	                          <form  id="folders" name="myFolders">
	                              <input type="hidden" id="myFolders_parentFolderId" 
	                                   name="parentFolderId" 
	                                   value="${parentFolderId}"/>
	                          </form>
	                      </div>
	                    <!--  end personal files and folders div -->
	                  </div>
	                  <!--  end first tab -->
	                  
	                  <!--  group workspaces -->
	                  <div id="tab2">
	                      <form id="groupWorkspaceForm">
	                          <input type="hidden" id="groupWorkspaceFormGroupWorkspaceId" name="groupWorkspaceId" value="${groupWorkspaceId}"/>
	                          <input type="hidden" id="groupWorkspaceFormGroupWorkspaceFolderId" name="groupWorkspaceFolderId" value="${groupWorkspaceFolderId}"/>
	                      </form>
	                      <div id="group_workspaces" class="hidden">
	                           
	                      </div>
	                  </div>
	                  <!-- end group workspaces -->
	                  
	                  <!--  start third tab -->
	                  <div id="tab3">
	                      <div id="newPersonalCollections" class="hidden">
	                          <form id="collections" name="myCollections">
	                              <input type="hidden" id="myCollections_parentCollectionId" 
	                                   name="parentCollectionId" 
	                                   value="${parentCollectionId}"/>
	                          </form>
	                      </div>
	                 </div>                
	                 <!--  end third tab -->
	                 
	                 <!--  Start forth tab -->
	                 <div id="tab4">
	                     <div id="workspace_search" class="hidden">
	                         <form id="userSearchForm" name="userSearchForm" 
	                             action="javascript:YAHOO.ur.workspace.search.executeUserSearch();">
	                         
	                             <table class="formTable">
	                                 <tr>
	                                     <td class="label">
	                                         Search:
	                                     </td>
	                                     <td class="input">
	                                         <input type="text" name="query" size="50"
	                                             value="${query}"/>
	                                     </td>
	                                     <td>
	                                         <button
	                                          class="ur_button" 
	                                          onmouseover="this.className='ur_buttonover';"
 		                                      onmouseout="this.className='ur_button';"
 		                                      onclick="javascript:YAHOO.ur.workspace.search.executeUserSearch();"
	                                          id="userSearchButton"><span class="magnifierBtnImg">&nbsp;</span>Search</button>
	                                      </td>
	                                 
	                                 </tr>
	                             </table>
	                         </form>
	                     
	                         <br/>
	                         <!--  location where search results will be placed -->
	                         <div id="search_results">
	                         </div>
	                     </div>
	                 </div>
	                 <!--  End fourth tab -->
	                 
	                 <!--  Start 5th -->
	                 <div id="tab5">
	                     <div id="inbox_tab" class="hidden">
	                         <br/>
	                    
	                            <button class="ur_button"
	                                    onmouseover="this.className='ur_buttonover';"
 		                                onmouseout="this.className='ur_button';"
 		                                onclick="javascript:YAHOO.ur.shared.file.inbox.moveSharedFiles();"> <span class="pageWhiteGoBtnImg">&nbsp;</span>Move To My Files</button>
	                           
	                            <button class="ur_button"
	                                    onmouseover="this.className='ur_buttonover';"
 		                                onmouseout="this.className='ur_button';"
 		                                onclick="javascript:YAHOO.ur.shared.file.inbox.deleteInboxFileConfirmDialog.showDialog();"> <span class="deleteBtnImg">&nbsp;</span>Delete</button>
	                     
	                         <br/>
	                         <br/>
	         
	                         <!--  location where shared inbox content will be placed -->
	                         <div id="shared_folder_inbox">
	                         </div>
	                     </div>
	                 </div>
	                 <!--  End 5th tab tab -->
	                 
	             </div>
	             <!--  end content div -->

	       </div>
	         <!--  end all-images tabs -->
	  </div>
	  <!--  end body tag -->
	  
	  <!--  this is the footer of the page -->
      <c:import url="/inc/footer.jsp"/>
      
    </div>
    <!--  end doc tag -->
		        
	<!--  personal folder dialog -->      
	<div id="newFolderDialog" class="hidden">
	    <div class="hd">Folder Information</div>
		<div class="bd">
		    <form id="addFolder" name="newFolderForm" 
		        method="post" action="user/addPersonalFolder.action">
		              
		        <input type="hidden" id="newFolderForm_parentFolderId"
		               name="parentFolderId" value="${parentFolderId}"/>

              	<div id="newFolderDialogFields">
              	    <c:import url="personal_folder_form.jsp"/>
              	</div>

		     </form>
		 </div>
		 <!-- end dialog body -->
	 </div>
	 <!--  end the new folder dialog -->
	 
	<!--  group folder dialog -->      
	<div id="groupFolderDialog" class="hidden">
	    <div class="hd">Group Folder Information</div>
		<div class="bd">
		    <form id="groupFolderForm" name="groupFolderForm" 
		        method="post" action="user/addGroupFolder.action">
		        
		         <input type="hidden" id="groupFolderForm_parentFolderId"
		               name="parentFolderId" value="${parentFolderId}"/>
		               
              	<div id="groupWorkspaceFolderDialogFields">
              	    <c:import url="group_workspace/group_workspace_folder_form.jsp"/>
              	</div>
		     </form>
		 </div>
		 <!-- end dialog body -->
	 </div>
	 <!--  end group folder dialog -->
	 
	          
	<!--  new collection dialog -->
	<div id="newCollectionDialog" class="hidden">
	    <div class="hd">Please enter folder name</div>
		<div class="bd">
		    <form name="newCollectionForm" method="post">
		                  
		    <input type="hidden" id="newCollectionForm_parentCollectionId" 
		        name="parentCollectionId" value="${parentCollectionId}"/>

              	<div id="newCollectionDialogFields">
              	    <c:import url="personal_collection_form.jsp"/>
              	</div>
              			              
		    </form>
		</div>
	</div>
	<!--  end the collection dialog -->  
	          
	<!--  new publication dialog -->
	<div id="newItemDialog" class="hidden">
	    <div class="hd">New Publication</div>
		<div class="bd">
		 	<div id="file_ownership_error_div" class="errorMessage"></div>
            <form name="newItemForm" method="post">
                <input type="hidden" id="newItemForm_parentCollectionId" name="parentCollectionId" />
		        <table class="formTable">
				    <tr>
				        <td>Articles(A, An, The, ...)</td>
				        <td>Title</td>
				    </tr>
				    <tr>                                           
				        <td><input type="text" class="input" name="itemArticles" size="15" id ="newItemForm_itemArticles" value="${itemArticles}"/></td>
				        <td><input type="text" class="input" name="itemName" size="60" id ="newItemForm_itemName" value="${itemName}"/></td>
				    </tr>
		        </table>
		    </form>
		</div>
	</div>
	<!--  end the publication dialog -->  
	      
	<!-- Dialog box for uploading a file for personal workspace -->
    <div id="singleFileUploadDialog" class="hidden">
	    <div class="hd">File Upload</div>
		<div class="bd">
		    <form id="singleFileUploadForm" name="singleFileUploadForm" 
		                 method="post" enctype="multipart/form-data"
		                 action="user/singleFileUpload.action">
		              
		         <input type="hidden" id="file_upload_parent_folder_id" 
		                     name="folderId" value="${parentFolderId}"/>
		         <div id="upload_form_fields">
		                     <c:import url="single_file_upload_frag.jsp"/>
		         </div>
		    </form>
		</div>
	</div>
	<!--  end file upload dialog for personal workspace -->   
	
	<!-- Dialog box for uploading a file for group workspace -->
    <div id="groupWorkspaceSingleFileUploadDialog" class="hidden">
	    <div class="hd">File Upload</div>
		<div class="bd">
		    <form id="groupWorkspaceSingleFileUploadForm" name="groupWorkspaceSingleFileUploadForm" 
		                 method="post" enctype="multipart/form-data"
		                 action="user/singleFileGroupWorkspaceUpload.action">
		              
		         <input type="hidden" id="file_upload_group_workspace_parent_folder_id" 
		                     name="folderId" value="${parentFolderId}"/>

		         <input type="hidden" id="file_upload_group_workspace_group_id" 
		                     name="groupWorkspaceId" value="${groupWorkspaceId}"/>
		                     
		         <div id="group_workspace_upload_form_fields">
		              <c:import url="group_workspace/group_workspace_single_file_upload_frag.jsp"/>
		         </div>
		    </form>
		</div>
	</div>
	<!--  end file upload dialog for personal workspace -->
	       
	       
    <!--  wait div -->
	<div id="wait_dialog_box" class="hidden">
	    <div class="hd">Processing...</div>
		<div class="bd">
		    <c:url var="wait" value="/page-resources/images/all-images/ajax-loader.gif"/>
		    <p><img src="${wait}"></img></p>
		</div>
	</div>       
	       
	          
	<!--  delete files folder dialog -->
	<div id="deleteFileFolderConfirmDialog" class="hidden">
	    <div class="hd">Delete?</div>
		<div class="bd">
		    <p>Do you want to delete the selected files and folders?</p>
		</div>
	</div>
	<!--  end delete files and folder dialog -->
	
    <!--  delete group workspace files folder dialog -->
	<div id="deleteGroupWorkspaceFileFolderConfirmDialog" class="hidden">
	    <div class="hd">Delete?</div>
		<div class="bd">
		    <p>Do you want to delete the selected files and folders?</p>
		</div>
	</div>
	<!--  end delete files and folder dialog -->

	<!--  delete inbox file dialog -->
	<div id="deleteInboxFileConfirmDialog" class="hidden">
	    <div class="hd">Delete?</div>
		<div class="bd">
		    <p>Do you want to delete the selected files?</p>
		</div>
	</div>
	<!--  end delete inbox file dialog -->
	          
	<!-- Dialog box for uploading a file -->
    <div id="versionedFileUploadDialog" class="hidden">
	    <div class="hd">New Version Upload</div>
		<div class="bd">
		           
		<form id="versionedFileUploadForm" name="versionedFileUploadForm" 
		    method="post" enctype="multipart/form-data"
		    action="user/singleFileUpload.action">
		                 
		    <div id="version_upload_form_fields">
		        <c:import url="upload_new_file_version.jsp"/>
		    </div>
		</form>
		</div>
	</div>
	<!--  end file upload dialog -->  
	
	<!-- Dialog box for uploading a file -->
    <div id="groupWorkspaceVersionedFileUploadDialog" class="hidden">
	    <div class="hd">New Version Upload</div>
		<div class="bd">
		           
		<form id="groupWorkspaceVersionedFileUploadForm" name="groupWorkspaceVersionedFileUploadForm" 
		    method="post" enctype="multipart/form-data"
		    action="user/uploadNewGroupFileVersion.action">
		                 
		    <div id="group_workspace_version_upload_form_fields">
		        <c:import url="group_workspace/upload_new_file_version.jsp"/>
		    </div>
		</form>
		</div>
	</div>
	<!--  end file upload dialog -->  
	          
	<div id="error_dialog_box" class="hidden">
	    <div class="hd">Error</div>
		<div class="bd">
		    <div id="default_error_dialog_content">
		    </div>
		 </div>
	 </div>
	            
	 <!--  invite confirmation dialog -->
	 <div id="inviteConfirmDialog" class="hidden">
	     <div class="hd">Invite?</div>
		 <div class="bd">

 		     <form id="invite_files_form" name="inviteFilesForm" 
		         method="post" 
		        action="user/viewInviteUser.action">		          
			    <div id="invite_form_fields">
			        <c:import url="invite_files_confirmation.jsp"/>
			    </div>
		    </form>
		              
         </div>
	 </div>
	 <!--  end invite confirmation dialog -->

	 <!--  new publication version dialog -->
	 <div id="newPublicationVersionConfirmation" class="hidden">
	     <div class="hd">Create new version?</div>
		 <div class="bd">
		     <form id="new_version_form" name="newVersionForm" 
		         method="post">		
		         
		         <input type="hidden" name="personalItemId"/>
		         <input type="hidden" name="parentCollectionId"/>
		                   
		     </form>		          
		     <p>This publication version is published to Institutional Collection. 
		        To publish to more collections choose 'Submit publication' link.
		        <div class="clear">&nbsp;</div>
		        If you want to edit this publication, a new version will be created.
		        Do you want to create new version for editing?
		     </p>
		 </div>
	 </div>
	 <!--  end new publication version dialog -->

	 <!--  Rename file dialog -->
	 <div id="renameFileDialog" class="hidden">
	     <div class="hd">Rename file</div>
		 <div class="bd">
		     <form id="rename_form" name="renameForm" 
		         method="post">		
		         <p align="left">
	              	<div id="renameFileDialogFields">
	              	    <c:import url="rename_file_form.jsp"/>
	              	</div>		             
		         </p>
		     </form>		          
		 </div>
	 </div>
	 <!--  end rename file dialog -->
	 
	 <!--  Rename file dialog -->
	 <div id="renameGroupWorkspaceFileDialog" class="hidden">
	     <div class="hd">Rename Group Workspace file</div>
		 <div class="bd">
		     <form id="rename_group_workspace_file_form" name="renameGroupWokspaceFileForm" 
		         method="post">		
		         <p align="left">
	              	<div id="renameGroupWorkspaceFileDialogFields">
	              	    <c:import url="group_workspace/rename_group_workspace_file_form.jsp"/>
	              	</div>		             
		         </p>
		     </form>		          
		 </div>
	 </div>
	 <!--  end rename file dialog -->
	 
	 <!-- Div to render the delete collection -->
	 <div id="deleteCollectionDiv" class="hidden">
	     <div class="hd">Delete?</div>
		 <div class="bd">
		     Do you want to delete the selected publications and folders?
		 </div>
	 </div>
	 
	 <!-- Dialog box for creating a group workspace -->
     <div id="newGroupWorkspaceDialog" class="hidden">
       <div class="hd">Group Workspace Information</div>
       <div class="bd">
          <form id="addGroupWorkspace" 
                            name="newGroupWorkspaceForm" 
		                    method="post"
		                    action="/user/createGroupWorkspace.action">
	          <div id="groupWorkspaceDialogFields">
	              <c:import url="/pages/admin/groupspace/group_workspace_form.jsp"/>
	          </div>
	      </form>
       </div>
     </div>
	 <!--  end file dialog for creating a group workspace -->  
	 
	 <!-- Dialog box for deleting a group workspace -->
	 <div id="deleteGroupWorkspaceDialog" class="hidden">
      <div class="hd">Delete Group Workspace</div>
		<div class="bd">
		    <form id="deleteGroupWorkspaceForm" name="deleteGroupWorkspace" method="post" 
		                action="/user/deleteGroupWorkspace.action">
			   <p>Are you sure you wish to delete the selected group workspace?</p>
			   <input type="hidden" id="groupWorkspaceDeleteId" name="groupWorkspaceId" value=""/>
			   <input type="hidden"  name="showOnlyMyGroupWorkspaces"  value="false"/>
		    </form>
		</div>
     </div>
	 <!-- Dialog box for deleting a group workspace -->
	 
	 

</body>
</html>