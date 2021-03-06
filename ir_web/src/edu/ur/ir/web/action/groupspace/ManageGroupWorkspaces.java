/**  
   Copyright 2008-2011 University of Rochester

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
package edu.ur.ir.web.action.groupspace;

import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.security.PermissionNotGrantedException;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Manage  group workspaces.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupWorkspaces extends Pager implements UserIdAware {

	/** eclipse generated id */
	private static final long serialVersionUID = 5292140116837950036L;
	
	/* Name for the content type */
	private String name;

	/* description for the content type */
	private String description;
	
	/* Logger */
	private static final Logger log = Logger.getLogger(ManageGroupWorkspaces.class);
	
	/* service for dealing with group space information */
	private GroupWorkspaceService groupWorkspaceService;

	/* id of the user trying to make changes */
	private Long userId;

	/* Service for dealing with group space information */
	private UserService userService;
	
	/* Group space object */
	private GroupWorkspace groupWorkspace;

	/* indicates successful action */
	private boolean success = true;
	
	/* list of groupspaces */
	private List<GroupWorkspace> groupWorkSpaces;

	/* id of the groupspace to edit */
	private Long id;

	/* Message that can be displayed to the user. */
	private String message;
	
	/* ACL service */
	private  SecurityService securityService;



	/**
	 * Default constructor
	 */
	public ManageGroupWorkspaces()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Initial load of all group spaces
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		groupWorkSpaces = groupWorkspaceService.getGroupWorkspacesNameOrder(rowStart, numberOfResultsToShow, OrderType.ASCENDING_ORDER);
		return SUCCESS;
	}
	
	/**
	 * Get a specific group space.
	 * 
	 * @return
	 */
	public String get()
	{
		groupWorkspace = groupWorkspaceService.get(id, false);
	    return "get";
	}
	
	/**
	 * Delete the specified group space.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("deleting a group space with id = " +  id);
		IrUser user = userService.getUser(userId, false);
		
		// get the group workspace
		groupWorkspace = groupWorkspaceService.get(id, false);
		
		// only owners and admins can delete  group workspaces
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		log.debug("workspace user = " + workspaceUser);
		
	    groupWorkspace = groupWorkspaceService.get(id,false);
	    try {
			groupWorkspaceService.delete(groupWorkspace, user);
		} catch (PermissionNotGrantedException e) {
			return "accessDenied";
		}
	    groupWorkSpaces = groupWorkspaceService.getGroupWorkspacesNameOrder(rowStart, numberOfResultsToShow, OrderType.ASCENDING_ORDER);

		return "deleted";
	}
	
	/**
	 * Create a new user group.
	 * 
	 * @return
	 */
	public String create()
	{
		log.debug("creating a group space with name = " + name);
		IrUser user = userService.getUser(userId, false);
		
		if( user == null || !user.hasRole(IrRole.ADMIN_ROLE)  )
		{
			return "accessDenied";
		}
		GroupWorkspace other = groupWorkspaceService.get(name);
		if( other == null )
		{
			if( !name.trim().equals("") )
			{
			    groupWorkspace = new GroupWorkspace(name, description);
		        groupWorkspaceService.save(groupWorkspace);
		        securityService.assignOwnerPermissions(groupWorkspace, user);
			}
			else
			{
				success = false;
				message = getText("groupSpaceNameError", 
				"Group workspace name cannot be empty");
				addFieldError("groupWorkspaceAlreadyExists", message);
			}
			
		} 
		else
		{
			success = false;
			message = getText("groupWorkspaceNameError", 
					new String[]{groupWorkspace.getName()});
			addFieldError("groupWorkspaceAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Update the group workspace.
	 * 
	 * @return success if the update is successful
	 * @throws DuplicateNameException - if a duplicate name error occurs
	 */
	public String update() throws DuplicateNameException
	{
		log.debug("updating group space  = " + name + " id = " + id);
		groupWorkspace = groupWorkspaceService.get(id, false);
		GroupWorkspace other = groupWorkspaceService.get(name);
		if( other == null )
		{
			if( !name.trim().equals("") )
			{
			    groupWorkspace.setName(name);
			    groupWorkspace.setDescription(description);
			    groupWorkspaceService.save(groupWorkspace);
			}
			else
			{
				success = false;
				message = getText("groupSpaceNameError", 
						"Group workspace name cannot be empty");
				    addFieldError("groupWorkspaceAlreadyExists", message);
			}
		}
		else
		{
			message = getText("groupSpaceNameError", 
					new String[]{groupWorkspace.getName()});
			    addFieldError("groupWorkspaceAlreadyExists", message);
			success = false;
		}
		
		return "get";

	}
	

	/**
	 * Get the total hits.
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return groupWorkspaceService.getCount().intValue();
	}

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Set the group space service
	 * 
	 * @param groupSpaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}

	/**
	 * Indicates a successful action
	 * 
	 * @return
	 */
	public boolean getSuccess() {
		return success;
	}

	/**
	 * Get a list of the groupspaces.
	 * 
	 * @return
	 */
	public List<GroupWorkspace> getGroupWorkspaces() 
	{
		return groupWorkSpaces;
	}
	
	/**
	 * Get the group space
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
		return groupWorkspace;
	}

	/**
	 * Set the id of the group space to load.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	
	/**
	 * Set the security service.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}
}
