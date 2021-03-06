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

package edu.ur.hibernate.ir.groupspace.db;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceFolderDAO;
import edu.ur.ir.user.IrUser;

/**
 * Persistent storage for group folder data.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceFolderDAO implements GroupWorkspaceFolderDAO{

	/** eclipse generated id*/
	private static final long serialVersionUID = 7151402796946586912L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupWorkspaceFolder> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceFolderDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspaceFolder>(GroupWorkspaceFolder.class);
	}
	
	/**
	 * Set the session factory.  
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
    {
        hbCrudDAO.setSessionFactory(sessionFactory);
    }

	/**
	 * Get the group folder by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspaceFolder getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}
	
	/**
	 * Get the path to the folder.
	 * 
	 * @param parentFolderId - id of the parent folder
	 * 
	 * @return - list of all folders in order - parent to child
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getFolderPath(GroupWorkspaceFolder folder)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceFolderPath");
		q.setParameter("leftValue", folder.getLeftValue());
		q.setParameter("rootId",folder.getTreeRoot().getId());
		q.setParameter("userId", folder.getOwner().getId());
		return q.list();
	}
	
	/**
	 * Get sub folders within parent folder for a group workspace
	 * 
	 * @param workspaceId Id of the group workspace containing the folders
	 * @param parentFolderId Id of the parent folder to start at - can be at any point
	 * in the tree
	 * 
	 * @return List of sub folders within the parent folder
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getFolders(Long workspaceId, Long parentFolderId )
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getWorkspaceFolders");
		q.setParameter("workspaceId", workspaceId);
		q.setParameter("parentId", parentFolderId);
		return q.list();
	}
	
	/**
	 * Get all the folders for a group workspace 
	 * @param workspaceId - id of the group workspace.
	 * 
	 * @return list of group workspace folders
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getAllFolders(Long workspaceId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllWorkspaceFolders");
		q.setParameter("workspaceId", workspaceId);
		return q.list();
	}
	
	
	/**
	 * Get the root folders for a given workspace.
	 * 
	 * @param workspaceId - id of the workspace
	 * @return list of folders at the root of the given workspace.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getRootFolders(Long workspaceId)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRootWorkspaceFolders");
		q.setParameter("workspaceId", workspaceId);
		return q.list();
	}

	/**
	 * Make the group folder persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspaceFolder entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the group folder transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspaceFolder entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	
	/**
	 * This returns all group workspace files for the specified parent folder.  This
	 * includes all files in sub folders.
	 * 
	 * @param groupFolder - group workspace folder to find all files in (including sub folders)
	 * @return all group files found.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFile> getAllFilesForFolder(GroupWorkspaceFolder groupFolder) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllGroupWorkspaceFilesForFolder");
		q.setParameter("leftValue", groupFolder.getLeftValue());
		q.setParameter("rightValue", groupFolder.getRightValue());
		q.setParameter("rootId", groupFolder.getTreeRoot().getId());
		return (List<GroupWorkspaceFile>) q.list();
	}
	
	/**
	 * This returns all group workspace folders for the specified parent folder.  This
	 * includes all folders in sub folders.
	 * 
	 * @param groupFolder - group workspace folder to find all folders in (including sub folders)
	 * @return all group folders found.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getAllFoldersForFolder(GroupWorkspaceFolder groupFolder) {
		
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllGroupWorkspaceFoldersForFolder");
		q.setParameter("leftValue", groupFolder.getLeftValue());
		q.setParameter("rightValue", groupFolder.getRightValue());
		q.setParameter("rootId", groupFolder.getTreeRoot().getId());
		return (List<GroupWorkspaceFolder>) q.list();
	}

	/**
	 * Get all folders for the given user that has the specified permission on the folder.
	 * 
	 * @param user - user to check
	 * @param permission - permission to check
	 * 
	 * @return all folders for which the user has the specified permission.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getAllFoldersUserHasPermissionFor(
			IrUser user, String permission) {
		Query q1 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getAllFoldersUserHasPermissionFor");
		q1.setParameter("className", GroupWorkspaceFolder.class.getName());
		q1.setParameter("permissionName", permission);
		q1.setParameter("userId", user.getId());
		List<GroupWorkspaceFolder> folders = (List<GroupWorkspaceFolder>) q1.list();
		return folders;
	}

	/**
	 * Find the folder for the specified folder name and 
	 * group workspace folder id.
	 * 
	 * @param name of the folder
	 * @param parentId id of the parent folder
	 * @return the found folder or null if the folder is not found.
	 */
	public GroupWorkspaceFolder getFolder(String name, Long parentId) {
		Query q1 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceFolderByNameParent");
		q1.setParameter("name", name);
		q1.setParameter("parentId", parentId);
		return (GroupWorkspaceFolder) q1.uniqueResult();
	}

	/**
	 * Find the root folder for the specified folder name and 
	 * group workspace id.
	 * 
	 * @param name of the folder
	 * @param groupWorkspaceId - id of the group workspace
	 * @return the found root folder or null if the folder is not found.
	 */
	public GroupWorkspaceFolder getRootFolder(String name, Long groupWorkspaceId) {
		Query q1 = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getRootGroupWorkspaceFolderByName");
		q1.setParameter("name", name);
		q1.setParameter("groupWorkspaceId", groupWorkspaceId);
		return (GroupWorkspaceFolder) q1.uniqueResult();
	}

	/**
	 * Get the group workspace folders in the given list with the specified ids.  If the list
	 * of folderIds is empty, no folders are returned.
	 * 
	 * @param groupWorkspaceId - id of the group workspace
	 * @param folderIds - list of folder ids to retrieve
	 * 
	 * @return the found folders
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspaceFolder> getFolders(Long groupWorkspaceId,
			List<Long> folderIds) {
		
		if( folderIds.size() > 0 )
		{
		    Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceFolders");
		    q.setParameter("groupWorkspaceId", groupWorkspaceId);
		    q.setParameterList("folderIds", folderIds);
		    return q.list();
		}
		else
		{
			return new LinkedList<GroupWorkspaceFolder>();
		}
	}

}
