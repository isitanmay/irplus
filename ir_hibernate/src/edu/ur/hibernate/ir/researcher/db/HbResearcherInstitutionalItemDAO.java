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

package edu.ur.hibernate.ir.researcher.db;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import edu.ur.hibernate.HbCrudDAO;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherInstitutionalItemDAO;

/**
 * Hibernate implementation of researcher institutional Item data access and storage.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbResearcherInstitutionalItemDAO implements ResearcherInstitutionalItemDAO {

	/** eclipse generated id */
	private static final long serialVersionUID = 1439886971641958964L;

	/**
	 * Helper for persisting information using hibernate. 
	 */
	private final HbCrudDAO<ResearcherInstitutionalItem> hbCrudDAO;

	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(HbResearcherInstitutionalItemDAO.class);

	
	/**
	 * Default Constructor
	 */
	public HbResearcherInstitutionalItemDAO() {
		hbCrudDAO = new HbCrudDAO<ResearcherInstitutionalItem>(ResearcherInstitutionalItem.class);
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
	 * Return ResearcherInstitutionalItem by id
	 */
	public ResearcherInstitutionalItem getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ResearcherInstitutionalItem entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ResearcherInstitutionalItem entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the root researcher institutional Items for given researcher
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getRootResearcherInstitutionalItems(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherInstitutionalItem> getRootResearcherInstitutionalItems(final Long researcherId)
	{
		log.debug("getRootResearcherInstitutionalItems::");
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
        criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
        criteria.add(Restrictions.isNull("parentFolder"));
        return criteria.list();
	}
    
	/**
	 * Get researcher institutional Items for specified researcher and specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getSubResearcherInstitutionalItems(Long, Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherInstitutionalItem> getSubResearcherInstitutionalItems(final Long researcherId, final Long parentCollectionId)
	{
		log.debug("getSubResearcherInstitutionalItems::");
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
        criteria.createCriteria("parentFolder").add(Restrictions.idEq(parentCollectionId));
        return criteria.list();
	}


	/**
	 * Find the specified items for the given researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getResearcherInstitutionalItems(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherInstitutionalItem> getResearcherInstitutionalItems(final Long researcherId, final List<Long> itemIds) {
		List<ResearcherInstitutionalItem> foundItems = new LinkedList<ResearcherInstitutionalItem>();
		if( itemIds.size() > 0 )
		{
			Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
			criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
            criteria.add(Restrictions.in("id", itemIds));
            foundItems =  criteria.list();
		}
		return foundItems;
	}

	/**
	 * Get a count of the researcher institutional Items containing this item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getResearcherInstitutionalItemCount(Long)
	 */
	public Long getResearcherInstitutionalItemCount(Long itemId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getResearcherInstitutionalItemCount");
		q.setLong("institutionalItemId", itemId);
		return (Long)q.uniqueResult();
	}
	/**
	 * Get a researcher institutional Items containing this item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherInstitutionalItemDAO#getResearcherInstitutionalItem(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherInstitutionalItem> getResearcherInstitutionalItem(Long itemId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getResearcherInstitutionalItem");
		q.setLong("institutionalItemId", itemId);
		return (List<ResearcherInstitutionalItem>) q.list();
	}

}
