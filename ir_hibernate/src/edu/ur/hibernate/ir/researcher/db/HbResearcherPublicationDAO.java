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
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherPublicationDAO;

/**
 * Hibernate implementation of researcher publication data access and storage.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class HbResearcherPublicationDAO implements ResearcherPublicationDAO{

	/** eclipse generated id  */
	private static final long serialVersionUID = -5481891957211563285L;

	/**  Helper for persisting information using hibernate.  */
	private final HbCrudDAO<ResearcherPublication> hbCrudDAO;

	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(HbResearcherPublicationDAO.class);

	
	/**
	 * Default Constructor
	 */
	public HbResearcherPublicationDAO() {
		hbCrudDAO = new HbCrudDAO<ResearcherPublication>(ResearcherPublication.class);
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
	 * Return ResearcherPublication by id
	 */
	public ResearcherPublication getById(Long id, boolean lock) {
		return hbCrudDAO.getById(id, lock);
	}

	/**
	 * 
	 * @see edu.ur.dao.CrudDAO#makePersistent(java.lang.Object)
	 */
	public void makePersistent(ResearcherPublication entity) {
		hbCrudDAO.makePersistent(entity);
	}

	/**
	 *  
	 * @see edu.ur.dao.CrudDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(ResearcherPublication entity) {
		hbCrudDAO.makeTransient(entity);
	}
	
	/**
	 * Get the root researcher publications for given researcher
	 * 
	 * @see edu.ur.ir.researcher.ResearcherPublicationDAO#getRootResearcherPublications(Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherPublication> getRootResearcherPublications(final Long researcherId)
	{
		log.debug("getRootResearcherPublications::");
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
        criteria.add(Restrictions.isNull("parentFolder"));
        return criteria.list();
	}
    
	/**
	 * Get researcher publications for specified researcher and specified parent folder
	 * 
	 * @see edu.ur.ir.researcher.ResearcherPublicationDAO#getSubResearcherPublications(Long, Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherPublication> getSubResearcherPublications(final Long researcherId, final Long parentCollectionId)
	{
		log.debug("getSubResearcherPublications::");
		Criteria criteria = hbCrudDAO.getSessionFactory().getCurrentSession().createCriteria(hbCrudDAO.getClazz());
		criteria.createCriteria("researcher").add(Restrictions.idEq(researcherId));
        criteria.createCriteria("parentFolder").add(Restrictions.idEq(parentCollectionId));
        return criteria.list();
	}


	/**
	 * Find the specified items for the given researcher.
	 * 
	 * @see edu.ur.ir.researcher.ResearcherPublicationDAO#getResearcherPublications(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<ResearcherPublication> getResearcherPublications(final Long researcherId, final List<Long> itemIds) {
		List<ResearcherPublication> foundItems = new LinkedList<ResearcherPublication>();
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
	 * Get a count of the researcher publications containing this item
	 * 
	 * @see edu.ur.ir.researcher.ResearcherPublicationDAO#getResearcherPublicationCount(Long)
	 */
	public Long getResearcherPublicationCount(Long itemId) {
		Query q = hbCrudDAO.getSessionFactory().getCurrentSession().getNamedQuery("getResearcherPublicationCount");
		q.setLong("itemId", itemId);
		return (Long)q.uniqueResult();
	}
}
