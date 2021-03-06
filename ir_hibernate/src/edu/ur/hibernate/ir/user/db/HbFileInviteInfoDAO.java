/**  
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

package edu.ur.hibernate.ir.user.db;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.user.FileInviteInfo;
import edu.ur.ir.user.FileInviteInfoDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * Persistence for invite information
 * 
 * @author Sharmila Ranganathan 
 *
 */
public class HbFileInviteInfoDAO implements FileInviteInfoDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 5758222725214280712L;
	
	/**  Helper for persisting information using hibernate.  */	
	private final HbCrudDAO<FileInviteInfo> hbCrudDAO;
	
	/**
	 * Default Constructor
	 */
	public HbFileInviteInfoDAO() {
		hbCrudDAO = new HbCrudDAO<FileInviteInfo>(FileInviteInfo.class);
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
	 * Returns all invitaion details for specified token.
	 * 
	 * @param token
	 */
	public FileInviteInfo findInviteInfoForToken(String token)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findInviteInfoForToken");
		q.setParameter("token", token);
		return (FileInviteInfo) q.uniqueResult();
	}

	/**
	 * Get the invite info by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public FileInviteInfo getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * Make the invite info persistent.
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(FileInviteInfo entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 * Make the invite info transient.
	 * 
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(FileInviteInfo entity) {
		hbCrudDAO.makeTransient(entity);
	}

	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	@SuppressWarnings("unchecked")
	public List<FileInviteInfo> getInviteInfoByEmail(String email) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("findInviteInfoForEmail");
		q.setParameter("email", email.trim().toLowerCase());
		return q.list();
	}
	
	
	/**
	 * Get the list of invite infos ordered by inviteor
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results to retrieve
	 * @param orderType - ascending/decending order
	 * 
	 * @return list of invitees found
	 */
	@SuppressWarnings("unchecked")
	public List<FileInviteInfo> getInviteInfosOrderByInviteor(int rowStart,
			int maxResults, OrderType orderType) {
		
		Query q = null;
		if (orderType.equals(OrderType.DESCENDING_ORDER)) {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getInviteInfosOrderByInvitorDesc");
		} else {
			q = hbCrudDAO
					.getSessionFactory()
					.getCurrentSession()
					.getNamedQuery(
							"getInviteInfosOrderByInvitorAsc");
		}

		q.setFirstResult(rowStart);
		q.setMaxResults(maxResults);
		q.setFetchSize(maxResults);
		return (List<FileInviteInfo>) q.list();
	}

	/**
	 * Get a count of invite infos in the system.
	 * 
	 * @see edu.ur.dao.CountableDAO#getCount()
	 */
	public Long getCount() {
		return (Long)hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("inviteInfoCount").uniqueResult();
	}
	
	/**
	 * Get the invites made by a particular user.
	 * 
	 * @param user - invites made by a given user
	 * @return - all invites made by the user or an empty list if no invites found
	 */
	@SuppressWarnings("unchecked")
	public List<FileInviteInfo> getInvitesMadeByUser(IrUser user)
	{
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getInviteInfoMadeByUser");
		q.setParameter("userId",user.getId());
		return q.list();
	}

}
