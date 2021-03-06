/**  
   Copyright 2008 - 2011 University of Rochester

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


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceDAO;
import edu.ur.order.OrderType;

/**
 * Hibernate DAO implementation for group spaces.
 * 
 * @author Nathan Sarr
 *
 */
public class HbGroupWorkspaceDAO implements GroupWorkspaceDAO
{

	/** eclipse generated id*/
	private static final long serialVersionUID = -7945008201300712513L;
	
	/** hibernate helper  */
	private final HbCrudDAO<GroupWorkspace> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbGroupWorkspaceDAO() {
		hbCrudDAO = new HbCrudDAO<GroupWorkspace>(GroupWorkspace.class);
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
	 * Get the group space by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public GroupWorkspace getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Save the group to persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(GroupWorkspace entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Remove the group space from persistent storage.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(GroupWorkspace entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Get the count for the groups.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceCount");
		return (Long)q.uniqueResult();
	}

	/**
	 * Get the group space by it's unique name.
	 * 
	 * @see edu.ur.dao.UniqueNameDAO#findByUniqueName(java.lang.String)
	 */
	public GroupWorkspace findByUniqueName(String name) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getGroupWorkspaceByName");
		q.setParameter("lowerCaseName", name.toLowerCase());
		return (GroupWorkspace)q.uniqueResult();
	}
	
	/**
	 * Get the list of group spaces ordered by name.
	 * 
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param orderType - Order (Desc/Asc) 
	 * 
	 * @return list of group spaces found.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspace> getGroupWorkspacesNameOrder(int rowStart, int numberOfResultsToShow, OrderType orderType)
	{	
	    Query q = null;
	    Session session = hbCrudDAO.getSessionFactory().getCurrentSession();
	    if(orderType.equals(OrderType.ASCENDING_ORDER))
	    {
	        q = session.getNamedQuery("groupWorkspaceByNameAsc");
	    } 
	    else
	    {
	        q = session.getNamedQuery("groupWorkspaceByNameDesc");
	    } 
			    
	    q.setFirstResult(rowStart);
	    q.setMaxResults(numberOfResultsToShow);
		q.setFetchSize(numberOfResultsToShow);
	    return q.list();
	}
	
	/**
	 * Get all group workspaces for a given user - this includes groups they own or belong to a group within the workspace.
	 * 
	 * @param userId - id of the user to get the group workspaces for
	 * 
	 * @return - list of all groups the user belongs to.
	 */
	@SuppressWarnings("unchecked")
	public List<GroupWorkspace> getGroupWorkspacesForUser(Long userId, int rowStart, int numberOfResultsToShow, OrderType orderType)
	{
	    Session session = hbCrudDAO.getSessionFactory() .getCurrentSession();
	    Query q = null;
	    if(orderType.equals(OrderType.ASCENDING_ORDER))
		{
		    q = session.getNamedQuery("groupWorkspaceByUserIdNameAsc");
		} 
		else
		{
		    q = session.getNamedQuery("groupWorkspaceByUserIdNameDesc");
		} 	    	
	    	
	    q.setParameter("userId", userId);
	    q.setFirstResult(rowStart);
	    q.setMaxResults(numberOfResultsToShow);
		q.setFetchSize(numberOfResultsToShow);
	    return q.list();
	}

	
	/**
	 * Get the count of group workspaces for the given user id.
	 */
	public Long getCount(Long userId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("groupWorkspaceCountByUserId");
		q.setParameter("userId", userId);
		return (Long)q.uniqueResult();
	}


}
