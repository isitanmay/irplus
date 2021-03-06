/*
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
*/

/**
 * This code is for dealing with adding and removing 
 * institutional collections
 */
YAHOO.namespace("ur.institution");

var getInstitutionalCollectionsAction = basePath + 'admin/getInstitutionalCollections.action';

// action to perform when submitting the personal collectino form.
var viewInstitutionalCollectionAction = basePath + 'admin/viewInstitutionalCollection.action';

// action to perform when submitting the personal collectino form.
var deleteInstiutionalCollectionsAction = basePath + 'admin/deleteInstitutionalCollections.action';

// create a new institutional collection
var newInstitutionalCollectionAction = basePath + 'admin/createInstitutionalCollection.action';


YAHOO.ur.institution = {

    /** select all checkboxes for collections */
    setCheckboxes: function()
    {
        checked = document.institutionalCollections.checkAllSetting.checked;
          
        var collectionIds = document.getElementsByName('collectionIds');
        urUtil.setCheckboxes(collectionIds, checked);
     
        var itemIds = document.getElementsByName('itemIds');
        urUtil.setCheckboxes(itemIds, checked);
    },
    
    /**
     * clear out any form data messages or input
     * in the new collection form
     */
    clearCollectionForm: function()
    {
        var div = document.getElementById('collectionNameError');
        div.innerHTML = "";
	    document.newCollectionForm.collectionName.value = "";
    }, 
    
    
    /**
     * This creates a hidden field appends it to the form for
     * adding new sub collections for a given parent collections id.
     */ 
    insertHiddenParentCollectionId: function()
    {
        var value = document.getElementById('institutionalCollections_parentCollectionId').value
    
        // create the input tag
        var formElement = document.institutionalCollections;
        var parentInput = document.getElementById('newCollectionForm_parentCollectionId');
	    parentInput.value = value;
    },
    
    /**
     * Creates a YUI new folder modal dialog for when a user wants to create 
     * a new collection
     */
    createNewCollectionDialog : function()
    {
    
	    // Define various event handlers for Dialog
	    var handleSubmit = function() 
	    {
	        // save the current page
	 	    this.submit();
	    };
	
	    // handle a cancel of the adding collection dialog
	    var handleCancel = function() {
	        YAHOO.ur.institution.clearCollectionForm();
	        YAHOO.ur.institution.newCollectionDialog.hide();
	    };
	
	    var handleSuccess = function(o) 
	    {
	    	// check for the timeout - forward user to login page if timout
	        // occured
	        if( !urUtil.checkTimeOut(o.responseText) )
	        {
	            //get the response from adding a collection
	            var response = eval("("+o.responseText+")");
	  
	            //if the collection was not added then show the user the error message.
	            // received from the server
	            if( response.collectionAdded == "false" )
	            {
	                var collectionNameError = document.getElementById('collectionNameError');
                    collectionNameError.innerHTML = '<p id="newCollectionForm_nameError">' + response.collectionMessage + '</p>';
                    YAHOO.ur.institution.newCollectionDialog.showDialog();
	            }
	            else
	            {
	                //hide the dialog
	                YAHOO.ur.institution.newCollectionDialog.hide();
	                // we can clear the form if the collection was added
	                YAHOO.ur.institution.clearCollectionForm();
	        
	                //forward to edit collection action
	                window.location = viewInstitutionalCollectionAction + '?collectionId=' +response.collectionId ;
	            }
	        }
	    };
	
	
	    var handleFailure = function(o) {
	        alert("New collection submission failed  " + o.status + " status text " + o.statusText);
	    };

	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new folder button is clicked.
	    YAHOO.ur.institution.newCollectionDialog = new YAHOO.widget.Dialog("newCollectionDialog", 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      close: true,
		      buttons : [ { text:"Submit", handler:handleSubmit, isDefault:true },
					      { text:"Cancel", handler:handleCancel } ]
		    } );
	

        // override the submit function
        YAHOO.ur.institution.newCollectionDialog.submit = function()
        {
            if( YAHOO.ur.institution.newCollectionDialog.validate() )
            {
                YAHOO.util.Connect.setForm('newCollectionForm');
                var cObj = YAHOO.util.Connect.asyncRequest('post', 
                    newInstitutionalCollectionAction, callback);
           
            }
        };
        
	    // Validate the entries in the form to require that both first and last name are entered
	    YAHOO.ur.institution.newCollectionDialog.validate = function() {
	        var data = this.getData();
		    if (data.collectionName == "" ) {
		        alert("A collection name must be entered");
			    return false;
		    } else {
			    return true;
		    }
	    };
	    
	    // show the dialog
	    YAHOO.ur.institution.newCollectionDialog.showDialog = function()
	    {
	        YAHOO.ur.institution.newCollectionDialog.center();
            YAHOO.ur.institution.newCollectionDialog.show();
	    }

	    // Wire up the success and failure handlers
	    var callback = { success: handleSuccess, failure: handleFailure };
											 				
	    // Render the Dialog
	    YAHOO.ur.institution.newCollectionDialog.render();
    },
 
    
   /**
     * Allow collections and items to be moved
     */
    moveCollectionData : function()
    {
        var viewMoveCollectionsAction = basePath + 'admin/viewMoveInstitutionalCollectionLocations.action';
        document.institutionalCollections.action = viewMoveCollectionsAction;
        document.institutionalCollections.submit();
    },

    /** create a dialog to confirm the deletion of institutional collections 
      * and items
      */
    createCollectionDeleteConfirmDialog : function() 
    {

        // Define various event handlers for Dialog
	    var handleYes = function() {
	        document.institutionalCollections.action = deleteInstiutionalCollectionsAction;
	        document.institutionalCollections.submit();
		    this.hide();
	    };
	
	    var handleNo = function() {
		    this.hide();
	    };
	
	    // Instantiate the Dialog
	    // make it modal - 
	    // it should not start out as visible - it should not be shown until 
	    // new folder button is clicked.
	    YAHOO.ur.institution.deleteCollectionDialog = new YAHOO.widget.Dialog("deleteCollectionDialog", 
            { width : "500px",
		      visible : false, 
		      modal : true,
		      close: true,
		      buttons: [ { text:"Yes", handler:handleYes, isDefault:true },
	                     { text:"No",  handler:handleNo } ]
		    } );
		    
		// show and center the dialog
        YAHOO.ur.institution.deleteCollectionDialog.showDialog = function()
        {
            YAHOO.ur.institution.deleteCollectionDialog.center();
	        YAHOO.ur.institution.deleteCollectionDialog.show();
        }
	    
	    // Render the Dialog
	    YAHOO.ur.institution.deleteCollectionDialog.render();
    },

    /** create an error dialog */
    createErrorDialog : function() 
    {
        // Define various event handlers for Dialog
	    var handleYes = function() {
		    var contentArea = document.getElementById('default_error_dialog_content');
	        contentArea.innerHTML = ""; 
	        this.hide();
	    };
	

	    // Instantiate the Dialog
	    YAHOO.ur.institution.errorDialog = 
	        new YAHOO.widget.Dialog("error_dialog_box", 
			    { width: "600px",
				  visible: false,
				  modal: true,
				  close: false,										   
				  buttons: [ { text:"Ok", handler:handleYes, isDefault:true } ]
			    } );
	
	    YAHOO.ur.institution.errorDialog.setHeader("Error");
	

	    // show and center the dialog
        YAHOO.ur.institution.errorDialog.showDialog = function()
        {
            YAHOO.ur.institution.errorDialog.center();
	        YAHOO.ur.institution.errorDialog.show();
        }
    
	    // Render the Dialog
	    YAHOO.ur.institution.errorDialog.render();
    },
    
    /**
     *  Handle a user selecting the browse 
     */
    handleBrowseClick : function(e) {  
    	var parentId = document.getElementById('newCollectionForm_parentCollectionId').value;
        window.location = getInstitutionalCollectionsAction + '?parentCollectionId='+parentId;
    },

    

    /** initialize the page
        this is called once the dom has been created */    
    init : function() {
        var parentCollectionId = document.getElementById('institutionalCollections_parentCollectionId').value
        YAHOO.ur.institution.createNewCollectionDialog();
        YAHOO.ur.institution.createCollectionDeleteConfirmDialog();
        YAHOO.ur.institution.createErrorDialog();
        var myTabs = new YAHOO.widget.TabView("collection-tabs");
        var tab0 = myTabs.getTab(0);
        tab0.addListener('click',YAHOO.ur.institution.handleBrowseClick);
        
    } 
};

// initialize the code once the dom is ready
YAHOO.util.Event.onDOMReady(YAHOO.ur.institution.init);
